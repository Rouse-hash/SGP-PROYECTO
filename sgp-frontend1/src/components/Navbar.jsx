import { getCorreo, getRol } from "../services/AuthService";

function Navbar({ onLogout }) {
  const correo = getCorreo();
  const rol = getRol();

  return (
    <header className="navbar">
      <div className="navbar-brand">
        <span className="navbar-logo">🏥</span>
        <h1 className="navbar-title">Sistema de Gestión de Personal</h1>
      </div>
      <div className="navbar-user">
        <div className="navbar-user-info">
          <span className="navbar-email">{correo}</span>
          <span className="navbar-rol">{rol}</span>
        </div>
        <button type="button" className="navbar-logout" onClick={onLogout}>
          Cerrar Sesión
        </button>
      </div>
    </header>
  );
}

export default Navbar;
