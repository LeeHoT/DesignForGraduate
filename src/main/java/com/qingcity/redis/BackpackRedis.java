package com.qingcity.redis;

import java.util.Map;

import com.qingcity.redis.common.Keys;

public class BackpackRedis {
	
	private static BackpackRedis instance = new BackpackRedis();
	
	public static BackpackRedis getInstance(){
		return instance;
	}

	/**
	 * 根据用户ID获取背包中所有数据
	 *
	 * @param userID
	 *            用户ID
	 * @return 用户信息Map
	 */
	public Map<String, String> get(String userID) {
		return RedisManager.hgetall(Keys.REDIS_USER_BACKPACK_PREFIX + userID);
	}

	/**
	 * 修改背包中的数据
	 *
	 * @param userID
	 *            用户ID
	 * @param field
	 *            字段名
	 * @param value
	 *            字段值
	 */
	public void add(String userID, String field, String value) {
		RedisManager.hset(Keys.REDIS_USER_BACKPACK_PREFIX + userID, field, value);
	}

	/**
	 * 增加背包中物品的属性
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
		return (int) RedisManager.hincrBy(Keys.REDIS_USER_BACKPACK_PREFIX + userID, key, increment);
	}

	/**
	 * 修改背包中多个物品的信息
	 *
	 * @param userID
	 *            用户ID
	 * @param map
	 *            用户信息
	 */
	public void alter(String userID, Map<String, String> map) {
		if (!map.isEmpty()) {
			for (String key : map.keySet()) {
				RedisManager.hset(Keys.REDIS_USER_BACKPACK_PREFIX + userID, key, map.get(key));
			}
		}
	}

	public void del(String... key) {
		String[] keys = new String[key.length];
		for (int i = 0; i < key.length; i++) {
			keys[i] = Keys.REDIS_USER_BACKPACK_PREFIX + key[i];
		}
		RedisManager.trandDel(keys);
	}

	/**
	 * 获取背包中指定物品的信息
	 *
	 * @param userID
	 *            用户ID
	 * @param key
	 *            字段
	 * @return 指定字段信息
	 */
	public String get(String userID, String key) {
		return RedisManager.hget(Keys.REDIS_USER_BACKPACK_PREFIX + userID, key);
	}

}
