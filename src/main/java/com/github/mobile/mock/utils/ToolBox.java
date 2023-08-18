package com.github.mobile.mock.utils;

import java.text.SimpleDateFormat;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;

public class ToolBox {

	/**
	 * 获取当前时间的字串
	 * 
	 * @return
	 */
	public static String getCurrentStringDateTime() {

		long t = System.currentTimeMillis();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return df.format(t);
	}

	public static String uuid() {
		return UUID.randomUUID().toString().replace("-", "").toUpperCase();
	}

	public static boolean checkUrl(HttpRequest httpRequest, String regex) {
		String host = httpRequest.headers().get(HttpHeaderNames.HOST);
		if (host != null && regex != null) {
			String url;
			if (httpRequest.uri().indexOf("/") == 0) {
				if (httpRequest.uri().length() > 1) {
					url = host + httpRequest.uri();
				} else {
					url = host;
				}
			} else {
				url = httpRequest.uri();
			}
			//System.out.println("最终待匹配的URL为：" + url);
			//System.out.println("调用的正则匹配结果为：" + url.matches(regex));
			return url.matches(regex);
		}
		return false;
	}
	
	/**
	 * 使用正则表达式匹配json中的key值后，将key全部变成小写
	 * @param json
	 * @return
	 */
	public static String keysToLowCase(String json){
		
		String regex = "\".*?(:)";
		Pattern pattern = Pattern.compile(regex);
		StringBuffer sb = new StringBuffer();
		Matcher m = pattern.matcher(json);
		while (m.find()) {
			//System.out.println("find");
            m.appendReplacement(sb, m.group().toLowerCase());
        }
		
		m.appendTail(sb);
        return sb.toString();        
	}
	
	/**
	 * 对原始的响应进行预处理，将value中的多个英文逗号替换成中文逗号
	 * 方便后续的替换
	 * @param resp
	 * @return
	 */
     public static String preHandle(String resp){
    	String dest = new String(resp);
 		String p = "\"([^\"]*)\"" ;
 		Pattern P=Pattern.compile(p);
 	    Matcher matcher = P.matcher(resp); 
 	   while(matcher.find())
	    {
 		   //获取原始的匹配串
	    	String tmp = matcher.group(0).replaceAll(p, "$1");
	    	if(tmp.contains(",") && !tmp.contains("\\")){	
	    		//获取修改后的串并将其替换
	    		String tmp2 = tmp.replace(",", "，");
	    		dest = dest.replaceAll(tmp, tmp2);    		
	    	}
	    }
    	 return dest;
     }
     
     public static String preHandle(String resp,String replace){
    	String dest = new String(resp);
 		String p = "\"([^\"]*)\"" ;
 		Pattern P=Pattern.compile(p);
 	    Matcher matcher = P.matcher(resp); 
 	   while(matcher.find())
	    {
 		   //获取原始的匹配串
	    	String tmp = matcher.group(0).replaceAll(p, "$1");
	    	if(tmp.contains(",") && !tmp.contains("\\")){	
	    		//获取修改后的串并将其替换
	    		String tmp2 = tmp.replace(",", replace);
	    		dest = dest.replaceAll(tmp, tmp2);    		
	    	}
	    }
    	 return dest;
     }
}
