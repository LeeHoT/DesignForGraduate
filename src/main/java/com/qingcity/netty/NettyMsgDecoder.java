package com.qingcity.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qingcity.entity.MsgEntity;

/**
 * 定长解码器 消息格式 
 * +--------+-----+--------+---------+---------+----------------+      +------+--------+---------+---------+----------------+ 
 * | Length1| CMD | USERID |TIMESTAMP| Length2 | Actual Content |----->|  CMD | USERID |TIMESTAMP| Length2 | Actual Content |
 * |  2字节       | 2字节 |  4字节       |   8字节       |   4字节       | "HELLO, WORLD" |      |  2字节 |  2字节       |   8字节       |   4字节       | "HELLO, WORLD" |
 * +--------+-----+--------+---------+---------+----------------+      +------+--------+---------+---------+----------------+
 */
public class NettyMsgDecoder extends LengthFieldBasedFrameDecoder {
	
	private static Logger logger = LoggerFactory.getLogger(NettyMsgDecoder.class);
	private Map<Integer, Long> times = new HashMap<Integer, Long>();

	/**
	 * @param byteOrder
	 * @param maxFrameLength
	 *            字节最大长度,大于此长度则抛出异常
	 * @param lengthFieldOffset
	 *            开始计算长度位置,这里使用0代表放置到最开始
	 * @param lengthFieldLength
	 *            描述长度所用字节数 encode时使用的是short型
	 * @param lengthAdjustment
	 *            长度补偿,这里由于命令码使用2个字节.需要将原来长度计算加2
	 * @param initialBytesToStrip
	 *            开始计算长度需要跳过的字节数
	 * @param failFast
	 */
	public NettyMsgDecoder(ByteOrder byteOrder, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
			int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
		super(byteOrder, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip,
				failFast);
	}

	public NettyMsgDecoder() {
		this(ByteOrder.BIG_ENDIAN, Integer.MAX_VALUE, 0, 2, 0, 2, false);
	}

	/**
	 * 根据构造方法自动处理粘包,拆包.然后调用此decode
	 */
	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
		if(byteBuf.readableBytes() == 0){
			return null;
		}
		ByteBuf frame = (ByteBuf) super.decode(ctx, byteBuf);
		if (frame == null) {
			logger.error("=============>: The message from Client is error!Please Check and send again");
			return null;
		}
		short cmd = frame.readShort();// 先读取2个字节长度命令码
		int userId = frame.readInt();//读取四字节的userId
		long time = frame.readLong();//读取8字节时间戳
		int msgLen = frame.readInt();// 再读取4个字节长度消息长
		
		if(times.containsKey(userId)){
			if(times.get(userId) == time){
				logger.info("==============>: 玩家[{}]的请求重复，直接忽略！",userId);
				return null;
			}
		}
		times.put(userId, time);
		
		logger.info("==============>: 协议号:[{}]",cmd);
		logger.info("==============>: 消息长度:[{}]",msgLen);
		//System.out.println("消息类型: " + protoType);
		logger.info("==============>: 消息体的实际接收长度: [{}]",frame.readableBytes());
		byte[] data = new byte[frame.readableBytes()];// 其它数据为实际数据
		frame.readBytes(data);
		if (msgLen != data.length) {
			logger.info("==============>: 消息实际长度和原始长度不一致。。请重新发送");
			return null;
		}
		MsgEntity msgVO = new MsgEntity();
		msgVO.setUserId(userId);
		msgVO.setMsgLength(msgLen);
		msgVO.setCmdCode(cmd);
		msgVO.setData(data);
		return msgVO;
	}

	public Map<Integer, Long> getTimes() {
		return times;
	}

	public void setTimes(Map<Integer, Long> times) {
		this.times = times;
	}

}
