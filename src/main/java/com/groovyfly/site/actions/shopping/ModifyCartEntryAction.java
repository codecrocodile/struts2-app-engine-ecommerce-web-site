/*
 * ModifyCartEntryAction.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.actions.shopping;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.struts2.interceptor.SessionAware;

import com.groovyfly.admin.sales.Discount;
import com.groovyfly.admin.sales.DiscountApplier;
import com.groovyfly.admin.sales.DiscountApplierFactory;
import com.groovyfly.admin.sales.DiscountException;
import com.groovyfly.admin.service.sales.DiscountsServiceIF;
import com.groovyfly.common.structures.ShoppingCart;
import com.groovyfly.common.structures.ShoppingCartEntry;
import com.groovyfly.common.structures.exceptions.NotEnoughStockException;
import com.groovyfly.common.util.SessionKey;
import com.groovyfly.site.service.shopping.ShoppingServiceIF;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Action used to modify the shopping cart. They will typically do this when viewing the shopping cart and before
 * they are ready to pay for the items in their cart.
 * 
 * @author Chris Hatton
 */
public class ModifyCartEntryAction extends ActionSupport implements SessionAware {
    
    private static final long serialVersionUID = 7584243309355582703L;
    
    private static Logger log = Logger.getLogger(ModifyCartEntryAction.class.getName());
    
    private DiscountsServiceIF discountsServiceIF;
    
    private ShoppingServiceIF shoppingServiceIF;
    
    private int productId;
    
    private String discountCode;
    
    
    private String newCartEntryTotal;
    
    private String newCartEntryItemCount;
    
    private BigDecimal subTotal;
    
    private String newSubTotal;
    
    private String newPostageAndPacking;
    
    
    private String newDiscountName;
    
    private String newDiscountTotal;
    
    private boolean discountSuccessful = false;
    
    private String discountMessage;
    
    
    private String newTotal;
    
    private String newItemCount;
    
    private int quantityLeftInStock;
    
    private boolean enoughInStock;
    
    private boolean enoughInStockForFullOrder;
    
    private boolean incrementSccessful;
    
    private Map<String, Object> session;
    
    /* 
     * @see org.apache.struts2.interceptor.SessionAware#setSession(java.util.Map)
     */
    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    /**
     * This will be set by the String ICO container.
     */
    public void setDiscountsServiceIF(DiscountsServiceIF discountsServiceIF) {
        this.discountsServiceIF = discountsServiceIF;
    }
    
    /**
     * This will be set by the String ICO container.
     */
    public void setShoppingServiceIF(ShoppingServiceIF shoppingServiceIF) {
        this.shoppingServiceIF = shoppingServiceIF;
    }

    public String incrementItem() throws Exception {
        try {
            ShoppingCart shoppingCart = (ShoppingCart) session.get(SessionKey.SHOPPING_CART);
            
            for (ShoppingCartEntry e : shoppingCart.getShoppingCartEntries()) {
                if (e.getProductId() == productId) {
                    
                    this.newCartEntryItemCount = Integer.toString(e.getQuantity());
                    this.quantityLeftInStock = e.getQuantityInStock();
                    this.setNewShoppingCartValuesForDisplay(shoppingCart); // just in case exception is thrown we always want to return the page values
                    
                    // 1. increment the quantity
                    e.setQuantity(e.getQuantity() + 1);
                    
                    // 2. set the quantity left in stock (this is really just for when the customer is redirected back from the conform page)
                    if (e.isQuantityInStockEnough() == false) {
                        e.setQuantity(e.getQuantity() - 1);
                        
                        this.discountSuccessful = true; // as nothing happened since the last time, it is successful
                        throw new NotEnoughStockException("Not enough in stock.", e.getQuantityInStock(), e.getName());
                    }
                    
                    // 3. adjust the cart quantity
                    shoppingServiceIF.adjustProductQuantityInCart(shoppingCart.getShoppingCartId(), productId, 1);
                    
                    // modify cart
                    shoppingCart.setItemCount(shoppingCart.getItemCount() + 1);
                    shoppingCart.setSubTotal(shoppingCart.getSubTotal().add(e.getUnitPrice()));
                    
                    // get values for display
                    newCartEntryTotal = e.getTotalPriceString();
                    newCartEntryItemCount = Integer.toString(e.getQuantity());
                    
                    this.applyDiscount(shoppingCart);
                    this.setNewShoppingCartValuesForDisplay(shoppingCart);
                    incrementSccessful = true;
                    
                    // update session for gae so that the new shopping cart will be persisted
                    session.put(SessionKey.SHOPPING_CART.toString(), shoppingCart);
                    
                    break;
                }
            }
            
            return ActionSupport.SUCCESS;    
        } catch (NotEnoughStockException e) {
            log.info("Not enough stock left to increment quantity in shopping cart.");
            this.quantityLeftInStock = e.getQuantityLeft();
            
            return ActionSupport.SUCCESS;   
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log.severe(sw.toString());
        }

        return ActionSupport.ERROR;
    }
    
