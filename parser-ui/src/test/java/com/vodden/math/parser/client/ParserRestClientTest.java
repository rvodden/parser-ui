package com.vodden.math.parser.client;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features", glue = "com.vodden.math.parser.acceptance.steps")
public class ParserRestClientTest {

}
