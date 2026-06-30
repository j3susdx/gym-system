package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    protected WebElement visible(By... localizadores) {
        RuntimeException ultimaExcepcion = null;

        for (By localizador : localizadores) {
            try {
                return wait.until(ExpectedConditions.visibilityOfElementLocated(localizador));
            } catch (TimeoutException | StaleElementReferenceException e) {
                ultimaExcepcion = e;
            }
        }

        throw ultimaExcepcion;
    }

    protected WebElement clickeable(By... localizadores) {
        RuntimeException ultimaExcepcion = null;

        for (By localizador : localizadores) {
            try {
                return wait.until(ExpectedConditions.elementToBeClickable(localizador));
            } catch (TimeoutException | StaleElementReferenceException e) {
                ultimaExcepcion = e;
            }
        }

        throw ultimaExcepcion;
    }

    protected void escribir(String texto, By... localizadores) {
        WebElement elemento = visible(localizadores);
        moverAlElemento(elemento);
        elemento.clear();
        elemento.sendKeys(texto == null ? "" : texto);
    }

    protected void seleccionarPorValor(String valor, By... localizadores) {
        WebElement elemento = visible(localizadores);
        moverAlElemento(elemento);
        new Select(elemento).selectByValue(valor);
    }

    protected void clickSeguro(By... localizadores) {
        WebElement elemento = clickeable(localizadores);
        moverAlElemento(elemento);

        try {
            elemento.click();
        } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
            WebElement elementoNuevo = clickeable(localizadores);
            moverAlElemento(elementoNuevo);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", elementoNuevo);
        }
    }

    protected void moverAlElemento(WebElement elemento) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});",
                elemento
        );

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected boolean cuerpoContiene(String texto) {
        try {
            return wait.until(driverActual -> {
                try {
                    return driverActual.findElement(By.tagName("body")).getText().contains(texto);
                } catch (StaleElementReferenceException e) {
                    return false;
                }
            });
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected boolean campoEsValido(By... localizadores) {
        WebElement elemento = visible(localizadores);
        Object resultado = ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].checkValidity();", elemento);

        return Boolean.TRUE.equals(resultado);
    }

    protected String mensajeValidacion(By... localizadores) {
        WebElement elemento = visible(localizadores);
        Object resultado = ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].validationMessage;", elemento);

        return resultado == null ? "" : resultado.toString();
    }

    protected boolean urlContiene(String texto) {
        try {
            return wait.until(driverActual -> driverActual.getCurrentUrl().contains(texto));
        } catch (TimeoutException e) {
            return false;
        }
    }
}