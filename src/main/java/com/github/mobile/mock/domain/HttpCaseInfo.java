package com.github.mobile.mock.domain;

public class HttpCaseInfo {

	private String caseid;
	private String casename;
	private String cmdcode;
	private String productname;
	private String respmsg;
	private String matchid;
	private String respMD5;
	private String url;
	private String method;
	private String operation;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public HttpCaseInfo() {

	}

	public String getCaseid() {
		return caseid;
	}

	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}

	public String getCasename() {
		return casename;
	}

	public void setCasename(String casename) {
		this.casename = casename;
	}

	public String getCmdcode() {
		return cmdcode;
	}

	public void setCmdcode(String cmdcode) {
		this.cmdcode = cmdcode;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public String getRespmsg() {
		return respmsg;
	}

	public void setRespmsg(String respmsg) {
		this.respmsg = respmsg;
	}

	public String getMatchid() {
		return matchid;
	}

	public void setMatchid(String matchid) {
		this.matchid = matchid;
	}

	public String getRespMD5() {
		return respMD5;
	}

	public void setRespMD5(String respMD5) {
		this.respMD5 = respMD5;
	}

	@Override
	public String toString() {
		return "HttpCaseInfo [caseid=" + caseid + ", casename=" + casename + ", cmdcode=" + cmdcode + ", productname="
				+ productname + ", respmsg=" + respmsg + ", matchid=" + matchid + ", respMD5=" + respMD5 + ", url="
				+ url + ", method=" + method + ", operation=" + operation + "]";
	}	
}
