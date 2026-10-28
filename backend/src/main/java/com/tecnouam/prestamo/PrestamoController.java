
package com.tecnouam.prestamo;

import com.tecnouam.dto.PrestamoRequestDTO;
import com.tecnouam.dto.PrestamoResponseDTO;
import com.tecnouam.shared.api.DatosInvalidosException;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    // GET: listar préstamos registrados
    @GetMapping
    public ResponseEntity<List<PrestamoResponseDTO>> listarPrestamos() {

        return ResponseEntity.ok(
                prestamoService.listarPrestamos()
        );
    }

    // POST: registrar nuevo préstamo
    @PostMapping
    public ResponseEntity<PrestamoResponseDTO> registrarPrestamo(
            @Valid @RequestBody PrestamoRequestDTO datos) {

        PrestamoResponseDTO creado =
                prestamoService.registrarPrestamo(datos);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(creado);
    }

    // PUT: registrar devolución
    @PutMapping("/{id}/devolver")
    public ResponseEntity<PrestamoResponseDTO> devolverPrestamo(
            @PathVariable Long id) {

        if (id == null || id <= 0) {
            throw new DatosInvalidosException(
                    "El ID del préstamo debe ser mayor que cero"
            );
        }

        return ResponseEntity.ok(
                prestamoService.devolverPrestamo(id)
        );
    }
}
