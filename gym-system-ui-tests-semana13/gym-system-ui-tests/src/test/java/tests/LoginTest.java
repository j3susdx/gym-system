package tests;

import base.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.DashboardPage;
import pages.LoginPage;
import utils.ReportExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ReportExtension.class)
public class LoginTest extends BaseTest {

    @Test
    public void loginCorrectoPermiteEntrarAlDashboard() {
        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.abrir(baseUrl);
        loginPage.iniciarSesion(userEmail, userPassword);

        DashboardPage dashboardPage = new DashboardPage(driver, wait);
        assertTrue(dashboardPage.estaVisible(), "No se visualizó el dashboard después del login correcto.");
    }

    @Test
    public void loginIncorrectoMuestraMensajeDeError() {
        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.abrir(baseUrl);
        loginPage.iniciarSesion("usuario_incorrecto@test.com", "claveIncorrecta123");

        assertTrue(loginPage.muestraMensajeError(), "No se mostró el mensaje de error con credenciales incorrectas.");
    }
}
