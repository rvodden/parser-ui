package com.vodden.math.parser.ui;

import org.junit.runner.RunWith;

import net.serenitybdd.cucumber.CucumberWithSerenity;

import cucumber.api.CucumberOptions;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(features = "classpath:features", glue = "com.vodden.math.parser.ui.listener.steps")
public class CalculationEventListenerTest {

}
