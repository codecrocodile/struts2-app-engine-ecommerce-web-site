/*
 * GroupingAction.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.actions.productmanagement;

import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import com.groovyfly.admin.actions.CRUDAction;
import com.groovyfly.admin.service.configuration.DuplicateUrlAliasException;
import com.groovyfly.admin.service.configuration.InvalidUrlAliasException;
import com.groovyfly.admin.service.configuration.UrlManagerServiceIF;
import com.groovyfly.admin.service.productmanagement.CategoryServiceIF;
import com.groovyfly.admin.service.productmanagement.ProductServiceIF;
import com.groovyfly.common.interfaces.UserAware;
import com.groovyfly.common.structures.Category;
import com.groovyfly.common.structures.GroupingProduct;
import com.groovyfly.common.structures.Lookup;
import com.groovyfly.common.structures.ProductAttribute;
import com.groovyfly.common.structures.User;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

/**
 * @author Chris Hatton
 * 
 * Created 24 Aug 2012
 */
public class GroupingAction extends CRUDAction implements UserAware, ModelDriven<User>, Preparable {
    
    private static Logger log = Logger.getLogger(GroupingAction.class.getName());
    
    private static final long serialVersionUID = 2760159772715797232L;

    private User user;
    
    private ProductServiceIF productServiceIF;
    
    private CategoryServiceIF categoryServiceIF;
    
    private UrlManagerServiceIF urlManagerServiceIF;
    
    /** These are product summaries for the listing page */
    private List<GroupingProduct> products; 
    
    /** This is the product we are viewing, editing or creating */
    private GroupingProduct product = new GroupingProduct();
    
    /** Id for the product we are viewing or editing */
    private int productId = 0;
    
    private List<Category> categories;
    
    private List<ProductAttribute> productAttributes;
    
    private List<Lookup> productStatuses;
    
    private String largePhoto;
    
    private String largePhotoContentType;
    
    private String largePhotoFileName;
    
    private String mediumPhoto;
    
    private String mediumPhotoContentType;
    
    private String mediumPhotoFileName;
    
    private String smallPhoto;
    
    private String smallPhotoContentType;
    
    private String smallPhotoFileName;
    
    private String smallerPhoto;
    
    private String smallerPhotoContentType;
    
    private String smallerPhotoFileName;
    
