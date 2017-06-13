package com.qingcity.redis;

import redis.clients.jedis.Tuple;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.qingcity.redis.common.Config;
import com.qingcity.util.RandomUtil;

/**
 * 
 * @author leehotin
 * @Date 2017年2月27日 下午4:52:17
 * @Description 排名数据缓存
 */
public class RankRedis {
	private static final RankRedis instance = new RankRedis();

	public static RankRedis getInstance() {
		return instance;
	}

	public void del(String key) {
		RedisManager.del(key);
	}

	/**
	 * 增加排行榜数据
	 *
	 * @param score
	 *            积分
	 * @param userID
	 *            用户ID
	 */
	public void add(String key, int score, String userID) {
		RedisManager.zadd(key, score, userID);
	}

	/**
	 * 增加排行榜数据
	 *
	 * @param userID
	 *            用户ID
	 * @param score
	 *            积分
	 */
	public void add(String key, String userID, int score) {
		RedisManager.zincrBy(key, userID, score);
	}

	/**
	 * 获取用户积分信息
	 *
	 * @param userID
	 *            用户ID
	 * @return 用户积分
	 */
	public int getScore(String key, String userID) {
		int score = 0;
		try {
			score = (int) RedisManager.zscore(key, userID);
			return score;
		} catch (Exception e) {
			return score;
		}
	}

	/**
	 * 获取用户积分倒序排名
	 *
	 * @param userID
	 *            用户ID
	 * @return 积分倒序排名
	 */
	public int getRank(String key, String userID) {
		int rank = 0;
		try {
			 rank= (int) RedisManager.zrevrank(key, userID);
			return rank;
		} catch (Exception e) {
			rank = 0;
			return rank;
		}
	}

	/**
	 * 获取排行段
	 *
	 * @param start
	 *            排名起始
	 * @param stop
	 *            排名结束
	 * @return 排名信息
	 */
	public Set<Tuple> getRangeWithScores(String key, int start, int stop) {
		return RedisManager.zrevrangeWithScores(key, start, stop);
	}

	/**
	 * 获取排行榜
	 *
	 * @param userID
	 *            用户ID
	 * @return 排行榜Map value 为userId_score
	 */
	public Map<String, String> getRanking(String key, String userID) {
		boolean flag = true;
		int start = 1;
		Map<String, String> map = null;
		
		System.out.println(key+"排行榜共有这么多条数据"+RedisManager.zCard(key));
		Set<Tuple> set = RankRedis.getInstance().getRangeWithScores(key, start, Config.RANKING_MAX);
		if(set.size()==0){
			System.out.println("不存在排行榜信息");
			 return map;
		}
		System.out.println("获取排名的键"+key+"大小为"+set.size());
		map = new LinkedHashMap<String, String>();			
		for (Tuple tuple : set) {
			String rankUserID = tuple.getElement();
			// long score = (long) tuple.getScore();
			// map.put(String.valueOf(start++), rankUserID + Keys.DELIMITER +
			String value = (String.valueOf(start++))+","+tuple.getScore();
			map.put(rankUserID,value);
			if (rankUserID.equals(userID))
				flag = false;
		}

		if (flag) {
			long rank = RankRedis.getInstance().getRank(key, userID);
			System.out.println("排行榜中玩家自己的成绩为"+rank);
			long score = getScore(key, userID);
			System.out.println("正确载入玩家成绩");
			map.put(userID,String.valueOf(rank)+","+score);
		}
		return map;
	}
}
