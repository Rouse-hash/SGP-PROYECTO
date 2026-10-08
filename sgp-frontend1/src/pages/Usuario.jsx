import { useState, useEffect } from 'react'

import UsuarioTable from '../components/UsuarioTable'

import {
  listarUsuarios,
  crearUsuario,
  actualizarUsuario,
  eliminarUsuario
} from '../services/UsuarioService'

import { getEmpleados } from '../services/EmpleadoService'

import '../styles/Usuario.css'

// Importa utilidades de validación
import {
  camposObligatoriosVacios,
  correoValido
} from '../utils/validaciones'

function Usuario() {
  const [usuario, setUsuario] = useState({
    idEmpleado: '',
    correo: '',
    password: '',
    rol: 'ADMIN',
    activo: true
  })

  const [usuarios, setUsuarios] = useState([])
  const [empleados, setEmpleados] = useState([])
  const [modoEdicion, setModoEdicion] = useState(false)
  const [idEditando, setIdEditando] = useState(null)

  const handleChange = (e) => {
    const { name, value } = e.target

    setUsuario({
      ...usuario,
      [name]: value
    })
  }

  const obtenerUsuarios = async () => {
    try {
      const respuesta = await listarUsuarios()
      setUsuarios(respuesta.data)
    } catch (error) {
      console.error('Error al obtener usuarios:', error)
    }
  }

  const obtenerEmpleados = async () => {
    try {
      const respuesta = await getEmpleados()
      setEmpleados(respuesta.data)
    } catch (error) {
      console.error('Error al obtener empleados:', error)
    }
  }

  useEffect(() => {
    const cargarDatos = async () => {
      await obtenerUsuarios()
      await obtenerEmpleados()
    }
    cargarDatos()
  }, [])

  const limpiarFormulario = () => {
    setUsuario({
      idEmpleado: '',
      correo: '',
      password: '',
      rol: 'ADMIN',
      activo: true
    })

    setModoEdicion(false)
    setIdEditando(null)
  }

  const editarUsuario = (usuarioSeleccionado) => {
    setUsuario({
      idEmpleado: usuarioSeleccionado.empleado?.idEmpleado || '',
      correo: usuarioSeleccionado.correo,
      password: '',
      rol: usuarioSeleccionado.rol,
      activo: usuarioSeleccionado.activo
    })

    setIdEditando(usuarioSeleccionado.id)
    setModoEdicion(true)
  }

  const guardarUsuario = async () => {
    if (!validarUsuario()) return

    try {
      if (modoEdicion) {
        await actualizarUsuario(idEditando, {
          correo: usuario.correo,
          password: usuario.password.trim() || null,
          rol: usuario.rol,
          activo: usuario.activo,
          empleado: { idEmpleado: Number(usuario.idEmpleado) }
        })
      } else {
        await crearUsuario({
          ...usuario,
          idEmpleado: Number(usuario.idEmpleado)
        })
      }

      limpiarFormulario()
      obtenerUsuarios()
    } catch (error) {
      const mensaje =
        error.response?.data?.mensaje ||
        'Error al guardar o actualizar usuario'
      alert(mensaje)
    }
  }

  // Valida el formulario antes de guardar
  const validarUsuario = () => {
    const obligatorios = camposObligatoriosVacios(usuario, [
      'idEmpleado',
      'correo',
      ...(modoEdicion ? [] : ['password'])
    ])

    if (obligatorios.length > 0) {
      alert('Todos los campos son obligatorios, diligencie el formulario completo')
      return false
    }

    if (!correoValido(usuario.correo)) {
      alert('Caracteres no permitidos: el correo electrónico no tiene un formato válido')
      return false
    }

    const duplicado = usuarios.some(
      (u) =>
        u.correo?.toLowerCase() === usuario.correo.trim().toLowerCase() &&
        u.id !== idEditando
    )

    if (duplicado) {
      alert('El correo electrónico ya se encuentra registrado')
      return false
    }

    return true
  }

  const borrarUsuario = async (idUsuario) => {
    const confirmar = window.confirm(
      '¿Está seguro de eliminar este usuario?'
    )

    if (!confirmar) {
      return
    }

    try {
      await eliminarUsuario(idUsuario)
      obtenerUsuarios()
    } catch (error) {
      console.error('Error al eliminar usuario:', error)
    }
  }

  return (
    <section className="usuario-module">
      <div className="module-header">
        <div>
          <span className="module-tag">Módulo Usuario</span>
          <h2>Gestión de Usuarios</h2>
          <p>Asigna roles a los empleados del sistema.</p>
        </div>
      </div>

      <div className="card form-card">
        <h3>
          {modoEdicion ? 'Editar Usuario' : 'Formulario Usuario'}
        </h3>

        <form className="usuario-form">
          <div className="form-group">
            <label>Empleado</label>
            <select
              name="idEmpleado"
              value={usuario.idEmpleado}
              onChange={handleChange}
            >
              <option value="">Seleccione un empleado</option>
              {empleados.map((emp) => (
                <option key={emp.idEmpleado} value={emp.idEmpleado}>
                  {emp.nombre} {emp.apellidos} - {emp.tipoDocumento}: {emp.numeroDocumento}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Correo</label>
            <input
              type="email"
              name="correo"
              value={usuario.correo}
              onChange={handleChange}
              placeholder="Ingrese el correo"
            />
          </div>

          <div className="form-group">
            <label>Contraseña</label>
            <input
              type="password"
              name="password"
              value={usuario.password}
              onChange={handleChange}
              placeholder={
                modoEdicion
                  ? 'Dejar vacío para mantener la contraseña'
                  : 'Ingrese la contraseña'
              }
            />
          </div>

          <div className="form-group">
            <label>Rol</label>
            <select
              name="rol"
              value={usuario.rol}
              onChange={handleChange}
            >
              <option value="ADMIN">ADMIN</option>
              <option value="EMPLEADO">EMPLEADO</option>
            </select>
          </div>

          <div className="form-actions">
            <button
              type="button"
              className="btn-primary"
              onClick={guardarUsuario}
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

      <div className="card table-card">
        <div className="table-title">
          <h3>Usuarios Registrados</h3>
          <p>Listado de usuarios consultados desde Spring Boot.</p>
        </div>

        <UsuarioTable
          usuarios={usuarios}
          onEditar={editarUsuario}
          onEliminar={borrarUsuario}
        />
      </div>
    </section>
  )
}

export default Usuario
