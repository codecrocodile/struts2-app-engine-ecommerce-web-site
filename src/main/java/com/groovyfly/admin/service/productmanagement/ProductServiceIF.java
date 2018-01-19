/*
 * ProductServiceIF.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.service.productmanagement;

import java.util.List;

import com.groovyfly.common.structures.GroupingProduct;
import com.groovyfly.common.structures.Lookup;
import com.groovyfly.common.structures.Product;
import com.groovyfly.common.structures.ProductAttribute;
import com.groovyfly.common.structures.ProductSearchQuery;

/**
 * @author Chris Hatton
 * 
 * Created 28 Jul 2012
 */
public interface ProductServiceIF {
    
    public List<Lookup> getProductStatuses(boolean includeRetired) throws Exception;
    
    public List<ProductAttribute> getProductAttributes() throws Exception;
    
    public List<ProductAttribute> getProductAttributes(List<Integer> productAttributeIds) throws Exception;
    
    public List<Product> getProductSummaries(boolean includeRetired) throws Exception;
    
    public List<Product> getProductSummaries(ProductSearchQuery productSearchQuery, boolean includeRetired) throws Exception;
    
    public List<Product> getProductSummaries(int categoryId, boolean groupingProductsOnly, int limit, int offset) throws Exception;
    
    public List<Product> getProductSummaries(List<Integer> productIds) throws Exception;
    
    public List<GroupingProduct> getGroupingProductSummaries(boolean includeRetired) throws Exception;
    
    public Product getProduct(int productId) throws Exception;
    
    public Product getProduct(String urlAlias) throws Exception;
    
    public Product saveProduct(Product product) throws Exception;
    
    public void retireProduct(int productId) throws Exception;

}
