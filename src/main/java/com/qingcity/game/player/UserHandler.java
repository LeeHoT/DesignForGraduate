package com.qingcity.game.player;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.protobuf.InvalidProtocolBufferException;
import com.qingcity.base.handler.CmdHandler;
import com.qingcity.base.handler.HandlerMsg;
import com.qingcity.base.util.ExceptionUtils;
import com.qingcity.base.util.StringUtil;
import com.qingcity.base.util.TimeUtil;
import com.qingcity.base.constants.ChatConstant;
import com.qingcity.base.constants.CmdConstant;
import com.qingcity.data.manager.PlayerChannelManager;
import com.qingcity.data.manager.PlayerManager;
import com.qingcity.data.manager.WorldGroup;
import com.qingcity.base.domain.GameResponse;
import com.qingcity.entity.MsgEntity;
import com.qingcity.entity.UserEntity;
import com.qingcity.proto.PlayerInfo.C2S_CreateRole;
import com.qingcity.proto.PlayerInfo.C2S_Login;
import com.qingcity.proto.PlayerInfo.C2S_Register;
import com.qingcity.proto.PlayerInfo.S2C_CreateRole;
import com.qingcity.proto.PlayerInfo.S2C_Result;
import com.qingcity.sd.manager.BadWordInit;
import com.qingcity.service.PlayerService;
import com.qingcity.service.UserService;

/**
 * 
 * @author leehotin
 * @Date 2017年4月27日 下午5:05:43
 * @Description 处理所有用户登录、注册、添加个人信息等请求
 *              特殊说明。。用户名就是手机号，目前用户名（username）字段已被抛弃，但是人存留着
 */
@Controller("userHandler")
public class UserHandler extends HandlerMsg implements CmdHandler {
	private static final Logger logger = LoggerFactory.getLogger(UserHandler.class);

	@Autowired
	private UserService userService;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private PlayerHandler playerHanler;

	public void handleMsg(MsgEntity msgEntity, GameResponse response) {
		if (isErrorMsg(msgEntity, response)) {
			logger.info("实际消息和原消息不符!长度不一致。");
			return;
		}
		switch (msgEntity.getCmdCode()) {// 根据命令码对应找到对应处理方法
		case CmdConstant.C2S_USER_LOGIN:
			handleLogin(msgEntity, response);
			break;
		case CmdConstant.C2S_USER_REGISTER:
			handleRegister(msgEntity, response);
			break;
		case CmdConstant.C2S_USER_CREATE_ROLE:
			handlerCreateRole(msgEntity, response);
			break;
		case CmdConstant.C2S_USER_GET_INDEX:
			index(msgEntity, response);
			break;
		default:
			logger.error("==============>: 命令码[{}]找不到", msgEntity.getCmdCode());
		}
	}

	/**
	 * 验证用户名和密码格式和内容是否符合最低标准
	 * 
	 * @param user
	 * @return
	 */
	public boolean validate(String phone, String password) {
		boolean flag = true; // 用户是否已登录成功
		try {
			if (StringUtil.isNull(phone) || phone.trim().getBytes("GBK").length != 11 || !isMobileNO(phone)) {
				// 用户名为空或用户名过短，无法登录
				logger.info("==============>: 手机号格式错误");
				flag = false;
			} else if (StringUtil.isNull(password) || password.length() < 6 || password.length() > 16) {
				// 密码为空，无法登录
				logger.info("==============>: 密码格式错误");
				flag = false;
			}
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
			;
			return false;
		}
		return flag;
	}

	private boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 处理用户登录请求
	 * 
	 * @param msgEntity
	 *            消息体 包含消息长度，协议号以及消息体
	 * @param response
	 *            返回消息对象
	 */
	private void handleLogin(MsgEntity msgEntity, GameResponse response) {

		S2C_Result.Builder s2c_result = S2C_Result.newBuilder();
		try {
			C2S_Login c2s_login = C2S_Login.parseFrom(msgEntity.getData());
			if (!validate(c2s_login.getPhone(), c2s_login.getPassword())) {
				handlerResMsg(s2c_result.build(), CmdConstant.S2C_USER_INFO_ERROR, 0, response);
				return;
			}
			int userId = userService.login(c2s_login.getPhone(), c2s_login.getPassword());
			if (userId == -1) {
				handlerResMsg(s2c_result.build(), CmdConstant.S2C_USER_LOGIN_FAILED, 0, response);
				return;
			}
			s2c_result.setUserId(userId);
			handlerResMsg(s2c_result.build(), CmdConstant.S2C_USER_LOGIN, userId, response);
			
			// 登录成功，记录玩家channel
			PlayerManager.getInstance().add(response.getChannel());
			PlayerChannelManager.getInstance().add(userId, response.getChannel());

			if (!userService.checkHaveCreatedRole(userId)) {
				// 登录成功，但是没有创建角色
				handlerResMsg(s2c_result.build(), CmdConstant.S2C_USER_NOT_CREATE_ROLE, userId, response);
				return;
			}
			msgEntity.setUserId(userId);
			
			// 处理登录成功的逻辑函数
			index(msgEntity, response);
		} catch (InvalidProtocolBufferException e1) {
			e1.printStackTrace();
		}
	}

