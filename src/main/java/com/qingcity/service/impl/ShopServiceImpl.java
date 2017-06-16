package com.qingcity.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingcity.base.util.TimeUtil;
import com.qingcity.entity.PlayerEntity;
import com.qingcity.redis.RedisManager;
import com.qingcity.sd.entity.DyeColorType;
import com.qingcity.sd.entity.ItemType;
import com.qingcity.sd.entity.ShopGiftPo;
import com.qingcity.sd.entity.ShopGroceryPo;
import com.qingcity.sd.entity.ShopRechargePo;
import com.qingcity.sd.entity.ShopType;
import com.qingcity.sd.manager.ShopInit;
import com.qingcity.service.PlayerService;
import com.qingcity.service.ShopService;

@Service
public class ShopServiceImpl implements ShopService {

	private static Logger logger = LoggerFactory.getLogger(ShopServiceImpl.class);

	@Autowired
	private PlayerService playerService;
	@Override
	public String buy(int userId, int type, int pid) {
		ShopType st = ShopType.parse(type);
		String result = null;
		if (st == ShopType.GROCERY) {
			// 购买杂货铺商品
			System.out.println("购买杂货铺");
			result = buyGrocery(userId, pid);
		}
		if (st == ShopType.GIFT) {
			// 购买礼包
			logger.info("购买礼包");
			result = buyGift(userId, pid);
		}
		if (st == ShopType.RECHARGE) {
			// 充值
			System.out.println("充值");
			result = recharge(userId, pid);
		}
		return result;
	}
	
	
	private void saveLimit(int save,int userId,String type){
		if(save>0){
			System.out.println("购买了限购商品");
		}
	}
	

	public String buyGift(int userId, int pid) {
		String detail = null;
		// 检查商品是否存在
		ShopGiftPo giftPo = null;
		List<ShopGiftPo> gift = ShopInit.getInstance().getGiftList();
		for (ShopGiftPo po : gift) {
			if (po.getID() == pid) {
				giftPo = po;
				break;
			}
		}
		if (giftPo == null) {
			logger.error("商品不存在");
			return detail;
		}
		PlayerEntity player = new PlayerEntity();
		// 检查钱够不够
		if (giftPo.getMoney() == ItemType.DIAMOND.getValue()) {
			// 用钻石购买
			player = playerService.selectByUserId(userId);
			if (player.getDiamond() >= giftPo.getPrize()) {
				// 钻石够 ,扣除钻石
				logger.info("扣除钻石");
				player.setDiamond(player.getDiamond() - giftPo.getPrize());
				// 更新礼品数量
				detail = giftPo.getDetail();
				gainProduct(player, detail);
				System.out.println("商品"+pid+"限购类型"+giftPo.getLimit());
				saveLimit(giftPo.getLimit(), userId, ShopType.GIFT.getValue()+"#"+pid);
			}
		} else if (giftPo.getMoney() == ItemType.GOLD.getValue()) {
			// 目前可能没这个需求
			player = playerService.selectByUserId(userId);
		} else {
			// RMB 目前可能没这个需求
			logger.error("数据有误，不应该需要人民币");
		}
		return detail;
		
	}

	public String buyGrocery(int userId, int pid) {
		// 检查商品是否存在
		ShopGroceryPo groceryPo = null;
		List<ShopGroceryPo> grocery = ShopInit.getInstance().getGroceryList();
		String detail = "";
		for (ShopGroceryPo po : grocery) {
			if (po.getID() == pid) {
				groceryPo = po;
			}
		}
		if (groceryPo == null) {
			logger.error("商品不存在");
			return detail;
		}
		PlayerEntity player = new PlayerEntity();
		// 检查钱够不够
		if (groceryPo.getMoney() == ItemType.DIAMOND.getValue()) {
			// 用钻石购买
			player = playerService.selectByUserId(userId);
			if (player.getDiamond() >= groceryPo.getPrize()) {
				// 钻石够 ,扣除钻石
				player.setDiamond(player.getDiamond() - groceryPo.getPrize());
				// 更新礼品数量
				detail = groceryPo.getDetail();
				gainProduct(player, detail);
			}
		} else if (groceryPo.getMoney() == ItemType.GOLD.getValue()) {
			// 目前可能没这个需求
			player = playerService.selectByUserId(userId);
		} else {
			// RMB 目前可能没这个需求
			logger.error("数据有误，不应该需要人民币");
		}
		return detail;
	}

