/*
 * SiteAction.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.actions;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.groovyfly.common.structures.Page;
import com.groovyfly.common.structures.ShoppingCart;
import com.groovyfly.site.service.CommonSiteServiceIF;
import com.opensymphony.xwork2.ActionSupport;

/**
 * This works in conjunction with BaseSiteInterceptor to set various fields on instances of this object.
 * 
 * @author Chris Hatton
 */
public abstract class BaseSiteAction extends ActionSupport {

    private static final long serialVersionUID = -5089068833532625317L;
    
    protected transient CommonSiteServiceIF commonSiteServiceIF;
    
    protected String menuHtml;
    
    protected String fliesByMonthHtml;
    
    protected String popularFliesHtml;
    
    protected ShoppingCart shoppingCart = new ShoppingCart("", Locale.UK);
    
    protected String urlAlias;
    
    protected Page page;
    
    /** These are the page parameters we have encoded in an seo friendly way e.g. /page-1 for paging info */
    protected Map<String, Object> pageParameters = new HashMap<String, Object>();
    
    /**
     * Constructor
     */
    public BaseSiteAction() {
        super();
    }

    public CommonSiteServiceIF getCommonSiteServiceIF() {
        return commonSiteServiceIF;
    }

    public void setCommonSiteServiceIF(CommonSiteServiceIF commonSiteServiceIF) {
        this.commonSiteServiceIF = commonSiteServiceIF;
    }
    
    public void retrievePage(String subjectTable) throws Exception {
        this.page = commonSiteServiceIF.getPage(subjectTable, urlAlias);
    }

    public String getMenuHtml() {
        return menuHtml;
    }

    public void setMenuHtml(String menuHtml) {
        this.menuHtml = menuHtml;
    }
    
    public String getFliesByMonthHtml() {
        return fliesByMonthHtml;
    }

    public void setFliesByMonthHtml(String fliesByMonthHtml) {
        this.fliesByMonthHtml = fliesByMonthHtml;
    }

    public String getPopularFliesHtml() {
        return popularFliesHtml;
    }

    public void setPopularFliesHtml(String popularFliesHtml) {
        this.popularFliesHtml = popularFliesHtml;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }
    
    public String getUrlAlias() {
        return urlAlias;
    }

    public void setUrlAlias(String urlAlias) {
        this.urlAlias = urlAlias;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Map<String, Object> getPageParameters() {
        return pageParameters;
    }

    public void setPageParameters(Map<String, Object> pageParameters) {
        this.pageParameters = pageParameters;
    }
}
