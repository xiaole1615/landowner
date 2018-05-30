package com.landowner.listener.server.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.landowner.game.netty.NettyServerService;
import com.landowner.listener.server.InitService;

@Service("initService")
public class InitServiceImpl implements InitService {

	@Resource
	NettyServerService nettyServerService;
	
	@Override
	public void start() {
		nettyServerService.start();
	}

	@Override
	public void close() {
		nettyServerService.close();
	}

}
