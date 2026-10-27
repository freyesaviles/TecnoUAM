package com.tecnouam.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "prestamos", 
       indexes = @Index(name = "idx_prestamos_equipo_estado", columnList = "equipo_id, estado"),
       check = @Check(constraints = "fecha_prevista_devolucion >= fecha_prestamo"))
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_id", nullable = false)
    private Equipo equipo;

    @NotNull
    @Size(max = 100)
    @Column(nullable = false)
    private String solicitante;

    @NotNull
    @Column(name = "fecha_prestamo", nullable = false)
    private LocalDate fechaPrestamo;

    @NotNull
    @Column(name = "fecha_prevista_devolucion", nullable = false)
    private LocalDate fechaPrevistaDevolucion;

    @Column(name = "fecha_devolucion_real")
    private LocalDate fechaDevolucionReal;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPrestamo estado;

    public Prestamo() {}

    public Prestamo(Equipo equipo, String solicitante, LocalDate fechaPrestamo, LocalDate fechaPrevistaDevolucion, EstadoPrestamo estado) {
        this.equipo = equipo;
        this.solicitante = solicitante;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaPrevistaDevolucion = fechaPrevistaDevolucion;
        this.estado = estado;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Equipo getEquipo() { return equipo; }
    public void setEquipo(Equipo equipo) { this.equipo = equipo; }

    public String getSolicitante() { return solicitante; }
    public void setSolicitante(String solicitante) { this.solicitante = solicitante; }

    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(LocalDate fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }

    public LocalDate getFechaPrevistaDevolucion() { return fechaPrevistaDevolucion; }
    public void setFechaPrevistaDevolucion(LocalDate fechaPrevistaDevolucion) { this.fechaPrevistaDevolucion = fechaPrevistaDevolucion; }

    public LocalDate getFechaDevolucionReal() { return fechaDevolucionReal; }
    public void setFechaDevolucionReal(LocalDate fechaDevolucionReal) { this.fechaDevolucionReal = fechaDevolucionReal; }

    public EstadoPrestamo getEstado() { return estado; }
    public void setEstado(EstadoPrestamo estado) { this.estado = estado; }
}
