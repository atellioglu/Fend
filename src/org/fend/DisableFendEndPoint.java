package org.fend;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Disables Fend EndPoint for method.<br/>
 * Example usage :  
 * <blockquote><pre>
 * {@code
 * RestMapping("/disable/fend/endpoint")
 * DisableFendEndPoint
 * public void someMethod(){}
 * }
 * </pre></blockquote>

 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DisableFendEndPoint {

}
