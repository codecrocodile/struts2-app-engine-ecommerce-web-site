/*
 * ShoppingServiceIF.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.service.shopping;

import java.util.Date;
import java.util.Map;

import com.groovyfly.common.structures.Customer;
import com.groovyfly.common.structures.ShoppingCart;
import com.groovyfly.common.structures.ShoppingCartEntry;
import com.groovyfly.common.structures.exceptions.NotEnoughStockException;
import com.groovyfly.common.util.PaymentProcessor;

/**
 * @author Chris Hatton
 */
public interface ShoppingServiceIF {
    
    /**
     * Adds a configurable grouped product to the shopping cart.
     */
    public ShoppingCartEntry addConfigurableGroupingProductToCart(ShoppingCart shoppingCart, int groupingProductId, Map<String, Integer> attributeToAttributeValue, int quantity) throws Exception;
    
    /**
     * Adjust the quantity of a product in the shopping cart.
     */
    public void adjustProductQuantityInCart(String shoppingCartId, int productId, int amount) throws Exception;
    
    /**
     * Deletes a product from the shopping cart. 
     */
    public void deleteProductFromCart(String shoppingCartId, int productId) throws Exception;
    
    /**
     * Creates an order or throws an exception if there is nor enough stock to fill the order.
     * 
     * @param shoppingCart
     * @param customer
     * @param paymentProcessor
     * @param transactionId
     * @throws Exception
     * @throws NotEnoughStockException
     * 
     * @return the order id
     */
    public String createOrder(ShoppingCart shoppingCart, Customer customer, PaymentProcessor paymentProcessor) throws Exception, NotEnoughStockException;
    
    /**
     * Confirms the order after it has been processed by the payment processor. We also store information about the 
     * transaction e.g. status of the transaction and a reason why the payment is pending if any. The main reason for
     * this is for eCheques that take around 5-7 working days to clear before we receive payment. Until the payment
     * is received we don't want to be posting the products.
     * 
     * @param transactionId
     * @param paymentDate
     * @param paymentStatus
     * @param pendingReason
     * @throws Exception
     */
    public void confirmOrder(String orderNumber, String paypalPayerId, String transactionId, Date paymentDate, String paymentStatus, String pendingReason) throws Exception;

}
