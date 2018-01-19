/*
 * ProductManagementReportingIF.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.service.productmanagement;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Chris Hatton
 */
public interface ProductManagementReportingServiceIF {
    
    public Map<Integer, Integer> getStockCount2ProductCount() throws Exception;
    
    public Map<String, Integer> getTotals() throws Exception;
    
    public Map<String, BigDecimal> getPriceStatistics() throws Exception;
    
    public Map<String, Integer> getShopPageStatistics() throws Exception;
    
    public Map<String, Integer> getSupplierStatistics() throws Exception;
    
    public Map<String, Integer> getProductStatusStatistics() throws Exception;

}
