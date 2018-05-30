package com.landowner.woker.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.landowner.woker.invoke.Invoker;


public class InvokerManager {

	private static Map<Short,Map<Short,Invoker>> invokers = new ConcurrentHashMap<>();
	
	public static void addInvoker(short module, short commander, Invoker invoker) {
		Map<Short, Invoker> map = invokers.get(module);
		if(map == null) {
			map = new HashMap<>();
			invokers.put(module, map);
		}
		map.put(commander, invoker);
	}
	
	public static Invoker getInvoker(short module, short command) {
		Map<Short, Invoker> map = invokers.get(module);
		if(map == null) {
			return null;
		}
		return map.get(command);
	}
	
}
