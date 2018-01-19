/*
 * DbUtil.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Date;
import java.util.List;

/**
 * @author Chris Hatton
 */
public class DbUtil {
    
    public static void setString(PreparedStatement pStmt, int index, String value) throws SQLException {
        if (value == null) {
            pStmt.setNull(index, Types.NULL);
        } else {
            pStmt.setString(index, value);
        }
    }
    
    public static void setInt(PreparedStatement pStmt, int index, int value) throws SQLException {
        if (value < 1) {
            pStmt.setNull(index, Types.NULL);
        } else {
            pStmt.setInt(index, value);
        }
    }
    
    public static void setDouble(PreparedStatement pStmt, int index, double value) throws SQLException {
        pStmt.setDouble(index, value);
    }
    
    public static void setInt(PreparedStatement pStmt, int index, boolean value) throws SQLException {
        if (value) {
            pStmt.setInt(index, 1);
        } else {
            pStmt.setInt(index, 0);
        }
    }
    
    public static void setDate(PreparedStatement pStmt, int index, Date value) throws SQLException {
        if (value == null) {
            pStmt.setNull(index, Types.NULL);
        } else {
            pStmt.setDate(index, new java.sql.Date(value.getTime()));
        }
    }
    
    public static void setBigDecimal(PreparedStatement pStmt, int index, BigDecimal value) throws SQLException {
        if (value == null) {
            pStmt.setNull(index, Types.NULL);
        } else {
            pStmt.setBigDecimal(index, value);
        }
    }

    public static void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
        }
    }

    public static void closeStatement(Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (Exception e) {
        }
    }

    public static void closeResultSet(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
        }
    }

    public static String quoteStringOrNull(String s) {
        if (s == null) {
            return "NULL";
        } else {
            return "'" + addSlashes(s) + "'";
        }
    }
    
    public static String addSlashes(String s) {
        return s.replace("'", "\\'");
    }
    
    public static String getInValue(int... values) {
        StringBuilder sb = new StringBuilder();
        
        for (int j : values) {
            sb.append(j + ",");                
        }

        String toReturn = "";
        if (sb.length() > 0) {
            toReturn = sb.substring(0, sb.lastIndexOf(","));
        }
        
        return toReturn;
    }
    
    public static String getInValue(List<Integer> values) {
        StringBuilder sb = new StringBuilder();
        
        for (int j : values) {
            sb.append(j + ",");                
        }

        String toReturn = "";
        if (sb.length() > 0) {
            toReturn = sb.substring(0, sb.lastIndexOf(","));
        }
        
        return toReturn;
    }

}
