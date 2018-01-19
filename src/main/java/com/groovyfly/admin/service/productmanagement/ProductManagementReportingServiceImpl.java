/*
 * ProductManagementReportingImpl.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.service.productmanagement;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.groovyfly.common.util.DbUtil;
import com.groovyfly.common.util.GroovyFlyDS;

/**
 * @author Chris Hatton
 */
public class ProductManagementReportingServiceImpl implements ProductManagementReportingServiceIF {
    
    private static Logger log = Logger.getLogger(ProductManagementReportingServiceImpl.class.getName());
    
    private GroovyFlyDS groovyFlyDS;

    public void setGroovyFlyDS(GroovyFlyDS groovyFlyDS) {
        this.groovyFlyDS = groovyFlyDS;
    }

    /* 
     * @see com.groovyfly.admin.service.productmanagement.ProductManagementReportingIF#getStockCount2ProductCount()
     */
    @Override
    public Map<Integer, Integer> getStockCount2ProductCount() throws Exception {
        Map<String, Integer> groupingCache = new LinkedHashMap<String, Integer>();
        groupingCache.put("0", 0);
        groupingCache.put("< 6", 0);
        groupingCache.put("7 - 12", 0);
        groupingCache.put("13 - 24", 0);
        groupingCache.put("25 - 48", 0);
        groupingCache.put("> 48", 0);
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT " +
                "    CASE " +
                "    WHEN stockLevel = 0 THEN '0' " +
                "    WHEN stockLevel <= 6 THEN '< 6' " +
                "    WHEN stockLevel >= 7 and stockLevel <= 12 THEN '7 - 12' " +
                "    WHEN stockLevel >= 13 and stockLevel <= 24 THEN '13 - 24' " +
                "    WHEN stockLevel >= 25 and stockLevel <= 48 THEN '25 - 48' " +
                "    WHEN stockLevel > 48 THEN '> 48' " +
                "    WHEN stockLevel IS NULL THEN 'Not Filled In (NULL)' " +
                "    END as stockRange," +
                "COUNT(1) AS productCount " +
                "FROM (SELECT stockLevel FROM product where stockLevel IS NOT NULL) as derived " +
                "GROUP BY stockRange " +
                "ORDER BY stockRange";

            log.info(sql);

            stmt = conn.createStatement();

            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                groupingCache.put(rs.getString(1), rs.getInt(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(stmt);
            DbUtil.closeResultSet(rs);
        }
        
        Map<Integer, Integer> toReturn = new LinkedHashMap<Integer, Integer>();
        int tickIndex = 1;
        for (Entry<String, Integer> e : groupingCache.entrySet()) {
            toReturn.put(tickIndex, e.getValue());
            tickIndex +=2;
        }
        
        return toReturn;
    }

    /* 
     * @see com.groovyfly.admin.service.productmanagement.ProductManagementReportingIF#getTotals()
     */
    @Override
    public Map<String, Integer> getTotals() throws Exception {
        Map<String, Integer> toReturn = new LinkedHashMap<String, Integer>();
      
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT " +
                "COUNT(productId) AS products, " +
                "SUM(isGroupingProduct) AS groupings, " +
                "COUNT(productId) - SUM(isGroupingProduct) AS nonGroupings, " +
                "SUM(stockLevel) AS stockedItems, " +
                "countDistinctGrouped.groupedProduct AS groupedProducts, " +
                "(COUNT(productId) - SUM(isGroupingProduct)) - countDistinctGrouped.groupedProduct AS nonGroupedProduct " +
                "FROM product " +
                "    JOIN (" +
                "        SELECT COUNT(distinctGrouped.productId) AS groupedProduct " +
                "        FROM (" +
                "            SELECT DISTINCT productId " +
                "            FROM groupingproduct2product" +
                "         ) distinctGrouped" +
                ") AS countDistinctGrouped ON 1 = 1";

            log.info(sql);

            stmt = conn.createStatement();

            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                toReturn.put("Products", rs.getInt("products"));
                toReturn.put("Grouped Products", rs.getInt("groupedProducts"));
                toReturn.put("Non-Grouped Products", rs.getInt("nonGroupedProduct"));
                toReturn.put("Groupings", rs.getInt("groupings"));
                toReturn.put("Non-Groupings", rs.getInt("nonGroupings"));
                toReturn.put("Stocked Items", rs.getInt("stockedItems"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(stmt);
            DbUtil.closeResultSet(rs);
        }
        
        return toReturn;
    }

    /* 
     * @see com.groovyfly.admin.service.productmanagement.ProductManagementReportingIF#getPriceStatistics()
     */
    @Override
    public Map<String, BigDecimal> getPriceStatistics() throws Exception {
        Map<String, BigDecimal> toReturn = new LinkedHashMap<String, BigDecimal>();
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT MIN(price) AS minPrice, ROUND(AVG(price), 2) AS averagePrice, MAX(price) AS maxPrice " +
                "FROM product";

            log.info(sql);

            stmt = conn.createStatement();

            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                toReturn.put("Highest Price", rs.getBigDecimal(3));
                toReturn.put("Average Price", rs.getBigDecimal(2));
                toReturn.put("Lowest Price", rs.getBigDecimal(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(stmt);
            DbUtil.closeResultSet(rs);
        }
        
        return toReturn;
    }

    /* 
     * @see com.groovyfly.admin.service.productmanagement.ProductManagementReportingIF#getShopPageStatistics()
     */
    @Override
    public Map<String, Integer> getShopPageStatistics() throws Exception {
        Map<String, Integer> toReturn = new LinkedHashMap<String, Integer>();
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT " +
                "SUM(CASE " +
                "    WHEN LENGTH(trim(pg.title)) <= 50  THEN 1 " +
                "    WHEN LENGTH(trim(pg.title)) >= 70  THEN 1 " +
                "    ELSE 0 " +
                "END) AS problemTitle, " +
                "SUM(CASE " +
                "    WHEN LENGTH(trim(pg.keywords)) = 0 THEN 1 " +
                "    WHEN (LENGTH(pg.keywords) - LENGTH(REPLACE(pg.keywords, ',', '')) + 1) < 10 THEN 1 " +
                "    WHEN (LENGTH(pg.keywords) - LENGTH(REPLACE(pg.keywords, ',', '')) + 1) > 15 THEN 1 " +
                "    ELSE 0 " +
                "END) AS problemKeywords, " +
                "SUM(CASE " +
                "    WHEN LENGTH(trim(pg.description)) < 150 THEN 1 " +
                "    WHEN LENGTH(trim(pg.description)) > 160 THEN 1 " +
                "    ELSE 0 " +
                "END) AS problemDesc, " +
                "SUM(CASE " +
                "    WHEN LENGTH(trim(pg.html)) = 0 THEN 1 " +
                "    ELSE 0 " +
                "end) AS problemHtml " +
                "FROM product p " +
                "JOIN page pg ON p.pageId = pg.pageId " +
                "WHERE p.isGroupingProduct = 1";

            log.info(sql);

            stmt = conn.createStatement();

            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                toReturn.put("Problem Title", rs.getInt(1));
                toReturn.put("Problem Keywords", rs.getInt(2));
                toReturn.put("Problem Description", rs.getInt(3));
                toReturn.put("Problem HTML", rs.getInt(4));
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(stmt);
            DbUtil.closeResultSet(rs);
        }
        
        return toReturn;
    }

    /* 
     * @see com.groovyfly.admin.service.productmanagement.ProductManagementReportingIF#getSupplierStatistics()
     */
    @Override
    public Map<String, Integer> getSupplierStatistics() throws Exception {
        Map<String, Integer> toReturn = new LinkedHashMap<String, Integer>();
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT s.companyName, COUNT(s.supplierId) AS supplyCount " +
                "FROM product p " +
                "JOIN supplier s ON p.supplierId = s.supplierId " +
                "WHERE p.isGroupingProduct = 0 " +
                "GROUP BY s.supplierId, s.companyName " +
                "ORDER BY s.companyName";

            log.info(sql);

            stmt = conn.createStatement();

            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                toReturn.put(rs.getString(1), rs.getInt(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(stmt);
            DbUtil.closeResultSet(rs);
        }
        
        return toReturn;
    }

    /* 
     * @see com.groovyfly.admin.service.productmanagement.ProductManagementReportingIF#getProductStatusStatistics()
     */
    @Override
    public Map<String, Integer> getProductStatusStatistics() throws Exception {
        Map<String, Integer> toReturn = new HashMap<String, Integer>();
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT ps.id, ps.description, count(p.productId) AS count " +
                "FROM productstatus_lu ps " +
                "LEFT OUTER JOIN product p ON p.productStatusId = ps.id " +
                "GROUP BY ps.id " +
                "ORDER BY ps.sortIndex";

            log.info(sql);

            stmt = conn.createStatement();

            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                toReturn.put(rs.getString(2), rs.getInt(3));
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(stmt);
            DbUtil.closeResultSet(rs);
        }
        
        return toReturn;
    }
}
