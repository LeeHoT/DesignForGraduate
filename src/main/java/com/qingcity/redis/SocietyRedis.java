package com.qingcity.redis;

import java.util.Map;
import java.util.Set;

import com.qingcity.redis.common.Keys;

/**
 * 
 * @author leehotin
 * @Date 2017年3月5日 下午5:01:07
 * @Description 公会缓存
 */
public class SocietyRedis {

	private static final SocietyRedis instance = new SocietyRedis();

	public static final SocietyRedis getInstance() {
		return instance;
	}

	/**
	 * 根据公会 id获取Redis全量的公会成员信息数据
	 *
	 * @param societyId
	 *            公会ID
	 * @return 公会信息
	 */
	public Map<String, String> hget(String societyId) {
		return RedisManager.hgetall(Keys.REDIS_SOCIETY_PREFIX + societyId);
	}

	/**
	 * 修改公会数据
	 *
	 * @param societyId
	 *            公会ID
	 * @param field
	 *            字段名
	 * @param value
	 *            字段值
	 */
	public void hadd(String societyId, String field, String value) {
		RedisManager.hset(Keys.REDIS_SOCIETY_PREFIX + societyId, field, value);
	}
	
	

	/**
	 * 增加公会某个属性的属性值
	 *
	 * @param societyId
	 *            公会ID
	 * @param field
	 *            字段
	 * @param increment
	 *            增加量
	 * @return 增加后的值
	 */
	public int hadd(String societyId, String field, int increment) {
		return (int) RedisManager.hincrBy(Keys.REDIS_SOCIETY_PREFIX + societyId, field, increment);
	}

	/**
	 * 修改用户信息
	 *
	 * @param societyId
	 *            公会ID
	 * @param map
	 *            公会信息
	 */
	public void halter(String societyId, Map<String, String> map) {
		if (!map.isEmpty()) {
			for (String key : map.keySet()) {
				RedisManager.hset(Keys.REDIS_USER_PREFIX + societyId, key, map.get(key));
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
	 * @param societyId
	 *            公会ID
	 * @param key
	 *            字段
	 * @return 指定字段信息
	 */
	public String hget(String societyId, String key) {
		return RedisManager.hget(Keys.REDIS_USER_PREFIX + societyId, key);
	}

	/**
	 ************************ 以上内容为hset,以下内容为list**************************
	 */

	/**
	 * 踢出公会成员
	 * 
	 * @param societyId
	 * @param member
	 */
	public void del(String societyId, String member) {
		RedisManager.srem(Keys.REDIS_SOCIETY_MEMBER_PREFIX + societyId, member);
	}

	/**
	 * 添加公会成员
	 * 
	 * @param societyId
	 *            自己id
	 * @param userId
	 *            好友id
	 */
	public void add(String societyId, String userId) {
		RedisManager.sadd(Keys.REDIS_SOCIETY_MEMBER_PREFIX + societyId, userId);
	}

	/**
	 * 获取公会玩家列表
	 *
	 * @param societyId
	 *            公会ID
	 * @return 成员Id列表
	 */
	public Set<String> getMembers(String societyId) {
		return RedisManager.smembers(Keys.REDIS_SOCIETY_MEMBER_PREFIX + societyId);
	}

	/**
	 * 添加公会申请
	 * 
	 * @param societyId
	 * @param userId
	 */
	public void addApply(String societyId, String userId) {
		RedisManager.sadd(Keys.REDIS_SOCIETY_APPLY_PREFIX + societyId, userId);
	}

	/**
	 * 将申请删除
	 * 
	 * @param societyId
	 * @param userId
	 */
	public void delApply(String societyId, String userId) {
		RedisManager.srem(Keys.REDIS_SOCIETY_APPLY_PREFIX + societyId, userId);
	}

	/**
	 * 查询当前公会的所有申请
	 * 
	 * @param societyId
	 * @return
	 */
	public Set<String> getApply(String societyId) {
		return RedisManager.smembers(Keys.REDIS_SOCIETY_APPLY_PREFIX + societyId);
	}

}
