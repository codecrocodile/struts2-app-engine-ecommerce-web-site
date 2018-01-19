/*
 * WebsiteCategory.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.structures.linkdirectory;

import java.util.List;

/**
 * @author Chris Hatton
 */
public class WebsiteCategory {

    private int websiteCategoryId;
    
    private String urlAlias;
    
    private String name;
    
    private String description;
    
    private int websiteCount;
    
    private List<Website> websites;
    
    /**
     * Constructor
     */
    public WebsiteCategory() {
        super();
    }

    public int getWebsiteCategoryId() {
        return websiteCategoryId;
    }

    public void setWebsiteCategoryId(int websiteCategoryId) {
        this.websiteCategoryId = websiteCategoryId;
    }

    public String getUrlAlias() {
        return urlAlias;
    }

    public void setUrlAlias(String urlAlias) {
        this.urlAlias = urlAlias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWebsiteCount() {
        return websiteCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWebsiteCount(int websiteCount) {
        this.websiteCount = websiteCount;
    }

    public List<Website> getWebsites() {
        return websites;
    }

    public void setWebsites(List<Website> websites) {
        this.websites = websites;
    }
    
    /* 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return name;
    }
    
}
