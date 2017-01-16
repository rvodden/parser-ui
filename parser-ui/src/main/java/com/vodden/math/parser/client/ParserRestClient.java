package com.vodden.math.parser.client;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.vodden.math.parser.client.domain.ParseRequest;
import com.vodden.math.parser.client.domain.ParseResponse;

public class ParserRestClient implements ParserClient {
	
	private static final String DEFAULT_PARSER_URL = "http://localhost:8080/parser/parser";
	private String parserUrl;
	
	private ObjectMapper objectMapper;
	private Client client;
	
	public ParserRestClient(ObjectMapper objectMapper, Client client, String parserUrl) {
		this.objectMapper = objectMapper;
		this.client = client;
		this.parserUrl = parserUrl;
	}
	
	public ParserRestClient( Client client ) {
		this(new ObjectMapper(), client, DEFAULT_PARSER_URL);
	}
	
	public ParserRestClient(String parserUrl) {
		this(new ObjectMapper(), Client.create(), parserUrl);
	}
	
	public ParserRestClient() {
		this(new ObjectMapper(), Client.create(), DEFAULT_PARSER_URL);
	}	

	@Override
	public ParseResponse calculate(ParseRequest parseRequest) throws IOException {
		
		WebResource webResource = client.resource(parserUrl);
		
		String requestJSON = objectMapper.writeValueAsString(parseRequest);
		
		ClientResponse clientResponse = webResource.accept(MediaType.APPLICATION_JSON_TYPE)
				.type(MediaType.APPLICATION_JSON_TYPE)
                .post(ClientResponse.class,requestJSON);
		
		if (clientResponse.getStatus() != 200) {
			   throw new RuntimeException("Failed : HTTP error code : "
				+ clientResponse.getStatus());
		}
		
		ParseResponse parseResponse;
		
		parseResponse = objectMapper.readValue(clientResponse.getEntity(String.class),ParseResponse.class);
		
		return parseResponse;
	}
}
