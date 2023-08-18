package com.github.mobile.mock.domain;

public class HttpResult {
	private String resultCode;
	private String resultmessage;
	private String returnTime;
	
	public HttpResult() {
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultmessage() {
		return resultmessage;
	}

	public void setResultmessage(String resultmessage) {
		this.resultmessage = resultmessage;
	}

	public String getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(String returnTime) {
		this.returnTime = returnTime;
	}

	@Override
	public String toString() {
		return "HttpResult [resultCode=" + resultCode + ", resultmessage=" + resultmessage + ", returnTime="
				+ returnTime + "]";
	}

}
