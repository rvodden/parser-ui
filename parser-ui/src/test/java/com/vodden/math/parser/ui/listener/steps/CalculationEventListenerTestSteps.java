package com.vodden.math.parser.ui.listener.steps;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.vodden.math.parser.acceptance.steps.ParserSteps;
import com.vodden.math.parser.client.ParserClient;
import com.vodden.math.parser.client.domain.ParseRequest;
import com.vodden.math.parser.client.domain.ParseResponse;
import com.vodden.math.parser.ui.CalculationBean;
import com.vodden.math.parser.ui.CalculationEventListener;
import com.vodden.math.parser.ui.ContextMocker;

import net.serenitybdd.core.Serenity;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CalculationEventListenerTestSteps implements ParserSteps {

	@Override
	@Given("I visit the webpage")
	public void theUserVisitsTheWebpage() {
		CalculationBean calculationBean = new CalculationBean();
		Serenity.getCurrentSession().put("calculationBean", calculationBean);
	}

	@Override
	@When("I submit the expression (.*)")
	public void theySubmitTheExpression(String expression) {
		CalculationBean calculationBean = (CalculationBean) Serenity.getCurrentSession().get("calculationBean");
		
		// create a session which returns a genuine calculationBean
		Map<String, Object> session = new HashMap<String, Object>();
		calculationBean.setExpression(expression);
		session.put("calculationBean", calculationBean);
		
		// mock out the more fiddly bits of the FacesContext
		ExternalContext externalContext = mock(ExternalContext.class);
		when(externalContext.getSessionMap()).thenReturn(session);
		
		FacesContext facesContext = ContextMocker.mockFacesContext();
		when(facesContext.getExternalContext()).thenReturn(externalContext);
		
	}

	@Override
	@Then("the result should be (\\d+)")
	public void theResultShouldBe(Double value) {
		CalculationBean calculationBean = (CalculationBean) Serenity.getCurrentSession().get("calculationBean");
		
		// Create a genuine parser response with the appropriate value
		ParseResponse parseResponse = new ParseResponse();
		parseResponse.setResult(value);
		
		// Mock out the parser client so it returns the genuine response (IOException will never happen here)
		ParserClient parserClient = mock(ParserClient.class);
		try {
			when(parserClient.calculate(any(ParseRequest.class))).thenReturn(parseResponse);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// create a new CalculationEventListener and give it the parserClient we just mocked.
		CalculationEventListener calculationEventListener = new CalculationEventListener(parserClient);
		
		// Spin up a mock ActionEvent and poke it at our CalculationEventListener
		ActionEvent event = mock(ActionEvent.class);
		calculationEventListener.processAction(event);
		
		// Check it got the answer right
		assertThat(calculationBean.getResult(),is(equalTo(value)));
		
	}

}
