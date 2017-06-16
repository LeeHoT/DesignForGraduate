package com.qingcity.chat.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.qingcity.base.util.ExceptionUtils;
import com.qingcity.base.util.StringUtil;
import com.qingcity.chat.domain.ChatMessageReq;
import com.qingcity.base.constants.ChatConstant;
import com.qingcity.data.manager.WorldGroup;
import com.qingcity.entity.MsgEntity;
import com.qingcity.entity.PlayerEntity;
import com.qingcity.proto.Chat.C2S_Chat;
import com.qingcity.proto.Chat.S2C_Chat;
import com.qingcity.service.PlayerService;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

/**
 * 
 * @author leehotin
 * @Date 2017年4月6日 下午5:23:24
 * @Description 处理世界消息
 */
@Controller
public class WorldMessageHandler implements ChatMessageHandler {

	private static Logger logger = LoggerFactory.getLogger(WorldMessageHandler.class);
	@Autowired
	private PlayerService playerService;

	@Override
	public void process(ChatMessageReq msgReq) {
		ChannelGroup channels = WorldGroup.getInstance().getChannels(msgReq.getChatMessage().getTarget());
		if (channels == null) {
			// 世界频道不存在
			logger.error("==============>: 在发布世界消息的时候，玩家已登录，却不存在世界频道");
			return;
		}
		sendMessage(msgReq);
	}

	@Override
	public void sendMessage(ChatMessageReq msgReq) {
		try {
			// 查到当前世界对应的所有玩家的channels
			ChannelGroup channels = WorldGroup.getInstance().getChannels(msgReq.getChatMessage().getTarget());
			// 获取发送过来的消息
			C2S_Chat chatMessage = msgReq.getChatMsg();
			S2C_Chat.Builder s2c_chat = S2C_Chat.newBuilder();
			String content = chatMessage.getContent();
			if(StringUtil.isNull(content)){
				return;
			}
			int userId = msgReq.getUserId();
			PlayerEntity playerEntity = playerService.selectByUserId(userId);
			if(playerEntity==null){
				return;
			}
			s2c_chat.setContent(chatMessage.getContent());
			s2c_chat.setNickname(playerEntity.getNickname());
			s2c_chat.setAvatar(playerEntity.getIcon()+"");
			s2c_chat.setNickname(playerEntity.getNickname());
			byte[] chatMsg = s2c_chat.build().toByteArray();
			MsgEntity worldRes = new MsgEntity();
			worldRes.setMsgLength(chatMsg.length);
			worldRes.setCmdCode((short) ChatConstant.WORLD_MSG);
			worldRes.setData(chatMsg);
			
			int sendNum = 0;
			for(Channel channel :channels){
				if(channel == msgReq.getChannel()){
					continue;
				}
				channel.writeAndFlush(worldRes);
				sendNum++;
			}
			logger.info("=============>: 聊天频道[{}]共有[{}]位玩家在线,成功转发消息给[{}]个玩家",ChatConstant.WORLD_MSG,channels.size(),sendNum);
		} catch (Exception e) {
			logger.error("=============>: 在转发世界消息的时候发生了异常：" + ExceptionUtils.getStackTrace(e));
		}

	}

}
