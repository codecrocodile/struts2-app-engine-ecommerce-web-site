/*
 * ConfiguratiionServiceImpl.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.service.configuration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Logger;

import com.groovyfly.common.structures.Address;
import com.groovyfly.common.structures.Company;
import com.groovyfly.common.util.DbUtil;
import com.groovyfly.common.util.GroovyFlyDS;

/**
 * @author Chris Hatton
 */
public class ConfigurationServiceImpl implements ConfigurationServiceIF {
    
    private static Logger log = Logger.getLogger(ConfigurationServiceImpl.class.getName());
    
    private final String COMPANY_NAME = "companyName";
    private final String COMPANY_ADDRESS_LINE_1 = "companyAddressLine1";
    private final String COMPANY_ADDRESS_LINE_2 = "companyAddressLine2";
    private final String COMPANY_ADDRESS_LINE_3 = "companyAddressLine3";
    private final String COMPANY_ADDRESS_LINE_4 = "companyAddressLine4";
    private final String COMPANY_COUNTRY = "companyCountry";
    private final String COMPANY_POSTCODE = "companyPostcode";
    private final String CUSTOMER_SERVICE_EMAIL = "customerServiceEmail";
    private final String WEBSITE_URL = "websiteUrl";
    private final String CUSTOMER_SERVICE_PHONE_NUMBER = "customerServicePhoneNumber";
    private final String IS_VAT_REGISTERED = "isVatRegisterd";
    private final String VAT_NUMBER = "vatNumber";
    
    private GroovyFlyDS groovyFlyDS;

    public void setGroovyFlyDS(GroovyFlyDS groovyFlyDS) {
        this.groovyFlyDS = groovyFlyDS;
    }

    /* 
     * @see com.groovyfly.admin.service.configuration.ConfigurationServiceIF#getCompany()
     */
    @Override
    public Company getCompany() throws Exception {
        Company c = new Company();
        Address a = new Address();
        c.setAddress(a);
        
        Connection conn = null;
        Statement pStmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT name, value " +
                "FROM configuration";

            log.info(sql);

            pStmt = conn.createStatement();
            rs = pStmt.executeQuery(sql);
            while (rs.next()) {
                String name = rs.getString(1);
                String value = rs.getString(2);
                if (name.equalsIgnoreCase(COMPANY_NAME)) {
                    c.setName(value);
                } else if (name.equalsIgnoreCase(COMPANY_ADDRESS_LINE_1)) {
                    a.setLine1(value);
                } else if (name.equalsIgnoreCase(COMPANY_ADDRESS_LINE_2)) {
                    a.setLine2(value);
                } else if (name.equalsIgnoreCase(COMPANY_ADDRESS_LINE_3)) {
                    a.setLine3(value);
                } else if (name.equalsIgnoreCase(COMPANY_ADDRESS_LINE_4)) {
                    a.setLine4(value);
                } else if (name.equalsIgnoreCase(COMPANY_COUNTRY)) {
                    a.setCountry(value);
                } else if (name.equalsIgnoreCase(COMPANY_POSTCODE)) {
                    a.setPostcode(value);
                } else if (name.equalsIgnoreCase(CUSTOMER_SERVICE_EMAIL)) {
                    c.setEmail(value);
                } else if (name.equalsIgnoreCase(WEBSITE_URL)) {
                    c.setWebsiteUrl(value);
                } else if (name.equalsIgnoreCase(CUSTOMER_SERVICE_PHONE_NUMBER)) {
                    c.setPhonenNumber(value);
                } else if (name.equalsIgnoreCase(IS_VAT_REGISTERED)) {
                    c.setVatRegistered(Boolean.valueOf(value));
                } else if (name.equalsIgnoreCase(VAT_NUMBER)) {
                    c.setVatNumber(value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(pStmt);
            DbUtil.closeResultSet(rs);
        }
        
        return c;
    }

    /* 
     * @see com.groovyfly.admin.service.configuration.ConfigurationServiceIF#saveCompany(com.groovyfly.common.structures.Company)
     */
    @Override
    public void saveCompany(Company company) throws Exception {
        // TODO Auto-generated method stub
        
    }

}
