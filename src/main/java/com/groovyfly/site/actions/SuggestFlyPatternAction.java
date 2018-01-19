/*
 * CustomerSuggestAction.java
 * 
 * Copyright (c) 2013 Groovy Fly
 */
package com.groovyfly.site.actions;

import com.groovyfly.site.service.datacollection.CustomerSuggestionServiceIF;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Chris Hatton
 * 
 * Created 26 May 2013
 */
public class SuggestFlyPatternAction extends BaseSiteAction {

    private static final long serialVersionUID = -5194433834637939740L;
    
    private CustomerSuggestionServiceIF customerSuggestionServiceIF;
    
    private String flyPatternList;
    
    private double XCoord = -999;
    
    private double YCoord = -999;

    public void setCustomerSuggestionServiceIF(CustomerSuggestionServiceIF customerSuggestionServiceIF) {
        this.customerSuggestionServiceIF = customerSuggestionServiceIF;
    }
    
    /* 
     * @see com.opensymphony.xwork2.ActionSupport#execute()
     */
    @Override
    public String execute() throws Exception {
        flyPatternList = customerSuggestionServiceIF.getFlyPatternAutoCompleteString();
        
        return ActionSupport.SUCCESS;
    }
    
    public String getFlyPatternList() {
        return flyPatternList;
    }
    
    public void setFlyPatternList(String flyPatternList) {
        this.flyPatternList = flyPatternList;
    }

    public double getXCoord() {
        return XCoord;
    }

    public void setXCoord(double xCoord) {
        XCoord = xCoord;
    }

    public double getYCoord() {
        return YCoord;
    }

    public void setYCoord(double yCoord) {
        YCoord = yCoord;
    }


}
