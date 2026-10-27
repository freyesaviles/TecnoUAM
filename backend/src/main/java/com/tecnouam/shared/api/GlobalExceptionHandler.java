
package com.tecnouam.shared.api;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiError> responder(
            HttpStatus estado,
            String mensaje,
            Map<String, String> detalles) {

        ApiError error = new ApiError(
                LocalDateTime.now(),
                estado.value(),
                mensaje,
                detalles
        );

        return ResponseEntity.status(estado).body(error);
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ApiError> recursoNoEncontrado(
            RecursoNoEncontradoException ex) {

        return responder(
                HttpStatus.NOT_FOUND,
                "Recurso no encontrado",
                Map.of("mensaje", ex.getMessage())
        );
    }

    @ExceptionHandler(ConflictoNegocioException.class)
    public ResponseEntity<ApiError> conflictoNegocio(
            ConflictoNegocioException ex) {

        return responder(
                HttpStatus.CONFLICT,
                "Conflicto de negocio",
                Map.of("mensaje", ex.getMessage())
        );
    }

    @ExceptionHandler(DatosInvalidosException.class)
    public ResponseEntity<ApiError> datosInvalidos(
            DatosInvalidosException ex) {

        return responder(
                HttpStatus.BAD_REQUEST,
                "Datos inválidos",
                Map.of("mensaje", ex.getMessage())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validarCampos(
            MethodArgumentNotValidException ex) {

        Map<String, String> errores = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errores.putIfAbsent(
                        error.getField(),
                        error.getDefaultMessage()
                )
        );

        ex.getBindingResult().getGlobalErrors().forEach(error ->
                errores.putIfAbsent(
                        error.getObjectName(),
                        error.getDefaultMessage()
                )
        );

        return responder(
                HttpStatus.BAD_REQUEST,
                "Error de validación",
                errores
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> violacionRestricciones(
            ConstraintViolationException ex) {

        return responder(
                HttpStatus.BAD_REQUEST,
                "Datos inválidos",
                Map.of("mensaje", "Los datos no cumplen las validaciones")
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> jsonInvalido(
            HttpMessageNotReadableException ex) {

        return responder(
                HttpStatus.BAD_REQUEST,
                "Solicitud inválida",
                Map.of("mensaje", "El JSON contiene datos incorrectos o fechas con formato inválido")
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> parametroInvalido(
            MethodArgumentTypeMismatchException ex) {

        return responder(
                HttpStatus.BAD_REQUEST,
                "Parámetro inválido",
                Map.of("mensaje", "El parámetro " + ex.getName() + " tiene un valor incorrecto")
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> integridadDatos(
            DataIntegrityViolationException ex) {

        return responder(
                HttpStatus.CONFLICT,
                "Conflicto de datos",
                Map.of("mensaje", "La operación incumple una restricción de la base de datos")
        );
    }
}
