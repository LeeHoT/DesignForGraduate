package com.qingcity.redis;

import java.util.Map;

import com.qingcity.redis.common.Keys;

/**
 * 
 * @author leehotin
 * @Date 2017年2月27日 上午11:21:57
 * @Description 用户登录数据保存
 */
public class UserRedis {
	private static final UserRedis instance = new UserRedis();

	public static UserRedis getInstance() {
		return instance;
	}

	/**
	 * 根据用户ID获取Redis全量的用户数据
	 *
	 * @param userID
	 *            用户ID
	 * @return 用户信息Map
	 */
	public Map<String, String> get(String userID) {
		return RedisManager.hgetall(Keys.REDIS_USER_PREFIX + userID);
	}

	/**
	 * 修改用户数据
	 *
	 * @param userID
	 *            用户ID
	 * @param field
	 *            字段名
	 * @param value
	 *            字段值
	 */
	public void add(String userID, String field, String value) {
		RedisManager.hset(Keys.REDIS_USER_PREFIX + userID, field, value);
	}

	/**
	 * 增加属性
	 *
	 * @param userID
	 *            用户ID
	 * @param key
	 *            字段
	 * @param increment
	 *            增加量
	 * @return 增加后的值
	 */
	public int add(String userID, String key, int increment) {
		return (int) RedisManager.hincrBy(Keys.REDIS_USER_PREFIX + userID, key, increment);
	}

	/**
	 * 修改用户信息
	 *
	 * @param userID
	 *            用户ID
	 * @param map
	 *            用户信息
	 */
	public void alter(String userID, Map<String, String> map) {
		if (!map.isEmpty()) {
			for (String key : map.keySet()) {
				RedisManager.hset(Keys.REDIS_USER_PREFIX + userID, key, map.get(key));
			}
		}
	}

	public void del(String... key) {
		String[] keys = new String[key.length];
		for (int i = 0; i < key.length; i++) {
			keys[i] = Keys.REDIS_USER_PREFIX + key[i];
		}
		RedisManager.trandDel(keys);
	}

	/**
	 * 获取用户指定信息
	 *
	 * @param userID
	 *            用户ID
	 * @param key
	 *            字段
	 * @return 指定字段信息
	 */
	public String get(String userID, String key) {
		return RedisManager.hget(Keys.REDIS_USER_PREFIX + userID, key);
	}

	/**
	 * 添加用户登录名和密码信息信息
	 * 
	 * @param username
	 * @param value
	 */
	public void addLoginRedis(String username, String value) {
		RedisManager.add(Keys.REDIS_USER_LOGIN_PREFIX + username, value);
	}

	/**
	 * 获取用户的登录密码
	 * 
	 * @param username
	 */
	public String getLoginRedis(String username) {
		return RedisManager.get(Keys.REDIS_USER_LOGIN_PREFIX + username);
	}
	
	/**
	 * 添加用户登录名和密码信息信息
	 * 
	 * @param phone
	 * @param uid
	 */
	public void addUidPhone(String phone, String uid) {
		RedisManager.add(Keys.REDIS_USERID_PHONE_PREFIX + phone, uid);
	}

	/**
	 * 获取用户的登录密码
	 * 
	 * @param phone
	 */
	public String getUidPhone(String phone) {
		return RedisManager.get(Keys.REDIS_USERID_PHONE_PREFIX + phone);
	}
}
