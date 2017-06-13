package com.qingcity.init.entity;

/**
 * 
 * @author leehotin
 * @Date 2017年4月12日 上午12:32:59
 * @Description TODO
 */
public class ShopGiftPo {

	@Override
	public String toString() {
		return ID + "," + money + "," + prize + "," + detail;
	}
	
	

	private int ID;

	private int money;

	private int prize;

	private String detail;
	private int limit;

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
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

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}
