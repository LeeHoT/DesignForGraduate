package com.qingcity.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qingcity.util.StringUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

public class GameResponse {
	private ERequestType requestType;
	private Command command;
	private Channel channel;
	private Object rtMessage;
	private String gain;
	private Map<String, Integer> tempMap = new HashMap<>();

	public GameResponse(Channel channel, Command command, ERequestType requestType) {
		this.channel = channel;
		this.command = command;
		this.requestType = requestType;
		this.rtMessage = Unpooled.buffer();
		this.gain="";
		//write(Integer.valueOf(command.getId()));
	}

	public String getGain() {
		return gain;
	}

	public void setGain(String gain) {
		if(!StringUtil.isEmpty(gain)){
			String ss[] = gain.split(";");
			for(String s:ss){
				String temp[] = s.split(",");
				if(tempMap.containsKey(temp[0])){
					tempMap.put(temp[0], tempMap.get(temp[0])+Integer.valueOf(temp[1]));
				}else{
					tempMap.put(temp[0], Integer.valueOf(temp[1]));
				}
			}
		}
		//再装换成String
		this.gain = StringUtil.map2String(tempMap);
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

	public void setCommand(Command command) {
		this.command = command;
	}

	public Channel getChannel() {
		return this.channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Object getRtMessage() {
		return this.rtMessage;
	}

	public void setRtMessage(Object rtMessage) {
		this.rtMessage = rtMessage;
	}

//	@SuppressWarnings("unchecked")
//	public void write(Object obj) {
//		switch (requestType) {
//		case HTTP:
//		case WEBSOCKET_TEXT:
//			((List<Object>) this.rtMessage).add(obj);
//			break;
//		case SOCKET:
//		case WEBSOCKET_BINARY:
//			ByteBuf buf = (ByteBuf) this.rtMessage;
//			if (obj == null)
//				return;
//			if ((obj instanceof String)) {
//				String tmp = (String) obj;
//				buf.writeInt(tmp.getBytes().length);
//				buf.writeBytes(tmp.getBytes());
//				return;
//			}
//			if ((obj instanceof Short))
//				buf.writeShort(((Short) obj).shortValue());
//			else if ((obj instanceof Integer))
//				buf.writeInt(((Integer) obj).intValue());
//			else if ((obj instanceof Long))
//				buf.writeLong(((Long) obj).longValue());
//			else if ((obj instanceof Float))
//				buf.writeFloat(((Float) obj).floatValue());
//			else if ((obj instanceof Byte))
//				buf.writeByte(((Byte) obj).byteValue());
//			break;
//		}
//	}
}
