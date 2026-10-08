import { API_BASE_URL } from "../config/api";
import api from "./api";

const API_URL = `${API_BASE_URL}/api/contratos`;

export const getContratos = () => {
  return api.get(API_URL);
};

export const crearContrato = (idEmpleado, contrato) => {
  return api.post(`${API_URL}/empleado/${idEmpleado}`, contrato);
};

export const actualizarContrato = (idContrato, contrato) => {
  return api.put(`${API_URL}/${idContrato}`, contrato);
};

export const eliminarContrato = (idContrato) => {
  return api.delete(`${API_URL}/${idContrato}`);
};
