package com.qingcity.base.constants;

/**
 * 命令码常量检测
 */
public interface CmdConstant {

	short   
	        /** 登录验证(不可直接转发)*/
			C2S_USER_LOGIN = 10,
			/** 注册验证(不可直接转发)*/
			C2S_USER_REGISTER = 11,
			/** 创建角色(不可直接转发)*/
			C2S_USER_CREATE_ROLE = 12,
			/** 玩家基本消息*/
			C2S_USER_GET_BASE_INFO = 13,
			/** 更新签名(不可直接转发)*/
			C2S_USER_UPDATE_RIGNATURE = 14,
			/** (弃用)更新财富，，包括金币，钻石，体力、经验等*/
			//C2S_USER_UPDATE_WEALTH = 15,
			/** 更新头像(不可直接转发)*/
			C2S_USER_UPDATE_AVATOR = 16,
			/** 获取首页信息*/
			C2S_USER_GET_INDEX = 17,
			/** 购买商品*/
			C2S_SHOP_BUY = 18,
			/** test*/
			C2S_TEST_HANDLER = 19,
			
			/** 登录验证*/
			S2C_USER_LOGIN = 1010,
		    /** 注册验证*/
			S2C_USER_REGISTER = 1011,
			/** 创建角色*/
			S2C_USER_CREATE_ROLE = 1012,
			/** 玩家基本消息*/
			S2C_USER_GET_BASE_INFO = 1013,
			/** 更新签名*/
			S2C_USER_UPDATE_RIGNATURE = 1014,
			/** (弃用)更新财富，，包括金币，钻石，体力、经验等*/
			//S2C_USER_UPDATE_WEALTH = 1015,
			/** 更新头像*/
			S2C_USER_UPDATE_AVATAR = 1016,
			/** 获取首页信息*/
			S2C_USER_GET_INDEX = 1017,
			/** 购买商品*/
			S2C_SHOP_BUY = 1018,
			/** test*/
			S2C_TEST_HANDLER = 1019,

			//错误码
            /** 未登录*/
			S2C_USER_NOT_LOGIN = 10001, 
			/** 用户信息格式有问题，登录或者注册*/
			S2C_USER_INFO_ERROR = 10002,
			/** 用户未激活（弃用）*/
			//S2C_USER_NOT_ACTIVATION = 10003,  
			/** 创建角色时角色名为空*/
			S2C_USER_ROLE_IS_NULL = 10004,
			/** 创建角色时包含非法字符*/
			S2C_USER_ROLE_IS_WORNG = 10005,
			/** 昵称存在*/
			S2C_USER_NICKNAME_EXIST = 10006,
			/** 注册时两个密码不一致*/
			S2C_USER_PASSWORD_NOT_SAME = 10007,
			/** 激活码不可用，，已被注册或者不存在*/
			S2C_USER_ACTIVATION_CAN_NOT_USE = 10008,
			/** 电话号已被使用*/
			S2C_USER_PHONE_USERD = 10009,
			/** 为创建角色*/
			S2C_USER_NOT_CREATE_ROLE = 10010,
			/** 角色名过长*/
			S2C_USER_NICKNAME_TOO_LONG = 10011,
			/** 账号被其他人登录*/
			S2C_USER_LOGIN_ON_OTHER_PLACE = 10012,
			/** 登录失败,用户名或者密码错误或者压根就没注册*/
			S2C_USER_LOGIN_FAILED = 10013,
			/** 数据发送完毕*/
			S2C_USER_INDEX_COMPLETE = 10014,
			/** 体力不足*/
			S2C_POWER_NOT_ENOUGH=10015,
			/** 已经在游戏了*/
			S2C_IN_GAME=10016,
			
			/** 消息有问题*/
			S2C_MESSAGE_IS_WRONG = 2001;
}
