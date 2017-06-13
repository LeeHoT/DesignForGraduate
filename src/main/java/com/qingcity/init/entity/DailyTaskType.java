package com.qingcity.init.entity;

/**
 * 
 * @author leehotin
 * @Date 2017年4月16日 下午7:22:03
 * @Description TODO
 */
public enum DailyTaskType {
	NULL(-1, -1),

	PLAIN(1, 5),

	PK(2, 3),

	SEND_POWER(3, 10),

	MESSAGE(4, 1),

	SPEAK_IN_SOCIETY(5, 1),

	ALL(6, 0),;
	private int value;
	private int times;

	private DailyTaskType(int value, int times) {
		this.value = value;
		this.times = times;
	}

	public static DailyTaskType parse(int value) {
		for (DailyTaskType type : DailyTaskType.values()) {
			if (type.getValue() == value) {
				return type;
			}
		}
		return NULL;
	}

	public int getValue() {
		return this.value;
	}

	public int getTimes() {
		return this.times;
	}

}
