import { API_BASE_URL } from "../config/api";
import api from "./api";
import { buscarPorDocumento, obtenerResumen, getMiEmpleado } from "./DetalleNominaService";

const NOMINAS_URL = `${API_BASE_URL}/api/nominas`;

export { buscarPorDocumento, obtenerResumen, getMiEmpleado };

export const getNominasPorEmpleado = (idEmpleado) => {
  return api.get(`${NOMINAS_URL}/empleado/${idEmpleado}`);
};
