/*
 * CategoryAction.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.actions.productmanagement;

import java.util.List;

import com.groovyfly.admin.actions.CRUDAction;
import com.groovyfly.admin.service.configuration.DuplicateUrlAliasException;
import com.groovyfly.admin.service.configuration.InvalidUrlAliasException;
import com.groovyfly.admin.service.configuration.UrlManagerServiceIF;
import com.groovyfly.admin.service.productmanagement.CategoryServiceIF;
import com.groovyfly.common.interfaces.UserAware;
import com.groovyfly.common.structures.Category;
import com.groovyfly.common.structures.User;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

/**
 * @author Chris Hatton
 */
public class CategoryAction extends CRUDAction implements UserAware, ModelDriven<User>, Preparable {

    private static final long serialVersionUID = -4421788331807002963L;
    
    private User user;

    private CategoryServiceIF categoryServiceIF;
    
    private UrlManagerServiceIF urlManagerServiceIF;

    private List<Category> categories;

    private Category category;

    private int categoryId;
    
    private String destination = "category";
    
    /* 
     * @see com.groovyfly.common.interfaces.UserAware#setUser(com.groovyfly.common.structures.User)
     */
    @Override
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Set by Spring IOC container.
     */
    public void setCategoryServiceIF(CategoryServiceIF categoryServiceIF) {
        this.categoryServiceIF = categoryServiceIF;
    }
    
    /**
     * Set by Spring IOC container.
     */
    public void setUrlManagerServiceIF(UrlManagerServiceIF urlManagerServiceIF) {
        this.urlManagerServiceIF = urlManagerServiceIF;
    }

    /* 
     * @see com.opensymphony.xwork2.Preparable#prepare()
     */
    @Override
    public void prepare() throws Exception { 
        categories = categoryServiceIF.getCategories(false);
    }

    /* 
     * @see com.opensymphony.xwork2.ModelDriven#getModel()
     */
    @Override
    public User getModel() {
        return user;
    }

    /* 
     * @see com.groovyfly.common.actions.CRUDAction#list()
     */
    @Override
    public String list() throws Exception {
        categories = categoryServiceIF.getCategories(false);

        return CRUDAction.LIST;
    }

    /* 
     * @see com.groovyfly.common.actions.CRUDAction#create()
     */
    @Override
    public String create() throws Exception {
        setCrudOperation(CREATE);
        setReadOnly(false);

        category = new Category();

        return Action.SUCCESS;
    }

    /* 
     * @see com.groovyfly.common.actions.CRUDAction#read()
     */
    @Override
    public String read() throws Exception {
        try {
            setCrudOperation(READ);
            setReadOnly(true);

            category = categoryServiceIF.getCategory(categoryId);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return Action.SUCCESS;
    }

    /* 
     * @see com.groovyfly.common.actions.CRUDAction#update()
     */
    @Override
    public String update() throws Exception {
        try {
            setCrudOperation(UPDATE);
            setReadOnly(false);

            category = categoryServiceIF.getCategory(categoryId);
            categories.remove(category); // remove the current category as we don't want to set the parent category as itself      
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return Action.SUCCESS;
    }

    /* 
     * @see com.groovyfly.common.actions.CRUDAction#delete()
     */
    @Override
    public String delete() throws Exception {
        categoryServiceIF.deleteCategory(categoryId);

        return this.list();
    }
    
    public void validateDelete() throws Exception {
        // TODO product count does not take into account the retired ones - what should we do with these. Delete them first?????
        int productCountInCategory = categoryServiceIF.getProductCountInCategory(categoryId);
        int categoryCountInCategory = categoryServiceIF.getCategoryCountInCategory(categoryId);
        
        if (categoryCountInCategory == 1) {
            destination = "category-list";
            this.addActionError("You have one sub-category that must be deleted first.");
        } else if (categoryCountInCategory > 1) {
            destination = "category-list";
            this.addActionError("You have " + categoryCountInCategory + " sub-categories that must be deleted first.");
        } 
        
        if (productCountInCategory == 1) {
            destination = "category-list";
            this.addActionError("You have one product in this category that must be deleted first.");
        } else if (productCountInCategory > 1) {
            destination = "category-list";
            this.addActionError("You have " + categoryCountInCategory + " products in this category that must be deleted first.");
        }
    }

    /* 
     * @see com.groovyfly.common.actions.CRUDAction#save()
     */
    @Override
    public String save() throws Exception {
        setCrudOperation(SAVE);
        
        if (category.getCategoryId() > 0) {
            categoryServiceIF.updateCategory(category);
        } else {
            categoryServiceIF.insertCategory(category);
        }

        return this.list();
    }
    
    public void validateSave() throws Exception {
        if (this.category.getName() == null || this.category.getName().trim().equals("")) {
            this.addFieldError("category.name", "A category name is required.");
        } else if (this.category.getName().trim().length() < 4){
            this.addFieldError("category.name", "The category name must be over 4 characters in length.");
        }
       
        String urlAlias = null;
        if (category.getUrlAlias() == null || category.getUrlAlias().trim().equals("")) {
            try {
                if (category.getPage() != null && category.getPage().getPageId() > 0) {
                    urlAlias = urlManagerServiceIF.getUrlAliasForExistingCategory(category.getPage().getPageId(), category.getParentId(), category.getName());                    
                } else {
                    urlAlias = urlManagerServiceIF.getUrlAliasForNewCategory(category.getParentId(), category.getName());    
                }
                category.setUrlAlias(urlAlias);
                category.getPage().setUrlAlias(urlAlias);
            } catch (InvalidUrlAliasException e) {
                this.addFieldError("category.name", "Category name does not translate to valid URL alias. Change name or specify a URL alias.");
            } catch (DuplicateUrlAliasException e) {
                this.addFieldError("category.name", "Category name URL alias translation in use. Change name or specify a URL alias.");
            }
        } else {
            try {
                if (category.getPage() != null && category.getPage().getPageId() > 0) {
                    urlAlias = urlManagerServiceIF.getUrlAliasForExistingCategory(category.getPage().getPageId(), category.getParentId(), category.getUrlAlias());
                } else {
                    urlAlias = urlManagerServiceIF.getUrlAliasForNewCategory(category.getParentId(), category.getUrlAlias());      
                }
                category.setUrlAlias(urlAlias);
                category.getPage().setUrlAlias(urlAlias);
            } catch (InvalidUrlAliasException e) {
                this.addFieldError("category.page.urlAlias", "Invalid URL alias");
            } catch (DuplicateUrlAliasException e) {
                this.addFieldError("category.page.urlAlias", "URL alias is already in use. Please specify another one.");
            }
        }
        
        if (this.category.getUrlAlias().length() > 2000) {
            this.addFieldError("category.page.urlAlias", "A URL alias is too long (2000 char max).");
        } 
        
        if (this.category.getName().length() > 50) {
            this.addFieldError("category.name", "Category name is too long (50 char max).");
        } 
        
        if (this.category.getDescription().length() > 100) {
            this.addFieldError("category.description", "Category description is too long (100 char max).");
        } 
    }

    public String up() throws Exception {
        categoryServiceIF.moveUpIndex(categoryId);

        return this.list();
    } 

    public String down() throws Exception {
        categoryServiceIF.moveDownIndex(categoryId);

        return this.list();
    }

    /* 
     * @see com.groovyfly.common.actions.CRUDAction#getDestination()
     */
    @Override
    public String getDestination() {
        return destination;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

}
