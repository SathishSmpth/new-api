package com.kamatchibotique.application.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.stereotype.Component;

import com.kamatchibotique.application.model.merchant.MerchantStore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component("cache")
public class CacheUtils {

	@Autowired
	@Qualifier("serviceCache")
	private Cache cache;

	public final static String REFERENCE_CACHE = "REF";
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheUtils.class);
	private final static String KEY_DELIMITER = "_";

	public void putInCache(Object object, String keyName) {
		cache.put(keyName, object);
	}

	public Object getFromCache(String keyName) {
		ValueWrapper vw = cache.get(keyName);
		return (vw != null) ? vw.get() : null;
	}

	public List<String> getCacheKeys(MerchantStore store) {
		org.ehcache.Cache<Object, Object> cacheImpl = (org.ehcache.Cache<Object, Object>) cache.getNativeCache();
		List<String> returnKeys = new ArrayList<>();
		for (Object key : cacheImpl) {
			try {
				String sKey = (String) key;
				// a key should be <storeId>_<rest of the key>
				int delimiterPosition = sKey.indexOf(KEY_DELIMITER);
				if (delimiterPosition > 0 && Character.isDigit(sKey.charAt(0))) {
					String keyRemaining = sKey.substring(delimiterPosition + 1);
					returnKeys.add(keyRemaining);
				}
			} catch (Exception e) {
				LOGGER.error("Key {} cannot be converted to a String or parsed", key, e);
			}
		}
		return returnKeys;
	}

	public void shutDownCache() {
		// Implement cache shutdown logic if necessary
	}

	public void removeFromCache(String keyName) {
		cache.evict(keyName);
	}

	public void removeAllFromCache(MerchantStore store) {
		org.ehcache.Cache<Object, Object> cacheImpl = (org.ehcache.Cache<Object, Object>) cache.getNativeCache();
		for (Object key : cacheImpl) {
			try {
				String sKey = (String) key;
				// a key should be <storeId>_<rest of the key>
				int delimiterPosition = sKey.indexOf(KEY_DELIMITER);
				if (delimiterPosition > 0 && Character.isDigit(sKey.charAt(0))) {
					cache.evict(key);
				}
			} catch (Exception e) {
				LOGGER.error("Key {} cannot be converted to a String or parsed", key, e);
			}
		}
	}
}
