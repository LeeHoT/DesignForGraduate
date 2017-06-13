package com.qingcity.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingcity.dao.UserMapper;
import com.qingcity.entity.PlayerEntity;
import com.qingcity.entity.UserEntity;
import com.qingcity.redis.UserRedis;
import com.qingcity.redis.common.Keys;
import com.qingcity.service.PlayerService;
import com.qingcity.service.UserService;
import com.qingcity.util.BeanUtil;
import com.qingcity.util.StringUtil;

/**
 * 
 * @author leehotin
 * @Date 2017年3月4日 下午12:14:07
 * @Description 玩家基础信息Service
 */
@Service("userService")
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private PlayerService playerService;

	@Override
	public synchronized int login(String phone, String password) {
		// 登录所用的用户名为用户的手机号，一定要主要这个问题
		String dbPassword = selectPassByPhone(phone);
		if (dbPassword != null && !dbPassword.trim().equals("") && dbPassword.equals(password)) {
			System.out.println("congration" + phone + "login success");
		} else {
			System.out.println("username or password is wrong");
			// 登陆失败
			return -1;
		}
		return selectUidByPhone(phone);
	}

	public boolean checkHaveCreatedRole(int userId) {

		// 检查是否有角色信息
		PlayerEntity player = playerService.selectByUserId(userId);
		if (player != null) {
			// 角色信息已创建
			return true;
		}
		// 未创建角色信息
		return false;
	}

	@Override
	public int register(UserEntity user, String code) {
		// 登录成功，，检测当前时间是开发还是公测
		// 检查激活码是否存在
		// 激活码可以使用
		if (isExistPhone(user.getPhone())) {
			System.out.println("phone has been");
			return -2;
		}
		// 可以注册。。先添加个人信息，后添加玩家基本信息进入玩家表player
		insertSelective(user);
		logger.info("玩家[{}]的基本信息填写完成,玩家id为[{}]", user.getUsername(), user.getUserId());
		return user.getUserId();
	}

	@Override
	public boolean isExistPhone(String phone) {
		String dbPhone = userMapper.isExistPhone(phone);
		if (dbPhone != null && !dbPhone.trim().equals("")) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isExistName(String username) {
		if (userMapper.isExistUsername(username) != null) {
			System.out.println("name can be use");
			return true;
		}
		return false;
	}

	@Override
	public boolean insertUser(UserEntity msg) {
		if (userMapper.insertUser(msg) > 0) {
			return true;
		}
		return false;
	}

	@Override
	public UserEntity selectUserByPhone(String phone) {
		String userIdStr = UserRedis.getInstance().get(phone, Keys.USER_ID);
		UserEntity userEntity = new UserEntity();
		if (userIdStr == null || "".equals(userIdStr)) {
			// 说明Redis没有当前用户名的数据
			// /TODO 方法需要更改
			userEntity = userMapper.selectUserByPhone(phone);
			if (userEntity == null) {
				return null;
			}
			saveUserRedis(userEntity);
		} else {
			userEntity = (UserEntity) BeanUtil.getInstance().map2Object(UserRedis.getInstance().get(userIdStr),
					userEntity);
		}
		return userEntity;
	}

	public String selectPassByPhone(String phone) {
		String pass = UserRedis.getInstance().getLoginRedis(phone);
		if (StringUtil.isNotNull(pass)) {
			return pass;
		}
		UserEntity user = selectUserByPhone(phone);
		if (user == null) {
			return null;
		}
		pass = user.getPasswordMd5();
		UserRedis.getInstance().addLoginRedis(phone, pass);
		return pass;
	}

	public Integer selectUidByPhone(String phone) {
		String userId = UserRedis.getInstance().getUidPhone(phone);
		if (userId != null && !userId.trim().equals("")) {
			return Integer.valueOf(userId);
		}
		UserEntity user = selectUserByPhone(phone);
		UserRedis.getInstance().addUidPhone(phone, String.valueOf(user.getUserId()));
		return user.getUserId();
	}

	private void saveUserRedis(UserEntity user) {
		Map<Object, Object> map = BeanUtil.getInstance().getFidldMap(user);
		for (Map.Entry<Object, Object> entry : map.entrySet()) {
			if (entry.getValue() != null) {
				UserRedis.getInstance().add(user.getUserId().toString(), String.valueOf(entry.getKey()),
						String.valueOf(entry.getValue()));
			}
		}
	}

	@Override
	public int insertSelective(UserEntity user) {
		userMapper.insertUserSelective(user);
		saveUserRedis(user);
		return user.getUserId();
	}

	@Override
	public UserEntity selectByUserId(int userId) {
		Map<String, String> map = UserRedis.getInstance().get(String.valueOf(userId));
		UserEntity user = new UserEntity();
		if (map != null && map.size() > 0) {
			user = (UserEntity) BeanUtil.getInstance().map2Object(map, user);
			return user;
		}
		user = userMapper.selectByUserId(userId);
		saveUserRedis(user);
		return user;
	}

	@Override
	public void updateByUserId(UserEntity userEntity) {
		userMapper.updateByUserId(userEntity);
		saveUserRedis(userEntity);
	}

	@Override
	public void updateByUserIdSelective(UserEntity user) {
		userMapper.updateByUserIdSelective(user);
		saveUserRedis(user);
	}
}
