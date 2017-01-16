package com.vodden.math.parser.acceptance.pages.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.vodden.math.parser.acceptance.pages.Home;
import com.vodden.math.parser.client.ParserClient;
import com.vodden.math.parser.client.domain.ParseRequest;
import com.vodden.math.parser.client.domain.ParseResponse;
import com.vodden.math.parser.ui.CalculationBean;
import com.vodden.math.parser.ui.CalculationEventListener;
import com.vodden.math.parser.ui.ContextMocker;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CalculationListenerEventHome implements Home {

	private CalculationBean calculationBean;

	@Override
	public void open() {
		calculationBean = new CalculationBean();
	}

	@Override
	public void calculate(String expression) {
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
	public Boolean checkResult(Double value) {
		// Create a genuine parser response with the appropriate value
		ParseResponse parseResponse = new ParseResponse();
		parseResponse.setResult(value);

		// Mock out the parser client so it returns the genuine response
		// (IOException will never happen here)
		ParserClient parserClient = mock(ParserClient.class);
		try {
			when(parserClient.calculate(any(ParseRequest.class))).thenReturn(parseResponse);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// create a new CalculationEventListener and give it the parserClient we
		// just mocked.
		CalculationEventListener calculationEventListener = new CalculationEventListener(parserClient);

		// Spin up a mock ActionEvent and poke it at our
		// CalculationEventListener
		ActionEvent event = mock(ActionEvent.class);
		calculationEventListener.processAction(event);

		// Check it got the answer right
		return value.equals(calculationBean.getResult());
	}

}
