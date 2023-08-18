package cn.com.wind.test.http;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.gson.Gson;

import com.github.mobile.mock.domain.HttpCaseInfo;
import com.github.mobile.mock.domain.HttpResult;
import com.github.mobile.mock.utils.ToolBox;

public class HttpServerTest {
	
	@Test
	public void test01(){
		Map<String,String> params = new HashMap<>();
		params.put("method","update");
		params.put("operation","");
		String basic = "{\"CaseID\":\"67a07499-bd5b\",\"CaseName\":\"每日金股\",\"cmdcode\":\"0167001\",\"ProductName\":\"WindStock\",\"matchid\":\"2001\",\"url\":\"trategyservice/app/expoHttp.htm\"}";
		String replacement = "{\"stockName\":\"白色巨石\",\"subjectId\":1098}";
		params.put("basic",basic);
		params.put("replacement",replacement);
		
		HttpCaseInfo single = HttpCaseService.getCase(params);

		String tc = HttpCaseService.checkHttpCase(single);
		System.out.println(single.getOperation().isEmpty());

	}
	
	@Test
	public void test02(){
		StringBuilder sb = new StringBuilder();
		String s = sb.toString();
		System.out.println(s.isEmpty());
		s = "";
		System.out.println("s = " + s);
		System.out.println(s.isEmpty());
	}
	
	@Test
	public void test03(){
		Gson gson = new Gson();
		HttpResult res = new HttpResult();
		res.setResultCode("E000");
		res.setResultmessage("获取请求和参数成功");
		res.setReturnTime(ToolBox.getCurrentStringDateTime());
		
		String result = gson.toJson(res);
		System.out.println(result);
	}

}
