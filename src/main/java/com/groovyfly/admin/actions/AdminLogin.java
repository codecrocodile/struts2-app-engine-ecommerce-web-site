/*
 * AdminLoginPageAction.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.actions;

import java.util.Map;
import java.util.logging.Logger;

import org.apache.struts2.interceptor.SessionAware;

import com.groovyfly.common.util.SessionKey;
import com.opensymphony.xwork2.Action;

/**
 * @author Chris Hatton
 */
public class AdminLogin extends BaseAdminAction implements SessionAware {
    
    private static Logger log = Logger.getLogger(AdminLogin.class.getName());

    private static final long serialVersionUID = -7198308033413190313L;

    private static final String NOT_REQUIRED = "Not_Required";

    private Map<String, Object> sessionMap;

    /*
     * @see
     * org.apache.struts2.interceptor.SessionAware#setSession(java.util.Map)
     */
    @Override
    public void setSession(Map<String, Object> session) {
        this.sessionMap = session;
    }

    /*
     * @see com.opensymphony.xwork2.ActionSupport#execute()
     */
    @Override
    public String execute() throws Exception {
        if (this.sessionMap.get(SessionKey.USER) != null) {
            
            log.info("user found in session");
            
            return NOT_REQUIRED;
        }
        
        log.info("user not found in session");

        return Action.SUCCESS;
    }

}
