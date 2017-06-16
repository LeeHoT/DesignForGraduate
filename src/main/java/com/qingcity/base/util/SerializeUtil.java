package com.qingcity.base.util;

import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 
 * @author leehotin
 * @Date 2017年2月27日 下午4:20:22
 * @Description 序列化工具
 */
public class SerializeUtil {
	private static final Logger logger = Logger.getLogger(SerializeUtil.class);

	/**
	 * 序列化对象
	 *
	 * @param object
	 *            对象
	 * @return byte[]
	 */
	public static byte[] serialize(Object object) {
		ObjectOutputStream oos;
		ByteArrayOutputStream baos;

		try {
			// 序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (Exception e) {
			logger.error("serialize object error: " + object + ", message: " + e);
		}

		return null;
	}

	/**
	 * 反序列化对象
	 *
	 * @param bytes
	 *            byte数组
	 * @return object
	 */
	public static Object unSerialize(byte[] bytes) {
		ByteArrayInputStream bais;

		try {
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			logger.error("unSerialize bytes error: " + bytes.toString() + ", message: " + e);
		}

		return null;
	}
}
