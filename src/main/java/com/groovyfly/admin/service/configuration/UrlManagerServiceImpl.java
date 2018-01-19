/*
 * UrlManagerServiceImpl.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.service.configuration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Logger;

import com.groovyfly.common.util.DbUtil;
import com.groovyfly.common.util.GroovyFlyDS;
import com.opensymphony.xwork2.util.URLUtil;


/**
 * @author Chris Hatton
 */
public class UrlManagerServiceImpl implements UrlManagerServiceIF {
    
    private static Logger log = Logger.getLogger(UrlManagerServiceImpl.class.getName());
    
    private GroovyFlyDS groovyFlyDS;

    public void setGroovyFlyDS(GroovyFlyDS groovyFlyDS) {
        this.groovyFlyDS = groovyFlyDS;
    }
    
    /* 
     * @see com.groovyfly.common.service.UrlManagerServiceIF#getNewUrlAliasForCategoryName(java.lang.String, int, java.lang.String)
     */
    @Override
    public String getUrlAliasForNewCategory(int parentCategoryId, String categoryNameOrAlias) throws Exception {
        String parentUrlAlias = this.getCategoryUrlAlias(parentCategoryId);
        if (parentUrlAlias == null || parentUrlAlias.trim().equals("")) {
            parentUrlAlias = Namespace.productCategory;
        }
        String pageAlias = this.getPageAlias(categoryNameOrAlias);
        String categoryUrlAlias = parentUrlAlias + "/" + pageAlias;
         
        if (this.isUrlAliasValid(categoryUrlAlias) == false) {
            throw new InvalidUrlAliasException();
        }
        
        if (this.isUrlAlaisUnique(categoryUrlAlias, null) == false) {
            throw new DuplicateUrlAliasException();
        }
        
        return categoryUrlAlias;
    }

    /* 
     * @see com.groovyfly.admin.service.configuration.UrlManagerServiceIF#getUrlAliasForExistingCategory(int, int, java.lang.String)
     */
    @Override
    public String getUrlAliasForExistingCategory(int existingPageId, int parentCategoryId, String categoryNameOrAlias) throws Exception {
        String parentUrlAlias = this.getCategoryUrlAlias(parentCategoryId);
        if (parentUrlAlias == null || parentUrlAlias.trim().equals("")) {
            parentUrlAlias = Namespace.productCategory;
        }
        
        if (categoryNameOrAlias.trim().startsWith(parentUrlAlias)) {
            categoryNameOrAlias = categoryNameOrAlias.substring(parentUrlAlias.length(), categoryNameOrAlias.length());
        }
        
        String pageAlias = this.getPageAlias(categoryNameOrAlias);
        String categoryUrlAlias = parentUrlAlias + "/" + pageAlias;
         
        if (this.isUrlAliasValid(categoryUrlAlias) == false) {
            throw new InvalidUrlAliasException();
        }
        
        if (this.isUrlAlaisUnique(categoryUrlAlias, existingPageId) == false) {
            throw new DuplicateUrlAliasException();
        }
        
        return categoryUrlAlias;
    }

    /* 
     * @see com.groovyfly.admin.service.configuration.UrlManagerServiceIF#getNewUrlAliasForProduct(int, java.lang.String)
     */
    @Override
    public String getUrlAliasForNewProduct(int categoryId, String productNameOrAlias) throws Exception {
        String parentUrlAlias = this.getCategoryUrlAlias(categoryId);
        if (parentUrlAlias == null || parentUrlAlias.trim().equals("")) {
            parentUrlAlias = Namespace.productCategory;
        }
        parentUrlAlias = parentUrlAlias.replace(Namespace.productCategory, Namespace.product);
        
        String pageAlias = this.getPageAlias(productNameOrAlias);
        String productUrlAlias = parentUrlAlias + "/" + pageAlias;
        
        
        if (this.isUrlAliasValid(productUrlAlias) == false) {
            throw new InvalidUrlAliasException();
        }
        
        if (this.isUrlAlaisUnique(productUrlAlias, null) == false) {
            throw new DuplicateUrlAliasException();
        }
        
        return productUrlAlias;
    }
    
