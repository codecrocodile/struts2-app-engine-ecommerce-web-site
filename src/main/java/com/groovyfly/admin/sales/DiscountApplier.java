/*
 * DiscountApplier.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.sales;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Calendar;

import com.groovyfly.common.structures.ShoppingCart;

/**
 * @author Chris Hatton
 * 
 * Created 8 Nov 2012
 */
public abstract class DiscountApplier implements Serializable {
    
    private static final long serialVersionUID = 6170772192782117597L;

    protected ShoppingCart shoppingCart;
    
    protected Discount discount;
    
    /**
     * Constructor
     */
    public DiscountApplier(Discount discount) {
        this.discount = discount;
    }
    
    public String applyDiscount(ShoppingCart shoppingCart) throws DiscountException {
        this.shoppingCart = shoppingCart;
        this.checkStartDate();
        this.checkEndDate();
        BigDecimal calculatedDiscount = this.calculateDiscount();
        this.discount.setValue(calculatedDiscount);
        this.shoppingCart.setDiscount(discount);

        return this.generateSuccessMessage(calculatedDiscount);
    }
    
    protected void checkStartDate() throws DiscountException {
        Calendar nowCal = Calendar.getInstance();
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(discount.getStartDate());
        
        if (nowCal.before(startCal)) {
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, shoppingCart.getLocale());
            throw new DiscountException("Discount does not start until " + dateFormat.format(startCal.getTime()));
        }
    }
    
    protected void checkEndDate() throws DiscountException {
        Calendar nowCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(discount.getEndDate());
        
        if (nowCal.after(endCal)) {
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, shoppingCart.getLocale());
            throw new DiscountException("Discount expired on " + dateFormat.format(endCal.getTime()));
        }
    }
    
    protected abstract BigDecimal calculateDiscount() throws DiscountException;
    
    protected abstract String generateSuccessMessage(BigDecimal calculateDiscount);

}
