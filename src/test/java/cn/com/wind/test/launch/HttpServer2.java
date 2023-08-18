package cn.com.wind.test.launch;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mobile.mock.domain.HttpCaseInfo;
import com.github.mobile.mock.httpservice.HttpCaseInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HttpServer2 {
	private int port;
	private LinkedBlockingQueue<HttpCaseInfo> queue;
	private static Logger log = LogManager.getLogger();

	public HttpServer2(int port, LinkedBlockingQueue<HttpCaseInfo> queue) {
		super();
		this.port = port;
		this.queue = queue;
	}

	/**
	 * 启动Http服务
	 */
	public void launch(){
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		ChannelFuture channelFuture = null;
		
		try{
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		
        serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
        .childHandler(new HttpCaseInitializer(queue));
        
        channelFuture = serverBootstrap.bind(port);
          
		} catch (Exception e){
			log.debug("Http Server启动异常，正在关闭.......");
			throw new RuntimeException("Http Server启动失败，Mock服务器停止");
		}
		finally {
			if(channelFuture != null){
				try {
					channelFuture.sync().channel().closeFuture().sync();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }				
	}
}
