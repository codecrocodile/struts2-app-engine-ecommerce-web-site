/*
 * DiscountsIF.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.service.sales;

import com.groovyfly.admin.sales.Discount;

/**
 * @author Chris Hatton
 */
public interface DiscountsServiceIF {
    
    /**
     * Gets the discount to be applied for the given discount code.
     */
    public Discount getDiscountForDiscountCode(String discountCode) throws Exception;

}
