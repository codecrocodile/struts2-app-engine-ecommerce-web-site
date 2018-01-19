/*
 * SetExpressCheckoutOperation.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.actions.shopping.paypal;

import java.util.logging.Logger;

import urn.ebay.api.PayPalAPI.PayPalAPIInterfaceServiceService;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutReq;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutRequestType;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutResponseType;
import urn.ebay.apis.eBLBaseComponents.LandingPageType;
import urn.ebay.apis.eBLBaseComponents.PaymentActionCodeType;
import urn.ebay.apis.eBLBaseComponents.SetExpressCheckoutRequestDetailsType;
import urn.ebay.apis.eBLBaseComponents.SolutionTypeType;

import com.groovyfly.common.structures.ShoppingCart;

/**
 * @author Chris Hatton
 */
public class SetExpressCheckoutOperation extends ExpressCheckoutOperation {
    
    private static Logger log = Logger.getLogger(SetExpressCheckoutOperation.class.getName());
    
    private String serverName;
    
    private int serverPort;
    
    private String contextPath;
    
    private String paypalUrl;
    
    /**
     * Constructor
     * @throws Exception 
     */
    public SetExpressCheckoutOperation(String token, String contextRealPath, String serverName, int serverPort, String contextPath, ShoppingCart shoppingCart) {
        super(token, contextRealPath);
        this.serverName = serverName;
        this.serverPort = serverPort;
        this.contextPath = contextPath;
        super.shoppingCart = shoppingCart;
    }
    
    public void doOperation() throws Exception {
        // get the basic objects used for the web service call
        PayPalAPIInterfaceServiceService service = new PayPalAPIInterfaceServiceService(configPath); // service is a wrapper for web service calls
        SetExpressCheckoutRequestType setExpressCheckoutReq = new SetExpressCheckoutRequestType(); // this is the web service method payload (and wrapper for the details of the web service method call)
        SetExpressCheckoutRequestDetailsType details = new SetExpressCheckoutRequestDetailsType(); // these are parameters of the web service method call e.g. basic charge details, Paypal page configuration etc..
        
        if (token != null) {
            details.setToken(token);
        }
        
        details.setSolutionType(SolutionTypeType.SOLE); // so they don't require a paypal account and can checkout as guest
        details.setLandingPage(LandingPageType.BILLING); // by default display the address fields and not paypal login
        details.setPaymentAction(PaymentActionCodeType.SALE); // SALE is the default but we will set it anyways (this is for immediate payment)   
        details.setNoShipping("2"); // no address is passed in this process so the shipping data must come from customers paypal account if possible
        details.setAllowNote("0"); // stops them from being able to enter note to us (can't see any reason to take notes at this time)


        // TODO create my own invoice at this point somewhere
        //            details.setInvoiceID(""); 
        
        // set the cancel and return urls
        this.setCancelAndReturnUrls(details);
        
        // set the details of the payment to be made by the customer
        details.setPaymentDetails(super.getPaymentDetails());
        
        // set custom paypal page style to bring in some Groovy Fly branding
        this.setPaypalPageStyles(details);
        
        // set the details of the request and call web service
        setExpressCheckoutReq.setSetExpressCheckoutRequestDetails(details);
        SetExpressCheckoutReq expressCheckoutReq = new SetExpressCheckoutReq();
        expressCheckoutReq.setSetExpressCheckoutRequest(setExpressCheckoutReq);
        
        // call the web service with the payment details 
        SetExpressCheckoutResponseType setExpressCheckoutResponse = service.setExpressCheckout(expressCheckoutReq);

        if (setExpressCheckoutResponse != null) {
            if (setExpressCheckoutResponse.getAck().toString().equalsIgnoreCase("SUCCESS")) {
               
                log.info("token = " + setExpressCheckoutResponse.getToken());

                this.token = setExpressCheckoutResponse.getToken();
                this.paypalUrl = "https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=" + setExpressCheckoutResponse.getToken();
            } else {
                this.errors = setExpressCheckoutResponse.getErrors();
                
                throw new Exception("Ack code is not success");
            }
        }
    }
    
    /*
     * Constructs and sets both the URL to go to when the customer cancels on the Paypal site and also the URL the 
     * customer should be returned to when they have entered their payment details.
     */
    private void setCancelAndReturnUrls(SetExpressCheckoutRequestDetailsType details) {
        StringBuffer url = new StringBuffer();
        
        url.append("http://");
        url.append(this.serverName);
        url.append(":");
        url.append(this.serverPort);
        url.append(this.contextPath);
        
        String returnURL = url.toString() + "/shop/review-paypal";
        String cancelURL = url.toString() + "/shop/shopping-cart-page";
        
        log.info("return url " + returnURL);
        log.info("cancel url " + cancelURL);
        
        details.setReturnURL(returnURL);
        details.setCancelURL(cancelURL);
    }
    
    /*
     * Set some custom styles on the paypal page to reassure the customer they are still dealing with Groovy Fly. 
     */
    private void setPaypalPageStyles(SetExpressCheckoutRequestDetailsType details) {
        details.setBrandName("Groovy Fly"); 
        details.setCppHeaderImage("http://www.eaa.myfishingclub.co.uk/GroovyFlyPaypalHeader.png"); //  TODO need to change this to use https on the live site
        details.setCppCartBorderColor("697002"); // green colour that roughly matches the colour of the header logo
    }

    public String getPaypalUrl() {
        return paypalUrl;
    }
}
