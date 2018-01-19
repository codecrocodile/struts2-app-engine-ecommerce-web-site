/*
 * CommentsAndRatingServiceImpl.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.groovyfly.common.util.DbUtil;
import com.groovyfly.common.util.GroovyFlyDS;

/**
 * @author Chris Hatton
 */
public class CommentsAndRatingServiceImpl implements CommentsAndRatingServiceIF {
    
    private GroovyFlyDS groovyFlyDS;
    
    public void setGroovyFlyDS(GroovyFlyDS groovyFlyDS) {
        this.groovyFlyDS = groovyFlyDS;
    }

    /* 
     * @see com.groovyfly.site.service.CommentsAndRatingServiceIF#addRating(int, int)
     */
    @Override
    public int addProductRating(int productId, int productRating, String userIpAddress) throws Exception {
        Connection conn = null;
        
        try {
            this.insertRating(conn, productId, productRating, userIpAddress);
            int newAverageProductRating = this.calculateAverageRating(conn, productId);
            this.updateProductWithAverageRating(conn, productId, newAverageProductRating);
            
            return newAverageProductRating;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
        }
    }
    
    private void insertRating(Connection conn, int productId, int productRating, String userIpAddress) throws Exception {
        PreparedStatement pStmt = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "INSERT INTO productreview (productId, starRating, userIpAddress, date) " +
                "VALUES (?, ?, ?, NOW())";

            pStmt = conn.prepareStatement(sql);
            int index = 0;
            pStmt.setInt(++index, productId);
            pStmt.setInt(++index, productRating);
            pStmt.setString(++index, userIpAddress);

            pStmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(pStmt);
        }
    }
    
    private int calculateAverageRating(Connection conn, int productId) throws Exception {
        int newAverageProductRating = 3; // the default
        
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        
        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT CEIL(AVG(starRating)) AS newAverageProductRating " +
                "FROM productreview " +
                "WHERE productId = ? " +
                "GROUP BY productId";

            pStmt = conn.prepareStatement(sql);
            int index = 0;
            DbUtil.setInt(pStmt, ++index, productId);

            rs = pStmt.executeQuery();
            while(rs.next()) {
                newAverageProductRating = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(pStmt);
        }
        
        return newAverageProductRating;
    }
    
    private void updateProductWithAverageRating(Connection conn, int productId, int newAverageProductRating) throws Exception {
        PreparedStatement pStmt = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "UPDATE product " +
                "SET averageStarRating = ? " +
                "WHERE productId = ?";

            pStmt = conn.prepareStatement(sql);
            int index = 0;
            DbUtil.setInt(pStmt, ++index, newAverageProductRating);
            DbUtil.setInt(pStmt, ++index, productId);

            pStmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(pStmt);
        }
    }

}
