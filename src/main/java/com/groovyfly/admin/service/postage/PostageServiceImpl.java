/*
 * PostageServiceImpl.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.service.postage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.groovyfly.admin.postage.PostalZone;
import com.groovyfly.common.util.DbUtil;
import com.groovyfly.common.util.GroovyFlyDS;

/**
 * @author Chris Hatton
 */
public class PostageServiceImpl implements PostageServiceIF {
    
    
    
    private GroovyFlyDS groovyFlyDS;

    public void setGroovyFlyDS(GroovyFlyDS groovyFlyDS) {
        this.groovyFlyDS = groovyFlyDS;
    }

    /* 
     * @see com.groovyfly.admin.service.postage.PostageServiceIF#getPostageZone(java.lang.String, java.lang.String)
     */
    @Override
    public PostalZone getPostageZone(String postalCourierCode, String countryCode) throws Exception {
        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT pz.postalZoneCode, pz.description " +
                "FROM postalzonecountries pzc " +
                "JOIN postalzone pz ON pzc.postalZoneCode = pz.postalZoneCode " +
                "WHERE pzc.countryCode = ? AND pz.postalCourierCode = ?";

            pStmt = conn.prepareStatement(sql);
            int index = 0;
            DbUtil.setString(pStmt, ++index, countryCode);
            DbUtil.setString(pStmt, ++index, postalCourierCode);

            rs = pStmt.executeQuery();
            while (rs.next()) {
                PostalZone postalZone = new PostalZone();
                postalZone.setZoneCode(rs.getString(1));
                postalZone.setZoneDescription(rs.getString(2));
                
                return postalZone;
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(pStmt);
            DbUtil.closeResultSet(rs);
        }
        
        return null;
    }

    /* 
     * @see com.groovyfly.admin.service.postage.PostageServiceIF#isEuropeanUnionCountry(java.lang.String)
     */
    @Override
    public boolean isEuropeanUnionCountry(String countryCode) throws Exception {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT pzc.europeanUnion " +
                "FROM postalzonecountries pzc " +
                "WHERE pzc.countryCode = " + DbUtil.quoteStringOrNull(countryCode);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int eu = rs.getInt(1);
                if (eu == 1) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(stmt);
            DbUtil.closeConnection(conn);
        }
        
        return false;
    }

}
