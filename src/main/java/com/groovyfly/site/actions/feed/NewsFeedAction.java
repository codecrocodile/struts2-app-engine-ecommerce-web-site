/*
 * RssAction.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.actions.feed;

import java.util.List;

import com.groovyfly.site.service.feed.NewsFeedServiceIF;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;

/**
 * @author Chris Hatton
 * 
 * Created 31 Aug 2012
 */
public class NewsFeedAction extends ActionSupport {

    private static final long serialVersionUID = 8929740591595850745L;
    
    private NewsFeedServiceIF newsFeedServiceIF;

    private SyndFeed siteNewsFeed;
    
    public void setNewsFeedServiceIF(NewsFeedServiceIF newsFeedServiceIF) {
        this.newsFeedServiceIF = newsFeedServiceIF;
    }

    /* 
     * @see com.opensymphony.xwork2.ActionSupport#execute()
     */
    @Override
    public String execute() throws Exception {
        
        SyndFeed feed = new SyndFeedImpl();
        feed.setTitle("GroovyFly.com Groovy Fly News");
        feed.setLink("http://www.groovyfly.com");
        feed.setDescription("The latest news on fishing flies for sale; fly fishing articles and Groovy Fly website updates.");
        feed.setLanguage("en-gb");

        List<SyndEntry> entries = this.newsFeedServiceIF.getSyndEntries(false);
        feed.setEntries(entries);

        this.siteNewsFeed = feed;
        
        return Action.SUCCESS;
    }

    public SyndFeed getSiteNewsFeed() {
        return siteNewsFeed;
    }

    public void setSiteNewsFeed(SyndFeed siteNewsFeed) {
        this.siteNewsFeed = siteNewsFeed;
    }

}
