/*
 * ProductDisplayAction.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.actions.shopping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.groovyfly.admin.service.productmanagement.ProductServiceIF;
import com.groovyfly.common.structures.GroupingProduct;
import com.groovyfly.common.structures.Product;
import com.groovyfly.common.structures.ProductAttribute;
import com.groovyfly.common.structures.ProductAttributeValue;
import com.groovyfly.site.actions.BaseSiteAction;
import com.opensymphony.xwork2.Action;

/**
 * This is the action that is called when the customer navigates to the product page. Based on the url it will get all the details 
 * about the product ready for display.
 * 
 * @author Chris Hatton
 */
public class ProductDisplayAction extends BaseSiteAction implements ServletRequestAware {

    private static final long serialVersionUID = -8754449161752801724L;
    
    private ProductServiceIF productServiceIF;
    
    private HttpServletRequest request;
    
    /** Quick display page will be used if we have a product id */
    private int productId;
    
    /** Normal display page will be used if we are using the url as id */
    private String productUrlAlias;
    
    private Product product;
    
    private GroupingProduct groupingProduct;
    
    private boolean multiProductDisplay;
    
    private List<ProductAttribute> productAttributes;
    
    public void setProductServiceIF(ProductServiceIF productServiceIF) {
        this.productServiceIF = productServiceIF;
    }
    
