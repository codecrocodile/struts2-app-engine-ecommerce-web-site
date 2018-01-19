/*
 * Website.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.structures.linkdirectory;

import java.util.Date;

/**
 * @author Chris Hatton
 */
public class Website {
    
    private int websiteId;
    
    private String websiteUrl;
    
    private boolean followLink;
    
    private int categoryId;
    
    private String bannerOrLogoUrl;
    
    private String title;
    
    private String description;
    
    private String backlinkUrl;
    
    private String contactEmail;
    
    private Date dateAdded;
    
    /**
     * Constructor
     */
    public Website() {
        super();
    }
    
    public int getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(int websiteId) {
        this.websiteId = websiteId;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public boolean isFollowLink() {
        return followLink;
    }

    public void setFollowLink(boolean followLink) {
        this.followLink = followLink;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getBannerOrLogoUrl() {
        return bannerOrLogoUrl;
    }

    public void setBannerOrLogoUrl(String bannerOrLogoUrl) {
        this.bannerOrLogoUrl = bannerOrLogoUrl;
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
        this.backlinkUrl = backlinkUrl;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }
    
}
