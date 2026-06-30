package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    protected String baseUrl;
    protected String userEmail;
    protected String userPassword;

    @BeforeEach
    public void setUp() {
        baseUrl = System.getProperty("baseUrl", "http://localhost");
        userEmail = System.getProperty("userEmail", "recepcion@gym.com");
        userPassword = System.getProperty("userPassword", "123456");

        long waitSeconds = Long.parseLong(System.getProperty("waitSeconds", "12"));
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1366,768");

        if (headless) {
            options.addArguments("--headless=new");
        }

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        wait = new WebDriverWait(driver, Duration.ofSeconds(waitSeconds));
    }

    @AfterEach
    public void tearDown(TestInfo testInfo) {
        guardarCapturaFinal(testInfo);

        if (driver != null) {
            driver.quit();
        }
    }

    private void guardarCapturaFinal(TestInfo testInfo) {
        if (driver == null) {
            return;
        }

        try {
            Files.createDirectories(Path.of("target", "evidencias"));
            File captura = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String nombreArchivo = testInfo.getDisplayName()
                    .replaceAll("[^a-zA-Z0-9-_]", "_") + ".png";

            Files.copy(
                    captura.toPath(),
                    Path.of("target", "evidencias", nombreArchivo),
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException | RuntimeException e) {
            System.out.println("No se pudo guardar la captura final: " + e.getMessage());
        }
    }
}
