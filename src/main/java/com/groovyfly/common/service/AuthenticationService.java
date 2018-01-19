package com.groovyfly.common.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.logging.Logger;

import com.google.appengine.api.rdbms.AppEngineDriver;
import com.groovyfly.common.structures.User;

public class AuthenticationService {

    private static Logger log = Logger.getLogger(AuthenticationService.class.getName());

    public AuthenticationService() {
        super();

    }

    public User authenticateUser(String username, String password) throws Exception {

        log.info("auth the user");

        try {
            DriverManager.registerDriver(new AppEngineDriver());
            log.info("driver registered");
            Connection conn = DriverManager.getConnection("jdbc:google:rdbms://groovyflydb:groovyflydb/groovyflydb", "root", "");
            log.info("conn made");
            Statement stmt = conn.createStatement();

            stmt.close();
            conn.close();

            log.info("resources closed");

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {

        }

        log.info("setting user");

        User u = new User();
        u.setForename("Chris");
        u.setSurname("Hatton");

        if (username.equals("admin") && password.equals("admin")) {
            return u;
        } else {
            return null;
        }
    }
}
