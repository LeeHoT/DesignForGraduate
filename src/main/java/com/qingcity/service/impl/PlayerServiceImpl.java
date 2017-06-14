package com.qingcity.service.impl;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingcity.dao.PlayerMapper;
import com.qingcity.entity.PlayerEntity;
import com.qingcity.init.manager.PlayerUpgradeInit;
import com.qingcity.redis.UserRedis;
import com.qingcity.redis.common.Keys;
import com.qingcity.service.PlayerService;
import com.qingcity.util.BeanUtil;
import com.qingcity.util.StringUtil;
import com.qingcity.util.TimeUtil;

/**
 * 
 * @author leehotin
 * @Date 2017年3月4日 下午12:15:21
 * @Description 玩家常用信息Service
 */
@Service("playerService")
public class PlayerServiceImpl implements PlayerService {

	private static final Logger logger = LoggerFactory.getLogger(PlayerServiceImpl.class);

	private static final Integer POWER_MILLISECOND = 10 * 60 * 1000;// 自动增加一体力需要10分钟的时间

	private static final int INITDIAMOND = 0; // 玩家初始钻石数
	private static final int INITGOLD = 0; // 玩家初始金币数
	private static final int INITPOWER = 5; // 玩家初始体力
	private static final String INITICON = "0";

	// private static final Integer CHAIR = 1;// 会长
	// private static final Integer VICE_CHAIR = 2;// 副会长
	private static final Integer MEMBER = 3;// 成员

	private static final Integer MAX_NICKNAME_LENTGH = 8;

	@Autowired
	private PlayerMapper playerMapper;

	@Override
	public int insertSelective(PlayerEntity player) {
		playerMapper.insertSelective(player);
		savePlayerRedis(player);
		return 1;
	}

