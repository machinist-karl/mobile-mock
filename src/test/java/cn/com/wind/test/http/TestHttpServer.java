package cn.com.wind.test.http;

import java.util.concurrent.LinkedBlockingQueue;

import com.github.mobile.mock.domain.HttpCaseInfo;

public class TestHttpServer {
	
	public static void main(String[] args) {
      int port = 10086;
      LinkedBlockingQueue<HttpCaseInfo> queue = new LinkedBlockingQueue();
      
      HttpServer server = new HttpServer(10086,queue);
      server.launch();
	}
}
