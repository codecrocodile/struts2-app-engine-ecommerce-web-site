/*
 * NullDiscountApplier.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.sales;

import java.math.BigDecimal;

/**
 * @author Chris Hatton
 * 
 * Created 8 Nov 2012
 */
public class NullDiscountApplier extends DiscountApplier {

    /** */
    private static final long serialVersionUID = 4372704896342516913L;

    /**
     * Constructor
     */
    public NullDiscountApplier(Discount discount) {
        super(discount);
    }

    /* 
     * @see com.groovyfly.admin.sales.DiscountApplier#calculateDiscount()
     */
    @Override
    public BigDecimal calculateDiscount() throws DiscountException {
        return new BigDecimal(0);
    }

    /* 
     * @see com.groovyfly.admin.sales.DiscountApplier#generateSuccessMessage(java.math.BigDecimal)
     */
    @Override
    public String generateSuccessMessage(BigDecimal calculateDiscount) {
        return "No discount to apply";
    }

    
}
