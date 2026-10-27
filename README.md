# TecnoUAM

Sistema de préstamo de equipos tecnológicos para el examen integrador de
Servicios Web (segundo corte).

El proyecto se divide en un backend Spring Boot con API REST, persistencia
relacional y seguridad JWT, y un frontend React que consume la API real.

## Equipo y responsabilidades

| Integrante | Responsabilidad principal | Rama sugerida |
| --- | --- | --- |
| Fernando Reyes | Base del proyecto, integración, pruebas y evidencias | `feature/fernando-integracion` |
| Jorddy Siezar | Entidades, relación JPA, migraciones y datos | `feature/jorddy-modelo-datos` |
| Max Martinez | CRUD, servicios, DTOs y reglas de negocio | `feature/max-api-negocio` |
| Emilio Carranza | Spring Security, JWT y roles | `feature/emilio-seguridad` |
| Victor Cabrera | React, Axios y visualización | `feature/victor-frontend` |

Las responsabilidades son el punto de partida: todos deben conocer el flujo
completo para la defensa técnica.

## Requisitos locales

- Java 21 o superior
- Maven 3.9 o superior
- Node.js 20 o superior y npm
- Docker Desktop

## Inicio rápido

1. Copiar las variables de entorno de ejemplo:

   ```bash
   cp .env.example .env
   ```

2. Levantar PostgreSQL:

   ```bash
   docker compose up -d postgres
   ```

3. Iniciar el backend:

   ```bash
   cd backend
   mvn spring-boot:run
   ```

4. En otra terminal, instalar dependencias e iniciar React:

   ```bash
   cd frontend
   npm install
   npm run dev
   ```

El frontend queda en `http://localhost:5173` y el backend en
`http://localhost:8080`.

## Variables de entorno

El archivo `.env.example` documenta las variables necesarias. Nunca se deben
subir `.env`, contraseñas reales ni secretos JWT al repositorio.

## Flujo de ramas y commits

Cada integrante trabaja en su rama y abre un pull request hacia `develop`.
Los commits deben ser pequeños, verificables y seguir esta forma:

```text
tipo(alcance): descripción breve en imperativo
```

Ejemplos: `feat(equipos): agrega validación de código patrimonial` o
`test(api): cubre conflicto al prestar equipo ocupado`.

Antes de abrir un pull request:

```bash
git fetch origin
git rebase origin/develop
git status
```

## Estado inicial

La base actual contiene configuración compartida, PostgreSQL reproducible,
esqueleto ejecutable de Spring Boot, shell visual de React y documentación de
trabajo. Las funcionalidades evaluadas se incorporan en las ramas asignadas.

## Endpoints objetivo

| Método | Ruta | Acceso |
| --- | --- | --- |
| `POST` | `/api/auth/login` | Público |
| `GET` | `/api/equipos` | `USER`, `ADMIN` |
| `GET` | `/api/equipos/{id}` | `USER`, `ADMIN` |
| `POST` | `/api/equipos` | `ADMIN` |
| `PUT` | `/api/equipos/{id}` | `ADMIN` |
| `DELETE` | `/api/equipos/{id}` | `ADMIN` |
| `GET` | `/api/prestamos` | `USER`, `ADMIN` |
| `POST` | `/api/prestamos` | `ADMIN` |
| `PUT` | `/api/prestamos/{id}/devolver` | `ADMIN` |

## Criterios de aceptación

- Las respuestas usan `201`, `200`, `204`, `400`, `401`, `403`, `404` y `409`
  según corresponda.
- Un equipo prestado no puede prestarse de nuevo.
- Devolver un préstamo libera el equipo.
- Un equipo con préstamos activos no puede eliminarse.
- React consulta datos de la API con un JWT real y no usa datos simulados.
- Postman incluye los casos positivos y negativos exigidos por el examen.

