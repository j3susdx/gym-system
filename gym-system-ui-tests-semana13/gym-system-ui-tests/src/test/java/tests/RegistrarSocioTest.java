package tests;

import base.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.CrearSocioPage;
import pages.DashboardPage;
import pages.LoginPage;
import pages.SociosPage;
import utils.ReportExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ReportExtension.class)
public class RegistrarSocioTest extends BaseTest {

    private void iniciarSesion() {
        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.abrir(baseUrl);
        loginPage.iniciarSesion(userEmail, userPassword);

        DashboardPage dashboardPage = new DashboardPage(driver, wait);
        assertTrue(dashboardPage.estaVisible(), "El usuario no ingresó correctamente al sistema.");
    }

    private String dniUnico() {
        long tiempo = System.currentTimeMillis();
        return "9" + String.valueOf(tiempo).substring(String.valueOf(tiempo).length() - 7);
    }

    @Test
    public void registrarNuevoSocioConDatosValidosYValidarEnListado() {
        iniciarSesion();

        long tiempo = System.currentTimeMillis();
        String nombre = "Socio Selenium " + tiempo;
        String dni = dniUnico();
        String telefono = "987654321";
        String email = "socio.selenium." + tiempo + "@test.com";

        CrearSocioPage crearSocioPage = new CrearSocioPage(driver, wait);
        crearSocioPage.abrir(baseUrl);
        crearSocioPage.registrarSocio(nombre, dni, telefono, email, "activo");

        SociosPage sociosPage = new SociosPage(driver, wait);
        assertTrue(
                sociosPage.contieneSocio(nombre, dni),
                "El socio registrado no aparece en el listado de socios."
        );
    }

    @Test
    public void dniEsObligatorioEnRegistroSocio() {
        iniciarSesion();

        CrearSocioPage crearSocioPage = new CrearSocioPage(driver, wait);
        crearSocioPage.abrir(baseUrl);
        crearSocioPage.completarFormulario("Socio Sin DNI", "", "987654321", "sindni@test.com", "activo");
        crearSocioPage.guardarSinEsperarRedireccion();

        assertTrue(crearSocioPage.dniEsInvalido(), "El campo DNI debería ser obligatorio.");
        assertTrue(crearSocioPage.permaneceEnFormulario(), "El sistema no debe guardar un socio sin DNI.");
        assertFalse(crearSocioPage.mensajeValidacionDni().isBlank(), "Debe existir mensaje de validación para DNI.");
    }

    @Test
    public void dniDebeTenerExactamenteOchoNumeros() {
        iniciarSesion();

        CrearSocioPage crearSocioPage = new CrearSocioPage(driver, wait);
        crearSocioPage.abrir(baseUrl);
        crearSocioPage.completarFormulario("Socio DNI Corto", "1234567", "987654321", "dnicorto@test.com", "activo");
        crearSocioPage.guardarSinEsperarRedireccion();

        assertTrue(crearSocioPage.dniEsInvalido(), "El sistema debe rechazar DNI con menos de 8 números.");
        assertTrue(crearSocioPage.permaneceEnFormulario(), "El sistema no debe guardar un socio con DNI inválido.");
    }

    @Test
    public void telefonoDebeTenerExactamenteNueveNumeros() {
        iniciarSesion();

        CrearSocioPage crearSocioPage = new CrearSocioPage(driver, wait);
        crearSocioPage.abrir(baseUrl);
        crearSocioPage.completarFormulario("Socio Telefono Corto", dniUnico(), "98765432", "telefonocorto@test.com", "activo");
        crearSocioPage.guardarSinEsperarRedireccion();

        assertTrue(crearSocioPage.telefonoEsInvalido(), "El sistema debe rechazar teléfono con menos de 9 números.");
        assertTrue(crearSocioPage.permaneceEnFormulario(), "El sistema no debe guardar un socio con teléfono inválido.");
    }

    @Test
    public void correoInvalidoDebeMostrarValidacion() {
        iniciarSesion();

        CrearSocioPage crearSocioPage = new CrearSocioPage(driver, wait);
        crearSocioPage.abrir(baseUrl);
        crearSocioPage.completarFormulario("Socio Correo Malo", dniUnico(), "987654321", "correo-invalido", "activo");
        crearSocioPage.guardarSinEsperarRedireccion();

        assertTrue(crearSocioPage.correoEsInvalido(), "El sistema debe rechazar un correo con formato inválido.");
        assertTrue(crearSocioPage.permaneceEnFormulario(), "El sistema no debe guardar un socio con correo inválido.");
    }

    @Test
    public void correoOpcionalPermiteRegistrarSocio() {
        iniciarSesion();

        long tiempo = System.currentTimeMillis();
        String nombre = "Socio Sin Correo " + tiempo;
        String dni = dniUnico();
        String telefono = "987654321";

        CrearSocioPage crearSocioPage = new CrearSocioPage(driver, wait);
        crearSocioPage.abrir(baseUrl);
        crearSocioPage.completarFormulario(nombre, dni, telefono, "", "activo");

        assertTrue(crearSocioPage.correoEsValido(), "El correo vacío debe ser válido porque es opcional.");

        crearSocioPage.guardarSinEsperarRedireccion();

        SociosPage sociosPage = new SociosPage(driver, wait);
        assertTrue(
                sociosPage.contieneSocio(nombre, dni),
                "El socio sin correo debería registrarse correctamente porque el correo es opcional."
        );
    }
}
