/*
 * LinkDirectoryServiceIF.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.service.linkdirectory;

import java.util.List;

import com.groovyfly.common.structures.linkdirectory.Website;
import com.groovyfly.common.structures.linkdirectory.WebsiteCategory;

/**
 * @author Chris Hatton
 */
public interface LinkDirectoryServiceIF {
    
    public List<Website> getLatestAddedWebsites(int limit) throws Exception;
    
    public List<WebsiteCategory> getRootCategories() throws Exception;
    
    public void saveWebsite(Website website) throws Exception;
    
    public WebsiteCategory getWebsiteCategory(String urlAlias) throws Exception;
    
}
