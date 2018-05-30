package com.landowner.game.netty.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

public class HandshakerManager {

	private static Map<Channel,WebSocketServerHandshaker> map = new ConcurrentHashMap<>();
	
	public static void addHandshaker(ChannelHandlerContext ctx,WebSocketServerHandshaker handshaker ) {
		map.put(ctx.channel(), handshaker);
	}
	
	public static void close(ChannelHandlerContext ctx, WebSocketFrame frame) {
		Channel channel = ctx.channel();
		WebSocketServerHandshaker handshaker = map.get(channel);
		map.remove(channel);
		handshaker.close(channel, (CloseWebSocketFrame)frame);
	}
}
