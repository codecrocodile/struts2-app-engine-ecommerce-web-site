/*
 * FinanceServiceIF.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.service.finance;

import java.util.List;

import com.groovyfly.admin.structures.finance.PriceRule;
import com.groovyfly.common.structures.Lookup;

/**
 * @author Chris Hatton
 * 
 * Created 28 Jul 2012
 */
public interface FinanceServiceIF {

    public List<Lookup> getVatRates(boolean includeRetired) throws Exception;
    
    public List<PriceRule> getPriceRules(boolean includeRetired) throws Exception;
}
