/*
 * RoyalMailPostageCalculator.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.postage;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import com.groovyfly.admin.service.postage.PostageServiceIF;
import com.groovyfly.common.structures.ShippingAddress;
import com.groovyfly.common.structures.ShoppingCart;
import com.groovyfly.common.structures.ShoppingCartEntry;

/**
 * @author Chris Hatton
 */
public class RoyalMailPostageCalculator {
    
    private static Logger log = Logger.getLogger(RoyalMailPostageCalculator.class.getName());
    
    private final String ROYAL_MAIL_COURIER_CODE = "01";
    
    private final int LARGE_LETTER_ESTIMATE_WEIGHT = 100; // grams
    
    private final int PARCEL_ESTIMATE_WEIGHT = 300; // grams
    
    private PostageServiceIF postageServiceIF;
    
    private CustomsDeclarationType customsDeclaration = CustomsDeclarationType.NONE;
    
    private PostalPackagingType packagingType; 
    
    private int packagingWeight;

    private PostalZone postageZone;
    
    
    /**
     * Constructor
     */
    public RoyalMailPostageCalculator() {
        super();
    }
    
    public void setPostageServiceIF(PostageServiceIF postageServiceIF) {
        this.postageServiceIF = postageServiceIF;
    }

    public BigDecimal getEstimatedPostage(ShoppingCart shoppingCart, Locale locale) throws Exception {
        
        log.info(locale.getCountry());
        
        return this.getPostageCost(shoppingCart, locale.getCountry());
    }
    
    public BigDecimal getActualPostage(ShoppingCart shoppingCart, ShippingAddress shippingAddress) throws Exception {
        return this.getPostageCost(shoppingCart, shippingAddress.getCountryCode());
    }
    
    private BigDecimal getPostageCost(ShoppingCart shoppingCart, String countryCode) throws Exception {
        
        
        
        this.customsDeclaration = this.getCustomsDeclarationRequired(shoppingCart, countryCode);
        
        log.info(customsDeclaration.name());
        
        this.packagingType = this.getSuggestedPackaging(shoppingCart.getShoppingCartEntries());
        this.postageZone = this.getPostageZone(countryCode);
        
        log.info(postageZone.getZoneDescription());
        
        return getPostageCost(shoppingCart, packagingType, postageZone, packagingWeight);
    }

    /*
     * Calculate the customs declaration required for this order.
     */
    private CustomsDeclarationType getCustomsDeclarationRequired(ShoppingCart shoppingCart, String countryCode) throws Exception {
        try {
            boolean europeanUnionCountry = postageServiceIF.isEuropeanUnionCountry(countryCode);
            
            if (!europeanUnionCountry) {
                if (shoppingCart.getTotal().compareTo(new BigDecimal(270.00)) <= 0) {
                    return CustomsDeclarationType.CN22;
                } else {
                    return CustomsDeclarationType.CN23;
                }
            } else {
                return CustomsDeclarationType.NONE;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    /*
     * Gets the estimated packaging type that will be required for the order.
     */
    private PostalPackagingType getSuggestedPackaging(List<ShoppingCartEntry> shoppingCartEntries) throws Exception {
        int minimumInternalPackagingLength = 0;
        int minimumInternalPackagingWidth = 0;
        int minimumInternalPackagingHeight = 0;
        int totalProductWeight = 0;
        
        for (ShoppingCartEntry e : shoppingCartEntries) {
            if (e.getLength() > minimumInternalPackagingLength) {
                minimumInternalPackagingLength = e.getLength();
            }
            if(e.getWidth() > minimumInternalPackagingWidth) {
                minimumInternalPackagingWidth = e.getWidth();
            }
            if (e.getHeight() > minimumInternalPackagingHeight) {
                minimumInternalPackagingHeight = e.getHeight();
            }
            
            totalProductWeight += e.getWeight();
        }
        
        if (minimumInternalPackagingHeight < 20) {
            this.packagingWeight = totalProductWeight + LARGE_LETTER_ESTIMATE_WEIGHT;
            return PostalPackagingType.LARGE_LETTER;
        } else {
            this.packagingWeight = totalProductWeight + PARCEL_ESTIMATE_WEIGHT;
            return PostalPackagingType.PARCEL;
        }
    }
    
    /*
     * Gets the Royal Mail postage zone for the country code given.
     */
    private PostalZone getPostageZone(String countryCode) throws Exception {
        try {
            return postageServiceIF.getPostageZone(ROYAL_MAIL_COURIER_CODE, countryCode);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    /*
     * Gets the postage cost. At the moment this is just what we are going to charge the customer ( a fixed cost ). In
     * the future we could make this more sophisticated, and actually work out based on the courier prices. 
     * 
     * NOTE: if we are going to offer free delivery over a certain order value, this would be the place to make that
     * determination.
     */
    private BigDecimal getPostageCost(ShoppingCart shoppingCart, PostalPackagingType packagingType, PostalZone postageZone, int packagingWeight) {
        if (postageZone.getZoneCode().equals("01")) { 
            // uk
            return new BigDecimal(1.50);
        } else { 
            // rest of the world
            return new BigDecimal(2.50);
        }
    }

    public CustomsDeclarationType getCustomsDeclaration() {
        return customsDeclaration;
    }

    public PostalPackagingType getPackagingType() {
        return packagingType;
    }

    public int getPackagingWeight() {
        return packagingWeight;
    }

    public PostalZone getPostageZone() {
        return postageZone;
    }
}
