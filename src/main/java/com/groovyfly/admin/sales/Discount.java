/*
 * Discount.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.sales;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Chris Hatton
 */
public class Discount implements Serializable {
    
    private static final long serialVersionUID = -831583761687535652L;

    private String discountCode;
    
    private DiscountType discountType = new DiscountType();
    
    private Date startDate;
    
    private Date endDate;
    
    private BigDecimal value = new BigDecimal(0);
    
    /**
     * Constructor
     */
    public Discount() {
        super();
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
