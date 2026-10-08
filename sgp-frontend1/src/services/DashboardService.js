import { API_BASE_URL } from "../config/api";
import api from "./api";

const API_URL = `${API_BASE_URL}/api/dashboard`;

export const obtenerDashboard = () => {
  return api.get(API_URL);
};
