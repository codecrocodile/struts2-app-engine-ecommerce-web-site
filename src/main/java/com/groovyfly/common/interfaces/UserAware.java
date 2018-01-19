/*
 * UserAware.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.interfaces;

import com.groovyfly.common.structures.User;

/**
 * Interface for Actions that should have the session User for the set on if there is one. It is expected this will be used 
 * primarily with the authentication interceptor.
 * 
 * @author Chris Hatton
 */
public interface UserAware {
	
	/**
	 * Sets the session user.
	 * 
	 * @param user
	 */
	public void setUser(User user);
	
}
