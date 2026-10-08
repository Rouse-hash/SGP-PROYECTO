import { API_BASE_URL } from "../config/api";
import api from "./api";

const API_URL = `${API_BASE_URL}/api/usuarios`;

export const listarUsuarios = () => {
  return api.get(API_URL);
};

export const crearUsuario = (usuario) => {
  return api.post(API_URL, usuario);
};

export const actualizarUsuario = (id, usuario) => {
  return api.put(`${API_URL}/${id}`, usuario);
};

export const eliminarUsuario = (id) => {
  return api.delete(`${API_URL}/${id}`);
};
