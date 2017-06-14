package com.qingcity.netty;

import com.qingcity.entity.MsgEntity;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 服务端这里继承<code>MessageToByteEncoder</code>更加方便
 */
public class NettyMsgEncoder extends MessageToByteEncoder<MsgEntity> {

	@Override
	protected void encode(ChannelHandlerContext ctx, MsgEntity msg, ByteBuf byteBuf) throws Exception {
		int dataLength = msg.getData() == null ? 0 : msg.getData().length;
		// 消息体前缀的长度 int(4)+short(2)+long(8)+int(4)
		int prefix = 18;
		byteBuf.ensureWritable(prefix + dataLength);
		//消息总长度，占用2字节
		byteBuf.writeShort(prefix + dataLength);
		// 协议号，占用2字节
		byteBuf.writeShort(msg.getCmdCode());
		//玩家id,占用4字节
		byteBuf.writeInt(msg.getUserId());
		//8个字节的时间戳
		byteBuf.writeLong(System.currentTimeMillis());
		// 写入消息长度 占用4字节
		byteBuf.writeInt(dataLength);
		if (dataLength > 0) {
			// 写入消息体，长度为dataLength
			byteBuf.writeBytes(msg.getData());
		}
	}

}
