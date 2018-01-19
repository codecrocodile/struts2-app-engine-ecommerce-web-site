/*
 * CustomerSuggestion.java
 * 
 * Copyright (c) 2013 Groovy Fly
 */
package com.groovyfly.site.service.datacollection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.groovyfly.common.util.DbUtil;
import com.groovyfly.common.util.GroovyFlyDS;

/**
 * @author Chris Hatton
 * 
 * Created 15 Sep 2013
 */
public class CustomerSuggestionServiceImpl implements CustomerSuggestionServiceIF {
    
    private GroovyFlyDS groovyFlyDS;
    
    public void setGroovyFlyDS(GroovyFlyDS groovyFlyDS) {
        this.groovyFlyDS = groovyFlyDS;
    }

    /* 
     * @see com.groovyfly.site.service.datacollection.CustomerSuggestionIF#getFlyPatternAutoCompleteString()
     */
    @Override
    public String getFlyPatternAutoCompleteString() throws Exception {
        StringBuilder sb = new StringBuilder();
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT name, category " +
                "FROM flypattern";

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while(rs.next()) {
                sb.append("\'");
                sb.append(rs.getString(1).replace("\'", "\\'"));
                sb.append(" (" + rs.getString(2) + ")");
                sb.append("\', ");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(stmt);
            DbUtil.closeConnection(conn);
        }
        
        return sb.toString();
    }

    /* 
     * @see com.groovyfly.site.service.datacollection.CustomerSuggestionServiceIF#saveFlyPatternSuggestion(double, double, java.util.List, java.lang.String, java.lang.String)
     */
    @Override
    public void saveFlyPatternSuggestion(double XCoord, double YCoord, List<String> patternNames, String name, String email) throws Exception {
        Connection conn = null;
        PreparedStatement pStmt = null;
        
        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "INSERT INTO flypatternsuggestions(xCoord, yCoord, patternName, customerName, customerEmail, dateCreated) " +
                "VALUES (?, ?, ?, ?, ?, NOW())";
            
            pStmt = conn.prepareStatement(sql);
            
            for (String patternName : patternNames) {
                int index = 0;
                DbUtil.setDouble(pStmt, ++index, XCoord);
                DbUtil.setDouble(pStmt, ++index, YCoord);
                DbUtil.setString(pStmt, ++index, patternName);
                DbUtil.setString(pStmt, ++index, name);
                DbUtil.setString(pStmt, ++index, email);
                
                pStmt.addBatch();
            }
            
            pStmt.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(pStmt);
            DbUtil.closeConnection(conn);
        }
        
    }

    /* 
     * @see com.groovyfly.site.service.datacollection.CustomerSuggestionServiceIF#getMostPopular(int)
     */
    @Override
    public List<String> getMostPopular(int limit) throws Exception {
        List<String> mostPopular = new ArrayList<String>();
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT patternName " +
                "FROM flypatternsuggestions " +
                "GROUP BY patternName " +
                "LIMIT " + limit;

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while(rs.next()) {
                mostPopular.add(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(stmt);
            DbUtil.closeConnection(conn);
        }
        
        return mostPopular;
    }

}
