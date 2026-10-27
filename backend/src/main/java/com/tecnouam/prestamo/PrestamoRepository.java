package com.tecnouam.prestamo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    /**
     * ¿El equipo tiene un préstamo en ese estado? Con ACTIVO sirve para
     * bloquear el préstamo duplicado y la eliminación del equipo.
     */
    boolean existsByEquipo_IdAndEstado(Long equipoId, EstadoPrestamo estado);

    Optional<Prestamo> findByEquipo_IdAndEstado(Long equipoId, EstadoPrestamo estado);

    /*
     * open-in-view está desactivado y la relación es LAZY: estas consultas
     * traen el equipo junto con el préstamo para poder armar el DTO sin
     * LazyInitializationException.
     */
    @EntityGraph(attributePaths = "equipo")
    List<Prestamo> findAllByOrderByFechaPrestamoDescIdDesc();

    @EntityGraph(attributePaths = "equipo")
    List<Prestamo> findByEstadoOrderByFechaPrestamoDescIdDesc(EstadoPrestamo estado);

    @EntityGraph(attributePaths = "equipo")
    Optional<Prestamo> findWithEquipoById(Long id);
}
