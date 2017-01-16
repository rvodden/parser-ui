package com.vodden.math.parser.ui;

import javax.inject.Singleton;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.google.inject.AbstractModule;
import com.vodden.math.parser.acceptance.pages.Home;
import com.vodden.math.parser.acceptance.pages.impl.CalculationListenerEventHome;
import com.vodden.math.parser.acceptance.pages.impl.RestClientHome;
import com.vodden.math.parser.test.annotations.TestType;

public class ParserModule extends AbstractModule {
	@TestType("CalculationEventListener")
	TestType calculationEventListenerTestType;
	
	@TestType("RestClient")
	TestType restClientTestType;
	
	@Override
	protected void configure() {
		bind(WireMockServerFactory.class).to(WireMockServerFactoryImpl.class);
		bind(WireMockServer.class).toProvider(WireMockServerFactory.class).in(Singleton.class);
		
		try {
			calculationEventListenerTestType = this.getClass().getDeclaredField("calculationEventListenerTestType").getAnnotation(TestType.class);
			restClientTestType = this.getClass().getDeclaredField("restClientTestType").getAnnotation(TestType.class);
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
			assert(false);
		}
		
		bind(Home.class).annotatedWith(restClientTestType).to(RestClientHome.class);
		bind(Home.class).annotatedWith(calculationEventListenerTestType).to(CalculationListenerEventHome.class);
		bind(Home.class).to(RestClientHome.class);
	}
}
