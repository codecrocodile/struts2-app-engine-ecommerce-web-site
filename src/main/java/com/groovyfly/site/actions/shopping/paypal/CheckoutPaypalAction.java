/*
 * CheckoutPaypalAction.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.actions.shopping.paypal;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.util.ServletContextAware;
import org.xml.sax.SAXException;

import urn.ebay.apis.eBLBaseComponents.ErrorType;

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
public class CheckoutPaypalAction extends BaseSiteAction implements ServletRequestAware, ServletContextAware, SessionAware {

    private static final long serialVersionUID = 2514604293037876187L;
    
    private static Logger log = Logger.getLogger(CheckoutPaypalAction.class.getName());
    
    private HttpServletRequest request;
    
    private ServletContext context;
    
    private Map<String, Object> session;
    
    private String paypalUrl;
    
    /* 
     * @see org.apache.struts2.interceptor.ServletRequestAware#setServletRequest(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
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
    public String execute() throws Exception { // TODO put back in the throw exception
        log.info("doing paypal checkout");
        
        SetExpressCheckoutOperation setExpressCheckoutOperation = null;
        
        try {
            String payPalToken = null;
            
            // if editing the cart after the customer has entered paypal already
            if (session.get(SessionKey.PAYPAL_TOKEN.toString()) != null) {
                payPalToken = (String) session.get(SessionKey.PAYPAL_TOKEN.toString());
            }
            
            setExpressCheckoutOperation = new SetExpressCheckoutOperation(
                    payPalToken, 
                    this.context.getRealPath("/"), 
                    this.request.getServerName(), 
                    this.request.getServerPort(),
                    this.request.getContextPath(),
                    this.shoppingCart);
            setExpressCheckoutOperation.doOperation();
            
            this.session.put(SessionKey.PAYPAL_TOKEN.toString(), setExpressCheckoutOperation.getToken());
            this.paypalUrl = setExpressCheckoutOperation.getPaypalUrl();
            
            return ActionSupport.SUCCESS;
   
        } catch (IOException e) {
            // possibly that it could not read the paypal sdk config properties file
            e.printStackTrace();
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (setExpressCheckoutOperation != null && setExpressCheckoutOperation.getErrors() != null) { 
                // if we have errors then we need to find out what they are...
                for (ErrorType et : setExpressCheckoutOperation.getErrors()) {
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

    /**
     * Gets the Paypal url that the customer should be forwarded to to enter their payment details.
     */
    public String getPaypalUrl() {
        return paypalUrl;
    }
}
