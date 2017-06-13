package com.qingcity.redis;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author leehotin
 * @Date 2017年3月3日 下午6:32:04
 * @Description 玩家通关记录存储Redis
 */
public class PassRecordRedis {

	private static final PassRecordRedis instance = new PassRecordRedis();

	public static PassRecordRedis getInstance() {
		return instance;
	}

	/**
	 * 根据用户ID获取Redis全量的用户通关数据
	 * 
	 * @param key
	 *            键前缀
	 * @return 用户通关信息Map
	 */
	public Map<String, String> get(String key) {
		return RedisManager.hgetall(key);
	}

	/**
	 * 修改用户通关数据，例如修改未通过歌曲为通过(备用)
	 * 
	 * @param key
	 *            键前缀
	 * @param userID
	 *            用户ID
	 * @param field
	 *            字段名
	 * @param value
	 *            字段值
	 */
	public void add(String key ,String field, String value) {
		RedisManager.hset(key, field, value);
	}
	

	/**
	 * 增加新通关歌曲
	 *
	 * @param key
	 *            键前缀
	 * @param userID
	 *            用户ID
	 * @param field
	 *            字段
	 * @param increment
	 *            增加量
	 * @return 增加后的值
	 */
	public int add(String key, String userID, String field, int increment) {
		return (int) RedisManager.hincrBy(key + userID, field, increment);
	}

	/**
	 * 修改用户信息
	 *
	 * @param userID
	 *            用户ID
	 * @param map
	 *            用户通关信息
	 */
	public void alter(String key, String userID, Map<String, String> map) {
		if (!map.isEmpty()) {
			for (String keys : map.keySet()) {
				RedisManager.hset(key + userID, keys, map.get(keys));
			}
		}
	}

	public void del(String... key) {
		String[] keys = new String[key.length];
		for (int i = 0; i < key.length; i++) {
			keys[i] = key + key[i];
		}
		RedisManager.trandDel(keys);
	}

	/**
	 * 获取用户指定信息
	 *
	 * @param key
	 *            键前缀
	 *
	 * @param userID
	 *            用户ID
	 * @param field
	 *            字段
	 * @return 指定字段信息
	 */
	public String get(String key, String userID, String field) {
		return RedisManager.hget(key + userID, field);
	}

}
