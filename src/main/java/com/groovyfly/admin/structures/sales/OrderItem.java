/*
 * OrderItem.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.structures.sales;

import java.math.BigDecimal;

/**
 * @author Chris Hatton
 */
public class OrderItem {
    
    private int orderItemId;
    
    private int productId;
    
    private String name;
    
    private String sku;
    
    private BigDecimal unitPrice = new BigDecimal(0.00);
    
    private BigDecimal totalPrice = new BigDecimal(0.00);
    
    /** The quantity of the product the customer wants */
    private int quantity;
    
    private String currencyCode;
    
    /**
     * Constructor
     */
    public OrderItem() {
        super();
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
