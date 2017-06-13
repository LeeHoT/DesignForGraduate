package com.qingcity.constants;

/**
 * 命令码常量检测
 */
public interface CmdConstant {

	short PING = 5000, // 心跳检测
			REP_PING = 5001, // 恢复心跳检测


			C2S_USER_LOGIN = 111, // 登录验证
			C2S_USER_REGISTER = 112, // 注册验证
			C2S_USER_CREATE_ROLE = 113, // 创建角色

			S2C_USER_LOGIN = 131, // 登录验证
			S2C_USER_REGISTER = 132, // 注册验证
			S2C_USER_CREATE_ROLE = 133, // 创建角色

			S2C_USER_NOT_LOGIN = 151, // 未登录
			S2C_USER_INFO_ERROR = 152, // 用户信息格式有问题，登录或者注册
			S2C_USER_NOT_ACTIVATION = 153, // 用户为激活
			S2C_USER_ROLE_IS_NULL = 154, // 创建角色时角色名为空
			S2C_USER_NICKNAME_EXIST = 155, // 昵称存在
			S2C_USER_PASSWORD_NOT_SAME = 156, // 注册时两个密码不一致
			S2C_USER_ACTIVATION_CAN_NOT_USE = 157, // 激活码不可用，，已被注册或者不存在
			S2C_USER_PHONE_USERD = 158, // 电话号已被使用
			S2C_USER_NOT_CREATE_ROLE = 159, // 为创建角色
			S2C_USER_NICKNAME_TOO_LONG = 160, // 角色名过长
			S2C_USER_LOGIN_ON_OTHER_PLACE = 161, // 账号被其他人登录
			S2C_USER_LOGIN_FAILED = 162, // 登录失败,用户名或者密码错误或者压根就没注册
			S2C_USER_INDEX_COMPLETE = 163, // 数据发送完毕
			S2C_PROJECT_NOT_EXIST = 164,//商品不存在
			S2C_SHOP_ITEM = 165,//买到的东西
			S2C_POWER_NOT_ENOUGH=166,//体力不足
			S2C_MUSIC_NOT_UNLOCK=167,//关卡未解锁
			S2C_LIMIT_GIFT_CAN_BUY_ONE_TIME=168,//限购
			S2C_ALL_MUSIC_PASSED=169,//所有音乐已通关
			S2C_UNLOCK_NEXT_MUSIC=170,//解锁下一首歌
			S2C_LEVEL_NOT_ENOUGH_UNLOCK_NEXT_MUSIC=171,//等级不足以解锁下一关
			S2C_IN_GAME=172,//已经在游戏了
			S2C_SCORE_IS_LOSE = 173,//成绩已失效，说明上次已经有过成绩保存或者并没有进行过打歌或者闯关
			S2C_STAR_NOT_FIT_TO_PASS = 174,//星星不足以通关

			// pk 命令设置
			C2S_PK_REQUEST = 201, // pk查找对手
			C2S_PK_PREPARED = 202, // PK准备完成
			C2S_PK_CANCEL = 203, // 请求取消pk
			C2S_PK_RESULT = 204, // pk自己结果

			S2C_PK_TARGET_INFO = 211, // pk对手信息
			S2C_PK_PREPARED = 212, // PK准备结果
			S2C_PK_CANCEL = 213, // 取消pk服务端反馈
			S2C_PK_RESULT = 214, // pk对手成绩结果

			S2C_PK_ERROR = 215, // 匹配出错

			// 每个城市界面中的各类请求
			C2S_PLAIN_DAY_RANK = 301, // 普通游戏日排行请求
			C2S_PLAIN_WEEK_RANK = 302, // 普通游戏周排行请求
			C2S_PLAIN_SEASON_RANK = 303, // 普通游戏赛季排行

			C2S_PLAIN_EASY_SCORE = 304, // 普通游戏普通难度音乐分数统计
			C2S_PLAIN_COMMON_SCORE = 305, // 普通游戏一般难度音乐分数统计
			C2S_PLAIN_HARD_SCORE = 306, // 普通游戏困难难度音乐分数统计

			C2S_PLAIN_PLAY = 307, // 打歌，需说明音乐id及难度,服务端减体力，音乐是否解锁等。
			C2S_PLAIN_PLAY_RESULT = 308, // 普通打歌结果发送,歌单成绩更新，体力变化，金钱

			C2S_PASS_INFO = 309, // 已闯关信息
			C2S_PASS_PLAY = 310, // 开始闯关.不用回复
			C2S_PASS_PLAY_RESULT = 311, // 闯关结果，不用回复


			C2S_PLAIN_ALL_SCORE = 312, // 一次回复三个难度成绩

			C2S_PLAIN_GET_ALl_GRADE = 313,// 获取城市评星总和
			C2S_PLAIN_GET_SEASON_TOTAL_RANK = 314,// 获取赛季总排行
			C2S_GET_PASS_SUM = 315,// 获取通关总数


			S2C_PLAIN_DAY_RANK = 321, // 普通游戏日排行请求,只发送对应的消息
			S2C_PLAIN_WEEK_RANK = 322, // 普通游戏周排行请求
			S2C_PLAIN_SEASON_RANK = 323, // 普通游戏赛季排行

