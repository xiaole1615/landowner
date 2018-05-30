package com.landowner.common.model;

import com.alibaba.fastjson.JSONObject;
import com.landowner.common.constant.ResultCode;

public class Response {

	private String pack;
	
	private int code = ResultCode.SUCCESS;
	
	private Object data;
	
	public String toJSONString() {
		JSONObject jo = new JSONObject();
		jo.put("pack", pack);
		jo.put("code", code);
		jo.put("data", data);
		return jo.toJSONString();
	}
	
	public String getPack() {
		return pack;
	}

	public void setPack(String pack) {
		this.pack = pack;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
}
