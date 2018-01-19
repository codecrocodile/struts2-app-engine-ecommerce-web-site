/*
 * SubscriptionServiceIF.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.service;

import java.util.List;

/**
 * @author Chris Hatton
 * 
 * Created 26 Aug 2012
 */
public interface SubscriptionServiceIF {
    
    public void subscribeToNewsLetter(String email) throws Exception;
    
    public void unsubscribeToNewsLetter(String emailMD5) throws Exception;
    
    public List<String> getNewsletterSubstriberEmails() throws Exception;
    
}
