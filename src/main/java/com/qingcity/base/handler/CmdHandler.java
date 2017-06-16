package com.qingcity.base.handler;

import com.qingcity.base.domain.GameResponse;
import com.qingcity.entity.MsgEntity;

/**
 * 
 * @author leehotin
 * @Date 2017年4月14日 下午1:57:24
 * @Description 处理消息的Controller,根据gameHandlerMap抉择处理消息的类型。
 */
public interface CmdHandler {
	public void handleMsg(MsgEntity msgEntity, GameResponse response) throws Exception;
}
