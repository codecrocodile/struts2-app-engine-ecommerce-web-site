/*
 * GroupingProduct.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.structures;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chris Hatton
 */
public class GroupingProduct extends Product {
    
    private int childProductCount;
    
    private List<Product> products = new ArrayList<Product>();
    
    private String childProductIds;
    
    /**
     * Constructor
     */
    public GroupingProduct() {
        super();
    }
    
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
    
    public int getChildProductCount() {
        return childProductCount;
    }

    public void setChildProductCount(int childProductCount) {
        this.childProductCount = childProductCount;
    }

    public String getChildProductIds() {
        return childProductIds;
    }

    public void setChildProductIds(String childProductIds) {
        this.childProductIds = childProductIds;
    }
}
