/*
 * CollectionAction.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.actions.shopping;

import java.util.ArrayList;
import java.util.List;

import com.groovyfly.admin.service.collection.CollectionServiceIF;
import com.groovyfly.admin.service.productmanagement.ProductServiceIF;
import com.groovyfly.common.structures.Product;
import com.groovyfly.site.actions.BaseSiteAction;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Chris Hatton
 */
public class CollectionAction extends BaseSiteAction {
    
    private static final long serialVersionUID = 4068113095727546033L;
    
    private CollectionServiceIF collectionServiceIF;
    
    private ProductServiceIF productServiceIF;
    
    private List<Product> productSummaries = new ArrayList<Product>();
    
    /**
     * This will be set by the Spring IOC container.
     */
    public void setCollectionServiceIF(CollectionServiceIF collectionServiceIF) {
        this.collectionServiceIF = collectionServiceIF;
    }
    

    /**
     * This will be set by the Spring IOC container.
     */
    public void setProductServiceIF(ProductServiceIF productServiceIF) {
        this.productServiceIF = productServiceIF;
    }

    /* 
     * @see com.opensymphony.xwork2.ActionSupport#execute()
     */
    @Override
    public String execute() throws Exception {
        try {
            super.retrievePage("collection");
            
            List<Integer> productIdsInCollection = collectionServiceIF.getProductIdsInCollection(super.getUrlAlias());
            
            if (productIdsInCollection.size() > 0) {
                this.productSummaries = productServiceIF.getProductSummaries(productIdsInCollection);    
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        
        return ActionSupport.SUCCESS;
    }

    public List<Product> getProductSummaries() {
        return productSummaries;
    }
}
