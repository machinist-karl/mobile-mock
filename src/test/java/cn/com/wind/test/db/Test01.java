package cn.com.wind.test.db;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.junit.Test;

import com.github.mobile.mock.domain.HttpReqRule;
import com.github.mobile.mock.jdbc.TxQueryRunner;

public class Test01 {
	@Test
	public void testConn()throws Exception{
		QueryRunner qr = new TxQueryRunner();
		String sql = "select * from tb_http_mockmatch";
		List<HttpReqRule> ruleList = qr.query(sql,new BeanListHandler<HttpReqRule>(HttpReqRule.class));
		
		System.out.println(ruleList.get(0));
	}
	
	@Test
	public void testClassPath()throws Exception{
		System.out.println(System.getProperty("java.class.path"));  
	}
	
	@Test
	public void testInsertAll()throws Exception{
		QueryRunner qr = new TxQueryRunner();
		String sql = "insert into tb_http_mockcaseinfo values(?,?,?,?,?,?,?,?,?)";
		Object[] params = {UUID.randomUUID().toString(),"插入Null字段","0267001","WindStock(万得股票)"
				, "快速转出后，账单中基金名称与基金代码显示为null","2001","4565sdfsdfsdf",null,null};
		
		int num = qr.update(sql, params);
		
		System.out.println("insert " + num + " records!");
	}
	
	
	@Test
	public void convertTime() throws Exception{
		long t = System.currentTimeMillis();
		SimpleDateFormat sdf_default = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sdf_default.format(t));
		
		
	}
	
	
	@Test
	public void testUpdateTime()throws Exception{
		QueryRunner qr = new TxQueryRunner();
		String sql = "update tb_http_mockcaseinfo set createDate = ?,updateTime = ? ";
		Object[] params = {};
		
		int num = qr.update(sql, params);
		
		System.out.println("insert " + num + " records!");
	}
	 
}
