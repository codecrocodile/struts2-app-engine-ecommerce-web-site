/*
 * BaseAdminInterceptor.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.interceptors;

import java.security.Principal;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.groovyfly.admin.actions.BaseAdminAction;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * @author Chris Hatton
 * 
 * Created 21 Jun 2012
 */
public class BaseAdminInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = 2727051841414657730L;
    
    private static Logger log = Logger.getLogger(BaseAdminInterceptor.class.getName());

    /*
     * @seecom.opensymphony.xwork2.interceptor.Interceptor#intercept(com.
     * opensymphony.xwork2.ActionInvocation)
     */
    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
    	
    	log.info("BaseSiteInteceptorCalled");
    	
        try {
        	
        	log.info("invocation action class = " + invocation.getAction().getClass());
        	
            BaseAdminAction action = (BaseAdminAction) invocation.getAction();
            
            HttpServletRequest request = ServletActionContext.getRequest();
            Principal userPrincipal = request.getUserPrincipal();
            action.setPrincipleName(userPrincipal.getName());
            UserService userService = UserServiceFactory.getUserService();
            action.setLogoutUrl(userService.createLogoutURL(request.getRequestURI()));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    
        return invocation.invoke();
        
        
        
        
//        Map<String, Object> session = invocation.getInvocationContext().getSession();
//        User user = (User) session.get(SessionKey.USER.toString());
//
//        String actionName = invocation.getProxy().getActionName();
//
//        if (user == null && (!actionName.equals("admin-login") && !actionName.equals("login"))) { // TODO
//                                                                                                  // add
//                                                                                                  // these
//                                                                                                  // actions
//                                                                                                  // as
//                                                                                                  // params
//            return Action.LOGIN;
//        } else {
//            Action action = (Action) invocation.getAction();
//            if (action instanceof UserAware) {
//                ((UserAware) action).setUser(user);
//            }
//
//            return invocation.invoke();
//        }
    }
}
