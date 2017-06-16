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
import com.qingcity.base.util.FileUtil;
import com.qingcity.base.util.GsonUtil;
import com.qingcity.init.entity.MainTaskPo;

public class MainTaskInit {

	private static Logger logger = LoggerFactory.getLogger(MainTaskInit.class);


	private Map<Integer, MainTaskPo> map = new HashMap<Integer, MainTaskPo>();
	private List<MainTaskPo> mainTaskPoList = new ArrayList<>();
	
	private static MainTaskInit instance = null;

	public static MainTaskInit getInstance() {
		if (instance == null) {
			instance = new MainTaskInit();
		}
		return instance;
	}

	public MainTaskInit() {
		instance = this;
	}

	public void init() {
			try {
				String u = getInstance().getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
				String filename = new File(u).getParent() + "/conf/config/properties/json/MainTask.json";
				String json = FileUtil.ReadFile(filename);
				Type t = new TypeToken<List<MainTaskPo>>() {}.getType();
				mainTaskPoList = GsonUtil.getInstance().fromJson(json, t);
				for(int i = 1;i<=mainTaskPoList.size();i++){
					map.put(i, mainTaskPoList.get(i-1));
				}
				logger.info("==============>: MainTask.json文件加载成功");
			} catch (Exception e) {
				logger.error("=============>: MainTask.jsonjson加载失败");
			}
	}
	
	public String getReward(int musicId,int difficulty){
		for(MainTaskPo po:mainTaskPoList){
			if(po.getMid()==musicId&&po.getDifficulty()==difficulty){
				return po.getDetial();
			}
		}
		return "";
	}
	
	public List<MainTaskPo> getNext(int musicId,int difficulty){
		List<MainTaskPo> list = new ArrayList<>();
		System.out.println("当前歌曲"+musicId+",难度为"+difficulty);
		int current = 1;
		for(MainTaskPo po : mainTaskPoList){
			if(po.getMid()==musicId&&po.getDifficulty()==difficulty){
				current = po.getID();
				break;
			}
		}
		System.out.println("当前任务id"+current);
		for(MainTaskPo po : mainTaskPoList){
			if(po.getOpen_pass()==current){
				list.add(po);
			}
		}
		return list;
	}

	public Map<Integer, MainTaskPo> getMap() {
		return map;
	}

	public void setMap(Map<Integer, MainTaskPo> map) {
		this.map = map;
	}

	public List<MainTaskPo> getMainTaskPoList() {
		return mainTaskPoList;
	}

	public void setMainTaskPoList(List<MainTaskPo> mainTaskPoList) {
		this.mainTaskPoList = mainTaskPoList;
	}
}
