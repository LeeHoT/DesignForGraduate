package com.qingcity.base.domain;

import com.qingcity.entity.MsgEntity;

import io.netty.handler.codec.http.FullHttpRequest;

public class Command {
	private int id;
	private MsgEntity messageData;
	private ERequestType requestType;
	FullHttpRequest request;

	public Command(ERequestType requestType, MsgEntity msg) {
		this.requestType = requestType;
		if (requestType == ERequestType.SOCKET) {
			this.messageData = msg;
			this.id = msg.getCmdCode();
		}
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setRequestType(ERequestType requestType) {
		this.requestType = requestType;
	}

	public MsgEntity getMessageData() {
		return messageData;
	}

	public void setMessageData(MsgEntity messageData) {
		this.messageData = messageData;
	}

	public ERequestType getRequestType() {
		return requestType;
	}
}
