// Importa Hooks de React
import { useState, useEffect } from 'react'

// Importa servicios de empleados
import {
  getEmpleados,
  crearEmpleado,
  actualizarEmpleado,
  eliminarEmpleado
} from "../services/EmpleadoService";

// Importa tabla de empleados
import EmpleadoTable from '../components/EmpleadoTable'

// Importa estilos del módulo
import '../styles/Empleado.css'

// Importa utilidades de validación
import {
  camposObligatoriosVacios,
  soloNumeros
} from '../utils/validaciones'

// Componente principal del módulo Empleado
function Empleado() {

  // Estado del formulario
  const [empleado, setEmpleado] = useState({
    nombre: '',
    apellidos: '',
    tipoDocumento: '',
    numeroDocumento: '',
    fechaNacimiento: '',
    estadoCivil: ''
  })

  // Estado para almacenar empleados en la tabla
  const [empleados, setEmpleados] = useState([])

  // Controla si estamos creando o editando
  const [modoEdicion, setModoEdicion] = useState(false)

  // Controla si el formulario está en solo lectura (fila seleccionada)
  const [soloLectura, setSoloLectura] = useState(false)

  // Guarda el ID del empleado que se está editando
  const [idEditando, setIdEditando] = useState(null)

  // Consulta empleados desde Spring Boot
  const obtenerEmpleados = async () => {
    try {
      const respuesta = await getEmpleados()
      setEmpleados(respuesta.data)
    } catch (error) {
      console.error('Error al obtener empleados:', error)
    }
  }

  // Carga empleados al abrir el módulo
  useEffect(() => {
    const cargarDatos = async () => {
      await obtenerEmpleados()
    }
    cargarDatos()
  }, [])

  // Actualiza el formulario cuando el usuario escribe
  const handleChange = (e) => {
    setEmpleado({
      ...empleado,
      [e.target.name]: e.target.value
    })
  }

  // Limpia formulario y cancela edición
  const limpiarFormulario = () => {
    setEmpleado({
      nombre: '',
      apellidos: '',
      tipoDocumento: '',
      numeroDocumento: '',
      fechaNacimiento: '',
      estadoCivil: ''
    })

    setModoEdicion(false)
    setSoloLectura(false)
    setIdEditando(null)
  }

  // Muestra la información del empleado sin permitir modificarla
  const verEmpleado = (empleadoSeleccionado) => {
    setEmpleado({
      nombre: empleadoSeleccionado.nombre,
      apellidos: empleadoSeleccionado.apellidos,
      tipoDocumento: empleadoSeleccionado.tipoDocumento,
      numeroDocumento: empleadoSeleccionado.numeroDocumento,
      fechaNacimiento: empleadoSeleccionado.fechaNacimiento,
      estadoCivil: empleadoSeleccionado.estadoCivil
    })

    setIdEditando(empleadoSeleccionado.idEmpleado)
    setModoEdicion(false)
    setSoloLectura(true)
  }

  // Carga un empleado seleccionado en el formulario y permite editarlo
  const editarEmpleado = (empleadoSeleccionado) => {
    setEmpleado({
      nombre: empleadoSeleccionado.nombre,
      apellidos: empleadoSeleccionado.apellidos,
      tipoDocumento: empleadoSeleccionado.tipoDocumento,
      numeroDocumento: empleadoSeleccionado.numeroDocumento,
      fechaNacimiento: empleadoSeleccionado.fechaNacimiento,
      estadoCivil: empleadoSeleccionado.estadoCivil
    })

    setIdEditando(empleadoSeleccionado.idEmpleado)
    setModoEdicion(true)
    setSoloLectura(false)
  }

  // Guarda un nuevo empleado o actualiza uno existente
  const guardarEmpleado = async () => {
    if (!validarEmpleado()) return

    try {
      if (modoEdicion) {
        await actualizarEmpleado(idEditando, empleado)
        console.log('Empleado actualizado correctamente')
      } else {
        await crearEmpleado(empleado)
        console.log('Empleado guardado correctamente')
      }

      limpiarFormulario()
      obtenerEmpleados()

    } catch (error) {
      const mensaje =
        error.response?.data?.mensaje ||
        'Error al guardar empleado'
      alert(mensaje)
    }
  }

  // Valida el formulario antes de guardar
  const validarEmpleado = () => {
    const obligatorios = camposObligatoriosVacios(empleado, [
      'nombre',
      'apellidos',
      'tipoDocumento',
      'numeroDocumento',
      'fechaNacimiento',
      'estadoCivil'
    ])

    if (obligatorios.length > 0) {
      alert('Todos los campos son obligatorios, diligencie el formulario completo')
      return false
    }

    if (!soloNumeros(empleado.numeroDocumento)) {
      alert('Caracteres no permitidos: el número de documento solo acepta números')
      return false
    }

    const duplicado = empleados.some(
      (e) =>
        e.numeroDocumento === empleado.numeroDocumento &&
        e.idEmpleado !== idEditando
    )

    if (duplicado) {
      alert('El número de documento ya se encuentra registrado')
      return false
    }

    return true
  }

  // Elimina un empleado
  const borrarEmpleado = async (idEmpleado) => {
    const confirmar = window.confirm(
      '¿Está seguro de eliminar este empleado?'
    )

    if (!confirmar) return

    try {
      await eliminarEmpleado(idEmpleado)
      obtenerEmpleados()
      console.log('Empleado eliminado correctamente')
    } catch (error) {
      console.error('Error al eliminar empleado:', error)
    }
  }

  return (
    <section className="empleado-module">

      {/* Encabezado del módulo */}
      <div className="module-header">
        <div>
          <span className="module-tag">Módulo Empleados</span>
          <h2>Gestión de Empleados</h2>
          <p>Registra empleados con datos completos.</p>
        </div>
      </div>

      {/* Formulario */}
      <div className="card form-card">
        <h3>
          {modoEdicion ? 'Editar Empleado' : 'Formulario Empleado'}
        </h3>

        <form className="empleado-form">

          {/* Nombre */}
          <div className="form-group">
            <label>Nombre</label>
            <input
              type="text"
              name="nombre"
              value={empleado.nombre}
              onChange={handleChange}
              disabled={soloLectura}
              placeholder="Ingrese el nombre"
            />
          </div>

          {/* Apellidos */}
          <div className="form-group">
            <label>Apellidos</label>
            <input
              type="text"
              name="apellidos"
              value={empleado.apellidos}
              onChange={handleChange}
              disabled={soloLectura}
              placeholder="Ingrese los apellidos"
            />
          </div>

          {/* Tipo Documento */}
          <div className="form-group">
            <label>Tipo Documento</label>
            <select
              name="tipoDocumento"
              value={empleado.tipoDocumento}
              onChange={handleChange}
              disabled={soloLectura}
            >
              <option value="">Seleccione un tipo</option>
              <option value="CC">Cédula de ciudadanía</option>
              <option value="TI">Tarjeta de identidad</option>
              <option value="PASAPORTE">Pasaporte</option>
            </select>
          </div>

          {/* Número Documento */}
          <div className="form-group">
            <label>Número Documento</label>
            <input
              type="text"
              name="numeroDocumento"
              value={empleado.numeroDocumento}
              onChange={handleChange}
              disabled={soloLectura}
              placeholder="Ingrese el número de documento"
            />
          </div>

          {/* Fecha Nacimiento */}
          <div className="form-group">
            <label>Fecha Nacimiento</label>
            <input
              type="date"
              name="fechaNacimiento"
              value={empleado.fechaNacimiento}
              onChange={handleChange}
              disabled={soloLectura}
            />
          </div>

          {/* Estado Civil */}
          <div className="form-group">
            <label>Estado Civil</label>
            <select
              name="estadoCivil"
              value={empleado.estadoCivil}
              onChange={handleChange}
              disabled={soloLectura}
            >
              <option value="">Seleccione un estado civil</option>
              <option value="SOLTERO">Soltero</option>
              <option value="CASADO">Casado</option>
              <option value="DIVORCIADO">Divorciado</option>
              <option value="VIUDO">Viudo</option>
            </select>
          </div>

          {/* Botones */}
          <div className="form-actions">
            <button
              type="button"
              className="btn-primary"
              onClick={guardarEmpleado}
              disabled={soloLectura}
            >
              {modoEdicion ? 'Actualizar' : 'Guardar'}
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
          <h3>Empleados Registrados</h3>
          <p>Listado de empleados consultados desde Spring Boot.</p>
        </div>

        <EmpleadoTable
          empleados={empleados}
          onSeleccionar={verEmpleado}
          onEditar={editarEmpleado}
          onEliminar={borrarEmpleado}
        />
      </div>
    </section>
  )
}

export default Empleado




