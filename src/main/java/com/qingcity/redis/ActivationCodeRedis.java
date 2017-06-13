package com.qingcity.redis;

import com.qingcity.redis.common.Keys;

/**
 * 
 * @author leehotin
 *
 */
public class ActivationCodeRedis {

	private static final ActivationCodeRedis instance = new ActivationCodeRedis();
	
	public static ActivationCodeRedis getInstance(){
		return instance;
	}
	
	/**
	 * 删除已经激活的玩家
	 * 
	 * @param key
	 *            好友id
	 */
	public void del(String userId) {
		RedisManager.srem(Keys.REDIS_ACTIVATION_CODE,userId);
	}

	/**
	 * 添加成功激活的玩家
	 * 
	 * @param userId
	 */        
	public void add(String userId) {
		RedisManager.sadd(Keys.REDIS_ACTIVATION_CODE,userId);
	}

	/**
	 * 判断已激活的玩家中是否索要查询的人
	 * @param userId
	 *            自己id
	 */
	public Boolean isMember(String userId) {
		return RedisManager.sIsMember(Keys.REDIS_ACTIVATION_CODE ,userId);
	}
}
