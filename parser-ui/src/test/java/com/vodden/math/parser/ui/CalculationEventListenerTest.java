package com.vodden.math.parser.ui;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.vodden.math.parser.client.ParserClient;
import com.vodden.math.parser.client.domain.ParseRequest;
import com.vodden.math.parser.client.domain.ParseResponse;

@RunWith(MockitoJUnitRunner.class)
public class CalculationEventListenerTest {
	
	@Mock
	private ActionEvent event;

	
	@Test
	public void EventListenerSetsResult() throws ClientHandlerException, UniformInterfaceException, IOException {
		FacesContext context = ContextMocker.mockFacesContext();
		
		try {
			
			Map<String, Object> session = new HashMap<String, Object>();
			CalculationBean calculationBean = new CalculationBean();
			calculationBean.setExpression("35");
			session.put("calculationBean", calculationBean);
			
			ExternalContext ext = mock(ExternalContext.class);
			when(ext.getSessionMap()).thenReturn(session);
			when(context.getExternalContext()).thenReturn(ext);

			ParseResponse parseResponse = new ParseResponse();
			parseResponse.setResult(35.0d);
			
			ParserClient parserClient = mock(ParserClient.class);
			when(parserClient.calculate(any(ParseRequest.class))).thenReturn(parseResponse);

			CalculationEventListener calculationEventListener = new CalculationEventListener(parserClient);
			calculationEventListener.processAction(event);
			
			assertEquals(35.0d,calculationBean.getResult(),0.01);
			
		} finally {
			context.release();
		}
		
	}

}
