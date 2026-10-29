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
completo para la defensa técnica. Cada integrante debe realizar sus cambios en
su propia rama y dejar commits identificables con su nombre de usuario de Git.

### Fernando Reyes — coordinación, integración, pruebas y evidencias

Rama: `feature/fernando-integracion`

- mantener la configuración compartida, README y variables de entorno;
- integrar las ramas del equipo en `main` y preparar la entrega final;
- crear y mantener la colección Postman;
- ejecutar los casos funcionales positivos y negativos;
- documentar capturas, resultados y pasos de demostración;
- preparar el guion de defensa técnica y verificar que los cinco integrantes
  tengan aportes demostrables.

Archivos esperados: `README.md`, `CONTRIBUTING.md`, `docs/`,
`postman/` y configuración del proyecto.

### Jorddy Siezar — entidades, relación JPA y base de datos

Rama: `feature/jorddy-modelo-datos`

- implementar las entidades `Equipo` y `Prestamo`;
- definir enums, restricciones, relación entre ambas entidades y nombres de
  columnas;
- crear repositorios Spring Data JPA;
- agregar migraciones o script SQL reproducible;
- incluir datos mínimos para la demostración;
- cubrir con pruebas la persistencia y las restricciones principales.

Archivos esperados: `backend/src/main/java/.../equipo/`,
`backend/src/main/java/.../prestamo/` y `backend/src/main/resources/db/`.

### Max Martinez — API, servicios y reglas de negocio

Rama: `feature/max-api-negocio`

- crear DTOs de entrada y salida;
- implementar servicios y controladores de equipos y préstamos;
- completar el CRUD de equipos;
- validar campos obligatorios y fechas;
- impedir préstamos sobre equipos `PRESTADO`;
- cambiar el estado del equipo al prestar y devolver;
- impedir eliminar equipos con préstamos activos;
- devolver códigos HTTP `201`, `200`, `204`, `400`, `404` y `409` según el
  caso.

Archivos esperados: `backend/src/main/java/.../equipo/`,
`backend/src/main/java/.../prestamo/` y manejo global de errores.

### Emilio Carranza — autenticación, JWT y autorización

Rama: `feature/emilio-seguridad`

- implementar `POST /api/auth/login`;
- configurar autenticación sin sesiones y filtro JWT;
- definir usuarios o cuentas de prueba con roles `USER` y `ADMIN`;
- proteger rutas según el rol requerido;
- mantener el secreto JWT únicamente en variables de entorno;
- verificar los casos `401 Unauthorized` y `403 Forbidden`.

Archivos esperados: `backend/src/main/java/.../security/`, configuración de
usuarios y documentación de variables requeridas.

### Victor Cabrera — React, Axios y visualización

Rama: `feature/victor-frontend`

- construir componentes funcionales con `useState` y `useEffect`;
- configurar Axios y el envío del token válido;
- mostrar la tabla con código patrimonial, nombre, tipo y estado;
- implementar estados de carga, lista vacía y error;
- confirmar que la lista se actualiza al recargar después de crear o prestar
  equipos desde Postman;
- evitar datos simulados como evidencia de integración.

Archivos esperados: `frontend/src/`, configuración de Axios y documentación de
la ejecución del frontend.

### Dependencias entre responsabilidades

```text
Fernando: base y contratos
       |
Jorddy: modelo y base de datos
       |
Max: API y reglas de negocio
       |
Emilio: seguridad y JWT
       |
Victor: consumo real desde React
       |
Fernando: pruebas, evidencias e integración final
```

Cada integrante debe revisar al menos una tarea de otro compañero y explicar
su parte durante la defensa. La división organiza el trabajo, pero no limita
el conocimiento que debe tener el equipo completo.

## Requisitos locales

- Java 21 o superior
- Maven 3.9 o superior
- Node.js 20 o superior y npm
- PostgreSQL 15 o superior
- pgAdmin 4 (opcional, para administrar visualmente PostgreSQL)

