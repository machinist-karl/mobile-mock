package cn.com.wind.test.rex;

import org.junit.Test;

import com.github.mobile.mock.utils.ToolBox;

public class JsonTest {
	
	@Test
	public void test01(){
		String s = "{\"CaseID\":\"67a07499-bd5b\",\"CaseName\":\"每日金股\",\"cmdcode\":\"0167001\",\"ProductName\":\"WindStock\",\"matchid\":\"2001\",\"url\":\"trategyservice/app/expoHttp.htm\"}";
		
		String[] y = s.substring(1, s.length() - 1).split(",");
		for(String str : y){
			System.out.println(str);
		}
		
	}

	@Test
	public void test02(){
		String s = "{\"CaseID\":\"67a07499-bd5b\",\"CaseName\":\"每日金股\",\"cmdcode\":\"0167001\",\"ProductName\":\"WindStock\",\"matchid\":\"2001\",\"url\":\"trategyservice/app/expoHttp.htm\"}";
		
		String dest = ToolBox.keysToLowCase(s);
		System.out.println(dest);
		
		
		
		
		
	}
	
	
	
}
