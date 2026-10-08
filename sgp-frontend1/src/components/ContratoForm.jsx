// Importa Hooks de React
import { useState, useEffect } from "react";

// Importa el servicio de empleados (función correcta: getEmpleados)
import { getEmpleados } from "../services/EmpleadoService";

// Componente ContratoForm: formulario independiente para crear contratos
function ContratoForm({ onGuardar }) {
  // Estado para almacenar la lista de empleados (dropdown)
  const [empleados, setEmpleados] = useState([]);

  // Estado del contrato (datos que se envían al backend)
  const [contrato, setContrato] = useState({
    idEmpleado: "",
    tipoContrato: "",
    fechaInicio: "",
    fechaFin: "",
    salario: ""
  });

  // Al montar el componente, cargar empleados desde el backend
  useEffect(() => {
    getEmpleados()
      .then(response => setEmpleados(response.data)) // guarda empleados en el estado
      .catch(error => console.error("Error cargando empleados:", error));
  }, []);

  // Maneja cambios en los inputs del formulario
  const handleChange = (e) => {
    setContrato({ ...contrato, [e.target.name]: e.target.value });
  };

  // Envía el contrato al componente padre (Contrato.jsx)
  const handleSubmit = (e) => {
    e.preventDefault();
    onGuardar(contrato); // llama la función pasada como prop
    // Reinicia el formulario
    setContrato({
      idEmpleado: "",
      tipoContrato: "",
      fechaInicio: "",
      fechaFin: "",
      salario: ""
    });
  };

  return (
    <form onSubmit={handleSubmit} className="contrato-form">
      {/* Dropdown de empleados */}
      <label>Empleado</label>
      <select
        name="idEmpleado"
        value={contrato.idEmpleado}
        onChange={handleChange}
        required
      >
        <option value="">Seleccione un empleado</option>
        {empleados.map(emp => (
          <option key={emp.idEmpleado} value={emp.idEmpleado}>
            {emp.nombre} {emp.apellidos}
          </option>
        ))}
      </select>

      {/* Tipo de contrato */}
      <label>Tipo Contrato</label>
      <select
        name="tipoContrato"
        value={contrato.tipoContrato}
        onChange={handleChange}
        required
      >
        <option value="">Seleccione un tipo</option>
        <option value="FIJO">Fijo</option>
        <option value="TEMPORAL">Temporal</option>
        <option value="INDEFINIDO">Indefinido</option>
        <option value="PRACTICAS">Prácticas</option>
      </select>

      {/* Fechas */}
      <label>Fecha Inicio</label>
      <input
        type="date"
        name="fechaInicio"
        value={contrato.fechaInicio}
        onChange={handleChange}
        required
      />

      <label>Fecha Fin</label>
      <input
        type="date"
        name="fechaFin"
        value={contrato.fechaFin}
        onChange={handleChange}
      />

      {/* Salario */}
      <label>Salario</label>
      <input
        type="number"
        name="salario"
        value={contrato.salario}
        onChange={handleChange}
        required
      />

      {/* Botones */}
      <button type="submit">Guardar</button>
      <button
        type="button"
        onClick={() =>
          setContrato({
            idEmpleado: "",
            tipoContrato: "",
            fechaInicio: "",
            fechaFin: "",
            salario: ""
          })
        }
      >
        Limpiar
      </button>
    </form>
  );
}

export default ContratoForm;


