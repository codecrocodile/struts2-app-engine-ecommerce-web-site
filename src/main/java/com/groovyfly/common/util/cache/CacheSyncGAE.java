/*
 * CacheGAE.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.util.cache;

import java.util.logging.Level;

import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;


/**
 * @author Chris Hatton
 */
public class CacheSyncGAE implements CacheIF {
    
    private MemcacheService syncCache;
    
    /**
     * Constructor
     */
    public CacheSyncGAE() {
        syncCache = MemcacheServiceFactory.getMemcacheService();
        syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
    }

    /* 
     * @see com.groovyfly.common.util.cache.CacheIF#put(com.groovyfly.common.util.cache.CacheKey, java.lang.Object)
     */
    @Override
    public void put(CacheKey cacheKey, Object value) {
        syncCache.put(cacheKey, value);
    }
    
    /* 
     * @see com.groovyfly.common.util.cache.CacheIF#put(com.groovyfly.common.util.cache.CacheKey, java.lang.Object, int)
     */
    @Override
    public void put(CacheKey cacheKey, Object value, int expireSecondsDelay) {
        syncCache.put(cacheKey, value, Expiration.byDeltaSeconds(expireSecondsDelay));
    }

    /* 
     * @see com.groovyfly.common.util.cache.CacheIF#get(com.groovyfly.common.util.cache.CacheKey)
     */
    @Override
    public Object get(CacheKey cacheKey) {
        return syncCache.get(cacheKey);
    }

}
