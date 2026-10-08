// Componente encargado de mostrar la tabla de empleados
import { useState } from 'react'

function EmpleadoTable({ empleados, onSeleccionar, onEditar, onEliminar }) {
  // Guarda el id de la fila seleccionada para resaltarla
  const [seleccionado, setSeleccionado] = useState(null)

  // Al hacer clic en la fila solo muestra la información en el formulario
  const handleRowClick = (e, empleado) => {
    if (e.target.tagName === 'BUTTON') return
    setSeleccionado(empleado.idEmpleado)
    onSeleccionar(empleado)
  }

  return (
    <table className="empleado-table">

      {/* Encabezado de la tabla */}
      <thead>
        <tr>
          <th>ID</th>
          <th>Nombre</th>
          <th>Apellidos</th>
          <th>Tipo Documento</th>
          <th>Número Documento</th>
          <th>Acciones</th>
        </tr>
      </thead>

      {/* Cuerpo de la tabla */}
      <tbody>

        {/* Recorre la lista de empleados y crea una fila por cada registro */}
        {empleados.map((empleado) => (
          <tr
            key={empleado.idEmpleado}
            className={seleccionado === empleado.idEmpleado ? 'fila-seleccionada' : ''}
            onClick={(e) => handleRowClick(e, empleado)}
          >
            <td>{empleado.idEmpleado}</td>
            <td>{empleado.nombre}</td>
            <td>{empleado.apellidos}</td>
            <td>{empleado.tipoDocumento}</td>
            <td>{empleado.numeroDocumento}</td>

            <td>
              {/* Envía el empleado seleccionado al formulario para editarlo */}
              <button
                type="button"
                className="btn-edit"
                onClick={() => onEditar(empleado)}
              >
                Editar
              </button>

              {/* Envía el ID del empleado para eliminarlo */}
              <button
                type="button"
                className="btn-delete"
                onClick={() => onEliminar(empleado.idEmpleado)}
              >
                Eliminar
              </button>
            </td>
          </tr>
        ))}

      </tbody>
    </table>
  )
}

// Exporta la tabla para usarla en Empleado.jsx
export default EmpleadoTable
