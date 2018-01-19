/*
 * LookupService.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.groovyfly.common.structures.Country;
import com.groovyfly.common.structures.Lookup;
import com.groovyfly.common.util.DbUtil;
import com.groovyfly.common.util.GroovyFlyDS;

/**
 * @author Chris Hatton
 * 
 *         Created 21 Jul 2012
 */
public class LookupServiceImpl implements LookupServiceIF {

    private static Logger log = Logger.getLogger(LookupServiceImpl.class.getName());

    private GroovyFlyDS groovyFlyDS;

    public void setGroovyFlyDS(GroovyFlyDS groovyFlyDS) {
        this.groovyFlyDS = groovyFlyDS;
    }

    /*
     * @see com.groovyfly.common.service.LookupServiceIF#getSalutations()
     */
    @Override
    public List<Lookup> getSalutations(boolean includeRetired) throws Exception {
        List<Lookup> salutations = new ArrayList<Lookup>();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String whereCondition = null;
            if (includeRetired) {
                whereCondition = "WHERE 1 = 1";
            } else {
                whereCondition = "WHERE retired = 0";
            }

            String sql = 
                "SELECT id, description, shortList, retired " + 
                "FROM salutation_lu " + 
                whereCondition + " " + 
                "ORDER BY id";

            log.info(sql);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            Lookup l = null;
            while (rs.next()) {
                l = new Lookup();

                l.setId(rs.getInt("id"));
                l.setDescription(rs.getString("description"));
                l.setShortList(rs.getBoolean("shortList"));
                l.setRetired(rs.getBoolean("retired"));

                salutations.add(l);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(stmt);
            DbUtil.closeResultSet(rs);
        }

        return salutations;
    }

    /*
     * @see com.groovyfly.common.service.LookupServiceIF#getCountries()
     */
    @Override
    public List<Lookup> getCountries(boolean includeRetired) throws Exception {
        List<Lookup> countries = new ArrayList<Lookup>();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String whereCondition = null;
            if (includeRetired) {
                whereCondition = "WHERE 1 = 1";
            } else {
                whereCondition = "WHERE retired = 0";
            }

            String sql = 
                "SELECT code, description, shortList, shortListOrder, retired " + 
                "FROM countries_lu " + 
                whereCondition + " " + 
                "ORDER BY shortList DESC, shortListOrder ASC, description ASC";

            log.info(sql);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            Country c = null;
            while (rs.next()) {
                c = new Country();
                c.setCode(rs.getString("code"));
                c.setDescription(rs.getString("description"));
                c.setShortList(rs.getBoolean("shortList"));
                c.setShortListOrder(rs.getInt("shortListOrder"));
                c.setRetired(rs.getBoolean("retired"));

                countries.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(stmt);
            DbUtil.closeResultSet(rs);
        }

        return countries;
    }

}
