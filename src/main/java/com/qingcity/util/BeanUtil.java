package com.qingcity.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

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
	public Map<Object, Object> getFidldMap(Object o) {
		Map<Object, Object> map = new LinkedHashMap<>();
		Field[] fields = o.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			try {
				String firstLetter = fields[i].getName().substring(0, 1).toUpperCase();
				String getter = "get" + firstLetter + fields[i].getName().substring(1);
				if (getter.equals("getSerialVersionUID")) {
					continue;
				}
				if (getter.contains("Is")) {
					getter = "is" + getter.substring(5, getter.length());
				}
				Method method = o.getClass().getMethod(getter, new Class[] {});
				Object value = method.invoke(o, new Object[] {});
				map.put(fields[i].getName(), value);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return null;
			}
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

		Field[] fields = o.getClass().getDeclaredFields();
		try {
			for (Field field : fields) {
				String name = field.getName();
				System.out.println("取值的时候的参数名为" + name);
				String setter = "set" + name.toUpperCase().substring(0, 1) + name.substring(1);
				System.out.println("setter方法" + setter);
				if (setter.equals("setSerialVersionUID")) {
					System.out.println("setSerialVersionUID()直接略过");
					continue;
				}
				if (setter.contains("Is")) {
					setter = "set" + setter.substring(5, setter.length());
				}
				Method method = o.getClass().getMethod(setter, new Class[] { field.getType() });
				Type type = field.getType();
				System.out.println("属性名为" + type.toString());
				if (type.equals("java.lang.String")) {
					method.invoke(o, map.get(field));
				} else if (type.equals("java.util.Date")) {
					method.invoke(o, TimeUtil.String2Date(map.get(field)));
				} else if (type.equals("java.sql.Timestamp")) {
					method.invoke(o, Timestamp.valueOf(map.get(field)));
				} else if (type.equals("java.lang.Integer")) {
					method.invoke(o, Integer.valueOf(map.get(field)));
				} else if (type.equals("boolean")) {
					method.invoke(o, Boolean.valueOf(map.get(field)));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return o;
	}
}
