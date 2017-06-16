package com.qingcity.base.handler;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.protobuf.GeneratedMessage;
import com.qingcity.base.constants.CmdConstant;
import com.qingcity.base.domain.GameResponse;
import com.qingcity.entity.MsgEntity;
import com.qingcity.proto.PlayerInfo.S2C_Result;

import io.netty.channel.Channel;

public abstract class HandlerMsg {
	@Autowired
	private MsgEntity resEntity;
	
	public boolean isErrorMsg(MsgEntity msgEntity, GameResponse response) {
		// 消息错误,返回错误信息
		if (msgEntity.getMsgLength() != msgEntity.getData().length) {
			S2C_Result.Builder s2c_result = S2C_Result.newBuilder();
			s2c_result.setResult(false);
			handlerResMsg(s2c_result.build(), CmdConstant.S2C_MESSAGE_IS_WRONG, msgEntity.getUserId(), response);
			return true;
		}
		return false;
	}

	/**
	 * 处理返回消息使用该。。 统一进行序列化
	 * 
	 * @param gm
	 * @param cmd
	 *            消息命令码
	 * @param response
	 *            response对象
	 */
	public void handlerResMsg(GeneratedMessage gm, Short cmd, int userId, GameResponse response) {
		resEntity.setMsgLength(gm.toByteArray().length);
		resEntity.setCmdCode(cmd);
		resEntity.setData(gm.toByteArray());
		resEntity.setUserId(userId);
		response.setRtMessage(resEntity);
		response.getChannel().writeAndFlush(response.getRtMessage());
	}

	/**
	 * 处理返回消息使用该。。 统一进行序列化
	 * 
	 * @param gm
	 * @param cmd
	 *            消息命令码
	 * @param channel
	 *            channel对象
	 */
	public void handlerResMsg(GeneratedMessage gm, Short cmd, int userId, Channel channel) {
		MsgEntity rMsg = new MsgEntity();
		rMsg.setMsgLength(gm.toByteArray().length);
		rMsg.setCmdCode(cmd);
		rMsg.setUserId(userId);
		rMsg.setData(gm.toByteArray());
		channel.writeAndFlush(rMsg);
	}
}
