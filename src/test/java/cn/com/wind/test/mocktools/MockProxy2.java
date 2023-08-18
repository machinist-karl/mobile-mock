package cn.com.wind.test.mocktools;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.io.FileUtils;

import com.github.monkeywie.proxyee.server.HttpProxyServer;
import com.github.monkeywie.proxyee.server.HttpProxyServerConfig;

import com.github.mobile.mock.dao.HttpCaseInfoDao;
import com.github.mobile.mock.dbservice.HttpCaseDBService;
import com.github.mobile.mock.domain.HttpCaseInfo;
import com.github.mobile.mock.utils.ToolBox;

public class MockProxy2 {
	public static void main(String[] args) throws Exception {
		LinkedBlockingQueue<HttpCaseInfo> queue = new LinkedBlockingQueue<HttpCaseInfo>();
		
		//将case1放入队列中
		HttpCaseInfo case1 = getCase();
		String cont = getContent("D:\\resp1.txt");
		case1.setRespmsg(cont);
		case1.setMatchid("2001");
		queue.put(case1);
		
		//将case2放入队列中
		HttpCaseInfo case2 = newCase();
		cont = getContent("D:\\resp2.txt");
		case2.setRespmsg(cont);
		case2.setMatchid("2001");
		//queue.put(case2);
		
		System.out.println("queue Size = " + queue.size());
		
		Map<String,String> mapping = new HashMap<>();
		mapping.put("2001", "service/app/expoHttp.htm");
		mapping.put("3001", "jfire/app/appRequest.do");
		
		HttpsMockHandlder handler = new HttpsMockHandlder(queue,mapping);
		
		HttpProxyServerConfig config =  new HttpProxyServerConfig();
		config.setHandleSsl(true);
		
		HttpProxyServer server = new HttpProxyServer();
		server.serverConfig(config).proxyInterceptInitializer(new HttpsMockInitializer2(handler));
		server.start(12306);

	}
	
	public static String getContent(String path){
		File file = new File(path);
		String resp = null;
		try {
			resp = FileUtils.readFileToString(file, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		} 		
		return resp;
	}
	
	public static HttpCaseInfo getCase(){
		//插入,测试OK
		String caseid = "7881015f-ffc5-431a-bf47-70ed12ea3638";
		HttpCaseInfoDao dao = new HttpCaseInfoDao();
		HttpCaseInfo info = dao.load(caseid);
		
		return info;
	}
	
	public static HttpCaseInfo setCase(HttpCaseInfo info){
		info.setCaseid(ToolBox.uuid());
		info.setCasename("用户扩展信息保存");
		info.setCmdcode("WD_USER_EXT_INFO_QUERY");
		info.setProductname("万得理财");
		info.setRespmsg("\"LatestMonthBenifit\":\"1480/2776\",");	
		info.setMatchid("3001");
		info.setRespMD5("185CE31A2A8A1BE432F5C2EBD650BA98");
		HttpCaseDBService single = new HttpCaseDBService(info);
		single.handle();
		
		return info;
	}
	
	public static HttpCaseInfo newCase(){
		HttpCaseInfo info = new HttpCaseInfo();
		info.setCaseid(ToolBox.uuid());
		info.setCasename("用户扩展信息保存");
		info.setCmdcode("WD_USER_EXT_INFO_QUERY");
		info.setProductname("万得理财");
		info.setRespmsg("\"LatestMonthBenifit\":\"1480/2776\",");	
		info.setMatchid("3001");
		info.setRespMD5("185CE31A2A8A1BE432F5C2EBD650BA98");
		HttpCaseDBService single = new HttpCaseDBService(info);
		single.handle();
		
		return info;
	}
}
