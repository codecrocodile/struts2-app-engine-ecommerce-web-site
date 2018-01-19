/*
 * PostageZone.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.postage;

import java.util.List;

import com.groovyfly.common.structures.Country;

/**
 * @author Chris Hatton
 */
public class PostalZone {
    
    private String zoneCode;
    
    private String zoneDescription;
    
    private List<Country> countries;
    
    /**
     * Constructor
     */
    public PostalZone() {
        super();
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getZoneDescription() {
        return zoneDescription;
    }

    public void setZoneDescription(String zoneDescription) {
        this.zoneDescription = zoneDescription;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }
    
    /**
     * Checks to see if the country in within this postage zone.
     */
    public boolean isCountryInZone(Country country) {
        for (Country c : countries) {
            if (country.equals(c)) {
                return true;
            }
        }
        return false;
    }

}
