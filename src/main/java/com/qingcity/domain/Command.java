package com.qingcity.domain;

import com.qingcity.entity.MsgEntity;

import io.netty.handler.codec.http.FullHttpRequest;

public class Command {
	private int id;
	@SuppressWarnings("unused")
	private MsgEntity messageData;
	@SuppressWarnings("unused")
	private ERequestType requestType;
	FullHttpRequest request;

	public Command(ERequestType requestType, MsgEntity msg) {
		this.requestType = requestType;
		if (requestType == ERequestType.SOCKET) {
			this.messageData = msg;
			this.id = msg.getCmdCode();
		} else {
			System.out.println("Command default");
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
}
