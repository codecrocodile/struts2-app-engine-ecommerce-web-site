/*
 * LinkDirectoryServiceImpl.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.service.linkdirectory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.groovyfly.common.structures.linkdirectory.Website;
import com.groovyfly.common.structures.linkdirectory.WebsiteCategory;
import com.groovyfly.common.util.DbUtil;
import com.groovyfly.common.util.GroovyFlyDS;

/**
 * @author Chris Hatton
 */
public class LinkDirectoryServiceImpl implements LinkDirectoryServiceIF {
    
    private GroovyFlyDS groovyFlyDS;

    public void setGroovyFlyDS(GroovyFlyDS groovyFlyDS) {
        this.groovyFlyDS = groovyFlyDS;
    }

    /* 
     * @see com.groovyfly.site.service.linkdirectory.LinkDirectoryIF#getLatestAddedWebsites(int)
     */
    @Override
    public List<Website> getLatestAddedWebsites(int limit) throws Exception {
        List<Website> latestList = new ArrayList<Website>();
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT linkDirectoryWebsiteId, websiteUrl, bannerOrLogoUrl, title, description, backlinkUrl, contactEmail, dateAdded, followLink " +
                "FROM linkdirectorywebsite " +
                "ORDER BY dateAdded DESC " +
                "LIMIT " + limit;

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            Website w = null;
            while (rs.next()) {
                w = new Website();
                w.setWebsiteId(rs.getInt(1));
                w.setWebsiteUrl(rs.getString(2));
                w.setBannerOrLogoUrl(rs.getString(3));
                w.setTitle(rs.getString(4));
                w.setDescription(rs.getString(5));
                w.setBacklinkUrl(rs.getString(6));
                w.setContactEmail(rs.getString(7));
                w.setDateAdded(rs.getDate(8));
                w.setFollowLink(rs.getInt(9) == 1 ? true : false);
                
                latestList.add(w);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(stmt);
            DbUtil.closeConnection(conn);
        }
        
        return latestList;
    }

    /* 
     * @see com.groovyfly.site.service.linkdirectory.LinkDirectoryIF#getRootCategories()
     */
    @Override
    public List<WebsiteCategory> getRootCategories() throws Exception {
        List<WebsiteCategory> catList = new ArrayList<WebsiteCategory>();
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT ldc.linkDirectoryCategoryId, p.urlAlias, ldc.name, ldc.description, ( " +
                "       SELECT count(1) AS cnt " +
                "       FROM linkdirectorywebsite2category w2c " +
                "       WHERE w2c.linkDirectoryCategoryId = ldc.linkDirectoryCategoryId ) AS cnt " +
                "FROM linkdirectorycategory ldc " +
                "JOIN page p ON ldc.pageId = p.pageId " +
                "ORDER BY ldc.linkDirectoryCategoryId";

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            WebsiteCategory wc = null;
            while (rs.next()) {
                wc = new WebsiteCategory();
                wc.setWebsiteCategoryId(rs.getInt(1));
                wc.setUrlAlias(rs.getString(2));
                wc.setName(rs.getString(3));
                wc.setDescription(rs.getString(4));
                wc.setWebsiteCount(rs.getInt(5));
                
                catList.add(wc);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(stmt);
            DbUtil.closeConnection(conn);
        }
        
        return catList;
    }

    /* 
     * @see com.groovyfly.site.service.linkdirectory.LinkDirectoryIF#saveWebsite(com.groovyfly.common.structures.linkdirectory.Website)
     */
    @Override
    public void saveWebsite(Website website) throws Exception {
        Connection conn = null;

        try {
            conn = groovyFlyDS.getConnection();
            conn.setAutoCommit(false);
            
            int websiteId = insertWebsite(conn, website);
            insertWebsite2Category(conn, website.getCategoryId(), websiteId);
            
            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            DbUtil.closeConnection(conn);
        }
    }
    
    private int insertWebsite(Connection conn, Website website) throws Exception {
        PreparedStatement pStmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "INSERT INTO linkdirectorywebsite (websiteUrl, bannerOrLogoUrl, title, description, backlinkUrl, contactEmail, dateAdded, followLink)  " +
                "VALUES (?, ?, ?, ?, ?, ?, NOW(), ?)";

            pStmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            int index = 0;
            DbUtil.setString(pStmt, ++index, website.getWebsiteUrl());
            DbUtil.setString(pStmt, ++index, website.getBannerOrLogoUrl());
            DbUtil.setString(pStmt, ++index, website.getTitle());
            DbUtil.setString(pStmt, ++index, website.getDescription());
            DbUtil.setString(pStmt, ++index, website.getBacklinkUrl());
            DbUtil.setString(pStmt, ++index, website.getContactEmail());
            DbUtil.setInt(pStmt, ++index, website.isFollowLink());

            pStmt.executeUpdate();
            rs = pStmt.getGeneratedKeys();

            while (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(pStmt);
        }
        
        return -1;
    }
    
    private void insertWebsite2Category(Connection conn, int categoryId, int websiteId) throws Exception {
        PreparedStatement pStmt = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "INSERT INTO linkdirectorywebsite2category (linkDirectoryCategoryId, linkDirectoryWebsiteId)  " +
                "VALUES (?, ?);";

            pStmt = conn.prepareStatement(sql);
            int index = 0;
            DbUtil.setInt(pStmt, ++index, categoryId);
            DbUtil.setInt(pStmt, ++index, websiteId);

            pStmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(pStmt);
        }
    }

