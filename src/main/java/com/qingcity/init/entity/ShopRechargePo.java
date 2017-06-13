package com.qingcity.init.entity;

/**
 * 
 * @author leehotin
 *
 */
public class ShopRechargePo {

	/** 商品id */
	private int id;

	private int amount;

	// RMB
	private int money;
	/** 商品价格 */
	private int prize;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getPrize() {
		return prize;
	}

	public void setPrize(int prize) {
		this.prize = prize;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	@Override
	public String toString() {
		return id + "," + amount + "," + money + "," + prize;
	}

}
