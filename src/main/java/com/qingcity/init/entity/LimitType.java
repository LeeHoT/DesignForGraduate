package com.qingcity.init.entity;

public enum LimitType {
	
	NO_LIMIT(0,false),
	
	DAY_LIMIT(1,true),
	
	WEEK_LIMIT(2,true),
	
	NEW_PLAYER(3,true),
	
	;
	
	private int value;
	private boolean limit;
	
	private LimitType(int value,boolean limit){
		this.value = value;
		this.limit = limit;
	}
	public static LimitType parse(int v){
		for(LimitType type : LimitType.values()){
			if(type.getValue() == v){
				return type;
			}
		}
		return NO_LIMIT;
	}
	
	public int getValue(){
		return this.value;
	}
	
	public boolean isLimited(){
		return this.limit;
	}

}
