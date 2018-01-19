/*
 * Product.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.structures;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * @author Chris Hatton
 */
public class Product {
    
    private static Logger log = Logger.getLogger(Product.class.getName());
    
    private int productId;
    
    private String urlAlias;
    
    /** this is for the attribute configuration i.e. configurable / non-configurable */
    private int productGroupingConfigId;
    
    private int attributeId1;
    
    private int attributeId2;
    
    private int attributeId3;
    
    private int attributeValueId1;
    
    private int attributeValueId2;
    
    private int attributeValueId3;
    
    private ProductAttribute attribute1;
    
    private ProductAttribute attribute2;
    
    private ProductAttribute attribute3;
    
    private String name;
    
    private int categoryId;
    
    private String description;
    
    private ProductImages images = new ProductImages();
    
    private int supplierId;
    
    private String sku;
    
    private BigDecimal price = new BigDecimal(0.00);
    
    private int priceRuleId;
    
    private int vatRuleId;
    
    private String storeageLocation;
    
    private int stockLevel;
    
    private int statusId;
    
    private byte averageStarRating;
    
    private Page page = new Page();
    
    private int inNoOfGroupings;
    
    private boolean retired;
    
    /** The locale, as relevant to the user */
    @SuppressWarnings("unused")
    private Locale locale = Locale.UK;
    
    /** Currency formatter */
    private NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.UK);
    
    /**
     * Constructor
     */
    public Product() {
        super();
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getUrlAlias() {
        return urlAlias;
    }

    public void setUrlAlias(String urlAlias) {
        this.urlAlias = urlAlias;
    }

    public int getProductGroupingConfigId() {
        return productGroupingConfigId;
    }

    public void setProductGroupingConfigId(int productGroupingConfigId) {
        this.productGroupingConfigId = productGroupingConfigId;
    }

    public int getAttributeId1() {
        return attributeId1;
    }

    public void setAttributeId1(int attributeId1) {
        this.attributeId1 = attributeId1;
    }

    public int getAttributeId2() {
        return attributeId2;
    }

    public void setAttributeId2(int attributeId2) {
        this.attributeId2 = attributeId2;
    }

    public int getAttributeId3() {
        return attributeId3;
    }

    public void setAttributeId3(int attributeId3) {
        this.attributeId3 = attributeId3;
    }

    public int getAttributeValueId1() {
        return attributeValueId1;
    }

    public void setAttributeValueId1(int attributeValueId1) {
        this.attributeValueId1 = attributeValueId1;
    }

    public int getAttributeValueId2() {
        return attributeValueId2;
    }

    public void setAttributeValueId2(int attributeValueId2) {
        this.attributeValueId2 = attributeValueId2;
    }

    public int getAttributeValueId3() {
        return attributeValueId3;
    }

    public void setAttributeValueId3(int attributeValueId3) {
        this.attributeValueId3 = attributeValueId3;
    }

    public ProductAttribute getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(ProductAttribute attribute1) {
        this.attribute1 = attribute1;
    }

    public ProductAttribute getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(ProductAttribute attribute2) {
        this.attribute2 = attribute2;
    }

    public ProductAttribute getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(ProductAttribute attribute3) {
        this.attribute3 = attribute3;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductImages getImages() {
        return images;
    }

    public void setImages(ProductImages images) {
        this.images = images;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getPriceRuleId() {
        return priceRuleId;
    }

    public void setPriceRuleId(int priceRuleId) {
        this.priceRuleId = priceRuleId;
    }

    public int getVatRuleId() {
        return vatRuleId;
    }

    public void setVatRuleId(int vatRuleId) {
        this.vatRuleId = vatRuleId;
    }

    public String getStoreageLocation() {
        return storeageLocation;
    }

    public void setStoreageLocation(String storeageLocation) {
        this.storeageLocation = storeageLocation;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public byte getAverageStarRating() {
        return averageStarRating;
    }

    public void setAverageStarRating(byte averageStarRating) {
        this.averageStarRating = averageStarRating;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public int getInNoOfGroupings() {
        return inNoOfGroupings;
    }

    public void setInNoOfGroupings(int inNoOfGroupings) {
        this.inNoOfGroupings = inNoOfGroupings;
    }

    public boolean isRetired() {
        return retired;
    }

    public void setRetired(boolean retired) {
        this.retired = retired;
    }
    
    /**
     * Gets the unit price of this product with currency symbol included. 
     */
    public String getPriceString() {
        
        log.info("getPriceString() " + numberFormat.format(price.doubleValue())); 
        
        return numberFormat.format(price.doubleValue());
    }
    
}
