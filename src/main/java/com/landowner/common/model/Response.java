package com.landowner.common.model;

import com.alibaba.fastjson.JSONObject;
import com.landowner.common.constant.ResultCode;
import com.landowner.game.netty.manager.SessionManager;
import com.landowner.game.netty.model.Session;

public class Response {

	private int pack;
	
	private int code = ResultCode.SUCCESS;
	
	private Object data;
	
	public Response(int pack, Object data) {
		this.pack = pack;
		this.data = data;
	}

	public String toJSONString() {
		JSONObject jo = new JSONObject();
		jo.put("pack", this.pack);
		jo.put("code", this.code);
		jo.put("data", this.data);
		return jo.toJSONString();
	}
	
	public void send(String id) {
		Session session = SessionManager.getSession(id);
		session.write(toJSONString());
	}
	
	public int getPack() {
		return pack;
	}

	public void setPack(int pack) {
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
