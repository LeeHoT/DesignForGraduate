package com.qingcity.entity;

import java.io.Serializable;

/**
 * 后台处理逻辑的核心实体类
 */

public class MsgEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private int msgLength;
	private short cmdCode;// 储存命令码
	private String requestType;//请求类型
	private int userId;
	private byte[] data;// 存放实际数据,用于protobuf解码成对应message
	

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public int getMsgLength() {
		return msgLength;
	}

	public void setMsgLength(int msgLength) {
		this.msgLength = msgLength;
	}

	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public short getCmdCode() {
		return cmdCode;
	}

	public void setCmdCode(short cmdCode) {
		this.cmdCode = cmdCode;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
}
