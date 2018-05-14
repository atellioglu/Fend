package org.fend;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Scans methods parameters such as RequestHeader, RequestBody or none!
 * @author abdullahtellioglu
 *
 */
public class MethodScanner {
	private Method method;
	private List<FendRequestHeader> headers;
	private FendRequestBody body;
	public MethodScanner(Method method) {
		this.method = method;
		headers = new ArrayList<>();
	}

	public FendRequestBody getBody() {
		return body;
	}

	public FendRequestHeader[] getHeaders() {
		FendRequestHeader[] header = new FendRequestHeader[headers.size()];
		for(int i =0;i < headers.size();i++) {
			header[i] = headers.get(i);
		}
		return header;
	}
	
	public void generate() {
		Parameter[] parameters = method.getParameters();
		for(Parameter parameter : parameters) {
			Annotation headerAnnotation = Util.findAnnotationByName(parameter, "RequestHeader");
			if(headerAnnotation !=null) {
				handleHeaderAnnotation(headerAnnotation,parameter);
			}else {
				Annotation bodyAnnotation = Util.findAnnotationByName(parameter, "RequestBody");
				if(bodyAnnotation!= null) {
					handleBodyAnnotation(bodyAnnotation, parameter);
				}
			}
			
		}
	}
	private void handleBodyAnnotation(Annotation annotation,Parameter parameter) {
		body = new FendRequestBody();
		RequestBody requestBody = (RequestBody) annotation;
		body.setRequired(requestBody.required());
		Class<?> parameterClassType = parameter.getType();
		body.setClassName(parameterClassType.getName());
		body.setBodyClass(parameterClassType);

	}
	private void handleHeaderAnnotation(Annotation annotation,Parameter parameter) {
		
		FendRequestHeader  header = new FendRequestHeader();
		RequestHeader requestHeader = (RequestHeader)annotation;
		header.setRequired(requestHeader.required());
		header.setName(requestHeader.name());
		header.setValue(requestHeader.value());
		header.setDefaultValue(requestHeader.defaultValue());
		
		Class<?> parameterClassType = parameter.getType();
		header.setParameterClassName(parameterClassType.getName());
		header.setParameterClass(parameterClassType);
		headers.add(header);
		
	}
}
