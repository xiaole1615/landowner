package com.landowner.common.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Request {

	private String pack;
	
	private String data;
	
	public String toJSONString() {
		JSONObject jo = new JSONObject();
		jo.put("pack", pack);
		jo.put("data", data);
		return jo.toJSONString();
	}
	
	public static Request parse(String str) {
		Request request = new Request();
		JSONObject jo = JSON.parseObject(str);
		request.setPack(jo.getString("pack"));
		request.setData(jo.getString("data"));
		return request;
	}
	
	public short getModule() {
		String module = pack.substring(0, 2);
		return Short.valueOf(module);
	}
	public short getCommand() {
		String command = pack.substring(2);
		return Short.parseShort(command);
	}

	public String getPack() {
		return pack;
	}

	public void setPack(String pack) {
		this.pack = pack;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	
}
