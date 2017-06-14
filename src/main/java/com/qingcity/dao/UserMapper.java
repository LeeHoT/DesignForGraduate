package com.qingcity.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.qingcity.entity.UserEntity;

/**
 * 
 * @author leehotin
 * @Date 2017年4月14日 下午1:46:41
 * @Description TODO
 */
@Repository
public interface UserMapper {

	/**
	 * 将当前玩家设置为已充值，即不再是首充玩家
	 * 
	 * @param userId
	 *            玩家id
	 */
	 public void updateChargedByUserId(int userId);

	/**
	 * 检查玩家是否是首次充值，如果是第一次则返回
	 * 
	 * @param userId
	 * @return
	 */
	 public Boolean isCharged(int userId);

	/**
	 * 添加用户信息
	 * 
	 * @param userEntity
	 */
	 public Integer insertUser(UserEntity userEntity);

	/**
	 * 可选择行的插入用户信息，例如仅增加电话号码等等
	 * 
	 * @param user
	 * @return
	 */
	 public Integer insertUserSelective(UserEntity user);

	/**
	 * 根据用户ID获取用户所有信息
	 * 
	 * @param userId
	 * @return
	 */
	 public UserEntity selectByUserId(Integer userid);

	/**
	 * 根据用户id删除玩家基本信息 ，，该方法只有管理员可以使用
	 * 
	 * @param userid
	 * @return
	 */
	 public Integer deleteByUserId(Integer userid);

	/**
	 * 不可修改注册时间
	 * 
	 * @param user
	 * @return
	 */
	 public Integer updateByUserIdSelective(UserEntity user);

	/**
	 * 不可修改注册时间
	 * 
	 * @param user
	 * @return
	 */
	 public Integer updateByUserId(UserEntity user);

	/**
	 * 根据用户名查询相关信息，可用于登录使用
	 * 
	 * @param phone
	 * @return
	 */
	 public UserEntity selectUserByPhone(String phone);

	/**
	 * 插入用户身份证号
	 * 
	 * @param idCard
	 */
	 public Integer insertIdCard(@Param("idCard") String idCard, @Param("userId") int userId);

	/**
	 * 获取身份证号
	 * 
	 * @param userId
	 * @return
	 */
	 public String getIdCard(int userId);

	/**
	 * 插入用户email
	 * 
	 * @param email
	 */
	 public Integer insertEmail(@Param("email") String email, @Param("userId") int userId);

	/**
	 * 获取用户EMAIL
	 * 
	 * @param userId
	 * @return
	 */

	 public String getEmail(int userId);

	/**
	 * 插入用户电话号
	 * 
	 * @param phone
	 */
	 public Integer insertPhone(@Param("phone") String phone, @Param("userId") int userId);

	/**
	 * 获取用户电话号
	 * 
	 * @param userId
	 * @return
	 */
	 public String getPhone(int userId);

	/**
	 * 查询该用户名username是否被注册
	 * 
	 * @param username
	 * @return 返回使用该用户名的用户username
	 */
	 public String isExistUsername(String username);

	/**
	 * 查询是否注册了该idCard
	 * 
	 * @param idCard
	 * @return 返回拥有该idCard的用户idCard
	 */
	 public String isExistIdCard(String idCard);

	/**
	 * 查询是否注册了该email
	 * 
	 * @param idCard
	 * @return 返回拥有该idCard的用户email
	 */
	 public String isExistEmail(String email);

	/**
	 * 查询是否注册了该phone
	 * 
	 * @param idCard
	 * @return 返回拥有该idCard的用户id
	 */
	 public String isExistPhone(String phone);

}
