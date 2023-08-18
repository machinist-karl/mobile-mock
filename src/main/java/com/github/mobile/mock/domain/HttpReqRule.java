package com.github.mobile.mock.domain;

public class HttpReqRule {
	private String matchid;
	private String reqpath;
	//private String productname;
	
	public HttpReqRule() {

	}

	public String getMatchid() {
		return matchid;
	}

	public void setMatchid(String matchid) {
		this.matchid = matchid;
	}

	public String getReqpath() {
		return reqpath;
	}

	public void setReqpath(String reqpath) {
		this.reqpath = reqpath;
	}

	/**
	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}
	**/

	@Override
	public String toString() {
		return "HttpReqRuleService [matchid=" + matchid + ", reqpath=" + reqpath +"]";
	}
		
}
