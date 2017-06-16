package com.qingcity.sd.manager;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.reflect.TypeToken;
import com.qingcity.base.util.FileUtil;
import com.qingcity.base.util.GsonUtil;
import com.qingcity.sd.entity.BadWordPo;

public class BadWordInit {

	private static Logger logger = LoggerFactory.getLogger(BadWordInit.class);

	private Set<BadWordPo> badWord = null;

	private static BadWordInit instance = null;

	public static BadWordInit getInstance() {
		if (instance == null) {
			instance = new BadWordInit();
		}
		return instance;
	}

	public BadWordInit() {
		instance = this;
	}

	public Set<BadWordPo> getBadWord() {
		return badWord;
	}

	public void setBadWord(Set<BadWordPo> badWord) {
		this.badWord = badWord;
	}

	public void init() {
		try {
			String u = getInstance().getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			String filename = new File(u).getParent()+"/conf/config/properties/json/badword.json";
			String json = FileUtil.ReadFile(filename);
			Type type = new TypeToken<Set<BadWordPo>>() {
			}.getType();
			badWord = GsonUtil.getInstance().fromJson(json, type);
			if(badWord!=null&&badWord.size()>0){
				logger.info("==============>: badword.json加载成功");
			}
		} catch (Exception e) {
			logger.error("=============>: badword.json加载失败");
		}
	}

	public String replace(String base, String replace) {
		Iterator<BadWordPo> it = badWord.iterator();
		String newBase = "";
		while (it.hasNext()) {
			String bad = it.next().getValue();
			StringBuffer sb = new StringBuffer();
			if (base.contains(bad)) {
				for (int i = 0; i < bad.length(); i++) {
					sb.append(replace);
				}
				newBase = base.replace(bad, sb.toString());
			}
		}
		return newBase;
	}

	public boolean containBadWord(String base) {
		Iterator<BadWordPo> it = badWord.iterator();
		BadWordPo bw = null;
		while (it.hasNext()) {
			bw = it.next();
			if (base.contains(bw.getValue())) {
				logger.info("包含非法字符[{}]",bw.getValue());
				return true;
			}
		}
		return false;
	}
}
