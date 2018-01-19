/*
 * ConfirmPaypalAction.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.actions.shopping.paypal;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.util.ServletContextAware;
import org.xml.sax.SAXException;

import urn.ebay.apis.eBLBaseComponents.ErrorType;

import com.groovyfly.common.email.Emailer;
import com.groovyfly.common.structures.Customer;
import com.groovyfly.common.structures.ShoppingCart;
import com.groovyfly.common.structures.exceptions.NotEnoughStockException;
import com.groovyfly.common.util.PaymentProcessor;
import com.groovyfly.common.util.SessionKey;
import com.groovyfly.site.actions.BaseSiteAction;
import com.groovyfly.site.service.shopping.ShoppingServiceIF;
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
public class ConfirmPaypalAction extends BaseSiteAction implements ServletContextAware, SessionAware {

    private static final long serialVersionUID = -431738527093957954L;
    
    private static Logger log = Logger.getLogger(ConfirmPaypalAction.class.getName());
    
    private ShoppingServiceIF shoppingServiceIF;
    
    private ServletContext context;
    
    private Map<String, Object> session;
    
    private String orderNumber;
    
    private Customer customer;
    
    private boolean waitingForFundsToClear;
    
    private String fundsToClearMessage;
    
    /**
     * This will be set by the String ICO container.
     */
    public void setShoppingServiceIF(ShoppingServiceIF shoppingServiceIF) {
        this.shoppingServiceIF = shoppingServiceIF;
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
        this.customer =  (Customer) session.get(SessionKey.CUSTOMER.toString());
    }
    
    /* 
     * @see com.opensymphony.xwork2.ActionSupport#execute()
     */
    @Override
    public String execute() throws Exception {
        log.info("confirming order paypal");
        
        // 1. check the session is valid for this confirmation to continue
        String paypalToken = null;
        String paypalPayerId = null;
        if (session.get(SessionKey.PAYPAL_TOKEN.toString()) != null) {
            paypalToken = (String) session.get(SessionKey.PAYPAL_TOKEN.toString());
        }
        if (session.get(SessionKey.PAYPAL_PAYER_ID.toString()) != null) {
            paypalPayerId = (String) session.get(SessionKey.PAYPAL_PAYER_ID.toString()); 
        }
        if (paypalToken == null || paypalPayerId == null) {
            // redirect to the checkout
            return ActionSupport.NONE;
        }
        
        DoExpressCheckoutOperation doExpressCheckoutOperation = null;
        
        try {
            
            // 2. check we have everything in stock and if not redirect back to the checkout page with message add way of amending the order
            // also as part of this transaction we should create the order and decrement the stock if we can
            this.orderNumber = shoppingServiceIF.createOrder(shoppingCart, customer, PaymentProcessor.PAYPAL);
            
            // 3. do the express checkout operation to confirm the order
            doExpressCheckoutOperation = new DoExpressCheckoutOperation(paypalToken, this.context.getRealPath("/"), paypalPayerId, shoppingCart);
            doExpressCheckoutOperation.doOperation();
            
            String transactionId = doExpressCheckoutOperation.getTransactionId();
            Date paymentDate = doExpressCheckoutOperation.getPaymentDate();
            String paymentStatus = doExpressCheckoutOperation.getPaymentStatus();
            String pendingReason = doExpressCheckoutOperation.getPendingReason();
            
            // 4. confirm this has gone through ok and also put transaction details on the order.
            shoppingServiceIF.confirmOrder(orderNumber, paypalPayerId, transactionId, paymentDate, paymentStatus, pendingReason);
            
            this.waitingForFundsToClear = doExpressCheckoutOperation.isWaitingForFundsToClear();
            this.fundsToClearMessage = doExpressCheckoutOperation.getFundsToClearMessage();
            
            // 5. send the confirmation email to the customer - get the right text and char size for the email title
            Emailer emailer = new Emailer();
            Customer customer = (Customer) session.get(SessionKey.CUSTOMER.toString());
            emailer.sendEmail("sales@groovyfly.com", "Groovy Fly", customer.getEmail(), customer.getDisplayName(), "Your Order with GroovyFly.com", this.createEmailMessageBody());
            
            // 6. clear the cart of session data now that we have completed the order
            this.clearShoppingData();
            
            return ActionSupport.SUCCESS;
            
        } catch (NotEnoughStockException e) {
            /*
             * If we get here then there is not enough stock for at least one of the items requested. We will have set the 
             * stock level on the shopping cart entry and will be able to check with isQuantityInStockEnough() method.
             * Redirect to shopping cart page so that they can amend their order.           
             */
            return ActionSupport.NONE;
            
        }  catch (SSLConfigurationException e) {
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
            if (doExpressCheckoutOperation != null && doExpressCheckoutOperation.getErrors() != null) { 
                // if we have errors then we need to find out what they are...
                for (ErrorType et : doExpressCheckoutOperation.getErrors()) {
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
    
    private String createEmailMessageBody() { // TODO fix this
        StringBuilder sb = new StringBuilder();
        sb.append("GroovyFly.com");
        sb.append("Thanks for your order, " + customer.getForename());
        sb.append("Ordering Information");
        sb.append("");
        sb.append("Grand Total:");
        sb.append("Order Summary");
        sb.append("");
        sb.append("");
        sb.append("Right to cancel");
        sb.append("Return policy");
        sb.append("www.groovyfly.com");
        
        return sb.toString();
    }
    
    /*
     * Clears all data related to the shopping for this session. We can do this once the order is complete and confirmed.
     */
    private void clearShoppingData() {
        session.remove(SessionKey.CUSTOMER.toString());
        session.remove(SessionKey.SHOPPING_CART.toString());
        session.remove(SessionKey.PAYPAL_PAYER_ID.toString());
        session.remove(SessionKey.PAYPAL_TOKEN.toString());
        
        super.shoppingCart = new ShoppingCart("", super.shoppingCart.getLocale());
    }

    public boolean isWaitingForFundsToClear() {
        return waitingForFundsToClear;
    }

    public String getFundsToClearMessage() {
        return fundsToClearMessage;
    }

    public String getOrderNumber() {
        return orderNumber;
    }
}
