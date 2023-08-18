package com.github.mobile.mock.boot;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.concurrent.LinkedBlockingQueue;

import com.github.mobile.mock.domain.HttpCaseInfo;
import com.github.monkeywie.proxyee.crt.CertPool;
import com.github.monkeywie.proxyee.crt.CertUtil;
import com.github.monkeywie.proxyee.exception.HttpProxyExceptionHandle;
import com.github.monkeywie.proxyee.intercept.HttpProxyInterceptInitializer;
import com.github.monkeywie.proxyee.proxy.ProxyConfig;
import com.github.monkeywie.proxyee.server.HttpProxyCACertFactory;
import com.github.monkeywie.proxyee.server.HttpProxyServer;
import com.github.monkeywie.proxyee.server.HttpProxyServerConfig;

import com.github.mobile.mock.SSLService.MockChannelInitializer;
import com.github.mobile.mock.httpservice.HttpCaseInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

public class MockServices extends HttpProxyServer {
	public final static HttpResponseStatus SUCCESS = new HttpResponseStatus(200, "Connection established");

	private HttpProxyCACertFactory caCertFactory;
	private HttpProxyServerConfig serverConfig;
	private HttpProxyInterceptInitializer proxyInterceptInitializer;
	private HttpProxyExceptionHandle httpProxyExceptionHandle;
	private ProxyConfig proxyConfig;

	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	
	private LinkedBlockingQueue<HttpCaseInfo> queue;

	// SSL Server的端口
	private final int SSL_SERVER_PORT = 12306;

	// Http Server的端口
	private final int HTTP_SERVER_PORT = 10086;
	
	
	public MockServices(LinkedBlockingQueue<HttpCaseInfo> lbq) {
		this.queue = lbq;
	}

	private void init() {
		if (serverConfig == null) {
			serverConfig = new HttpProxyServerConfig();
		}
		serverConfig.setProxyLoopGroup(new NioEventLoopGroup(serverConfig.getProxyGroupThreads()));

		if (serverConfig.isHandleSsl()) {
			try {
				serverConfig.setClientSslCtx(
						SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build());
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				X509Certificate caCert;
				PrivateKey caPriKey;
				if (caCertFactory == null) {
					caCert = CertUtil.loadCert(classLoader.getResourceAsStream("ca.crt"));
					caPriKey = CertUtil.loadPriKey(classLoader.getResourceAsStream("ca_private.der"));
				} else {
					caCert = caCertFactory.getCACert();
					caPriKey = caCertFactory.getCAPriKey();
				}
				// 璇诲彇CA璇佷功浣跨敤鑰呬俊鎭�
				serverConfig.setIssuer(CertUtil.getSubject(caCert));
				// 璇诲彇CA璇佷功鏈夋晥鏃舵(server璇佷功鏈夋晥鏈熻秴鍑篊A璇佷功鐨勶紝鍦ㄦ墜鏈轰笂浼氭彁绀鸿瘉涔︿笉瀹夊叏)
				serverConfig.setCaNotBefore(caCert.getNotBefore());
				serverConfig.setCaNotAfter(caCert.getNotAfter());
				// CA绉侀挜鐢ㄤ簬缁欏姩鎬佺敓鎴愮殑缃戠珯SSL璇佷功绛捐瘉
				serverConfig.setCaPriKey(caPriKey);
				// 鐢熶骇涓�瀵归殢鏈哄叕绉侀挜鐢ㄤ簬缃戠珯SSL璇佷功鍔ㄦ�佸垱寤�
				KeyPair keyPair = CertUtil.genKeyPair();
				serverConfig.setServerPriKey(keyPair.getPrivate());
				serverConfig.setServerPubKey(keyPair.getPublic());
			} catch (Exception e) {
				serverConfig.setHandleSsl(false);
			}
		}
		if (proxyInterceptInitializer == null) {
			proxyInterceptInitializer = new HttpProxyInterceptInitializer();
		}
		if (httpProxyExceptionHandle == null) {
			httpProxyExceptionHandle = new HttpProxyExceptionHandle();
		}
	}

	public HttpProxyServer serverConfig(HttpProxyServerConfig serverConfig) {
		this.serverConfig = serverConfig;
		return this;
	}

	public HttpProxyServer proxyInterceptInitializer(HttpProxyInterceptInitializer proxyInterceptInitializer) {
		this.proxyInterceptInitializer = proxyInterceptInitializer;
		return this;
	}

	public HttpProxyServer httpProxyExceptionHandle(HttpProxyExceptionHandle httpProxyExceptionHandle) {
		this.httpProxyExceptionHandle = httpProxyExceptionHandle;
		return this;
	}

	public HttpProxyServer proxyConfig(ProxyConfig proxyConfig) {
		this.proxyConfig = proxyConfig;
		return this;
	}

	public HttpProxyServer caCertFactory(HttpProxyCACertFactory caCertFactory) {
		this.caCertFactory = caCertFactory;
		return this;
	}

	public void start() {
		init();
		bossGroup = new NioEventLoopGroup(serverConfig.getBossGroupThreads());
		workerGroup = new NioEventLoopGroup(serverConfig.getWorkerGroupThreads());

		ServerBootstrap SSLServer = null;
		ServerBootstrap HttpServer = null;
		ChannelFuture SSLCh = null;
		ChannelFuture HttpCh = null;

		try {
			// 创建SSL的服务
			SSLServer = new ServerBootstrap();
			MockChannelInitializer channel = new MockChannelInitializer(serverConfig, proxyInterceptInitializer,
					proxyConfig, httpProxyExceptionHandle);
			SSLServer.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.DEBUG)).childHandler(channel);

			SSLCh = SSLServer.bind(SSL_SERVER_PORT).sync();
			
			//创建Http服务
			HttpServer = new ServerBootstrap();
			HttpServer.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					  .childHandler(new HttpCaseInitializer(queue));
			HttpCh = HttpServer.bind(HTTP_SERVER_PORT).sync();	
			
			SSLCh.channel().closeFuture().sync();
			HttpCh.channel().closeFuture().sync();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(SSLCh != null){
				try {
					SSLCh.sync().channel().closeFuture().sync();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			if(HttpCh != null){
				try {
					HttpCh.sync().channel().closeFuture().sync();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public void close() {
		serverConfig.getProxyLoopGroup().shutdownGracefully();
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
		CertPool.clear();
	}
}
