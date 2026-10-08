import { API_BASE_URL } from "../config/api";
import api from './api'

const API = `${API_BASE_URL}/api/publica/colombia`

export const getDepartamentos = () =>
  api.get(`${API}/departamentos`)

export const getRegiones = () =>
  api.get(`${API}/regiones`)

export const getMunicipiosPorDepartamento = (idDepartamento) =>
  api.get(`${API}/departamentos/${idDepartamento}/municipios`)
