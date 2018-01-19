/*
 * DbUtil.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Chris Hatton
 * 
 * Created 17 Jul 2012
 */
public class GroovyFlyDS {

    /**
     * Gets a java sql connection to the groovyflydb database. This connection string will be changed by the google
     * app engine sql driver when used in local development to target the local MySql instance. This means there is
     * no need to change it for deployment.
     * 
     * Just remember to close the connection after use and in the event of an exception being thrown as there is no 
     * automatic connection closing like there is in an EJB container.
     * 
     * @return Connection
     * @throws SQLException
     */
    public Connection getConnection() throws Exception {

        Connection conn = null;

        try {        	
//            DriverManager.registerDriver(new AppEngineDriver());
//
//              conn = DriverManager.getConnection("jdbc:google:rdbms://groovyflydb:groovyflydb/groovyflydb", "root", "");
////            conn = DriverManager.getConnection("jdbc:google:rdbms://groovyflydb:groovyflytestdb/groovyflytestdb", "root", "");
        	
        	// this is not the same as using the eclipse gae plugin. donesn't make sense to the AppEngineDriver
        	// the purpose of that was to be able to set properties within eclipse and have connection strings faked to be the same 
        	// for local and remote??????
        	
        	Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/groovyflydb20131004";

            conn = DriverManager.getConnection(url, "root","");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return conn;
    }

}
