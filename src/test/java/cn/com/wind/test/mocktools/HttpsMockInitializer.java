package cn.com.wind.test.mocktools;

import com.github.monkeywie.proxyee.intercept.HttpProxyInterceptInitializer;
import com.github.monkeywie.proxyee.intercept.HttpProxyInterceptPipeline;

public class HttpsMockInitializer extends HttpProxyInterceptInitializer{
	private HttpsMockIntercept httpsResponse;
	
	public HttpsMockInitializer(HttpsMockIntercept resp){
		httpsResponse = resp;
	}
		
	@Override
	public void init(HttpProxyInterceptPipeline pipeline) {
		pipeline.addLast(httpsResponse);
	}
}
