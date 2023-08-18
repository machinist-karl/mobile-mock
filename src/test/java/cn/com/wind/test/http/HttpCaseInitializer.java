package cn.com.wind.test.http;

import java.util.concurrent.LinkedBlockingQueue;

import com.github.mobile.mock.domain.HttpCaseInfo;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpCaseInitializer extends ChannelInitializer<SocketChannel> {
	
	private LinkedBlockingQueue<HttpCaseInfo> queue;
	
	public HttpCaseInitializer(LinkedBlockingQueue<HttpCaseInfo> queue){
		super();
		this.queue = queue;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("HttpServerCodec",new HttpServerCodec());
		pipeline.addLast("aggregator", new HttpObjectAggregator(10*1024*1024));
		pipeline.addLast("testHttpServerHandler()",new HttpCaseHandler(queue));
		
	}

}
