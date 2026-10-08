import { useEffect, useState } from "react";
import {
  buscarPorDocumento,
  getMiEmpleado,
  obtenerResumen,
  subirArchivo,
  eliminarArchivo,
  descargarArchivo,
} from "../services/DetalleNominaService";
import "../styles/DetalleNomina.css";
import { campoVacio, soloNumeros } from "../utils/validaciones";

function DetalleNomina() {
  const [numeroDocumento, setNumeroDocumento] = useState("");
  const [resumen, setResumen] = useState(null);
  const [empleado, setEmpleado] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [subiendo, setSubiendo] = useState(false);
  const [rol] = useState(localStorage.getItem("rol"));

  const cargarEmpleado = async (empleado) => {
    setLoading(true);
    setError("");
    setResumen(null);
    setEmpleado(null);
    try {
      setEmpleado(empleado);
      const resResp = await obtenerResumen(empleado.idEmpleado);
      setResumen(resResp.data);
    } catch {
      setError("Error al cargar la información del empleado.");
    } finally {
      setLoading(false);
    }
  };

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
    try {
      const empResp = await buscarPorDocumento(numeroDocumento.trim());
      await cargarEmpleado(empResp.data);
    } catch (err) {
      if (err.response && err.response.status === 404) {
        setError("No se encontró un empleado con ese número de documento.");
      } else {
        setError("Error al buscar el empleado.");
      }
    }
  };

  useEffect(() => {
    if (rol === "EMPLEADO") {
      const cargarMiDetalle = async () => {
        setLoading(true);
        setError("");
        try {
          const empResp = await getMiEmpleado();
          const emp = empResp.data;
          if (!emp) {
            setError("No tienes un empleado asociado a tu cuenta.");
            return;
          }
          setNumeroDocumento(emp.numeroDocumento || "");
          await cargarEmpleado(emp);
        } catch {
          setError("Error al cargar tu información.");
        } finally {
          setLoading(false);
        }
      };
      cargarMiDetalle();
    }
  }, [rol]);

  const handleSubirArchivo = async (e) => {
    const file = e.target.files[0];
    if (!file || !empleado) return;

    const tipo = prompt("Tipo de archivo: HV (Hoja de Vida), CEDULA, CONTRATO, OTRO");
    if (!tipo) return;

    setSubiendo(true);
    try {
      await subirArchivo(empleado.idEmpleado, file, tipo.toUpperCase());
      const resResp = await obtenerResumen(empleado.idEmpleado);
      setResumen(resResp.data);
    } catch {
      alert("Error al subir el archivo.");
    } finally {
      setSubiendo(false);
      e.target.value = "";
    }
  };

  const handleEliminarArchivo = async (idArchivo) => {
    if (!window.confirm("¿Está seguro de eliminar este archivo?")) return;
    try {
      await eliminarArchivo(idArchivo);
      const resResp = await obtenerResumen(empleado.idEmpleado);
      setResumen(resResp.data);
    } catch {
      alert("Error al eliminar el archivo.");
    }
  };

  const handleDescargar = async (idArchivo, nombreOriginal) => {
    try {
      const resp = await descargarArchivo(idArchivo);
      const url = window.URL.createObjectURL(new Blob([resp.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", nombreOriginal);
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch {
      alert("Error al descargar el archivo.");
    }
  };

  const tipoArchivoLabel = (tipo) => {
    const labels = { HV: "Hoja de Vida", CEDULA: "Cédula", CONTRATO: "Contrato", OTRO: "Otro" };
    return labels[tipo] || tipo;
  };

  const formatBytes = (bytes) => {
    if (!bytes) return "0 B";
    const k = 1024;
    const sizes = ["B", "KB", "MB"];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(1)) + " " + sizes[i];
  };

  return (
    <section className="detalle-nomina print-area">
      <div className="detalle-header no-print">
        <h2>Detalle N&oacute;mina</h2>
        {rol === "EMPLEADO" ? (
          <p>Aqu&iacute; puedes consultar tu informaci&oacute;n.</p>
        ) : (
          <p>Busque un empleado por su n&uacute;mero de documento para ver toda su informaci&oacute;n.</p>
        )}
      </div>

      {rol !== "EMPLEADO" && (
        <form className="detalle-busqueda no-print" onSubmit={handleBuscar}>
          <div className="busqueda-field">
            <label htmlFor="numDoc">N&uacute;mero de documento</label>
            <input
              id="numDoc"
              type="text"
              value={numeroDocumento}
              onChange={(e) => setNumeroDocumento(e.target.value)}
              placeholder="Ingrese el n&uacute;mero de documento"
            />
          </div>
          <button type="submit" className="btn-buscar" disabled={loading}>
            {loading ? "Buscando..." : "Buscar"}
          </button>
        </form>
      )}

      {error && <div className="detalle-error no-print">{error}</div>}

      {resumen && (
        <>
          <div className="detalle-actions no-print">
            {rol === "ADMIN" && (
              <label className="btn-subir">
                {subiendo ? "Subiendo..." : "Subir archivo"}
                <input type="file" onChange={handleSubirArchivo} hidden />
              </label>
            )}
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
                      <td>${Number(c.salario).toLocaleString("es-CO")}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            ) : (
              <p className="sin-datos">No tiene contratos registrados.</p>
            )}
          </div>

          <div className="detalle-section">
            <h3>Archivos</h3>
            {resumen.archivos && resumen.archivos.length > 0 ? (
              <table className="detalle-table">
                <thead>
                  <tr>
                    <th>Tipo</th>
                    <th>Nombre</th>
                    <th>Tama&ntilde;o</th>
                    <th>Fecha</th>
                    {rol === "ADMIN" && <th className="no-print">Acciones</th>}
                  </tr>
                </thead>
                <tbody>
                  {resumen.archivos.map((a) => (
                    <tr key={a.id}>
                      <td>{tipoArchivoLabel(a.tipo)}</td>
                      <td>{a.nombreOriginal}</td>
                      <td>{formatBytes(a.tamano)}</td>
                      <td>{new Date(a.fechaSubida).toLocaleDateString("es-CO")}</td>
                      {rol === "ADMIN" && (
                        <td className="no-print">
                          <button className="btn-descargar" onClick={() => handleDescargar(a.id, a.nombreOriginal)}>
                            Descargar
                          </button>
                          <button className="btn-eliminar" onClick={() => handleEliminarArchivo(a.id)}>
                            Eliminar
                          </button>
                        </td>
                      )}
                    </tr>
                  ))}
                </tbody>
              </table>
            ) : (
              <p className="sin-datos">No hay archivos subidos.</p>
            )}
          </div>
        </>
      )}

      {empleado && !resumen && <div className="detalle-loading">Cargando detalle...</div>}
    </section>
  );
}

export default DetalleNomina;
