/*
 * NewsFeedServiceIF.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.service.feed;

import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;

/**
 * @author Chris Hatton
 * 
 * Created 31 Aug 2012
 */
public interface NewsFeedServiceIF {

    public List<SyndEntry> getSyndEntries(boolean includeRetired) throws Exception;
    
    public void saveSyndEntry(SyndEntry syndEntry) throws Exception;
    
    public void retireSyndEntry(int syndEntryId) throws Exception;
    
}
