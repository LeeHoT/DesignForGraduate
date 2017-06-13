package com.qingcity.init.manager;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.reflect.TypeToken;
import com.qingcity.init.entity.BadWordPo;
import com.qingcity.util.FileUtil;
import com.qingcity.util.GsonUtil;

public class BadWordInit {

	private static Logger logger = LoggerFactory.getLogger(BadWordInit.class);

	private Set<BadWordPo> badWord = null;

	private static BadWordInit instance = null;

	public static BadWordInit getInstance() {
		if (instance == null) {
			System.out.println("新建banwordInit对象");
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
				logger.info("badword.json加载成功");
			}
		} catch (Exception e) {
			logger.error("badword.json加载失败");
			e.printStackTrace();
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
		System.out.println("contain badword: "+badWord);
		Iterator<BadWordPo> it = badWord.iterator();
		while (it.hasNext()) {
			if (base.contains(it.next().getValue())) {
				return true;
			}
		}
		return false;
	}
}
