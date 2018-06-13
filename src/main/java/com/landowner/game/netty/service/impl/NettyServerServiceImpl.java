package com.landowner.game.netty.service.impl;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import com.landowner.common.constant.SystemConstant;
import com.landowner.game.netty.handler.ServerHandler;
import com.landowner.game.netty.service.NettyServerService;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

@Service("nettyServerService")
public class NettyServerServiceImpl implements NettyServerService, DisposableBean {

    private ExecutorService threadPool = Executors.newFixedThreadPool(100);
    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;
	
    private ServerHandler handler;
	@Override
	public void start() {
		final int heartTime = SystemConstant.HEART_TIME;
		bossGroup = new NioEventLoopGroup();
		workGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workGroup);
			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.option(ChannelOption.SO_BACKLOG, 100);
			bootstrap.handler(new LoggingHandler(LogLevel.DEBUG));
			bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel chnnel) throws Exception {
					handler = new ServerHandler(threadPool);
					ChannelPipeline pl = chnnel.pipeline();
					//心跳
					pl.addLast(new IdleStateHandler(heartTime, heartTime, heartTime));
					//将请求和应答消息解码为http消息
                    pl.addLast(new HttpServerCodec());
                    //将http消息的多个部分合成一条完整的http消息
                    pl.addLast(new HttpObjectAggregator(65536));
                    //向客户端发送html5文件
                    pl.addLast(new ChunkedWriteHandler());
                    //添加处理器
                    pl.addLast(handler);
				}
				
			});

			bootstrap.bind(SystemConstant.SERVER_PORT);
		} catch (Exception e) {
		}
		System.out.println("---------------started---------------------");
	}

	@Override
	public void close() {
		Optional.ofNullable(bossGroup).map(bossGroup -> bossGroup.shutdownGracefully());
		Optional.ofNullable(workGroup).map(workGroup -> workGroup.shutdownGracefully());
	}

	@Override
	public void destroy() throws Exception {
		close();
		threadPool.shutdown();
	}
}
