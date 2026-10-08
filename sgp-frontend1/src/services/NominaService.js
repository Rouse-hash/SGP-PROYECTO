import { API_BASE_URL } from "../config/api";
import api from './api'

const API = `${API_BASE_URL}/api/nominas`

const prepararDatos = (nomina) => ({
  ...nomina,
  empleado: { idEmpleado: Number(nomina.empleado) },
  contrato: { idContrato: Number(nomina.contrato) },
  salarioBase: Number(nomina.salarioBase) || null,
  deducciones: nomina.deducciones === '' ? null : Number(nomina.deducciones),
  bonificaciones: nomina.bonificaciones === '' ? null : Number(nomina.bonificaciones),
  totalPagado: Number(nomina.totalPagado) || null
})

export const getNominas = () =>
  api.get(API)

export const crearNomina = (nomina) =>
  api.post(API, prepararDatos(nomina))

export const actualizarNomina = (idNomina, nomina) =>
  api.put(`${API}/${idNomina}`, prepararDatos(nomina))

export const eliminarNomina = (idNomina) =>
  api.delete(`${API}/${idNomina}`)
