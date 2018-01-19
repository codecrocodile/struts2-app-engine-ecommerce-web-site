/*
 * ProductAttributeValue.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.structures;

/**
 * @author Chris Hatton
 * 
 * Created 30 Jul 2012
 */
public class ProductAttributeValue {
    
    private int id;
    
    private String description;
    
    private boolean defaultChoosen;

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
    
    public boolean isDefaultChoosen() {
        return defaultChoosen;
    }
    

    public void setDefaultChoosen(boolean defaultChoosen) {
        this.defaultChoosen = defaultChoosen;
    }
    

    /* 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return description;
    }
    
    /* 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProductAttributeValue) {
            ProductAttributeValue other = (ProductAttributeValue) obj;
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
