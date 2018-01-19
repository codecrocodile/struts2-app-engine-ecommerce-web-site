/*
 * CommentsAndRatingServiceIF.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.service;

/**
 * @author Chris Hatton
 * 
 * Created 30 Sep 2012
 */
public interface CommentsAndRatingServiceIF {
    
    public int addProductRating(int productId, int productRating, String userIpAddress) throws Exception;
    
}
