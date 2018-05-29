package com.landowner.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="lo.server")
public class Constant {

	public static int SERVER_PORT;
	
	public static int HEART_TIME;


	public void setServerPort(int serverPort) {
		Constant.SERVER_PORT = serverPort;
	}
	public static void setHeartTime(int heartTime) {
		Constant.HEART_TIME = heartTime;
	}
	
	
	
}
