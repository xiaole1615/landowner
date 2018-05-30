package com.landowner.game.netty.handler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

public class ServerHandler extends ChannelInboundHandlerAdapter {

	private ExecutorService threadPool;
    private WebSocketServerHandshaker handshaker;

	public ServerHandler(ExecutorService threadPool) {
		this.threadPool = threadPool;
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("ServerHandler.channelRegistered()");
		super.channelRegistered(ctx);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("ServerHandler.channelActive()");
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("ServerHandler.channelInactive()");
		super.channelInactive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("读取客户的消息");
		threadPool.execute(() -> {
			if(msg instanceof FullHttpRequest) {//握手
				handHttpRequest(ctx, (FullHttpRequest)msg);
			}
		});
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		System.out.println("ServerHandler.userEventTriggered()");
		super.userEventTriggered(ctx, evt);
	}
	
	private void handHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
		System.out.println("要握手");
		//非握手请求
		if(!req.decoderResult().isSuccess() || !"websocket".equals(req.headers().get("Upgrade"))) {
            sendHttpReponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
		}
		
		HttpMethod method = req.method();
        String uri = req.uri();
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri);
        Map<String, List<String>> parameters = queryStringDecoder.parameters();
        for (Map.Entry<String, List<String>> entry : parameters.entrySet()) {
            System.out.println("key:"+entry.getKey());
            for (String s : entry.getValue()) {
                System.out.println("\tvalue:"+s);
            }
        }
        List<String> userName = parameters.get("userName");
        if(userName != null){
            if(!"user".equals(parameters.get("userName").get(0))){
                DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
                ChannelFuture f = ctx.channel().writeAndFlush(response);
                f.addListener(ChannelFutureListener.CLOSE);
                return;
            }
        }
        String webSocketURL = "ws://" + req.headers().get(HttpHeaderNames.HOST) + req.uri();
        WebSocketServerHandshakerFactory handshakerFactory = new WebSocketServerHandshakerFactory(webSocketURL, null, false);
        handshaker = handshakerFactory.newHandshaker(req);
        if(handshakerFactory == null){
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        }else {
            handshaker.handshake(ctx.channel(), req);
        }

	}
	private void sendHttpReponse(ChannelHandlerContext ctx,FullHttpRequest req,FullHttpResponse res) {
        //返回给客户端
        if(res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        //如果非keep-Alive,关闭连接，服务端向客户端发送数据
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if(!HttpUtil.isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
