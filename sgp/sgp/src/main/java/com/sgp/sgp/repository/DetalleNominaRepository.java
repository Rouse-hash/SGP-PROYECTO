package com.sgp.sgp.repository;

import com.sgp.sgp.model.DetalleNomina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleNominaRepository extends JpaRepository<DetalleNomina, Long> {

    List<DetalleNomina> findByNomina_IdNomina(Long idNomina);

    void deleteByNomina_IdNomina(Long idNomina);
}
