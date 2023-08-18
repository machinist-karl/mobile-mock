package cn.com.wind.newserver;

import com.github.monkeywie.proxyee.exception.HttpProxyExceptionHandle;
import com.github.monkeywie.proxyee.handler.HttpProxyServerHandle;
import com.github.monkeywie.proxyee.intercept.HttpProxyInterceptInitializer;
import com.github.monkeywie.proxyee.proxy.ProxyConfig;
import com.github.monkeywie.proxyee.server.HttpProxyServerConfig;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class MockChannelInitializer extends ChannelInitializer<Channel> {
	private HttpProxyServerConfig serverConfig;
	private HttpProxyInterceptInitializer proxyInterceptInitializer;
	private ProxyConfig proxyConfig;
	private HttpProxyExceptionHandle httpProxyExceptionHandle;
	
	public MockChannelInitializer(HttpProxyServerConfig serverConfig,
			HttpProxyInterceptInitializer proxyInterceptInitializer, ProxyConfig proxyConfig,
			HttpProxyExceptionHandle httpProxyExceptionHandle) {
		super();
		this.serverConfig = serverConfig;
		this.proxyInterceptInitializer = proxyInterceptInitializer;
		this.proxyConfig = proxyConfig;
		this.httpProxyExceptionHandle = httpProxyExceptionHandle;
	}
	
	@Override
	protected void initChannel(Channel ch) throws Exception {
		ch.pipeline().addLast("httpCodec", new HttpServerCodec());
		ch.pipeline().addLast("serverHandle", new HttpProxyServerHandle(serverConfig,
				proxyInterceptInitializer, proxyConfig, httpProxyExceptionHandle));		
	}



	
}
