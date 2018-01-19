/*
 * PostageServiceIF.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.service.postage;

import com.groovyfly.admin.postage.PostalZone;

/**
 * @author Chris Hatton
 */
public interface PostageServiceIF {
    
    /**
     * This gets the postage zone.
     * 
     * @param postalCourierCode
     *   The code to represent the postal courier e.g. Royal Mail etc...
     * @param countryCode
     *   The 2 letter ISO 3166 code to represent the country.       
     * @return PostalZone
     * @throws Exception
     */
    public PostalZone getPostageZone(String postalCourierCode, String countryCode) throws Exception;
    
    /**
     * Tells you if the country is within the EU.
     * 
     * @param countryCode
     *  The 2 letter ISO 3166 code to represent the country.
     * @return true if within the EU, false otherwise.
     * @throws Exception
     */
    public boolean isEuropeanUnionCountry(String countryCode) throws Exception;
    

}
