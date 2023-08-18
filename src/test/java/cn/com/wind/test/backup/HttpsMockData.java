package cn.com.wind.test.backup;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.github.mobile.mock.domain.HttpCaseInfo;

public class HttpsMockData {

	private HttpCaseInfo info;
	private String original;
	private String operation;
	private List<String> keys;
	//private String[] replacement;
	private String method;
	private Map<String,Object> kv;
	private static Logger log = LogManager.getLogger();

	public HttpsMockData(HttpCaseInfo info, String original) {
		this.info = info;
		this.original = original;
		init();
	}

	public void init() {
		method = info.getMethod();
		String msg = info.getRespmsg();
/*		if (method.equals("add")) {
			replacement = new String[] { msg };
			return;
		}*/
		operation = info.getOperation();

		if (method.equals("delete")) {
			keys = getDeleteKeys(msg);
			return;
		}

		//replacement = msg.substring(1, msg.length() - 1).split(",");
		keys = getUpdateKeys(msg);
	}

	public String getMockString() {
		log.info("当前使用的Method值为：" + method);

		if (method.equals("update")) {
			return update();
		}

		if (method.equals("add")) {
			return add();
		}

		if (method.equals("delete")) {
			return delete();
		}

		return null;
	}
	
	public String getMockString(String methodName) {
		String result = null;
		Class<?> clazz = this.getClass();
		Method m = null;
		try {
			 m = clazz.getMethod(method);	
			 result = (String) m.invoke(this);
			 
		} catch (Exception e) {
			log.debug("未找到方法:" + info.getMethod());
		} 
				
		return result;
	}

	public String add() {
		return info.getRespmsg();
	}

	public String delete() {
		String dest = original;
		int index;
		String sub;

		if (!operation.equals("1")) {
			for (int i = 0; i < keys.size(); i++) {
				String key = keys.get(i);
				if(!dest.contains(key)){
					log.info("未在服务器应答报文中找到"+ key + "字段，因此无法删除！");
					continue;
				}
				String[] arr = getRegexData(key);
				
				
				index = dest.indexOf(arr[2]);
				sub = dest.substring(index - 1, index);
				// 如果该替换字符串为非首个
				if (sub.equals(",")) {
					dest = dest.replaceAll(arr[0], arr[1]).replace("," + arr[2], "");
					continue;
				}
				
				//如果该替换字符串为首个				
				dest = dest.replaceAll(arr[0], arr[1]).replace(arr[2] + ",", "");

			}
			return dest;
		}

		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			if(!dest.contains(key)){
				log.info("未在服务器应答报文中找到"+ key + "字段，因此无法删除！");
				continue;
			}
			String[] arr = getRegexData(key);

			index = dest.indexOf(arr[2]);
			sub = dest.substring(index - 1, index);

			// 如果该替换字符串为非首个
			if (sub.equals(",")) {
				dest = dest.replaceFirst(arr[0], arr[1]).replaceFirst("," + arr[2], "");
				continue;
			}
			// 如果该替换字符串位处于首个
			dest = dest.replaceFirst(arr[0], arr[1]).replaceFirst(arr[2] + ",", "");
		}

		return dest;
	}

	public String update() {
		String dest = original;

		if (!operation.equals("1")) {
			for (int i = 0; i < keys.size(); i++) {
				String key = keys.get(i);
				if(!dest.contains(key)){
					log.info("未在服务器应答报文中找到"+ key + "字段，因此无法替换！");
					continue;
				}
				String[] arr = getRegexData(key);
				//dest = dest.replaceAll(arr[0], arr[1]).replace(arr[2], replacement[i]);
				String ds = "\"" + key + "\":" + kv.get(key);
				dest = dest.replaceAll(arr[0], arr[1]).replace(arr[2], ds);
			}
			return dest;
		}

		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			if(!dest.contains(key)){
				log.info("未在服务器应答报文中找到"+ key + "字段，因此无法替换！");
				continue;
			}
			String[] arr = getRegexData(key);
			String ds = "\"" + key + "\":" + kv.get(key);
			dest = dest.replaceFirst(arr[0], arr[1]).replaceFirst(arr[2], ds);
			//dest = dest.replaceFirst(arr[0], arr[1]).replaceFirst(arr[2], replacement[i]);
		}

		return dest;
	}

	public String[] getRegexData(String key) {
		String base = "\"" + key;
		String[] arr = new String[] { base + ".*?([,}$])", base + "\":$1", base + "\":" };
		//log.info("base = " + base);

		return arr;
	}

	public List<String> getUpdateKeys(String json) {

		List<String> keys = new ArrayList<>();
		JsonParser parser = new JsonParser();
		JsonObject jsonObj = parser.parse(json).getAsJsonObject();
		Iterator<?> i$ = jsonObj.entrySet().iterator();
		kv = new HashMap<>();
		while (i$.hasNext()) {
			Map.Entry<?, ?> entry = (Map.Entry<?, ?>) i$.next();
			String k = (String) entry.getKey();
			keys.add(k);
			Object v = entry.getValue();
			kv.put(k, v);
		}

		return keys;
	}

	public List<String> getDeleteKeys(String json) {		
		//去除字符串中的引号
		 String tmp = json.replace("\"", "");
		 log.info("tmp去除双引号：" + tmp);
		 
		 if(!tmp.startsWith("{" ) && !tmp.endsWith("}")){
			 log.info("做delte操作时，RespMsg值的格式不符合要求，未包含{}");
			 return null;
		 }
		
		//去除{}
		tmp = tmp.substring(1, tmp.length() - 1);
		log.info("tmp去除{}：" + tmp);

		List<String> keys = new ArrayList<>();
		//{}中仅包含一个元素
		if(!json.contains(",")){
			if(!original.contains(tmp)){
				log.info("所传的key：" + tmp + "不存在，无法进行替换操作");
				return null;
			}
			keys.add(tmp);
			return keys;
		}
		
		//包含逗号
		return Arrays.asList(tmp.split(","));
	}
	
	/**
	 * 如果是delete操作，将HttpCaseInfo
	 * @param single
	 */
	public void delRespMsgQuotation(HttpCaseInfo single){
		String tmp = single.getRespmsg();
		tmp = tmp.replaceAll("'", "").replace("\"", "");
		single.setRespmsg(tmp);
		log.info("修改后的respmsg值为：" + tmp);
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
}
