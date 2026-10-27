package com.tecnouam.repository;

import com.tecnouam.model.EstadoPrestamo;
import com.tecnouam.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    List<Prestamo> findByEstado(EstadoPrestamo estado);
    List<Prestamo> findByEquipoIdAndEstado(Long equipoId, EstadoPrestamo estado);
}
