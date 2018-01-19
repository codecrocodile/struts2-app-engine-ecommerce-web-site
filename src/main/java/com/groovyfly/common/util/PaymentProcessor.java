/*
 * PaymentProcessor.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.util;

/**
 * @author Chris Hatton
 * 
 * Created 24 Nov 2012
 */
public enum PaymentProcessor {
    PAYPAL {
        
        /* 
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return "Paypal";
        }
    }
}
