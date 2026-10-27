package com.tecnouam.equipo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipoRepository extends JpaRepository<Equipo, Long> {

    Optional<Equipo> findByCodigoPatrimonial(String codigoPatrimonial);

    /** Para validar unicidad al crear (POST). */
    boolean existsByCodigoPatrimonial(String codigoPatrimonial);

    /** Para validar unicidad al actualizar (PUT): ignora al propio equipo. */
    boolean existsByCodigoPatrimonialAndIdNot(String codigoPatrimonial, Long id);
}
