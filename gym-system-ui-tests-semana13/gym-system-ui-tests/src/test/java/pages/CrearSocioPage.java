package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CrearSocioPage extends BasePage {

    private final By inputNombreId = By.id("nombre");
    private final By inputNombreName = By.name("nombre");

    private final By inputDniId = By.id("dni");
    private final By inputDniName = By.name("dni");

    private final By inputTelefonoId = By.id("telefono");
    private final By inputTelefonoName = By.name("telefono");

    private final By inputEmailSocioId = By.id("emailSocio");
    private final By inputEmailName = By.name("email");

    private final By selectEstadoId = By.id("estado");
    private final By selectEstadoName = By.name("estado");

    private final By botonGuardarId = By.id("btn-guardar-socio");
    private final By botonGuardarCss = By.cssSelector("button[type='submit']");

    public CrearSocioPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void abrir(String baseUrl) {
        driver.get(baseUrl + "/socios/crear");
        visible(inputNombreId, inputNombreName);
    }

    public void completarFormulario(String nombre, String dni, String telefono, String email, String estado) {
        escribir(nombre, inputNombreId, inputNombreName);
        escribir(dni, inputDniId, inputDniName);
        escribir(telefono, inputTelefonoId, inputTelefonoName);
        escribir(email, inputEmailSocioId, inputEmailName);
        seleccionarPorValor(estado, selectEstadoId, selectEstadoName);
    }

    public void guardarSinEsperarRedireccion() {
        clickSeguro(botonGuardarId, botonGuardarCss);
    }

    public void registrarSocio(String nombre, String dni, String telefono, String email, String estado) {
        completarFormulario(nombre, dni, telefono, email, estado);
        guardarSinEsperarRedireccion();

        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/socios/index"),
                ExpectedConditions.urlContains("/socios"),
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table")),
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".table"))
        ));
    }

    public boolean permaneceEnFormulario() {
        return driver.getCurrentUrl().contains("/socios/crear")
                || driver.findElement(By.tagName("body")).getText().contains("Registrar Nuevo Socio");
    }

    public boolean dniEsInvalido() {
        return !campoEsValido(inputDniId, inputDniName);
    }

    public boolean telefonoEsInvalido() {
        return !campoEsValido(inputTelefonoId, inputTelefonoName);
    }

    public boolean correoEsInvalido() {
        return !campoEsValido(inputEmailSocioId, inputEmailName);
    }

    public boolean correoEsValido() {
        return campoEsValido(inputEmailSocioId, inputEmailName);
    }

    public String mensajeValidacionDni() {
        return mensajeValidacion(inputDniId, inputDniName);
    }

    public String mensajeValidacionTelefono() {
        return mensajeValidacion(inputTelefonoId, inputTelefonoName);
    }

    public String mensajeValidacionCorreo() {
        return mensajeValidacion(inputEmailSocioId, inputEmailName);
    }
}