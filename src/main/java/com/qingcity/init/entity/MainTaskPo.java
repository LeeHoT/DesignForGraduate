package com.qingcity.init.entity;

/**
 * 
 * @author leehotin
 * @Date 2017年4月16日 下午7:29:59
 * @Description TODO
 */
public class MainTaskPo {
	
	private int ID;
	
	private String name;
	
	private int mid;
	
	private int difficulty;
	
	private int grade;
	
	private int open_level;
	
	private int open_pass;
	
	private int open_city;
	
	private String detial;
	
	private String detial_reward_task;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getOpen_level() {
		return open_level;
	}

	public void setOpen_level(int open_level) {
		this.open_level = open_level;
	}

	public int getOpen_pass() {
		return open_pass;
	}

	public void setOpen_pass(int open_pass) {
		this.open_pass = open_pass;
	}

	public int getOpen_city() {
		return open_city;
	}

	public void setOpen_city(int open_city) {
		this.open_city = open_city;
	}

	public String getDetial() {
		return detial;
	}

	public void setDetial(String detial) {
		this.detial = detial;
	}

	public String getDetial_reward_task() {
		return detial_reward_task;
	}

	public void setDetial_reward_task(String detial_reward_task) {
		this.detial_reward_task = detial_reward_task;
	}
	
	

}
