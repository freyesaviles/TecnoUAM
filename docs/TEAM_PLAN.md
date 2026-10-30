# Plan de trabajo del equipo

## Orden recomendado

1. **Fernando** mantiene la base, variables, configuración PostgreSQL local,
   documentación y la rama
   de integración.
2. **Jorddy** agrega el modelo persistente y migraciones reproducibles.
3. **Max** implementa API, DTOs, validaciones y reglas de préstamo.
4. **Emilio** protege la API y agrega cuentas de prueba con JWT.
5. **Victor** conecta React a los endpoints protegidos.
6. **Fernando** arma colección Postman, pruebas funcionales, capturas y
   checklist de defensa.

## Dependencias entre tareas

```text
Base del proyecto
      |
      +--> Modelo JPA y base de datos
                 |
                 +--> Servicios y controladores
                              |
                              +--> Seguridad JWT
                                           |
                                           +--> React y Postman
```

Las ramas pueden crearse desde `main` y abrir pull requests hacia `main` en ese
orden.
Si alguien necesita avanzar antes, puede coordinar contratos de DTO y rutas en
un documento para evitar conflictos.

## Contratos que deben acordarse temprano

- `Equipo`: `id`, `codigoPatrimonial`, `nombre`, `tipo`, `estado`.
- `Prestamo`: `id`, `equipo`, `nombreSolicitante`, `fechaPrestamo`,
  `fechaPrevistaDevolucion`, `estado`.
- Estados: `DISPONIBLE`/`PRESTADO` y `ACTIVO`/`DEVUELTO`.
- Error JSON consistente para validación, recurso inexistente y conflicto.
- Login con `username`, `password` y respuesta con `token` y `roles`.

## Reunión de integración

Antes de cada merge, el integrante explica al menos un caso que el docente
podría preguntar. La defensa debe demostrar aportes identificables de los cinco
y comprensión del sistema completo.
