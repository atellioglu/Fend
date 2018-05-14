package com.tll.fend;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class ControllerScanner {
	private Class<?> controller;
	private RequestMapping controllerMapping;
	private List<FendUrl> fendUrls;
	public ControllerScanner(Class<?> controller) {
		this.controller = controller;
		fendUrls = new ArrayList<>();
	}
	
	public void generate() {
		if(controller == null) {
			throw new RuntimeException("Controller class is null");
		}
		generateClassRequestMapping();
		extractMethods();
	}
	private void extractMethods() {
		for(Method met : controller.getDeclaredMethods()) {
			Annotation isFendEndpoint = Util.findAnnotationByName(met, "DisableFendEndPoint");
			if(isFendEndpoint == null) {
				Annotation isMethodRequestMapped = Util.findAnnotationByName(met, "RequestMapping");
				Annotation isGetMapped = Util.findAnnotationByName(met, "GetMapping");
				Annotation isPostMapped = Util.findAnnotationByName(met, "PostMapping");
				Annotation isPutMapped = Util.findAnnotationByName(met, "PutMapping");
				FendUrl fendUrl = null;
				if(isMethodRequestMapped != null) {
					fendUrl =handleRequestMapping(met, isMethodRequestMapped);
				}else if(isGetMapped != null) {
					fendUrl =handleGetMapping(met, isGetMapped);
				}else if(isPostMapped != null) {
					fendUrl = handlePostMapping(met, isPostMapped);
				}else if(isPutMapped != null) {
					fendUrl = handlePutMapping(met, isPutMapped);
				}
				if(fendUrl != null) {
					MethodScanner methodScanner = new MethodScanner(met);
					methodScanner.generate();
					FendRequestHeader[] headerArr = methodScanner.getHeaders();
					List<FendRequestHeader> list = new ArrayList<>();
					for(int i =0;i< headerArr.length;i++) {
						list.add(headerArr[i]);
					}
					fendUrl.setHeaders(list);
					fendUrl.setRequestBody(methodScanner.getBody());
					fendUrls.add(fendUrl);
				}
			}
		}
	}	
	private FendUrl handlePutMapping(Method method, Annotation annotation) {
		PutMapping mapping = (PutMapping) annotation;
		return handleMapping(mapping.consumes(),mapping.produces(),new RequestMethod[] {RequestMethod.PUT},mapping.value());
	}
	private FendUrl handleGetMapping(Method method, Annotation annotation) {
		GetMapping mapping = (GetMapping) annotation;
		return handleMapping(mapping.consumes(),mapping.produces(),new RequestMethod[] {RequestMethod.GET},mapping.value());
	}
	private FendUrl handlePostMapping(Method method, Annotation annotation) {
		PostMapping mapping = (PostMapping) annotation;
		return handleMapping(mapping.consumes(),mapping.produces(),new RequestMethod[] {RequestMethod.POST},mapping.value());
		
	}
	private FendUrl handleRequestMapping(Method method, Annotation annotation) {
		RequestMapping reqMapping = (RequestMapping)annotation;
		return handleMapping(reqMapping.consumes(),reqMapping.produces(),reqMapping.method(),reqMapping.value());
	}
	private FendUrl handleMapping(String[] consumes, String[] produces,RequestMethod[] requestMethods,String[] values) {
		FendUrl fendUrl = new FendUrl();
		
		fendUrl.setConsumes(consumes);
		fendUrl.setProduces(produces);
		if(requestMethods == null) {
			fendUrl.setMethods(controllerMapping.method());
		}else {
			fendUrl.setMethods(requestMethods);	
		}
		if(controllerMapping!=null) {
			fendUrl.setValues(Util.addPrefixToStringArr(controllerMapping.value(),values));	
		}else {
			fendUrl.setValues(values);
		}
		if(fendUrl.getMethods() == null || fendUrl.getMethods().length == 0) {
			fendUrl.setMethods(new RequestMethod[] {RequestMethod.GET});
		}
		
		return fendUrl;
	}
	private void generateClassRequestMapping() {
		Annotation clazzReqMapping = Util.findAnnotationByName(controller, "RequestMapping");
		if(clazzReqMapping != null) {
			RequestMapping requestMapping = (RequestMapping)clazzReqMapping;
			this.controllerMapping = requestMapping;
		}
	}

	public Class<?> getController() {
		return controller;
	}
	public List<FendUrl> getFendUrls() {
		return fendUrls;
	}
	
	
	
	
	
}