	public String recharge(int userId, int pid) {
		String detail = null;
		ShopRechargePo rechargePo = null;
		List<ShopRechargePo> gift = ShopInit.getInstance().getRechargeList();
		for (ShopRechargePo po : gift) {
			if (po.getId() == pid) {
				rechargePo = po;
			}
		}
		if (rechargePo == null) {
			logger.error("商品不存在");
			return detail;
		}
		PlayerEntity player = new PlayerEntity();
		player.setUserId(userId);
		// TODO 跳到三方支付，并生成订单，此处假设三方支付成功
		detail="1,"+rechargePo.getAmount();
		
		gainProduct(player, detail);
		sendToRedis(userId, rechargePo.toString());
		
		return detail;
	}

	@Override
	public String gainProduct(PlayerEntity player, String detail) {
		if (detail == null) {
			// 什么都没有
			return null;
		}
		detail = getDetail(detail);

		String ss[] = detail.split(";");
		Map<Integer, Integer> temp = new HashMap<>();
		for (String sub : ss) {
			String s[] = sub.split(",");
			int itemId = Integer.valueOf(s[0]);
			int number = Integer.valueOf(s[1]);
			if (itemId == ItemType.GOLD.getValue()) {
				// 金币
				player.setGold(player.getGold() + number);
			} else if (itemId == ItemType.DIAMOND.getValue()) {
				// 钻石
				player.setDiamond(player.getDiamond() + number);
			} else if (itemId == ItemType.POWER.getValue()) {
				// 体力
				player.setPower(player.getPower() + number);
			} else if(itemId ==ItemType.EXP.getValue()){
				playerService.calculateExp(player, number);
			}else {
				
			}
		}
		for (Map.Entry<Integer, Integer> entry : temp.entrySet()) {
			// 背包里没有
			System.out.println("添加进背包" + entry.getKey());
		}
		// 保存奖励信息
		playerService.updateByUserIdSelective(player);
		return detail;
	}

	public String getDetail(String detail) {
		if (detail == null) {
			// 什么都没有
			return null;
		}
		StringBuilder sb = new StringBuilder();
		String ss[] = detail.split(";");
		for (String sub : ss) {
			String s[] = sub.split(",");
			int itemId = Integer.valueOf(s[0]);
			int number = Integer.valueOf(s[1]);
			// 只对颜色进行处理
			if (itemId == ItemType.DYE.getValue()) {
				// 是颜色
				List<Integer> colors = new ArrayList<>();
				Map<Integer, Integer> temp = new HashMap<>();
				// TODO 同一种item必须在一个子串里。。
				// 根据数量取随机列表
				colors = DyeColorType.getRandom(number);
				for (Integer color : colors) {
					System.out.println("买了一个颜色" + color);
					if (temp.containsKey(color)) {
						// 已经加过这个颜色
						System.out.println("已经加过颜色" + color);
						temp.put(color, temp.get(color) + 1);
					} else {
						temp.put(color, 1);
					}
				}
				// 转字符串
				for (Map.Entry<Integer, Integer> entry : temp.entrySet()) {
					sb.append(entry.getKey()).append(",").append(entry.getValue()).append(";");
				}
				// 去掉最后一个逗号
				if (sb.length() > 0) {
					sb.deleteCharAt(sb.length() - 1);
				}
				String str = sb.toString();
				detail = detail.replace(itemId + "," + number, str);
			}
		}
		return detail;
	}

	public void sendToRedis(int userId, String detail) {
		RedisManager.add("buy_" + userId + "_" + TimeUtil.getCurrentTimestamp(), detail);
	}
}
