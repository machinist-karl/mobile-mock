package com.github.mobile.mock.SSLService;

import com.github.monkeywie.proxyee.intercept.HttpProxyInterceptInitializer;
import com.github.monkeywie.proxyee.intercept.HttpProxyInterceptPipeline;

public class HttpsMockInitializer extends HttpProxyInterceptInitializer{
	private HttpsMockHandlder httpsResponse;
	
	public HttpsMockInitializer(HttpsMockHandlder resp){
		httpsResponse = resp;
	}
		
	@Override
	public void init(HttpProxyInterceptPipeline pipeline) {
		pipeline.addLast(httpsResponse);
	}
}
