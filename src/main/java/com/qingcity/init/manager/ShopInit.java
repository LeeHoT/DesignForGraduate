package com.qingcity.init.manager;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.reflect.TypeToken;
import com.qingcity.init.entity.ShopGiftPo;
import com.qingcity.init.entity.ShopGroceryPo;
import com.qingcity.init.entity.ShopRechargePo;
import com.qingcity.init.entity.ShopType;
import com.qingcity.util.FileUtil;
import com.qingcity.util.GsonUtil;

public class ShopInit {

	private static Logger logger = LoggerFactory.getLogger(ShopInit.class);

	private List<ShopGiftPo> giftList = new ArrayList<ShopGiftPo>();
	private List<ShopRechargePo> rechargeList = new ArrayList<ShopRechargePo>();
	private List<ShopGroceryPo> groceryList = new ArrayList<ShopGroceryPo>();

	private static ShopInit instance = null;

	public static ShopInit getInstance() {
		if (instance == null) {
			instance = new ShopInit();
		}
		return instance;
	}

	public ShopInit() {
		instance = this;
	}

	public void init() {
		for (ShopType type : ShopType.values()) {
			if(type.getPo().equals("null")){
				continue;
			}
			try {
				String u = getInstance().getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
				String filename = new File(u).getParent() + "/conf/config/properties/json/" + type.getPo() + ".json";
				String json = FileUtil.ReadFile(filename);
				if (type.getValue() == ShopType.GIFT.getValue()) {
					Type t = new TypeToken<List<ShopGiftPo>>() {
					}.getType();
					giftList = GsonUtil.getInstance().fromJson(json, t);
				} else if (type.getValue() == ShopType.GROCERY.getValue()) {
					Type t = new TypeToken<List<ShopGroceryPo>>() {
					}.getType();
					groceryList = GsonUtil.getInstance().fromJson(json, t);
				} else if (type.getValue() == ShopType.RECHARGE.getValue()) {
					Type t = new TypeToken<List<ShopRechargePo>>() {
					}.getType();
					rechargeList = GsonUtil.getInstance().fromJson(json, t);
				} else {
					logger.error("未知的商店类型[{}]", type);
				}
				logger.info("==============>: "+type.getPo() + ".json文件加载成功");
			} catch (Exception e) {
				logger.error("=============>: "+type.getPo() + ".jsonjson加载失败");
			}
		}
	}

	public List<ShopGiftPo> getGiftList() {
		return giftList;
	}

	public void setGiftList(List<ShopGiftPo> giftList) {
		this.giftList = giftList;
	}

	public List<ShopRechargePo> getRechargeList() {
		return rechargeList;
	}

	public void setRechargeList(List<ShopRechargePo> rechargeList) {
		this.rechargeList = rechargeList;
	}

	public List<ShopGroceryPo> getGroceryList() {
		return groceryList;
	}

	public void setGroceryList(List<ShopGroceryPo> groceryList) {
		this.groceryList = groceryList;
	}
	
}
