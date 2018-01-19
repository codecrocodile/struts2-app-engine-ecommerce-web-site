/*
 * ConfigurationServiceIF.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.service.configuration;

import com.groovyfly.common.structures.Company;

/**
 * @author Chris Hatton
 * 
 * Created 5 Dec 2012
 */
public interface ConfigurationServiceIF {
    
    /**
     * Gets my company information from the website configuration. 
     */
    public Company getCompany() throws Exception;
    
    /**
     * Save my company information to the website configuration. 
     */
    public void saveCompany(Company company) throws Exception;

}
