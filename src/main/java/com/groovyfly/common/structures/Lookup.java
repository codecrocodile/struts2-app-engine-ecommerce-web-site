/*
 * Lookup.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.structures;

/**
 * @author Chris Hatton
 */
public class Lookup {

    private int id;

    private String description;

    private boolean shortList;

    private int shortListOrder;

    private boolean retired;

    /**
     * Constructor
     */
    public Lookup() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isShortList() {
        return shortList;
    }

    public void setShortList(boolean shortList) {
        this.shortList = shortList;
    }

    public int getShortListOrder() {
        return shortListOrder;
    }

    public void setShortListOrder(int shortListOrder) {
        this.shortListOrder = shortListOrder;
    }

    public boolean isRetired() {
        return retired;
    }

    public void setRetired(boolean retired) {
        this.retired = retired;
    }
}
