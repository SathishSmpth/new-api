package com.kamatchibotique.application.utils;

import com.kamatchibotique.application.utils.CacheUtils;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Autowired;

@Component
public class WebApplicationCacheUtils {
	
	@Autowired
	private CacheUtils cache;
	
	public Object getFromCache(String key) throws Exception {
		return cache.getFromCache(key);
	}
	
	public void putInCache(String key, Object object) throws Exception {
		cache.putInCache(object, key);
	}

}
