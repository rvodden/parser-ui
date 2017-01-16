package com.vodden.math.parser.acceptance.pages.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.logging.Logger;

import javax.inject.Inject;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.vodden.math.parser.acceptance.pages.Home;
import com.vodden.math.parser.client.ParserRestClient;
import com.vodden.math.parser.client.domain.ParseRequest;
import com.vodden.math.parser.client.domain.ParseResponse;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class RestClientHome implements Home {

	private static final Logger LOGGER = Logger.getLogger("RestClientHome.class");

	private ParseRequest parseRequest;

	private WireMockServer wireMockServer;

	@Inject
	private RestClientHome(WireMockServer wireMockServer) {
		this.wireMockServer = wireMockServer;
	}

	@Override
	public void open() {

	}

	@Override
	public void calculate(String expression) {
		parseRequest = new ParseRequest();
		parseRequest.setExpression(expression);
	}

	@Override
	public Boolean checkResult(Double value) {
		
		if (!wireMockServer.isRunning()) {
			wireMockServer.start();
			LOGGER.info("WireMock Server started on port: " + wireMockServer.port());
			
		};
		
		String urlValue = "";
		
		try {
			urlValue = URLEncoder.encode(
				    value.toString(),
				    java.nio.charset.StandardCharsets.UTF_8.toString()
				  );
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			assert(false);
		}
		
		ParserRestClient parserRestClient = new ParserRestClient("http://localhost:" + wireMockServer.port() + "/parser/parser/" + urlValue);
		ParseResponse parseResponse;
		
		WireMock wireMock = new WireMock(wireMockServer.port());
		wireMock.register(post(urlEqualTo("/parser/parser/" + urlValue))
	            .willReturn(aResponse()
	                .withHeader("Content-Type", "application/json")
	                .withBody("{ \"result\": \"" + value + "\"}")));
		
		try { // IOException will never happen here
			parseResponse = parserRestClient.calculate(parseRequest);
			Double result = parseResponse.getResult();
			LOGGER.info("Got result : " + result.toString()); 
			return BigDecimal.valueOf(value).equals(BigDecimal.valueOf(result));
		} catch (IOException e) {
			e.printStackTrace();
			assert(false);
		}
		
		return false;
	}

}
