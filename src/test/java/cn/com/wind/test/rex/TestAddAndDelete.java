package cn.com.wind.test.rex;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.github.mobile.mock.SSLService.HttpsMockData;
import com.github.mobile.mock.domain.HttpCaseInfo;

public class TestAddAndDelete {

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
	
	
	@Test
	public void testAdd() throws Exception{//Test OK
		String orgi = FileUtils.readFileToString(new File("D:\\gs.txt"), "UTF-8");
		System.out.println(orgi);
		
		HttpCaseInfo info = getCase01(orgi);
		HttpsMockData data = new HttpsMockData(info,orgi);
		data.init();
		String result = data.add();
		//System.out.println(result);
		//System.out.println("Mock data：" + result);		
	}
	
	@Test //Pass
	public void testDelete01() throws Exception{//仅删除一个key值,所有key
		String orgi = FileUtils.readFileToString(new File("D:\\gs.txt"), "UTF-8");
		
		String replace = "{\"price\"}";
		HttpCaseInfo info = getCase01(replace);
		info.setOperation("0");
		info.setMethod("delete");
		HttpsMockData data = new HttpsMockData(info,orgi);
		data.init();
		String result = data.getMockString();
		System.out.println("Mock data：" + result);
	}
	
	@Test //Pass
	public void testDelete02() throws Exception{//仅删除最前面的key值,所有key
		String orgi = FileUtils.readFileToString(new File("D:\\gs.txt"), "UTF-8");
		
		String replace = "{bright}";
		HttpCaseInfo info = getCase01(replace);
		info.setOperation("0");
		info.setMethod("delete");
		HttpsMockData data = new HttpsMockData(info,orgi);
		data.init();
		String result = data.getMockString();
		System.out.println("Mock data：" + result);
	}
	
	@Test //Pass
	public void testDelete03() throws Exception{//一次替换多个,所有key
		String orgi = FileUtils.readFileToString(new File("D:\\gs.txt"), "UTF-8");
		
		String replace = "{bright,id,increase,stockName,subjectId}";
		HttpCaseInfo info = getCase01(replace);
		info.setOperation("0");
		info.setMethod("delete");
		HttpsMockData data = new HttpsMockData(info,orgi);
		data.init();
		String result = data.getMockString();
		System.out.println("Mock data：" + result);
	}
	
	@Test //Pass
	public void testDelete04() throws Exception{//一次替换多个,只替换一个符合的key
		String orgi = FileUtils.readFileToString(new File("D:\\gs.txt"), "UTF-8");
		
		String replace = "{bright,id,increase,stockName,subjectId}";
		HttpCaseInfo info = getCase01(replace);
		info.setOperation("1");
		info.setMethod("delete");
		HttpsMockData data = new HttpsMockData(info,orgi);
		data.init();
		String result = data.getMockString();
		System.out.println("Mock data：" + result);
	}
	
	@Test //Pass
	public void testDelete05() throws Exception{//一次替换多个,只替换一个符合的key
		String orgi = FileUtils.readFileToString(new File("D:\\gs.txt"), "UTF-8");
		
		String replace = "{stockCode}";
		HttpCaseInfo info = getCase01(replace);
		info.setOperation("1");
		info.setMethod("delete");
		HttpsMockData data = new HttpsMockData(info,orgi);
		data.init();
		String result = data.getMockString();
		System.out.println("Mock data：" + result);
	}
}
