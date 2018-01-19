/*
 * PurchaseAction.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.actions.shopping;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.apache.struts2.interceptor.SessionAware;

import com.groovyfly.common.structures.ShoppingCart;
import com.groovyfly.common.structures.ShoppingCartEntry;
import com.groovyfly.common.structures.exceptions.InvalidQuantityException;
import com.groovyfly.common.structures.exceptions.NotEnoughStockException;
import com.groovyfly.common.util.SessionKey;
import com.groovyfly.site.service.shopping.ShoppingServiceIF;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Chris Hatton
 */
public class PurchaseAction extends ActionSupport implements SessionAware {

    private static final long serialVersionUID = -6911998310573939509L;
    
    private static Logger log = Logger.getLogger(PurchaseAction.class.getName());
    
    private static final int SINGLE_PRODUCT_DISPLAY_TYPE = 0;
    
    private static final int NON_CONFIGURABLE_GROUPED_PRODUCT_DISPLAY_TYPE = 1;
    
    private static final int CONFIGURABLE_GROUPED_PRODUCT_DISPLAY_TYPE = 2;
    
    private ShoppingServiceIF shoppingServiceIF;
    
    private Map<String, Object> session;
    
    private int productDisplayType;
    
    private int productId;
    
    private Map<String, Integer> attributeToAttributeValue = new HashMap<String, Integer>();
    
    @SuppressWarnings("unused")
    private Map<String, Integer> productIdToQuantity = new HashMap<String, Integer>();
    
    private int quantity;
    
    private ShoppingCart shoppingCart;
    
    private ShoppingCartEntry shoppingCartEntry;
    
    private String errorMessage;
    
    private String stockMessage;
    
    private int quantityLeftInStock;

    /**
     * This will be set by the Spring ICO container.
     * 
     * @param shoppingServiceIF
     */
    public void setShoppingServiceIF(ShoppingServiceIF shoppingServiceIF) {
        this.shoppingServiceIF = shoppingServiceIF;
    }
    
    /* 
     * @see org.apache.struts2.interceptor.SessionAware#setSession(java.util.Map)
     */
    @Override
    public void setSession(Map<String, Object> session) {
       this.session = session; 
    }

    /* 
     * @see com.opensymphony.xwork2.ActionSupport#execute()
     */
    @Override
    public String execute() throws Exception {
        log.info("purchase product");
        
        try {
            this.validateParams();
            
            // get existing shopping cart, if not one then create it and add to the user session
            Object object = session.get(SessionKey.SHOPPING_CART);
            if (object != null) {
                shoppingCart = (ShoppingCart) object;
            } else {
                UUID randomUUID = UUID.randomUUID();
                String shoppingCartId = randomUUID.toString();
                shoppingCart = new ShoppingCart(shoppingCartId, Locale.UK);
            }
            
            // decide how to process the order based on how the product(s) are displayed
            if (productDisplayType == SINGLE_PRODUCT_DISPLAY_TYPE) {
                this.addProductToCart(shoppingCart);
            } else if (productDisplayType == NON_CONFIGURABLE_GROUPED_PRODUCT_DISPLAY_TYPE) {
                this.addNonConfigurableGroupingProductToCart(shoppingCart);
            } else if (productDisplayType == CONFIGURABLE_GROUPED_PRODUCT_DISPLAY_TYPE) {
                this.addConfigurableGroupingProductToCart(shoppingCart);    
            } else {
                throw new Exception("display type not recognised");
            }   
            
            // update session for gae so that the new shopping cart will be persisted
            session.put(SessionKey.SHOPPING_CART.toString(), shoppingCart);
           
            
        } catch (InvalidQuantityException e) {
            this.errorMessage = "Trying to add quantities less than one is not permitted.";
        } catch (NotEnoughStockException e) {
            this.quantityLeftInStock = e.getQuantityLeft();
            if (this.quantityLeftInStock == 0) {
                this.stockMessage = "Sorry, there are no more, " + e.getProductItemName() + ", left in stock.";
            } else if (this.quantityLeftInStock == 1) {
                this.stockMessage = "Sorry, there is only " + e.getQuantityLeft() + ", " + e.getProductItemName() + ", left in stock.";
            } else {
                this.stockMessage = "Sorry, there are only " + e.getQuantityLeft() + ", " + e.getProductItemName() + ", left in stock.";    
            }
        } catch (Exception e) {
            e.printStackTrace();
            // TODO we need to log this
            // SERIOUS PROBLEM if we get an error here this should be recorded for immediate investigation 
            this.errorMessage = "There seems to be a problem with the site at the moment. This has been loggged and is been dealt with by our technical team.";
        }
        
        return ActionSupport.SUCCESS;
    }
    
    private void validateParams() throws InvalidQuantityException {
        if (quantity < 1) {
            throw new InvalidQuantityException("");
        }
    }
    
    private void addConfigurableGroupingProductToCart(ShoppingCart shoppingCart) throws NotEnoughStockException, Exception {
        this.shoppingCartEntry = shoppingServiceIF.addConfigurableGroupingProductToCart(
                shoppingCart, 
                productId, 
                attributeToAttributeValue, 
                quantity);
    }
    
    private void addNonConfigurableGroupingProductToCart(ShoppingCart shoppingCart) throws Exception {
        // TODO implement
    }
    
    private void addProductToCart(ShoppingCart shoppingCart) throws Exception {
        // TODO implement
    }
    
    public void setProductDisplayType(int productDisplayType) {
        this.productDisplayType = productDisplayType;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Map<String, Integer> getAttributeToAttributeValue() {
        return attributeToAttributeValue;
    }

    public void setAttributeToAttributeValue(Map<String, Integer> attributeToAttributeValue) {
        this.attributeToAttributeValue = attributeToAttributeValue;
    }

    public void setProductIdToQuantity(Map<String, Integer> productIdToQuantity) {
        this.productIdToQuantity = productIdToQuantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }
    
    public ShoppingCartEntry getShoppingCartEntry() {
        return shoppingCartEntry;
    }

    public void setShoppingCartEntry(ShoppingCartEntry shoppingCartEntry) {
        this.shoppingCartEntry = shoppingCartEntry;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getStockMessage() {
        return stockMessage;
    }

    public void setStockMessage(String stockMessage) {
        this.stockMessage = stockMessage;
    }

    public int getQuantityLeftInStock() {
        return quantityLeftInStock;
    }

    public void setQuantityLeftInStock(int quantityLeftInStock) {
        this.quantityLeftInStock = quantityLeftInStock;
    }
}
