package cn.com.wind.test.rex;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.junit.Test;


public class Bug01 {
	
	@Test //Pass
	public void testUpdate01() throws Exception{
		String orgi = FileUtils.readFileToString(new File("D:\\bug.txt"), "UTF-8");
		String dest = new String(orgi);
		
		String p = "\"([^\"]*)\"" ;
		//String p = "(\"*[^:][,]+.*?[^:]){2}\"" ;
		Pattern P=Pattern.compile(p);
	    Matcher matcher1=P.matcher(orgi); 
	    while(matcher1.find())
	    {
	    	String tmp = matcher1.group(0).replaceAll(p, "$1");
	        System.out.println(tmp);
	    	if(tmp.contains(",") && !tmp.contains("\\")){
	    		
	    		String tmp2 = tmp.replace(",", "，");
	    		dest = dest.replaceAll(tmp, tmp2);
	    		
	    	}
	    	//System.out.println(matcher1.replaceAll(tmp));
	    }

      System.out.println(dest);
	}
	
	@Test //Pass
	public void testUpdate02() throws Exception{
		String orgi = FileUtils.readFileToString(new File("D:\\bug.txt"), "UTF-8");
		String dest = new String(orgi);
		
		//String p = ":\".*?\"" ;
		String p = "(\"[^:][,]+.*?[^:]){2}\"" ;
		Pattern P=Pattern.compile(p);
	    Matcher matcher1=P.matcher(orgi); 
	    while(matcher1.find())
	    {
	    	String tmp = matcher1.group(0).replaceAll(p, "$1");
	        System.out.println(tmp);
	    	if(tmp.contains(",") && !tmp.contains("\\")){
	    		
	    		String tmp2 = tmp.replace(",", "，");
	    		dest = dest.replaceAll(tmp, tmp2);
	    		
	    	}
	    	//System.out.println(matcher1.replaceAll(tmp));
	    }

      System.out.println(dest);
	}
}
