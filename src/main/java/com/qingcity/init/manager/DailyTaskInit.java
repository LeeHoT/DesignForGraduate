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
import com.qingcity.init.entity.DailyTaskPo;
import com.qingcity.util.FileUtil;
import com.qingcity.util.GsonUtil;

public class DailyTaskInit {

	private static Logger logger = LoggerFactory.getLogger(DailyTaskInit.class);

	private Map<Integer, String> map = new HashMap<Integer, String>();
	private List<DailyTaskPo> dailyTaskPoList = new ArrayList<>();

	private static DailyTaskInit instance = null;

	public static DailyTaskInit getInstance() {
		if (instance == null) {
			instance = new DailyTaskInit();
		}
		return instance;
	}

	public DailyTaskInit() {
		instance = this;
	}

	public void init() {
		try {
			String u = getInstance().getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			String filename = new File(u).getParent() + "/conf/config/properties/json/DailyTask.json";
			String json = FileUtil.ReadFile(filename);
			Type t = new TypeToken<List<DailyTaskPo>>() {
			}.getType();
			dailyTaskPoList = GsonUtil.getInstance().fromJson(json, t);
			for (int i = 0; i < dailyTaskPoList.size(); i++) {
				map.put(dailyTaskPoList.get(i).getID(), dailyTaskPoList.get(i).getDetail());
			}
			logger.info("==============>: DailyTask.json文件加载成功");
		} catch (Exception e) {
			logger.error("=============>: DailyTask.jsonjson加载失败");
		}
	}

	public String getReward(int id) {
		if (map.containsKey(id)) {
			return map.get(id);
		}
		return null;
	}

	public Map<Integer, String> getMap() {
		return map;
	}

	public void setMap(Map<Integer, String> map) {
		this.map = map;
	}

	public List<DailyTaskPo> getDailyTaskPoList() {
		return dailyTaskPoList;
	}

	public void setDailyTaskPoList(List<DailyTaskPo> dailyTaskPoList) {
		this.dailyTaskPoList = dailyTaskPoList;
	}

}
