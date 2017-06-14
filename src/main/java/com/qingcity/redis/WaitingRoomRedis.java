package com.qingcity.redis;

import com.qingcity.redis.common.Config;
import com.qingcity.redis.common.Keys;

/**
 * 
 * @author leehotin
 * @Date 2017年4月27日 上午11:21:30
 * @Description 等待房间
 */
public class WaitingRoomRedis {
	private static final WaitingRoomRedis instance = new WaitingRoomRedis();

	public static WaitingRoomRedis getInstance() {
		return instance;
	}

	/**
	 * 根据等级上下浮动找到等待的玩家 有事务控制
	 *
	 * @param degree
	 *            等级
	 * @return 房间ID
	 */
	public String getRandomRoom(String musicId, int level) {
		int min = ((level - Config.PLAYER_RANK_DRIFT) < 0) ? 1 : level - Config.PLAYER_RANK_DRIFT;
		int max = level + Config.PLAYER_RANK_DRIFT;

		return RedisManager.zrandom(Keys.REDIS_PK_GAME_WAITING_ROOM_PREFIX + musicId, min, max);
	}

	/**
	 * 增加等待玩家的房间
	 *
	 * @param degree
	 *            等级
	 * @param roomID
	 *            房间ID
	 */
	public void add(String musicId, int degree, String roomID) {
		RedisManager.zadd(Keys.REDIS_PK_GAME_WAITING_ROOM_PREFIX + musicId, degree, roomID);
	}

	/**
	 * 删除等待玩家的房间
	 *
	 * @param roomID
	 *            房间ID
	 */
	public void del(String musicId, String roomID) {
		RedisManager.zrem(Keys.REDIS_PK_GAME_WAITING_ROOM_PREFIX + musicId, roomID);
	}
}
