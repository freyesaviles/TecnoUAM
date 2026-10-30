# Guía de pruebas y evidencias

Este documento contiene el procedimiento detallado para que el profesor pueda
repetir la demostración y el resumen de las pruebas realizadas por el equipo.

## Preparar el entorno

1. Iniciar PostgreSQL local y crear una base de datos llamada `tecnouam`.
2. Configurar `.env` con:

   ```text
   DATABASE_URL=jdbc:postgresql://localhost:5432/tecnouam
   DATABASE_USERNAME=<usuario-postgresql>
   DATABASE_PASSWORD=<contraseña-postgresql>
   JWT_SECRET=<secreto-de-64-o-mas-caracteres>
   JWT_EXPIRATION_MINUTES=120
   ```

3. Iniciar el backend:

   ```bash
   cd backend
   mvn spring-boot:run
   ```

4. Iniciar React en otra terminal:

   ```bash
   cd frontend
   npm install
   npm run dev
   ```

5. Verificar `GET http://localhost:8080/api/health`. La respuesta esperada es:

   ```json
   {"status":"UP","service":"tecnouam-backend"}
   ```

## Demostración en React

Abrir `http://localhost:5173`.

| Cuenta | Verificación |
| --- | --- |
| `admin` / `admin123` | La cabecera muestra `admin · ADMIN` y se carga el inventario real. |
| `user` / `user123` | La cabecera muestra `user · USER` y puede consultar el inventario. |

Cerrar sesión entre ambas pruebas. La diferencia de permisos se demuestra en
Postman, porque la vista React se concentra en consultar y mostrar equipos.

## Demostración con Postman

Importar `postman/TecnoUAM.postman_collection.json` y ejecutar las carpetas en
este orden:

1. **01 - Autenticación**
   - Login ADMIN: `200`, token y `ROLE_ADMIN`.
   - Login USER: `200`, token y `ROLE_USER`.
   - Login incorrecto: `401`.
2. **02 - Seguridad**
   - Consulta sin token: `401`.
   - Consulta como USER: `200`.
   - Crear equipo como USER: `403`.
   - Registrar préstamo como USER: `403`.
3. **03 - Equipos**
   - Crear equipos como ADMIN: `201`.
   - Consultar y actualizar equipos: `200`.
4. **04 - Préstamos y devolución**
   - Registrar préstamo: `201`.
   - Intentar un segundo préstamo del mismo equipo: `409`.
   - Intentar eliminar el equipo prestado: `409`.
   - Consultar préstamos: `200`.
   - Devolver préstamo: `200` y estado `DEVUELTO`.
   - Consultar equipo liberado: `200` y estado `DISPONIBLE`.
   - Eliminar equipo liberado: `204`.

La colección genera códigos patrimoniales y fechas de prueba automáticamente,
y guarda los tokens en variables de colección. No se deben publicar los JWT en
capturas ni en commits.

## Pruebas equivalentes con cURL

Login:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"admin","password":"admin123"}'
```

Consulta autenticada, reemplazando `<TOKEN_ADMIN>` por el token recibido:

```bash
curl http://localhost:8080/api/equipos \
  -H 'Authorization: Bearer <TOKEN_ADMIN>'
```

Para comprobar `403`, repetir una operación de escritura con el token de
`user`.

## Pruebas automatizadas propias

Ejecutadas desde `backend/` contra PostgreSQL local:

```bash
mvn test
```

Resultado:

```text
Tests run: 14, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

La suite cubre generación y validación JWT, persistencia, migraciones Flyway,
restricciones de préstamo, eliminación y la regresión de logins consecutivos.

También se ejecutó un flujo HTTP de 17 casos:

| Grupo | Casos verificados |
| --- | ---: |
| Health, autenticación y token | 6 |
| Permisos por rol | 4 |
| Creación y préstamo | 3 |
| Conflictos y devolución | 4 |
| **Total** | **17** |

Todos pasaron con los códigos esperados: `200`, `201`, `204`, `401`, `403` y
`409`. Los registros creados por el flujo de evidencia se devolvieron y se
eliminaron al finalizar.

## Evidencias visuales

Las capturas corresponden a la aplicación funcionando en `localhost`, con la
extensión de modo oscuro desactivada:

- [React autenticado como ADMIN](evidence/react-admin.png)
- [React autenticado como USER](evidence/react-user.png)
- [Respuesta `GET /api/health`](evidence/api-health.png)
