package com.qingcity.task;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qingcity.data.manager.PlayerChannelManager;
import com.qingcity.data.manager.PlayerManager;
import com.qingcity.data.manager.SocietyGroup;
import com.qingcity.data.manager.WorldGroup;

import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;

public class CheckChannelStatusTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(CheckChannelStatusTask.class);
	public static final Long CLIENT_OUTLINE_TIME = 600000L;// 5分钟
	public static final Long RUNNING = 10000L;//10秒轮询一次。。

	@SuppressWarnings("rawtypes")
	@Override
	public void run() {
		/**
		 * 轮询lastPingTime,时间超过一定值之后则视为掉线。直接踢掉。
		 */
		logger.debug("运没运行检测程序啊");
		for (Map.Entry entry : PlayerManager.getInstance().lastPingTime.entrySet()) {
			long time = (long) entry.getValue();
			if (System.currentTimeMillis() - time >= CLIENT_OUTLINE_TIME) {
				logger.warn("channel:[{}] had outline,it's time to kick off", entry.getKey());
				kickOff(entry.getKey());
			}

		}
	}
	
	public void kickOff(Object key){
		// 将玩家移出ping装置
		PlayerManager.getInstance().lastPingTime.remove(key);
		// 将玩家移出channel列表
		PlayerChannelManager.getInstance().removeChannel((SocketChannel) key);
		// 将玩家移出世界频道
		WorldGroup.getInstance().removeFromGroup((Channel) key);
		// 将玩家移出公会频道
		SocietyGroup.getInstance().removeFromGroup((Channel)key);
	}
}
