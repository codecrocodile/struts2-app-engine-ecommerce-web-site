/*
 * PercentageDiscountApplier.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.sales;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.logging.Logger;

/**
 * @author Chris Hatton
 */
public class PercentageDiscountApplier extends DiscountApplier {
    
    private static final long serialVersionUID = -8306997279616830972L;
    
    private static Logger log = Logger.getLogger(PercentageDiscountApplier.class.getName());

    private final BigDecimal ONEHUNDERT = new BigDecimal(100);
    
    private int percentage;
    
    private BigDecimal minimumSpend;

    /**
     * Constructor
     */
    public PercentageDiscountApplier(Discount discount, int percentage, BigDecimal minimumSpend) {
        super(discount);
        this.percentage = percentage;
        this.minimumSpend = minimumSpend;
    }

    /* 
     * @see com.groovyfly.admin.sales.DiscountApplier#calculateDiscount()
     */
    @Override
    public BigDecimal calculateDiscount() throws DiscountException {
        
        log.info("calculate discount");
        
        if (shoppingCart.getSubTotal().compareTo(minimumSpend) < 0) {
            shoppingCart.setDiscount(null);
            shoppingCart.setDiscountApplier(null);

            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(shoppingCart.getLocale());
            throw new DiscountException("Discount cannot be applied to orders under " + numberFormat.format(minimumSpend));
        }
        
        BigDecimal discountValue = this.percentage(shoppingCart.getSubTotal(), new BigDecimal(percentage));
        
        return discountValue;
    }
    
    public BigDecimal percentage(BigDecimal subTotal, BigDecimal percentage){
        return subTotal.multiply(percentage).divide(ONEHUNDERT);
    }

    /* 
     * @see com.groovyfly.admin.sales.DiscountApplier#generateSuccessMessage(java.math.BigDecimal)
     */
    @Override
    public String generateSuccessMessage(BigDecimal calculateDiscount) {
        return "10% discount applied";
    }
}
