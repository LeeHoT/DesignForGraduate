package com.qingcity.sd.entity;

/**
 * 
 * @author lihuiting
 *
 */
public enum ShopType {

	NULL(-1,"null"),

	GROCERY(2,"ShopGrocery"),

	GIFT(1,"ShopGift"),

	RECHARGE(3,"ShopRecharge"), ;

	private int value;
	private String po;

	private ShopType(int value,String po) {
		this.value = value;
		this.po = po;
	}

	public static ShopType parse(int value) {
		for (ShopType st : ShopType.values()) {
			if (st.getValue() == value) {
				return st;
			}
		}
		return NULL;
	}

	public int getValue() {
		return value;
	}
	
	public String getPo(){
		return po;
	}
}
