-- ============================================================
-- TecnoUAM - Script de base de datos (PostgreSQL)
-- Uso: crear la BD y ejecutar este script en pgAdmin o en la
-- consola de Database de IntelliJ IDEA.
--   CREATE DATABASE tecnouam;
-- Nota: la app usa ddl-auto=update, por lo que las tablas
-- también se crean solas al iniciar el backend. Este script
-- permite reproducir la demostración desde cero.
-- crear base datos con CREATE DATABASE tecnouam;
-- ============================================================

DROP TABLE IF EXISTS prestamos;
DROP TABLE IF EXISTS equipos;
DROP TABLE IF EXISTS usuarios;

CREATE TABLE usuarios (
    id        BIGSERIAL PRIMARY KEY,
    username  VARCHAR(50)  NOT NULL UNIQUE,
    password  VARCHAR(100) NOT NULL,           -- hash BCrypt
    rol       VARCHAR(20)  NOT NULL CHECK (rol IN ('USER', 'ADMIN'))
);

CREATE TABLE equipos (
    id                  BIGSERIAL PRIMARY KEY,
    codigo_patrimonial  VARCHAR(50)  NOT NULL UNIQUE,
    nombre              VARCHAR(100) NOT NULL,
    tipo                VARCHAR(50)  NOT NULL,
    estado              VARCHAR(20)  NOT NULL CHECK (estado IN ('DISPONIBLE', 'PRESTADO'))
);

CREATE TABLE prestamos (
    id                         BIGSERIAL PRIMARY KEY,
    equipo_id                  BIGINT       NOT NULL REFERENCES equipos(id),
    solicitante                VARCHAR(100) NOT NULL,
    fecha_prestamo             DATE         NOT NULL,
    fecha_prevista_devolucion  DATE         NOT NULL,
    fecha_devolucion_real      DATE,
    estado                     VARCHAR(20)  NOT NULL CHECK (estado IN ('ACTIVO', 'DEVUELTO')),
    CONSTRAINT ck_fechas CHECK (fecha_prevista_devolucion >= fecha_prestamo)
);

CREATE INDEX idx_prestamos_equipo_estado ON prestamos (equipo_id, estado);

