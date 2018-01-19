/*
 * InvalidQuantityException.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.structures.exceptions;

/**
 * @author Chris Hatton
 */
public class InvalidQuantityException extends Exception {

    private static final long serialVersionUID = -3035686742553012244L;

    /**
     * Constructor
     */
    public InvalidQuantityException(String msg) {
        super(msg);
    }
}
