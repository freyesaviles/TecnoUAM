
package com.tecnouam.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EquipoRequestDTO(

        @NotBlank(message = "El código patrimonial es obligatorio")
        @Size(max = 50, message = "El código patrimonial no puede superar los 50 caracteres")
        String codigoPatrimonial,

        @NotBlank(message = "El nombre del equipo es obligatorio")
        @Size(max = 120, message = "El nombre no puede superar los 120 caracteres")
        String nombre,

        @NotBlank(message = "El tipo de equipo es obligatorio")
        @Size(max = 60, message = "El tipo no puede superar los 60 caracteres")
        String tipo

) {}
