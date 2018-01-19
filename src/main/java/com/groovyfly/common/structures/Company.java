/*
 * Company.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.structures;

import java.io.Serializable;

/**
 * @author Chris Hatton
 * 
 * Created 5 Dec 2012
 */
public class Company implements Serializable {

    private static final long serialVersionUID = 3170668460454750284L;

    private String name;
    
    private Address address;
    
    private String email;
    
    private String websiteUrl;
    
    private String phonenNumber;
    
    private boolean isVatRegistered;
    
    private String vatNumber;
    
    /**
     * Constructor
     */
    public Company() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getPhonenNumber() {
        return phonenNumber;
    }

    public void setPhonenNumber(String phonenNumber) {
        this.phonenNumber = phonenNumber;
    }

    public boolean isVatRegistered() {
        return isVatRegistered;
    }

    public void setVatRegistered(boolean isVatRegistered) {
        this.isVatRegistered = isVatRegistered;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }
}
