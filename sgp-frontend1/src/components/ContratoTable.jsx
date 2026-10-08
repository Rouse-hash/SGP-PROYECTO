// Tabla de contratos
import { useState } from "react";

function ContratoTable({ contratos, onSeleccionar, onEditar, onEliminar }) {
  // Guarda el id de la fila seleccionada para resaltarla
  const [seleccionado, setSeleccionado] = useState(null);

  // Al hacer clic en la fila solo muestra la información en el formulario
  const handleRowClick = (e, contrato) => {
    if (e.target.tagName === "BUTTON") return;
    setSeleccionado(contrato.idContrato);
    onSeleccionar(contrato);
  };

  return (
    <table className="contrato-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Empleado</th>
          <th>Tipo Contrato</th>
          <th>Fecha Inicio</th>
          <th>Fecha Fin</th>
          <th>Salario</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        {contratos.map((contrato) => (
          <tr
            key={contrato.idContrato}
            className={seleccionado === contrato.idContrato ? "fila-seleccionada" : ""}
            onClick={(e) => handleRowClick(e, contrato)}
          >
            {/* ID del contrato */}
            <td>{contrato.idContrato}</td>

            {/*  Mostrar nombre y apellidos del empleado asociado */}
            <td>
              {contrato.empleado
                ? `${contrato.empleado.nombre} ${contrato.empleado.apellidos}`
                : "Sin empleado"}
            </td>

            {/* Tipo de contrato */}
            <td>{contrato.tipoContrato}</td>

            {/* Fechas */}
            <td>{contrato.fechaInicio}</td>
            <td>{contrato.fechaFin}</td>

            {/* Salario */}
            <td>{contrato.salario}</td>

            {/*  Botones con mismas clases que en módulo empleados */}
            <td>
              <button
                className="btn-edit"
                onClick={() => onEditar(contrato)}
              >
                Editar
              </button>
              <button
                className="btn-delete"
                onClick={() => onEliminar(contrato.idContrato)}
              >
                Eliminar
              </button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}

export default ContratoTable;



