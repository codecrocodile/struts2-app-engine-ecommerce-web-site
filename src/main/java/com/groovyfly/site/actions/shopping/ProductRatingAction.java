/*
 * ProductRatingAction.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.actions.shopping;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.groovyfly.site.service.CommentsAndRatingServiceIF;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Chris Hatton
 */
public class ProductRatingAction extends ActionSupport implements ServletRequestAware {

    private static final long serialVersionUID = 8755319014240914681L;
    
    private HttpServletRequest request;
    
    private CommentsAndRatingServiceIF commentsAndRatingServiceIF;
    
    private int productId;
    
    private int productRating;
    
    private int newAverageProductRating = 3; // default being 3 just in case an error is thrown
    
    private String successMessage;
    
    public void setCommentsAndRatingServiceIF(CommentsAndRatingServiceIF commentsAndRatingServiceIF) {
        this.commentsAndRatingServiceIF = commentsAndRatingServiceIF;
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
            // guard against someone trying knobble the ratings
            if (productRating > 5 || productRating < 1) {
                productRating = 5; // instead give us a good score
            }
            
            newAverageProductRating = commentsAndRatingServiceIF.addProductRating(productId, productRating, request.getRemoteAddr());
            
        } catch (Exception e) {
            e.printStackTrace();
            // don't send back error, it's not that important that we need to let the user know
        }

        successMessage =  createSuccessMessage();
        
        return ActionSupport.SUCCESS;
    }
    
    private String createSuccessMessage() {
        StringBuilder sb = new StringBuilder("You have choosen ");
        switch (productRating) {
        case 1:
            sb.append("\"Very Poor\"");
            break;
        case 2:
            sb.append("\"Poor\"");
            break;
        case 3:
            sb.append("\"OK\"");
            break;
        case 4:
            sb.append("\"Good\"");
            break;
        case 5:
            sb.append("\"Very Good\"");
            break;
        default:
            sb.append("an invalid rating!");
        }
        
        return sb.toString();
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public int getProductRating() {
        return productRating;
    }

    public void setProductRating(int productRating) {
        this.productRating = productRating;
    }

    public int getNewAverageProductRating() {
        return newAverageProductRating;
    }

    public void setNewAverageProductRating(int newAverageProductRating) {
        this.newAverageProductRating = newAverageProductRating;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

}
