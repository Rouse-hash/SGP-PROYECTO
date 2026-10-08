function UsuarioTable({ usuarios, onEditar, onEliminar }) {
  return (
    <table className="usuario-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Empleado</th>
          <th>Correo</th>
          <th>Rol</th>
          <th>Activo</th>
          <th>Acciones</th>
        </tr>
      </thead>

      <tbody>
        {usuarios.map((usuario) => (
          <tr key={usuario.id}>
            <td>{usuario.id}</td>
            <td>
              {usuario.empleado
                ? `${usuario.empleado.nombre} ${usuario.empleado.apellidos}`
                : 'Sin empleado'}
            </td>
            <td>{usuario.correo}</td>
            <td>{usuario.rol}</td>
            <td>{usuario.activo ? 'Sí' : 'No'}</td>

            <td>
              <button
                type="button"
                className="btn-edit"
                onClick={() => onEditar(usuario)}
              >
                Editar
              </button>

              <button
                type="button"
                className="btn-delete"
                onClick={() => onEliminar(usuario.id)}
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

export default UsuarioTable
