/*
 * CollectionServiceImpl.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.service.collection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.groovyfly.common.util.DbUtil;
import com.groovyfly.common.util.GroovyFlyDS;

/**
 * @author Chris Hatton
 */
public class CollectionServiceImpl implements CollectionServiceIF {
    
    private GroovyFlyDS groovyFlyDS;

    public void setGroovyFlyDS(GroovyFlyDS groovyFlyDS) {
        this.groovyFlyDS = groovyFlyDS;
    }

    /* 
     * @see com.groovyfly.admin.service.collection.CollectionServiceIF#getProductIdsInCollection(int)
     */
    @Override
    public List<Integer> getProductIdsInCollection(String urlAlias) throws Exception {
        List<Integer> idList = new ArrayList<Integer>();
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT c2p.productId " +
                "FROM collection c " +
                "JOIN collection2product c2p ON c2p.collectionId = c.collectionId " +
                "WHERE c.urlAlias = " + DbUtil.quoteStringOrNull(urlAlias);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                idList.add(rs.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(stmt);
            DbUtil.closeConnection(conn);
        }
        
        return idList;
    }

}
