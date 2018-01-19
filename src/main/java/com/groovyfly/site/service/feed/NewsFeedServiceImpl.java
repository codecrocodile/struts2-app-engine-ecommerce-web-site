/*
 * NewsFeedServiceImpl.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.service.feed;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.groovyfly.common.util.DbUtil;
import com.groovyfly.common.util.GroovyFlyDS;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;

/**
 * @author Chris Hatton
 * 
 * Created 31 Aug 2012
 */
public class NewsFeedServiceImpl implements NewsFeedServiceIF {
    
    private static Logger log = Logger.getLogger(NewsFeedServiceImpl.class.getName());
    
    private GroovyFlyDS groovyFlyDS;

    public void setGroovyFlyDS(GroovyFlyDS groovyFlyDS) {
        this.groovyFlyDS = groovyFlyDS;
    }

    /* 
     * @see com.groovyfly.site.service.feed.NewsFeedServiceIF#getSyndEntries()
     */
    @Override
    public List<SyndEntry> getSyndEntries(boolean includeRetired) throws Exception {
        List<SyndEntry> entries = new ArrayList<SyndEntry>();
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String whereCondition = null;
            if (includeRetired) {
                whereCondition = "WHERE 1 = 1 ";
            } else {
                whereCondition = "WHERE retired = 0 ";
            }
            
            String sql = 
                "SELECT id, title, link, content, publicationDate, retired " +
                "FROM newsfeedentry " + 
                whereCondition;

            log.info(sql);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            SyndEntry e = null;
            while (rs.next()) {
                e = new SyndEntryImpl();
                
                String title = rs.getString("title");
                if (title != null) {
                    e.setTitle(title);    
                }
                String link = rs.getString("link");
                if (link != null) {
                    e.setLink(link);
                }
                
                SyndContent content = new SyndContentImpl();
                content.setType("text/html");
                content.setValue(rs.getString("content"));
                e.setDescription(content);
                e.setPublishedDate(rs.getDate("publicationDate"));
                
                entries.add(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(stmt);
            DbUtil.closeResultSet(rs);
        }

        return entries;
    }

    /* 
     * @see com.groovyfly.site.service.feed.NewsFeedServiceIF#saveSyndEntry(com.sun.syndication.feed.synd.SyndEntry)
     */
    @Override
    public void saveSyndEntry(SyndEntry syndEntry) throws Exception {
        Connection conn = null;
        PreparedStatement pStmt = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "INSERT INTO newsfeedentry (title, link, content, publicationDate, retired) " +
                "VALUES (?, ?, ?, ?, 0)";

            log.info(sql);

            pStmt = conn.prepareStatement(sql);
            int index = 0;
            DbUtil.setString(pStmt, ++index, syndEntry.getTitle());
            DbUtil.setString(pStmt, ++index, syndEntry.getLink());
            SyndContent sc = syndEntry.getDescription();
            DbUtil.setString(pStmt, ++index, sc.getValue());
            DbUtil.setDate(pStmt, ++index, syndEntry.getPublishedDate());
            
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
     * @see com.groovyfly.site.service.feed.NewsFeedServiceIF#retireSyndEntry(com.sun.syndication.feed.synd.SyndEntry)
     */
    @Override
    public void retireSyndEntry(int syndEntryId) throws Exception {
        Connection conn = null;
        PreparedStatement pStmt = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "UPDATE newsfeedentry " +
            	"SET retired = 1 " +
            	"WHERE id = ?";

            log.info(sql);

            pStmt = conn.prepareStatement(sql);
            int index = 0;
            DbUtil.setInt(pStmt, ++index, syndEntryId);

            pStmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(pStmt);
        }
    }
    
}
