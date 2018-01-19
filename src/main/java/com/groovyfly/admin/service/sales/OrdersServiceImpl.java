/*
 * OrdersServiceImpl.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.service.sales;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.groovyfly.admin.structures.sales.Order;
import com.groovyfly.admin.structures.sales.OrderItem;
import com.groovyfly.common.structures.Customer;
import com.groovyfly.common.structures.ShippingAddress;
import com.groovyfly.common.util.DbUtil;
import com.groovyfly.common.util.GroovyFlyDS;

/**
 * @author Chris Hatton
 * 
 * Created 7 Dec 2012
 */
public class OrdersServiceImpl implements OrdersServiceIF {

    private GroovyFlyDS groovyFlyDS;

    public void setGroovyFlyDS(GroovyFlyDS groovyFlyDS) {
        this.groovyFlyDS = groovyFlyDS;
    }
    
    /* 
     * @see com.groovyfly.admin.service.sales.OrdersServiceIF#getOrder(java.lang.String)
     */
    @Override
    public Order getOrder(String orderId) throws Exception {
        Connection conn = null;
        try {
            conn = groovyFlyDS.getConnection();

            Order order = this.getOrder(conn, orderId);
            List<OrderItem> orderItems = this.getOrderItems(conn, orderId);
            order.setOrderItems(orderItems);
            
            return order;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
        }
    }
    
    private Order getOrder(Connection conn, String orderId) throws Exception {
        PreparedStatement pStmt = null;
        ResultSet rs = null;

        try {
            String sql = 
                "SELECT orderId, orderStatusCode, " +
                "       shippingName, shippingAddressLine1, shippingAddressLine2, shippingAddressLine3, shippingAddressLine4, " +
                "       shippingCountry, shippingPostcode, shippingAddressStatus, " +
                "       title, forename, surname, email, emailStatus, phoneNumber, " +
                "       paymentprocessor, paypalPayerId, paypalTransationId, paypalPaymentDate, paypalPaymentStatus, payaplPendingReason, " +
                "       subTotalAmount, postageAndPackingAmount, discountCode, discountAmount, total, dateCreated " +
                "FROM `order` " +
                "WHERE orderId = ?";

            pStmt = conn.prepareStatement(sql);
            int index = 0;
            DbUtil.setString(pStmt, ++index, orderId);

            rs = pStmt.executeQuery();
            while(rs.next()) {
                Order o = new Order();
                o.setOrderId(orderId);
                o.setOrderStatusCode(rs.getString("orderStatusCode"));
                o.setShippingAddress(this.extractShippingAddress(rs));
                o.setCustomer(this.extractCustomer(rs));
                o.setPaymentProcessor(rs.getString("paymentprocessor"));
                o.setPaypalPayerId(rs.getString("paypalPayerId"));
                o.setPaypalTransationId(rs.getString("paypalTransationId"));
                o.setPaypalPaymentDate(rs.getDate("paypalPaymentDate"));
                o.setPaypalPaymentStatus(rs.getString("paypalPaymentStatus"));
                o.setPayaplPendingReason(rs.getString("payaplPendingReason"));
                o.setSubTotalAmount(rs.getBigDecimal("subTotalAmount"));
                o.setPostageAndPacking(rs.getBigDecimal("postageAndPackingAmount"));
                o.setDiscountCode(rs.getString("discountCode"));
                if (rs.getBigDecimal("discountAmount") != null) {
                    o.setDiscountAmount(rs.getBigDecimal("discountAmount"));    
                }
                
                o.setTotal(rs.getBigDecimal("total"));
                
                o.setDateCreated(rs.getDate("dateCreated"));
                
                return o;
            }
           
            return null;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(pStmt);
            DbUtil.closeResultSet(rs);
        }
    }
    
    private ShippingAddress extractShippingAddress(ResultSet rs) throws SQLException {
        ShippingAddress ca = new ShippingAddress();
        ca.setName(rs.getString("shippingName"));
        ca.setLine1(rs.getString("shippingAddressLine1"));
        ca.setLine2(rs.getString("shippingAddressLine2"));
        ca.setLine3(rs.getString("shippingAddressLine3"));
        ca.setLine4(rs.getString("shippingAddressLine4"));
        ca.setCountry(rs.getString("shippingCountry"));
        ca.setPostcode(rs.getString("shippingPostcode"));
        
        return ca;
    }
    
    private Customer extractCustomer(ResultSet rs) throws SQLException {
        Customer c = new Customer();
        c.setTitle(rs.getString("title"));
        c.setForename(rs.getString("forename"));
        c.setSurname(rs.getString("surname"));
        c.setEmail(rs.getString("email"));
        c.setEmailStatus(rs.getString("emailStatus"));
        c.setPhoneNumber(rs.getString("phoneNumber"));
        
        return c;
    }
    
    private List<OrderItem> getOrderItems(Connection conn, String orderId) throws Exception {
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        
        PreparedStatement pStmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT orderItemsId, orderId, productId, name, sku, quantity, unitPrice, totalPrice, currencyCode " +
                "FROM orderitems " +
                "WHERE orderId = ?";

            pStmt = conn.prepareStatement(sql);
            int index = 0;
            DbUtil.setString(pStmt, ++index, orderId);
            
            rs = pStmt.executeQuery();
            
            OrderItem oi = null;
            while(rs.next()) {
                oi = new OrderItem();
                oi.setOrderItemId(rs.getInt("orderItemsId"));
                oi.setName(rs.getString("name"));
                oi.setProductId(rs.getInt("productId"));
                oi.setSku(rs.getString("sku"));
                oi.setQuantity(rs.getInt("quantity"));
                oi.setUnitPrice(rs.getBigDecimal("unitPrice"));
                oi.setTotalPrice(rs.getBigDecimal("totalPrice"));
                oi.setCurrencyCode(rs.getString("currencyCode"));
                
                orderItems.add(oi);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(pStmt);
        }
        
        return orderItems;
    }

}
