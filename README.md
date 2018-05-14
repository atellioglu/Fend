# Fend
Visual endpoint tool for Spring Boot.
## Getting Started
Firstly, add dependency to your POM.xml with Maven.
```
Maven URL comes here
```
Secondly, create a bean in your configuration file.
```
@Configuration
public class FendConfiguration {
	@Bean
	public Fend fend() {
		Fend fend = new Fend();//1
		fend.setPackageScan("PACKAGE PATH");//2
		fend.setProjectMode(ProjectMode.DEVELOPMENT);//3
		fend.scan();//4
		return fend;
	}
}
```
* **1** - Creates a new instance.
* **2** - Sets package to scan(which holds Controllers or RestControllers)
* **3** - Sets projection mode(Fend is only available in **DEVELOPMENT** mode, it would throw an exception if mode is **PRODUCTION**
* **4** - Scans all EndPoint urls which set at (2).


At last, you should provide an EndPoint URL for Fend. <br/>
**Basic example**:
```
	@Autowired
	Fend fend;//1
  
	@GetMapping("dev/fend")
	public void fend(HttpServletResponse response) {
		response.setContentType("application/json");//2
		response.setCharacterEncoding("utf-8");//3
		try {
			response.getWriter().write(fend.json());//4
		} catch( ProjectModeException e){//5
			e.printStackTrace();
			response.setStatus(404);//6
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
```
* **1** - Autowiring bean which we created at FendConfiguration before.
* **2** - Response should be returned as json so we set ContentType as **application/json**
* **3** - Response character encoding.
* **4** - **fend.json()** method returns a json text which contains all response.
* **5** - If project mode set **PRODUCTION**, an exception will thrown.\
* **6** - If we are not in development mode, we should hide this endpoint URL.

##Extras
###Configuration extra options
```
fend.setCacheable(boolean);//should generate json text every time, or cache it after first usage.
fend.scanAsync();//Normally whenever you enter the url specified for fend, json will be generated. If you use this method, scanning and generating start asnyc.
```
###EndPoint extra options
```
//If you want to disable any method to display on Fend URL you should add **@DisableFendEndPoint** annotation to it.
@DisableFendEndPoint
public ResponseEntity<?> disabledFendURL(){
    //this method will not shown in json. 
}
```
