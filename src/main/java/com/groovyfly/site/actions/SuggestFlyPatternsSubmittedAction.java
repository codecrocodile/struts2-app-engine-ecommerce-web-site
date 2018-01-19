/*
 * SuggestFlyPatternSubmittedAction.java
 * 
 * Copyright (c) 2013 Groovy Fly
 */
package com.groovyfly.site.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.groovyfly.site.service.datacollection.CustomerSuggestionServiceIF;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * @author Chris Hatton
 */
public class SuggestFlyPatternsSubmittedAction extends BaseSiteAction implements Preparable {
    
    private static Logger log = Logger.getLogger(SuggestFlyPatternsSubmittedAction.class.getName());

    private static final long serialVersionUID = 5984820088268142786L;
    
    private transient CustomerSuggestionServiceIF customerSuggestionServiceIF;
    
    private String flyPatternList;
    
    private double XCoord = -999;
    
    private double YCoord = -999;
    
    private List<String> patternNames = new ArrayList<String>();
    
    private String name;
    
    private String email;

    private List<String> mostPopular = new ArrayList<String>();
    
    public void setCustomerSuggestionServiceIF(CustomerSuggestionServiceIF customerSuggestionServiceIF) {
        this.customerSuggestionServiceIF = customerSuggestionServiceIF;
    }
    
    /* 
     * @see com.opensymphony.xwork2.ActionSupport#execute()
     */
    @Override
    public String execute() throws Exception {
        
        try {
            customerSuggestionServiceIF.saveFlyPatternSuggestion(XCoord, YCoord, patternNames, name, email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        prepResults();
        
        return ActionSupport.SUCCESS;
    }
    
    private void prepResults() {
        try {
            mostPopular  = customerSuggestionServiceIF.getMostPopular(5);
            
            log.info("most popular  = " + mostPopular.size() );
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
    
    /* 
     * @see com.opensymphony.xwork2.Preparable#prepare()
     */
    @Override
    public void prepare() throws Exception {
        flyPatternList = customerSuggestionServiceIF.getFlyPatternAutoCompleteString();
    }
    
    /* 
     * @see com.opensymphony.xwork2.ActionSupport#validate()
     */
    @Override
    public void validate() {
        boolean hasOnePatternValid = false;
        for (String patternName : patternNames) {
            if (patternName != null && patternName.trim().length() > 3) {
                hasOnePatternValid = true;
                break;
            }
        }
        
        if (!hasOnePatternValid) {
            this.addFieldError("patternNames", "Please supply at least one fly pattern please.");
        }
    }
    
    public boolean isSinglePatternSubmitted() {
        boolean isSinglePatternSubmitted = true;
        int cnt = 0;
        for (String s : patternNames) {
            if (s != null && s.trim().length() > 3) {
                cnt++;
            }
            
            if (cnt > 1) {
                isSinglePatternSubmitted = false;
                break;
            }
        }
        return isSinglePatternSubmitted;        
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

    public List<String> getPatternNames() {
        return patternNames;
    }

    public void setPatternNames(List<String> patternNames) {
        this.patternNames = patternNames;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getMostPopular() {
        return mostPopular;
    }

    public void setMostPopular(List<String> mostPopular) {
        this.mostPopular = mostPopular;
    }
}
