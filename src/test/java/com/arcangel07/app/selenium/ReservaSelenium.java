package com.arcangel07.app.selenium;

import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ReservaSelenium {

    private int segundosAEsperar = 10;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private Double total;
    private Double minValue;

    @Test
    public void reserva() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(options);
        driver.navigate().to("https://www.vivaair.com");

        this.cancelarAlerta(driver);
        WebElement quitarAlerta = driver.findElement(By.id("onesignal-slidedown-cancel-button"));
        WebElement checkIda = driver.findElement(By.className("box"));
        WebElement origen = driver.findElement(By.id("criteria-airport-select-departure-input"));
        WebElement destino = driver.findElement(By.id("criteria-airport-select-destination-input"));
        WebElement buscarVuelo = driver.findElement(By.id("criteria-search-button-desktop"));

        quitarAlerta.click();
        checkIda.click();
        origen.clear();
        origen.sendKeys("Medellin", Keys.ENTER);
        destino.sendKeys("Bogota", Keys.ENTER);
        LocalDateTime fechaActual = LocalDateTime.now();
        LocalDateTime diaDespues = fechaActual.plusDays(1);
        String fechaFormat = diaDespues.format(dtf);

        WebElement fecha = driver.findElement(By.xpath("//*[@id=\"month-2021-03-01\"]/table/tbody/tr[4]/td[4]/button"));
        fecha.sendKeys(fechaFormat, Keys.ENTER);

        buscarVuelo.click();

        // Llamada a los demas metodos
        elegirMenorPrecio(driver);
        getValorTotal(driver);
    }

    private Double elegirMenorPrecio(WebDriver driver) {

        try {
            driver.manage().timeouts().implicitlyWait(segundosAEsperar , TimeUnit.SECONDS);
            List<WebElement> precios = driver.findElements((By.className("from-price")));

            List<Double> valores = new ArrayList<>(0);

            precios.forEach(i -> {
                valores.add(Double.parseDouble(i.getText().replaceAll("[COP, ,]", "")));
            });

            minValue = valores.stream().min(Comparator.naturalOrder()).get();

            precios.forEach(i -> {
                if (minValue == Double.parseDouble(i.getText().replaceAll("[COP, ,]", ""))) {
                    i.click();
                }
            });

        } finally {
            //driver.quit();
        }
        return minValue;
    }

    private Double getValorTotal(WebDriver driver) {
        try {
            driver.manage().timeouts().implicitlyWait(segundosAEsperar , TimeUnit.SECONDS);
            WebElement carta = driver.findElement(By.xpath("//*[@id=\"Vkh_NTU4MH4gfn5NREV_MDMvMjUvMjAyMSAxNzoyOX5CT0d_MDMvMjUvMjAyMSAxODoyNn5_\"]/div[2]/div/div/div[2]/div[1]/div/div[5]/div"));
            carta.click();

            WebElement continuar = driver.findElement(By.xpath("//*[@id=\"flights-scroll-to\"]/div[2]/button"));
            continuar.sendKeys(Keys.ENTER);

            WebElement cancel1 = driver.findElement(By.xpath("//*[@id=\"seats-modal-body\"]/div/div[5]/div[2]/div[3]/div[1]/button"));
            driver.manage().timeouts().implicitlyWait(segundosAEsperar , TimeUnit.SECONDS);
            cancel1.sendKeys(Keys.ENTER);

            driver.manage().timeouts().implicitlyWait(segundosAEsperar , TimeUnit.SECONDS);
            WebElement cancel2 = driver.findElement(By.xpath("//*[@id=\"app\"]/div[12]/div/div[2]/div[2]/button[1]"));
            cancel2.click();

            // Alertas
            WebElement alert = driver.findElement(By.xpath("//*[@id=\"services\"]/div[1]/div/div[2]/div/div[2]/div/div/div[3]/div[2]/button"));
            alert.sendKeys(Keys.ENTER);

            // Checks
            driver.manage().timeouts().implicitlyWait(segundosAEsperar , TimeUnit.SECONDS);
            WebElement check1 = driver.findElement(By.xpath("//*[@id=\"pass\"]/div[2]/div/div[1]/div[2]/div[2]/div"));
            check1.click();

            WebElement check2 = driver.findElement(By.xpath("//*[@id=\"expressLine\"]/div[2]/div/div[1]/div[2]/div[2]/div"));
            check2.click();

            WebElement check3 = driver.findElement(By.xpath("//*[@id=\"cancelProtection\"]/div[2]/div/div[1]/div[2]/div[2]/div"));
            check3.click();

            WebElement check4 = driver.findElement(By.xpath("//*[@id=\"insurance-ssr\"]/div/div/div/label/span[1]"));
            check4.click();

            WebElement valorTotal = driver.findElement(By.xpath("//*[@id=\"basket-icon\"]/div[1]/div[2]/span"));
            String value = valorTotal.getText().replaceAll("[COP, Total:, ,]", "");
            total = Double.parseDouble(value);
        } finally {
           // driver.quit();
        }
        return total;
    }

    public void cancelarAlerta(WebDriver driver) {
        List<WebElement> alerta = driver.findElements(By.className("icon material-icons newsletter_popup_close"));
        if (alerta.size() != 0) {
            WebElement button_closeSubs = driver
                    .findElement(By.className("icon material-icons newsletter_popup_close"));
            button_closeSubs.click();
        }
    }

    public Double getMinValue() {
        return minValue;
    }

    public Double getTotal() {
        return total;
    }
}
