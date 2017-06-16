package com.qingcity.server.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.qingcity.base.util.ExceptionUtils;
import com.qingcity.base.dispatcher.ChatMessageDispatcher;
import com.qingcity.base.dispatcher.HandlerDispatcher;
import com.qingcity.base.domain.ERequestType;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 
 * @author leehotin
 * @Date 2017年2月1日 上午11:10:02
 * @Description 初始化服务器配置
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
	private static final Logger logger = LoggerFactory.getLogger(ServerInitializer.class);
	
	@Autowired
	private HandlerDispatcher handlerDispatcher;
	@Autowired
	private ChatMessageDispatcher chatMessageDispatcher;
	// 消息请求类型
	private String requestType = ERequestType.SOCKET.getValue();

	public void init() {
		logger.info("==============>: init the HandlerDispatcher");
		new Thread(this.handlerDispatcher).start();
		new Thread(this.chatMessageDispatcher).start();
	}

	/**
	 * 初始化channel
	 */
	public void initChannel(SocketChannel ch) {
		try {
			if (ERequestType.SOCKET.getValue().equals(this.requestType.trim().toLowerCase())) {
				logger.info("==============>: init the channel of type : " + this.requestType.trim().toLowerCase());
				ch.pipeline().addLast("protobufDecoder", new NettyMsgDecoder());
				ch.pipeline().addLast("protobufEncoder", new NettyMsgEncoder());
			} else if (ERequestType.HTTP.getValue().equals(this.requestType.trim().toLowerCase())) {
				ch.pipeline().addLast("codec-http", new HttpServerCodec());
				ch.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
			} else {
				logger.error("==============>: cannot inti the channel ,the type : " + this.requestType.trim()
						+ "cannot be find,please check the request type whether it is " + ERequestType.SOCKET.getValue()
						+ " !");
				return;
			}
			ch.pipeline().addLast("handler", new ServerAdapter(this.handlerDispatcher, this.chatMessageDispatcher));
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

	}

	public void setHandlerDispatcher(HandlerDispatcher handlerDispatcher) {
		this.handlerDispatcher = handlerDispatcher;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public ChatMessageDispatcher getChatMessageDispatcher() {
		return chatMessageDispatcher;
	}

	public void setChatMessageDispatcher(ChatMessageDispatcher chatMessageDispatcher) {
		this.chatMessageDispatcher = chatMessageDispatcher;
	}

	public String getRequestType() {
		return this.requestType;
	}
}