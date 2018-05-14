package org.fend;

public class FendRequestHeader {
	private boolean required = true;
	private String name;
	private String parameterClassName;
	private Class<?> parameterClass;
	private String value;
	private String defaultValue;
	//for primitives
	
	
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getParameterClassName() {
		return parameterClassName;
	}
	public void setParameterClassName(String parameterClassName) {
		this.parameterClassName = parameterClassName;
	}
	public Class<?> getParameterClass() {
		return parameterClass;
	}
	public void setParameterClass(Class<?> parameterClass) {
		this.parameterClass = parameterClass;
	}
	
	
	

}
