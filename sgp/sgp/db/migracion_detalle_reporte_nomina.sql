-- Migración: crear tablas detalle_nomina y reporte_nomina y rellenarlas con las nóminas existentes.
-- Estructura espejo del patrón del profesor (detalle_solicitud / estados).

CREATE TABLE IF NOT EXISTS detalle_nomina (
  id_detalle    BIGINT NOT NULL AUTO_INCREMENT,
  id_nomina     BIGINT NOT NULL,
  tipo_concepto VARCHAR(255) NOT NULL,
  descripcion   VARCHAR(255),
  valor         DECIMAL(38,2) NOT NULL,
  estado        VARCHAR(255) NOT NULL,
  fecha         DATE,
  PRIMARY KEY (id_detalle),
  CONSTRAINT fk_detalle_nomina_nomina FOREIGN KEY (id_nomina) REFERENCES nomina(id_nomina)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS reporte_nomina (
  id_reporte          BIGINT NOT NULL AUTO_INCREMENT,
  id_empleado         BIGINT NOT NULL,
  id_nomina           BIGINT,
  fecha_generacion    DATE NOT NULL,
  total_salario       DECIMAL(38,2) NOT NULL,
  total_deducciones   DECIMAL(38,2) NOT NULL,
  total_bonificaciones DECIMAL(38,2) NOT NULL,
  total_pagado        DECIMAL(38,2) NOT NULL,
  PRIMARY KEY (id_reporte),
  CONSTRAINT fk_reporte_empleado FOREIGN KEY (id_empleado) REFERENCES empleado(id_empleado),
  CONSTRAINT fk_reporte_nomina FOREIGN KEY (id_nomina) REFERENCES nomina(id_nomina)
) ENGINE=InnoDB;

INSERT INTO detalle_nomina (id_nomina, tipo_concepto, descripcion, valor, estado, fecha)
SELECT id_nomina, 'SALARIO', 'Salario base', salario_base, 'PAGADO', fecha_pago FROM nomina;

INSERT INTO detalle_nomina (id_nomina, tipo_concepto, descripcion, valor, estado, fecha)
SELECT id_nomina, 'DEDUCCION', 'Deducciones', COALESCE(deducciones,0), 'PAGADO', fecha_pago FROM nomina;

INSERT INTO detalle_nomina (id_nomina, tipo_concepto, descripcion, valor, estado, fecha)
SELECT id_nomina, 'BONIFICACION', 'Bonificaciones', COALESCE(bonificaciones,0), 'PAGADO', fecha_pago FROM nomina;

INSERT INTO reporte_nomina (id_empleado, id_nomina, fecha_generacion, total_salario, total_deducciones, total_bonificaciones, total_pagado)
SELECT id_empleado, id_nomina, fecha_pago, salario_base, COALESCE(deducciones,0), COALESCE(bonificaciones,0), total_pagado FROM nomina;
