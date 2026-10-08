import { API_BASE_URL } from "../config/api";
import api from "./api";

const API_URL = `${API_BASE_URL}/api/auth`;

export const login = async (correo, password) => {
  const response = await api.post(`${API_URL}/login`, { correo, password });
  const data = response.data;
  localStorage.setItem("token", data.token);
  localStorage.setItem("correo", data.correo);
  localStorage.setItem("rol", data.rol);
  return data;
};

export const logout = () => {
  localStorage.removeItem("token");
  localStorage.removeItem("correo");
  localStorage.removeItem("rol");
};

export const isAuthenticated = () => {
  return !!localStorage.getItem("token");
};

export const getRol = () => {
  return localStorage.getItem("rol");
};

export const getCorreo = () => {
  return localStorage.getItem("correo");
};

export const getUsuario = () => {
  const token = localStorage.getItem("token");
  const correo = localStorage.getItem("correo");
  const rol = localStorage.getItem("rol");
  if (!token) return null;
  return { token, correo, rol };
};
