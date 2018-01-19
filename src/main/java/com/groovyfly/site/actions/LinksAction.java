/*
 * LinksAction.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.actions;

import java.util.List;
import java.util.logging.Logger;

import org.apache.struts2.interceptor.validation.SkipValidation;

import com.groovyfly.admin.sitemanagement.BacklinkReporter;
import com.groovyfly.admin.sitemanagement.BacklinkReporter.BacklinkStatus;
import com.groovyfly.common.structures.linkdirectory.Website;
import com.groovyfly.common.structures.linkdirectory.WebsiteCategory;
import com.groovyfly.site.service.linkdirectory.LinkDirectoryServiceIF;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.Preparable;

/**
 * @author Chris Hatton
 */
public class LinksAction extends BaseSiteAction implements Preparable {
    
    private static final long serialVersionUID = 5600353089619260840L;
    
    private static Logger log = Logger.getLogger(LinksAction.class.getName());
    
    private LinkDirectoryServiceIF linkDirectoryServiceIF;
    
    private List<Website> lastAddedWebsites;
    
    private List<WebsiteCategory> rootWebsiteCategories;
    
    private int categoryId;
    
    private String websiteUrl;
    
    private String title;
    
    private String description;
    
    private String backlinkUrl;
    
    private String contactEmail;
    
    private String addedMessage;

    public void setLinkDirectoryServiceIF(LinkDirectoryServiceIF linkDirectoryServiceIF) {
        this.linkDirectoryServiceIF = linkDirectoryServiceIF;
    }
    
    /* 
     * @see com.opensymphony.xwork2.Preparable#prepare()
     */
    @Override
    public void prepare() throws Exception {
        log.info("prepare");
        try {
            this.lastAddedWebsites = linkDirectoryServiceIF.getLatestAddedWebsites(3);
            this.rootWebsiteCategories = linkDirectoryServiceIF.getRootCategories();   
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    @SkipValidation
    public String list() throws Exception {
        return Action.SUCCESS;
    }
    
    public String add() throws Exception {
        try {
            
            boolean followLink = false;
            
            if (this.backlinkUrl != null && !this.backlinkUrl.trim().equals("")) {
                BacklinkReporter r = new BacklinkReporter();
                BacklinkStatus backlinkStatus = r.getBacklinkStatus(websiteUrl);
                log.info(backlinkStatus.toString());
                switch(backlinkStatus) {
                case FOUND:
                    this.addedMessage = 
                        "Thank you, a backlink to Groovy Fly was found and your listing was successfully added. " +
                        "You will now receive the full benefits of a listing with Groovy Fly. We will check the status " +
                        "of this link periodically.";
                    followLink = true;
                    break;
                case FOUND_WITH_NO_FOLLOW:
                    this.addedMessage = 
                        "Thank you, a backlink to Groovy Fly was found and your listing was successfully added. " +
                        "We found the backlink with a nofollow attribute. A nofollow attribute will be added to your listing. " +
                        "We will check the status of this link periodically. If you remove the nofollow attribute you " +
                        "will receive the full benefits of a listing with Groovy Fly.";
                    break;
                case NOT_FOUND:
                    this.addedMessage = 
                        "Thank you, your listing was successfully added. We did not find a backlink therfore a nofollow " +
                        "attribute will be added to your listing. We will check the status of this link periodically. If you " +
                        "add a backlink without a nofollow attribute you will receive the full benefits of a listing with Groovy Fly.";
                    break;
                }
            } else {
                
                log.info("no backlinkurl");
                this.addedMessage = "";
            }
            
            Website website = new Website();
            website.setWebsiteUrl(this.websiteUrl);
            website.setCategoryId(this.categoryId);
            website.setBannerOrLogoUrl(null);
            website.setTitle(this.title);
            website.setDescription(this.description);
            website.setBacklinkUrl(this.backlinkUrl);
            website.setContactEmail(this.contactEmail);
            website.setFollowLink(followLink);
            
            linkDirectoryServiceIF.saveWebsite(website);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        categoryId = 0;
        websiteUrl = null;
        title = null;
        description = null;
        backlinkUrl = null;
        contactEmail = null;
        
        prepare();

        return Action.SUCCESS;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        if (!websiteUrl.trim().equals("") && !websiteUrl.startsWith("http://") && !websiteUrl.startsWith("https://")) {
            this.websiteUrl = "http://" + websiteUrl;
        } else {
            this.websiteUrl = websiteUrl;    
        }
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBacklinkUrl() {
        return backlinkUrl;
    }

    public void setBacklinkUrl(String backlinkUrl) {
        if (!backlinkUrl.trim().equals("") && !backlinkUrl.startsWith("http://") && !backlinkUrl.startsWith("https://")) {
            this.backlinkUrl = "http://" + backlinkUrl;
        } else {
            this.backlinkUrl = backlinkUrl;    
        }
    }

    public List<Website> getLastAddedWebsites() {
        return lastAddedWebsites;
    }

    public List<WebsiteCategory> getRootWebsiteCategories() {
        return rootWebsiteCategories;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getAddedMessage() {
        return addedMessage;
    }

    public void setAddedMessage(String addedMessage) {
        this.addedMessage = addedMessage;
    }
}
