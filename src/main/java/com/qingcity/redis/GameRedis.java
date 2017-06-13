package com.qingcity.redis;

import com.qingcity.redis.common.Keys;

/**
 * 
 * @author leehotin
 * @Date 2017年3月1日 上午10:51:54
 * @Description 保存所有正在进行pk的玩家数据，包括正在匹配和匹配成功但未完赛的玩家
 */
public class GameRedis {

	private static final GameRedis instance = new GameRedis();

	public static GameRedis getInstance() {
		return instance;
	}

	public void add(String userId, String roomId) {
		RedisManager.add(Keys.REDIS_PK_USERS_PREFIX + userId, roomId);
	}

	/**
	 * 删除两个pk的玩家
	 * 
	 * @param userId
	 * @param targetId
	 */
	public void del(String userId) {
		RedisManager.del(userId);
	}

	public String get(String userId) {
		return RedisManager.get(userId);
	}

}
