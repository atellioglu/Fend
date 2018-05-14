package com.tll.fend;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMethod;

public class FendUrl {
	private String[] values;
	private RequestMethod[] methods;
	private String[] consumes;
	private String[] produces;
	private List<FendRequestHeader> headers;
	private FendRequestBody requestBody;
	
	public List<FendRequestHeader> getHeaders() {
		return headers;
	}

	public void setHeaders(List<FendRequestHeader> headers) {
		this.headers = headers;
	}

	public String[] getConsumes() {
		return consumes;
	}
	public void setConsumes(String[] consumes) {
		this.consumes = consumes;
	}
	public String[] getProduces() {
		return produces;
	}
	public void setProduces(String[] produces) {
		this.produces = produces;
	}
	public RequestMethod[] getMethods() {
		return methods;
	}
	public void setMethods(RequestMethod[] methods) {
		this.methods = methods;
	}
	public void setValues(String[] values) {
		this.values = values;
	}
	public String[] getValues() {
		return this.values;
	}
	
	public FendRequestBody getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(FendRequestBody requestBody) {
		this.requestBody = requestBody;
	}

	@Override
	public String toString() {
		return "FendUrl [values=" + Arrays.toString(values) + ", methods=" + Arrays.toString(methods) + ", consumes="
				+ Arrays.toString(consumes) + ", produces=" + Arrays.toString(produces) + "]";
	}
	
	
	
	

}
