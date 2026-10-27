# Contribuir a TecnoUAM

## Antes de comenzar

1. Clona el repositorio y configura `.env` a partir de `.env.example`.
2. Crea una rama con el nombre de tu responsabilidad.
3. No trabajes directamente sobre `main` ni `develop`.

## Commits

Usamos Conventional Commits en español y en modo imperativo:

```text
feat(modulo): agrega una capacidad
fix(modulo): corrige un comportamiento
test(modulo): agrega cobertura
docs(modulo): actualiza instrucciones
chore(modulo): ajusta configuración
```

Cada commit debe compilar o dejar claramente documentado qué falta para que
compile. No mezcles formateo masivo con cambios funcionales.

## Pull requests

El pull request debe incluir:

- resumen del cambio;
- pruebas ejecutadas y resultado;
- endpoint o pantalla afectada;
- evidencia visual cuando aplique;
- riesgos o pasos pendientes.

Antes de solicitar revisión, actualiza tu rama con `develop` y confirma que no
se incluyan secretos ni archivos generados.

