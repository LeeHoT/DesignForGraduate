package com.qingcity.redis;

import com.qingcity.redis.common.Keys;

/**
 * 
 * @author leehotin
 * @Date 2017年3月26日 下午8:38:47
 * @Description 衣服Redis
 */
public class DressRedis {

	private static DressRedis instance = new DressRedis();

	public static DressRedis getInstance() {
		return instance;
	}

	/**
	 * 删除衣服
	 */
	public void del(String userId) {
		RedisManager.del(Keys.REDIS_USER_DRESS_PREFIX + userId);
	}

	/**
	 * 添加或更新衣服
	 * 
	 * @param userId
	 *            自己id
	 * @param dress
	 *            衣服串
	 */
	public void add(String userId, String dress) {
		RedisManager.add(Keys.REDIS_USER_DRESS_PREFIX + userId, dress);
	}
	
	/**
	 * 查询衣服
	 * @param userId
	 * @return
	 */
	public String get(String userId){
		return RedisManager.get(Keys.REDIS_USER_DRESS_PREFIX+userId);
	}
}
