/*
 * JsonProductSearchAction.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.actions.productmanagement;

import java.util.List;

import com.groovyfly.admin.service.productmanagement.ProductServiceIF;
import com.groovyfly.common.structures.Product;
import com.groovyfly.common.structures.ProductSearchQuery;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Chris Hatton
 * 
 * Created 1 Sep 2012
 */
public class JsonProductSearchAction extends ActionSupport {
    
    private static final long serialVersionUID = 3366166151921104711L;
    
    private ProductServiceIF productServiceIF;
    
    private ProductSearchQuery productSearchQuery;
    
    private List<Product> productSummaries;
    
    public void setProductServiceIF(ProductServiceIF productServiceIF) {
        this.productServiceIF = productServiceIF;
    }
    
    /* 
     * @see com.opensymphony.xwork2.ActionSupport#execute()
     */
    @Override
    public String execute() throws Exception {
        this.productSummaries = productServiceIF.getProductSummaries(productSearchQuery, false);
        
        return ActionSupport.SUCCESS;
    }

    public ProductSearchQuery getProductSearchQuery() {
        return productSearchQuery;
    }

    public void setProductSearchQuery(ProductSearchQuery productSearchQuery) {
        this.productSearchQuery = productSearchQuery;
    }

    public List<Product> getProductSummaries() {
        return productSummaries;
    }

    public void setProductSummaries(List<Product> productSummaries) {
        this.productSummaries = productSummaries;
    }

}
