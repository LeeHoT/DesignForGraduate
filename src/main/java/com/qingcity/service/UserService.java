package com.qingcity.service;

import org.springframework.stereotype.Service;

import com.qingcity.entity.UserEntity;

/**
 * 
 * @author leehot
 * @description: 主要负责用户的登录。注册和退出。
 */
@Service
public interface UserService {

	/**
	 * 玩家登录
	 * 
	 * @param user
	 *            玩家信息，， 其中包含用户名及密码
	 * @return -1 登录失败   -2 没有激活  -3 需要创建角色 否则返回玩家id
	 */
	int login(String phone,String password);
	
	/**
	 * 检测是否创建角色
	 * @param userId
	 * @return
	 */
	public boolean checkHaveCreatedRole(int userId);

	/**
	 * 玩家注册 ，此时只插入玩家的username passwordMD5以及 regTime
	 * 
	 * @param user
	 *            注册玩家的信息
	 *            @code 公测时只需传递空值，在方法中并不会使用
	 * @return 注册结果信息, -2 表示手机号已被使用，-1表示激活码不可用
	 * 
	 */
	int register(UserEntity user,String code);

	/**
	 * 可选择的插入玩家信息 但 username password 以及 regTime 为必填项，，使用时需要注意,推荐使用
	 * 
	 * @param user
	 *            玩家信息对象
	 * @return >1 插入成功 否则插入失败
	 */
	int insertSelective(UserEntity user);

	/**
	 * 根据玩家id查询玩家信息
	 * 
	 * @param userId
	 *            玩家id
	 * @return 玩家信息 为空则表示玩家不存在
	 */
	UserEntity selectByUserId(int userId);

	/**
	 * 不可更改注册时间(所有)
	 * 
	 * @param userId
	 */
	void updateByUserId(UserEntity userEntity);

	/**
	 * 可更改除注册时间外的任意一项或者多项
	 * 
	 * @param user
	 */
	void updateByUserIdSelective(UserEntity user);

	/**
	 * 插入玩家信息，，用于注册，，不推荐使用
	 * 
	 * @param user
	 * @return
	 */
	boolean insertUser(UserEntity user);

	/**
	 * 根据用户名密码获取用户信息，用于登录
	 * 
	 * @param phone
	 *            用户名
	 * @return null则用户名或密码错误 否则返回登录玩家的基本信息
	 */
	UserEntity selectUserByPhone(String phone);

	/**
	 * 检验当前电话号是否已经存在
	 * 
	 * @param phone
	 *            需要检验的电话号
	 * @return true 已存在 false 不存在
	 */
	boolean isExistPhone(String phone);

	/**
	 * 根据用户名查询相关信息，检测是否存在
	 * 
	 * @param username
	 *            玩家的用户名
	 * @return true 表示该用户名已被占用，false 表示用户名不存在，可用
	 */
	boolean isExistName(String username);

}
