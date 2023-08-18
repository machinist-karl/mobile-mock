package cn.com.wind.test.rex;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class RexTest {
	
	@Test
	public void test01() throws Exception{
		String dst = FileUtils.readFileToString(new File("d:\\gs.txt"), "UTF-8");
		String key = "subjectId";
		String regex = "\"" + key + ".*?([,}$])";
		String replacement = "\"" + key + "\"" + ":" + "$1";
		//String t = dst.replaceAll(regex, replacement);
		String r = "\"" + key + "\":";
		String n = r + "123";
		String t = dst.replaceAll(regex, replacement).replace(r,n);
		System.out.println("t = " + t);
	}
	
	@Test
	public void test02() throws Exception{
		String dst = FileUtils.readFileToString(new File("d:\\ks.txt"), "UTF-8");
		dst.replaceAll("\\s*", "");
		String key = "subjectId";
		String regex = "\"" + key + ".*?([,}\\]$])";
		String replacement = "\"" + key + "\"" + ":" + "$1";
		//String t = dst.replaceAll(regex, replacement);
		String r = "\"" + key + "\":";
		String n = r + "998";
		System.out.println(dst.replaceAll(regex, replacement));
		String t = dst.replaceAll(regex, replacement).replace(r,n);
		
		System.out.println("t = " + t);
	}
}
