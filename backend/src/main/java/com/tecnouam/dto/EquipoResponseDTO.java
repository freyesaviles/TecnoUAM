
package com.tecnouam.dto;

import com.tecnouam.equipo.EstadoEquipo;

public record EquipoResponseDTO(

        Long id,
        String codigoPatrimonial,
        String nombre,
        String tipo,
        EstadoEquipo estado

) {}
