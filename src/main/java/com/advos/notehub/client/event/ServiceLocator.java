/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advos.notehub.client.event;

/**
 *
 * @author aisyahumar
 */

import java.util.HashMap;
import java.util.Map;

public enum ServiceLocator {
	
	INSTANCE;
	
	private Map<Class<?>, Class<?>> services = new HashMap<>();
	private Map<Class<?>, Object> cache = new HashMap<>();
	
	public <T> void registerService(Class<T> service, Class<? extends T> provider) {
		services.put(service,  provider);
	}
	
	public <T> T getService(Class<T> type) {
		Class<?> provider = services.get(type);
		
		try {
			//Object instance = cache.getOrDefault(type, provider.getConstructor().newInstance());
			//cache.put(type, instance);
			Object instance;
			if(cache.containsKey(type)) {
				instance = cache.get(type);
			}else {
				instance = provider.getConstructor().newInstance();
				cache.put(type, instance);
			}
			return type.cast(instance);
		}catch(Exception e) {
			throw new IllegalArgumentException("Service is not available");
		}
        }

}
