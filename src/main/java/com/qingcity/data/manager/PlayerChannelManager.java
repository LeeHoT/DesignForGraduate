package com.qingcity.data.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qingcity.constants.CmdConstant;
import com.qingcity.entity.MsgEntity;
import com.qingcity.proto.PlayerInfo.S2C_Result;

import io.netty.channel.Channel;

/**
 * @author leehotin
 * @Date 2017年2月5日 上午10:33:50
 * @Description 角色通到管理器
 */
public class PlayerChannelManager {

	private static final Logger logger = LoggerFactory.getLogger(PlayerChannelManager.class);

	private static PlayerChannelManager instance = new PlayerChannelManager();

	public static PlayerChannelManager getInstance() {
		return instance;
	}

	/**
	 * Map<userId,channel>
	 */
	private static Map<Integer, Channel> channelMap = new ConcurrentHashMap<Integer, Channel>();

	public Map<Integer, Channel> getChannel() {
		return channelMap;
	}

	/**
	 * 添加玩家Channel
	 * 
	 * @param userId
	 *            玩家id
	 * @param channel
	 *            玩家Channel
	 */
	public synchronized void add(int userId, Channel channel) {
		logger.info("Add channel:[{}] into channelMap,玩家[{}]已登录", userId, userId);
		if(channelMap.containsKey(userId)){
			//玩家已登录，踢掉之前的玩家并通知
			S2C_Result.Builder s2c_result = S2C_Result.newBuilder();
			MsgEntity msg = new MsgEntity();
			msg.setCmdCode(CmdConstant.S2C_USER_LOGIN_ON_OTHER_PLACE);
			msg.setData(s2c_result.build().toByteArray());
			msg.setUserId(userId);
			channelMap.get(userId).writeAndFlush(msg);
			channelMap.get(userId).close();
		}
		channelMap.put(userId, channel);
	}

	/**
	 * 获取玩家Channel
	 * 
	 * @param userId
	 *            玩家id
	 * @return 当前客户端Channel
	 */
	public Channel getChannel(int userId) {
		return channelMap.get(userId);
	}

	/**
	 * 根据channel值获取玩家Id
	 * 
	 * @param channel
	 * @return channel 对应的玩家Id
	 */
	public Integer getIdByChannel(Channel channel) {
		int userId = 0;
		for (Map.Entry<Integer, Channel> pChannel : channelMap.entrySet()) {
			if (pChannel.getValue().equals(channel)) {
				userId = pChannel.getKey();
			}
		}
		return userId;
	}

	/**
	 * 移除玩家
	 * 
	 * @param Channel
	 */
	@SuppressWarnings("rawtypes")
	public void removeChannel(Channel channel) {
		for (Map.Entry entry : channelMap.entrySet()) {
			if (entry.getValue() == channel) {
				logger.warn("Remove channel:[{}] from channelMap!,玩家[{}]掉线", channel, entry.getKey());
				channelMap.remove(entry.getKey());
			}
			logger.debug("当前共有[{}]个玩家在线", channelMap.size());
		}
	}
}
