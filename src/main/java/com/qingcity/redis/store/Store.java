package com.qingcity.redis.store;

import io.netty.channel.ChannelHandlerContext;

import java.util.Collection;

/**
 * 
 * @author leehotin
 * @Date 2017年4月2日 下午4:17:48
 * @Description 
 */
public interface Store {

	void remove(String sessionId);

	void add(String sessionId, ChannelHandlerContext ctx);

	Collection<ChannelHandlerContext> getClients();

    ChannelHandlerContext get(String sessionId);

	boolean checkExist(String sessionId);
}
