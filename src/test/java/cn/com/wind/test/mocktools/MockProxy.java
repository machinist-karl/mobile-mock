package cn.com.wind.test.mocktools;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.github.monkeywie.proxyee.server.HttpProxyServer;
import com.github.monkeywie.proxyee.server.HttpProxyServerConfig;

public class MockProxy {
	public static void main(String[] args) {
		HttpProxyServerConfig config =  new HttpProxyServerConfig();
		config.setHandleSsl(true);
		
		String url = "service/app/expoHttp.htm";
        String resp = null;
        //File file = new File("d:\\respString.txt");
        File file = new File("d:\\resp2.txt");
        try {
			resp = FileUtils.readFileToString(file, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		} 
		HttpsMockIntercept intercept = new HttpsMockIntercept(url,resp);
				
		HttpProxyServer server = new HttpProxyServer();
		server.serverConfig(config).proxyInterceptInitializer(new HttpsMockInitializer(intercept));
		server.start(12306);
	}
}
