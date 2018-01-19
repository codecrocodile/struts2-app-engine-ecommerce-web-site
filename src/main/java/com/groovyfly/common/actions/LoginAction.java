/*
 * LoginAction.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.actions;

import java.util.Map;
import java.util.logging.Logger;

import org.apache.struts2.interceptor.SessionAware;

import com.groovyfly.admin.actions.BaseAdminAction;
import com.groovyfly.common.service.AuthenticationService;
import com.groovyfly.common.structures.User;
import com.groovyfly.common.util.SessionKey;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Chris Hatton
 * 
 *         Created 2 Jun 2012
 */
public class LoginAction extends BaseAdminAction implements SessionAware {

    private static final long serialVersionUID = -7870175969432273196L;

    private static Logger log = Logger.getLogger(LoginAction.class.getName());

    private Map<String, Object> sessionMap;

    private String username;

    private String password;

    /*
     * @see
     * org.apache.struts2.interceptor.SessionAware#setSession(java.util.Map)
     */
    @Override
    public void setSession(Map<String, Object> session) {
        this.sessionMap = session;
    }

    /*
     * @see com.opensymphony.xwork2.ActionSupport#validate()
     */
    @Override
    public void validate() {

        if (this.getUsername() == null || this.getUsername().trim().equals("")) {
            this.addFieldError("username", "Username is required.");
        }

        if (this.getPassword() == null || this.getPassword().trim().equals("")) {
            this.addFieldError("password", "Password is required.");
        }
    }

    /*
     * @see com.opensymphony.xwork2.ActionSupport#execute()
     */
    @Override
    public String execute() throws Exception {
        log.info("execute() LoginAction m");

        AuthenticationService as = new AuthenticationService();
        User u = as.authenticateUser(username, password);
        
        log.info("user set " + u);

        if (u == null) {
            
            log.info("return to input");
            
            return ActionSupport.INPUT;
        } else {
            
            try {
                sessionMap.put(SessionKey.USER.toString(), u);    
            } catch (Exception e) {
                e.printStackTrace();
            }
            

            log.info("success");
            
            return ActionSupport.SUCCESS;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
