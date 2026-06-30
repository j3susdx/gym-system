package tests;

import base.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.CrearSocioPage;
import pages.DashboardPage;
import pages.LoginPage;
import pages.SociosPage;
import utils.ReportExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ReportExtension.class)
public class RegistrarSocioTest extends BaseTest {

    @Test
    public void registrarNuevoSocioYValidarEnListado() {
        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.abrir(baseUrl);
        loginPage.iniciarSesion(userEmail, userPassword);

        DashboardPage dashboardPage = new DashboardPage(driver, wait);
        assertTrue(dashboardPage.estaVisible(), "El usuario no ingresó correctamente al sistema.");

        long tiempo = System.currentTimeMillis();
        String nombre = "Socio Selenium " + tiempo;
        String dni = "9" + String.valueOf(tiempo).substring(String.valueOf(tiempo).length() - 7);
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
}
