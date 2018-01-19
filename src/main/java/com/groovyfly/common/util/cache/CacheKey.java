/*
 * CacheKey.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.util.cache;

/**
 * These are the keys used to store objects (including html strings) in an in memory cache for fast access.
 * 
 * @see #com.groovyfly.common.util.cache.CacheIF
 * 
 * @author Chris Hatton
 */
public enum CacheKey {
    MENU_HTML,
    MONTH_FLIES_HTML,
    POPULAR_HTML,
    COUNTRY_LIST_ITEMS_HTML
}
