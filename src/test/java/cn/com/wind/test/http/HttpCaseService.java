package cn.com.wind.test.http;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import com.github.mobile.mock.domain.HttpCaseInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

public class HttpCaseService {
	
	private ChannelHandlerContext ctx;
	private FullHttpRequest msg;
	private Map<String,String> params;

	
	private static Logger log = LogManager.getLogger();
	
	public HttpCaseService(ChannelHandlerContext ctx, FullHttpRequest msg) {
		this.ctx = ctx;
		this.msg = msg;
	}
	
	/**
	 * 获取请求参数列表
	 */
	public void parse(){
		HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(msg);
		decoder.offer(msg);
		
		params = new HashMap<>();
		List<InterfaceHttpData> list = decoder.getBodyHttpDatas();
		
		for(InterfaceHttpData t : list){
			Attribute data = (Attribute) t;
			try {
				params.put(data.getName().toLowerCase().trim(), data.getValue());	
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
	
	public HttpCaseInfo getCase(){
		String basic = params.get("basic");
		Gson gson = new Gson();
		basic = basic.toLowerCase();
		HttpCaseInfo single = gson.fromJson(basic, HttpCaseInfo.class);
		single.setMethod(params.get("method"));
		single.setOperation(params.get("operation"));
		String data = params.get("replacement");
		single.setRespmsg(data);
		single.setRespMD5(DigestUtils.md5Hex(data));
		
		return single;
	}
	
	public static HttpCaseInfo getCase(Map<String,String> params){
		String basic = params.get("basic");
		Gson gson = new Gson();
		basic = basic.toLowerCase();
		HttpCaseInfo single = gson.fromJson(basic, HttpCaseInfo.class);
		single.setMethod(params.get("method"));
		single.setOperation(params.get("operation"));
		String data = params.get("replacement");
		single.setRespmsg(data);
		single.setRespMD5(DigestUtils.md5Hex(data));
		
		return single;
	}
	
	public static String checkHttpCase(Object httpcase){		
		StringBuilder sb = new StringBuilder();
		for(Field f : httpcase.getClass().getDeclaredFields()){
			try {
				f.setAccessible(true);
				String t = (String)f.get(httpcase);
				if(t == null){
					String name = f.getName();
					sb.append("请求参数\"").append(name).append("\"的值为空，请检查请求参数");
					break;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
}
