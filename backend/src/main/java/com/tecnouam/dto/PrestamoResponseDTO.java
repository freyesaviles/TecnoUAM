
package com.tecnouam.dto;

import java.time.LocalDate;
import com.tecnouam.prestamo.EstadoPrestamo;

public record PrestamoResponseDTO(

        Long id,
        Long equipoId,
        String nombreSolicitante,
        LocalDate fechaPrestamo,
        LocalDate fechaPrevistaDevolucion,
        LocalDate fechaDevolucionReal,
        EstadoPrestamo estado

) {}
