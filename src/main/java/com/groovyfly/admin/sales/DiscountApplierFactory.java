/*
 * DiscountApplierFactory.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.sales;

import java.math.BigDecimal;
import java.util.logging.Logger;

/**
 * @author Chris Hatton
 */
public class DiscountApplierFactory {
    
    private static Logger log = Logger.getLogger(DiscountApplierFactory.class.getName());
    
    public static DiscountApplier getDiscountApplier(Discount discount) {
        if (discount.getDiscountType().getDiscountTypeId() == 1) {
            return new PercentageDiscountApplier(discount, 10, new BigDecimal(10.00));
        } else {
            log.severe("Request for non-existing discount applier. Check database matches something the factory can return.");
            return null;
        }
    }

}
