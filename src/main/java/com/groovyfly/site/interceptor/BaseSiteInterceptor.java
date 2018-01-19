/*
 * BaseSiteInterceptor.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.interceptor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.groovyfly.common.structures.Page;
import com.groovyfly.common.structures.ShoppingCart;
import com.groovyfly.common.util.SessionKey;
import com.groovyfly.site.actions.BaseSiteAction;
import com.groovyfly.site.service.CommonSiteServiceIF;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * This interceptor does all the common processing that should be done on each http request. Remember that the whole
 * of the site is a shopping site (even when user is just reading articles) so processing related to that must be done 
 * on each request.
 * <p>
 * The data resulting from this processing will be set on the BaseSiteAction that all actions created for the site will
 * extend.
 * <p>
 * The expected things to do done in this class include:
 * 
 * <li>Collecting cookies and any appropriate processing
 * <li>Set common data on BaseSiteAction e.g. menu html, flies of the month, popular flies, etc..
 * <li>Set cart and item data on BaseSiteAction if the user has started shopping
 * 
 * @author Chris Hatton
 */
public class BaseSiteInterceptor extends AbstractInterceptor {
    
    private static Logger log = Logger.getLogger(BaseSiteInterceptor.class.getName());
    
    private static final long serialVersionUID = -4599367523428417821L;
    
    private CommonSiteServiceIF commonSiteServiceIF;
    
    public void setCommonSiteServiceIF(CommonSiteServiceIF commonSiteServiceIF) {
        this.commonSiteServiceIF = commonSiteServiceIF;
    }

    /* 
     * @see com.opensymphony.xwork2.interceptor.AbstractInterceptor#intercept(com.opensymphony.xwork2.ActionInvocation)
     */
    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        log.info("intercept(ActionInvocation invocation)");
        
        try {
            BaseSiteAction action = (BaseSiteAction) invocation.getAction();
            action.setCommonSiteServiceIF(commonSiteServiceIF);

            // set shopping cart if there is one
            Map<String, Object> session = invocation.getInvocationContext().getSession();
            
            Object object = session.get(SessionKey.SHOPPING_CART);
            if (object != null) {
                ShoppingCart shoppingCart = (ShoppingCart) object;
                action.setShoppingCart(shoppingCart);
            }
            
            // get the common stuff for all the pages
            String menuHtml = commonSiteServiceIF.getSiteMenuHtml();
            action.setMenuHtml(menuHtml);
            
            String fliesByMonthHtml = commonSiteServiceIF.getFliesForMonthHtml();
            action.setFliesByMonthHtml(fliesByMonthHtml);
            
            String popularFliesHtml = commonSiteServiceIF.getPopularFliesHtml();
            action.setPopularFliesHtml(popularFliesHtml);
            
            HttpServletRequest request = ServletActionContext.getRequest();
            String urlAlias = this.parseSeoFriendyParameters(action, request.getServletPath());
            if (urlAlias.endsWith("/")) {
                urlAlias = urlAlias.substring(0, urlAlias.length() -1);
            }
            
            action.setUrlAlias(urlAlias);
            
            Page page = this.commonSiteServiceIF.getPage("page", urlAlias);
            action.setPage(page);
            
            
            String invoke = invocation.invoke();
            
            // post processing
            // if we had a shopping cart object to set on the action then it might have been modified. we need to set this 
            // again because of GAE, so that it will store it properly again (it will be serialised, threfore will be a diffenrt object instance)
            if (object != null) {
                session.put(SessionKey.SHOPPING_CART.toString(), action.getShoppingCart());    
            }
            
            return invoke;
            
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log.severe(sw.toString());
            
            throw e;
        }
    }
    
    /*
     * Parses seo friendly parameters and returns the page url alias.
     */
    private String parseSeoFriendyParameters(BaseSiteAction action, String servletPath) {   
        
        String urlAlias = null;
        String paramString = null;
        if (servletPath.contains("/page-")) { // so we expect all urls with parameters to start with the page parameter
            urlAlias = servletPath.substring(0, servletPath.indexOf("/page-"));
            paramString = servletPath.substring(servletPath.indexOf("/page-") -1, servletPath.length());
            parseParamString(action, paramString);
        } else {
            urlAlias = servletPath;
        }
        
        return urlAlias;
    }
    
    private void parseParamString(BaseSiteAction action, String paramString) {
        String[] paramStrings = paramString.split("/");
        for (int i = 0; i < paramStrings.length; i++) {
            if (paramStrings[i].trim().startsWith("page-")) {
                int page = Integer.valueOf(paramStrings[i].substring(5));
                action.getPageParameters().put("page", page);
                break;
            }
        }
    }
}
