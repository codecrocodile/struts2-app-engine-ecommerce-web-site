/*
 * User.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.structures;


/**
 * @author Chris Hatton
 */
public class User extends Person {
    /** */
    private static final long serialVersionUID = 2471655831526115689L;

    private int userId;

    private String username;

    private String password;

    public User() {
        super();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int id) {
        this.userId = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
