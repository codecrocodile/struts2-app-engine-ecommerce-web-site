/*
 * BaseAdminAction.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.actions;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Chris Hatton
 * 
 * Created 29 Dec 2012
 */
public class BaseAdminAction extends ActionSupport {

    private static final long serialVersionUID = 3983400037232386441L;
    
    private String principleName;
    
    private String logoutUrl;

    public String getPrincipleName() {
        return principleName;
    }

    public void setPrincipleName(String principleName) {
        this.principleName = principleName;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

}
