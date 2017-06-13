package com.qingcity.init.entity;

import java.util.ArrayList;
import java.util.List;

import com.qingcity.util.RandomUtil;

public enum DyeColorType {
	/**红*/
	RED(51),
	/**橙*/
	ORANGE(52),
	/**黄*/
	YELLOW(53),
	/**绿*/
	GREEN(54),
	/**蓝*/
	BLUE(55),
	/**紫*/
	PURPLE(56),
	/**粉*/
	PINK(57),
	/**黑*/
	BLACK(58),
	/**白*/
	WHITE(59),
	
	;
	
	private int value;
	
	private DyeColorType(int value){
		this.value = value;
	}
	
	public static DyeColorType parse(int v){
		for(DyeColorType type:DyeColorType.values()){
			if(type.getValue()==v){
				return type;
			}
		}
		return BLACK;
	}
	
	public int getValue(){
		return value;
	}
	
	public static List<Integer> getRandom(int number){
		List<Integer> list = new ArrayList<>();
		for(int i =0;i<number;i++){
			int id = RandomUtil.getRandomInt(RED.getValue(), WHITE.getValue());
			list.add(id);
		}
		return list;
	}
}
