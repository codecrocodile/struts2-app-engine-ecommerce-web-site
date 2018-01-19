/*
 * Page.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.structures;

import java.io.Serializable;

/**
 * @author Chris Hatton
 */
public class Page implements Serializable {
    
    private static final long serialVersionUID = 8602440622457723772L;

    private int pageId;
    
    private String urlAlias;
    
    private String title;
    
    private String metaKeywords;
    
    private String metaDescription;
    
    private String html; // TODO remove this and replace with block elements that will be used in the page

    /**
     * Constructor
     */
    public Page() {
        super();
    }
    
    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }
    
    public String getUrlAlias() {
        return urlAlias;
    }

    public void setUrlAlias(String urlAlias) {
        this.urlAlias = urlAlias;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMetaKeywords() {
        return metaKeywords;
    }

    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
