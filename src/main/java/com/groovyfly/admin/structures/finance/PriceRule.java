/*
 * PriceRule.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.structures.finance;

/**
 * @author Chris Hatton
 * 
 *         Created 29 Jul 2012
 */
public class PriceRule {

    private int priceRuleId;

    private String name;

    private String description;

    private String code;

    private boolean retired;

    /**
     * Constructor
     */
    public PriceRule() {
        super();
    }

    public int getPriceRuleId() {
        return priceRuleId;
    }

    public void setPriceRuleId(int priceRuleId) {
        this.priceRuleId = priceRuleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isRetired() {
        return retired;
    }

    public void setRetired(boolean retired) {
        this.retired = retired;
    }
}
