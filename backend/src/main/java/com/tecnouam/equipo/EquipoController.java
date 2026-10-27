
package com.tecnouam.equipo;

import com.tecnouam.dto.EquipoRequestDTO;
import com.tecnouam.dto.EquipoResponseDTO;
import com.tecnouam.shared.api.DatosInvalidosException;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipos")
public class EquipoController {

    private final EquipoService equipoService;

    public EquipoController(EquipoService equipoService) {
        this.equipoService = equipoService;
    }

    // GET: listar todos los equipos
    @GetMapping
    public ResponseEntity<List<EquipoResponseDTO>> listarEquipos() {

        return ResponseEntity.ok(
                equipoService.listarEquipos()
        );
    }

    // GET: buscar equipo por ID
    @GetMapping("/{id}")
    public ResponseEntity<EquipoResponseDTO> buscarPorId(
            @PathVariable Long id) {

        validarId(id);

        return ResponseEntity.ok(
                equipoService.buscarPorId(id)
        );
    }

    // POST: registrar equipo
    @PostMapping
    public ResponseEntity<EquipoResponseDTO> crearEquipo(
            @Valid @RequestBody EquipoRequestDTO datos) {

        EquipoResponseDTO creado =
                equipoService.crearEquipo(datos);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(creado);
    }

    // PUT: actualizar equipo
    @PutMapping("/{id}")
    public ResponseEntity<EquipoResponseDTO> actualizarEquipo(
            @PathVariable Long id,
            @Valid @RequestBody EquipoRequestDTO datos) {

        validarId(id);

        return ResponseEntity.ok(
                equipoService.actualizarEquipo(id, datos)
        );
    }

    // DELETE: eliminar equipo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEquipo(
            @PathVariable Long id) {

        validarId(id);

        equipoService.eliminarEquipo(id);

        return ResponseEntity.noContent().build();
    }

    // Validar identificador recibido por URL
    private void validarId(Long id) {

        if (id == null || id <= 0) {
            throw new DatosInvalidosException(
                    "El ID del equipo debe ser mayor que cero"
            );
        }
    }
}
