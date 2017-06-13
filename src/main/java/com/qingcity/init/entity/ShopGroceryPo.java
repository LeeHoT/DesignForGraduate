package com.qingcity.init.entity;

/**
 * 
 * @author leehotin
 * @Date 2017年4月12日 上午12:33:41
 * @Description TODO
 */
public class ShopGroceryPo {

	private int ID;

	private String detail;
	/** 购买所需类型 0 金币 1钻石 */
	private int money;

	private int prize;
	


	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getPrize() {
		return prize;
	}

	public void setPrize(int prize) {
		this.prize = prize;
	}

	@Override
	public String toString() {
		return ID + "," + detail + "," + money + "," + prize;
	}

}
