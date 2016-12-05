package com.vodden.math.parser.ui;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean()
@SessionScoped
public class CalculationBean {
	private String expression;
	private Double result;
	
	public String getExpression() {
		return expression;
	}
	
	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	public Double getResult() {
		return result;
	}
	
	public void setResult(Double result) {
		this.result = result;
	}
}
