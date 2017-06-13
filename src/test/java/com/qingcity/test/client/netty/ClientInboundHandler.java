package com.qingcity.test.client.netty;

import com.google.protobuf.InvalidProtocolBufferException;
import com.qingcity.constants.ChatConstant;
import com.qingcity.constants.CmdConstant;
import com.qingcity.entity.MsgEntity;
import com.qingcity.proto.ChatProto.S2C_ChatMessage;
import com.qingcity.proto.PlayerInfo.S2C_GetPlayerInfo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 
 * @author Administrator
 * @Date 2017年3月25日 下午5:11:18
 * @Description TODO
 */
public class ClientInboundHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		MsgEntity message = (MsgEntity) msg;
		System.out.println("收到服务器的消息");
		try {
			System.out.println(message.getCmdCode());
			if(message.getCmdCode()==CmdConstant.S2C_USER_GET_BASE_INFO){
				S2C_GetPlayerInfo play = S2C_GetPlayerInfo.parseFrom(message.getData());
				System.out.println("钻石"+play.getPlayer().getWealth().getDiamond());
				System.out.println("金币"+play.getPlayer().getWealth().getGold());
			}
			if (message.getCmdCode() == CmdConstant.REP_PING) {
				System.out.println("客户端收到服务器PING回应消息");
				System.out.println(msg);
			} else if (message.getCmdCode() == ChatConstant.SOCIETY_MSG
					|| message.getCmdCode() == ChatConstant.WORLD_MSG) {
				S2C_ChatMessage s2c = S2C_ChatMessage.parseFrom(message.getData());
				System.out.println("chat content is "+s2c.getContent());
			}else if(message.getCmdCode()==CmdConstant.S2C_FRIEND_LIST){
				System.out.println("shoudao l a.");
			}
			if(message.getCmdCode()==CmdConstant.S2C_USER_GET_BASE_INFO){
				S2C_GetPlayerInfo info = S2C_GetPlayerInfo.parseFrom(message.getData());
				System.out.println("玩家id"+message.getUserId());
				System.out.println("玩家昵称"+info.getPlayer().getNickname());
				System.out.println("玩家等级"+info.getPlayer().getLevel());
				System.out.println("玩家钻石"+info.getPlayer().getWealth().getDiamond());
			}
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// if (msg instanceof HttpContent) {
		// HttpContent content = (HttpContent) msg;
		// ByteBuf buf = content.content();
		// System.out.println(buf.toString(io.netty.util.CharsetUtil.UTF_8));
		// buf.release();
		// }
		// if (msg instanceof ByteBuf) {
		// ByteBuf messageData = (ByteBuf) msg;
		// int commandId = messageData.readInt();
		// int length = messageData.readInt();
		// byte[] c = new byte[length];
		// messageData.readBytes(c);
		// System.out.println("commandId:"+commandId+"\tmessage:"+new
		// String(c));
		// }
	}

	/**
	 * 回应客户端心跳检测
	 * 
	 * @param ctx
	private void repPing(ChannelHandlerContext ctx) {
		KeepAliveMsg.Builder keepAlive = KeepAliveMsg.newBuilder();
		MsgEntity msg = new MsgEntity();
		byte[] pingB = keepAlive.build().toByteArray();
		msg.setCmdCode(CmdConstant.REP_PING);
		msg.setData(pingB);
		msg.setMsgLength(pingB.length);
		ctx.writeAndFlush(msg);
	}
	 */
}
