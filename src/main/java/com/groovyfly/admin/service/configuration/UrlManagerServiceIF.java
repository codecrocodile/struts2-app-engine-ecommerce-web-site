/*
 * UrlManagerServiceIF.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.service.configuration;

/**
 * @author Chris Hatton
 */
public interface UrlManagerServiceIF {

    public String getUrlAliasForNewCategory(int parentCategoryId, String categoryNameOrAlias) throws Exception;
    
    public String getUrlAliasForExistingCategory(int existingPageId, int parentCategoryId, String categoryNameOrAlias) throws Exception;
    
    public String getUrlAliasForNewProduct(int categoryId, String productNameOrAlias) throws Exception;
    
    public String getUrlAliasForExistingProduct(int existingPageId, int categoryId, String productNameOrAlias) throws Exception;
    
}
