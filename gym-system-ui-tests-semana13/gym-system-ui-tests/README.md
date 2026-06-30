# Gym System UI Tests - Semana 13

Proyecto de pruebas UI con Selenium WebDriver, Maven y Page Object Model para el sistema Gym System.

## Pruebas incluidas

- Login correcto.
- Login incorrecto.
- Registro correcto de socio.
- DNI obligatorio.
- DNI con exactamente 8 números.
- Teléfono con exactamente 9 números.
- Correo inválido rechazado.
- Correo vacío permitido por ser opcional.

## Ejecución local

Desde esta carpeta:

```bash
mvn clean test -DbaseUrl=http://127.0.0.1:8000 -Dheadless=false
```

En GitHub Actions:

```bash
mvn clean test -DbaseUrl=http://127.0.0.1:8000 -Dheadless=true
```

## Reportes

- `target/surefire-reports/`
- `target/reporte-ui/reporte-resultados.txt`
- `target/evidencias/`
