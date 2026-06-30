package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends BasePage {

    private final By inputEmailId = By.id("email");
    private final By inputEmailName = By.name("email");

    private final By inputPasswordId = By.id("password");
    private final By inputPasswordName = By.name("password");

    private final By botonLoginId = By.id("btn-login");
    private final By botonLoginCss = By.cssSelector("button[type='submit']");

    private final By alertaError = By.cssSelector(".alert-danger");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void abrir(String baseUrl) {
        driver.get(baseUrl + "/auth/index");
    }

    public void iniciarSesion(String correo, String password) {
        escribir(correo, inputEmailId, inputEmailName);
        escribir(password, inputPasswordId, inputPasswordName);
        clickeable(botonLoginId, botonLoginCss).click();
    }

    public boolean muestraMensajeError() {
        try {
            return visible(alertaError).isDisplayed();
        } catch (TimeoutException e) {
            String textoPagina = driver.findElement(By.tagName("body")).getText();
            return textoPagina.contains("incorrect") || textoPagina.contains("inhabilitada");
        }
    }
}
