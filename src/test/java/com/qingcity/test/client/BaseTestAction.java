package com.qingcity.test.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.qingcity.constants.CmdConstant;
import com.qingcity.constants.PortConstant;
import com.qingcity.entity.MsgEntity;
import com.qingcity.netty.NettyMsgDecoder;
import com.qingcity.netty.NettyMsgEncoder;
import com.qingcity.test.client.netty.ClientInboundHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class BaseTestAction {

	public static List<Channel> channelList = new ArrayList<Channel>();
	public static List<Channel> chatChannelList = new ArrayList<Channel>();

	private static final int READ_IDEL_TIME_OUT = 120; // 读超时
	private static final int WRITE_IDEL_TIME_OUT = 15;// 写超时
	private static final int ALL_IDEL_TIME_OUT = 60; // 所有超时

	private static EventLoopGroup workerGroup = new NioEventLoopGroup();

	private static final int CLIENT = 1;
	private static Bootstrap b;

	// 定义客户端没有收到服务端的pong消息的最大次数
	//private static final int MAX_UN_REC_PONG_TIMES = 3;

	// 多长时间未请求后，发送心跳
	//private static final int WRITE_WAIT_SECONDS = 5;

	// 隔N秒后重连
	//private static final int RE_CONN_WAIT_SECONDS = 5;

	// 客户端连续N次没有收到服务端的pong消息 计数器
	//private static int unRecPongTimes = 0;
	// 是否停止
	//private static boolean isStop = false;
	public static void connectServer(int clientNum) {
		b = new Bootstrap();
		b.group(workerGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<Channel>() {
			@Override
			protected void initChannel(Channel ch) throws Exception {
				ch.pipeline().addLast("frameDecoder", new NettyMsgDecoder());
				ch.pipeline().addLast("frameEncoder", new NettyMsgEncoder());
				ch.pipeline().addLast("heartbeat", new IdleStateHandler(READ_IDEL_TIME_OUT, WRITE_IDEL_TIME_OUT,
						ALL_IDEL_TIME_OUT, TimeUnit.SECONDS));
				ch.pipeline().addLast("handler", new ClientInboundHandler());
			}
		});

		if(clientNum ==0){
			clientNum = CLIENT;
		}
		for (int i = 0; i < clientNum; i++) {
			//ChannelFuture future = b.connect("118.89.233.59", PortConstant.GAME_SERVER);
			ChannelFuture future = b.connect("127.0.0.1", PortConstant.GAME_SERVER);
			channelList.add(future.channel());
		}
	}

}
