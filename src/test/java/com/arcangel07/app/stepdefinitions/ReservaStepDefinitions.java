package com.arcangel07.app.stepdefinitions;

import com.arcangel07.app.selenium.ReservaSelenium;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class ReservaStepDefinitions {

    ReservaSelenium reservaSelenium = new ReservaSelenium();
    private Double valueMin;
    private Double valueTotal;

    @Given("^Quiero seleccionar un vuelo barato$")
    public void seleccionarVueloBarato() {
        reservaSelenium.reserva();
    }

    @When("^Comparar los valores$")
    public void compararValorMinimo() {
        valueMin = reservaSelenium.getMinValue();
        valueTotal = reservaSelenium.getTotal();
    }

    @Then("^Deberia tener de resultado si los valores son iguales$")
    public void verificarResultado() {
        Assert.assertEquals(valueMin, valueTotal);
    }
}
