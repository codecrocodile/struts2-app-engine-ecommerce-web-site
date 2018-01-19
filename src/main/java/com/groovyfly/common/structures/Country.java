/*
 * Country.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.structures;

/**
 * Countries are based on the ISO 3166 standard. The Code instance variable is
 * the alpha-2 country code. Note that these countries and codes are subject to
 * change over time.
 * 
 * @author Chris Hatton
 */
public class Country extends Lookup {

    private String code;

    /**
     * Constructor
     */
    public Country() {
        super();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    /* 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Country) {
            Country other = (Country) obj;
            if(other.code.equalsIgnoreCase(this.code)) {
                return true;
            }
        }
        
        return false;
    }
    
    /* 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.code.hashCode();
    }
}
