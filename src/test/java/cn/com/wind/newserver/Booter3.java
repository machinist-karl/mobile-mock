package cn.com.wind.newserver;


import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.monkeywie.proxyee.server.HttpProxyServerConfig;

import com.github.mobile.mock.SSLService.HttpsMockHandlder;
import com.github.mobile.mock.SSLService.HttpsMockInitializer;
import com.github.mobile.mock.domain.HttpCaseInfo;


public class Booter3 {

	private static Logger log = LogManager.getLogger();

	public static void main(String[] args) {
		
		// 创建Mock Case队列
		LinkedBlockingQueue<HttpCaseInfo> queue = new LinkedBlockingQueue<>();

		/**
		 * 启动Mock Server
		 */
		log.info("----------------开始创建Mock Service.......................");
		HttpsMockHandlder handler = new HttpsMockHandlder(queue);
		HttpProxyServerConfig config = new HttpProxyServerConfig();
		config.setHandleSsl(true);

		MockServices sslserver = new MockServices(queue);
		sslserver.serverConfig(config).proxyInterceptInitializer(new HttpsMockInitializer(handler));
		sslserver.start();
	}
}
