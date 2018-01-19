/*
 * DiscountException.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.sales;

/**
 * @author Chris Hatton
 */
public class DiscountException extends Exception {

    private static final long serialVersionUID = -5721610077874811704L;
    
    /**
     * Constructor
     */
    public DiscountException(String message) {
        super(message);
    }

}
