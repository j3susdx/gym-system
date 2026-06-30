package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DashboardPage extends BasePage {

    public DashboardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public boolean estaVisible() {
        try {
            return wait.until(driverActual -> {
                try {
                    String url = driverActual.getCurrentUrl();
                    String texto = driverActual.findElement(By.tagName("body")).getText();

                    return url.contains("/home")
                            || url.contains("/dashboard")
                            || texto.contains("Panel de Control")
                            || texto.contains("Bienvenido")
                            || texto.contains("Próximos Vencimientos")
                            || texto.contains("Dashboard")
                            || texto.contains("Socios");
                } catch (StaleElementReferenceException e) {
                    return false;
                }
            });
        } catch (TimeoutException e) {
            return false;
        }
    }
}