/*
 * DiscountType.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.sales;

import java.io.Serializable;

/**
 * @author Chris Hatton
 * 
 *         Created 8 Nov 2012
 */
public class DiscountType implements Serializable {

    private static final long serialVersionUID = 4773809296143396179L;

    private int discountTypeId;

    private String name;

    private String description;

    /**
     * Constructor
     */
    public DiscountType() {
        super();
    }

    public int getDiscountTypeId() {
        return discountTypeId;
    }

    public void setDiscountTypeId(int discountTypeId) {
        this.discountTypeId = discountTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
