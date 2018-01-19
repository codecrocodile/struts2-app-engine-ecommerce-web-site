/*
 * ShoppingCart.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.structures;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import com.groovyfly.admin.sales.Discount;
import com.groovyfly.admin.sales.DiscountApplier;
import com.groovyfly.admin.sales.DiscountException;


/**
 * Represents a shopping cart for the current user session.
 * 
 * @author Chris Hatton
 */
public class ShoppingCart implements Serializable {
    
    private static final long serialVersionUID = -260843003502681695L;
    
    private static Logger log = Logger.getLogger(ShoppingCart.class.getName());

    private String shoppingCartId;
    
    private BigDecimal minimumSpend = new BigDecimal(0);
    
    private int itemCount = 0;
    
    private BigDecimal subTotal = new BigDecimal(0);
    
    private List<ShoppingCartEntry> shoppingCartEntries = new ArrayList<ShoppingCartEntry>();
    
    private BigDecimal postageAndPacking = new BigDecimal(0);
    
    private DiscountApplier discountApplier;
    
    private Discount discount;
    
    private Locale locale = Locale.UK;
    
    private NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.UK);
    
    /**
     * Constructor
     */
    public ShoppingCart(String shoppingCartId, Locale locale) {
        super();
        this.shoppingCartId = shoppingCartId;
        this.locale = locale;
        this.numberFormat = NumberFormat.getCurrencyInstance(locale);
    }

    public String getShoppingCartId() {
        return shoppingCartId;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }
    
    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }
    
    public List<ShoppingCartEntry> getShoppingCartEntries() {
        return shoppingCartEntries;
    }

    /**
     * Adds an entry to the shopping cart. This will update the item count and total value of items in
     * the shopping cart. If an entry of the same item exists in the cart then the existing entry for
     * the item will be updated instead of being added.
     */
    public void addShoppingCartEntry(ShoppingCartEntry shoppingCartEntry) {
        itemCount += shoppingCartEntry.getQuantity();
        subTotal = subTotal.add(shoppingCartEntry.getTotalPrice());
        
        boolean updatedExistingEntry = false;
        for (ShoppingCartEntry e : shoppingCartEntries) {
            if (e.getProductId() == shoppingCartEntry.getProductId()) {
                e.setQuantity(e.getQuantity() + shoppingCartEntry.getQuantity());
                updatedExistingEntry = true;
                break;
            }
        }
        
        if (!updatedExistingEntry) { // if no existing entry was found for the item then add the new entry
            
            log.info("entry added " +shoppingCartEntry.getProductId());
            
            shoppingCartEntries.add(shoppingCartEntry);
        }
    }
    
    /**
     * Removes a shopping cart entry from the shopping cart. This will update the item count and total value of items in
     * the shopping cart. 
     */
    public void removeShopppingCartEntry(ShoppingCartEntry shoppingCartEntry) {
        itemCount -= shoppingCartEntry.getQuantity();
        subTotal = subTotal.subtract(shoppingCartEntry.getTotalPrice());
        
        shoppingCartEntries.remove(shoppingCartEntry);
    }
    
    public BigDecimal getPostageAndPacking() {
        return postageAndPacking;
    }

    public void setPostageAndPacking(BigDecimal postageAndPacking) {
        this.postageAndPacking = postageAndPacking;
    }
    
    public BigDecimal getMinimumSpend() {
        return minimumSpend;
    }

    public void setMinimumSpend(BigDecimal minimumSpend) {
        this.minimumSpend = minimumSpend;
    }

    public DiscountApplier getDiscountApplier() {
        return discountApplier;
    }

    public void setDiscountApplier(DiscountApplier discountApplier) {
        this.discountApplier = discountApplier;
    }
    
    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        this.numberFormat = NumberFormat.getCurrencyInstance(locale);
    }
    
    public BigDecimal getTotal() {
        BigDecimal total = new BigDecimal(0);
        total = total.add(getSubTotal());
        total = total.add(getPostageAndPacking());
        
        if (discount != null) {
            total = total.subtract(discount.getValue());    
        }
        
        return total;
    }
    
    public boolean isBellowMinimumSpend() {
        if (subTotal.compareTo(minimumSpend) < 0) {
            return true;
        } else {
            return false;
        }
    }
    
    public String applyDiscount() throws DiscountException, Exception {
        if (discountApplier != null) {
            return this.discountApplier.applyDiscount(this);
        }
        
        return null;
    }
    
    /**
     * Checks the shopping cart entries to see if there is enough stock to fill the order. It is assumed there
     * is until the customer confirms the order at the end of the checkout process.
     */
    public boolean isEnoughQuantityInStock() {
        for (ShoppingCartEntry e : shoppingCartEntries) {
            if (e.isQuantityInStockEnough() == false) {
                return false;
            }
        }
        return true;
    }
    
    /* GET STRING VALUES REQUIRED FOR DISPLAY */
    
    public String getLocaleCurrencyStringValue(BigDecimal value) {
        return this.numberFormat.format(value.doubleValue());
    }
    
    public String getMinimumSpendString() {
        return numberFormat.format(minimumSpend.doubleValue());
    }
    
    public String getSubTotalString() {
        return numberFormat.format(subTotal.doubleValue());
    }
    
    public String getPostageAndPackingString() {
        return numberFormat.format(postageAndPacking.doubleValue());
    }
    
    public String getDiscountTotalString() {
        if (discount != null) {
            return numberFormat.format(discount.getValue().doubleValue());    
        } else {
            return numberFormat.format(new BigDecimal(0));
        }
    }
    
    public String getTotalString() {
        return numberFormat.format(this.getTotal().doubleValue());
    }
    
}
