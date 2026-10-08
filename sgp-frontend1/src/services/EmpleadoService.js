import { API_BASE_URL } from "../config/api";
import api from "./api";

const API_URL = `${API_BASE_URL}/api/empleados`;

export const getEmpleados = () => {
  return api.get(API_URL);
};

export const crearEmpleado = (empleado) => {
  return api.post(API_URL, empleado);
};

export const actualizarEmpleado = (idEmpleado, empleado) => {
  return api.put(`${API_URL}/${idEmpleado}`, empleado);
};

export const eliminarEmpleado = (idEmpleado) => {
  return api.delete(`${API_URL}/${idEmpleado}`);
};
