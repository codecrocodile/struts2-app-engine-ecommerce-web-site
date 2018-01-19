/*
 * CustomsForm.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.postage;

/**
 * @author Chris Hatton
 */
public enum CustomsDeclarationType {
    
    /**
     * When no customs declaration form is required.
     */
    NONE,
    
    /**
     * When order value is up to and including 270.00 GRB
     */
    CN22,
    
    /**
     * When order value is in excess of 270.00 GRB
     */
    CN23,
}
