/*
 * FinanceServiceImpl.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.service.finance;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.groovyfly.admin.structures.finance.PriceRule;
import com.groovyfly.common.structures.Lookup;
import com.groovyfly.common.util.DbUtil;
import com.groovyfly.common.util.GroovyFlyDS;

/**
 * @author Chris Hatton
 * 
 * Created 28 Jul 2012
 */
public class FinanceServiceImpl implements FinanceServiceIF {
    
    private static Logger log = Logger.getLogger(FinanceServiceImpl.class.getName());

    private GroovyFlyDS groovyFlyDS;

    public void setGroovyFlyDS(GroovyFlyDS groovyFlyDS) {
        this.groovyFlyDS = groovyFlyDS;
    }
    
    /* 
     * @see com.groovyfly.admin.service.finance.FinanceServiceIF#getVatRates(boolean)
     */
    @Override
    public List<Lookup> getVatRates(boolean includeRetired) throws Exception {
        List<Lookup> vatRates = new ArrayList<Lookup>();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            StringBuilder sql = new StringBuilder();
            sql.append(
                "SELECT id, description " +
                "FROM vatrate_lu "
            );

            if (includeRetired == false) {
                sql.append(" ");
                sql.append("WHERE retired = 0 ");
            }
            
            sql.append("ORDER BY sortIndex");

            log.info(sql.toString());

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql.toString());

            Lookup l = null;
            while (rs.next()) {
                l = new Lookup();
                l.setId(rs.getInt("id"));
                l.setDescription(rs.getString("description"));
                
                vatRates.add(l);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(stmt);
            DbUtil.closeResultSet(rs);
        }

        return vatRates;
    }

    /* 
     * @see com.groovyfly.admin.service.finance.FinanceServiceIF#getPriceRules(boolean)
     */
    @Override
    public List<PriceRule> getPriceRules(boolean includeRetired) throws Exception {
        List<PriceRule> priceRules = new ArrayList<PriceRule>();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            StringBuilder sql = new StringBuilder();
            sql.append(
                "SELECT id, name, description, code, sortIndex, retired " +
                "FROM pricerule "
            );

            if (includeRetired == false) {
                sql.append(" ");
                sql.append("WHERE retired = 0 ");
            }
            
            sql.append("ORDER BY sortIndex");

            log.info(sql.toString());

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql.toString());

            PriceRule pr = null;
            while (rs.next()) {
                pr = new PriceRule();
                pr.setPriceRuleId(rs.getInt("id"));
                pr.setName(rs.getString("name"));
                pr.setDescription(rs.getString("description"));
                pr.setCode(rs.getString("code"));
                pr.setRetired(rs.getBoolean("retired"));
                
                priceRules.add(pr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(stmt);
            DbUtil.closeResultSet(rs);
        }
        
        return priceRules;
    }

}