    /* 
     * @see org.apache.struts2.interceptor.ServletRequestAware#setServletRequest(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }
    
    /* 
     * @see com.opensymphony.xwork2.ActionSupport#execute()
     */
    @Override
    public String execute() throws Exception {
        try {
            parseUrl();
            if (productId != 0) {
                product = productServiceIF.getProduct(productId);
            } else {
                product = productServiceIF.getProduct(productUrlAlias);    
            }
            super.page = product.getPage();
            
            if (product instanceof GroupingProduct) {
                groupingProduct = (GroupingProduct) product;
                multiProductDisplay = true;
            }
            
            productAttributes = getProductAttributes(product); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
       
        return Action.SUCCESS;
    }
    
    /*
     * Parses the url used to call this action to extract the url alias used to identify the product the user wants
     * to view.
     */
    private void parseUrl() {
        this.productUrlAlias = StringUtils.removeStart(request.getRequestURI(), request.getContextPath() + "/shop/product");
    }
    
    /*
     * Gets the product attributes and attribute values to display for the user to choose from. Note that only attribute 
     * values assigned to products on display will returned so that they can't choose a product not in the system.
     */
    private List<ProductAttribute> getProductAttributes(Product product) throws Exception {
        // see what attributes we have to choose from for the given product
        List<Integer> productAttributeIds = new ArrayList<Integer>();
        if (product.getAttributeId1() != 0) {
            productAttributeIds.add(product.getAttributeId1());
        }
        if (product.getAttributeId2() != 0) {
            productAttributeIds.add(product.getAttributeId2());
        }
        if (product.getAttributeId3() != 0) {
            productAttributeIds.add(product.getAttributeId3());
        }
        
        // get the attributes and all the possible values for the attributes
        List<ProductAttribute> productAttributes = productServiceIF.getProductAttributes(productAttributeIds);
        
        // now filter out the attribute values to only show the ones relevant for the product
        removeAttributeValuesNotInUse(product, productAttributes);
        
        return productAttributes;
    }

    /*
     * Filters out attribute values that we are not using, so that they can't choose a product not in the system.
     */
    private void removeAttributeValuesNotInUse(Product product, List<ProductAttribute> productAttributes) {

        // collect the permissible attribute values
        Map<Integer, HashSet<Integer>> permissableAttributeToAttributeValues = new HashMap<Integer, HashSet<Integer>>();
        
        if (product instanceof GroupingProduct) {
            // if grouping product then get all the attributes of the child products
            for (Product p : ((GroupingProduct) product).getProducts()) {
                if (p.getAttribute1() != null) {
                    if (permissableAttributeToAttributeValues.containsKey(p.getAttribute1().getId())) {
                        permissableAttributeToAttributeValues.get(p.getAttribute1().getId()).add(p.getAttribute1().getChoosenAttributeValue().getId());
                    } else {
                        HashSet<Integer> permissableAttributeValues = new HashSet<Integer>();
                        permissableAttributeValues.add(p.getAttribute1().getChoosenAttributeValue().getId());
                        permissableAttributeToAttributeValues.put(p.getAttribute1().getId(), permissableAttributeValues);
                    }    
                }
                if (p.getAttribute2() != null) {
                    if (permissableAttributeToAttributeValues.containsKey(p.getAttribute2().getId())) {
                        permissableAttributeToAttributeValues.get(p.getAttribute2().getId()).add(p.getAttribute2().getChoosenAttributeValue().getId());
                    } else {
                        HashSet<Integer> permissableAttributeValues = new HashSet<Integer>();
                        permissableAttributeValues.add(p.getAttribute2().getChoosenAttributeValue().getId());
                        permissableAttributeToAttributeValues.put(p.getAttribute2().getId(), permissableAttributeValues);
                    }    
                }
                if (p.getAttribute3() != null) {
                    if (permissableAttributeToAttributeValues.containsKey(p.getAttribute3().getId())) {
                        permissableAttributeToAttributeValues.get(p.getAttribute3().getId()).add(p.getAttribute3().getChoosenAttributeValue().getId());
                    } else {
                        HashSet<Integer> permissableAttributeValues = new HashSet<Integer>();
                        permissableAttributeValues.add(p.getAttribute3().getChoosenAttributeValue().getId());
                        permissableAttributeToAttributeValues.put(p.getAttribute3().getId(), permissableAttributeValues);
                    }    
                }
            }
        } else {
            // if just the product then it is only the attributes of that product is permissible
            if (product.getAttributeId1() != 0) {
                HashSet<Integer> permissableAttributeValues = new HashSet<Integer>();
                permissableAttributeValues.add(product.getAttributeValueId1());
                permissableAttributeToAttributeValues.put(product.getAttributeId1(), permissableAttributeValues);
            }
            if (product.getAttributeId2() != 0) {
                HashSet<Integer> permissableAttributeValues = new HashSet<Integer>();
                permissableAttributeValues.add(product.getAttributeValueId2());
                permissableAttributeToAttributeValues.put(product.getAttributeId2(), permissableAttributeValues);
            }
            if (product.getAttributeId3() != 0) {
                HashSet<Integer> permissableAttributeValues = new HashSet<Integer>();
                permissableAttributeValues.add(product.getAttributeValueId3());
                permissableAttributeToAttributeValues.put(product.getAttributeId3(), permissableAttributeValues); 
            }
        }
        
        // now remove attribute values if they are not permissible
        List<ProductAttributeValue> toRemove = new ArrayList<ProductAttributeValue>();
        for (ProductAttribute pa : productAttributes) {
            toRemove.clear();
            for (ProductAttributeValue pav : pa.getProductAttributeValues()) {
                if (!permissableAttributeToAttributeValues.get(pa.getId()).contains(pav.getId())) {
                    toRemove.add(pav);
                }
            }
            for (ProductAttributeValue pav : toRemove) {
                pa.getProductAttributeValues().remove(pav);
            }
        }
    }
    
    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Product getProduct() {
        return product;
    }

    public GroupingProduct getGroupingProduct() {
        return groupingProduct;
    }

    public boolean isMultiProductDisplay() {
        return multiProductDisplay;
    }

    public List<ProductAttribute> getProductAttributes() {
        return productAttributes;
    }

    /**
     * For the attributes we have it will return the default attribute value id for the attribute id given.
     */
    public Integer getDefaultAttributeValueId(int productAttributeId) {
        for (ProductAttribute pa : productAttributes) {
            if (pa.getId() == productAttributeId) {
                for (ProductAttributeValue pav : pa.getProductAttributeValues()) {
                    if (pav.isDefaultChoosen()) {
                        return pav.getId();
                    }
                }
                // if no default then it will have been filtered out so just choose the first one
                return pa.getProductAttributeValues().get(0).getId();
            } 
        }
        
        // should never get here
        return -1;
    }
}
