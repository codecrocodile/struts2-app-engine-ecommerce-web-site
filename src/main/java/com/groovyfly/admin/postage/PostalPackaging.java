/*
 * Packaging.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.postage;

import java.math.BigDecimal;

/**
 * @author Chris Hatton
 */
public class PostalPackaging {
    
    /** Database primary key for this packaging */
    private int packagingId;
    
    /** This is the type of packaging container */
    private PostalPackagingType packagingType;
    
    /** Description of this packaging e.g. letter, large letter, box, postal tube, etc... */
    private String packagingDescription;
    
    /** This is the largest dimension usually (mm) */
    private int length;
    
    /** This is the second largest dimension usually (mm)*/
    private int width;
    
    /** This is the smallest dimension usually (mm) **/
    private int height;
    
    /** Weight of packaging */ 
    private int weight;
    
    /** Database primary key of the supplier */
    private int supplierId;
    
    /** Cost of this packaging */
    private BigDecimal cost = new BigDecimal(0);
    
    /**
     * Constructor
     */
    public PostalPackaging() {
        super();
    }

    public PostalPackagingType getPackagingType() {
        return packagingType;
    }

    public void setPackagingType(PostalPackagingType packagingType) {
        this.packagingType = packagingType;
    }

    public int getPackagingId() {
        return packagingId;
    }

    public void setPackagingId(int packagingId) {
        this.packagingId = packagingId;
    }

    public String getPackagingDescription() {
        return packagingDescription;
    }

    public void setPackagingDescription(String packagingDescription) {
        this.packagingDescription = packagingDescription;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }
    
    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * Does not take into account the internal dimensions nor if any of the other dimensions will accommodate.
     * Common sense by the user has to be applied here when packing.
     */
    public boolean canAccomodateDimentions(int length, int width, int height) {
        if (length < this.length && width < this.width && height < this.height) {
            return true;
        } else {
            return false;
        }
    }

}
