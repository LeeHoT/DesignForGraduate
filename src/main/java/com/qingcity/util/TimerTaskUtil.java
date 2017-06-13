package com.qingcity.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 全局定时任务工具类
 *
 * @author leehot 2015-2-6
 */
public class TimerTaskUtil {

	private static Logger logger = LoggerFactory.getLogger(TimerTaskUtil.class);

	/**
	 * 定时器设置
	 */
	private final static ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();

	private static TimerTaskUtil instance = new TimerTaskUtil();

	public static TimerTaskUtil getInstance() {
		return instance;
	}

	/**
	 * 运行定时器
	 *
	 * @param task
	 * @param delay
	 * @param unit
	 */
	public void run(final Runnable task, long delay, TimeUnit unit) {
		try {
			timer.scheduleAtFixedRate(task, 0, delay, unit);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * 关闭定时器
	 */
	public static void stop() {
		timer.shutdown();
	}
}