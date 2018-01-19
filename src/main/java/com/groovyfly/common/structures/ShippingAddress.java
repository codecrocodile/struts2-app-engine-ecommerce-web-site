/*
 * CustomerAddress.java
 * 
 * Copyright 2012 Groovy Fly
 */
package com.groovyfly.common.structures;

/**
 * @author Chris Hatton
 */
public class ShippingAddress extends Address {

    private static final long serialVersionUID = 1378726439807624036L;

    /** Person�s name associated with this shipping address */
    private String name;
    
    /** 2 letter ISO 3166 code for the country */
    private String countryCode;
    
    private String addressStatus;
    
    /**
     * Constructor
     */
    public ShippingAddress() {
        super();
    }

    public String getName() {
        return name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressStatus() {
        return addressStatus;
    }

    public void setAddressStatus(String addressStatus) {
        this.addressStatus = addressStatus;
    }
    
}
