/*
 * NewsletterSubscribeAction.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.actions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.groovyfly.site.service.SubscriptionServiceIF;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.validators.EmailValidator;

/**
 * @author Chris Hatton
 * 
 * Created 26 Aug 2012
 */
public class NewsletterSubscribeAction extends ActionSupport {

    private static final long serialVersionUID = -656500115633045928L;
    
//    private static Logger log = Logger.getLogger(NewsletterSubscribeAction.class.getName());
    
    private SubscriptionServiceIF subscriptionServiceIF;
    
    private String email;
    
    private String message;
    
    private String hash;
    
    /* 
     * @see com.opensymphony.xwork2.ActionSupport#execute()
     */
    @Override
    public String execute() throws Exception {
        if (email.trim().equals("")) {
            message = "Please enter an email address if you want to subscribe to our newsletter";
        } else if (!this.validateEmail(email)) {
            message = email + " is not a vaild email address. We cannot subscribe you with this address. Please check and try again.";
        } else {
            subscriptionServiceIF.subscribeToNewsLetter(email);
            
            message = "Thank you, your are now subscribed to our newsletter. You can end your subscription at any time by following the instructions at the bottom of our next newsletter.";    
        }
        
        return Action.SUCCESS;
    }
    
    public String unsubscribe() throws Exception {
        subscriptionServiceIF.unsubscribeToNewsLetter(hash);
        
        return Action.SUCCESS;
    }
    
    public void setSubscriptionServiceIF(SubscriptionServiceIF subscriptionServiceIF) {
        this.subscriptionServiceIF = subscriptionServiceIF;
    }
    
    public boolean validateEmail(final String email){
        Pattern pattern = Pattern.compile(EmailValidator.EMAIL_ADDRESS_PATTERN);
        Matcher matcher = pattern.matcher(email);
        
        return matcher.matches();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
    
}