    public String decrementItem() throws Exception {
        try {
            ShoppingCart shoppingCart = (ShoppingCart) session.get(SessionKey.SHOPPING_CART);
            
            for (ShoppingCartEntry e : shoppingCart.getShoppingCartEntries()) {
                if (e.getProductId() == productId) {
                    
                    // 1. adjust quantity in cart
                    shoppingServiceIF.adjustProductQuantityInCart(shoppingCart.getShoppingCartId(), productId, -1);
                    
                    // 2. modify the cart entry
                    e.setQuantity(e.getQuantity() - 1);
                    
                    // 3. modify the cart totals
                    shoppingCart.setItemCount(shoppingCart.getItemCount() - 1);
                    shoppingCart.setSubTotal(shoppingCart.getSubTotal().subtract(e.getUnitPrice()));
                    
                    // 4. set the new entry values for display
                    newCartEntryTotal = e.getTotalPriceString();
                    newCartEntryItemCount = Integer.toString(e.getQuantity());
                    
                    enoughInStock = e.isQuantityInStockEnough();
                    
                    // 6. re-apply discount and set values for display
                    this.applyDiscount(shoppingCart);
                    this.setNewShoppingCartValuesForDisplay(shoppingCart);
                    
                    // update session for gae so that the new shopping cart will be persisted
                    session.put(SessionKey.SHOPPING_CART.toString(), shoppingCart);
                    
                    break;
                }
            }
            
            return ActionSupport.SUCCESS;            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ActionSupport.ERROR;
    }
    
    public String deleteItem() throws Exception {
        try {
            ShoppingCart shoppingCart = (ShoppingCart) session.get(SessionKey.SHOPPING_CART);
            
            ShoppingCartEntry entryToRemove = null;
            
            for (ShoppingCartEntry e : shoppingCart.getShoppingCartEntries()) {
                if (e.getProductId() == productId) {
                    entryToRemove = e;
                    break;
                }
            }
            
            if (entryToRemove != null) {
                shoppingServiceIF.deleteProductFromCart(shoppingCart.getShoppingCartId(), productId);
                shoppingCart.removeShopppingCartEntry(entryToRemove);    
            }
            
            if (shoppingCart.getShoppingCartEntries().size() == 0) {
                shoppingCart.setPostageAndPacking(new BigDecimal(0));
            }
            
            this.applyDiscount(shoppingCart);
            this.setNewShoppingCartValuesForDisplay(shoppingCart); 
            
            // update session for gae so that the new shopping cart will be persisted
            session.put(SessionKey.SHOPPING_CART.toString(), shoppingCart);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ActionSupport.SUCCESS;
    }
    
    public String applyDiscountCode() throws Exception {
        try {
            ShoppingCart shoppingCart = (ShoppingCart) session.get(SessionKey.SHOPPING_CART);
            
            // get the discount to be applied
            Discount discount = this.discountsServiceIF.getDiscountForDiscountCode(discountCode);
            
            if (discount == null) {
                throw new DiscountException("Invalid discount code");
            }
            
            DiscountApplier discountApplier = DiscountApplierFactory.getDiscountApplier(discount);
            
            if (discountApplier == null) {
                throw new DiscountException("Invalid discount type");
            }
            
            if (shoppingCart == null || shoppingCart.getItemCount() == 0) {
                throw new DiscountException("Cannot discount empty cart");
            } else {
                shoppingCart.setDiscountApplier(discountApplier); 
                this.applyDiscount(shoppingCart);  
            }
            
            if (shoppingCart != null) {
                this.setNewShoppingCartValuesForDisplay(shoppingCart);    
            }
            
            // update session for gae so that the new shopping cart will be persisted
            session.put(SessionKey.SHOPPING_CART.toString(), shoppingCart);
            
        } catch (DiscountException e) {
            discountMessage = e.getMessage();
            discountSuccessful = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ActionSupport.SUCCESS;
    }
    
    private void applyDiscount(ShoppingCart shoppingCart) throws DiscountException, Exception {
        try {
            discountMessage = shoppingCart.applyDiscount();
            discountSuccessful = true;
        } catch (DiscountException de) {
            discountMessage = de.getMessage();
            discountSuccessful = false;
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log.severe(sw.toString());
            
            throw e;
        }
    }
    
    private void setNewShoppingCartValuesForDisplay(ShoppingCart shoppingCart) {
        this.enoughInStockForFullOrder = shoppingCart.isEnoughQuantityInStock();
        
        newSubTotal = shoppingCart.getSubTotalString();
        subTotal = shoppingCart.getSubTotal();
        
        newPostageAndPacking = shoppingCart.getPostageAndPackingString();
        
        if (shoppingCart.getDiscount() != null) {
            newDiscountName = shoppingCart.getDiscount().getDiscountType().getName();
            newDiscountTotal = shoppingCart.getLocaleCurrencyStringValue(shoppingCart.getDiscount().getValue());    
        } else {
            newDiscountName = "No discount";
            newDiscountTotal = shoppingCart.getLocaleCurrencyStringValue(new BigDecimal(0));
        }
        
        newTotal = shoppingCart.getTotalString();
        newItemCount = Integer.toString(shoppingCart.getItemCount());
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public String getNewCartEntryTotal() {
        return newCartEntryTotal;
    }

    public void setNewCartEntryTotal(String newCartEntryTotal) {
        this.newCartEntryTotal = newCartEntryTotal;
    }

    public String getNewCartEntryItemCount() {
        return newCartEntryItemCount;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public void setNewCartEntryItemCount(String newCartEntryItemCount) {
        this.newCartEntryItemCount = newCartEntryItemCount;
    }

    public String getNewSubTotal() {
        return newSubTotal;
    }

    public void setNewSubTotal(String newSubTotal) {
        this.newSubTotal = newSubTotal;
    }

    public String getNewPostageAndPacking() {
        return newPostageAndPacking;
    }

    public void setNewPostageAndPacking(String newPostageAndPacking) {
        this.newPostageAndPacking = newPostageAndPacking;
    }

    public String getNewDiscountName() {
        return newDiscountName;
    }

    public void setNewDiscountName(String newDiscountName) {
        this.newDiscountName = newDiscountName;
    }

    public String getNewDiscountTotal() {
        return newDiscountTotal;
    }

    public void setNewDiscountTotal(String newDiscountTotal) {
        this.newDiscountTotal = newDiscountTotal;
    }

    public String getNewTotal() {
        return newTotal;
    }

    public void setNewTotal(String newTotal) {
        this.newTotal = newTotal;
    }

    public String getNewItemCount() {
        return newItemCount;
    }

    public void setNewItemCount(String newItemCount) {
        this.newItemCount = newItemCount;
    }

    public String getDiscountMessage() {
        return discountMessage;
    }

    public void setDiscountMessage(String discountMessage) {
        this.discountMessage = discountMessage;
    }

    public boolean isDiscountSuccessful() {
        return discountSuccessful;
    }

    public void setDiscountSuccessful(boolean discountSuccessful) {
        this.discountSuccessful = discountSuccessful;
    }
    
    public int getQuantityLeftInStock() {
        return quantityLeftInStock;
    }

    public void setQuantityLeftInStock(int quantityLeftInStock) {
        this.quantityLeftInStock = quantityLeftInStock;
    }

    public boolean isEnoughInStock() {
        return enoughInStock;
    }

    public void setEnoughInStock(boolean enoughInStock) {
        this.enoughInStock = enoughInStock;
    }

    public boolean isEnoughInStockForFullOrder() {
        return enoughInStockForFullOrder;
    }

    public void setEnoughInStockForFullOrder(boolean enoughInStockForFullOrder) {
        this.enoughInStockForFullOrder = enoughInStockForFullOrder;
    }

    public boolean isIncrementSccessful() {
        return incrementSccessful;
    }

    public void setIncrementSccessful(boolean incrementSccessful) {
        this.incrementSccessful = incrementSccessful;
    }

}
