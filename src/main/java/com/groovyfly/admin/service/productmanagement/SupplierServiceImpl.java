/*
 * SupplierServiceImpl.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.service.productmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.groovyfly.admin.structures.Supplier;
import com.groovyfly.common.structures.Address;
import com.groovyfly.common.structures.Person;
import com.groovyfly.common.util.DbUtil;
import com.groovyfly.common.util.GroovyFlyDS;

/**
 * @author Chris Hatton
 * 
 * Created 19 Jul 2012
 */
public class SupplierServiceImpl implements SupplierServiceIF {

    private static Logger log = Logger.getLogger(SupplierServiceImpl.class.getName());

    private GroovyFlyDS groovyFlyDS;

    /**
     * Constructor
     */
    public SupplierServiceImpl() {
        super();
    }

    public void setGroovyFlyDS(GroovyFlyDS groovyFlyDS) {
        this.groovyFlyDS = groovyFlyDS;
    }

    /* 
     * @see com.groovyfly.common.service.SupplierServiceIF#getSuppliers()
     */
    @Override
    public List<Supplier> getSuppliers(boolean includeRetired) throws Exception {

        List<Supplier> suppliers = new ArrayList<Supplier>();

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
                "SELECT supplierId, shortCode, companyName, contactTitle, contactForname, contactSurname, " +
                "	addressLine1, addressLine2, addressLine3, addressLine4, country, postocde, " +
                "	tel, mobile, fax, email, notes, retired " +
                "FROM supplier " +
                whereCondition + 
                "ORDER BY companyName ";

            log.info(sql);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            Supplier s = null;
            Person p = null;
            Address a = null;
            while (rs.next()) {
                s = new Supplier();
                s.setRetired(rs.getInt("retired") == 1 ? true : false);

                s.setSupplierId(rs.getInt("supplierId"));
                s.setShortCode(rs.getString("shortCode"));
                
                if (s.isRetired()) {
                    s.setCompanyName(rs.getString("companyName") + " (retired)");                    
                } else {
                    s.setCompanyName(rs.getString("companyName"));    
                }

                p = new Person();
                p.setTitle(rs.getString("contactTitle"));
                p.setForename(rs.getString("contactForname"));
                p.setSurname(rs.getString("contactSurname"));
                s.setContactPerson(p);

                a = new Address();
                a.setLine1(rs.getString("addressLine1"));
                a.setLine2(rs.getString("addressLine2"));
                a.setLine3(rs.getString("addressLine3"));
                a.setLine4(rs.getString("addressLine4"));
                a.setCountry(rs.getString("country"));
                a.setPostcode(rs.getString("postocde"));
                s.setAddress(a);

                s.setTel(rs.getString("tel"));
                s.setMobile(rs.getString("mobile"));
                s.setFax(rs.getString("fax"));
                s.setEmail(rs.getString("email"));
                s.setNotes(rs.getString("notes"));

                suppliers.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(stmt);
            DbUtil.closeResultSet(rs);
        }

        return suppliers;
    }

    /* 
     * @see com.groovyfly.admin.service.SupplierServiceIF#getSupplier(int)
     */
    @Override
    public Supplier getSupplier(int id) throws Exception {

        Supplier supplier = new Supplier();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT supplierId, shortCode, companyName, contactTitle, contactForname, contactSurname, " +
                "	addressLine1, addressLine2, addressLine3, addressLine4, country, postocde, " +
                "	tel, mobile, fax, email, notes, retired " +
                "FROM supplier " +
                "WHERE supplierId = " + id;

            log.info(sql);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                supplier.setSupplierId(rs.getInt("supplierId"));
                supplier.setShortCode(rs.getString("shortCode"));
                supplier.setCompanyName(rs.getString("companyName"));

                Person p = new Person();
                p.setTitle(rs.getString("contactTitle"));
                p.setForename(rs.getString("contactForname"));
                p.setSurname(rs.getString("contactSurname"));
                supplier.setContactPerson(p);

                Address a = new Address();
                a.setLine1(rs.getString("addressLine1"));
                a.setLine2(rs.getString("addressLine2"));
                a.setLine3(rs.getString("addressLine3"));
                a.setLine4(rs.getString("addressLine4"));
                a.setCountry(rs.getString("country"));
                a.setPostcode(rs.getString("postocde"));
                supplier.setAddress(a);

                supplier.setTel(rs.getString("tel"));
                supplier.setMobile(rs.getString("mobile"));
                supplier.setFax(rs.getString("fax"));
                supplier.setEmail(rs.getString("email"));
                supplier.setNotes(rs.getString("notes"));
                supplier.setRetired(rs.getInt("retired") == 1 ? true : false);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(stmt);
            DbUtil.closeResultSet(rs);
        }

