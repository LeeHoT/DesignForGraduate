package com.qingcity.redis;

import java.util.Set;

import com.qingcity.redis.common.Keys;

/**
 * 
 * @author leehotin
 * @Date 2017年3月5日 上午10:45:47
 * @Description 好友列表存储
 */
public class FriendsRedis {

	private static final FriendsRedis instance = new FriendsRedis();

	public static FriendsRedis getInstance() {
		return instance;
	}

	/**
	 * 删除好友
	 * 
	 * @param key
	 *            好友id
	 */
	public void del(String userId, String key) {
		RedisManager.srem(Keys.REDIS_FRIENDS_PREFIX + userId, key);
	}

	/**
	 * 添加好友
	 * 
	 * @param userId
	 *            自己id
	 * @param frienId
	 *            好友id
	 */
	public void add(String userId, String frienId) {
		RedisManager.sadd(Keys.REDIS_FRIENDS_PREFIX + userId, frienId);
	}

	/**
	 * 获取玩家好友列表
	 *
	 * @param userID
	 *            用户ID
	 * @return 好友列表
	 */
	public Set<String> getFriends(String userId) {
		return RedisManager.smembers(Keys.REDIS_FRIENDS_PREFIX + userId);
	}

	/**
	 * 判断是否是自己的好友
	 * 
	 * @param userId
	 *            自己id
	 * @param friendId
	 *            好友id
	 * @return
	 */
	public Boolean isFriends(String userId, String friendId) {
		return RedisManager.sIsMember(Keys.REDIS_FRIENDS_PREFIX + userId, friendId);
	}

	/**
	 * 查询两个人的共同好友列表
	 * 
	 * @param userId
	 * @param friendId
	 * @return
	 */
	public Set<String> getMutualFriend(String userId, String friendId) {
		return RedisManager.sinter(Keys.REDIS_FRIENDS_PREFIX + userId, Keys.REDIS_FRIENDS_PREFIX + friendId);
	}
}
