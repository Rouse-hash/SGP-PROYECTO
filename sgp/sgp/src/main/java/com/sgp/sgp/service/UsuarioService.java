package com.sgp.sgp.service;

// Importa la entidad Usuario sobre la cual se realizarán
// las operaciones de negocio.
import com.sgp.sgp.model.Usuario;

// Permite trabajar con listas de objetos.
import java.util.List;

// Permite manejar valores que pueden o no existir,
// evitando errores por valores nulos.
import java.util.Optional;

/**
 * Interfaz que define las operaciones de negocio
 * relacionadas con la gestión de usuarios.
 *
 * Esta interfaz pertenece a la capa Service
 * dentro de la arquitectura por capas.
 *
 * Su principal objetivo es establecer un contrato
 * entre el Controller y la implementación del servicio
 * (UsuarioServiceImpl).
 *
 * El Controller únicamente conoce esta interfaz,
 * sin depender de la implementación concreta,
 * favoreciendo el desacoplamiento del sistema.
 */
public interface UsuarioService {

    /**
     * Consulta todos los usuarios registrados
     * en la base de datos.
     *
     * Esta operación es utilizada por el
     * método GET del controlador.
     *
     * @return Lista de usuarios registrados.
     */
    List<Usuario> listarUsuarios();

    /**
     * Busca un usuario utilizando su identificador.
     *
     * Si el usuario existe devuelve un objeto Optional
     * con la información encontrada.
     *
     * Si no existe, devuelve Optional.empty().
     *
     * @param id Identificador único del usuario.
     * @return Optional<Usuario>.
     */
    Optional<Usuario> buscarPorId(Long id);

    /**
     * Registra un nuevo usuario.
     *
     * Esta operación es utilizada por
     * el método POST del controlador.
     *
     * Antes de guardar un usuario pueden realizarse
     * validaciones adicionales, por ejemplo:
     *
     * • Verificar que el correo no exista.
     * • Validar reglas de negocio.
     * • Cifrar la contraseña (BCrypt).
     *
     * @param usuario Información del usuario.
     * @return Usuario registrado.
     */
    Usuario guardarUsuario(Usuario usuario);

    /**
     * Actualiza la información de un usuario existente.
     *
     * Esta operación es utilizada por
     * el método PUT del controlador.
     *
     * @param id Identificador del usuario.
     * @param usuario Información actualizada.
     * @return Usuario actualizado.
     */
    Usuario actualizarUsuario(Long id, Usuario usuario);

    /**
     * Elimina un usuario de la base de datos.
     *
     * Esta operación es utilizada por
     * el método DELETE del controlador.
     *
     * @param id Identificador del usuario.
     */
    void eliminarUsuario(Long id);

    /**
     * Busca un usuario mediante
     * su correo electrónico.
     *
     * Este método es utilizado principalmente
     * durante el proceso de autenticación
     * (Login con JWT).
     *
     * También puede utilizarse para validar
     * que un correo no esté registrado previamente.
     *
     * @param correo Correo electrónico del usuario.
     * @return Optional<Usuario>.
     */
    Optional<Usuario> buscarPorCorreo(String correo);

    Usuario guardarUsuarioMigrado(Usuario usuario);

}
