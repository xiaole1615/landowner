package com.landowner.woker.invoke;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Invoker {

	private Method method;
	
	private Object target;

	public static Invoker valueOf(Method method, Object target) {
		Invoker invoker = new Invoker(method,target);
		return invoker;
	}
	
	public Object invoke(Object...paramValues) {
		
		try {
			return this.method.invoke(target, paramValues);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	} 
	
	
	public Invoker(Method method, Object target) {
		super();
		this.method = method;
		this.target = target;
	}



	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}
	
	
}
