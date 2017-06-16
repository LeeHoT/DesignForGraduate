package com.qingcity.sd.entity;

public enum TaskType {
	
	NULL(-1),
	
	DAILY_TASK(1),
	
	MAIN_TASK(2),
	
	;
	
	private int value;
	private TaskType(int value){
		this.value = value;
	}
	
	public static TaskType parse(int value){
		for(TaskType type: TaskType.values()){
			if(type.getValue() == value){
				return type;
			}
		}
		return NULL;
	}
	
	public int getValue(){
		return this.value;
	}

}