	public void handlerCreateRole(MsgEntity msgEntity, GameResponse response) {
		S2C_CreateRole.Builder s2c_createRole = S2C_CreateRole.newBuilder();
		try {
			C2S_CreateRole c2s_createRole = C2S_CreateRole.parseFrom(msgEntity.getData());
			int userId = msgEntity.getUserId();
			if (StringUtil.isNull(c2s_createRole.getNickname())) {
				// 昵称为空
				logger.info("==============>: 昵称格式有误");
				handlerResMsg(s2c_createRole.build(), CmdConstant.S2C_USER_ROLE_IS_NULL, userId, response);
				return;
			}
			if (BadWordInit.getInstance().containBadWord(c2s_createRole.getNickname())) {
				logger.info("==============>: 昵称包含非法字符");
				handlerResMsg(s2c_createRole.build(), CmdConstant.S2C_USER_ROLE_IS_WORNG, userId, response);
				return;
			}

			if (c2s_createRole.getNickname().trim().getBytes("GBK").length > 16) {
				logger.info("==============>: 昵称长度超过限制");
				handlerResMsg(s2c_createRole.build(), CmdConstant.S2C_USER_NICKNAME_TOO_LONG, userId, response);
				return;
			}
			// 名字格式符合要求
			boolean result = playerService.initPlayer(userId, c2s_createRole.getNickname());
			if (!result) {
				handlerResMsg(s2c_createRole.build(), CmdConstant.S2C_USER_NICKNAME_EXIST, userId, response);
				return;
			}
			handlerResMsg(s2c_createRole.build(), CmdConstant.S2C_USER_CREATE_ROLE, userId, response);
			index(msgEntity, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 处理用户注册请求
	 * 
	 * @param msgEntity
	 * @param response
	 */

	private synchronized void handleRegister(MsgEntity msgEntity, GameResponse response) {
		S2C_Result.Builder s2c_result = S2C_Result.newBuilder();
		try {
			C2S_Register c2s_register = C2S_Register.parseFrom(msgEntity.getData());
			String phone = c2s_register.getPhone();
			String password = c2s_register.getPassword();
			if (!validate(phone, password) || StringUtil.isNull(c2s_register.getPassword())|| StringUtil.isNull(c2s_register.getPassword2())) {
				handlerResMsg(s2c_result.build(), CmdConstant.S2C_USER_INFO_ERROR, 0, response);
				return;
			}
			// 判断两次密码是否一致
			if (!c2s_register.getPassword().equals(c2s_register.getPassword2())) {
				handlerResMsg(s2c_result.build(), CmdConstant.S2C_USER_PASSWORD_NOT_SAME, 0, response);
				return;
			}
			// 符合条件
			UserEntity user = new UserEntity();
			user.setPhone(phone);
			user.setPasswordMd5(password);
			user.setRegTime(TimeUtil.getCurrentTimestamp());
			int result = userService.register(user);
			if (result == -1) {
				handlerResMsg(s2c_result.build(), CmdConstant.S2C_USER_ACTIVATION_CAN_NOT_USE, 0, response);
				return;
			}
			if (result == -2) {
				handlerResMsg(s2c_result.build(), CmdConstant.S2C_USER_PHONE_USERD, 0, response);
				return;
			}
			// 注册成功
			logger.info("==============>: 账号[{}]注册成功",phone);
			handlerResMsg(s2c_result.build(), CmdConstant.S2C_USER_REGISTER, user.getUserId(), response);
		} catch (InvalidProtocolBufferException e) {
			ExceptionUtils.getStackTrace(e);
		}
	}

	/**
	 * 登录成功
	 * 	 */
	public void index(MsgEntity msgEntity, GameResponse response) {
		S2C_Result.Builder s2c_result = S2C_Result.newBuilder();
		s2c_result.setResult(true);
		// 分配玩家聊天频道
		//PlayerEntity player = playerService.selectByUserId(msgEntity.getUserId());
		WorldGroup.getInstance().addToGroup(ChatConstant.WORLD_MSG, response.getChannel());
		// 下行玩家基本信息
		playerHanler.handleGetPlayerInfo(msgEntity, response);
	}
}
