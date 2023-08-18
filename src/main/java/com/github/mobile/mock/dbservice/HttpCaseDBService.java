package com.github.mobile.mock.dbservice;

import com.github.mobile.mock.dao.HttpCaseInfoDao;
import com.github.mobile.mock.domain.HttpCaseInfo;

public class HttpCaseDBService {
	private HttpCaseInfoDao dao = new HttpCaseInfoDao();
	private HttpCaseInfo hci;
		
	public HttpCaseDBService() {
	}

	public HttpCaseDBService(HttpCaseInfo info) {
		this.hci = info;
	}

	/**
	 * 插入用例信息到数据库
	 * @param HttpCaseInfo
	 */
	public void add(HttpCaseInfo c){
		dao.add(c);
	}
		
	/**
	 * 更新数据库中的用例信息
	 * @param HttpCaseInfo
	 */
	public void update(HttpCaseInfo c){
		dao.update(c);
	}
	
	/**
	 * 获取单个用例信息
	 * @param caseid
	 */
	public HttpCaseInfo load(String cid){
		return dao.load(cid);
	}
	
	/**
	 * 处理用例，将数据库中的数据与文件中读取的实例对象进行比较
	 * @return mock应答报文内容
	 */
	public String handle(){
		if(hci == null){
			return "ERROR";
		}
		
		//数据库查找该条用例是否存在
		HttpCaseInfo dbCase = load(hci.getCaseid());
		
		//如果数据库无该条记录，则将用例直接插入数据库
		if(dbCase == null){
			add(hci);
			return hci.getRespmsg();//直接返回Mock应答内容
		}
		
		//如果数据库中有该条记录，则判断数据MD5值是否相等
		if(hci.getRespMD5().equals(dbCase.getRespMD5())){//相等，则直接返回Mock应答内容
			return hci.getRespmsg();
		}
		
		//不相等，则先更新数据库，再返回Mock应答内容给调用者
		update(hci);
		return hci.getRespmsg();
	}
}
