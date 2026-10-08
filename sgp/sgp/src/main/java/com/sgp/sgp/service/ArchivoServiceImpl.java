package com.sgp.sgp.service;

import com.sgp.sgp.model.ArchivoEmpleado;
import com.sgp.sgp.repository.ArchivoEmpleadoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ArchivoServiceImpl implements ArchivoService {

    private final ArchivoEmpleadoRepository archivoRepository;
    private final Path uploadDir;

    public ArchivoServiceImpl(ArchivoEmpleadoRepository archivoRepository,
                              @Value("${app.upload.dir}") String uploadDir) {
        this.archivoRepository = archivoRepository;
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de uploads: " + this.uploadDir);
        }
    }

    @Override
    @Transactional
    public ArchivoEmpleado guardarArchivo(Long idEmpleado, MultipartFile archivo, String tipo) {
        String nombreOriginal = archivo.getOriginalFilename();
        String extension = "";
        if (nombreOriginal != null && nombreOriginal.contains(".")) {
            extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
        }
        String nombreArchivo = UUID.randomUUID().toString() + extension;

        Path dirEmpleado = uploadDir.resolve(String.valueOf(idEmpleado));
        try {
            Files.createDirectories(dirEmpleado);
            Path rutaCompleta = dirEmpleado.resolve(nombreArchivo);
            Files.copy(archivo.getInputStream(), rutaCompleta, StandardCopyOption.REPLACE_EXISTING);

            ArchivoEmpleado archivoEntidad = new ArchivoEmpleado(
                    idEmpleado, nombreOriginal, nombreArchivo,
                    tipo, rutaCompleta.toString(), archivo.getContentType(), archivo.getSize()
            );
            archivoEntidad.setFechaSubida(LocalDateTime.now());

            return archivoRepository.save(archivoEntidad);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArchivoEmpleado> listarArchivos(Long idEmpleado) {
        return archivoRepository.findByIdEmpleadoOrderByFechaSubidaDesc(idEmpleado);
    }

    @Override
    @Transactional(readOnly = true)
    public Resource descargarArchivo(Long idArchivo) {
        ArchivoEmpleado archivo = archivoRepository.findById(idArchivo)
                .orElseThrow(() -> new RuntimeException("Archivo no encontrado con ID: " + idArchivo));

        try {
            Path ruta = Paths.get(archivo.getRuta()).normalize();
            Resource resource = new UrlResource(ruta.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("No se pudo leer el archivo: " + archivo.getNombreOriginal());
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error al acceder al archivo: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void eliminarArchivo(Long idArchivo) {
        ArchivoEmpleado archivo = archivoRepository.findById(idArchivo)
                .orElseThrow(() -> new RuntimeException("Archivo no encontrado con ID: " + idArchivo));

        try {
            Path ruta = Paths.get(archivo.getRuta());
            Files.deleteIfExists(ruta);
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar el archivo del disco: " + e.getMessage());
        }

        archivoRepository.delete(archivo);
    }

    @Override
    public ArchivoEmpleado buscarPorId(Long idArchivo) {
        return archivoRepository.findById(idArchivo)
                .orElseThrow(() -> new RuntimeException("Archivo no encontrado con ID: " + idArchivo));
    }
}
