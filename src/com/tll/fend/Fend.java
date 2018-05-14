package com.tll.fend;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ValueConstants;

import com.google.gson.Gson;
/**
 * Main class, it should be created @Bean in Spring Configuration. <br/>
 * This class scans all Controllers or RestController in controller folder.
 *   
 * @author abdullahtellioglu
 *
 */
public class Fend {
	private static final Logger logger = Logger.getLogger(Fend.class.getSimpleName());
	//route package holder
	private String routePackage;
	//holds all classes into a list that scanned files.
	private List<Class<?>> classList;
	//contains class in 
	private Map<Class<?>,List<FendUrl>> fendUrlMap;
	//to generate cached json
	private String cachedJson = null;
	//Project mode holder
	private ProjectMode projectMode = ProjectMode.DEVELOPMENT;
	//default cachable is true
	private boolean cacheable = true;
	public void setPackageScan(String packageName) {
		routePackage = packageName;
		fendUrlMap = new HashMap<>();
	}
	
	public void scan() {
		if(routePackage == null) {
			throw new RuntimeException("Package name is not initialized");
		}
		
		if(projectMode == ProjectMode.PRODUCTION) {
			return;
		}
		classList = ClassFinder.find(routePackage);
		for(int i =0;i < classList.size();i++) {
			Class<?> currentClass = classList.get(i);
			
			if(isClassController(currentClass)) {
				logger.info("Found countroller:"+currentClass.getSimpleName());
				ControllerScanner scanner = new ControllerScanner(currentClass);
				scanner.generate();
				fendUrlMap.put(currentClass, scanner.getFendUrls());
				
			}
		}
	}
	public void setCacheable(boolean cacheable) {
		this.cacheable = cacheable;
	}
	private boolean isClassController(Class<?> clazz) {
		Annotation[] annotations = clazz.getAnnotations();
		for(Annotation annotation : annotations){
			if(annotation instanceof Controller)
				return true;
			if(annotation instanceof RestController)
				return true;
		}
		return false;
	}
	
	/**
	 * Not completed, normally it would return html file!
	 * @return null
	 */
	public String html() {
		return null;
	}
	/**
	 * Generate json format of api or etc.. 
	 * @return json string
	 * @throws JSONException 
	 * @throws ProjectModeException throw is project mode is setted PRODUCTION 
	 */
	public String json() throws JSONException,ProjectModeException{
		//do not generate json at production
		if(projectMode == ProjectMode.PRODUCTION) {
			throw new ProjectModeException("Project is at PRODUCTION mode.");
		} 
		if(cachedJson != null && cacheable) {
			return cachedJson;
		}
		JSONArray jsonArray = new JSONArray();
		for(Map.Entry<Class<?>, List<FendUrl>> entry : fendUrlMap.entrySet()) {
			List<FendUrl> fendys = entry.getValue();
			for(FendUrl fend : fendys) {
				JSONObject object = new JSONObject();
				if(fend.getConsumes() != null && fend.getConsumes().length != 0) {
					JSONArray consumesArr = new JSONArray();
					consumesArr.put(Arrays.asList(fend.getConsumes()));
					object.put("consumes", consumesArr);
				}
				if(fend.getProduces() != null && fend.getProduces().length != 0) {
					JSONArray prodArr = new JSONArray();
					prodArr.put(Arrays.asList(fend.getProduces()));
					object.put("produces", prodArr);
				}
				if(fend.getValues() != null && fend.getValues().length != 0) {
					JSONArray valueArr = new JSONArray();
					valueArr.put(Arrays.asList(fend.getValues()));
					object.put("values", valueArr);
				}
				if(fend.getMethods() != null && fend.getMethods().length != 0) {
					JSONArray methods = new JSONArray();
					for(RequestMethod method : fend.getMethods()) {
						methods.put(method.name());
					}
					object.put("methods", methods);
				}
				if(fend.getHeaders() != null) {
					JSONArray headerJsonArray = new JSONArray();
					List<FendRequestHeader> headers = fend.getHeaders();
					if(headers != null) {
						for(FendRequestHeader header : headers) {
							JSONObject headerJSONObj = new JSONObject();
							headerJSONObj.put("typeName",header.getParameterClassName());
							headerJSONObj.put("name", header.getName());
							if(!header.getDefaultValue().equals(ValueConstants.DEFAULT_NONE)) {
								headerJSONObj.put("defaultValue", header.getDefaultValue());	
							}
							headerJSONObj.put("value",header.getValue());
							Object newInstance;
							try {
								newInstance = header.getParameterClass().newInstance();
								String instanceJSON = new Gson().toJson(newInstance);
								headerJSONObj.put("object", instanceJSON);
							} catch (InstantiationException | IllegalAccessException e) {
								logger.severe(e.getMessage());
							}
							headerJsonArray.put(headerJSONObj);
						}
						
					}
					if(fend.getRequestBody() != null) {
						FendRequestBody body = fend.getRequestBody();
						JSONObject bodyObj = new JSONObject();
						bodyObj.put("required", body.isRequired());
						bodyObj.put("typeName", body.getClassName());
						JSONObject data = new JSONObject();
						Field[] variables = Util.getVariables(body.getBodyClass());
						for(int i =0;i < variables.length;i++) {
							Field field = variables[i];
							String type = field.getType().getSimpleName();
							String name = field.getName();
							//final variables not included!
							 if (!java.lang.reflect.Modifier.isFinal(field.getModifiers())) {
								 data.put(name, type); 
							 }
						}
						if(data.length() != 0) {
							JSONObject obj = new JSONObject();
							obj.put(body.getBodyClass().getSimpleName(), data);
							object.put("body", obj);
							
						}else {
							object.put("body", bodyObj);	
						}
					}
					object.put("headers", headerJsonArray);
				}
				jsonArray.put(object);
			}
		}
		cachedJson = jsonArray.toString();
		return cachedJson;
	}
	public Map<Class<?>,List<FendUrl>> getFendUrls(){
		return fendUrlMap;
	}
	public void setProjectMode(ProjectMode projectMode) {
		this.projectMode = projectMode;
	}

	public void scanAsync() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				//async thread to execute json or html file
				scan();
				try {
					json();
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (ProjectModeException e) {
					e.printStackTrace();
				}
				html();
			}
		}).start();
		
	}
	
}
