package com.qingcity.init.manager;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.reflect.TypeToken;
import com.qingcity.init.entity.PlayerUpgradePo;
import com.qingcity.util.FileUtil;
import com.qingcity.util.GsonUtil;

public class PlayerUpgradeInit {
	
	private static Logger logger = LoggerFactory.getLogger(PlayerUpgradeInit.class);


	private Map<Integer, PlayerUpgradePo> upgradeMap = new HashMap<Integer, PlayerUpgradePo>();
	private List<PlayerUpgradePo> playerUpgradePoList = new ArrayList<>();
	
	private static PlayerUpgradeInit instance = null;

	public static PlayerUpgradeInit getInstance() {
		if (instance == null) {
			instance = new PlayerUpgradeInit();
		}
		return instance;
	}

	public PlayerUpgradeInit() {
		instance = this;
	}

	public void init() {
			try {
				String u = getInstance().getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
				String filename = new File(u).getParent() + "/conf/config/properties/json/PlayerUpgrade.json";
				String json = FileUtil.ReadFile(filename);
				Type t = new TypeToken<List<PlayerUpgradePo>>() {}.getType();
				playerUpgradePoList = GsonUtil.getInstance().fromJson(json, t);
				for(int i = 0;i<playerUpgradePoList.size();i++){
					upgradeMap.put(i, playerUpgradePoList.get(i));
				}
				logger.info("PlayerUpgrade.json文件加载成功");
			} catch (Exception e) {
				logger.error("PlayerUpgrade.jsonjson加载失败");
				e.printStackTrace();
			}
	}

	public Map<Integer, PlayerUpgradePo> getUpgradeMap() {
		return upgradeMap;
	}

	public void setUpgradeMap(Map<Integer, PlayerUpgradePo> upgradeMap) {
		this.upgradeMap = upgradeMap;
	}
	
	public PlayerUpgradePo getPlayerUpgradePo(int level){
		if(!upgradeMap.containsKey(level)){
			return null;
		}
		return upgradeMap.get(level);
	}
	
	/**
	 * 获取体力上限
	 * @param level
	 * @return
	 */
	public int getPowerLimit(int level){
		//TODO 此处有个常量表记录会更好
		int power = 5;
		for(PlayerUpgradePo po: playerUpgradePoList){
			if(po.getLevel()<=level){
				power = power +po.getIncrPower();
			}
		}
		return power;
		
	}
}
