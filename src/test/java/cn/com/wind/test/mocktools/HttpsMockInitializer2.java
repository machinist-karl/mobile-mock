package cn.com.wind.test.mocktools;

import com.github.monkeywie.proxyee.intercept.HttpProxyInterceptInitializer;
import com.github.monkeywie.proxyee.intercept.HttpProxyInterceptPipeline;

public class HttpsMockInitializer2 extends HttpProxyInterceptInitializer{
	private HttpsMockHandlder httpsResponse;
	
	public HttpsMockInitializer2(HttpsMockHandlder resp){
		httpsResponse = resp;
	}
		
	@Override
	public void init(HttpProxyInterceptPipeline pipeline) {
		pipeline.addLast(httpsResponse);
	}
}
