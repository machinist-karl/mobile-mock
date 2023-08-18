package com.github.mobile.mock.dbservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.mobile.mock.dao.HttpReqRuleDao;
import com.github.mobile.mock.domain.HttpReqRule;

public class HttpReqRuleService {
	private HttpReqRuleDao dao = new HttpReqRuleDao();
		
	/**
	 * 加载matchid与请求路径对应关系表
	 */
	
	public List<HttpReqRule> loadAll(){
		return dao.loadAll();
	}
	
	/**
	 * 加载Http请求识别规则到内存中
	 * @return
	 */
	public Map<String,String> loadRule(){
		List<HttpReqRule> list = this.loadAll();
		if(list == null || list.size() == 0){
			return null;
		}
		Map<String,String> mapping = new HashMap<>();
		int len = list.size();
		for(int i = 0;i < len;i++){
			HttpReqRule tmp = list.get(i);
			mapping.put(tmp.getMatchid(),tmp.getReqpath());
		}
		
		return mapping;
	}
}
