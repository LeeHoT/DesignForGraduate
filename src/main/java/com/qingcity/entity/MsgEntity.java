package com.qingcity.entity;

import java.io.Serializable;

/**
 * 
 * @author leehotin
 * @Date 2017年4月14日 下午1:53:10
 * @Description 后台处理逻辑的核心实体类
 */
public class MsgEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private int msgLength;
	private short cmdCode;// 储存命令码
	private int userId;
	private byte[] data;// 存放实际数据,用于protobuf解码成对应message

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
