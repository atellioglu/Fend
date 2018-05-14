package com.tll.fend;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Util {

	@SuppressWarnings("unchecked")
	public static <T extends Annotation> T findAnnotationByName(Class<?> clazz, String annotationName){
		Annotation[] annotations = clazz.getAnnotations();
		for(Annotation anno: annotations) {
			if(anno.annotationType().getSimpleName().equals(annotationName)) {
				return (T)anno;
			}
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public static <T extends Annotation> T findAnnotationByName(Method method, String annotationName){
		Annotation[] annotations = method.getAnnotations();
		for(Annotation anno: annotations) {
			if(anno.annotationType().getSimpleName().equals(annotationName)) {
				return (T)anno;
			}
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public static <T extends Annotation> T findAnnotationByName(Parameter parameter, String annotationName){
		Annotation[] annotations = parameter.getAnnotations();
		for(Annotation anno: annotations) {
			if(anno.annotationType().getSimpleName().equals(annotationName)) {
				return (T)anno;
			}
		}
		return null;
	}
	public static String[] addPrefixToStringArr(String pre, String[] arr) {
		if(arr == null)
			return null;
		if(pre == null)
			return arr;
		String[] newArr = new String[arr.length];
		for(int i =0;i<arr.length;i++) {
			newArr[i] = pre + arr[i];
		}
		return newArr;
	}
	public static String[] addPrefixToStringArr(String[] pre, String[] arr) {
		if(arr == null)
			return null;
		if(pre == null)
			return arr;
		List<String[]> prefixes = new ArrayList<>();
		int count =0;
		for(int i=0;i<pre.length;i++) {
			String[] preArr = addPrefixToStringArr(pre[i], arr);
			count += preArr.length;
			prefixes.add(preArr);
		}
		String[] res = new String[count];
		int index =0;
		for(int i =0;i<prefixes.size();i++) {
			String[] strings = prefixes.get(i);
			for(int j=0;j<strings.length;j++) {
				res[index] = strings[j];
				index++;
			}
		}
		return res;
	}
	public static Field[] getVariables(Class<?> clazz){
		Field[] declaredFields = clazz.getDeclaredFields();
		return declaredFields;
	}
    private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();

    public static boolean isWrapperType(Class<?> clazz)
    {
        return WRAPPER_TYPES.contains(clazz);
    }
    
    private static Set<Class<?>> getWrapperTypes()
    {
        Set<Class<?>> ret = new HashSet<>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        return ret;
    }
}
