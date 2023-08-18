package cn.com.wind.test.backup;


import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.monkeywie.proxyee.server.HttpProxyServer;
import com.github.monkeywie.proxyee.server.HttpProxyServerConfig;

import com.github.mobile.mock.SSLService.HttpsMockHandlder;
import com.github.mobile.mock.SSLService.HttpsMockInitializer;
import com.github.mobile.mock.domain.HttpCaseInfo;



public class Booter {

	// Mock Server的端口
	private  static int MOCK_SERVER_PORT = 12306;
	
	// Http Server的端口
	private  static int HTTP_SERVER_PORT = 10086;
	
	private static Logger log = LogManager.getLogger();

	public static void main(String[] args) {
		
		log.info("---------------------------系统开始初始化.....................");		
		// 创建Mock Case队列
		LinkedBlockingQueue<HttpCaseInfo> queue = new LinkedBlockingQueue<>();
		log.info("----------------初始化用例处理队列成功--------------");

		/**
		 * 启动Http处理进程,用于接口消息处理
		 */	      	      
	     HttpServer server = new HttpServer(HTTP_SERVER_PORT,queue);
	     server.launch();
		 log.info("----------------Http进程启动完毕--------------");		

		/**
		 * 启动Mock Server
		 */
		log.info("----------------开始创建Mock Server.......................");
		HttpsMockHandlder handler = new HttpsMockHandlder(queue);
		HttpProxyServerConfig config = new HttpProxyServerConfig();
		config.setHandleSsl(true);

		HttpProxyServer sslserver = new HttpProxyServer();
		sslserver.serverConfig(config).proxyInterceptInitializer(new HttpsMockInitializer(handler));
		sslserver.start(MOCK_SERVER_PORT);
		log.info("----------------Mock Server启动完毕.......................");
		log.info("---------------------------系统开始初始完毕------------------------");
	}
}
