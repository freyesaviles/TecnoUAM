# TecnoUAM

Sistema web para administrar el inventario y el préstamo de equipos
tecnológicos de un centro de capacitación. El proyecto corresponde al examen
integrador de Servicios Web, segundo corte.

## Propósito

TecnoUAM demuestra un flujo completo de Servicios Web: una interfaz React
consume una API REST protegida con JWT, la API aplica las reglas de negocio y
PostgreSQL conserva el inventario y los préstamos.

El sistema permite consultar equipos, registrar y devolver préstamos, controlar
la disponibilidad y diferenciar permisos entre usuarios `USER` y `ADMIN`.

## Tecnologías

| Capa | Tecnología |
| --- | --- |
| Frontend | React, Vite y Axios |
| Backend | Java 21+, Spring Boot, Spring Data JPA |
| Seguridad | Spring Security y JWT |
| Base de datos | PostgreSQL, administrable con pgAdmin 4 |
| Migraciones | Flyway |
| Pruebas | JUnit, Maven, Postman y cURL |

## Arquitectura

```text
React (localhost:5173)
        |
        | Axios + Authorization: Bearer <JWT>
        v
Spring Boot REST API (localhost:8080)
        |
        | JPA/Hibernate + Flyway
        v
PostgreSQL local (pgAdmin)
```

El backend es la fuente de verdad. React consulta el inventario real mediante
`GET /api/equipos`; las operaciones y permisos se validan en la API.

## Responsabilidades del equipo

| Integrante | Área principal | Rama |
| --- | --- | --- |
| Fernando Reyes | Integración, configuración, README, Postman y evidencias | `feature/fernando-hardening` |
| Jorddy Siezar | Entidades, JPA, repositorios, migraciones y datos | `feature/jorddy-modelo-datos` |
| Max Martinez | DTOs, servicios, CRUD y reglas de negocio | `feature/max-api-negocio` |
| Emilio Carranza | Spring Security, JWT, autenticación y roles | `feature/emilio-seguridad` |
| Victor Cabrera | React, Axios, sesión y visualización | `feature/victor-frontend` |

Todos los integrantes deben conocer el flujo completo y explicar su aporte en
la defensa. El plan de trabajo ampliado está en
[`docs/TEAM_PLAN.md`](docs/TEAM_PLAN.md).

## Requisitos e instalación

- Java 21 o superior.
- Maven 3.9 o superior.
- Node.js 20 o superior y npm.
- PostgreSQL 15 o superior.
- pgAdmin 4 opcional para administrar la base de datos.

Docker no es necesario. PostgreSQL se ejecuta localmente y se configura con
`DATABASE_URL`, `DATABASE_USERNAME` y `DATABASE_PASSWORD`.

1. Crea una base de datos PostgreSQL llamada `tecnouam` desde pgAdmin o psql.
2. Copia y configura las variables de entorno:

   ```bash
   cp .env.example .env
   ```

3. Inicia el backend:

   ```bash
   cd backend
   mvn spring-boot:run
   ```

4. En otra terminal, inicia React:

   ```bash
   cd frontend
   npm install
   npm run dev
   ```

URLs locales:

- Frontend: `http://localhost:5173`
- Backend: `http://localhost:8080`
- Health: `http://localhost:8080/api/health`

Flyway crea y valida las tablas al iniciar el backend.

## Cuentas de demostración

| Usuario | Contraseña | Rol |
| --- | --- | --- |
| `admin` | `admin123` | `ADMIN` |
| `user` | `user123` | `USER` |

Como `ADMIN` se pueden crear, actualizar y eliminar equipos, registrar
préstamos y devolverlos. Como `USER` se puede consultar el inventario, pero las
operaciones administrativas responden `403 Forbidden`.

## Endpoints

Excepto health y login, las rutas requieren
`Authorization: Bearer <JWT>`.

| Método | Ruta | Acceso | Resultado principal |
| --- | --- | --- | --- |
| `GET` | `/api/health` | Público | Estado del backend (`200`). |
| `POST` | `/api/auth/login` | Público | JWT y roles (`200`). |
| `GET` | `/api/equipos` | `USER`, `ADMIN` | Lista de equipos (`200`). |
| `GET` | `/api/equipos/{id}` | `USER`, `ADMIN` | Equipo por ID (`200`). |
| `POST` | `/api/equipos` | `ADMIN` | Crear equipo (`201`). |
| `PUT` | `/api/equipos/{id}` | `ADMIN` | Actualizar equipo (`200`). |
| `DELETE` | `/api/equipos/{id}` | `ADMIN` | Eliminar equipo (`204`). |
| `GET` | `/api/prestamos` | `USER`, `ADMIN` | Lista de préstamos (`200`). |
| `POST` | `/api/prestamos` | `ADMIN` | Registrar préstamo (`201`). |
| `PUT` | `/api/prestamos/{id}/devolver` | `ADMIN` | Devolver y liberar equipo (`200`). |

Reglas principales: un equipo prestado no puede prestarse de nuevo, un equipo
con préstamo activo no puede eliminarse y devolver un préstamo lo deja
`DISPONIBLE`. Los errores de validación y negocio utilizan `400`, `401`, `403`,
`404` o `409`, según corresponda.

## Testing y demostración

Para la guía completa del profesor, cuerpos JSON, flujo de Postman, cURL y
resultados detallados, consulta [`docs/TESTING.md`](docs/TESTING.md).

Pruebas automatizadas ejecutadas:

```text
cd backend
mvn test
Tests run: 14, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

El flujo HTTP propio verificó 17 casos, incluyendo JWT, `401`, `403`, `201`,
`409`, devolución y eliminación de equipos.

## Evidencias

Las capturas fueron tomadas con la aplicación ejecutándose en `localhost`, sin
la extensión de modo oscuro y sin mostrar el editor T3 Code.

![React autenticado como ADMIN](docs/evidence/react-admin.png)

![React autenticado como USER](docs/evidence/react-user.png)

![Respuesta de health de la API](docs/evidence/api-health.png)

## Flujo de trabajo

Cada integrante trabaja en su rama, realiza commits pequeños y abre un pull
request hacia `main`. Formato recomendado:

```text
tipo(alcance): descripción breve en imperativo
```

No se deben subir `.env`, contraseñas reales ni secretos JWT.
