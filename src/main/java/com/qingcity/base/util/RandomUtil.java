package com.qingcity.base.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 
 * @author leehotin
 * @Date 2017年2月23日 下午2:03:00
 * @Description 随机数工具，单例模式
 */
public class RandomUtil {

	public static final String ALL_CHAR = "-_#&$@+-*/%()[]0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static final String LETTER_CHAR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static final String NUMBER_CHAR = "0123456789";

	public static final String SPECIAL_CHAR = "-_#&$@+-*/%()[]";

	public static final String ACTIVATION_CODE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static final String LETTER_NUMBER_CHAR = LETTER_CHAR + NUMBER_CHAR;

	/**
	 * 返回一个定长的随机字符串
	 * 
	 * @param chars
	 *            模型串
	 * @param length
	 *            随机长度
	 * @return
	 */
	public static String randomString(String chars, int length) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(chars.charAt(random.nextInt(chars.length())));
		}
		return sb.toString();
	}

	public static String createActivationCode() {
		StringBuilder random = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			random.append(randomString(ACTIVATION_CODE, 5)).append("-");
		}
		random.deleteCharAt(random.length() - 1);
		return random.toString();
	}

	public static List<String> createActivationCode(int number) {
		List<String> string = new LinkedList<String>();
		for (; string.size() < number;) {
			String result = createActivationCode();
			if (!string.contains(result)) {
				string.add(result);
			}
		}
		return string;
	}

	/**
	 * 返回一个定长的随机字符串字母全部大写
	 * 
	 * @param chars
	 *            模型串
	 * @param length
	 *            随机字符串长度
	 * @return 随机字符串
	 */
	public static String randomLowerString(String chars, int length) {
		return randomString(chars, length).toLowerCase();
	}

	/**
	 * 返回一个定长的随机字符串字母全部小写
	 * 
	 * @param chars
	 *            模型串
	 * @param length
	 *            随机字符串长度
	 * @return 随机字符串
	 */
	public static String randomUpperString(String chars, int length) {
		return randomString(chars, length).toLowerCase();
	}

	/**
	 * 生成一个定长的纯0字符串
	 * 
	 * @param length
	 *            字符串长度
	 * @return 纯0字符串
	 */
	public static String randomZeroString(int length) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sb.append("0");
		}
		return sb.toString();
	}

	// 双重校验锁获取一个Random单例
	public static ThreadLocalRandom getRandom() {
		return ThreadLocalRandom.current();
	}

	/**
	 * 获得一个[0,max)之间的随机整数。
	 * 
	 * @param max
	 * @return
	 */
	public static int getRandomInt(int max) {
		return getRandom().nextInt(max);
	}

	/**
	 * 获得一个[min, max]之间的随机整数
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRandomInt(int min, int max) {
		return getRandom().nextInt(max - min + 1) + min;
	}

	/**
	 * 从数组中随机获取一个元素
	 * 
	 * @param array
	 * @return
	 */
	public static <E> E getRandomElement(E[] array) {
		return array[getRandomInt(array.length)];
	}

	/**
	 * 从list中随机取得一个元素
	 * 
	 * @param list
	 * @return
	 */
	public static <E> E getRandomElement(List<E> list) {
		return list.get(getRandomInt(list.size()));
	}

	/**
	 * 从set中随机取得一个元素
	 * 
	 * @param set
	 * @return
	 */
	public static <E> E getRandomElement(Set<E> set) {
		int rn = getRandomInt(set.size());
		int i = 0;
		for (E e : set) {
			if (i == rn) {
				return e;
			}
			i++;
		}
		return null;
	}

	/**
	 * 从map中随机取得一个key
	 * 
	 * @param map
	 * @return
	 */
	public static <K, V> K getRandomKeyFromMap(Map<K, V> map) {
		int rn = getRandomInt(map.size());
		int i = 0;
		for (K key : map.keySet()) {
			if (i == rn) {
				return key;
			}
			i++;
		}
		return null;
	}

	/**
	 * 从map中随机取得一个value
	 * 
	 * @param map
	 * @return
	 */
	public static <K, V> V getRandomValueFromMap(Map<K, V> map) {
		int rn = getRandomInt(map.size());
		int i = 0;
		for (V value : map.values()) {
			if (i == rn) {
				return value;
			}
			i++;
		}
		return null;
	}

	/**
	 * 生成一个n位的随机数，用于验证码等
	 * 
	 * @param n
	 * @return
	 */
	public static String getRandNumber(int n) {
		String rn = "";
		if (n > 0 && n < 10) {
			// Random r = new Random();
			StringBuffer str = new StringBuffer();
			for (int i = 0; i < n; i++) {
				str.append('9');
			}
			int num = Integer.parseInt(str.toString());
			while (rn.length() < n) {
				rn = String.valueOf(ThreadLocalRandom.current().nextInt(num));
			}
		} else {
			rn = "0";
		}
		return rn;
	}

}
