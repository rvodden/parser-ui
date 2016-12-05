package com.vodden.math.parser.ui;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import com.vodden.math.parser.client.ParserClient;
import com.vodden.math.parser.client.ParserRestClient;
import com.vodden.math.parser.client.domain.ParseRequest;
import com.vodden.math.parser.client.domain.ParseResponse;

public class CalculationEventListener implements ActionListener {
	
	Logger logger = Logger.getLogger(CalculationEventListener.class.getName());
	
	private ParserClient parserClient;
	
	public CalculationEventListener(ParserClient parserClient) {
		this.parserClient = parserClient;
	}
	
	public CalculationEventListener() {
		this(new ParserRestClient());
	}
	
	@Override
	public void processAction(ActionEvent event) {
		ParseRequest parseRequest;
		ParseResponse parseResponse;
		
		CalculationBean calculationBean = (CalculationBean) FacesContext.getCurrentInstance().
		         getExternalContext().getSessionMap().get("calculationBean");
		
		parseRequest = new ParseRequest();
		parseRequest.setExpression(calculationBean.getExpression());
		
		try {
			parseResponse = parserClient.calculate(parseRequest);
			calculationBean.setResult(parseResponse.getResult());
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Failure whilst processing calculation request", e);
		}	
		
	}

}
