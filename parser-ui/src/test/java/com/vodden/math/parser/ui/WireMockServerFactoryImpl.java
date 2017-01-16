package com.vodden.math.parser.ui;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class WireMockServerFactoryImpl implements WireMockServerFactory {

	@Override
	public WireMockServer get() {
		return new WireMockServer(options().dynamicPort());
	}

}
