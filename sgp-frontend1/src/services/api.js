import axios from "axios";
import { API_BASE_URL } from "../config/api";

const api = axios.create({
  baseURL: API_BASE_URL,
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

let cerrandoSesion = false;

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const tieneToken = !!localStorage.getItem("token");
    if (error.response?.status === 401 && tieneToken && !cerrandoSesion) {
      cerrandoSesion = true;
      localStorage.removeItem("token");
      localStorage.removeItem("correo");
      localStorage.removeItem("rol");
      window.location.reload();
    }
    return Promise.reject(error);
  }
);

export default api;