## Inicio rápido

1. Copiar las variables de entorno de ejemplo:

   ```bash
   cp .env.example .env
   ```

2. Crear la base de datos desde pgAdmin:

   - Conéctate al servidor local `localhost:5432`.
   - Crea una base de datos llamada `tecnouam`.
   - Usa en `.env` las mismas credenciales de esa instalación.

   pgAdmin administra PostgreSQL; el servidor PostgreSQL debe estar instalado y
   ejecutándose en la computadora.

3. Iniciar el backend:

   ```bash
   cd backend
   mvn spring-boot:run
   ```

   Flyway crea las tablas y carga los datos demo de
   `backend/src/main/resources/db/migration/`.

4. En otra terminal, instalar dependencias e iniciar React:

   ```bash
   cd frontend
   npm install
   npm run dev
   ```

El frontend queda en `http://localhost:5173` y el backend en
`http://localhost:8080`.

## Probar el login en React

Abre `http://localhost:5173` después de iniciar backend y frontend.

### Como administrador

```text
Usuario: admin
Contraseña: admin123
```

La cabecera mostrará `admin · ADMIN`. El administrador tiene permisos para
crear, actualizar y eliminar equipos, registrar préstamos y devolverlos. Las
operaciones administrativas se demuestran en Postman.

### Como usuario

Cierra sesión y usa:

```text
Usuario: user
Contraseña: user123
```

La cabecera mostrará `user · USER`. Puede consultar la lista de equipos y
préstamos, pero el backend rechazará las operaciones administrativas con `403`.
React muestra la consulta protegida de equipos; Postman permite demostrar las
diferencias completas entre ambos roles.

## Probar con Postman

Importa `postman/TecnoUAM.postman_collection.json` y ejecuta las carpetas en
este orden:

1. `01 - Autenticación` obtiene y guarda automáticamente los tokens de
   `admin` y `user`.
2. `02 - Seguridad` comprueba `401`, `403` y consulta autenticada.
3. `03 - Equipos` crea, consulta y actualiza equipos.
4. `04 - Préstamos y devolución` prueba préstamo duplicado, eliminación
   bloqueada, devolución, liberación y eliminación permitida.

La colección genera códigos y fechas de prueba automáticamente. Sus variables
se almacenan en la colección, por lo que no hay que copiar manualmente los
tokens entre solicitudes.

## Variables de entorno

El archivo `.env.example` documenta las variables necesarias. Nunca se deben
subir `.env`, contraseñas reales ni secretos JWT al repositorio.

## Flujo de ramas y commits

Cada integrante trabaja en su rama y abre un pull request hacia `main`.
Los commits deben ser pequeños, verificables y seguir esta forma:

```text
tipo(alcance): descripción breve en imperativo
```

Ejemplos: `feat(equipos): agrega validación de código patrimonial` o
`test(api): cubre conflicto al prestar equipo ocupado`.

Antes de abrir un pull request:

```bash
git fetch origin
git rebase origin/main
git status
```

## Estado actual

La base actual contiene configuración compartida, migraciones Flyway para
PostgreSQL local, backend Spring Boot con JWT, frontend React autenticado,
colección Postman y documentación de pruebas.

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

## Cuentas demo

Las cuentas están pensadas únicamente para la demostración académica y se
mantienen en memoria:

| Usuario | Contraseña | Rol |
| --- | --- | --- |
| `admin` | `admin123` | `ADMIN` |
| `user` | `user123` | `USER` |

## Criterios de aceptación

- Las respuestas usan `201`, `200`, `204`, `400`, `401`, `403`, `404` y `409`
  según corresponda.
- Un equipo prestado no puede prestarse de nuevo.
- Devolver un préstamo libera el equipo.
- Un equipo con préstamos activos no puede eliminarse.
- React consulta datos de la API con un JWT real y no usa datos simulados.
- Postman incluye los casos positivos y negativos exigidos por el examen.
