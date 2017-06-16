package com.qingcity.base.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * 
 * @author leehotin
 * @Date 2017年3月2日 下午5:19:04
 * @Description 配置文件读取
 */
public class ConfigUtil {

	private static final Logger LOG = Logger.getLogger(ConfigUtil.class);

	private static Properties config = null;

	private static Map<String, String> configMap = new HashMap<String, String>();

	/**
	 * 获取配置项
	 *
	 * @param key
	 *            配置项
	 * @return 配置信息
	 */
	public static String get(String key) {
		if (configMap.isEmpty()) {
			init();
		}
		return configMap.containsKey(key) ? configMap.get(key) : "";
	}

	/**
	 * 加载配置文件
	 */
	public static void init() {
		configMap.clear();
		ResourceBundle bundle = ResourceBundle.getBundle("config/properties/config");
		Set<String> keySet = bundle.keySet();
		for (String key : keySet) {
			configMap.put(key, bundle.getString(key));
		}
	}

	/**
	 * 返回系统config.properties配置信息
	 * 
	 * @param key
	 *            key值
	 * @return value值
	 */
	public static String getProperty(String key) {
		if (config == null) {
			synchronized (ConfigUtil.class) {
				if (null == config) {
					try {
						Resource resource = new ClassPathResource("config.properties");
						config = PropertiesLoaderUtils.loadProperties(resource);
					} catch (IOException e) {
						LOG.error(e.getMessage(), e);
					}
				}
			}
		}
		return config.getProperty(key);
	}
}
