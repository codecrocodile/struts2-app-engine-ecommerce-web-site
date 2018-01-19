/*
 * HomePageAction.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.actions;

import com.opensymphony.xwork2.Action;

/**
 * @author Chris Hatton
 * 
 * Created 25 Aug 2012
 */
public class HomePageAction extends BaseSiteAction {

    private static final long serialVersionUID = 6063707318244394237L;
    
    private String countryListItemsHtml;

    public String execute() throws Exception {
        this.countryListItemsHtml = super.commonSiteServiceIF.getCollectionListItemsHtml();
        return Action.SUCCESS;
    }
    


    public String getCountryListItemsHtml() {
        return countryListItemsHtml;
    }
}
