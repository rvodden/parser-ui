package com.vodden.math.parser.ui;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CalculationBeanTest {
		
	private CalculationBean calculationBeanUnderTest;
	
	
	@Test
	public void TestSetAndGetResult() {
		calculationBeanUnderTest = new CalculationBean();
		Double result;
		
		calculationBeanUnderTest.setResult(3.0d);
		result = calculationBeanUnderTest.getResult();
		assertEquals(3.0d, result, 0.01d);
	}
	
	@Test
	public void TestSetAndGetExpression() {
		calculationBeanUnderTest = new CalculationBean();
		String expression;
		
		calculationBeanUnderTest.setExpression("34");
		expression = calculationBeanUnderTest.getExpression();
		assertEquals("34", expression);
	}
	

}
