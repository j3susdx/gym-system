package pages;

import org.openqa.selenium.By;
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
                String url = driverActual.getCurrentUrl();
                String texto = driverActual.findElement(By.tagName("body")).getText();

                return url.contains("/home")
                        || texto.contains("Panel de Control")
                        || texto.contains("Bienvenido")
                        || texto.contains("Próximos Vencimientos")
                        || texto.contains("Dashboard");
            });
        } catch (TimeoutException e) {
            return false;
        }
    }
}
