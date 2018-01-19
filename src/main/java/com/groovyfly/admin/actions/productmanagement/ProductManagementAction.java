/*
 * ProductManagementAction.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.actions.productmanagement;

import java.math.BigDecimal;
import java.util.Map;
import java.util.logging.Logger;

import com.groovyfly.admin.actions.BaseAdminAction;
import com.groovyfly.admin.service.productmanagement.ProductManagementReportingServiceIF;
import com.groovyfly.common.interfaces.UserAware;
import com.groovyfly.common.structures.User;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author Chris Hatton
 */
public class ProductManagementAction extends BaseAdminAction implements UserAware, ModelDriven<User> {

    private static final long serialVersionUID = -1537595613982296502L;
    
    private static Logger log = Logger.getLogger(ProductManagementAction.class.getName());

    private User user;
    
    private ProductManagementReportingServiceIF productManagementReportingServiceIF;
    
    private Map<Integer, Integer> stockCount2ProductCount;
    
    private Map<String, Integer> totals;
    
    private Map<String, BigDecimal> priceStatistics;
    
    private Map<String, Integer> pageStatistics;
    
    private Map<String, Integer> supplierStatistics;
    
    private Map<String, Integer> productStatusStatistics;

    public void setProductManagementReportingServiceIF(ProductManagementReportingServiceIF productManagementReportingServiceIF) {
        this.productManagementReportingServiceIF = productManagementReportingServiceIF;
    }

    /* 
     * @see com.opensymphony.xwork2.ModelDriven#getModel()
     */
    @Override
    public User getModel() {
        return user;
    }

    /* 
     * @see com.groovyfly.common.interfaces.UserAware#setUser(com.groovyfly.common.structures.User)
     */
    @Override
    public void setUser(User user) {
        this.user = user;
    }
    
    /* 
     * @see com.opensymphony.xwork2.ActionSupport#execute()
     */
    @Override
    public String execute() throws Exception {
        stockCount2ProductCount = productManagementReportingServiceIF.getStockCount2ProductCount();
        totals = productManagementReportingServiceIF.getTotals();
        priceStatistics = productManagementReportingServiceIF.getPriceStatistics();
        pageStatistics = productManagementReportingServiceIF.getShopPageStatistics();
        supplierStatistics = productManagementReportingServiceIF.getSupplierStatistics();
        productStatusStatistics = productManagementReportingServiceIF.getProductStatusStatistics();
        
        return ActionSupport.SUCCESS;
    }

    public Map<Integer, Integer> getStockCount2ProductCount() {
        return stockCount2ProductCount;
    }

    public Map<String, Integer> getTotals() {
        return totals;
    }

    public Map<String, BigDecimal> getPriceStatistics() {
        log.info("price st " + priceStatistics.size());
        
        return priceStatistics;
    }

    public Map<String, Integer> getPageStatistics() {
        return pageStatistics;
    }

    public Map<String, Integer> getSupplierStatistics() {
        return supplierStatistics;
    }

    public Map<String, Integer> getProductStatusStatistics() {
        return productStatusStatistics;
    }
}
