package com.landowner.game.netty.handler;

import java.util.concurrent.ExecutorService;

import com.alibaba.fastjson.JSONObject;
import com.landowner.common.constant.PackNum;
import com.landowner.common.model.Request;
import com.landowner.common.model.Response;
import com.landowner.common.model.User;
import com.landowner.game.netty.manager.HandshakerManager;
import com.landowner.game.netty.manager.SessionManager;
import com.landowner.game.netty.model.Session;
import com.landowner.game.woker.invoke.Invoker;
import com.landowner.game.woker.manager.InvokerManager;
import com.landowner.util.IdGenerator;

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
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

/** 
 * ClassName: ServerHandler <br/> 
 * Description: nettHandler. <br/>
 * Date: 2018年5月31日-上午10:36:49 <br/> 
 * @author meter  <br/>
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

	private ExecutorService threadPool;

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
		Session session = new Session(ctx.channel());
		User user = (User) session.getAttachment();
		SessionManager.removeSession(user.getUserId());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("读取客户的消息");
		threadPool.execute(() -> {
			if(msg instanceof FullHttpRequest) {//握手
				handHttpRequest(ctx, (FullHttpRequest)msg);
			}else if(msg instanceof WebSocketFrame) {
				disposeWebsocketFrame(ctx, (WebSocketFrame)msg);
			}
		});
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		System.out.println("ServerHandler.userEventTriggered()");
		
	}
	
	/** 
	 * disposeWebsocketFrame:消息处理.  
	 * @author meter
	 * @param ctx
	 * @param frame 
	 */  
	private void disposeWebsocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
		//websocket关闭指令
		if(frame instanceof CloseWebSocketFrame) {
			HandshakerManager.close(ctx, frame);
			return;
		}
		//判断是否是ping指令
		if(frame instanceof PingWebSocketFrame) {
			ctx.channel().writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		//判断是否是二进制消息
		if(!(frame instanceof TextWebSocketFrame)) {
			throw new RuntimeException("目前不支持二进制消息..");
		}
		String request = ((TextWebSocketFrame)frame).text();
		Request parse = Request.parse(request);
		short module = parse.getModule();
		short command = parse.getCommand();
		
		Invoker invoker = InvokerManager.getInvoker(module, command);
		invoker.invoke(parse.getData());
	}
	
	/** 
	 * handHttpRequest:webSocket请求握手.  
	 * @author meter
	 * @param ctx
	 * @param req 
	 */  
	private void handHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
		System.out.println("要握手");
		//非握手请求
		if(!req.decoderResult().isSuccess() || !"websocket".equals(req.headers().get("Upgrade"))) {
            sendHttpReponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
		}
		
		/*HttpMethod method = req.method();
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
        }*/
        String webSocketURL = "ws://" + req.headers().get(HttpHeaderNames.HOST) + req.uri();
        WebSocketServerHandshakerFactory handshakerFactory = new WebSocketServerHandshakerFactory(webSocketURL, null, false);
        WebSocketServerHandshaker handshaker = handshakerFactory.newHandshaker(req);
        HandshakerManager.addHandshaker(ctx, handshaker);
        if(handshaker == null){
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        }else {
            handshaker.handshake(ctx.channel(), req);
        }
        //添加session
        String id = IdGenerator.getIdString();
        Session session = new Session(ctx.channel());
        User user = new User();
        user.setUserId(id);
        session.setAttachment(user);
        SessionManager.addSession(id, session);
        
        //握手成功，响应信息
        JSONObject jo = new JSONObject();
        
        jo.put("id", id);
        new Response(PackNum.LOGIN, jo.toJSONString()).send(id);

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
