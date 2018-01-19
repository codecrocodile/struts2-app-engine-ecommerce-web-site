/*
 * LookupServiceIF.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.service;

import java.util.List;

import com.groovyfly.common.structures.Lookup;

/**
 * @author Chris Hatton
 * 
 *         Created 21 Jul 2012
 */
public interface LookupServiceIF {

    public List<Lookup> getSalutations(boolean includeRetired) throws Exception;

    public List<Lookup> getCountries(boolean includeRetired) throws Exception;

}
