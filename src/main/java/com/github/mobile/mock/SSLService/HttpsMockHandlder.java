package com.github.mobile.mock.SSLService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import com.github.mobile.mock.domain.HttpCaseInfo;
import com.github.mobile.mock.utils.ToolBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.monkeywie.proxyee.intercept.HttpProxyInterceptPipeline;
import com.github.monkeywie.proxyee.intercept.common.FullResponseIntercept;

import com.github.mobile.mock.dbservice.HttpCaseDBService;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.CharsetUtil;

public class HttpsMockHandlder extends FullResponseIntercept {
	private LinkedBlockingQueue<HttpCaseInfo> queue;
	private String reqCmdCode = "CMDCODE_IN_REQUEST_NOT_EXIST";
	private HttpCaseInfo info;
	private final String replace = "★★★★★";
	private static final String FLAG = "ONLY_MATCH_URL";
	private static Logger log = LogManager.getLogger();

	public HttpsMockHandlder(LinkedBlockingQueue<HttpCaseInfo> lbq) {
		queue = lbq;
	}

	@Override
	public void beforeRequest(Channel clientChannel, HttpContent httpContent, HttpProxyInterceptPipeline pipeline)
			throws Exception {

		if (HttpMethod.POST == pipeline.getHttpRequest().method()) {

			HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(pipeline.getHttpRequest());
			decoder.offer(httpContent.copy());

			List<InterfaceHttpData> parmList = decoder.getBodyHttpDatas();
			Map<String, String> params = new HashMap<>();

			for (InterfaceHttpData parm : parmList) {

				Attribute data = (Attribute) parm;
				params.put(data.getName(), data.getValue());
			}
			
			String tCode = params.get("cmdCode");
			
			if(tCode != null){
				reqCmdCode = tCode;
			}
			
		}
		log.info("reqCmdCode = "+ reqCmdCode);
		super.beforeRequest(clientChannel, httpContent, pipeline);
	}

	@Override
	public void handelResponse(HttpRequest httpRequest, FullHttpResponse httpResponse,
			HttpProxyInterceptPipeline pipeline) {

		// 获取App Server原始的响应报文
		String original = httpResponse.content().toString(CharsetUtil.UTF_8);
		log.info("App Server原始的响应报文为：" + original);
		
		//对原始的响应报文做预处理，将value中的英文逗号全部替换
		String dest = ToolBox.preHandle(original,replace);

		HttpsMockData data = new HttpsMockData(info,dest);
		//String dest = data.getMockString(null);
		dest = data.getMockString();
		if(dest.contains(replace)){
			dest = dest.replace(replace, ",");
		}
		log.info("用例" + info.getCasename() + "被Mock后的报文为："  + dest);

		httpResponse.content().clear();
		httpResponse.content().writeBytes(dest.getBytes());

		// 将结果更新到数据库
		HttpCaseDBService his = new HttpCaseDBService(info);
		his.handle();
	}

	@Override
	public boolean match(HttpRequest httpRequest, HttpResponse httpResponse, HttpProxyInterceptPipeline pipeline) {

		if (queue.isEmpty()) {
			log.debug("队列为空，跳过Mock规则匹配");
			return false;
		}

		HttpRequest req = pipeline.getHttpRequest();
		String reqURL = req.uri();
		
		for (HttpCaseInfo tmp : queue) {
			String queURL = tmp.getUrl();
			String code = tmp.getCmdcode();
			String url = "(.*)" + queURL + "(.*)";
						
			if(ToolBox.checkUrl(req, url)){
				if(code.toUpperCase().equals(FLAG) || code.equals(reqCmdCode)){
					this.info = tmp;
					queue.remove(info);					
					log.info("App Request = " + reqURL + "||" + "Cuttent Case info = "
							+ this.info.toString() + "||" + "match result=" + true);					
					return true;
				}
			}
		}
		
		return false;
	}		
}
