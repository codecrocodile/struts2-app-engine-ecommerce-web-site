/*
 * Category.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.structures;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chris Hatton
 */
public class Category {

    private int categoryId;

    private int parentId;
    
    private String urlAlias;
    
    private String name;

    private String description;

    private String path;

    private int sortIndex;

    private boolean firstInCategory;

    private boolean lastInCategory;

    private boolean retired;
    
    private int productCountInCategory;
    
    private Page page = new Page();
    
    private List<Category> subCategories = new ArrayList<Category>();

    /**
     * Constructor
     */
    public Category() {
        super();
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int id) {
        this.categoryId = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
    
    public String getUrlAlias() {
        return urlAlias;
    }

    public void setUrlAlias(String urlAlias) {
        this.urlAlias = urlAlias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }

    public boolean isFirstInCategory() {
        return firstInCategory;
    }

    public void setFirstInCategory(boolean firstInCategory) {
        this.firstInCategory = firstInCategory;
    }

    public boolean isLastInCategory() {
        return lastInCategory;
    }

    public void setLastInCategory(boolean lastInCategory) {
        this.lastInCategory = lastInCategory;
    }

    public boolean isRetired() {
        return retired;
    }

    public void setRetired(boolean retired) {
        this.retired = retired;
    }
    
    public int getProductCountInCategory() {
        return productCountInCategory;
    }

    public void setProductCountInCategory(int productCountInCategory) {
        this.productCountInCategory = productCountInCategory;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<Category> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<Category> subCategories) {
        this.subCategories = subCategories;
    }
    
    /* 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Category) {
            Category other = (Category) obj;
            if (this.categoryId == other.categoryId) {
                return true;
            }
        }
        
        return false;
    }
    
    /* 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.getCategoryId();
    }
}
