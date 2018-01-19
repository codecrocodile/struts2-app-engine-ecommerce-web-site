/*
 * CollectionServiceIF.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.service.collection;

import java.util.List;

/**
 * @author Chris Hatton
 */
public interface CollectionServiceIF {
    
    public List<Integer> getProductIdsInCollection(String urlAlias) throws Exception;
    
}
