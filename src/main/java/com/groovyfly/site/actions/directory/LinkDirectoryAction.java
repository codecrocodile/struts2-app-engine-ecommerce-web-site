/*
 * DirectoryAction.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.actions.directory;

import com.groovyfly.common.structures.linkdirectory.WebsiteCategory;
import com.groovyfly.site.actions.BaseSiteAction;
import com.groovyfly.site.service.linkdirectory.LinkDirectoryServiceIF;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Chris Hatton
 */
public class LinkDirectoryAction extends BaseSiteAction {

    private static final long serialVersionUID = -4980525067932812272L;
    
    private LinkDirectoryServiceIF linkDirectoryServiceIF;

    private WebsiteCategory websiteCategory;

    public void setLinkDirectoryServiceIF(LinkDirectoryServiceIF linkDirectoryServiceIF) {
        this.linkDirectoryServiceIF = linkDirectoryServiceIF;
    }

    /* 
     * @see com.opensymphony.xwork2.ActionSupport#execute()
     */
    @Override
    public String execute() throws Exception {
        this.websiteCategory = linkDirectoryServiceIF.getWebsiteCategory(super.getUrlAlias());
        
        return ActionSupport.SUCCESS;
    }

    public WebsiteCategory getWebsiteCategory() {
        return websiteCategory;
    }

}
