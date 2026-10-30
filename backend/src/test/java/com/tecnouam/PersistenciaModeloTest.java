package com.tecnouam.modelo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.tecnouam.equipo.Equipo;
import com.tecnouam.equipo.EquipoRepository;
import com.tecnouam.equipo.EstadoEquipo;
import com.tecnouam.prestamo.EstadoPrestamo;
import com.tecnouam.prestamo.Prestamo;
import com.tecnouam.prestamo.PrestamoRepository;

import jakarta.validation.ConstraintViolationException;

/**
 * Pruebas de persistencia y restricciones del modelo.
 * Usan el PostgreSQL local (mismas migraciones Flyway que la app) y cada
 * prueba hace rollback, así que no dejan datos.
 * Requisito: PostgreSQL debe estar ejecutándose y la base indicada en
 * {@code DATABASE_URL} debe existir.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PersistenciaModeloTest {

    @Autowired
    private EquipoRepository equipos;

    @Autowired
    private PrestamoRepository prestamos;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private JdbcTemplate jdbc;

    private final LocalDate hoy = LocalDate.now();

    private String codigoUnico() {
        return "T-" + UUID.randomUUID().toString().substring(0, 8);
    }

    private Equipo nuevoEquipo() {
        return equipos.saveAndFlush(new Equipo(codigoUnico(), "Equipo de prueba", "LAPTOP"));
    }

    @Test
    void guardaEquipoDisponibleYLoBuscaPorCodigo() {
        Equipo e = nuevoEquipo();

        Equipo leido = equipos.findByCodigoPatrimonial(e.getCodigoPatrimonial()).orElseThrow();

        assertThat(leido.getId()).isNotNull();
        assertThat(leido.getEstado()).isEqualTo(EstadoEquipo.DISPONIBLE);
        assertThat(equipos.existsByCodigoPatrimonial(e.getCodigoPatrimonial())).isTrue();
        assertThat(equipos.existsByCodigoPatrimonialAndIdNot(e.getCodigoPatrimonial(), e.getId())).isFalse();
    }

    @Test
    void rechazaCodigoPatrimonialDuplicado() {
        Equipo e = nuevoEquipo();

        assertThatThrownBy(() -> equipos.saveAndFlush(new Equipo(e.getCodigoPatrimonial(), "Otro", "TABLETA")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void rechazaEquipoSinNombre() {
        assertThatThrownBy(() -> equipos.saveAndFlush(new Equipo(codigoUnico(), " ", "LAPTOP")))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void guardaPrestamoActivoConSuEquipo() {
        Equipo e = nuevoEquipo();

        Prestamo p = prestamos.saveAndFlush(new Prestamo(e, "Luis Pérez", hoy, hoy.plusDays(3)));
        em.clear();

        Prestamo leido = prestamos.findWithEquipoById(p.getId()).orElseThrow();
        assertThat(leido.getEstado()).isEqualTo(EstadoPrestamo.ACTIVO);
        assertThat(leido.getEquipo().getId()).isEqualTo(e.getId());
        assertThat(prestamos.existsByEquipo_IdAndEstado(e.getId(), EstadoPrestamo.ACTIVO)).isTrue();
    }

    @Test
    void rechazaFechaPrevistaAnteriorALaFechaDePrestamo() {
        Equipo e = nuevoEquipo();

        assertThatThrownBy(() -> prestamos.saveAndFlush(new Prestamo(e, "Luis Pérez", hoy, hoy.minusDays(1))))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void laBaseDeDatosTambienRechazaFechasInvalidas() {
        Equipo e = nuevoEquipo();

        assertThatThrownBy(() -> jdbc.update(
                "insert into prestamos (equipo_id, nombre_solicitante, fecha_prestamo, "
                        + "fecha_prevista_devolucion, estado) values (?, 'Directo', ?, ?, 'ACTIVO')",
                e.getId(), java.sql.Date.valueOf(hoy), java.sql.Date.valueOf(hoy.minusDays(1))))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void rechazaSegundoPrestamoActivoDelMismoEquipo() {
        Equipo e = nuevoEquipo();
        prestamos.saveAndFlush(new Prestamo(e, "Primero", hoy, hoy.plusDays(2)));

        assertThatThrownBy(() -> prestamos.saveAndFlush(new Prestamo(e, "Segundo", hoy, hoy.plusDays(2))))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void permiteNuevoPrestamoActivoTrasLaDevolucion() {
        Equipo e = nuevoEquipo();
        Prestamo primero = prestamos.saveAndFlush(new Prestamo(e, "Primero", hoy, hoy.plusDays(2)));

        primero.marcarDevuelto(hoy);
        prestamos.saveAndFlush(primero);
        prestamos.saveAndFlush(new Prestamo(e, "Segundo", hoy, hoy.plusDays(4)));

        assertThat(prestamos.existsByEquipo_IdAndEstado(e.getId(), EstadoPrestamo.ACTIVO)).isTrue();
        assertThat(prestamos.findByEstadoOrderByFechaPrestamoDescIdDesc(EstadoPrestamo.DEVUELTO))
                .extracting(Prestamo::getId).contains(primero.getId());
    }

    @Test
    void alEliminarUnEquipoSeEliminaSuHistorialDePrestamos() {
        Equipo e = nuevoEquipo();
        Prestamo p = prestamos.saveAndFlush(new Prestamo(e, "Luis", hoy, hoy.plusDays(1)));
        p.marcarDevuelto(hoy);
        prestamos.saveAndFlush(p);
        Long equipoId = e.getId();

        // Elimina las entidades administradas antes de borrar el equipo para
        // comprobar la cascada real de PostgreSQL, no una referencia JPA
        // pendiente en el contexto de persistencia.
        em.clear();
        equipos.deleteById(equipoId);
        equipos.flush();
        em.clear();

        assertThat(equipos.findById(equipoId)).isEmpty();
        assertThat(prestamos.existsById(p.getId())).isFalse();
    }

    @Test
    void lasMigracionesFlywayEstanAplicadas() {
        // V1 (tablas) y V2 (datos demo). No se verifican los datos en sí
        // porque Postman puede modificarlos durante las demostraciones.
        Integer aplicadas = jdbc.queryForObject(
                "select count(*) from flyway_schema_history where success", Integer.class);

        assertThat(aplicadas).isGreaterThanOrEqualTo(2);
    }
}
