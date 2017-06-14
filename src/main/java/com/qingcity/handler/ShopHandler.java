package com.qingcity.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.qingcity.constants.CmdConstant;
import com.qingcity.domain.GameResponse;
import com.qingcity.entity.MsgEntity;

@Controller
public class ShopHandler extends HandlerMsg implements CmdHandler {

	private static final Logger logger = LoggerFactory.getLogger(ShopHandler.class);

	@Override
	public void handleMsg(MsgEntity msgEntity, GameResponse response) throws Exception {
		if (isErrorMsg(msgEntity, response)) {
			logger.error("实际消息和原消息不符!长度不一致。");
			return;
		}
		switch (msgEntity.getCmdCode()) {// 根据命令码对应找到对应处理方法
		case CmdConstant.C2S_SHOP_BUY:
		    handleBuy(msgEntity, response);
			break;
		default:
			break;
		}
	}
	
	public void handleBuy(MsgEntity msgEntity, GameResponse response){
		
		logger.info("==============>: 检查货币是否充足后购买");
		logger.info("==============>: 购买商品");
		
		
		
//		S2C_Shop.Builder s2c_shop = S2C_Shop.newBuilder();
//		try {
//			C2S_Shop c2s_shop = C2S_Shop.parseFrom(msgEntity.getData());
//			int type = c2s_shop.getType();
//			int itemId = c2s_shop.getId();
//			System.out.println("购买商品");
//			String gain = shopService.buy(msgEntity.getUserId(), type, itemId);
//			System.out.println("购买的商品"+gain);
//			if(StringUtil.isEmpty(gain)||gain.equals("limit")){
//				s2c_shop.setResult(false);
//				System.out.println("已买过的限购商品，无法再次购买");
//				handlerResMsg(s2c_shop.build(), CmdConstant.S2C_LIMIT_GIFT_CAN_BUY_ONE_TIME, msgEntity.getUserId(), response);
//				return;
//			}else{
//				s2c_shop.setResult(true);
//				response.setGain(gain);
//			}
//			s2c_shop.setId(itemId);
//			s2c_shop.setType(type);
//			handlerResMsg(s2c_shop.build(), CmdConstant.S2C_SHOP_BUY, msgEntity.getUserId(), response);
//		} catch (InvalidProtocolBufferException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
