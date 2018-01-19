/*
 * UserConfirmPaypalAction.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.actions.shopping.paypal;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.util.ServletContextAware;
import org.xml.sax.SAXException;

import urn.ebay.apis.eBLBaseComponents.ErrorType;

import com.groovyfly.admin.postage.RoyalMailPostageCalculator;
import com.groovyfly.common.structures.Customer;
import com.groovyfly.common.structures.ShippingAddress;
import com.groovyfly.common.util.SessionKey;
import com.groovyfly.site.actions.BaseSiteAction;
import com.opensymphony.xwork2.ActionSupport;
import com.paypal.exception.ClientActionRequiredException;
import com.paypal.exception.HttpErrorException;
import com.paypal.exception.InvalidCredentialException;
import com.paypal.exception.InvalidResponseDataException;
import com.paypal.exception.MissingCredentialException;
import com.paypal.exception.SSLConfigurationException;
import com.paypal.sdk.exceptions.OAuthException;

/**
 * @author Chris Hatton
 */
public class ReviewPaypalAction extends BaseSiteAction implements ServletContextAware, SessionAware {

    private static final long serialVersionUID = 8967745609730526693L;
    
    private static Logger log = Logger.getLogger(ReviewPaypalAction.class.getName());
    
    private RoyalMailPostageCalculator royalMailPostageCalculator;
    
    private ServletContext context;
    
    private Map<String, Object> session;
    
    private Customer customer;
    
    private List<String> shippingAddressFields;
    
    /** token given back from paypal to identify the transaction */
    private String token;
    
    /** id given back form paypal to identify the customer in the transaction */
    private String payerID;
    
    public void setRoyalMailPostageCalculator(RoyalMailPostageCalculator royalMailPostageCalculator) {
        this.royalMailPostageCalculator = royalMailPostageCalculator;
    }
    
    /* 
     * @see org.apache.struts2.util.ServletContextAware#setServletContext(javax.servlet.ServletContext)
     */
    @Override
    public void setServletContext(ServletContext context) {
        this.context = context;
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
        
        log.info("token " + token);
        log.info("payerId " + payerID);
        
        // TODO if the token is null in the session or the token passed here does not match the session then redirect to the checkout page
        String paypalToken = null;
        if (session.get(SessionKey.PAYPAL_TOKEN.toString()) != null) {
            paypalToken = (String) session.get(SessionKey.PAYPAL_TOKEN.toString());
        }
        if (token == null || payerID == null || !paypalToken.equalsIgnoreCase(token)) {
            return ActionSupport.NONE;
        }
        
        GetExpressCheckoutDetailsOperation getExpressCheckoutDetails = null;
        
        try {
            getExpressCheckoutDetails = new GetExpressCheckoutDetailsOperation(token, this.context.getRealPath("/"));
            getExpressCheckoutDetails.doOperation();
            
            this.customer = getExpressCheckoutDetails.getCustomer();
            this.shippingAddressFields = this.createShippingAddressFields(customer.getShippingAddress());
            
            // TODO now calculate the shipping
            BigDecimal actualPostage = royalMailPostageCalculator.getActualPostage(shoppingCart, customer.getShippingAddress());
            shoppingCart.setPostageAndPacking(actualPostage);
            
            
            session.put(SessionKey.CUSTOMER.toString(), this.customer);
            session.put(SessionKey.PAYPAL_TOKEN.toString(), this.token);
            session.put(SessionKey.PAYPAL_PAYER_ID.toString(), this.payerID);
            
            return ActionSupport.SUCCESS;
            
        } catch (SSLConfigurationException e) {
            e.printStackTrace();
        } catch (InvalidCredentialException e) {
            e.printStackTrace();
        } catch (HttpErrorException e) {
            e.printStackTrace();
        } catch (InvalidResponseDataException e) {
            e.printStackTrace();
        } catch (ClientActionRequiredException e) {
            e.printStackTrace();
        } catch (MissingCredentialException e) {
            e.printStackTrace();
        } catch (OAuthException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (getExpressCheckoutDetails != null && getExpressCheckoutDetails.getErrors() != null) { 
                // if we have errors then we need to find out what they are...
                for (ErrorType et : getExpressCheckoutDetails.getErrors()) {
                    log.info("-------------------------------------------------------------------");
                    log.info(et.getErrorCode());
                    log.info(et.getShortMessage());
                    log.info(et.getLongMessage());
                    log.info("-------------------------------------------------------------------");
                }
            }
        }
        
        return ActionSupport.ERROR;
    }
    
    /*
     * Put address fields into an array list so that we can easily display them on the review page. This also 
     * removes fields which have not been entered so there are no line gaps in the address that is displayed.
     */
    private List<String> createShippingAddressFields(ShippingAddress customerAddress) {
        List<String> shippingAddressFieldsList = new ArrayList<String>();
        
        shippingAddressFieldsList.add(customerAddress.getName());
        shippingAddressFieldsList.add(customerAddress.getLine1());
        shippingAddressFieldsList.add(customerAddress.getLine2());
        shippingAddressFieldsList.add(customerAddress.getLine3());
        shippingAddressFieldsList.add(customerAddress.getLine4());
        shippingAddressFieldsList.add(customerAddress.getPostcode());
        shippingAddressFieldsList.add(customerAddress.getCountry());
        
        shippingAddressFieldsList.removeAll(Collections.singleton(null));  
        
        return shippingAddressFieldsList;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPayerID() {
        return payerID;
    }

    public void setPayerID(String payerID) {
        this.payerID = payerID;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<String> getShippingAddressFields() {
        return shippingAddressFields;
    }
}
