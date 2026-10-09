import { useEffect, useState } from 'react'
import {
  getNominas,
  crearNomina,
  actualizarNomina,
  eliminarNomina
} from '../services/NominaService'
import {
  getDepartamentos,
  getMunicipiosPorDepartamento
} from '../services/ColombiaService'
import { getEmpleados } from '../services/EmpleadoService'
import { getContratos } from '../services/ContratoService'

import NominaTable from '../components/NominaTable'
import '../styles/Nomina.css'
import {
  camposObligatoriosVacios,
  esNumeroValido
} from '../utils/validaciones'

function Nomina() {
  const [nominas, setNominas] = useState([])
  const [empleados, setEmpleados] = useState([])
  const [contratos, setContratos] = useState([])
  const [departamentos, setDepartamentos] = useState([])
  const [municipios, setMunicipios] = useState([])
  const [modoEdicion, setModoEdicion] = useState(false)
  const [idEditando, setIdEditando] = useState(null)

  const [formData, setFormData] = useState({
    empleado: '',
    contrato: '',
    fechaPago: '',
    salarioBase: '',
    deducciones: '',
    bonificaciones: '',
    totalPagado: '',
    estado: '',
    departamento: '',
    municipio: ''
  })

  const cargarNominas = async () => {
    try {
      const response = await getNominas()
      setNominas(response.data)
    } catch (error) {
      console.error('Error al cargar nóminas:', error)
    }
  }

  const cargarDepartamentos = async () => {
    try {
      const response = await getDepartamentos()
      setDepartamentos(response.data)
    } catch (error) {
      console.error('Error al cargar departamentos:', error)
    }
  }

  const cargarMunicipios = async (idDepartamento) => {
    if (!idDepartamento) return
    try {
      const response = await getMunicipiosPorDepartamento(idDepartamento)
      setMunicipios(response.data)
    } catch (error) {
      console.error('Error al cargar municipios:', error)
    }
  }

  const cargarEmpleados = async () => {
    try {
      const response = await getEmpleados()
      setEmpleados(response.data)
    } catch (error) {
      console.error('Error al cargar empleados:', error)
    }
  }

  const cargarContratos = async () => {
    try {
      const response = await getContratos()
      setContratos(response.data)
    } catch (error) {
      console.error('Error al cargar contratos:', error)
    }
  }

  useEffect(() => {
    const cargarDatos = async () => {
      await cargarNominas()
      await cargarDepartamentos()
      await cargarEmpleados()
      await cargarContratos()
    }
    cargarDatos()
  }, [])

  const handleChange = (e) => {
    const { name, value } = e.target
    const nuevosDatos = { ...formData, [name]: value }

    if (['salarioBase', 'deducciones', 'bonificaciones'].includes(name)) {
      const base = parseFloat(nuevosDatos.salarioBase) || 0
      const ded = parseFloat(nuevosDatos.deducciones) || 0
      const bon = parseFloat(nuevosDatos.bonificaciones) || 0
      nuevosDatos.totalPagado = (base + bon - ded).toFixed(2)
    }

    if (name === 'departamento') {
      const dept = departamentos.find(d => d.nombre === value)
      setFormData({ ...nuevosDatos, municipio: '' })
      if (dept) cargarMunicipios(dept.id)
    } else {
      setFormData(nuevosDatos)
    }
  }

  const guardarNomina = async () => {
    if (!validarNomina()) return

    try {
      if (idEditando) {
        await actualizarNomina(idEditando, formData)
      } else {
        await crearNomina(formData)
      }
      limpiarFormulario()
      cargarNominas()
    } catch (error) {
      const mensaje =
        error.response?.data?.mensaje ||
        'Error al guardar nómina'
      alert(mensaje)
    }
  }

  // Valida el formulario antes de guardar
  const validarNomina = () => {
    const obligatorios = camposObligatoriosVacios(formData, [
      'empleado',
      'contrato',
      'fechaPago',
      'salarioBase',
      'estado',
      'departamento',
      'municipio'
    ])

    if (obligatorios.length > 0) {
      alert('Todos los campos son obligatorios, diligencie el formulario completo')
      return false
    }

    const etiquetas = {
      salarioBase: 'Salario Base',
      deducciones: 'Deducciones',
      bonificaciones: 'Bonificaciones'
    }

    for (const campo of ['salarioBase', 'deducciones', 'bonificaciones']) {
      const valor = String(formData[campo]).trim()
      if (valor !== '' && !esNumeroValido(valor)) {
        alert(`Caracteres no permitidos: ${etiquetas[campo]} solo acepta números`)
        return false
      }
    }

    if (Number(formData.salarioBase) <= 0) {
      alert('El salario base debe ser mayor que cero')
      return false
    }

    return true
  }

  const editarNomina = (nomina) => {
    setFormData({
      empleado: nomina.empleado?.idEmpleado || '',
      contrato: nomina.contrato?.idContrato || '',
      fechaPago: nomina.fechaPago,
      salarioBase: nomina.salarioBase,
      deducciones: nomina.deducciones,
      bonificaciones: nomina.bonificaciones,
      totalPagado: nomina.totalPagado,
      estado: nomina.estado,
      departamento: nomina.departamento,
      municipio: nomina.municipio
    })
    setIdEditando(nomina.idNomina)
    setModoEdicion(true)
    if (nomina.departamento) {
      const dept = departamentos.find(d => d.nombre === nomina.departamento)
      if (dept) cargarMunicipios(dept.id)
    }
  }

  const borrarNomina = async (idNomina) => {
    const confirmar = window.confirm(
      '¿Está seguro de eliminar esta nómina?'
    )
    if (!confirmar) return

    try {
      await eliminarNomina(idNomina)
      cargarNominas()
    } catch (error) {
      console.error('Error al eliminar nómina:', error)
    }
  }

  const limpiarFormulario = () => {
    setFormData({
      empleado: '',
      contrato: '',
      fechaPago: '',
      salarioBase: '',
      deducciones: '',
      bonificaciones: '',
      totalPagado: '',
      estado: '',
      departamento: '',
      municipio: ''
    })
    setModoEdicion(false)
    setIdEditando(null)
  }

  return (
    <section className="nomina-module">
      <div className="module-header">
        <div>
          <span className="module-tag">Módulo Nómina</span>
          <h2>Gestión de Nóminas</h2>
          <p>Gestión de Nóminas</p>
        </div>
      </div>

      <div className="card form-card">
        <h3>
          {modoEdicion ? 'Editar Nómina' : 'Formulario Nómina'}
        </h3>

        <form className="nomina-form">
          <div className="form-group">
            <label>Empleado</label>
            <select
              name="empleado"
              value={formData.empleado}
              onChange={handleChange}
              required
            >
              <option value="">Seleccione un empleado</option>
              {empleados.map((e) => (
                <option key={e.idEmpleado} value={e.idEmpleado}>
                  {e.nombre} {e.apellidos}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Contrato</label>
            <select
              name="contrato"
              value={formData.contrato}
              onChange={handleChange}
              required
            >
              <option value="">Seleccione un contrato</option>
              {contratos.map((c) => (
                <option key={c.idContrato} value={c.idContrato}>
                  Contrato #{c.idContrato} - {c.tipoContrato}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Fecha Pago</label>
            <input
              type="date"
              name="fechaPago"
              value={formData.fechaPago}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label>Salario Base</label>
            <input
              type="number"
              name="salarioBase"
              placeholder="Ingrese el salario base"
              value={formData.salarioBase}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label>Deducciones</label>
            <input
              type="number"
              name="deducciones"
              placeholder="Ingrese las deducciones"
              value={formData.deducciones}
              onChange={handleChange}
            />
          </div>

          <div className="form-group">
            <label>Bonificaciones</label>
            <input
              type="number"
              name="bonificaciones"
              placeholder="Ingrese las bonificaciones"
              value={formData.bonificaciones}
              onChange={handleChange}
            />
          </div>

          <div className="form-group">
            <label>Total Pagado</label>
            <input
              type="text"
              name="totalPagado"
              value={formData.totalPagado ? `$${Number(formData.totalPagado).toLocaleString('es-CO')}` : ''}
              readOnly
              className="input-readonly"
            />
          </div>

          <div className="form-group">
            <label>Estado</label>
            <select
              name="estado"
              value={formData.estado}
              onChange={handleChange}
              required
            >
              <option value="">Seleccione un estado</option>
              <option value="Pendiente">Pendiente</option>
              <option value="Pagado">Pagado</option>
            </select>
          </div>

          <div className="form-group">
            <label>Departamento</label>
            <select
              name="departamento"
              value={formData.departamento}
              onChange={handleChange}
              required
            >
              <option value="">Seleccione un departamento</option>
              {departamentos.map((d) => (
                <option key={d.id} value={d.nombre}>{d.nombre}</option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Municipio</label>
            <select
              name="municipio"
              value={formData.municipio}
              onChange={handleChange}
              required
            >
              <option value="">Seleccione un municipio</option>
              {municipios.map((m) => (
                <option key={m.id} value={m.nombre}>{m.nombre}</option>
              ))}
            </select>
          </div>

          <div className="form-actions">
            <button
              type="button"
              className="btn-primary"
              onClick={guardarNomina}
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
          <h3>Nóminas Registradas</h3>
          <p>Listado de nóminas consultadas desde Spring Boot.</p>
        </div>

        <NominaTable
          nominas={nominas}
          onEditar={editarNomina}
          onEliminar={borrarNomina}
        />
      </div>
    </section>
  )
}

export default Nomina
