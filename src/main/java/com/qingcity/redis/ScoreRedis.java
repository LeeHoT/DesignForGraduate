package com.qingcity.redis;

import java.util.Map;

public class ScoreRedis {

	private static ScoreRedis instance = new ScoreRedis();

	public static ScoreRedis getInstance() {
		return instance;
	}

	/**
	 * 根据用户ID和前缀获取Redis全量的歌曲数据(打歌中的的歌曲成绩信息)
	 *
	 * @param userID
	 *            用户ID
	 * @return 用户信息Map
	 */
	public Map<String, String> get(String prefix, String userID) {
		return RedisManager.hgetall(prefix + userID);
	}

	/**
	 * 修改玩家为歌曲id为field的成绩为新的value
	 *
	 * @param userID
	 *            用户ID
	 * @param field
	 *            字段名
	 * @param value
	 *            字段值
	 */
	public void add(String prefix, String userID, String field, String value) {
		RedisManager.hset(prefix + userID, field, value);
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
	public int add(String prefix, String userID, String key, int increment) {
		return (int) RedisManager.hincrBy(prefix + userID, key, increment);
	}

	/**
	 * 修改玩家的多个信息
	 *
	 * @param userID
	 *            用户ID
	 * @param map
	 *            用户信息
	 */
	public void alter(String prefix, String userID, Map<String, String> map) {
		if (!map.isEmpty()) {
			for (String key : map.keySet()) {
				RedisManager.hset(prefix + userID, key, map.get(key));
			}
		}
	}

	// public void del(String prefix, String... key) {
	// String[] keys = new String[key.length];
	// for (int i = 0; i < key.length; i++) {
	// keys[i] = prefix + key[i];
	// }
	// RedisManager.trandDel(keys);
	// }

	public void del(String prefix, String userId) {
		RedisManager.del(prefix + userId);
	}

	/**
	 * 获取用户指定歌曲的信息
	 *
	 * @param userID
	 *            用户ID
	 * @param key
	 *            字段
	 * @return 指定字段信息
	 */
	public String get(String prefix, String userID, String key) {
		return RedisManager.hget(prefix + userID, key);
	}

}
