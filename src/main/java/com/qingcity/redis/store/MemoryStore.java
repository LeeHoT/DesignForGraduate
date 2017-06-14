package com.qingcity.redis.store;

import io.netty.channel.ChannelHandlerContext;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author 定义RedisStore
 * @Date 2017年4月14日 下午1:40:16
 * @Description 
 */
public class MemoryStore implements Store {

	private static final ConcurrentHashMap<String, ChannelHandlerContext> clients = new ConcurrentHashMap<String, ChannelHandlerContext>();

	@Override
	public ChannelHandlerContext get(String key) {
        if (this.checkExist(key)) {
            return clients.get(key);
        }

		return null;
	}

	@Override
	public void remove(String key) {
        clients.remove(key);
	}

	@Override
	public void add(String key, ChannelHandlerContext client) {
		if (key == null || client == null) {
            return;
        }

        clients.put(key, client);
	}

	@Override
	public Collection<ChannelHandlerContext> getClients() {
        return clients.values();
	}

	@Override
	public boolean checkExist(String key) {
        return clients.containsKey(key);
	}

}