package com.qingcity.redis.store;

import io.netty.util.Timer;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author leehotin
 * @Date 2017年4月1日 上午10:22:05
 * @Description 用于等待PK过程中保存玩家等待的任务
 */
public class WaitingRoomStore {

	private static final ConcurrentHashMap<String, Timer> timers = new ConcurrentHashMap<String, Timer>();

	public static void remove(String sessionId) {
		timers.remove(sessionId);
	}

	public static void add(String sessionId, Timer timer) {
		timers.put(sessionId, timer);
	}

	public static Timer get(String sessionId) {
		return timers.get(sessionId);
	}
}
