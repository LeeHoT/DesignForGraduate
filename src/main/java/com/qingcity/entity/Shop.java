package com.qingcity.entity;

import java.util.Date;

import com.qingcity.init.entity.ShopType;

/**
 * 
 * @author lihuiting
 *
 */
public class Shop {

	/** 玩家id */
	private int userId;
	/** s商店类型 */
	private ShopType shopType;
	/** 购买的商品id */
	private int pid;
	/** 消费货币 */
	private int money;
	/** 购买数量 */
	private int number;
	/** 商品总价 */
	private float prize;
	/** 购买时间 */
	private Date buyDate;

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public ShopType getShopType() {
		return shopType;
	}

	public void setShopType(ShopType shopType) {
		this.shopType = shopType;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public float getPrize() {
		return prize;
	}

	public void setPrize(float prize) {
		this.prize = prize;
	}

	public Date getBuyDate() {
		return buyDate;
	}

	public void setBuyDate(Date buyDate) {
		this.buyDate = buyDate;
	}

}
