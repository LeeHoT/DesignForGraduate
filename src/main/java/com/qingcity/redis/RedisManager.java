package com.qingcity.redis;

import redis.clients.jedis.*;

import java.util.*;

import com.qingcity.redis.common.Config;

/**
 * 
 * @author leehotin
 * @Date 2017年2月27日 上午11:17:34
 * @Description Redis管理类
 */
public class RedisManager {

	private static JedisPool jedisPool;

	static {
		if (jedisPool == null) {
			// 建立连接池配置参数
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(Config.REDIS_MAX_TOTAL);
			config.setMaxWaitMillis(Config.REDIS_MAX_WAIT_MILLIS);
			config.setMaxIdle(Config.REDIS_MAX_IDLE);
			config.setTestOnBorrow(Config.REDIS_TEST_ON_BORROW);

			// config.setMaxTotal(8);
			// config.setMaxWaitMillis(100000);
			// config.setMaxIdle(8);
			// config.setTestOnBorrow(true);
			// 创建连接池
			System.out.println("创建Redis 连接池");
			// jedisPool = new JedisPool(config, "127.0.0.1",6379, 504000,
			// "root");

			 jedisPool = new JedisPool(config, Config.REDIS_HOST,
			 Config.REDIS_PORT, Config.REDIS_EXPIRE_TIME);
//			jedisPool = new JedisPool(config, Config.REDIS_HOST, Config.REDIS_PORT, Config.REDIS_EXPIRE_TIME,
//					Config.REDIS_AUTH);

		}
	}
	
