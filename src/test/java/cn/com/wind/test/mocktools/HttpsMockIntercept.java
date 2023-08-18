package cn.com.wind.test.mocktools;

import com.github.monkeywie.proxyee.intercept.HttpProxyInterceptPipeline;
import com.github.monkeywie.proxyee.intercept.common.FullResponseIntercept;
import com.github.monkeywie.proxyee.util.HttpUtil;

import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

public class HttpsMockIntercept extends FullResponseIntercept{
	
	private String url;
	private String resp;
	
	public HttpsMockIntercept(String url,String resp) {
		this.url = url;
		this.resp = resp;
	}

	@Override
	public void handelResponse(HttpRequest httpRequest, FullHttpResponse httpResponse, HttpProxyInterceptPipeline pipeline) {
        httpResponse.content().clear();               
        httpResponse.content().writeBytes(resp.getBytes());		
	}

	@Override
	public boolean match(HttpRequest httpRequest, HttpResponse httpResponse, HttpProxyInterceptPipeline pipeline) {
		System.out.println("(.*)" + url + "(.*)");
		return HttpUtil.checkUrl(pipeline.getHttpRequest(), "(.*)" + url + "(.*)");
	}
	
	public void set(String url,String resp){
		this.url = url;
		this.resp = resp;
	}
	
}