        return supplier;
    }

    /* 
     * @see com.groovyfly.common.service.SupplierServiceIF#saveSuppliers(java.util.List)
     */
    @Override
    public void saveSuppliers(List<Supplier> suppliers) throws SQLException {

    }

    /* 
     * @see com.groovyfly.admin.service.productmanagement.SupplierServiceIF#saveSupplier(com.groovyfly.admin.structures.Supplier)
     */
    @Override
    public void saveSupplier(Supplier supplier) throws Exception {
        if (supplier.getSupplierId() > 0) {
            this.updateSupplier(supplier);
        } else {
            this.insertSupplier(supplier);
        }
    }

    /* 
     * @see com.groovyfly.admin.service.SupplierServiceIF#insertSupplier(com.groovyfly.admin.structures.Supplier)
     */
    @Override
    public void insertSupplier(Supplier supplier) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "INSERT INTO supplier (" +
                "	shortCode, companyName, contactTitle, contactForname, contactSurname, " +
                "	addressLine1, addressLine2, addressLine3, addressLine4, country, postocde, " +
                "	tel, mobile, fax, email, notes ) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            log.fine(sql);

            stmt = conn.prepareStatement(sql);

            int index = 0;
            DbUtil.setString(stmt, ++index, supplier.getShortCode());
            DbUtil.setString(stmt, ++index, supplier.getCompanyName());
            DbUtil.setString(stmt, ++index, supplier.getContactPerson().getTitle());
            DbUtil.setString(stmt, ++index, supplier.getContactPerson().getForename());
            DbUtil.setString(stmt, ++index, supplier.getContactPerson().getSurname());
            DbUtil.setString(stmt, ++index, supplier.getAddress().getLine1());
            DbUtil.setString(stmt, ++index, supplier.getAddress().getLine2());
            DbUtil.setString(stmt, ++index, supplier.getAddress().getLine3());
            DbUtil.setString(stmt, ++index, supplier.getAddress().getLine4());
            DbUtil.setString(stmt, ++index, supplier.getAddress().getCountry());
            DbUtil.setString(stmt, ++index, supplier.getAddress().getPostcode());
            DbUtil.setString(stmt, ++index, supplier.getTel());
            DbUtil.setString(stmt, ++index, supplier.getMobile());
            DbUtil.setString(stmt, ++index, supplier.getFax());
            DbUtil.setString(stmt, ++index, supplier.getEmail());
            DbUtil.setString(stmt, ++index, supplier.getNotes());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(stmt);
        }
    }

    /* 
     * @see com.groovyfly.admin.service.SupplierServiceIF#updateSupplier(com.groovyfly.admin.structures.Supplier)
     */
    @Override
    public void updateSupplier(Supplier supplier) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "UPDATE supplier " +
                "SET shortCode = ?, companyName = ?, contactTitle = ?, contactForname = ?, contactSurname = ?, " +
                "	addressLine1 = ?, addressLine2 = ?, addressLine3 = ?, addressLine4 = ?, country = ?, postocde = ?, " +
                "	tel = ?, mobile = ?, fax = ?, email = ?, notes = ?, retired = ? " +
                "WHERE supplierId = ?";

            log.fine(sql);

            stmt = conn.prepareStatement(sql);

            int index = 0;
            DbUtil.setString(stmt, ++index, supplier.getShortCode());
            DbUtil.setString(stmt, ++index, supplier.getCompanyName());
            DbUtil.setString(stmt, ++index, supplier.getContactPerson().getTitle());
            DbUtil.setString(stmt, ++index, supplier.getContactPerson().getForename());
            DbUtil.setString(stmt, ++index, supplier.getContactPerson().getSurname());
            DbUtil.setString(stmt, ++index, supplier.getAddress().getLine1());
            DbUtil.setString(stmt, ++index, supplier.getAddress().getLine2());
            DbUtil.setString(stmt, ++index, supplier.getAddress().getLine3());
            DbUtil.setString(stmt, ++index, supplier.getAddress().getLine4());
            DbUtil.setString(stmt, ++index, supplier.getAddress().getCountry());
            DbUtil.setString(stmt, ++index, supplier.getAddress().getPostcode());
            DbUtil.setString(stmt, ++index, supplier.getTel());
            DbUtil.setString(stmt, ++index, supplier.getMobile());
            DbUtil.setString(stmt, ++index, supplier.getFax());
            DbUtil.setString(stmt, ++index, supplier.getEmail());
            DbUtil.setString(stmt, ++index, supplier.getNotes());
            stmt.setInt(++index, supplier.isRetired() == true ? 1 : 0);
            stmt.setInt(++index, supplier.getSupplierId());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(stmt);
        }
    }

    public void retireSupplier(int id) throws Exception {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "UPDATE supplier " +
                "SET retired = 1 " +
                "WHERE supplierId = " + id;

            log.info(sql);

            stmt = conn.createStatement();
            stmt.executeUpdate(sql);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(stmt);
        }
    }

}
