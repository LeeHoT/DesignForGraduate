package com.qingcity.domain;

import com.qingcity.entity.MsgEntity;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class GameRequest {
	private ERequestType requestType;
	private Command command;
	private Channel channel;
	private ChannelHandlerContext ctx;
	private MsgEntity msg;

	public GameRequest(ChannelHandlerContext ctx, ERequestType requestType, MsgEntity msg) {
		try {
			this.ctx = ctx;
			this.channel = ctx.channel();
			this.requestType = requestType;
			this.msg = msg;
			this.command = new Command(requestType, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	public MsgEntity getMsg() {
		return msg;
	}

	public void setMsg(MsgEntity msg) {
		this.msg = msg;
	}

	public GameRequest(Channel channel, Command command) {
		this.channel = channel;
		this.command = command;
	}

	public GameRequest(Command command) {
		this.channel = null;
		this.command = command;
	}

	public Channel getChannel() {
		return this.channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

	public int getCommandId() {
		if (this.command != null) {
			return this.command.getId();
		}
		return -1;
	}

	public ERequestType getRequestType() {
		return this.requestType;
	}

	public void setRequestType(ERequestType requestType) {
		this.requestType = requestType;
	}

	public Command getCommand() {
		return this.command;
	}
}