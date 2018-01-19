/*
 * SubscriptionServiceImpl.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import com.groovyfly.common.util.DbUtil;
import com.groovyfly.common.util.GroovyFlyDS;

/**
 * @author Chris Hatton
 * 
 * Created 26 Aug 2012
 */
public class SubscriptionServiceImpl implements SubscriptionServiceIF {

    private static Logger log = Logger.getLogger(SubscriptionServiceImpl.class.getName());
    
    private GroovyFlyDS groovyFlyDS;

    public void setGroovyFlyDS(GroovyFlyDS groovyFlyDS) {
        this.groovyFlyDS = groovyFlyDS;
    }
    
    /* 
     * @see com.groovyfly.site.service.SubscriptionServiceIF#subscribeToNewsLetter(java.lang.String)
     */
    @Override
    public void subscribeToNewsLetter(String email) throws Exception {
        Connection conn = null;
        PreparedStatement pStmt = null;
        
        email = email.trim();
        
        byte[] md5 = DigestUtils.md5(email + "groovy-tag" + Calendar.getInstance().getTimeInMillis());
        String encodedString = new String(Hex.encodeHex(md5));

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "INSERT INTO newslettersubscribers (email, emailMD5, dateSubscribed, dateUnsubscribed) " +
                "SELECT ?, ?, ?, NULL " +
                "FROM DUAL " +
                "WHERE NOT EXISTS ( " +
                "       SELECT 1 FROM newslettersubscribers " +
                "       WHERE email = ?" +
                ")";

            log.fine(sql);

            pStmt = conn.prepareStatement(sql);
            
            int index = 0;
            DbUtil.setString(pStmt, ++index, email);
            DbUtil.setString(pStmt, ++index, encodedString);
            DbUtil.setDate(pStmt, ++index, Calendar.getInstance().getTime());
            DbUtil.setString(pStmt, ++index, email);

            pStmt.executeUpdate();
            
            // just in case they are re-subscribing
            this.resubscribe(conn, email);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(pStmt);
        }
    }
    
    private void resubscribe(Connection conn, String email) throws Exception {
        PreparedStatement pStmt = null;
        
        email = email.trim();

        try {
            String sql = 
                "UPDATE newslettersubscribers " +
                "SET dateUnsubscribed = NULL " +
                "WHERE email = ?";

            log.fine(sql);

            pStmt = conn.prepareStatement(sql);
            
            int index = 0;
            DbUtil.setString(pStmt, ++index, email);

            pStmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(pStmt);
        }
    }

    /* 
     * @see com.groovyfly.site.service.SubscriptionServiceIF#unsubscribeToNewsLetter(java.lang.String)
     */
    @Override
    public void unsubscribeToNewsLetter(String emailMD5) throws Exception {
        Connection conn = null;
        PreparedStatement pStmt = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "UPDATE newslettersubscribers " +
                "SET dateUnsubscribed = ? " +
                "WHERE emailMD5 = ?";

            log.info(sql);

            pStmt = conn.prepareStatement(sql);
            int index = 0;
            DbUtil.setDate(pStmt, ++index, Calendar.getInstance().getTime());
            DbUtil.setString(pStmt, ++index, emailMD5);
            
            pStmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(pStmt);
        }
    }

    /* 
     * @see com.groovyfly.site.service.SubscriptionServiceIF#getNewsletterSubstriberEmails()
     */
    @Override
    public List<String> getNewsletterSubstriberEmails() throws Exception {
        List<String> emails = new ArrayList<String>();
      
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT email " +
                "FROM newslettersubscribers " +
                "WHERE dateUnsubscribed IS NULL";

            log.info(sql);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                emails.add(rs.getString(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(stmt);
            DbUtil.closeResultSet(rs);
        }

        return emails;
    }
}
