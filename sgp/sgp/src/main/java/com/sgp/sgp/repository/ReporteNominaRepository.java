package com.sgp.sgp.repository;

import com.sgp.sgp.model.ReporteNomina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteNominaRepository extends JpaRepository<ReporteNomina, Long> {

    void deleteByNomina_IdNomina(Long idNomina);
}
