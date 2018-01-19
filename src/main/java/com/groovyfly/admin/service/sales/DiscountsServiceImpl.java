/*
 * DiscountsServiceImpl.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.service.sales;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

import com.groovyfly.admin.sales.Discount;
import com.groovyfly.admin.sales.DiscountType;
import com.groovyfly.common.util.DbUtil;
import com.groovyfly.common.util.GroovyFlyDS;

/**
 * @author Chris Hatton
 */
public class DiscountsServiceImpl implements DiscountsServiceIF {
    
    private static Logger log = Logger.getLogger(DiscountsServiceImpl.class.getName());
    
    private GroovyFlyDS groovyFlyDS;

    public void setGroovyFlyDS(GroovyFlyDS groovyFlyDS) {
        this.groovyFlyDS = groovyFlyDS;
    }

    /* 
     * @see com.groovyfly.admin.service.sales.DiscountsIF#getDiscountForDiscountCode(java.lang.String)
     */
    @Override
    public Discount getDiscountForDiscountCode(String discountCode) throws Exception {
        Discount discount = null;
        
        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT d.discountCode, d.discountTypeId, d.startDate, d.endDate, d.fixedValue, dt.name, dt.description " +
                "FROM discount d " +
                "JOIN discounttype dt ON d.discountTypeId = dt.discountTypeId " +
                "WHERE discountCode = ?";

            log.info(sql);

            pStmt = conn.prepareStatement(sql);
            int index = 0;
            DbUtil.setString(pStmt, ++index, discountCode);

            rs = pStmt.executeQuery();
            boolean found = rs.next();
            if (found) {
                discount = new Discount();
                
                discount.setDiscountCode(discountCode);
                
                DiscountType type = new DiscountType();
                type.setDiscountTypeId(rs.getInt("discountTypeId"));
                type.setName(rs.getString("name"));
                type.setDescription(rs.getString("description"));

                discount.setDiscountType(type);
                discount.setStartDate(rs.getDate("startDate"));
                discount.setEndDate(rs.getDate("endDate"));
                discount.setValue(rs.getBigDecimal("fixedValue"));
                
            } 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(pStmt);
            DbUtil.closeResultSet(rs);
        }
        
        return discount;
    }

}
