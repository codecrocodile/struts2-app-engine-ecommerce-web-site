/*
 * CommonSiteServiceIF.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.service;

import com.groovyfly.common.structures.Page;


/**
 * @author Chris Hatton
 */
public interface CommonSiteServiceIF {
    
    public Page getPage(String subjectTable, String urlAlias) throws Exception; 
    
    public String getSiteMenuHtml() throws Exception;
    
    public String getFliesForMonthHtml() throws Exception;
    
    public String getPopularFliesHtml() throws Exception;
    
    public String getCollectionListItemsHtml() throws Exception;
    
}
