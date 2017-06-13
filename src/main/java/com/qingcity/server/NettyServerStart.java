package com.qingcity.server;

import java.io.File;

import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.qingcity.constants.PortConstant;
import com.qingcity.netty.ServerInitializer;

/**
 * NettyServer 启动程序，加载log4j和spring 配置文件,设定服务器端口,初始化Initializer
 * 
 * @author 李慧婷
 *
 */
public class NettyServerStart {

	private static final Logger logger = LoggerFactory.getLogger(NettyServerStart.class);
	private static int port;

	public static ApplicationContext factory;

	public static void main(String[] args) throws Exception {

		
		String u = new NettyServerStart().getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		String filename = new File(u).getParent() + "/conf/config/log4j.xml";
		DOMConfigurator.configureAndWatch(filename);
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = PortConstant.GAME_SERVER;
		}
		run();
	}

	private static void run() throws Exception {
		logger.info("load the spring config file");
		factory = new ClassPathXmlApplicationContext("classpath:config/applicationContext.xml");
		ServerInitializer initializer = (ServerInitializer) factory.getBean(ServerInitializer.class);
		// 设定Server 监听端口
		NettyServer server = new NettyServer(port);
		// 设定 initializer
		server.setInitializer(initializer);
		// 加载Pipeline 解码器 NettyMsgDecoder 编码器NettyMsgEncoder
		logger.info("GameServer Starting...");
		server.run();
	}
}