package com.qingcity.redis.common;

/**
 * 
 * @author leehotin
 * @Date 2017年4月28日 上午11:11:07
 * @Description Redis键
 */
public class Keys {

	// 统一使用的字段分隔符
	public static final String DELIMITER = "_";

	// Redis 用户相关字段
	public static final String REDIS_USER_PREFIX = "user_";// 用户信息数据保存Redis前缀
	
	public static final String REDIS_USER_LOGIN_PREFIX = "login_";
	
	public static final String REDIS_USERID_PHONE_PREFIX = "userId_phone_";

	public static final String USER_ID = "userId";// 玩家id的field

	public static final String USER_NAME = "username";// 玩家名字field

	public static final String USER_NICKNAME = "nickname";// 昵称

	public static final String USER_AVATAR = "avatar";// 头像

	public static final String USER_LEVEL = "level";// 等级

	public static final String USER_ROOM_ID = "roomId";// 房间id

	public static final String USER_POWER = "power";

	public static final String USER_GOLD = "gold";

	public static final String USER_DIAMOND = "diamond";

	public static final String USER_SOCIETY_ID = "societyId";
	
	public static final String USER_ID_CARD = "idCard";
	
	public static final String USER_EMAIL = "email";
	
	public static final String USER_FIRST_CHARGE = "isCharge";

	public static final String USER_CONTRIBUTION = "contribution";

	public static final String USER_SOCIETY_JOB = "job";

	public static final String USER_LAST_LOGIN_TIME = "lastLoginTime";

	public static final String USER_EXPERIENCE = "experience";
	
	public static final String USER_SIGNATURE="signature";
	
	public static final String USER_LAST_POWER_UPDATE_TIME = "lastPowUpdateTime";
	
	public static final String REDIS_USER_BACKPACK_PREFIX = "backpack_";
	
	
	public static final String REDIS_ACTIVATION_CODE="activation_code";
	
	/**
	 * 衣服
	 */
	public static final String REDIS_USER_DRESS_PREFIX = "dress_";

	// Redis城市相关字段
	public static final String REDIS_YANG_SCORE = "yang_score_";// 漾青城各类成绩排行前缀

	public static final String REDIS_COOL_SCORE = "cool_score_";// 炫青城各类成绩排行前缀

	public static final String REDIS_BLACK_SOCRE = "black_socre_";// 墨青城各类成绩排行前缀

	public static final String REDIS_ALL_SCORE = "all_socre_";// 主页面总歌曲排行

	public static final String REDIS_MUSIC_LIST_EASY_SCORE_PREFIX = "music_plain_easy_score_";// 打歌界面easy难度玩家歌曲成绩

	public static final String REDIS_MUSIC_LIST_COMMON_SCORE_PREFIX = "music_plain_COMMON_score_";// 打歌界面一般难度玩家歌曲成绩

	public static final String REDIS_MUSIC_LIST_HARD_SCORE_PREFIX = "music_plain_HARD_score_";// 打歌界面困难难度玩家歌曲成绩

	public static final String REDIS_MUSIC_DAY_EASY_SCORE_PREFIX = "music_day_easy_score_";// 简单难度每日成绩存储

	public static final String REDIS_MUSIC_DAY_COMMON_SCORE_PREFIX = "music_day_common_score_";// 一般难度每日成绩存储

	public static final String REDIS_MUSIC_DAY_HARD_SCORE_PREFIX = "music_day_hard_score_";// 困难难度每日成绩存储

	public static final String REDIS_MUSIC_MAX_SCORE_PREFIX = "music_max_score_";// 玩家每首音乐最高成绩

	// 好友Redis键
	public static final String REDIS_FRIENDS_PREFIX = "friends_";

	// Society 相关Redis 数据
	public static final String REDIS_SOCIETY_PREFIX = "society_";// hset

	public static final String REDIS_SOCIETY_MEMBER_PREFIX = "society_member_";// list

	public static final String REDIS_SOCIETY_APPLY_PREFIX = "society_apply_";

	public static final String REDIS_SOCIETY_RANK = "society_rank";

	public static final String SOCIETY_ID = "societyId";

	public static final String SOCIETY_NAME = "name";

	public static final String SOCIETY_NOTICE = "notice";

	public static final String SOCIETY_MEMBER_NUM = "number";

	public static final String SOCIETY_PREDISDENT_ID = "predisdentId";// 会长id
	// Redis通关记录相关字段

	public static final String REDIS_PASS_RECORD_EASY_PREFIX = "pass_record_easy_";

	public static final String REDIS_PASS_RECORD_COMMON_PREFIX = "pass_record_common_";

	public static final String REDIS_PASS_RECORD_HARD_PREFIX = "pass_record_hard_";

	// Redis 序列相关字段
	public static final String REDIS_SEQUENCE = "sequence";

	public static final String SEQUENCE_USER_ID = "user_id";

	public static final String SEQUENCE_ROOM_ID = "room_id";

	// PK结果相关字段
	// public static final String BATTLE_RESULT = "result";
	//
	// public static final String BATTLE_RESULT_WIN = "0";
	//
	// public static final String BATTLE_RESULT_FAIL = "1";
	//
	// public static final String BATTLE_RESULT_DRAW = "2";

	// Redis 排行榜相关字段
	public static final String REDIS_PLAIN_GAME_RANK = "plain_game_rank";// 普通排行榜

	public static final String REDIS_PK_GAME_RANK = "pk_game_rank";// pk排行榜

	public static final String REDIS_PK_USERS_PREFIX = "pk_users_";

	// Redis 游戏房间相关字段
	public static final String REDIS_PK_GAME_ROOM_PREFIX = "pk_game_room_";

	public static final String ROOM_ROUND = "round";

	public static final String ROOM_SOURCE_ID = "source_id";

	public static final String ROOM_SOURCE_RANK = "source_rank";

	public static final String ROOM_TARGET_ID = "target_id";

	public static final String ROOM_TARGET_RANK = "target_rank";

	public static final String ROOM_SOURCE_SCORE = "source_score";

	public static final String ROOM_TARGET_SCORE = "target_score";

	// 等待对手的roomID有序集合
	public static final String REDIS_PK_GAME_WAITING_ROOM_PREFIX = "pk_game_waiting_room_";

	public static final String SYSTEM_STATUS = "status";

	public static final String SYSTEM_STATUS_OK = "ok";

	public static final String HEARTBEAT_INTERVAL = "heartbeat_interval";
	
	
	public static final String SYSTEM_INIT_DATA_MANAGER_PREFIX= "init_data_manager_";
	
	public static final String SHOP_LIMIT_GIFT_PREFIX = "shop_limit_gift_";
	
	public static final String DAILY_TASK_PREFIX = "daily_task_";
	
	public static final String MAIN_TASK_PREFIX = "main_task_";
	
	public static final String PLAIN_MUSIC_STATUS_PREFIX = "plain_music_status_";
	
	public static final String PASS_MUSIC_STATUS_PREFIX = "pass_music_status_";
	
	public static final String PK_STATUS_PREFIX = "pk_status_";
	
	public static final String IN_GAME_PLAYER_LIST ="in_game_players";
	
}
