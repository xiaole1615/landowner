package com.landowner.common.constant;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="landowner.server")
public class SystemConstant {

	public static int SERVER_PORT;
	
	public static int HEART_TIME;


	public void setServerPort(int serverPort) {
		SystemConstant.SERVER_PORT = serverPort;
	}
	public static void setHeartTime(int heartTime) {
		SystemConstant.HEART_TIME = heartTime;
	}
	
	
	
}
