/*
 * NotEnoughStockException.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.structures.exceptions;

/**
 * This exception is to be used when there is not enough products in stock for the operation 
 * that is being carried out.
 *  
 * @author Chris Hatton
 */
@SuppressWarnings("serial")
public class NotEnoughStockException extends Exception {
    
    private int quantityLeft;
    
    private String productItemName;
    
    /**
     * Constructor
     */
    public NotEnoughStockException(String message) {
        super(message);
    }
    
    /**
     * Constructor
     */
    public NotEnoughStockException(String message, int quantityLeft, String productItemName) {
        super(message);
        this.quantityLeft = quantityLeft;
        this.productItemName = productItemName;
    }

    public int getQuantityLeft() {
        return quantityLeft;
    }

    public void setQuantityLeft(int quantityLeft) {
        this.quantityLeft = quantityLeft;
    }

    public String getProductItemName() {
        return productItemName;
    }

    public void setProductItemName(String productItemName) {
        this.productItemName = productItemName;
    }

}
