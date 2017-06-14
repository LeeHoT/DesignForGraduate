package com.qingcity.dao;

import java.sql.Timestamp;

import org.apache.ibatis.annotations.Param;

import org.springframework.stereotype.Repository;

import com.qingcity.entity.PlayerEntity;

/**
 * 
 * @author leehotin
 * @Date 2017年4月4日 下午12:13:07
 * @Description 玩家Mapper接口
 */
@Repository("playerMapper")
public interface PlayerMapper {

	/**
	 * 根据id查询昵称
	 * 
	 * @param userId
	 * @return
	 */
	public String selectNicknameById(int userId);

	/**
	 * 获取当前玩家的头像名称
	 * 
	 * @param userId
	 *            玩家Id
	 * @return 玩家的头像名称
	 */
	public String getIcon(int userId);

	/**
	 * 玩家更换头像
	 * 
	 * @param icon
	 *            新头像的名字
	 * @param userID
	 *            玩家id
	 */
	public Integer updateIcon(@Param("icon") String icon, @Param("userId") int userId);

	/**
	 * 根据玩家id查询玩家信息
	 * 
	 * @param userId
	 *            玩家id
	 * @return 玩家信息
	 */
	public PlayerEntity selectByUserId(Integer userId);

	/**
	 * 根据昵称查询玩家信息
	 * 
	 * @param nickname
	 * @return 玩家信息
	 */
	public PlayerEntity selectByNickname(String nickname);

	/**
	 * 只查询该等级的第一个玩家 主要用于pk时使用
	 * 
	 * @param level
	 *            要求的玩家等级
	 * @return 返回查询到的玩家Id
	 */
	public Integer selectUserByLevel(Integer level);

	/**
	 * 根据玩家id删除玩家，一般不使用，，玩家数据在删除users表中的数据时也随之删除
	 * 
	 * @param playerId
	 *            玩家id
	 * @return 更新行数 >0 则表示更新成功
	 */
	public Integer deleteByUserId(Integer userId);

	/**
	 * 添加玩家信息。。只在注册时使用 所有属性均不能为空
	 * 
	 * @param player
	 *            玩家信息对象
	 * @return 更新行数 >0 则表示更新成功
	 */
	public Integer insertPlayer(PlayerEntity player);

	/**
	 * 可选择性的添加玩家信息，可仅插入一项或多项。。一般不使用
	 * 
	 * @param player
	 *            玩家信息对象
	 * @return 更新行数 >0 则表示更新成功
	 */
	public Integer insertSelective(PlayerEntity player);

	/**
	 * 可选择性的更新玩家信息，可仅更新一项或者多项属性，例如增加体力、钻石等
	 * 
	 * @param player
	 *            玩家信息对象
	 * @return 更新行数 >0 则表示更新成功
	 */
	public Integer updateByUserIdSelective(PlayerEntity player);

	/**
	 * 更新用户体力
	 * 
	 * @param playerId
	 *            需要更新的玩家id
	 * @param power
	 *            新增的体力数量
	 */
	public void updatePower(@Param("userId") int userId, @Param("lastPowUpdateTime") Timestamp lastPowUpdateTime,
			@Param("power") int power);

	/**
	 * 根据玩家id获取当前体力值
	 * 
	 * @param playerId
	 *            玩家id
	 * @return 当前玩家当前的体力值
	 */
	public Integer getPower(int userId);

	/**
	 * 获取玩家体力上次刷新时间
	 * 
	 * @param playerId
	 *            玩家id
	 * @return 当前玩家体力上次更新时间
	 */
	public Timestamp getLastPowUpdateTime(int userId);

	/**
	 * 获取用户金币数
	 * 
	 * @param playerId
	 * @return
	 */
	public Integer getGold(int userId);

	/**
	 * 更新用户金币数
	 * 
	 * @param playerId
	 */
	public void updateGold(@Param("userId") int userId, @Param("gold") int gold);

	/**
	 * 获取用户钻石数
	 * 
	 * @param playerId
	 * @return
	 */
	public Integer getDiamond(int userId);

	/**
	 * 更新用户金币数
	 * 
	 * @param playerId
	 */
	public void updateDiamond(@Param("userId") int userId, @Param("diamond") int diamond);

	/**
	 * 更新玩家昵称
	 * 
	 * @param userId
	 *            玩家id
	 * @param nickname
	 *            更新后的昵称
	 */
	public void updateNickname(@Param("userId") int userId, @Param("nickname") String nickname);

	/**
	 * 离开公会
	 * 
	 * @param userId
	 *            准备离开工会的玩家id
	 */
	public void quitSociety(int userId);

	/**
	 * 加入公会
	 * 
	 * @param userId
	 *            玩家id
	 * @param societyId
	 *            公会id
	 */
	public Integer joinSociety(@Param("userId") int userId, @Param("societyId") int societyId, @Param("job") int job);

	/**
	 * 更新玩家贡献值
	 * 
	 * @param userId
	 *            玩家id
	 * @param contribution
	 *            新增贡献值
	 */
	public void updateContribution(@Param("userId") int userId, @Param("contribution") int contribution);

	/**
	 * 更新玩家在公会中的职位
	 * 
	 * @param userId
	 *            玩家id
	 * @param job
	 *            1 会长，2 副会长，3 成员
	 */
	public void updateJob(@Param("userId") int userId, @Param("job") int job);

	/**
	 * 查询玩家的公会信息
	 * 
	 * @param userId
	 * @return
	 */
	public PlayerEntity getPlayerSocietyInfo(int userId);

	/**
	 * 查询公会人数
	 * 
	 * @param societyId
	 * @return
	 */
	public Integer selectSocietyMemberNum(int societyId);

	/**
	 * 更新玩家个性签名
	 * 
	 * @param signature
	 *            个性签名，不得超过五十个字
	 * @param userId
	 *            玩家id
	 * @return
	 */
	public Integer updateSignature(@Param("signature") String signature, @Param("userId") Integer userId);

	/**
	 * 更新玩家最近一次登录时间
	 * 
	 * @param lastLoginTime
	 *            最近一次登录时间
	 * @param userId
	 *            玩家id
	 * @return
	 */
	public Integer updateLoginTime(@Param("lastLoginTime") Timestamp lastLoginTime, @Param("userId") Integer userId);
}
