package com.vodden.math.parser.client;

import java.io.IOException;

import com.vodden.math.parser.client.domain.ParseRequest;
import com.vodden.math.parser.client.domain.ParseResponse;

public interface ParserClient {
	
	public ParseResponse calculate(ParseRequest parseRequest) throws IOException ;
	
}
