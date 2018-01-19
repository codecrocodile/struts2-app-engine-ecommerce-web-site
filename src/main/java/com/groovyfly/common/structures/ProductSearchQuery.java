/*
 * ProductSearchQuery.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.structures;


/**
 * @author Chris Hatton
 */
public class ProductSearchQuery {

    public String name;
    
    public int categoryId;
    
    private int attributeId1;
    
    private int attributeId2;
    
    private int attributeId3;
    
    private String productIdExcludeString;
    
    /**
     * Constructor
     */
    public ProductSearchQuery() {
        super();
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getAttributeId1() {
        return attributeId1;
    }

    public void setAttributeId1(int attributeId1) {
        this.attributeId1 = attributeId1;
    }

    public int getAttributeId2() {
        return attributeId2;
    }

    public void setAttributeId2(int attributeId2) {
        this.attributeId2 = attributeId2;
    }

    public int getAttributeId3() {
        return attributeId3;
    }

    public void setAttributeId3(int attributeId3) {
        this.attributeId3 = attributeId3;
    }

    public String getProductIdExcludeString() {
        return productIdExcludeString;
    }

    public void setProductIdExcludeString(String productIdExcludeString) {
        this.productIdExcludeString = productIdExcludeString;
    }
}
