package com.qingcity.netty;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qingcity.base.task.CheckChannelStatusTask;
import com.qingcity.chat.domain.ChatMessageReq;
import com.qingcity.base.constants.ChatConstant;
import com.qingcity.base.constants.CmdConstant;
import com.qingcity.data.manager.PlayerChannelManager;
import com.qingcity.base.dispatcher.ChatMessageDispatcher;
import com.qingcity.base.dispatcher.HandlerDispatcher;
import com.qingcity.base.domain.ERequestType;
import com.qingcity.base.domain.GameRequest;
import com.qingcity.entity.MsgEntity;
import com.qingcity.proto.PlayerInfo.S2C_Result;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerAdapter extends SimpleChannelInboundHandler<MsgEntity> {

	private static final Logger logger = LoggerFactory.getLogger(ServerAdapter.class);

	private ChatMessageDispatcher chatMessageDispatcher;
	private HandlerDispatcher handlerDispatcher;
	
	private Map<Channel, Long> timeRecord = new HashMap<Channel, Long>();
	
	public void setHandlerDispatcher(HandlerDispatcher handlerDispatcher) {
		this.handlerDispatcher = handlerDispatcher;
	}

	public void setChatMessageDispatcher(ChatMessageDispatcher chatMessageDispatcher) {
		this.chatMessageDispatcher = chatMessageDispatcher;
	}

	public ServerAdapter(HandlerDispatcher handlerDispatcher, ChatMessageDispatcher chatMessageDispatcher) {
		this.handlerDispatcher = handlerDispatcher;
		this.chatMessageDispatcher = chatMessageDispatcher;
	}

	public ServerAdapter() {
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("==============>: [Client OnLine]:" + ctx.channel().remoteAddress() + "上线");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.info("==============>: [Client OutLine]:" + ctx.channel().remoteAddress() + "掉线并将其移出服务器");
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MsgEntity msg) throws Exception {
		if (msg == null) {
			// 无消息内容
			return;
		}
		if(msg.getCmdCode() != CmdConstant.C2S_USER_LOGIN && msg.getCmdCode() != CmdConstant.C2S_USER_REGISTER){
			//不是登陆和注册就需要去查询是否已经登陆，没有登陆就直接返回
			if(!PlayerChannelManager.getInstance().getChannel().containsKey(msg.getUserId())){
				//玩家没有登陆
				S2C_Result.Builder result  = S2C_Result.newBuilder();
				result.setResult(false);
				byte[] b = result.build().toByteArray();
				MsgEntity message = new MsgEntity();
				message.setCmdCode(CmdConstant.S2C_USER_NOT_LOGIN);
				message.setData(b);
				ctx.writeAndFlush(message);
				return;
			}
		}
		if (ChatConstant.WORLD_MSG == msg.getCmdCode()) {
			sendMessage(ctx, msg);
		}else{
			socketRequest(ctx, msg);
			
		}
	}

	public void sendMessage(ChannelHandlerContext ctx, MsgEntity msg) {
		this.chatMessageDispatcher.addMessage(new ChatMessageReq(ctx, msg));
	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("=============>: [Client]: " + ctx.channel().remoteAddress() + "在" + new Date() + "发生连接异常");
		CheckChannelStatusTask ccst = new CheckChannelStatusTask();
		ccst.kickOff(ctx.channel());
		logger.error(cause.getMessage());
		ctx.close();
	}

	private void socketRequest(ChannelHandlerContext ctx, MsgEntity msg) throws Exception {
		this.handlerDispatcher.addMessage(new GameRequest(ctx, ERequestType.SOCKET, msg));
	}

	public Map<Channel, Long> getTimeRecord() {
		return timeRecord;
	}

	public void setTimeRecord(Map<Channel, Long> timeRecord) {
		this.timeRecord = timeRecord;
	}
}
