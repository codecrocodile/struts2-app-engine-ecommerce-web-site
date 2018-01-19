/*
 * ShoppingCartEntry.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.structures;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Represents one entry made to the shopping cart for a product unit.
 * 
 * @author Chris Hatton
 */
public class ShoppingCartEntry implements Serializable {

    private static final long serialVersionUID = 5124435780387976382L;
    
    /** Database primary key for this product unit */
    private int productId;
    
    /** Name of the product as sold on the website */
    private String name;
    
    /** Small square image of the product */
    private String smallerImageUrl;
    
    /** Full sized rectangular image of the product */
    private String largeImageUrl;
    
    /** Description of product images to be used */
    private String imageAltTagDesc;
    
    /** Stock Keeping Unit code assigned by our company */
    private String sku;
    
    /** Length of product, this is the largest dimension usually (mm) */
    private int length;
    
    /** Width of product, this is the second largest dimension usually (mm)*/
    private int width;
    
    /** Height of product, this is the smallest dimension usually (mm) **/
    private int height;
    
    /** Weight of product, including any packaging it comes with from supplier */ 
    private int weight;
    
    /** Unit price of the product as sold on the website */
    private BigDecimal unitPrice = new BigDecimal(0.00);
    
    /** The quantity of the product the customer wants */
    private int quantity;
    
    /** the quantity of the product we have to stock */
    private int quantityInStock = Integer.MAX_VALUE;

    /** The locale, as relevant to the user */
    private Locale locale = Locale.UK;
    
    /** Currency formatter */
    private NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.UK);
    
    /**
     * Constructor
     */
    public ShoppingCartEntry(Locale locale) {
        super();
        this.locale = locale;
        this.numberFormat = NumberFormat.getCurrencyInstance(locale);
    }
    
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSmallerImageUrl() {
        return smallerImageUrl;
    }

    public void setSmallerImageUrl(String smallerImageUrl) {
        this.smallerImageUrl = smallerImageUrl;
    }

    public String getLargeImageUrl() {
        return largeImageUrl;
    }

    public void setLargeImageUrl(String largeImageUrl) {
        this.largeImageUrl = largeImageUrl;
    }

    public String getImageAltTagDesc() {
        return imageAltTagDesc;
    }

    public void setImageAltTagDesc(String imageAltTagDesc) {
        this.imageAltTagDesc = imageAltTagDesc;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
    
    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }
    
    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        this.numberFormat = NumberFormat.getCurrencyInstance(locale);
    }
    
    /**
     * Gets the unit price of this product with currency symbol included. 
     */
    public String getUnitPriceString() {
        return numberFormat.format(unitPrice.doubleValue());
    }
    
    /**
     * Gets the total price of this product.
     */
    public BigDecimal getTotalPrice() {
        return this.unitPrice.multiply(new BigDecimal(quantity));
    }

    /**
     * Gets the unit price of this product with currency symbol included   
     */
    public String getTotalPriceString() {
        return numberFormat.format(this.getTotalPrice().doubleValue());
    }
    
    /**
     * Tells you if there is enough of the product in stock to fill the order.
     * 
     * @return true if we have enough in stock, false otherwise
     */
    public boolean isQuantityInStockEnough() {
        if (this.quantity <= this.quantityInStock) {
            return true;
        } else {
            return false;
        }
    }

    /* 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ShoppingCartEntry) {
            ShoppingCartEntry other = (ShoppingCartEntry) obj;
             if (other.productId == this.productId) {
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
        return this.productId;
    }
}
