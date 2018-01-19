/*
 * Customer.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.structures;

/**
 * @author Chris Hatton
 */
public class Customer extends Person {

    private static final long serialVersionUID = 4594788080883773793L;
    
    private String payPalPayerId;

    private String emailStatus;
    
    private ShippingAddress shippingAddress;
    
    /**
     * Constructor
     */
    public Customer() {
        super();
    }

    public String getPayPalPayerId() {
        return payPalPayerId;
    }

    public void setPayPalPayerId(String payPalPayerId) {
        this.payPalPayerId = payPalPayerId;
    }

    public String getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(String emailStatus) {
        this.emailStatus = emailStatus;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getDisplayName() {
        StringBuilder sb = new StringBuilder();
        
        if (this.getTitle() != null) {
            sb.append(this.getTitle());
            sb.append(" ");
        }
        sb.append(this.getForename());
        sb.append(" ");
        sb.append(this.getSurname());
        
        return sb.toString();
    }
}
