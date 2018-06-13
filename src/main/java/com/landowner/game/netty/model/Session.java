  
package com.landowner.game.netty.model;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/** 
 * ClassName: SessionManager <br/> 
 * Description: 该类的描述信息. <br/>
 * Date: 2018年5月30日-下午5:52:13 <br/> 
 * @author xiexiaole  <br/>
 */
public class Session {
	
	private Channel channel;
	
	/**
	 * 绑定对象key
	 */
	public static AttributeKey<Object> ATTACHMENT_KEY  = AttributeKey.valueOf("ATTACHMENT_KEY");
	
	public Session(Channel channel) {
		this.channel = channel;
	}

	/**
	 * 会话绑定对象
	 * @return
	 */
	public Object getAttachment() {
		Object object = channel.attr(ATTACHMENT_KEY).get();
		return object;
	}
	
	/**
	 * 绑定对象
	 * @return
	 */
	public void setAttachment(Object attachment){
		channel.attr(ATTACHMENT_KEY).set(attachment);
	}
	
	/**
	 * 移除绑定对象
	 * @return
	 */
	public void removeAttachment(){
		channel.attr(ATTACHMENT_KEY).set(null);
	}
	
	/**
	 * 向会话中写入消息
	 * @param message
	 */
	public void write(String message){
		channel.writeAndFlush(message);
	}
	
	/**
	 * 判断会话是否在连接中
	 * @return
	 */
	public boolean isConnected(){
		return channel.isActive();
	}
	
	/**
	 * 关闭
	 * @return
	 */
	public void close(){
		channel.close();
	}
}
  