	private void savePlayerRedis(PlayerEntity player) {
		Map<String, Object> map = BeanUtil.getInstance().getFidldMap(player);
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() != null) {
				UserRedis.getInstance().add(String.valueOf(player.getUserId()), entry.getKey(),String.valueOf(entry.getValue()));
			}
		}
	}

	/************************* 已经验证 ******************************/

	@Override
	public Integer updateNickname(int userId, String nickname) {
		// 检查昵称的长度
		if (nickname.length() > MAX_NICKNAME_LENTGH) {
			// 昵称过长
			logger.debug("==============>: 昵称过长");
			return -1;
		}
		// 可以更改昵称
		playerMapper.updateNickname(userId, nickname);
		// 数据库更新成功，更新缓存
		UserRedis.getInstance().add(String.valueOf(userId), Keys.USER_NICKNAME, nickname);
		return 1;
	}

	@Override
	public String selectNicknameById(int userId) {
		// 从缓存中查询
		String nickname = UserRedis.getInstance().get(String.valueOf(userId), Keys.USER_NICKNAME);
		if (nickname != null && !nickname.equals("")) {
			// 缓存中有，直接返回
			return nickname;
		}
		// 缓存中没有，从数据库查询后保存的缓存
		nickname = playerMapper.selectNicknameById(userId);
		UserRedis.getInstance().add(String.valueOf(userId), Keys.USER_NICKNAME, nickname);
		return nickname;
	}

	@Override
	public boolean calculateExp(PlayerEntity player, int exp) {
		boolean flag = true;
		// 玩家当前等级升级所需总经验
		boolean needUpdate = false;
		int tempLevel = player.getLevel();
		int tmepExp = player.getExperience();
		int remain = exp + tmepExp;
		do {
			int upTotal = PlayerUpgradeInit.getInstance().getPlayerUpgradePo(tempLevel).getExp();
			// 获取添加经验后剩余的经验
			if (remain >= upTotal) {
				remain = remain - upTotal;
				// 说明经验至少够升的下一级
				tempLevel++;
				needUpdate = true;
			} else {
				flag = false;
			}
		} while (flag);
		player.setLevel(tempLevel);
		player.setExperience(remain);
		return needUpdate;
	}

	@Override
	public PlayerEntity selectByUserId(int userId) {
		// 查看昵称是否存在redis，昵称的存在与否决定是否有该玩家的基础数据信息
		String nickname = UserRedis.getInstance().get(String.valueOf(userId), Keys.USER_NICKNAME);
		PlayerEntity playerEntity = new PlayerEntity();
		playerEntity.setUserId(userId);
		if (StringUtil.isNotEmpty(nickname)) {
			// 说明Redis中存在数据
			Map<String, String> userInfo = UserRedis.getInstance().get(String.valueOf(userId));
			playerEntity.setLevel(Integer.valueOf(userInfo.get(Keys.USER_LEVEL)));
			playerEntity.setPower(Integer.valueOf(userInfo.get(Keys.USER_POWER)));
			playerEntity.setLastPowUpdateTime(TimeUtil.String2Timestamp(userInfo.get(Keys.USER_LAST_POWER_UPDATE_TIME)));
			playerEntity.setNickname(userInfo.get(Keys.USER_NICKNAME));
			playerEntity.setGold(Integer.valueOf(userInfo.get(Keys.USER_GOLD)));
			playerEntity.setDiamond(Integer.valueOf(userInfo.get(Keys.USER_DIAMOND)));
			playerEntity.setContribution(Integer.valueOf(userInfo.get(Keys.USER_CONTRIBUTION)));
			playerEntity.setIcon(userInfo.get(Keys.USER_AVATAR));
			playerEntity.setExperience(Integer.valueOf(userInfo.get(Keys.USER_EXPERIENCE)));
			playerEntity.setJob(Integer.valueOf(userInfo.get(Keys.USER_SOCIETY_JOB)));
			playerEntity.setSocietyId(Integer.valueOf(userInfo.get(Keys.USER_SOCIETY_ID)));
			playerEntity.setLastLoginTime(TimeUtil.String2Timestamp(userInfo.get(Keys.USER_LAST_LOGIN_TIME)));
			playerEntity.setExperience(Integer.valueOf(userInfo.get(Keys.USER_EXPERIENCE)));
			playerEntity.setSignature(userInfo.get(Keys.USER_SIGNATURE));
			return playerEntity;
		}
		// 缓存中没有，从数据库中查
		playerEntity = playerMapper.selectByUserId(userId);
		if (playerEntity != null) {
			Map<String, String> map = new LinkedHashMap<>();
			Map<String, Object> objMap = BeanUtil.getInstance().getFidldMap(playerEntity);
			for (Map.Entry<String, Object> entry : objMap.entrySet()) {
				map.put(entry.getKey(), String.valueOf(entry.getValue()));
			}
			UserRedis.getInstance().adds(String.valueOf(userId), map);
		}
		return playerEntity;
	}

	@Override
	public Timestamp getLastAddPowerTime(int userId) {
		// 缓存中查一查
		String time = UserRedis.getInstance().get(String.valueOf(userId), Keys.USER_LAST_POWER_UPDATE_TIME);
		if (time != null && !time.equals("")) {
			// 缓存中存在，直接返回
			return TimeUtil.String2Timestamp(time);
		}
		// 从数据库中查询
		Timestamp updateTime = playerMapper.getLastPowUpdateTime(userId);
		// 更新缓存，
		UserRedis.getInstance().add(String.valueOf(userId), Keys.USER_LAST_POWER_UPDATE_TIME,
				String.valueOf(updateTime));
		return updateTime;
	}

	@Override
	public boolean recoverPower(PlayerEntity player) {
		Timestamp lastUpdateTime = player.getLastPowUpdateTime();
		int number = (int) ((System.currentTimeMillis() - lastUpdateTime.getTime()) / POWER_MILLISECOND);
		int total = number + player.getPower();
		int max = PlayerUpgradeInit.getInstance().getPowerLimit(player.getLevel());
		total = total > max ? max : total;
		if(number == 0){
			return false;
		}
		if (total != max) {
			player.setLastPowUpdateTime(new Timestamp(lastUpdateTime.getTime() + number * POWER_MILLISECOND));
		}else{
			player.setLastPowUpdateTime(TimeUtil.getCurrentTimestamp());
		}
		player.setPower(total);
		
		return true;
	}

	@Override
	public int deleteByUserId(int userId) {
		return playerMapper.deleteByUserId(userId);

	}

	@Override
	public int updateByUserIdSelective(PlayerEntity playerEntity) {
		// 更新数据库
		playerMapper.updateByUserIdSelective(playerEntity);
		// 更新缓存
		savePlayerRedis(playerEntity);
		return 1;
	}

	@Override
	public int selectUserByLevel(int level) {
		return playerMapper.selectUserByLevel(level);
	}

	@Override
	public int getPower(int userId) {

		return playerMapper.getPower(userId);

	}

	@Override
	public int updateGold(int gold, int userId) {
		int old_gold = getGold(userId);// 查询当前玩家的原始体力
		if (gold + old_gold < 0) {
			// 此时金币不足，消耗体力，且现有体力不足
			logger.info("==============>: 玩家[{}]的金币数量不足，无法完成消费", userId);
			return -1;
		}
		playerMapper.updateGold(userId, gold);
		UserRedis.getInstance().add(String.valueOf(userId), Keys.USER_GOLD, gold);
		return 1;
	}

	@Override
	public int getGold(int userId) {
		// 从缓存中查一查
		String gold = UserRedis.getInstance().get(String.valueOf(userId), Keys.USER_GOLD);
		if (gold != null && !gold.equals("")) {
			// 缓存中有，，直接返回
			return Integer.valueOf(gold);
		}
		// 缓存中没有，查询后保存到缓存
		int dbGold = playerMapper.getGold(userId);
		UserRedis.getInstance().add(String.valueOf(userId), Keys.USER_POWER, String.valueOf(dbGold));
		return dbGold;
	}

	@Override
	public int updateDiamond(int diamond, int userId) {
		int old_diamond = getDiamond(userId);
		if (old_diamond + diamond < 0) {
			logger.info("==============>: 玩家[{}]的钻石数量不足，无法完成消费", userId);
			return -1;
		}
		playerMapper.updateDiamond(userId, diamond);
		UserRedis.getInstance().add(String.valueOf(userId), Keys.USER_DIAMOND, diamond);
		return 1;
	}

	@Override
	public int getDiamond(int userId) {
		// 缓存中走一走
		String diamond = UserRedis.getInstance().get(String.valueOf(userId), Keys.USER_DIAMOND);
		if (diamond != null && !diamond.equals("")) {
			// 缓存中有，，直接返回
			return Integer.valueOf(diamond);
		}
		// 缓存中没有，查询后保存到缓存
		int dbDiamond = playerMapper.getDiamond(userId);
		UserRedis.getInstance().add(String.valueOf(userId), Keys.USER_DIAMOND, String.valueOf(dbDiamond));
		return dbDiamond;
	}

	@Override
	public Integer updateIcon(String icon, int userId) {
		// 头像直接更新数据库然后保存到缓存
		int result = playerMapper.updateIcon(icon, userId);
		if (result <= 0) {
			// 更新失败
			logger.error("玩家[{}]的头像更新失败" + userId);
			return -1;
		}
		UserRedis.getInstance().add(String.valueOf(userId), Keys.USER_AVATAR, icon);
		return 1;
	}

	@Override
	public String getIcon(int userId) {
		String avatar = UserRedis.getInstance().get(String.valueOf(userId), Keys.USER_AVATAR);
		if (StringUtil.isNotNull(avatar)) {
			return avatar;
		}
		avatar = playerMapper.getIcon(userId);
		UserRedis.getInstance().add(String.valueOf(userId), Keys.USER_AVATAR, avatar);
		return avatar;
	}

	@Override
	public boolean hasManagerAuth(int userId) {
		PlayerEntity player = playerMapper.getPlayerSocietyInfo(userId);
		if (player.getJob() != MEMBER) {
			return true;
		}
		return false;
	}

	@Override
	public PlayerEntity selectByNickname(String nickname) {
		return playerMapper.selectByNickname(nickname);
	}

	@Override
	public void updateLoginTime(int userId) {
		int result = playerMapper.updateLoginTime(TimeUtil.getCurrentTimestamp(), userId);
		if (result <= 0) {
			// 更新失败
			logger.error("==============>: 登录时间更新失败");
			return;
		}
		UserRedis.getInstance().add(String.valueOf(userId), Keys.USER_LAST_LOGIN_TIME,
				String.valueOf(TimeUtil.getCurrentTimestamp()));
	}

	@Override
	public void updateSignature(String signature, int userId) {
		int result = playerMapper.updateSignature(signature, userId);
		if (result <= 0) {
			logger.error("==============>: 个性签名更新失败");
		}
		UserRedis.getInstance().add(String.valueOf(userId), Keys.USER_SIGNATURE, signature);
		logger.info("==============>: 个性签名缓存更新成功");
	}

	@Override
	public boolean initPlayer(int userId, String nickname) {
		if (selectByNickname(nickname) != null) {
			return false;
		}
		// 初始化基本信息
		PlayerEntity playerEntity = new PlayerEntity();
		playerEntity.setUserId(userId);
		playerEntity.setExperience(0);
		playerEntity.setLevel(1);
		playerEntity.setDiamond(INITDIAMOND);
		playerEntity.setGold(INITGOLD);
		playerEntity.setPower(INITPOWER);
		playerEntity.setLastPowUpdateTime(TimeUtil.getCurrentTimestamp());
		playerEntity.setIcon(INITICON);
		playerEntity.setSocietyId(-1);
		playerEntity.setJob(0);
		playerEntity.setContribution(0);
		playerEntity.setNickname(nickname);
		playerEntity.setSignature("乐随你动");
		playerEntity.setLastLoginTime(TimeUtil.getCurrentTimestamp());
		insertSelective(playerEntity);
		return true;
	}

}
