package com.qingcity.handler;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.protobuf.InvalidProtocolBufferException;
import com.qingcity.constants.ChatConstant;
import com.qingcity.constants.CmdConstant;
import com.qingcity.data.manager.PlayerChannelManager;
import com.qingcity.data.manager.PlayerManager;
import com.qingcity.data.manager.SocietyGroup;
import com.qingcity.data.manager.WorldGroup;
import com.qingcity.domain.GameResponse;
import com.qingcity.entity.MsgEntity;
import com.qingcity.entity.PlayerEntity;
import com.qingcity.entity.UserEntity;
import com.qingcity.init.manager.BadWordInit;
import com.qingcity.proto.PlayerInfo.C2S_CreateRole;
import com.qingcity.proto.PlayerInfo.C2S_Login;
import com.qingcity.proto.PlayerInfo.C2S_Register;
import com.qingcity.proto.PlayerInfo.S2C_CreateRole;
import com.qingcity.proto.PlayerInfo.S2C_Result;

import com.qingcity.service.PlayerService;
import com.qingcity.service.UserService;
import com.qingcity.util.ExceptionUtils;
import com.qingcity.util.StringUtil;
import com.qingcity.util.TimeUtil;

/**
 * 
 * @author leehotin
 * @Date 2017年2月27日 下午5:05:43
 * @Description 处理所有用户登录、注册、添加个人信息等请求
 *              特殊说明。。用户名就是手机号，目前用户名（username）字段已被抛弃，但是人存留着
 * 
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
			logger.error("实际消息和原消息不符!长度不一致。");
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
			System.out.println("player create role");
			handlerCreateRole(msgEntity, response);
			break;
		case CmdConstant.C2S_USER_GET_INDEX:
			index(msgEntity, response);
			handleLogin(msgEntity, response);
			break;
		default:
			logger.warn("命令码[{}]找不到", msgEntity.getCmdCode());
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
				logger.debug("手机号格式错误");
				flag = false;
			} else if (StringUtil.isNull(password) || password.length() < 6 || password.length() > 16) {
				// 密码为空，无法登录
				logger.debug("密码格式错误");
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
				// s2c_result.setResult(false);
				handlerResMsg(s2c_result.build(), CmdConstant.S2C_USER_INFO_ERROR, 0, response);
				return;
			}
			int userId = userService.login(c2s_login.getPhone(), c2s_login.getPassword());
			if (userId == -1) {
				// s2c_result.setResult(false);
				// 登录失败
				System.out.println("login failed");
				handlerResMsg(s2c_result.build(), CmdConstant.S2C_USER_LOGIN_FAILED, 0, response);
				return;
			}
			System.out.println("player  id  is " + userId);
			// s2c_result.setResult(true);
			s2c_result.setUserId(userId);
			handlerResMsg(s2c_result.build(), CmdConstant.S2C_USER_LOGIN, userId, response);
			
			// 登录成功，记录玩家channel
			PlayerManager.getInstance().add(response.getChannel());
			PlayerChannelManager.getInstance().add(userId, response.getChannel());

			if (!userService.checkHaveCreatedRole(userId)) {
				// s2c_result.setResult(false);
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
				System.out.println("昵称格式有误");
				// s2c_createRole.setResult(false);
				handlerResMsg(s2c_createRole.build(), CmdConstant.S2C_USER_ROLE_IS_NULL, userId, response);
				return;
			}
			if (BadWordInit.getInstance().containBadWord(c2s_createRole.getNickname())) {
				System.out.println("昵称包含屏蔽字");
				handlerResMsg(s2c_createRole.build(), CmdConstant.S2C_USER_ROLE_IS_NULL, userId, response);
				return;
			}

			if (c2s_createRole.getNickname().trim().getBytes("GBK").length > 16) {
				// s2c_createRole.setResult(false);
				handlerResMsg(s2c_createRole.build(), CmdConstant.S2C_USER_NICKNAME_TOO_LONG, userId, response);
				return;
			}
			// 名字格式符合要求
			System.out.println("===============要注册的用户名为 " + c2s_createRole.getNickname());
			System.out.println(c2s_createRole.getNickname().trim());
			boolean result = playerService.initPlayer(userId, c2s_createRole.getNickname());
			if (!result) {
				// s2c_createRole.setResult(false);
				handlerResMsg(s2c_createRole.build(), CmdConstant.S2C_USER_NICKNAME_EXIST, userId, response);
				return;
			}
			// s2c_createRole.setResult(true);
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
			if (!validate(phone, password) || StringUtil.isNull(c2s_register.getPassword())
					|| StringUtil.isNull(c2s_register.getPassword2())) {
				// 处理protobuf消息
				// s2c_result.setResult(false);
				handlerResMsg(s2c_result.build(), CmdConstant.S2C_USER_INFO_ERROR, 0, response);
				return;
			}
			// 判断两次密码是否一致
			if (!c2s_register.getPassword().equals(c2s_register.getPassword2())) {
				// s2c_result.setResult(false);
				handlerResMsg(s2c_result.build(), CmdConstant.S2C_USER_PASSWORD_NOT_SAME, 0, response);
				return;
			}
			// 符合条件
			// TODO 还需要接着完善
			UserEntity user = new UserEntity();
			user.setPhone(phone);
			user.setPasswordMd5(password);
			user.setRegTime(TimeUtil.getCurrentTimestamp());
			int result = userService.register(user, c2s_register.getCode());
			if (result == -1) {
				// s2c_result.setResult(false);
				handlerResMsg(s2c_result.build(), CmdConstant.S2C_USER_ACTIVATION_CAN_NOT_USE, 0, response);
				return;
			}
			if (result == -2) {
				// s2c_result.setResult(false);
				handlerResMsg(s2c_result.build(), CmdConstant.S2C_USER_PHONE_USERD, 0, response);
				return;
			}
			// 注册成功
			System.out.println("register success");
			// s2c_result.setResult(true);
			handlerResMsg(s2c_result.build(), CmdConstant.S2C_USER_REGISTER, user.getUserId(), response);
		} catch (InvalidProtocolBufferException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * 登录成功
	 * 	 */
	public void index(MsgEntity msgEntity, GameResponse response) {
		S2C_Result.Builder s2c_result = S2C_Result.newBuilder();
		s2c_result.setResult(true);
		
		// 分配玩家频道
		// 查询玩家公会
		PlayerEntity player = playerService.selectByUserId(msgEntity.getUserId());
		SocietyGroup.getInstance().addToGroup(player.getSocietyId(), response.getChannel());
		WorldGroup.getInstance().addToGroup(ChatConstant.WORLD_MSG, response.getChannel());
		// 下发消息
		// 下发玩家基本信息
		System.out.println("消息投中玩家id" + msgEntity.getUserId());
		playerHanler.handleGetPlayerInfo(msgEntity, response);

		handlerResMsg(s2c_result.build(), CmdConstant.S2C_USER_INDEX_COMPLETE, msgEntity.getUserId(), response);

	}
}
