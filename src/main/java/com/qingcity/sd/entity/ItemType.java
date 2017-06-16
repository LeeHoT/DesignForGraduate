package com.qingcity.sd.entity;

public enum ItemType {

	NULL(-1,false),

	GOLD(0,false),

	DIAMOND(1,false),

	POWER(2,false),
	
	RENAME_CARD(3,true),
	
	BRUSH(4,true),
	
	DYE(5,true),
	
	WORKBOX(6,true),
	
	DOUBLE_GLOD(7,true),
	
	DOUBLE_EXP(8,true),
	
	SOCIETY_RENAME_CARD(9,true),
	
	CLOTHES(10,true),
	
	PANTS(11,true),
	
	SHOES(12,true),
	
	PACKAGE(13,true),
	
	EXP(14,false),
	
	;

	private int value;

	private ItemType(int value,boolean inPack) {
		this.value = value;

	}

	public static ItemType parse(int value) {
		for (ItemType type : ItemType.values()) {
			if (type.getValue() == value) {
				return type;
			}
		}
		return NULL;
	}

	public int getValue() {
		return this.value;
	}
}
