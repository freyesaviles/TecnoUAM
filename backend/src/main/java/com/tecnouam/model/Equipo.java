package com.tecnouam.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "equipos")
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "codigo_patrimonial", nullable = false, unique = true)
    private String codigoPatrimonial;

    @NotNull
    @Size(max = 100)
    @Column(nullable = false)
    private String nombre;

    @NotNull
    @Size(max = 50)
    @Column(nullable = false)
    private String tipo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoEquipo estado;

    public Equipo() {}

    public Equipo(String codigoPatrimonial, String nombre, String tipo, EstadoEquipo estado) {
        this.codigoPatrimonial = codigoPatrimonial;
        this.nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigoPatrimonial() { return codigoPatrimonial; }
    public void setCodigoPatrimonial(String codigoPatrimonial) { this.codigoPatrimonial = codigoPatrimonial; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public EstadoEquipo getEstado() { return estado; }
    public void setEstado(EstadoEquipo estado) { this.estado = estado; }
}
