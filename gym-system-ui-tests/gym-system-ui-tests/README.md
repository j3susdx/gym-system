# Gym System UI Tests - Semana 12

Proyecto separado para aplicar la **Semana 12: Automatización de Pruebas de Interfaz (UI) con Selenium WebDriver y Page Object Model (POM)** al sistema Gym System.

## Flujo automatizado principal

`Login -> Registrar socio -> Validar socio en listado`

Este flujo valida una función central del sistema: la gestión de socios.

## Estructura del proyecto

```text
gym-system-ui-tests/
├── pom.xml
├── src/test/java/base/BaseTest.java
├── src/test/java/pages/BasePage.java
├── src/test/java/pages/LoginPage.java
├── src/test/java/pages/DashboardPage.java
├── src/test/java/pages/CrearSocioPage.java
├── src/test/java/pages/SociosPage.java
├── src/test/java/tests/LoginTest.java
├── src/test/java/tests/RegistrarSocioTest.java
└── src/test/java/utils/ReportExtension.java
```

## Requisitos

- Java JDK 17 o superior.
- Maven instalado.
- Google Chrome instalado.
- El sistema `gym-system` debe estar levantado y funcionando.

## Datos por defecto

El proyecto usa estos datos por defecto:

```text
URL: http://localhost
Usuario: recepcion@gym.com
Clave: 123456
```

Si usas otros datos, pásalos por consola con `-D`.

## Ejecutar las pruebas

Desde la carpeta del proyecto:

```bash
mvn test
```

Si tu sistema usa otra URL:

```bash
mvn test -DbaseUrl=http://localhost:8080
```

Si tienes otro usuario:

```bash
mvn test -DbaseUrl=http://localhost -DuserEmail=admin@irongym.com -DuserPassword=123456
```

Para ejecutar en modo oculto, sin abrir el navegador visualmente:

```bash
mvn test -Dheadless=true
```

## Reportes generados

Después de ejecutar, Maven genera reportes en:

```text
target/surefire-reports
```

También se genera un reporte simple Pass/Fail en:

```text
target/reporte-ui/reporte-resultados.txt
```

Y capturas finales de evidencia en:

```text
target/evidencias
```

## Capturas sugeridas para tu informe

1. Sistema Gym System abierto en el login.
2. Código del proyecto con carpetas `pages`, `tests`, `base` y `utils`.
3. Archivo `pom.xml` con Selenium y WebDriverManager.
4. Ejecución de `mvn test` en consola.
5. Resultado Pass/Fail en `target/reporte-ui/reporte-resultados.txt`.
6. Capturas generadas en `target/evidencias`.

## Nota sobre selectores

El proyecto intenta usar primero selectores por `id` y luego por `name` como respaldo.

Para cumplir mejor con la guía, se recomienda agregar IDs estables en los formularios principales del sistema original. Revisa el archivo:

```text
docs/cambios_recomendados_en_gym_system.md


Al ejecutar el proyecto de automatización con Maven, se generó la carpeta target, donde se almacenan los reportes de ejecución, evidencias y resultados de las pruebas automatizadas. Esto demuestra que el sistema fue validado mediante Selenium WebDriver aplicando el patrón Page Object Model.

Ahora abre reporte-resultados.txt y dime si te aparece PASS o FAIL.
```