    /* 
     * @see com.groovyfly.admin.service.configuration.UrlManagerServiceIF#getUrlAliasForExistingProduct(int, java.lang.String)
     */
    @Override
    public String getUrlAliasForExistingProduct(int existingPageId, int categoryId, String productNameOrAlias) throws Exception {
        
        log.info("getUrlAliasForExistingProduct()" + productNameOrAlias);
        
        String parentUrlAlias = this.getCategoryUrlAlias(categoryId);
        if (parentUrlAlias == null || parentUrlAlias.trim().equals("")) {
            parentUrlAlias = Namespace.productCategory;
        }
        parentUrlAlias = parentUrlAlias.replace(Namespace.productCategory, Namespace.product);
        
        log.info(parentUrlAlias);
        
        if (productNameOrAlias.trim().startsWith(parentUrlAlias)) {
            log.info("sub string");
            productNameOrAlias = productNameOrAlias.substring(parentUrlAlias.length(), productNameOrAlias.length());
        }
        log.info(productNameOrAlias);
        
        String pageAlias = this.getPageAlias(productNameOrAlias);
        String productUrlAlias = parentUrlAlias + "/" + pageAlias;
        
        if (this.isUrlAliasValid(productUrlAlias) == false) {
            throw new InvalidUrlAliasException();
        }
        
        if (this.isUrlAlaisUnique(productUrlAlias, existingPageId) == false) {
            throw new DuplicateUrlAliasException();
        }
        
        log.info(productUrlAlias);
        
        return productUrlAlias;
    }
    
    private String getCategoryUrlAlias(int categoryId) throws Exception {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT urlAlias " +
                "FROM category " +
                "WHERE categoryId = " + categoryId;

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            rs.next();

            return rs.getString(1);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(stmt);
            DbUtil.closeConnection(conn);
        }
    }
    
    private String getPageAlias(String pageNameOrAlias) {
        
        if (pageNameOrAlias.trim().length() < 1)  {
            throw new IllegalStateException("");
        }
        
        // 1. trim leading and trailing whitespace
        // 2. convert to lower case
        // 3. replace & with and 
        // 4. remove single quotes
        // 5. replace whitespace with "-"
        // 6. replace chars but [a-z][1-9][/] with ""
        // 7. replace multiple / with one /
        // 8. remove first and last char if /
        
        pageNameOrAlias = pageNameOrAlias.trim();
        pageNameOrAlias = pageNameOrAlias.toLowerCase();
        pageNameOrAlias = pageNameOrAlias.replace("&", "and");
        pageNameOrAlias = pageNameOrAlias.replace("'", "");
        pageNameOrAlias = pageNameOrAlias.replaceAll("[\\s]+", "-");
        pageNameOrAlias = pageNameOrAlias.replaceAll("[^a-z1-9/]", "-");
        pageNameOrAlias = pageNameOrAlias.replaceAll("/{2,}", "/");

        if (pageNameOrAlias.substring(0, 1).equals("/")) {
            pageNameOrAlias = pageNameOrAlias.substring(1, pageNameOrAlias.length());
        }
        if (pageNameOrAlias.substring(pageNameOrAlias.length()  - 1, pageNameOrAlias.length()).equals("/")) {
            pageNameOrAlias = pageNameOrAlias.substring(0, pageNameOrAlias.length() - 1);
        }
        
        return pageNameOrAlias;
    }
    
    private boolean isUrlAliasValid(String urlAlias) {
        String sampleBaseUrl = "http://goovyfly.com";
        return URLUtil.verifyUrl(sampleBaseUrl + urlAlias);
    }
    
    private boolean isUrlAlaisUnique(String urlAlias, Integer existingPageId) throws Exception {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT count(1) AS cnt " +
                "FROM page  " +
                "WHERE urlAlias = " + DbUtil.quoteStringOrNull(urlAlias);
            
            if (existingPageId != null) {
                sql += " AND pageId != " + existingPageId;
            }

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            rs.next();
            
            if (rs.getInt(1) == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(stmt);
            DbUtil.closeConnection(conn);
        }
    }

}
