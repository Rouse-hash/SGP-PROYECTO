package com.sgp.sgp.service;

import com.sgp.sgp.model.ArchivoEmpleado;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArchivoService {
    ArchivoEmpleado guardarArchivo(Long idEmpleado, MultipartFile archivo, String tipo);
    List<ArchivoEmpleado> listarArchivos(Long idEmpleado);
    Resource descargarArchivo(Long idArchivo);
    void eliminarArchivo(Long idArchivo);
    ArchivoEmpleado buscarPorId(Long idArchivo);
}
