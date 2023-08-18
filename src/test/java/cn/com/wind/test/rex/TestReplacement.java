package cn.com.wind.test.rex;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.github.mobile.mock.SSLService.HttpsMockData;
import com.github.mobile.mock.domain.HttpCaseInfo;

public class TestReplacement {

	public HttpCaseInfo getCase01(String msg){
		HttpCaseInfo info = new HttpCaseInfo();
		info.setCaseid("87987545");
		info.setCasename("每日金股");
		info.setCmdcode("0167001");
		info.setMatchid("2001");
		info.setMethod("add");
		info.setOperation("1");
		info.setProductname("Wind Stock");
		info.setRespMD5("YYUYIBMJHKIWHDIFJH");
		info.setRespmsg(msg);
		info.setUrl("/strategyservice/image");
		
		return info;
	}
	
	
	@Test //Pass
	public void testUpdate01() throws Exception{//仅更新一个key的值,所有key
		String orgi = FileUtils.readFileToString(new File("D:\\gs.txt"), "UTF-8");
		
		String replace = "{\"price\":null}";
		HttpCaseInfo info = getCase01(replace);
		info.setOperation("0");
		info.setMethod("update");
		HttpsMockData data = new HttpsMockData(info,orgi);
		data.init();
		String result = data.getMockString();
		System.out.println("Mock data：" + result);
	}
	
	@Test //Pass
	public void testUpdate02() throws Exception{//更新多个key的值,所有key
		String orgi = FileUtils.readFileToString(new File("D:\\gs.txt"), "UTF-8");
		
		String replace = "{\"bright\":\"小朋友，你好\",\"id\":9527,\"stockName\":\"万得超级股\",\"subjectId\":9999999}";
		HttpCaseInfo info = getCase01(replace);
		info.setOperation("0");
		info.setMethod("update");
		HttpsMockData data = new HttpsMockData(info,orgi);
		data.init();
		String result = data.getMockString();
		System.out.println("Mock data：" + result);
	}
	
	@Test //Pass
	public void testUpdate03() throws Exception{//更新多个key的值,第一次出现的key
		String orgi = FileUtils.readFileToString(new File("D:\\gs.txt"), "UTF-8");
		
		String replace = "{\"bright\":\"小朋友，你好\",\"id\":9527,\"stockName\":\"万得超级股\",\"subjectId\":9999999}";
		HttpCaseInfo info = getCase01(replace);
		info.setOperation("1");
		info.setMethod("update");
		HttpsMockData data = new HttpsMockData(info,orgi);
		data.init();
		String result = data.getMockString();
		System.out.println("Mock data：" + result);
	}
	
	@Test //Pass
	public void testUpdate04() throws Exception{//更新多个key的值,第一次出现的key
		String orgi = FileUtils.readFileToString(new File("D:\\gs.txt"), "UTF-8");
		
		String replace = "{\"stockCode\":\"10099.WC\"}";
		HttpCaseInfo info = getCase01(replace);
		info.setOperation("1");
		info.setMethod("update");
		HttpsMockData data = new HttpsMockData(info,orgi);
		//data.init();
		String result = data.getMockString("book");
		System.out.println("Mock data：" + result);
	}
}