    /* 
     * @see com.groovyfly.site.service.linkdirectory.LinkDirectoryServiceIF#getWebsiteCategories(java.lang.String)
     */
    @Override
    public WebsiteCategory getWebsiteCategory(String urlAlias) throws Exception {
        WebsiteCategory category = new WebsiteCategory();
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            int categoryId = this.getCategoryId(conn, urlAlias);
            
            String sql = 
                "SELECT name, description " +
                "FROM linkdirectorycategory " +
                "WHERE linkDirectoryCategoryId = " + categoryId;

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            rs.next();
            
            category.setWebsiteCategoryId(categoryId);
            category.setName(rs.getString(1));
            category.setDescription(rs.getString(2));
            
            List<Website> websites = this.getWebsites(conn, categoryId);
            category.setWebsites(websites);
            category.setWebsiteCount(websites.size());

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(stmt);
            DbUtil.closeConnection(conn);
        }
        
        return category;
    }

    private int getCategoryId(Connection conn, String urlAlias) throws Exception {
        PreparedStatement pStmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT ldc.linkDirectoryCategoryId  " +
                "FROM linkdirectorycategory ldc " +
                "JOIN page p ON ldc.pageId = p.pageId " +
                "WHERE p.urlAlias = ?";

            pStmt = conn.prepareStatement(sql);
            int index = 0;
            DbUtil.setString(pStmt, ++index, urlAlias);

            rs = pStmt.executeQuery();
            rs.next();
            
            return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(pStmt);
        }
    }
    
    public List<Website> getWebsites(Connection conn, int categoryId) throws Exception {
        List<Website> websiteList = new ArrayList<Website>();
        
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT ldw.linkDirectoryWebsiteId, ldw.websiteUrl, ldw.bannerOrLogoUrl, ldw.title, ldw.description, " +
                "       ldw.backlinkUrl, ldw.contactEmail, ldw.dateAdded, ldw.followLink  " +
                "FROM linkdirectorywebsite2category w2c " +
                "JOIN linkdirectorywebsite ldw ON ldw.linkDirectoryWebsiteId = w2c.linkDirectoryWebsiteId  " +
                "WHERE w2c.linkDirectoryCategoryId = " + categoryId;

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            Website w = null;
            while (rs.next()) {
                w = new Website();
                w.setWebsiteId(rs.getInt("linkDirectoryWebsiteId"));
                w.setWebsiteUrl(rs.getString("websiteUrl"));
                w.setBannerOrLogoUrl(rs.getString("bannerOrLogoUrl"));
                w.setTitle(rs.getString("title"));
                w.setDescription(rs.getString("description"));
                w.setBacklinkUrl(rs.getString("backlinkUrl"));
                w.setContactEmail(rs.getString("contactEmail"));
                w.setDateAdded(rs.getDate("dateAdded"));
                w.setFollowLink(rs.getBoolean("followLink"));

                websiteList.add(w);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(stmt);
        }
        
        return websiteList;
    }
}
