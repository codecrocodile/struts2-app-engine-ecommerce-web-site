/*
 * CustomerSuggestion.java
 * 
 * Copyright (c) 2013 Groovy Fly
 */
package com.groovyfly.site.service.datacollection;

import java.util.List;

/**
 * @author Chris Hatton
 * 
 * Created 15 Sep 2013
 */
public interface CustomerSuggestionServiceIF {
    
    public String getFlyPatternAutoCompleteString() throws Exception;
    
    public void saveFlyPatternSuggestion(double XCoord, double YCoord, List<String> patternNames, String name, String email) throws Exception;
    
    public List<String> getMostPopular(int limit) throws Exception;
    
}
