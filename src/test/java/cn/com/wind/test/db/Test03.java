package cn.com.wind.test.db;

import org.junit.Test;

import com.github.mobile.mock.dao.HttpCaseInfoDao;
import com.github.mobile.mock.dbservice.HttpCaseDBService;
import com.github.mobile.mock.domain.HttpCaseInfo;
import com.github.mobile.mock.utils.ToolBox;

public class Test03 {

	@Test
	public void testHttpCaseInfoServiceInsert(){
		//插入,测试OK
		String caseid = "7881015f-ffc5-431a-bf47-70ed12ea3638";
		HttpCaseInfoDao dao = new HttpCaseInfoDao();
		HttpCaseInfo info = dao.load(caseid);
		
		System.out.println("查询结果"+info);
		
		info.setCaseid(ToolBox.uuid());
		info.setCasename("投资经理分页查询");
		info.setCmdcode("CMD_XCX_FUND_MANAGER_LIST_PAGE");
		info.setProductname("万得理财");
		info.setRespmsg("\"resultCode\":\"E0000\",");	
		info.setMatchid("3001");
		info.setRespMD5("709c6bed11cc1b936055ea85048f0abf");
		HttpCaseDBService single = new HttpCaseDBService(info);
		single.handle();
	}
	
	@Test
	public void testHttpCaseInfoServiceUpdate(){
		//更新,测试OK
		//用例没变情况下
		String caseid = "7881015f-ffc5-431a-bf47-70ed12ea3638";
		HttpCaseInfoDao dao = new HttpCaseInfoDao();
		HttpCaseInfo info = dao.load(caseid);
		
		System.out.println("查询结果"+info);
		
		//info.setCaseid(ToolBox.uuid());
		info.setCasename("基金-产品档案-持仓接口");
		info.setCmdcode("CMD_XCX_FUND_HOLD_ASSET");
		info.setProductname("万得理财");
		info.setRespmsg("\"returnTime\":\"1517814281111\",");	
		info.setMatchid("3001");
		info.setRespMD5("928FD808EAED1F62D5E60E9EE55D0D15");
		HttpCaseDBService single = new HttpCaseDBService(info);
		String resp = single.handle();
		System.out.println("新的结果为：" + resp);
	}
	
}
