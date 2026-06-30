package tests;

import base.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.CrearSocioPage;
import pages.DashboardPage;
import pages.LoginPage;
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
        String ultimos = String.valueOf(tiempo);
        return "9" + ultimos.substring(ultimos.length() - 7);
    }

    @Test
    public void dniEsObligatorioEnRegistroSocio() {
        iniciarSesion();

        CrearSocioPage crearSocioPage = new CrearSocioPage(driver, wait);
        crearSocioPage.abrir(baseUrl);

        crearSocioPage.completarFormulario(
                "Socio Sin Dni",
                "",
                "987654321",
                "sindni@test.com",
                "activo"
        );

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

        crearSocioPage.completarFormulario(
                "Socio Dni Corto",
                "1234567",
                "987654321",
                "dnicorto@test.com",
                "activo"
        );

        crearSocioPage.guardarSinEsperarRedireccion();

        assertTrue(crearSocioPage.dniEsInvalido(), "El sistema debe rechazar DNI con menos de 8 números.");
        assertTrue(crearSocioPage.permaneceEnFormulario(), "El sistema no debe guardar un socio con DNI inválido.");
    }

    @Test
    public void telefonoDebeTenerExactamenteNueveNumeros() {
        iniciarSesion();

        CrearSocioPage crearSocioPage = new CrearSocioPage(driver, wait);
        crearSocioPage.abrir(baseUrl);

        crearSocioPage.completarFormulario(
                "Socio Telefono Corto",
                dniUnico(),
                "98765432",
                "telefonocorto@test.com",
                "activo"
        );

        crearSocioPage.guardarSinEsperarRedireccion();

        assertTrue(crearSocioPage.telefonoEsInvalido(), "El sistema debe rechazar teléfono con menos de 9 números.");
        assertTrue(crearSocioPage.permaneceEnFormulario(), "El sistema no debe guardar un socio con teléfono inválido.");
    }
}