			S2C_PLAIN_EASY_SCORE = 324, // 普通游戏普通难度音乐分数统计
			S2C_PLAIN_COMMON_SCORE = 325, // 普通游戏一般难度音乐分数统计
			S2C_PLAIN_HARD_SCORE = 326, // 普通游戏困难难度音乐分数统计

			S2C_PLAIN_PLAY = 327, // 打歌，需说明音乐id及难度,服务端减体力
			S2c_PLAIN_PLAY_RESULT = 328, // 普通打歌结果发送

			S2C_PASS_INFO = 329, // 已闯关信息
			S2C_PASS_PLAY = 330, // 开始闯关.不用回复
			S2C_PASS_PLAY_RESULT = 341, // 闯关结果回复


			S2C_PLAIN_ALL_SCORE = 342,

			S2C_PLAIN_GET_ALl_GRADE = 343,// 获取城市评星总和
			S2C_PLAIN_GET_SEASON_TOTAL_RANK = 344,// 获取赛季总排行
			S2C_GET_PASS_SUM = 345,// 获取通关总数


			// 好友相关命令码400+
			C2S_FRIEND_ADD_REQUEST = 401, // 添加好友请求
			C2S_FRIEND_LOOK_REQUEST = 402, // 查看为确定的好友请求
			C2S_FRIEND_CONFIRM = 403, // 确定好友请求关系
			C2S_FRIEND_SEARCH_BY_ID = 404, // 根据id查询玩家
			C2S_FRIEND_SEARCH_BY_NAME = 405, // 更具名字查询玩家
			C2S_FRIEND_RECOMMEND = 406, // 请求推荐好友
			C2S_FRIEND_LIST = 407, // 请求查看好友列表

			S2C_FRIEND_ADD_REQUEST = 411, // 添加好友请求
			S2C_FRIEND_LOOK_REQUEST = 412, // 查看为确定的好友请求
			S2C_FRIEND_CONFIRM = 413, // 确定好友请求关系
			S2C_FRIEND_SEARCH_BY_ID = 414, // 根据id查询玩家
			S2C_FRIEND_SEARCH_BY_NAME = 415, // 更具名字查询玩家
			S2C_FRIEND_RECOMMEND = 416, // 请求推荐好友
			S2C_FRIEND_LIST = 417, // 请求查看好友列表

			// 公会相关命令码
			C2S_SOCIETY_CREATE = 501, // 创建公会
			C2S_SOCIETY_APPLY = 502, // 申请加入公会
			C2S_SOCIETY_GET_APPLY = 503, // 获取申请
			C2S_SOCIETY_CONFIRM_APPLY = 504, // 回应申请
			C2S_SOCIETY_QUIT = 506, // 离开公会
			C2S_SOCIETY_UPDATE_NAME = 507, // 更新公会名字
			C2S_SOCIETY_UPDATE_NOTICE = 508, // 更新公会公告
			C2S_SOCIETY_QUERY_MEMBER = 509, // 查询公会成员
			C2S_SOCIETY_ABDICATE = 510, // 让位公会会长
			C2S_SOCIETY_RANK = 511,

			S2C_SOCIETY_CREATE = 521, // 创建公会
			S2C_SOCIETY_APPLY = 522, // 申请加入公会
			S2C_SOCIETY_GET_APPLY = 523, // 获取申请
			S2C_SOCIETY_CONFIRM_APPLY = 524, // 回应申请
			S2C_SOCIETY_QUIT = 526, // 离开公会
			S2C_SOCIETY_UPDATE_NAME = 527, // 更新公会名字
			S2C_SOCIETY_UPDATE_NOTICE = 528, // 更新公会公告
			S2C_SOCIETY_QUERY_MEMBER = 529, // 查询公会成员
			S2C_SOCIETY_ABDICATE = 530, // 让位公会会长
			S2C_SOCIETY_RANK = 531,


			C2S_CHANGE_DRESS = 601,// 换衣服
			C2S_GET_DRESS = 602,// 看衣服

			S2C_CHANGE_DRESS = 611, // 换衣服
			S2C_GET_DRESS = 612, // 看衣服

			C2S_USER_GET_BASE_INFO = 701, // 玩家基本消息
			C2S_USER_UPDATE_RIGNATURE = 702,// 更新签名
			C2S_USER_UPDATE_WEALTH = 703,// 更新财富，，包括金币，钻石，体力、经验等
			C2S_USER_UPDATE_AVATOR = 704,// 更新头像
			C2S_USER_GET_INDEX = 705,// 获取首页信息
			C2S_SHOP_BUY = 706,//商店购买

			S2C_USER_GET_BASE_INFO = 721, // 玩家基本消息
			S2C_USER_UPDATE_RIGNATURE = 722,// 更新签名
			S2C_USER_UPDATE_WEALTH = 723,// 更新财富，，包括金币，钻石，体力、经验等
			S2C_USER_UPDATE_AVATAR = 724,// 更新头像
			S2C_USER_GET_INDEX = 725,// 获取首页信息
			S2C_SHOP_BUY = 726,//商店购买
			S2C_NORMAL_REWARD = 727,//普通奖励

			C2S_DRAW_A_LOTTERY = 1001, // 抽奖
			S2C_MESSAGE_IS_WRONG = 2001;// 消息有问题

}
