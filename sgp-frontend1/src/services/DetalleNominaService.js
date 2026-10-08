import { API_BASE_URL } from "../config/api";
import api from "./api";

const EMPLEADOS_URL = `${API_BASE_URL}/api/empleados`;
const ARCHIVOS_URL = `${API_BASE_URL}/api/archivos`;

export const buscarPorDocumento = (numeroDocumento) => {
  return api.get(`${EMPLEADOS_URL}/documento/${numeroDocumento}`);
};

export const getMiEmpleado = () => {
  return api.get(`${EMPLEADOS_URL}/me`);
};

export const obtenerResumen = (idEmpleado) => {
  return api.get(`${EMPLEADOS_URL}/${idEmpleado}/resumen`);
};

export const subirArchivo = (idEmpleado, archivo, tipo) => {
  const formData = new FormData();
  formData.append("archivo", archivo);
  formData.append("tipo", tipo);
  return api.post(`${ARCHIVOS_URL}/empleado/${idEmpleado}`, formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });
};

export const listarArchivos = (idEmpleado) => {
  return api.get(`${ARCHIVOS_URL}/empleado/${idEmpleado}`);
};

export const descargarArchivo = (idArchivo) => {
  return api.get(`${ARCHIVOS_URL}/${idArchivo}/descargar`, {
    responseType: "blob",
  });
};

export const eliminarArchivo = (idArchivo) => {
  return api.delete(`${ARCHIVOS_URL}/${idArchivo}`);
};