    /**
     * Set by Spring IOC container.
     */
    public void setProductServiceIF(ProductServiceIF productServiceIF) {
        this.productServiceIF = productServiceIF;
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
     * @see com.opensymphony.xwork2.Preparable#prepare()
     */
    public void prepare() throws Exception {
        categories = categoryServiceIF.getCategories(true);
        productAttributes = productServiceIF.getProductAttributes();
        productStatuses = productServiceIF.getProductStatuses(true);
    }

    /* 
     * @see com.groovyfly.common.actions.CRUDAction#list()
     */
    @Override
    public String list() throws Exception {
        this.products = productServiceIF.getGroupingProductSummaries(false);
        
        return CRUDAction.LIST;
    }

    /* 
     * @see com.groovyfly.common.actions.CRUDAction#create()
     */
    @Override
    public String create() throws Exception {
        this.setCrudOperation(CRUDAction.CREATE);
        
        return CRUDAction.SUCCESS;
    }

    /* 
     * @see com.groovyfly.common.actions.CRUDAction#read()
     */
    @Override
    public String read() throws Exception {
        this.setCrudOperation(CRUDAction.READ);
        this.setReadOnly(true);
        
        this.product = (GroupingProduct) productServiceIF.getProduct(productId);
        
        return CRUDAction.SUCCESS;
    }

    /* 
     * @see com.groovyfly.common.actions.CRUDAction#update()
     */
    @Override
    public String update() throws Exception {
        this.setCrudOperation(CRUDAction.UPDATE);
        this.setReadOnly(false);
        
        this.product = (GroupingProduct) productServiceIF.getProduct(productId);
        
        return CRUDAction.SUCCESS;
    }

    /* 
     * @see com.groovyfly.common.actions.CRUDAction#delete()
     */
    @Override
    public String delete() throws Exception {
        this.setCrudOperation(CRUDAction.DELETE);
        productServiceIF.retireProduct(productId);

        return this.list();
    }

    /* 
     * @see com.groovyfly.common.actions.CRUDAction#save()
     */
    @Override
    public String save() throws Exception {
        if (largePhoto != null && largePhoto.length() > 0) {
            InputStream inputStream = IOUtils.toInputStream(largePhoto, "ISO-8859-1");
            product.getImages().setLargeImageBytes(IOUtils.toByteArray(inputStream));
            product.getImages().setLargeImageMimeType(largePhotoContentType);
            product.getImages().setLargeImageFileName(largePhotoFileName);
        }
        
        if (mediumPhoto != null && mediumPhoto.length() > 0) {
            InputStream inputStream = IOUtils.toInputStream(mediumPhoto, "ISO-8859-1");
            product.getImages().setMediumImageBytes(IOUtils.toByteArray(inputStream));
            product.getImages().setMediumImageMimeType(mediumPhotoContentType);
            product.getImages().setMediumImageFileName(mediumPhotoFileName);
        }
        
        if (smallPhoto != null && smallPhoto.length() > 0) {
            InputStream inputStream = IOUtils.toInputStream(smallPhoto, "ISO-8859-1");
            product.getImages().setSmallImageBytes(IOUtils.toByteArray(inputStream));
            product.getImages().setSmallImageMimeType(smallPhotoContentType);
            product.getImages().setSmallImageFileName(smallPhotoFileName);
        }
        
        if (smallerPhoto != null && smallerPhoto.length() > 0) {
            InputStream inputStream = IOUtils.toInputStream(smallerPhoto, "ISO-8859-1");
            product.getImages().setSmallerImageBytes(IOUtils.toByteArray(inputStream));
            product.getImages().setSmallerImageMimeType(smallerPhotoContentType);
            product.getImages().setSmallerImageFileName(smallerPhotoFileName);
        }
        
        productServiceIF.saveProduct(product);
       
        return this.list();
    }
    
    public void validateSave() throws Exception {
        log.info("validate save");
        
        String urlAlias = null;
        if (product.getUrlAlias() == null || product.getUrlAlias().trim().equals("")) {
            try {
                if (product.getPage() != null && product.getPage().getPageId() > 0) {
                    urlAlias = urlManagerServiceIF.getUrlAliasForExistingProduct(product.getPage().getPageId(), product.getCategoryId(), product.getName());                    
                } else {
                    urlAlias = urlManagerServiceIF.getUrlAliasForNewProduct(product.getCategoryId(), product.getName());    
                }
                product.setUrlAlias(urlAlias);
                product.getPage().setUrlAlias(urlAlias);
            } catch (InvalidUrlAliasException e) {
                this.addFieldError("product.name", "Product name does not translate to valid URL alias. Change name or specify a URL alias.");
            } catch (DuplicateUrlAliasException e) {
                this.addFieldError("product.name", "Product name URL alias translation in use. Change name or specify a URL alias.");
            }
        } else {
            try {
                if (product.getPage() != null && product.getPage().getPageId() > 0) {
                    urlAlias = urlManagerServiceIF.getUrlAliasForExistingProduct(product.getPage().getPageId(), product.getCategoryId(), product.getUrlAlias());
                } else {
                    urlAlias = urlManagerServiceIF.getUrlAliasForNewProduct(product.getCategoryId(), product.getUrlAlias());      
                }
                product.setUrlAlias(urlAlias);
                product.getPage().setUrlAlias(urlAlias);
            } catch (InvalidUrlAliasException e) {
                this.addFieldError("product.urlAlias", "Invalid URL alias");
            } catch (DuplicateUrlAliasException e) {
                this.addFieldError("product.urlAlias", "URL alias is already in use. Please specify another one.");
            }
        }
        
        //TODO validate all the fields
    }

    /* 
     * @see com.groovyfly.common.actions.CRUDAction#getDestination()
     */
    @Override
    public String getDestination() {
        return "groupings";
    }

    public List<GroupingProduct> getProducts() {
        return products;
    }

    public GroupingProduct getProduct() {
        return product;
    }

    public void setProduct(GroupingProduct product) {
        this.product = product;
    }
    
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<ProductAttribute> getProductAttributes() {
        return productAttributes;
    }

    public void setProductAttributes(List<ProductAttribute> productAttributes) {
        this.productAttributes = productAttributes;
    }

    public List<Lookup> getProductStatuses() {
        return productStatuses;
    }

    public void setProductStatuses(List<Lookup> productStatuses) {
        this.productStatuses = productStatuses;
    }
    
    public void setLargePhoto(String largePhoto) {
        this.largePhoto = largePhoto;
    }

    public void setLargePhotoContentType(String largePhotoContentType) {
        this.largePhotoContentType = largePhotoContentType;
    }

    public void setLargePhotoFileName(String largePhotoFileName) {
        this.largePhotoFileName = largePhotoFileName;
    }

    public void setMediumPhoto(String mediumPhoto) {
        this.mediumPhoto = mediumPhoto;
    }

    public void setMediumPhotoContentType(String mediumPhotoContentType) {
        this.mediumPhotoContentType = mediumPhotoContentType;
    }

    public void setMediumPhotoFileName(String mediumPhotoFileName) {
        this.mediumPhotoFileName = mediumPhotoFileName;
    }

    public void setSmallPhoto(String smallPhoto) {
        this.smallPhoto = smallPhoto;
    }

    public void setSmallPhotoContentType(String smallPhotoContentType) {
        this.smallPhotoContentType = smallPhotoContentType;
    }

    public void setSmallPhotoFileName(String smallPhotoFileName) {
        this.smallPhotoFileName = smallPhotoFileName;
    }

    public void setSmallerPhoto(String smallerPhoto) {
        this.smallerPhoto = smallerPhoto;
    }

    public void setSmallerPhotoContentType(String smallerPhotoContentType) {
        this.smallerPhotoContentType = smallerPhotoContentType;
    }

    public void setSmallerPhotoFileName(String smallerPhotoFileName) {
        this.smallerPhotoFileName = smallerPhotoFileName;
    }
}
