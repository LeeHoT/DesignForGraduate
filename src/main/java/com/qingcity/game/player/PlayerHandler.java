package com.qingcity.game.player;

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
import com.qingcity.base.constants.CmdConstant;
import com.qingcity.base.domain.GameResponse;
import com.qingcity.entity.MsgEntity;
import com.qingcity.entity.PlayerEntity;
import com.qingcity.proto.PlayerInfo.C2S_UpdateAvatar;
import com.qingcity.proto.PlayerInfo.C2S_UpdateSignature;
import com.qingcity.proto.PlayerInfo.Player;
import com.qingcity.proto.PlayerInfo.S2C_GetPlayerInfo;
import com.qingcity.proto.PlayerInfo.S2C_Result;
import com.qingcity.proto.PlayerInfo.Wealth;
import com.qingcity.service.PlayerService;

@Controller("playerHandler")
public class PlayerHandler extends HandlerMsg implements CmdHandler {
	private static final Logger logger = LoggerFactory.getLogger(UserHandler.class);
	
	private static final int SIGNATURE_LENGTH_LIMITED = 100;//100字节

	@Autowired
	private PlayerService playerService;

	@Override
	public void handleMsg(MsgEntity msgEntity, GameResponse response)
			throws Exception {
		if (isErrorMsg(msgEntity, response)) {
			return;
		}
		switch (msgEntity.getCmdCode()) {// 根据命令码对应找到对应处理方法
		case CmdConstant.C2S_USER_GET_BASE_INFO:
			handleGetPlayerInfo(msgEntity, response);
			break;
		case CmdConstant.C2S_USER_UPDATE_RIGNATURE:
			handleUpdateRignature(msgEntity, response);
			break;
		case CmdConstant.C2S_USER_UPDATE_AVATOR:
			handleUpdateAvator(msgEntity, response);
			break;
		default:
			break;
		}
	}

	/**
	 * 获取基本信息
	 * @param msgEntity
	 * @param response
	 */
	public void handleGetPlayerInfo(MsgEntity msgEntity, GameResponse response) {
		S2C_GetPlayerInfo.Builder s2c_getPlayerInfo = S2C_GetPlayerInfo.newBuilder();
		try {
			PlayerEntity player = playerService.selectByUserId(msgEntity.getUserId());
			if(player!=null){
				Player.Builder playerProto= parsePlayerInfo(player, response);
				s2c_getPlayerInfo.setPlayer(playerProto);
			}else{
				logger.error("=============>: 玩家"+msgEntity.getUserId()+"不存在");
				return;
			}
			handlerResMsg(s2c_getPlayerInfo.build(), CmdConstant.S2C_USER_GET_BASE_INFO, msgEntity.getUserId(),response);
		} catch (Exception e) {
			logger.error("=============>: "+ExceptionUtils.getStackTrace(e));
		}
	}
	
	/**
	 * 更新个人签名
	 * 
	 * @param msgEntity
	 * @param response
	 */
	public void handleUpdateRignature(MsgEntity msgEntity, GameResponse response) {
		S2C_Result.Builder s2c_result = S2C_Result.newBuilder();
		try {
			C2S_UpdateSignature c2s_updateSignature = C2S_UpdateSignature.parseFrom(msgEntity.getData());
			if(StringUtil.isEmpty(c2s_updateSignature.getSignature())||c2s_updateSignature.getSignature().getBytes("GBK").length>SIGNATURE_LENGTH_LIMITED){
				s2c_result.setResult(false);
			}else{
				PlayerEntity player = new PlayerEntity();
				player.setUserId(msgEntity.getUserId());
				player.setSignature(c2s_updateSignature.getSignature());
				playerService.updateByUserIdSelective(player);
				s2c_result.setResult(true);
			}
			handlerResMsg(s2c_result.build(), CmdConstant.S2C_USER_UPDATE_AVATAR,msgEntity.getUserId(), response);
		} catch (Exception e) {
			logger.error("=============>: 更新个人签名异常");
			logger.error("=============>: "+ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * 更换头像
	 * 
	 * @param msgEntity
	 * @param response
	 */
	public void handleUpdateAvator(MsgEntity msgEntity, GameResponse response) {
		S2C_Result.Builder s2c_result = S2C_Result.newBuilder();
		try {
			C2S_UpdateAvatar c2s_updateAvatar = C2S_UpdateAvatar.parseFrom(msgEntity.getData());
			PlayerEntity player = new PlayerEntity();
			player.setUserId(msgEntity.getUserId());
			player.setIcon(c2s_updateAvatar.getAvatar()+"");
			playerService.updateByUserIdSelective(player);
			handlerResMsg(s2c_result.build(), CmdConstant.S2C_USER_UPDATE_AVATAR, msgEntity.getUserId(), response);
		} catch (InvalidProtocolBufferException e) {
			logger.error("=============>: 更新头像异常");
			logger.error("=============>: "+ExceptionUtils.getStackTrace(e));
		}
	}

	private Player.Builder parsePlayerInfo(PlayerEntity player,GameResponse response) {
		Player.Builder player2 = Player.newBuilder();
		Wealth.Builder wealth = Wealth.newBuilder();
		wealth.setDiamond(player.getDiamond());
		wealth.setGold(player.getGold());
		player2.setNickname(player.getNickname())
				.setLevel(player.getLevel())
				.setExperience(player.getExperience())
				.setPower(player.getPower())
				.setSignature(player.getSignature())
				.setAvatar(player.getIcon()==null?0:Integer.parseInt(player.getIcon()))
				.setSocietyId(player.getSocietyId())
				.setContribution(player.getContribution())
				.setLastLoginTime(TimeUtil.Timestamp2String((player.getLastLoginTime())))
				.setWealth(wealth);
		return player2;
	}
}
