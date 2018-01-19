/*
 * OrdersServiceIF.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.service.sales;

import com.groovyfly.admin.structures.sales.Order;

/**
 * @author Chris Hatton
 */
public interface OrdersServiceIF {
    
    public Order getOrder(String orderId) throws Exception;
    
}
