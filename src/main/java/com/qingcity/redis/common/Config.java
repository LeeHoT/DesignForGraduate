package com.qingcity.redis.common;

import com.qingcity.base.util.ConfigUtil;

/**
 * 
 * @author leehotin
 * @Date 2017年4月2日 下午4:36:00
 * @Description 游戏中的一些相关配置信息
 */
public class Config {

	// 心跳配置
	// public static int HEARTBEAT_TIMEOUT =
	// Integer.parseInt(ConfigUtil.get("heartbeat_timeout"));

	// public static int HEARTBEAT_INTERVAL =
	// Integer.parseInt(ConfigUtil.get("heartbeat_interval"));

	// 服务器配置
	public static int SERVER_PORT = Integer.parseInt(ConfigUtil.get("server_port"));

	// Redis 相关配置
	public static String REDIS_HOST = ConfigUtil.get("redis_host");

	public static int REDIS_PORT = Integer.parseInt(ConfigUtil.get("redis_port"));

	public static String REDIS_AUTH = ConfigUtil.get("redis_auth");

	public static int REDIS_MAX_TOTAL = Integer.parseInt(ConfigUtil.get("redis_max_total"));

	public static int REDIS_MAX_IDLE = Integer.parseInt(ConfigUtil.get("redis_max_idle"));

	public static int REDIS_MAX_WAIT_MILLIS = Integer.parseInt(ConfigUtil.get("redis_max_wait_millis"));

	public static boolean REDIS_TEST_ON_BORROW = Boolean.parseBoolean(ConfigUtil.get("redis_test_on_borrow"));

	public static int REDIS_EXPIRE_TIME = Integer.parseInt(ConfigUtil.get("redis_expire_time"));
	// 游戏相关配置
	public static int PLAYER_RANK_DRIFT = Integer.parseInt(ConfigUtil.get("player_rank_drift"));
	//
	public static int PLAYER_TIMEOVER = Integer.parseInt(ConfigUtil.get("player_timeover"));
	//
	// public static int LAYOUT_TIMEOVER =
	// Integer.parseInt(ConfigUtil.get("layout_timeover"));
	//
	// public static int ATTACK_TIMEOVER =
	// Integer.parseInt(ConfigUtil.get("attack_timeover"));
	//
	public static int RANKING_MAX = Integer.parseInt(ConfigUtil.get("ranking_max"));

	// 调试常量
	// public static int CONSTANT_SCORE_A =
	// Integer.parseInt(ConfigUtil.get("constant_score_a"));
	//
	// public static int CONSTANT_SCORE_B =
	// Integer.parseInt(ConfigUtil.get("constant_score_b"));
	//
	// public static int CONSTANT_SCORE_C =
	// Integer.parseInt(ConfigUtil.get("constant_score_c"));
	//
	// public static int CONSTANT_SCORE_D =
	// Integer.parseInt(ConfigUtil.get("constant_score_d"));

	// 等级与积分的对应关系
	public static String DEGREE_POINTS = ConfigUtil.get("degree_points");
}
