// Componente encargado de mostrar la tabla de nómina
function NominaTable({ nominas, onEditar, onEliminar }) {
  return (
    <table className="nomina-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Empleado</th>
          <th>Contrato</th>
          <th>Fecha Pago</th>
          <th>Salario Base</th>
          <th>Deducciones</th>
          <th>Bonificaciones</th>
          <th>Total Pagado</th>
          <th>Estado</th>
          <th>Departamento</th>
          <th>Municipio</th>
          <th>Acciones</th>
        </tr>
      </thead>

      <tbody>
        {nominas.map((nomina) => (
          <tr key={nomina.idNomina}>
            <td>{nomina.idNomina}</td>
            <td>{nomina.empleado?.nombre} {nomina.empleado?.apellidos}</td>
            <td>{nomina.contrato?.idContrato}</td>
            <td>{nomina.fechaPago}</td>
            <td>{nomina.salarioBase}</td>
            <td>{nomina.deducciones}</td>
            <td>{nomina.bonificaciones}</td>
            <td>{nomina.totalPagado}</td>
            <td>{nomina.estado}</td>
            <td>{nomina.departamento}</td>
            <td>{nomina.municipio}</td>
            <td>
              <button
                type="button"
                className="btn-edit"
                onClick={() => onEditar(nomina)}
              >
                Editar
              </button>

              <button
                type="button"
                className="btn-delete"
                onClick={() => onEliminar(nomina.idNomina)}
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

export default NominaTable
