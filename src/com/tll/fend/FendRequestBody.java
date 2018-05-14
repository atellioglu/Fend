package com.tll.fend;

public class FendRequestBody {
	private String name;
	private String className;
	private Class<?> bodyClass;
	private boolean required = false;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public Class<?> getBodyClass() {
		return bodyClass;
	}
	public void setBodyClass(Class<?> bodyClass) {
		this.bodyClass = bodyClass;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	
	
	
}
