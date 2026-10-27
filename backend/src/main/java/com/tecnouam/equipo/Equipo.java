package com.tecnouam.equipo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Equipo tecnológico prestable (portátil, proyector, tableta...).
 * Tabla: equipos.
 */
@Entity
@Table(name = "equipos")
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "codigo_patrimonial", nullable = false, unique = true, length = 50)
    private String codigoPatrimonial;

    @NotBlank
    @Size(max = 120)
    @Column(name = "nombre", nullable = false, length = 120)
    private String nombre;

    @NotBlank
    @Size(max = 60)
    @Column(name = "tipo", nullable = false, length = 60)
    private String tipo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoEquipo estado = EstadoEquipo.DISPONIBLE;

    protected Equipo() {
        // requerido por JPA
    }

    public Equipo(String codigoPatrimonial, String nombre, String tipo) {
        this.codigoPatrimonial = codigoPatrimonial;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public void marcarPrestado() {
        this.estado = EstadoEquipo.PRESTADO;
    }

    public void marcarDisponible() {
        this.estado = EstadoEquipo.DISPONIBLE;
    }

    public boolean estaDisponible() {
        return estado == EstadoEquipo.DISPONIBLE;
    }

    public Long getId() {
        return id;
    }

    public String getCodigoPatrimonial() {
        return codigoPatrimonial;
    }

    public void setCodigoPatrimonial(String codigoPatrimonial) {
        this.codigoPatrimonial = codigoPatrimonial;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public EstadoEquipo getEstado() {
        return estado;
    }

    public void setEstado(EstadoEquipo estado) {
        this.estado = estado;
    }
}
