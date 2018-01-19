/*
 * CacheIF.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.util.cache;

/**
 * This is a simple cache that can be used to store objects (including html strings). We use this when we are 
 * requesting something frequently that the result does not change often, and requires some processing or database 
 * access.
 * 
 * This should speed-up the experience for the user, and at the same time reduce costs if processing charged on
 * a usage basis.
 * 
 * @author Chris Hatton
 */
public interface CacheIF {

    public void put(CacheKey cacheKey, Object value);
    
    public void put(CacheKey cacheKey, Object value, int expireTime);
    
    public Object get(CacheKey cacheKey);
    
}
