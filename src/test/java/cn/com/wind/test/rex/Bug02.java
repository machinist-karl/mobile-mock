package cn.com.wind.test.rex;
import org.junit.Test;

public class Bug02 {
	
	@Test
	public void testUpdate01() throws Exception{
		String msg = "{\"adTarget\": \"券商描述02,券商描述03\"}";
		
		String dest = msg.substring(1, msg.length() - 1);
		System.out.println("dest1 = " + dest);
		
		String[] replacement;
		
		if(!dest.contains(",\"")){
			replacement = new String[] { msg };
		}
	}
	
	@Test
	public void testUpdate02() throws Exception{
		String target = "★★★★★";
		String msg = "Abc★★★★★ererevgfgfd";
	    String newStr = msg.replace(target, ",");
	    System.out.println(newStr);  
	}
	
}
