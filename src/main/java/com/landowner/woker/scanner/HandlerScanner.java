package com.landowner.woker.scanner;

import java.lang.reflect.Method;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import com.landowner.woker.annotation.SocketCommand;
import com.landowner.woker.annotation.SocketModule;
import com.landowner.woker.invoke.Invoker;
import com.landowner.woker.manager.InvokerManager;

@Component
public class HandlerScanner implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		
		Class<? extends Object> clazz = bean.getClass();
		Class<?>[] interfaces = clazz.getInterfaces();
		
		if(interfaces != null && interfaces.length > 0) {
			for (Class<?> interfa : interfaces) {
				SocketModule socketModule = interfa.getAnnotation(SocketModule.class);
				if(socketModule == null) {
					continue;
				}
				Method[] methods = interfa.getMethods();
				if(methods != null && methods.length > 0) {
					for (Method method : methods) {
						SocketCommand socketCommand = method.getAnnotation(SocketCommand.class);
						if(socketCommand == null) {
							continue;
						}
						short module = socketModule.module();
						short command = socketCommand.command();
						if(InvokerManager.getInvoker(module, command) == null) {
							InvokerManager.addInvoker(module, command, Invoker.valueOf(method, bean));
						}
					}
				}
			}
		}
		return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
	}
	
	

}
