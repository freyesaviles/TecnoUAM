
package com.tecnouam.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PrestamoRequestDTO(

        @NotNull(message = "El ID del equipo es obligatorio")
        @Positive(message = "El ID del equipo debe ser mayor que cero")
        Long equipoId,

        @NotBlank(message = "El nombre del solicitante es obligatorio")
        @Size(max = 120, message = "El nombre no puede superar los 120 caracteres")
        String nombreSolicitante,

        @NotNull(message = "La fecha del préstamo es obligatoria")
        LocalDate fechaPrestamo,

        @NotNull(message = "La fecha prevista de devolución es obligatoria")
        LocalDate fechaPrevistaDevolucion

) {

    @JsonIgnore
    @AssertTrue(message = "La devolución prevista no puede ser anterior a la fecha del préstamo")
    public boolean isRangoFechasValido() {

        if (fechaPrestamo == null || fechaPrevistaDevolucion == null) {
            return true;
        }

        return !fechaPrevistaDevolucion.isBefore(fechaPrestamo);
    }
}
