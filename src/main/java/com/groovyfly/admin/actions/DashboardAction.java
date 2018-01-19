/*
 * DashboardAction.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.actions;

import com.groovyfly.common.interfaces.UserAware;
import com.groovyfly.common.structures.User;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author Chris Hatton
 * 
 *         Created 21 Jun 2012
 */
public class DashboardAction extends BaseAdminAction implements UserAware, ModelDriven<User> {

    private static final long serialVersionUID = 7502556144966950244L;

    private User user;

    /*
     * @see
     * com.groovyfly.common.interfaces.UserAware#setUser(com.groovyfly.common
     * .structures.User)
     */
    @Override
    public void setUser(User user) {
        this.user = user;
    }

    /*
     * @see com.opensymphony.xwork2.ActionSupport#execute()
     */
    @Override
    public String execute() throws Exception {

        return Action.SUCCESS;
    }

    /*
     * @see com.opensymphony.xwork2.ModelDriven#getModel()
     */
    @Override
    public User getModel() {
        return user;
    }
}
