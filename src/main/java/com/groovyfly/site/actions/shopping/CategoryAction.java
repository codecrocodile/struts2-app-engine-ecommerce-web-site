/*
 * CategoryAction.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.actions.shopping;

import java.util.List;

import com.groovyfly.admin.service.productmanagement.CategoryServiceIF;
import com.groovyfly.admin.service.productmanagement.ProductServiceIF;
import com.groovyfly.common.structures.Category;
import com.groovyfly.common.structures.Product;
import com.groovyfly.site.actions.BaseSiteAction;
import com.opensymphony.xwork2.Action;

/**
 * @author Chris Hatton
 */
public class CategoryAction extends BaseSiteAction {
       
    private static final long serialVersionUID = 8366865019336478192L;
    
    private static final int PRODUCTS_LIMIT = 21;
    
    private CategoryServiceIF categoryServiceIF;
    
    private ProductServiceIF productServiceIF;
    
    private int noOfPagesInCategory;
    
    private int currentPage;
    
    private Category category;
    
    private List<Product> productSummaries;
    
    public void setCategoryServiceIF(CategoryServiceIF categoryServiceIF) {
        this.categoryServiceIF = categoryServiceIF;
    }

    public void setProductServiceIF(ProductServiceIF productServiceIF) {
        this.productServiceIF = productServiceIF;
    }
    
    /* 
     * @see com.opensymphony.xwork2.ActionSupport#execute()
     */
    @Override
    public String execute() throws Exception {
        try {
            this.category = this.categoryServiceIF.getCategoryForPageId(super.page.getPageId());
            
            if (super.getPageParameters().get("page") != null) {
                int productCountIncategory = this.categoryServiceIF.getProductCountInCategory(category.getCategoryId());
                this.noOfPagesInCategory = (int) Math.ceil(productCountIncategory / PRODUCTS_LIMIT) + 1;
                int page = (Integer) super.getPageParameters().get("page");
                this.currentPage = page;
                int offset = (page * PRODUCTS_LIMIT) - PRODUCTS_LIMIT;
                this.productSummaries = productServiceIF.getProductSummaries(category.getCategoryId(), true, PRODUCTS_LIMIT, offset);
            } else {
                this.noOfPagesInCategory = 1;
                this.currentPage = 1;
                this.productSummaries = productServiceIF.getProductSummaries(category.getCategoryId(), true, Integer.MAX_VALUE, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
       
        return Action.SUCCESS;
    }

    public Category getCategory() {
        return category;
    }

    public List<Product> getProductSummaries() {
        return productSummaries;
    }

    public int getNoOfPagesInCategory() {
        return noOfPagesInCategory;
    }

    public int getCurrentPage() {
        return currentPage;
    }
}
