import { useState } from "react";
import { login } from "../services/AuthService";
import { campoVacio, correoValido } from "../utils/validaciones";
import "../styles/Login.css";

function Login({ onLoginSuccess }) {
  const [correo, setCorreo] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    if (campoVacio(correo) || campoVacio(password)) {
      alert("Todos los campos son obligatorios, diligencie el formulario completo");
      return;
    }

    if (!correoValido(correo)) {
      alert("Caracteres no permitidos: el correo electrónico no tiene un formato válido");
      return;
    }

    setLoading(true);

    try {
      const data = await login(correo, password);
      if (onLoginSuccess) {
        onLoginSuccess(data);
      }
    } catch (err) {
      if (err.response && err.response.status === 401) {
        setError("Credenciales inválidas. Verifica tu correo y contraseña.");
      } else {
        setError("Error de conexión con el servidor.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <div className="login-header">
          <span className="login-logo">🏥</span>
          <h1>Sistema de Gestión de Personal</h1>
          <p>Hospital Universitario San Ignacio HUSI</p>
        </div>

        <form className="login-form" onSubmit={handleSubmit}>
          <h2>Iniciar Sesión</h2>

          {error && (
            <div className="login-error" role="alert">
              <span className="login-error-icon">⚠️</span>
              <span>{error}</span>
            </div>
          )}

          <div className="login-field">
            <label htmlFor="correo">Correo electrónico</label>
            <input
              id="correo"
              type="email"
              value={correo}
              onChange={(e) => {
                setCorreo(e.target.value);
                setError("");
              }}
              placeholder="ejemplo@correo.com"
              required
            />
          </div>

          <div className="login-field">
            <label htmlFor="password">Contrase&ntilde;a</label>
            <input
              id="password"
              type="password"
              value={password}
              onChange={(e) => {
                setPassword(e.target.value);
                setError("");
              }}
              placeholder="Ingrese su contrase&ntilde;a"
              required
            />
          </div>

          <button type="submit" className="login-btn" disabled={loading}>
            {loading ? "Ingresando..." : "Ingresar"}
          </button>
        </form>
      </div>
    </div>
  );
}

export default Login;
