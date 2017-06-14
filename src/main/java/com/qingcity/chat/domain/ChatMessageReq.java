package com.qingcity.chat.domain;

import com.qingcity.entity.MsgEntity;
import com.qingcity.proto.Chat.C2S_Chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * @author leehotin
 * @Date 2017年4月6日 下午5:31:07
 * @Description 聊天消息请求整理
 */
public class ChatMessageReq {

	
	private C2S_Chat chatMsg;
	private Channel channel;
	private ChannelHandlerContext ctx;
	private int userId;

	public ChatMessageReq(ChannelHandlerContext ctx, MsgEntity msg) {
		try {
			
			this.chatMsg =C2S_Chat.parseFrom(msg.getData());
			this.ctx = ctx;
			this.channel = ctx.channel();
			userId = msg.getUserId();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public C2S_Chat getChatMsg() {
		return chatMsg;
	}

	public void setChatMsg(C2S_Chat chatMsg) {
		this.chatMsg = chatMsg;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public C2S_Chat getChatMessage() {
		return chatMsg;
	}

	public void setChatMessage(C2S_Chat obj) {
		this.chatMsg = obj;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

}
