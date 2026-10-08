import { useEffect, useState } from "react";
import {
  buscarPorDocumento,
  obtenerResumen,
  getNominasPorEmpleado,
  getMiEmpleado,
} from "../services/ReporteNominaService";
import "../styles/ReporteNomina.css";
import { campoVacio, soloNumeros } from "../utils/validaciones";

function ReporteNomina() {
  const [numeroDocumento, setNumeroDocumento] = useState("");
  const [resumen, setResumen] = useState(null);
  const [nominas, setNominas] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [rol] = useState(localStorage.getItem("rol"));

  const cargarReporteEmpleado = async (empleado) => {
    setLoading(true);
    setError("");
    setResumen(null);
    setNominas([]);
    try {
      const resResp = await obtenerResumen(empleado.idEmpleado);
      setResumen(resResp.data);

      const nomResp = await getNominasPorEmpleado(empleado.idEmpleado);
      setNominas(nomResp.data || []);
    } catch {
      setError("Error al generar el reporte.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (rol === "EMPLEADO") {
      const cargarMiReporte = async () => {
        setLoading(true);
        try {
          const empResp = await getMiEmpleado();
          const emp = empResp.data;
          if (!emp) {
            setError("No tienes un empleado asociado a tu cuenta.");
            return;
          }
          setNumeroDocumento(emp.numeroDocumento || "");
          await cargarReporteEmpleado(emp);
        } catch {
          setError("Error al cargar tu reporte.");
        } finally {
          setLoading(false);
        }
      };
      cargarMiReporte();
    }
  }, [rol]);

  const handleBuscar = async (e) => {
    e.preventDefault();
    if (campoVacio(numeroDocumento)) {
      alert("El número de documento es obligatorio");
      return;
    }
    if (!soloNumeros(numeroDocumento)) {
      alert("Caracteres no permitidos: el número de documento solo acepta números");
      return;
    }
    setLoading(true);
    setError("");
    setResumen(null);
    setNominas([]);

    try {
      const empResp = await buscarPorDocumento(numeroDocumento.trim());
      const emp = empResp.data;

      const resResp = await obtenerResumen(emp.idEmpleado);
      setResumen(resResp.data);

      const nomResp = await getNominasPorEmpleado(emp.idEmpleado);
      setNominas(nomResp.data || []);
    } catch (err) {
      if (err.response && err.response.status === 404) {
        setError("No se encontró un empleado con ese número de documento.");
      } else {
        setError("Error al generar el reporte.");
      }
    } finally {
      setLoading(false);
    }
  };

  const handleImprimir = () => {
    window.print();
  };

  const formatCurrency = (value) => {
    return new Intl.NumberFormat("es-CO", {
      style: "currency",
      currency: "COP",
      minimumFractionDigits: 0,
    }).format(value || 0);
  };

  const totalPagado = nominas.reduce((acc, n) => acc + (n.totalPagado || 0), 0);
  const totalSalarioBase = nominas.reduce((acc, n) => acc + (n.salarioBase || 0), 0);
  const totalDeducciones = nominas.reduce((acc, n) => acc + (n.deducciones || 0), 0);
  const totalBonificaciones = nominas.reduce((acc, n) => acc + (n.bonificaciones || 0), 0);

  return (
    <section className="reporte-nomina print-area">
      <div className="detalle-header no-print">
        <h2>Reporte N&oacute;mina</h2>
        {rol === "EMPLEADO" ? (
          <p>Aqu&iacute; puedes consultar e imprimir tu reporte de n&oacute;mina.</p>
        ) : (
          <p>Busque un empleado por su n&uacute;mero de documento para generar su reporte de n&oacute;mina.</p>
        )}
      </div>

      {rol !== "EMPLEADO" && (
        <form className="detalle-busqueda no-print" onSubmit={handleBuscar}>
          <div className="busqueda-field">
            <label htmlFor="numDocReporte">N&uacute;mero de documento</label>
            <input
              id="numDocReporte"
              type="text"
              value={numeroDocumento}
              onChange={(e) => setNumeroDocumento(e.target.value)}
              placeholder="Ingrese el n&uacute;mero de documento"
            />
          </div>
          <button type="submit" className="btn-buscar" disabled={loading}>
            {loading ? "Buscando..." : "Generar reporte"}
          </button>
        </form>
      )}

      {error && <div className="detalle-error no-print">{error}</div>}

      {resumen && (
        <>
          <div className="detalle-actions no-print">
            <button type="button" className="btn-imprimir" onClick={handleImprimir}>
              Imprimir / PDF
            </button>
          </div>

          <div className="reporte-titulo">
            <h3>Reporte de N&oacute;mina</h3>
            <p>Generado para el empleado: {resumen.empleado.nombre} {resumen.empleado.apellidos}</p>
          </div>

          <div className="detalle-section">
            <h3>Datos del Empleado</h3>
            <table className="detalle-table detalle-data-table">
              <tbody>
                <tr><td className="label">Nombre</td><td>{resumen.empleado.nombre} {resumen.empleado.apellidos}</td></tr>
                <tr><td className="label">Tipo Documento</td><td>{resumen.empleado.tipoDocumento}</td></tr>
                <tr><td className="label">N&uacute;mero Documento</td><td>{resumen.empleado.numeroDocumento}</td></tr>
                <tr><td className="label">Fecha Nacimiento</td><td>{resumen.empleado.fechaNacimiento || "-"}</td></tr>
                <tr><td className="label">Estado Civil</td><td>{resumen.empleado.estadoCivil || "-"}</td></tr>
              </tbody>
            </table>
          </div>

          <div className="detalle-section">
            <h3>Contratos</h3>
            {resumen.contratos && resumen.contratos.length > 0 ? (
              <table className="detalle-table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Tipo</th>
                    <th>Fecha Inicio</th>
                    <th>Fecha Fin</th>
                    <th>Salario</th>
                  </tr>
                </thead>
                <tbody>
                  {resumen.contratos.map((c) => (
                    <tr key={c.idContrato}>
                      <td>{c.idContrato}</td>
                      <td>{c.tipoContrato}</td>
                      <td>{c.fechaInicio}</td>
                      <td>{c.fechaFin || "Indefinido"}</td>
                      <td>{formatCurrency(c.salario)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            ) : (
              <p className="sin-datos">No tiene contratos registrados.</p>
            )}
          </div>

          <div className="detalle-section">
            <h3>N&oacute;minas del Empleado</h3>
            {nominas.length > 0 ? (
              <table className="detalle-table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Fecha Pago</th>
                    <th>Departamento</th>
                    <th>Municipio</th>
                    <th>Salario Base</th>
                    <th>Deducciones</th>
                    <th>Bonificaciones</th>
                    <th>Total Pagado</th>
                    <th>Estado</th>
                  </tr>
                </thead>
                <tbody>
                  {nominas.map((n) => (
                    <tr key={n.idNomina}>
                      <td>{n.idNomina}</td>
                      <td>{n.fechaPago}</td>
                      <td>{n.departamento}</td>
                      <td>{n.municipio}</td>
                      <td>{formatCurrency(n.salarioBase)}</td>
                      <td>{formatCurrency(n.deducciones)}</td>
                      <td>{formatCurrency(n.bonificaciones)}</td>
                      <td className="total-pagado">{formatCurrency(n.totalPagado)}</td>
                      <td>{n.estado}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            ) : (
              <p className="sin-datos">No tiene n&oacute;minas registradas.</p>
            )}
          </div>

          <div className="detalle-section">
            <h3>Resumen de N&oacute;mina</h3>
            <div className="resumen-cards">
              <div className="resumen-card">
                <span className="resumen-card-label">N&uacute;mero de n&oacute;minas</span>
                <span className="resumen-card-value">{nominas.length}</span>
              </div>
              <div className="resumen-card">
                <span className="resumen-card-label">Salario base total</span>
                <span className="resumen-card-value">{formatCurrency(totalSalarioBase)}</span>
              </div>
              <div className="resumen-card">
                <span className="resumen-card-label">Deducciones totales</span>
                <span className="resumen-card-value">{formatCurrency(totalDeducciones)}</span>
              </div>
              <div className="resumen-card">
                <span className="resumen-card-label">Bonificaciones totales</span>
                <span className="resumen-card-value">{formatCurrency(totalBonificaciones)}</span>
              </div>
              <div className="resumen-card resumen-card-total">
                <span className="resumen-card-label">Total pagado</span>
                <span className="resumen-card-value">{formatCurrency(totalPagado)}</span>
              </div>
            </div>
          </div>
        </>
      )}

      {loading && <div className="detalle-loading">Generando reporte...</div>}
    </section>
  );
}

export default ReporteNomina;
