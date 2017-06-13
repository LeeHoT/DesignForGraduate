package com.qingcity.redis;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.qingcity.redis.common.Config;
import com.qingcity.redis.common.Keys;
import com.qingcity.redis.store.WaitingRoomStore;

/**
 * 
 * @author leehotin
 * @Date 2017年2月28日 下午4:54:26
 * @Description Pk匹配房间Redis数据保存
 */
public class RoomRedis {

	private static final Map<String, String> map = new LinkedHashMap<>();

	private static final RoomRedis instance = new RoomRedis();

	public static RoomRedis getInstance() {
		return instance;
	}

	/**
	 * 获取房间属性值
	 *
	 * @param roomID
	 *            房间ID
	 * @param key
	 *            字段
	 * @return 字段值
	 */
	public String get(String roomID, String key) {
		return RedisManager.hget(Keys.REDIS_PK_GAME_ROOM_PREFIX + roomID, key);
	}

	/**
	 * 增加房间属性值
	 *
	 * @param roomID
	 *            房间ID
	 * @param field
	 *            字段
	 * @param value
	 *            字段值
	 */
	public void add(String roomID, String field, String value) {
		RedisManager.hset(Keys.REDIS_PK_GAME_ROOM_PREFIX + roomID, field, value);
	}

	/**
	 * 字段增加增量
	 *
	 * @param roomID
	 *            房间ID
	 * @param field
	 *            字段
	 * @param increment
	 *            增量
	 */
	public void add(String roomID, String field, int increment) {
		RedisManager.hincrBy(Keys.REDIS_PK_GAME_ROOM_PREFIX + roomID, field, increment);
	}

	/**
	 * 删除字段
	 *
	 * @param roomID
	 *            房间ID
	 * @param field
	 *            字段
	 */
	public void del(String roomID, String field) {
		RedisManager.hdel(Keys.REDIS_PK_GAME_ROOM_PREFIX + roomID, field);
	}

	/**
	 * 删除房间
	 * 
	 * @param roomId
	 */
	public void del(String roomId) {
		RedisManager.del(Keys.REDIS_PK_GAME_ROOM_PREFIX + roomId);
	}

	/**
	 * 获取对手
	 *
	 * @param userID
	 *            用户ID
	 * @return 对手ID/AI
	 */
	public Map<String, String> getPlayer(String musicId, String userID) {

		// 查询当前玩家排名
		int rank = RankRedis.getInstance().getScore(Keys.REDIS_PK_GAME_RANK, String.valueOf(userID));
		String targetID;
		String roomID = WaitingRoomRedis.getInstance().getRandomRoom(musicId, rank);
		String targetRank;

		if (roomID != null && !roomID.equals("")) {// 匹配成功
			targetID = RoomRedis.getInstance().get(roomID, Keys.ROOM_SOURCE_ID);
			targetRank = RoomRedis.getInstance().get(roomID, Keys.ROOM_SOURCE_RANK);

			// 将用户的等级和ID数据录入
			RoomRedis.getInstance().add(roomID, Keys.ROOM_TARGET_ID, userID);
			RoomRedis.getInstance().add(roomID, Keys.ROOM_TARGET_RANK, rank);

			// 设置用户已经进入游戏
			GameRedis.getInstance().add(userID, roomID);

			// 将对手移出等待队列
			WaitingRoomRedis.getInstance().del(musicId, roomID);
			// 对方不用再等待，取消定时
			WaitingRoomStore.get(String.valueOf(targetID)).stop();
			WaitingRoomStore.remove(String.valueOf(targetID));
		} else {
			roomID = String.valueOf(SequenceRedis.getInstance().getNext(Keys.SEQUENCE_ROOM_ID));

			WaitingRoomRedis.getInstance().add(musicId, rank, roomID);

			// 将用户的ID和等级数据录入
			RoomRedis.getInstance().add(roomID, Keys.ROOM_SOURCE_ID, userID);
			RoomRedis.getInstance().add(roomID, Keys.ROOM_SOURCE_RANK, String.valueOf(rank));

			// 设置用户已经进入游戏
			GameRedis.getInstance().add(userID, roomID);

			// 增加定时器
			timerWaitingPlayerTimeover(musicId, userID, roomID, rank);

			targetID = RoomRedis.getInstance().get(roomID, Keys.ROOM_TARGET_ID);
			targetRank = RoomRedis.getInstance().get(roomID, Keys.ROOM_TARGET_RANK);
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put(Keys.ROOM_TARGET_ID, targetID);
		map.put(Keys.ROOM_TARGET_RANK, targetRank);

		return map;
	}

	/**
	 * 等待对手，超时后替换为AI
	 *
	 * @param userID
	 *            用户ID
	 * @param roomID
	 *            房间ID
	 * @param degree
	 * 
	 */
	private void timerWaitingPlayerTimeover(final String musicId, final String userID, final String roomID,
			final int rank) {
		Timer timer = new HashedWheelTimer();

		timer.newTimeout(new TimerTask() {
			@Override
			public void run(Timeout timeout) throws Exception {
				// 超时后，从等待队列中移除，设置AI为对方玩家
				WaitingRoomRedis.getInstance().del(musicId, roomID);

				// 设置对手为等级新近的一位随机玩家********************需要修改
				int randomRank = rank
						+ (int) Math.round(Math.random() * (2 * Config.PLAYER_RANK_DRIFT) - Config.PLAYER_RANK_DRIFT);
				RoomRedis.getInstance().add(roomID, Keys.ROOM_TARGET_ID, "0");
				RoomRedis.getInstance().add(roomID, Keys.ROOM_TARGET_RANK, randomRank);
				WaitingRoomStore.remove(userID);
			}
		}, Config.PLAYER_TIMEOVER, TimeUnit.SECONDS);
		WaitingRoomStore.add(userID, timer);
	}

	public static void main(String[] args) {

		Map<String, String> map = in();
		System.out.println(map.get("李慧婷"));
		System.out.println(RoomRedis.getInstance().get("123", Keys.ROOM_TARGET_ID));
		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(map.get("李慧婷"));
		System.out.println(RoomRedis.getInstance().get("123", Keys.ROOM_TARGET_ID));
	}

	public static void time() {
		Timer time = new HashedWheelTimer();
		time.newTimeout(new TimerTask() {
			@Override
			public void run(Timeout timeout) throws Exception {
				// 将用户的等级和ID数据录入
				RoomRedis.getInstance().add("123", Keys.ROOM_TARGET_ID, "李慧婷");
				RoomRedis.getInstance().add("123", Keys.ROOM_TARGET_RANK, "1000");

			}
		}, 5, TimeUnit.SECONDS);

	}

	public static Map<String, String> in() {
		Map<String, String> map = new HashMap<String, String>();
		time();
		map.put(Keys.ROOM_TARGET_ID, RoomRedis.getInstance().get("123", Keys.ROOM_TARGET_ID));
		map.put(Keys.ROOM_TARGET_RANK, RoomRedis.getInstance().get("123", Keys.ROOM_TARGET_RANK));
		return map;
	}
}
