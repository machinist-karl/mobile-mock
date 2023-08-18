package cn.com.wind.test.mocktools;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import com.github.monkeywie.proxyee.intercept.HttpProxyInterceptPipeline;
import com.github.monkeywie.proxyee.intercept.common.FullResponseIntercept;
import com.github.monkeywie.proxyee.util.HttpUtil;

import com.github.mobile.mock.domain.HttpCaseInfo;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

public class HttpsMockHandlder extends FullResponseIntercept {
	private LinkedBlockingQueue<HttpCaseInfo> queue;
	private Map<String, String> mapping;
	private HttpCaseInfo info;


	public HttpsMockHandlder(LinkedBlockingQueue<HttpCaseInfo> lbq, Map<String, String> table) {
		queue = lbq;
		mapping = table;
	}

	@Override
	public void handelResponse(HttpRequest httpRequest, FullHttpResponse httpResponse,
			HttpProxyInterceptPipeline pipeline) {

		String resp = info.getRespmsg();
		httpResponse.content().clear();
		httpResponse.content().writeBytes(resp.getBytes());
	}

	@Override
	public boolean match(HttpRequest httpRequest, HttpResponse httpResponse, HttpProxyInterceptPipeline pipeline) {
		
		if(queue.isEmpty()){
			return false;
		}

		HttpCaseInfo tmp = queue.element();// 取得Head元素，但不删除
		String url = "(.*)" + mapping.get(tmp.getMatchid()) + "(.*)";
		//url = "(.*)" + url + "(.*)";
		Boolean ismatch = HttpUtil.checkUrl(pipeline.getHttpRequest(), url);
		// 如果匹配，则将info赋值并将节点从队列中去除
		if (ismatch) {
			try {
				info = queue.take();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
}
