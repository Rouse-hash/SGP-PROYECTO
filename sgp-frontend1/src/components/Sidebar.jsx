const menuItems = {
  ADMIN: [
    { key: "dashboard", label: "Dashboard", icon: "📊" },
    { key: "empleados", label: "Empleados", icon: "👥" },
    { key: "contratos", label: "Contratos", icon: "📄" },
    { key: "usuarios", label: "Usuarios", icon: "👤" },
    { key: "nominas", label: "Nómina", icon: "💰" },
    { key: "reporte-nomina", label: "Reporte Nómina", icon: "🖨️" },
    { key: "detalle-nomina", label: "Detalle Nómina", icon: "📋" },
  ],
  EMPLEADO: [
    { key: "dashboard", label: "Dashboard", icon: "📊" },
    { key: "reporte-nomina", label: "Reporte Nómina", icon: "🖨️" },
    { key: "detalle-nomina", label: "Detalle Nómina", icon: "📋" },
  ],
};

function Sidebar({ rol, moduloActivo, onModuloChange }) {
  const items = menuItems[rol] || menuItems.EMPLEADO;

  return (
    <aside className="sidebar">
      <nav className="sidebar-nav">
        {items.map((item) => (
          <button
            key={item.key}
            type="button"
            className={`sidebar-btn ${moduloActivo === item.key ? "active" : ""}`}
            onClick={() => onModuloChange(item.key)}
          >
            <span className="sidebar-icon">{item.icon}</span>
            <span className="sidebar-label">{item.label}</span>
          </button>
        ))}
      </nav>
    </aside>
  );
}

export default Sidebar;