	public static void batchDel(String patten){
		Jedis client = jedisPool.getResource();
		Set<String> set = client.keys(patten + "*");
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String keyStr = it.next();
			System.out.println(keyStr);
			client.del(keyStr);
		}
		releaseResource(client);
	}

	public static void flushDB() {
		Jedis client = jedisPool.getResource();
		try {
			client.flushDB();
		} finally {
			releaseResource(client);
		}
	}

	/**
	 * 释放redis资源
	 * 
	 * @param jedis
	 */
	private static void releaseResource(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

	/**
	 * 添加key value的键值对
	 * 
	 * @param key
	 * @param value
	 */
	public static void add(String key, String value) {
		Jedis client = jedisPool.getResource();
		try {
			client.set(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			releaseResource(client);
		}
	}

	/**
	 * 删除键位key的String，String键值对
	 * 
	 * @param key
	 */
	public static void del(String key) {
		Jedis client = jedisPool.getResource();
		try {
			client.del(key);
		} finally {
			releaseResource(client);
		}
	}

	/**
	 * 获取键为key的value字符串，key 不存在那么返回特殊值 nil
	 * 
	 * @param key
	 * @return
	 */
	public static String get(String key) {
		Jedis client = jedisPool.getResource();
		String value = null;
		try {
			value = client.get(key);
		} finally {
			releaseResource(client);
		}
		return value;
	}

	/**
	 * 删除指定的key
	 * 
	 * @param key
	 */
	public static void trandDel(String... key) {
		Jedis client = jedisPool.getResource();
		try {
			Transaction tx = client.multi();
			client.del(key);
			tx.exec();
		} finally {
			releaseResource(client);
		}
		
	}

	/**
	 * 为名称为key 的String 增 increment 操作
	 * 
	 * @param key
	 * @param increment
	 * @return
	 */
	public static long incrBy(String key, long increment) {
		// final String keyf = (String) key;
		// redisTemplate.execute(new RedisCallback<Long>() {
		// public Long doInRedis(RedisConnection connection) throws
		// DataAccessException {
		// return connection.del(keyf.getBytes());
		// }
		// });
		Jedis client = jedisPool.getResource();
		long value = 0;
		try {
			 value = client.incrBy(key, increment);
		} finally {
			releaseResource(client);
		}
		return value;
	}

	/**
	 * 向名称为key的hash中添加元素field<—>value
	 * 
	 * @param key
	 * @param field
	 * @param value
	 */
	public static void hset(String key, String field, String value) {
		Jedis client = jedisPool.getResource();
		try {
			client.hset(key, field, value);
			
		} finally {
			releaseResource(client);
		}
	}

	/**
	 * 向名称为key的hash中添加元素field<—>bytes
	 * 
	 * @param key
	 * @param field
	 * @param bytes
	 */
	public static void hset(byte[] key, byte[] field, byte[] bytes) {
		Jedis client = jedisPool.getResource();
		try {
			client.hset(key, field, bytes);
		} finally {
			releaseResource(client);
		}
	}

	/**
	 * 返回名称为key的hash中field对应的value
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public static String hget(String key, String field) {
		Jedis client = jedisPool.getResource();
		String value  = null;
		try {
			value = client.hget(key, field);
		} finally {
			releaseResource(client);
		}
		return value;
	}

	/**
	 * 返回名称为key的hash中所有的键（field）及其对应的value
	 * 
	 * @param key
	 * @return
	 */
	public static Map<String, String> hgetall(String key) {
		Jedis client = jedisPool.getResource();
		Map<String, String> valueMap = client.hgetAll(key);
		releaseResource(client);

		return valueMap;
	}

	/**
	 * 将名称为key的hash中field的value增加integer
	 * 
	 * @param key
	 * @param field
	 * @param increment
	 * @return
	 */
	public static long hincrBy(String key, String field, long increment) {
		Jedis client = jedisPool.getResource();
		long value = 0;
		try {
			value = client.hincrBy(key, field, increment);
		} finally {
			releaseResource(client);
		}
		return value;
	}

	/**
	 * 名称为key的hash中是否存在键为field的域
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public static boolean hexists(String key, String field) {
		
		Jedis client = jedisPool.getResource();
		boolean flag = false;
		try {
			flag = client.hexists(key, field);
		} finally {
			releaseResource(client);
		}
		return flag;
	}

	/**
	 * 删除名称为key的hash中键为field的域
	 * 
	 * @param key
	 * @param field
	 */
	public static void hdel(String key, String... field) {
		
		Jedis client = jedisPool.getResource();
		try {
			client.hdel(key, field);
		} finally {
			releaseResource(client);
		}
	}

	/**
	 * 求并集
	 * 
	 * @param keys
	 * @return
	 */
	public static Set<String> sunion(List<String> keys) {
		Jedis client = jedisPool.getResource();
		Set<String> valueSet = null;
		try {
		    valueSet = client.sunion((String[]) keys.toArray());
		} finally {
			releaseResource(client);
		}
		return valueSet;
	}

	/**
	 * 返回一个集合的全部成员，该集合是所有给定集合的交集。 不存在的 key 被视为空集。当给定集合当中有一个空集时，结果也为空集(根据集合运算定律)。
	 * 
	 * @param key
	 *            过个键
	 * @return
	 */
	public static Set<String> sinter(String... key) {
		Jedis client = jedisPool.getResource();
		Set<String> set = null;
		try {
			set = client.sinter(key);
		} finally {
			releaseResource(client);
		}
		return set;
	}

	/**
	 * 向名称为key的set中添加元素member
	 * 
	 * @param key
	 * @param member
	 */
	public static void sadd(String key, String member) {
		Jedis client = jedisPool.getResource();
		try {
			client.sadd(key, member);
		} finally {
			releaseResource(client);
		}
	}
	
	public static void saddBatch(String key,String[] list){
		Jedis client = jedisPool.getResource();
		try {
			client.sadd(key, list);
		} finally {
			releaseResource(client);
		}
	}

	/**
	 * 删除名称为key的set中的元素member
	 * 
	 * @param key
	 * @param member
	 */
	public static void srem(String key, String member) {
		Jedis client = jedisPool.getResource();
		try {
			client.srem(key, member);
		} finally {
			releaseResource(client);
		}
	}

	/**
	 * 向名称为key的zset中添加元素member，score用于排序。如果该元素已经存在，则根据score更新该元素的顺序。
	 * 
	 * @param key
	 * @param score
	 * @param member
	 */
	public static void zadd(String key, long score, String member) {
		Jedis client = jedisPool.getResource();
		try {
			client.zadd(key, score, member);
		} finally {
			releaseResource(client);
		}
	}

	public static Long zCard(String key) {
		Jedis client = jedisPool.getResource();
		long num =0;
		try {
			num = client.zcard(key);
		} finally {
			releaseResource(client);
		}
		return num;
	}

	/**
	 * 返回集合 key 中的所有成员。不存在的 key 被视为空集合。
	 * 
	 * @param key
	 *            键
	 * @return
	 */
	public static Set<String> smembers(String key) {
		Jedis client = jedisPool.getResource();
		Set<String> set = null;
		try {
			set = client.smembers(key);
			
		} finally {
			releaseResource(client);
		}
		return set;
	}

	/**
	 * 判断 member 元素是否集合 key 的成员。
	 * 
	 * @param key
	 *            键
	 * @param member
	 * @return
	 */
	public static Boolean sIsMember(String key, String member) {
		Jedis client = jedisPool.getResource();
		Boolean bool = false;
		try {
			 bool= client.sismember(key, member);
			
		} finally {
			releaseResource(client);
		}
		return bool;
	}

	/**
	 * 如果在名称为key的zset中已经存在元素member，则该元素的score增加increment；否则向集合中添加该元素，
	 * 其score的值为increment
	 * 
	 * @param key
	 * @param member
	 * @param increment
	 */
	public static void zincrBy(String key, String member, int increment) {
		Jedis client = jedisPool.getResource();
		try {
			client.zincrby(key, increment, member);
		} finally {
			releaseResource(client);
		}
	}

	/**
	 * 返回名称为key的zset（元素已按score从大到小排序）中member元素的rank（即index，从0开始），若没有member元素，返回“
	 * nil
	 * 
	 * @param key
	 * @param member
	 * @return
	 */
	public static long zrevrank(String key, String member) {
		Jedis client = jedisPool.getResource();
		long rank = 0;
		try {
			 rank = client.zrevrank(key, member);
		} finally {
			releaseResource(client);
		}
		return rank + 1l;
	}

	/**
	 * 返回名称为key的zset中元素element的score
	 * 
	 * @param key
	 * @param memebr
	 * @return
	 */
	public static long zscore(String key, String memebr) {
		Jedis client = jedisPool.getResource();
		long score = 0;
		try {
			score = Math.round(client.zscore(key, memebr));
		} finally {
			releaseResource(client);
		}

		return score;
	}

	public static Set<Tuple> zrevrangeWithScores(String key, long start, long stop) {
		Jedis client = jedisPool.getResource();
		Set<Tuple> set = null;
		try {
			set = client.zrevrangeWithScores(key, --start, --stop);
			System.out.println("获取从" + start + "到" + stop + "排名的玩家,chaxun dao gongyou " + set.size());
		} finally {
			releaseResource(client);
		}
		return set;
	}

	/**
	 * 回名称为key的zset中score >= min且score <= max的所有元素
	 * 
	 * @param key
	 * @param start
	 * @param stop
	 * @return
	 */
	public static Set<String> zrangeByScore(String key, int min, int max) {
		Jedis client = jedisPool.getResource();
		Set<String> set = null;
		try {
			set = client.zrangeByScore(key, min, max);
		} finally {
			releaseResource(client);
		}
		return set;
	}

	/**
	 * 删除名称为key的zset中的元素member
	 * 
	 * @param key
	 * @param member
	 */
	public static void zrem(String key, String member) {
		Jedis client = jedisPool.getResource();
		try {
			client.zrem(key, member);
		} finally {
			releaseResource(client);
		}
	}

	/**
	 * 获取min-max范围内的随机一个member
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public static String zrandom(String key, int min, int max) {
		Jedis client = jedisPool.getResource();
		String member = null;
		try {
			Transaction trans = client.multi();
			Response<Set<String>> response = trans.zrangeByScore(key, min, max);
			Set<String> members = response.get();
			if (!members.isEmpty()) {
				member = new ArrayList<String>(members).get(new Random().nextInt(members.size()));
				trans.zrem(key, member);
			}
			trans.exec();
		} finally {
			releaseResource(client);
		}
		return member;
	}
}
