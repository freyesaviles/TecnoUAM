package com.tecnouam.repository;

import com.tecnouam.model.Equipo;
import com.tecnouam.model.EstadoEquipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Long> {
    Optional<Equipo> findByCodigoPatrimonial(String codigoPatrimonial);
    List<Equipo> findByEstado(EstadoEquipo estado);
}
