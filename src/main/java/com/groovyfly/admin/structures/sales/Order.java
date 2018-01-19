/*
 * Order.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.structures.sales;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.groovyfly.common.structures.Customer;
import com.groovyfly.common.structures.ShippingAddress;


/**
 * @author Chris Hatton
 */
public class Order {
    
    private String orderId;
    
    private String orderStatusCode;
    
    private Customer customer;
    
    private ShippingAddress shippingAddress;
    
    private String paymentProcessor;
    
    private String paypalPayerId;
    
    private String paypalTransationId;
    
    private Date paypalPaymentDate;
    
    private String paypalPaymentStatus;
    
    private String payaplPendingReason;
    
    private BigDecimal subTotalAmount = new BigDecimal(0);
    
    private BigDecimal vatAmount = new BigDecimal(0);
    
    private BigDecimal postageAndPacking = new BigDecimal(0);
    
    private String discountCode;
    
    private BigDecimal discountAmount = new BigDecimal(0);
    
    private BigDecimal total;
    
    private Date dateCreated;
    
    private List<OrderItem> orderItems;
    
    /**
     * Constructor
     */
    public Order() {
        super();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatusCode() {
        return orderStatusCode;
    }

    public void setOrderStatusCode(String orderStatusCode) {
        this.orderStatusCode = orderStatusCode;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPaymentProcessor() {
        return paymentProcessor;
    }

    public void setPaymentProcessor(String paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }

    public String getPaypalPayerId() {
        return paypalPayerId;
    }

    public void setPaypalPayerId(String paypalPayerId) {
        this.paypalPayerId = paypalPayerId;
    }

    public String getPaypalTransationId() {
        return paypalTransationId;
    }

    public void setPaypalTransationId(String paypalTransationId) {
        this.paypalTransationId = paypalTransationId;
    }

    public Date getPaypalPaymentDate() {
        return paypalPaymentDate;
    }

    public void setPaypalPaymentDate(Date paypalPaymentDate) {
        this.paypalPaymentDate = paypalPaymentDate;
    }

    public String getPaypalPaymentStatus() {
        return paypalPaymentStatus;
    }

    public void setPaypalPaymentStatus(String paypalPaymentStatus) {
        this.paypalPaymentStatus = paypalPaymentStatus;
    }

    public String getPayaplPendingReason() {
        return payaplPendingReason;
    }

    public void setPayaplPendingReason(String payaplPendingReason) {
        this.payaplPendingReason = payaplPendingReason;
    }

    public BigDecimal getSubTotalAmount() {
        return subTotalAmount;
    }

    public void setSubTotalAmount(BigDecimal subTotalAmount) {
        this.subTotalAmount = subTotalAmount;
    }
    
    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public BigDecimal getPostageAndPacking() {
        return postageAndPacking;
    }

    public void setPostageAndPacking(BigDecimal postageAndPacking) {
        this.postageAndPacking = postageAndPacking;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }
    
    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
