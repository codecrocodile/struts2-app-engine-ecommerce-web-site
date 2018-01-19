/*
 * ShoppingCartAction.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.actions.shopping;

import java.math.BigDecimal;
import java.util.logging.Logger;

import com.groovyfly.admin.postage.RoyalMailPostageCalculator;
import com.groovyfly.site.actions.BaseSiteAction;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Chris Hatton
 */
public class ShoppingCartAction extends BaseSiteAction {

    private static final long serialVersionUID = -4030181954426648670L;
    
    private static Logger log = Logger.getLogger(ShoppingCartAction.class.getName());
    
    private RoyalMailPostageCalculator royalMailPostageCalculator;
    
    
    public void setRoyalMailPostageCalculator(RoyalMailPostageCalculator royalMailPostageCalculator) {
        this.royalMailPostageCalculator = royalMailPostageCalculator;
    }

    /* 
     * @see com.opensymphony.xwork2.ActionSupport#execute()
     */
    @Override
    public String execute() throws Exception {
        log.info("ShoppingCartAction");
        
        if (shoppingCart != null) {
            if (shoppingCart.getShoppingCartEntries().size() > 0) {
                
                BigDecimal estimatedPostage = royalMailPostageCalculator.getEstimatedPostage(shoppingCart, shoppingCart.getLocale());
                shoppingCart.setPostageAndPacking(estimatedPostage);    
            } else {
                shoppingCart.setPostageAndPacking(new BigDecimal(0));
            }
            
            if (shoppingCart.getMinimumSpend().doubleValue() == 0) {
                shoppingCart.setMinimumSpend(new BigDecimal(5.50)); // TODO we should really get this from db    
            }
        }
        
        return ActionSupport.SUCCESS;
    }

}
