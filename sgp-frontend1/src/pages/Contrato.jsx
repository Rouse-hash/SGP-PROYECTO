// Importa Hooks de React
import { useState, useEffect } from "react";

// Importa servicios de contratos
import {
  getContratos,
  crearContrato,
  actualizarContrato,
  eliminarContrato
} from "../services/ContratoService";

// Importa servicio de empleados (función correcta: getEmpleados)
import { getEmpleados } from "../services/EmpleadoService";

// Importa tabla de contratos
import ContratoTable from "../components/ContratoTable";

// Importa estilos del módulo
import "../styles/Contrato.css";

// Importa utilidades de validación
import {
  camposObligatoriosVacios,
  esNumeroValido
} from "../utils/validaciones";

// Componente principal del módulo Contrato
function Contrato() {
  // Estado del formulario de contrato
  const [contrato, setContrato] = useState({
    idEmpleado: "",
    tipoContrato: "",
    fechaInicio: "",
    fechaFin: "",
    salario: ""
  });

  // Lista de empleados para el dropdown
  const [empleados, setEmpleados] = useState([]);

  // Estado para almacenar contratos en la tabla
  const [contratos, setContratos] = useState([]);

  // Controla si estamos creando o editando
  const [modoEdicion, setModoEdicion] = useState(false);

  // Controla si el formulario está en solo lectura (fila seleccionada)
  const [soloLectura, setSoloLectura] = useState(false);

  // Guarda el ID del contrato que se está editando
  const [idEditando, setIdEditando] = useState(null);

  // Consulta contratos desde Spring Boot
  const obtenerContratos = async () => {
    try {
      const respuesta = await getContratos();
      setContratos(respuesta.data);
    } catch (error) {
      console.error("Error al obtener contratos:", error);
    }
  };

  // Consulta empleados desde Spring Boot
  const obtenerEmpleados = async () => {
    try {
      const respuesta = await getEmpleados(); // ✅ corregido
      setEmpleados(respuesta.data);
    } catch (error) {
      console.error("Error al obtener empleados:", error);
    }
  };

  // Carga contratos y empleados al abrir el módulo
  useEffect(() => {
    const cargarDatos = async () => {
      await obtenerContratos();
      await obtenerEmpleados();
    };
    cargarDatos();
  }, []);

  // Actualiza el formulario cuando el usuario escribe
  const handleChange = (e) => {
    setContrato({
      ...contrato,
      [e.target.name]: e.target.value
    });
  };

  // Limpia formulario y cancela edición
  const limpiarFormulario = () => {
    setContrato({
      idEmpleado: "",
      tipoContrato: "",
      fechaInicio: "",
      fechaFin: "",
      salario: ""
    });

    setModoEdicion(false);
    setSoloLectura(false);
    setIdEditando(null);
  };

  // Muestra la información del contrato sin permitir modificarla
  const verContrato = (contratoSeleccionado) => {
    setContrato({
      idEmpleado: contratoSeleccionado.empleado?.idEmpleado || "",
      tipoContrato: contratoSeleccionado.tipoContrato,
      fechaInicio: contratoSeleccionado.fechaInicio,
      fechaFin: contratoSeleccionado.fechaFin,
      salario: contratoSeleccionado.salario
    });

    setIdEditando(contratoSeleccionado.idContrato);
    setModoEdicion(false);
    setSoloLectura(true);
  };

  // Carga un contrato seleccionado en el formulario y permite editarlo
  const editarContrato = (contratoSeleccionado) => {
    setContrato({
      idEmpleado: contratoSeleccionado.empleado?.idEmpleado || "",
      tipoContrato: contratoSeleccionado.tipoContrato,
      fechaInicio: contratoSeleccionado.fechaInicio,
      fechaFin: contratoSeleccionado.fechaFin,
      salario: contratoSeleccionado.salario
    });

    setIdEditando(contratoSeleccionado.idContrato);
    setModoEdicion(true);
    setSoloLectura(false);
  };

  // Guarda un nuevo contrato o actualiza uno existente
  const guardarContrato = async () => {
    if (!validarContrato()) return;

    try {
      if (modoEdicion) {
        const payload = {
          tipoContrato: contrato.tipoContrato,
          fechaInicio: contrato.fechaInicio,
          fechaFin: contrato.fechaFin,
          salario: contrato.salario,
          empleado: { idEmpleado: contrato.idEmpleado }
        };
        await actualizarContrato(idEditando, payload);
        console.log("Contrato actualizado correctamente");
      } else {
        await crearContrato(contrato.idEmpleado, contrato);
        console.log("Contrato guardado correctamente");
      }

      limpiarFormulario();
      obtenerContratos();
    } catch (error) {
      const mensaje =
        error.response?.data?.mensaje ||
        "Error al guardar contrato";
      alert(mensaje);
    }
  };

  // Valida el formulario antes de guardar
  const validarContrato = () => {
    const obligatorios = camposObligatoriosVacios(contrato, [
      "idEmpleado",
      "tipoContrato",
      "fechaInicio",
      "salario"
    ]);

    if (obligatorios.length > 0) {
      alert("Todos los campos son obligatorios, diligencie el formulario completo");
      return false;
    }

    if (!esNumeroValido(contrato.salario)) {
      alert("Caracteres no permitidos: el salario solo acepta números");
      return false;
    }

    if (Number(contrato.salario) <= 0) {
      alert("El salario debe ser mayor que cero");
      return false;
    }

    if (contrato.fechaFin && contrato.fechaFin < contrato.fechaInicio) {
      alert("La fecha de fin no puede ser anterior a la fecha de inicio");
      return false;
    }

    return true;
  };

  // Elimina un contrato
  const borrarContrato = async (idContrato) => {
    const confirmar = window.confirm(
      "¿Está seguro de eliminar este contrato?"
    );

    if (!confirmar) return;

    try {
      await eliminarContrato(idContrato);
      obtenerContratos();
      console.log("Contrato eliminado correctamente");
    } catch (error) {
      console.error("Error al eliminar contrato:", error);
    }
  };

  return (
    <section className="contrato-module">
      {/* Encabezado del módulo */}
      <div className="module-header">
        <div>
          <span className="module-tag">Módulo Contratos</span>
          <h2>Gestión de Contratos</h2>
          <p>Registra contratos asociados a empleados.</p>
        </div>
      </div>

      {/* Formulario */}
      <div className="card form-card">
        <h3>{modoEdicion ? "Editar Contrato" : "Formulario Contrato"}</h3>

        <form className="contrato-form">
          {/* Dropdown de empleados */}
          <div className="form-group">
            <label>Empleado</label>
            <select
              name="idEmpleado"
              value={contrato.idEmpleado}
              onChange={handleChange}
              disabled={soloLectura}
            >
              <option value="">Seleccione un empleado</option>
              {empleados.map((emp) => (
                <option key={emp.idEmpleado} value={emp.idEmpleado}>
                  {emp.nombre} {emp.apellidos}
                </option>
              ))}
            </select>
          </div>

          {/* Tipo Contrato */}
          <div className="form-group">
            <label>Tipo Contrato</label>
            <select
              name="tipoContrato"
              value={contrato.tipoContrato}
              onChange={handleChange}
              disabled={soloLectura}
            >
              <option value="">Seleccione un tipo</option>
              <option value="FIJO">Fijo</option>
              <option value="TEMPORAL">Temporal</option>
              <option value="INDEFINIDO">Indefinido</option>
              <option value="PRACTICAS">Prácticas</option>
            </select>
          </div>

          {/* Fecha Inicio */}
          <div className="form-group">
            <label>Fecha Inicio</label>
            <input
              type="date"
              name="fechaInicio"
              value={contrato.fechaInicio}
              onChange={handleChange}
              disabled={soloLectura}
            />
          </div>

          {/* Fecha Fin */}
          <div className="form-group">
            <label>Fecha Fin</label>
            <input
              type="date"
              name="fechaFin"
              value={contrato.fechaFin}
              onChange={handleChange}
              disabled={soloLectura}
            />
          </div>

          {/* Salario */}
          <div className="form-group">
            <label>Salario</label>
            <input
              type="number"
              name="salario"
              value={contrato.salario}
              onChange={handleChange}
              disabled={soloLectura}
              placeholder="Ingrese el salario"
            />
          </div>

          {/* Botones */}
          <div className="form-actions">
            <button
              type="button"
              className="btn-primary"
              onClick={guardarContrato}
              disabled={soloLectura}
            >
              {modoEdicion ? "Actualizar" : "Guardar"}
            </button>

            <button
              type="button"
              className="btn-secondary"
              onClick={limpiarFormulario}
            >
              Limpiar
            </button>
          </div>
        </form>
      </div>

      {/* Tabla */}
      <div className="card table-card">
        <div className="table-title">
          <h3>Contratos Registrados</h3>
          <p>Listado de contratos consultados desde Spring Boot.</p>
        </div>

        <ContratoTable
          contratos={contratos}
          onSeleccionar={verContrato}
          onEditar={editarContrato}
          onEliminar={borrarContrato}
        />
      </div>
    </section>
  );
}

export default Contrato;



