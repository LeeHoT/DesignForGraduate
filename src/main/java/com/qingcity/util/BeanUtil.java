
package com.qingcity.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.ibatis.javassist.bytecode.annotation.BooleanMemberValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanUtil {

	private static final BeanUtil instance = new BeanUtil();

	public static BeanUtil getInstance() {
		return instance;
	}

	private static final Logger logger = LoggerFactory.getLogger(BeanUtil.class);

	/**
	 * 根据属性名获取属性值
	 */
	public Object getFieldValueByName(String fieldName, Object o) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			Method method = o.getClass().getMethod(getter, new Class[] {});
			Object value = method.invoke(o, new Object[] {});
			return value;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 获取属性名数组
	 */
	public String[] getFieldName(Object o) {
		Field[] fields = o.getClass().getDeclaredFields();
		String[] fieldNames = new String[fields.length];
		for (int i = 0; i < fields.length; i++) {
			System.out.println(fields[i].getType());
			fieldNames[i] = fields[i].getName();
		}
		return fieldNames;
	}

	/**
	 * 返回属性名和属性值的map
	 * 
	 * @param o
	 * @return
	 */
	public Map<String, Object> getFidldMap(Object o) {
		Map<String, Object> map = new HashMap<String, Object>();
		BeanInfo beanInfo;
		try {
			beanInfo = Introspector.getBeanInfo(o.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();  
			for (PropertyDescriptor property : propertyDescriptors) {  
				String key = property.getName();  
				// 过滤class属性  
				if (!key.equals("class")) {  
					// 得到property对应的getter方法  
					Method getter = property.getReadMethod();  
					Object value = getter.invoke(o);
					map.put(key, value);  
				}  
			}  
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}catch (InvocationTargetException e) {
			e.printStackTrace();
		}   
		return map;
	}

	/**
	 * 获取对象的所有属性值，返回一个对象数组
	 */
	public Object[] getFieldValues(Object o) {
		String[] fieldNames = this.getFieldName(o);
		Object[] value = new Object[fieldNames.length];
		for (int i = 0; i < fieldNames.length; i++) {
			value[i] = this.getFieldValueByName(fieldNames[i], o);
		}
		return value;
	}

	public Object map2Object(Map<String, String> map, Object o) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(o.getClass());  
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();  
            for (PropertyDescriptor property : propertyDescriptors) {  
                String key = property.getName();
                Type type= property.getPropertyType();
                // 过滤class属性  
                if (map.containsKey(key)) {
                	String value = map.get(key);  
                	// 得到property对应的setter方法  
                	Method setter = property.getWriteMethod();  
                	if(type.equals(Boolean.class)){
                		setter.invoke(o, Boolean.parseBoolean(value));  
                	}else if(type.equals(Integer.class)){
                		setter.invoke(o, Integer.parseInt(value)); 
                	}else if(type.equals(String.class)){
                		setter.invoke(o, value); 
                	}else if(type.equals(Timestamp.class)){
                		setter.invoke(o, Timestamp.valueOf(value)); 
                	}else if(type.equals(Short.class)){
                		setter.invoke(o, Short.parseShort(value)); 
                	}else if(type.equals(Long.class)){
                		setter.invoke(o, Long.parseLong(value)); 
                	}
                }  
            }  
		} catch (Exception e) {
			e.printStackTrace();
		}
		return o;
	}
}
