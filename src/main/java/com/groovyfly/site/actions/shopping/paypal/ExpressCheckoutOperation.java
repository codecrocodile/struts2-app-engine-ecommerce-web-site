/*
 * PaypalOperation.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.actions.shopping.paypal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import urn.ebay.apis.CoreComponentTypes.BasicAmountType;
import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;
import urn.ebay.apis.eBLBaseComponents.ErrorType;
import urn.ebay.apis.eBLBaseComponents.ItemCategoryType;
import urn.ebay.apis.eBLBaseComponents.PaymentActionCodeType;
import urn.ebay.apis.eBLBaseComponents.PaymentDetailsItemType;
import urn.ebay.apis.eBLBaseComponents.PaymentDetailsType;

import com.groovyfly.common.structures.ShoppingCart;
import com.groovyfly.common.structures.ShoppingCartEntry;

/**
 * @author Chris Hatton
 */
public abstract class ExpressCheckoutOperation {
    
    protected String token;
    
    protected String configPath;
    
    protected ShoppingCart shoppingCart;
    
    protected List<ErrorType> errors;
    
    /**
     * Constructor
     */
    public ExpressCheckoutOperation(String token, String contextRealPath) {
        this.token = token;
        this.configPath = contextRealPath + "/WEB-INF/paypal-sdk-config.properties";
    }
    
    public abstract void doOperation() throws Exception;
    
    /*
     * Sets the bulk of the payment details as taken from the shopping cart e.g. the items, shipping, totals, etc...
     */
    protected List<PaymentDetailsType> getPaymentDetails() {
        PaymentDetailsType paymentDetails = new PaymentDetailsType();
        paymentDetails.setPaymentAction(PaymentActionCodeType.SALE);  // SALE is the default but we will set it anyways (this is for immediate payment)
        
        // totals for the payment
        BigDecimal itemTotal = new BigDecimal(0);
        BigDecimal orderTotal = new BigDecimal(0);

        // items for the payment
        List<PaymentDetailsItemType> lineItems = new ArrayList<PaymentDetailsItemType>();
        for (ShoppingCartEntry e : shoppingCart.getShoppingCartEntries()) {
            PaymentDetailsItemType item = new PaymentDetailsItemType();
            item.setName(e.getName());
            item.setQuantity(e.getQuantity());
            
            BasicAmountType amt = new BasicAmountType();
            amt.setCurrencyID(CurrencyCodeType.fromValue("GBP")); // TODO change this depending on what currency we are using in the display
            amt.setValue(e.getUnitPrice().toString());
            item.setAmount(amt);
            
            item.setItemCategory(ItemCategoryType.PHYSICAL); // we will always be selling physical items    

            lineItems.add(item);
            
            itemTotal = itemTotal.add(e.getTotalPrice());
            orderTotal = orderTotal.add(e.getTotalPrice());
        }
        // add item for the discount if there is one
        if (shoppingCart.getDiscount() != null) {
            PaymentDetailsItemType item = new PaymentDetailsItemType();
            item.setName(shoppingCart.getDiscount().getDiscountType().getName());
            item.setQuantity(1);
            
            BasicAmountType amt = new BasicAmountType();
            amt.setCurrencyID(CurrencyCodeType.fromValue("GBP")); // TODO change this depending on what currency we are using in the display
            amt.setValue(shoppingCart.getDiscount().getValue().setScale(2, BigDecimal.ROUND_DOWN).negate().toString());
            item.setAmount(amt);
            
            item.setItemCategory(ItemCategoryType.PHYSICAL); // we will always be selling physical items    

            lineItems.add(item);
            
            itemTotal = itemTotal.subtract(shoppingCart.getDiscount().getValue().setScale(2, BigDecimal.ROUND_DOWN));
            orderTotal = orderTotal.subtract(shoppingCart.getDiscount().getValue().setScale(2, BigDecimal.ROUND_DOWN));
        }
        
        paymentDetails.setPaymentDetailsItem(lineItems);
        
        // overall payment details
        List<PaymentDetailsType> payDetails = new ArrayList<PaymentDetailsType>();
        
        BasicAmountType shippingTotal = new BasicAmountType();
        shippingTotal.setValue(shoppingCart.getPostageAndPacking().toString()); 
        shippingTotal.setCurrencyID(CurrencyCodeType.fromValue("GBP"));
        paymentDetails.setShippingTotal(shippingTotal);
        
        orderTotal = orderTotal.add(shoppingCart.getPostageAndPacking());

        BasicAmountType itemsTotal = new BasicAmountType();
        itemsTotal.setValue(itemTotal.toString());
        itemsTotal.setCurrencyID(CurrencyCodeType.fromValue("GBP"));
        paymentDetails.setOrderTotal(new BasicAmountType(CurrencyCodeType.fromValue("GBP"), orderTotal.toString()));
        
        paymentDetails.setItemTotal(itemsTotal);
        
        payDetails.add(paymentDetails);
        
        return payDetails;
    }
    
    public String getToken() {
        return token;
    }
    
    public List<ErrorType> getErrors() {
        return errors;
    }
}
