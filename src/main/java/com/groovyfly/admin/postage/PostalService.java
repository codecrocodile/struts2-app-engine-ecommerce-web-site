/*
 * PostageService.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.postage;

/**
 * @author Chris Hatton
 */
public class PostalService {
    
    private String postalServiceCode;
    
    private String postalCourierCode;
    
    private String postalServiceName;
    
    // might add information such as delivery aim (working days), max/min weight and sizes and also tracking information
    
    /**
     * Constructor
     */
    public PostalService() {
        super();
    }

    public String getPostalServiceCode() {
        return postalServiceCode;
    }

    public void setPostalServiceCode(String postalServiceCode) {
        this.postalServiceCode = postalServiceCode;
    }

    public String getPostalCourierCode() {
        return postalCourierCode;
    }

    public void setPostalCourierCode(String postalCourierCode) {
        this.postalCourierCode = postalCourierCode;
    }

    public String getPostalServiceName() {
        return postalServiceName;
    }

    public void setPostalServiceName(String postalServiceName) {
        this.postalServiceName = postalServiceName;
    }
  
}
