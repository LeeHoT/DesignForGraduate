package com.qingcity.init.entity;

/**
 * 
 * @author leehotin
 * @Date 2017年4月16日 上午11:33:55
 * @Description TODO
 */
public class PlayerUpgradePo {
	private int id;
	
	private int level;
	
	private int exp;
	/**体力上限增长数值*/
	private int incrPower;
	/**升级奖励钻石*/
	private int rewardDiamond;
	/**升级奖励金币*/
	private int rewardGold;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getIncrPower() {
		return incrPower;
	}

	public void setIncrPower(int incrPower) {
		this.incrPower = incrPower;
	}

	public int getRewardDiamond() {
		return rewardDiamond;
	}

	public void setRewardDiamond(int rewardDiamond) {
		this.rewardDiamond = rewardDiamond;
	}

	public int getRewardGold() {
		return rewardGold;
	}

	public void setRewardGold(int rewardGold) {
		this.rewardGold = rewardGold;
	}

}
