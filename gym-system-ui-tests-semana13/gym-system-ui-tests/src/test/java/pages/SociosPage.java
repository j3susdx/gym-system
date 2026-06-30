package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SociosPage extends BasePage {

    private final By tablaSociosId = By.id("tabla-socios");
    private final By tablaSociosCss = By.cssSelector(".table-data");
    private final By cualquierTabla = By.tagName("table");

    public SociosPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void abrir(String baseUrl) {
        driver.get(baseUrl + "/socios/index");
        visible(tablaSociosId, tablaSociosCss, cualquierTabla);
    }

    public boolean contieneSocio(String nombre, String dni) {
        try {
            return wait.until(driverActual -> {
                String textoTabla = visible(tablaSociosId, tablaSociosCss, cualquierTabla).getText();
                return textoTabla.contains(nombre) && textoTabla.contains(dni);
            });
        } catch (TimeoutException e) {
            return false;
        }
    }
}
