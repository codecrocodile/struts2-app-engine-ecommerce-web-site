/*
 * ProductAttributes.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.structures;

import java.util.List;


/**
 * @author Chris Hatton
 * 
 * Created 30 Jul 2012
 */
public class ProductAttribute {

    private int id;
    
    private String description;
    
    private ProductAttributeValue choosenAttributeValue;

    private List<ProductAttributeValue> productAttributeValues;
    
    /**
     * Constructor
     */
    public ProductAttribute() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ProductAttributeValue> getProductAttributeValues() {
        return productAttributeValues;
    }

    public void setProductAttributeValues(List<ProductAttributeValue> productAttributeValues) {
        this.productAttributeValues = productAttributeValues;
    }
    
    public ProductAttributeValue getChoosenAttributeValue() {
        return choosenAttributeValue;
    }
    

    public void setChoosenAttributeValue(ProductAttributeValue choosenAttributeValue) {
        this.choosenAttributeValue = choosenAttributeValue;
    }
    
    /* 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String stringVal = this.description + ":"  + choosenAttributeValue.getDescription();
        
        return stringVal;
    }
    

    /* 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProductAttribute) {
            ProductAttribute other = (ProductAttribute) obj;
            return id == other.id;
        }
        
        return false;
    }
    
    /* 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return id;
    }
    
}
