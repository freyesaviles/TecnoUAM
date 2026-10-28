
package com.tecnouam.prestamo;

import com.tecnouam.dto.PrestamoRequestDTO;
import com.tecnouam.dto.PrestamoResponseDTO;
import com.tecnouam.equipo.Equipo;
import com.tecnouam.equipo.EstadoEquipo;
import com.tecnouam.shared.api.ConflictoNegocioException;
import com.tecnouam.shared.api.DatosInvalidosException;
import com.tecnouam.shared.api.RecursoNoEncontradoException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final EntityManager entityManager;
    private final Validator validator;

    public PrestamoService(
            PrestamoRepository prestamoRepository,
            EntityManager entityManager,
            Validator validator) {

        this.prestamoRepository = prestamoRepository;
        this.entityManager = entityManager;
        this.validator = validator;
    }

    // Convierte la entidad Prestamo en un DTO
    private PrestamoResponseDTO convertirDTO(Prestamo prestamo) {

        return new PrestamoResponseDTO(
                prestamo.getId(),
                prestamo.getEquipo().getId(),
                prestamo.getNombreSolicitante(),
                prestamo.getFechaPrestamo(),
                prestamo.getFechaPrevistaDevolucion(),
                prestamo.getFechaDevolucionReal(),
                prestamo.getEstado()
        );
    }

    // Validar los datos recibidos
    private void validarDatos(PrestamoRequestDTO datos) {

        if (datos == null) {
            throw new DatosInvalidosException(
                    "Los datos del préstamo son obligatorios"
            );
        }

        Set<ConstraintViolation<PrestamoRequestDTO>> errores =
                validator.validate(datos);

        if (!errores.isEmpty()) {
            throw new DatosInvalidosException(
                    errores.iterator().next().getMessage()
            );
        }

        if (datos.fechaPrevistaDevolucion()
                .isBefore(datos.fechaPrestamo())) {

            throw new DatosInvalidosException(
                    "La devolución prevista no puede ser anterior al préstamo"
            );
        }
    }

    // 1. Listar todos los préstamos
    public List<PrestamoResponseDTO> listarPrestamos() {

        return prestamoRepository
                .findAllByOrderByFechaPrestamoDescIdDesc()
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    // 2. Registrar un nuevo préstamo
    @Transactional
    public PrestamoResponseDTO registrarPrestamo(
            PrestamoRequestDTO datos) {

        validarDatos(datos);

        // Bloquear el equipo durante la operación
        Equipo equipo = entityManager.find(
                Equipo.class,
                datos.equipoId(),
                LockModeType.PESSIMISTIC_WRITE
        );

        if (equipo == null) {
            throw new RecursoNoEncontradoException(
                    "No existe el equipo con ID: " + datos.equipoId()
            );
        }

        // Validar disponibilidad
        if (!equipo.estaDisponible()) {
            throw new ConflictoNegocioException(
                    "El equipo ya se encuentra PRESTADO"
            );
        }

        // Comprobar que no tenga préstamos activos
        boolean tienePrestamoActivo =
                prestamoRepository.existsByEquipo_IdAndEstado(
                        equipo.getId(),
                        EstadoPrestamo.ACTIVO
                );

        if (tienePrestamoActivo) {
            throw new ConflictoNegocioException(
                    "El equipo ya tiene un préstamo activo"
            );
        }

        // Crear préstamo
        Prestamo prestamo = new Prestamo(
                equipo,
                datos.nombreSolicitante().trim(),
                datos.fechaPrestamo(),
                datos.fechaPrevistaDevolucion()
        );

        // Actualizar estado del equipo
        equipo.marcarPrestado();

        // Guardar y sincronizar cambios
        Prestamo guardado =
                prestamoRepository.saveAndFlush(prestamo);

        return convertirDTO(guardado);
    }

    // 3. Registrar devolución
    @Transactional
    public PrestamoResponseDTO devolverPrestamo(Long id) {

        if (id == null || id <= 0) {
            throw new DatosInvalidosException(
                    "El ID del préstamo debe ser mayor que cero"
            );
        }

        // Bloquear el préstamo para evitar doble devolución
        Prestamo prestamo = entityManager.find(
                Prestamo.class,
                id,
                LockModeType.PESSIMISTIC_WRITE
        );

        if (prestamo == null) {
            throw new RecursoNoEncontradoException(
                    "No existe un préstamo con ID: " + id
            );
        }

        // Verificar que todavía esté activo
        if (!prestamo.estaActivo()) {
            throw new ConflictoNegocioException(
                    "Este préstamo ya fue devuelto"
            );
        }

        LocalDate fechaActual = LocalDate.now();

        if (fechaActual.isBefore(prestamo.getFechaPrestamo())) {
            throw new DatosInvalidosException(
                    "No se puede devolver un préstamo con fecha futura"
            );
        }

        // Bloquear también el equipo relacionado
        Equipo equipo = entityManager.find(
                Equipo.class,
                prestamo.getEquipo().getId(),
                LockModeType.PESSIMISTIC_WRITE
        );

        if (equipo == null) {
            throw new RecursoNoEncontradoException(
                    "El equipo asociado ya no existe"
            );
        }

        // Registrar devolución y liberar equipo
        prestamo.marcarDevuelto(fechaActual);
        equipo.marcarDisponible();

        entityManager.flush();

        return convertirDTO(prestamo);
    }
}
