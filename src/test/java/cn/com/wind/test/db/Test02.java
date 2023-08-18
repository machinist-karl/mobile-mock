package cn.com.wind.test.db;

import java.util.List;

import org.junit.Test;

import com.github.mobile.mock.dao.HttpCaseInfoDao;
import com.github.mobile.mock.dao.HttpReqRuleDao;
import com.github.mobile.mock.domain.HttpCaseInfo;
import com.github.mobile.mock.domain.HttpReqRule;
import com.github.mobile.mock.utils.ToolBox;

public class Test02 {
	
	@Test
	public void testload(){
		
		//查询
		String caseid = "7881015f-ffc5-431a-bf47-70ed12ea3638";
		HttpCaseInfoDao dao = new HttpCaseInfoDao();
		HttpCaseInfo info = dao.load(caseid);
		
		System.out.println("查询结果"+info);
		
		/**
		 *  //更新
			info.setCasename("字段为小数");
			info.setCmdcode("0998805");
			info.setRespmsg("99/77");		
			dao.update(info);
		 */
				
		//插入
		info.setCaseid(ToolBox.uuid());
		info.setCasename("万得理财查询新发基金接口");
		info.setCmdcode("CMD_XCX_NEW_FUND_LIST_PAGE");
		info.setProductname("万得理财");
		info.setRespmsg("\"message\":\"请求成功\",");	
		info.setMatchid("3001");
		info.setRespMD5("84C63E2080419DDF554FE5EA207D5209");
		dao.add(info);
	}
	
	@Test
	public void testfindrule(){
		HttpReqRuleDao rule = new HttpReqRuleDao();
		List<HttpReqRule> list = rule.loadAll();
		
		System.out.println(list.get(0).toString());
	}
	
}
