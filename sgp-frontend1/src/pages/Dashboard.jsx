import { useState, useEffect } from "react";
import { obtenerDashboard } from "../services/DashboardService";
import { getCorreo } from "../services/AuthService";
import "../styles/Dashboard.css";

function Dashboard() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await obtenerDashboard();
        setData(response.data);
      } catch {
        setError("Error al cargar el dashboard");
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  if (loading) {
    return <div className="dashboard-loading">Cargando dashboard...</div>;
  }

  if (error) {
    return <div className="dashboard-error">{error}</div>;
  }

  if (!data) return null;

  const cards = [
    { titulo: "Total Empleados", valor: data.totalEmpleados, icono: "👥", color: "#0d3b66" },
    { titulo: "Contratos", valor: data.totalContratos, icono: "📄", color: "#2563eb" },
    { titulo: "Usuarios", valor: data.totalUsuarios, icono: "👤", color: "#7c3aed" },
    { titulo: "Nóminas", valor: data.totalNominas, icono: "💰", color: "#059669" },
  ];

  const formatCurrency = (value) => {
    return new Intl.NumberFormat("es-CO", {
      style: "currency",
      currency: "COP",
      minimumFractionDigits: 0,
    }).format(value);
  };

  const correo = getCorreo();
  const nombreUsuario = correo ? correo.split("@")[0] : "Usuario";

  return (
    <section className="dashboard">
      <div className="dashboard-header">
        <h2>Dashboard</h2>
        <p>Resumen general del sistema de gesti&oacute;n de personal</p>
      </div>

      <div className="dashboard-welcome-card">
        <div className="dashboard-welcome-icon">👋</div>
        <div className="dashboard-welcome-text">
          <h3>&iexcl;Bienvenido, {nombreUsuario}!</h3>
          <p>Desde este panel puedes consultar el estado general del sistema y acceder r&aacute;pidamente a los m&oacute;dulos de SGP.</p>
        </div>
      </div>

      <div className="dashboard-cards">
        {cards.map((card) => (
          <div key={card.titulo} className="dashboard-card" style={{ borderTopColor: card.color }}>
            <div className="dashboard-card-icon">{card.icono}</div>
            <div className="dashboard-card-info">
              <span className="dashboard-card-value">{card.valor}</span>
              <span className="dashboard-card-title">{card.titulo}</span>
            </div>
          </div>
        ))}
      </div>

      <div className="dashboard-summary-card">
        <div className="dashboard-summary-icon">💵</div>
        <div className="dashboard-summary-info">
          <span className="dashboard-summary-value">{formatCurrency(data.resumenNomina)}</span>
          <span className="dashboard-summary-title">Total pagado en nóminas</span>
        </div>
      </div>

      <div className="dashboard-tables">
        <div className="dashboard-table-section">
          <h3>&Uacute;ltimos Empleados</h3>
          <table className="dashboard-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Documento</th>
              </tr>
            </thead>
            <tbody>
              {data.ultimosEmpleados && data.ultimosEmpleados.length > 0 ? (
                data.ultimosEmpleados.map((emp) => (
                  <tr key={emp.idEmpleado}>
                    <td>{emp.idEmpleado}</td>
                    <td>{emp.nombre} {emp.apellidos}</td>
                    <td>{emp.tipoDocumento}: {emp.numeroDocumento}</td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="3">No hay empleados registrados</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>

        <div className="dashboard-table-section">
          <h3>&Uacute;ltimos Contratos</h3>
          <table className="dashboard-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Tipo</th>
                <th>Salario</th>
              </tr>
            </thead>
            <tbody>
              {data.ultimosContratos && data.ultimosContratos.length > 0 ? (
                data.ultimosContratos.map((con) => (
                  <tr key={con.idContrato}>
                    <td>{con.idContrato}</td>
                    <td>{con.tipoContrato}</td>
                    <td>{formatCurrency(con.salario)}</td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="3">No hay contratos registrados</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </section>
  );
}

export default Dashboard;
