package com.tecnouam.prestamo;

import java.time.LocalDate;

import com.tecnouam.equipo.Equipo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Préstamo de un equipo a un solicitante. Muchos préstamos (historial) pueden
 * referirse al mismo equipo, pero solo uno puede estar ACTIVO a la vez
 * (garantizado en base de datos con un índice único parcial).
 * Tabla: prestamos.
 */
@Entity
@Table(name = "prestamos")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "equipo_id", nullable = false, foreignKey = @ForeignKey(name = "fk_prestamos_equipo"))
    private Equipo equipo;

    @NotBlank
    @Size(max = 120)
    @Column(name = "nombre_solicitante", nullable = false, length = 120)
    private String nombreSolicitante;

    @NotNull
    @Column(name = "fecha_prestamo", nullable = false)
    private LocalDate fechaPrestamo;

    @NotNull
    @Column(name = "fecha_prevista_devolucion", nullable = false)
    private LocalDate fechaPrevistaDevolucion;

    /** Se completa al registrar la devolución; null mientras el préstamo está ACTIVO. */
    @Column(name = "fecha_devolucion_real")
    private LocalDate fechaDevolucionReal;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoPrestamo estado = EstadoPrestamo.ACTIVO;

    protected Prestamo() {
        // requerido por JPA
    }

    public Prestamo(Equipo equipo, String nombreSolicitante, LocalDate fechaPrestamo,
            LocalDate fechaPrevistaDevolucion) {
        this.equipo = equipo;
        this.nombreSolicitante = nombreSolicitante;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaPrevistaDevolucion = fechaPrevistaDevolucion;
    }

    /** Regla: la fecha prevista no puede ser anterior a la fecha del préstamo. */
    @AssertTrue(message = "La fecha prevista de devolución no puede ser anterior a la fecha del préstamo")
    public boolean isRangoFechasValido() {
        if (fechaPrestamo == null || fechaPrevistaDevolucion == null) {
            return true; // los nulos los reporta @NotNull
        }
        return !fechaPrevistaDevolucion.isBefore(fechaPrestamo);
    }

    public boolean estaActivo() {
        return estado == EstadoPrestamo.ACTIVO;
    }

    public void marcarDevuelto(LocalDate fechaDevolucion) {
        this.estado = EstadoPrestamo.DEVUELTO;
        this.fechaDevolucionReal = fechaDevolucion;
    }

    public Long getId() {
        return id;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public String getNombreSolicitante() {
        return nombreSolicitante;
    }

    public void setNombreSolicitante(String nombreSolicitante) {
        this.nombreSolicitante = nombreSolicitante;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaPrevistaDevolucion() {
        return fechaPrevistaDevolucion;
    }

    public void setFechaPrevistaDevolucion(LocalDate fechaPrevistaDevolucion) {
        this.fechaPrevistaDevolucion = fechaPrevistaDevolucion;
    }

    public LocalDate getFechaDevolucionReal() {
        return fechaDevolucionReal;
    }

    public EstadoPrestamo getEstado() {
        return estado;
    }

    public void setEstado(EstadoPrestamo estado) {
        this.estado = estado;
    }
}
