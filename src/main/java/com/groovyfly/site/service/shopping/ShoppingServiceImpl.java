/*
 * ShoppingServiceImpl.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.service.shopping;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.apache.commons.codec.digest.DigestUtils;

import com.groovyfly.common.structures.Customer;
import com.groovyfly.common.structures.Product;
import com.groovyfly.common.structures.ProductImages;
import com.groovyfly.common.structures.ShippingAddress;
import com.groovyfly.common.structures.ShoppingCart;
import com.groovyfly.common.structures.ShoppingCartEntry;
import com.groovyfly.common.structures.exceptions.NotEnoughStockException;
import com.groovyfly.common.util.DbUtil;
import com.groovyfly.common.util.GroovyFlyDS;
import com.groovyfly.common.util.PaymentProcessor;

/**
 * @author Chris Hatton
 */
public class ShoppingServiceImpl implements ShoppingServiceIF {
    
    private static Logger log = Logger.getLogger(ShoppingServiceImpl.class.getName());
    
    private GroovyFlyDS groovyFlyDS;
    
    public void setGroovyFlyDS(GroovyFlyDS groovyFlyDS) {
        this.groovyFlyDS = groovyFlyDS;
    }
    
    /* 
     * Adds or updates the shopping cart in database and returns an entry to be added to the session shopping cart.
     * 
     * @see com.groovyfly.site.service.shopping.ShoppingServiceIF#addProductToShoppingCart(java.lang.String, int, java.util.Map, int)
     */
    @Override
    public ShoppingCartEntry addConfigurableGroupingProductToCart(ShoppingCart shoppingCart, int groupingProductId, 
            Map<String, Integer> attributeToAttributeValue, int quantity) throws NotEnoughStockException, Exception {
        Connection conn = null;
        PreparedStatement pStmt = null;
        
        try {
            conn = groovyFlyDS.getConnection();

            Product productToAdd = this.getProductUnit(conn, groupingProductId, attributeToAttributeValue);
            int productsInStock = this.getProductsInStock(conn, productToAdd.getProductId());
            int quantityAddedAlready = this.hasProductAdded(conn, shoppingCart.getShoppingCartId(), productToAdd.getProductId());
            productsInStock = productsInStock - quantityAddedAlready;
            
            if (quantity > productsInStock) {
                throw new NotEnoughStockException("Not enough products to stock", productsInStock, productToAdd.getName());
            }
            boolean hasProductAdded = quantityAddedAlready > 0 ? true : false;
            
            String sql = null;
            if (hasProductAdded) {
                sql = 
                    "UPDATE shoppingcart sc " +
                    "SET quantity = quantity + ?, date = NOW() " +
                    "WHERE shoppingCartId = ? AND productId = ?";                
            } else {
                sql = 
                    "INSERT INTO shoppingcart (shoppingCartId, productId, quantity, date) " +
                    "VALUES (?, ?, ?, NOW())";
            }
            
            pStmt = conn.prepareStatement(sql);
            
            if (hasProductAdded) {
                int index = 0;
                DbUtil.setInt(pStmt, ++index, quantity);
                DbUtil.setString(pStmt, ++index, shoppingCart.getShoppingCartId());
                DbUtil.setInt(pStmt, ++index, productToAdd.getProductId());
            } else {
                int index = 0;
                DbUtil.setString(pStmt, ++index, shoppingCart.getShoppingCartId());
                DbUtil.setInt(pStmt, ++index, productToAdd.getProductId());
                DbUtil.setInt(pStmt, ++index, quantity);
            }
            
            pStmt.executeUpdate();
            
            ShoppingCartEntry e = new ShoppingCartEntry(shoppingCart.getLocale());
            e.setProductId(productToAdd.getProductId());
            e.setName(productToAdd.getName());
            e.setSku(productToAdd.getSku());
            e.setSmallerImageUrl(productToAdd.getImages().getSmallerImageUrl());
            e.setLargeImageUrl(productToAdd.getImages().getLargeImageUrl());
            e.setImageAltTagDesc(productToAdd.getImages().getAltTagDescription());
            e.setQuantity(quantity);
            e.setUnitPrice(productToAdd.getPrice());
            
            shoppingCart.addShoppingCartEntry(e);
            
            return e; // return the entry as we need to display the information
            
        } catch(NotEnoughStockException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(pStmt);
        }
    }
    
    /* 
     * @see com.groovyfly.site.service.shopping.ShoppingServiceIF#getProductsInStock(int)
     */
    private int getProductsInStock(Connection conn, int productId) throws Exception {
        int productsInStock = 0;
        
        PreparedStatement pStmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT stockLevel " +
                "FROM product " +
                "WHERE productId = ?";
            
            pStmt = conn.prepareStatement(sql);
            int index = 0;
            DbUtil.setInt(pStmt, ++index, productId);
            rs = pStmt.executeQuery();
            while(rs.next()) {
                productsInStock = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(pStmt);
            DbUtil.closeResultSet(rs);
        }
        
        return productsInStock;
    }
    
    /*
     * This tells us if the customer already have an entry in their shopping cart for a product. We can use this
     * to decide if we should insert a new entry or update the existing entry.
     */
    private int hasProductAdded(Connection conn, String shoppingCartId, int productId) throws Exception {
        int quantityAdded = 0;
        
        PreparedStatement pStmt = null;
        ResultSet rs = null;

        try {

            String sql = 
                "SELECT quantity " +
                "FROM shoppingcart sc " +
                "WHERE sc.shoppingCartId = ? AND sc.productId = ?";

            pStmt = conn.prepareStatement(sql);
            int index = 0;
            DbUtil.setString(pStmt, ++index, shoppingCartId);
            DbUtil.setInt(pStmt, ++index, productId);

            rs = pStmt.executeQuery();
            while (rs.next()) {
                quantityAdded = rs.getInt(1);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(pStmt);
            DbUtil.closeResultSet(rs);
        }
        
        return quantityAdded;
    }
    
    /*
     * Gets the product unit for the given grouped product id and attributes.
     */
    private Product getProductUnit(Connection conn, int groupingProductId, Map<String, Integer> attributeToAttributeValue) throws Exception {
        Statement stmt = null;
        ResultSet rs = null;

        try {

            String attributeCondition = this.getWhereConditionsForAttributes(attributeToAttributeValue);
            
            String sql = 
                "SELECT p.productId, p.name, p.description, p.smallerImageUrl, p.largeImageUrl, p.imageAltTagDesc, p.sku, p.price " +
                "FROM product p " +
                "WHERE p.productId IN ( " +
                "       SELECT productId " +
                "       FROM groupingproduct2product " +
                "WHERE groupingProductId = " + groupingProductId + " ) AND p.retired = 0 " + attributeCondition;
            
            log.info(sql);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            Product p = null;
            while (rs.next()) {
                p = new Product();    
                p.setProductId(rs.getInt("productId"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                
                ProductImages i = new ProductImages();
                i.setSmallerImageUrl(rs.getString("smallerImageUrl"));
                i.setLargeImageUrl(rs.getString("largeImageUrl"));
                i.setAltTagDescription(rs.getString("imageAltTagDesc"));
                p.setImages(i);
                
                p.setSku(rs.getString("sku"));
                p.setPrice(rs.getBigDecimal("price"));
            }  
            
            return p;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(stmt);
            DbUtil.closeResultSet(rs);
        }
    }

    /*
     * Where condition to get product with the given attributes. 
     * 
     * NOTE: this might be changed in the future if we change to store the attribute for a product differently.
     */
    private String getWhereConditionsForAttributes(Map<String, Integer> attributeToAttributeValue) {
        StringBuilder sb = new StringBuilder();
        
        for (Entry<String, Integer> e : attributeToAttributeValue.entrySet()) {
           sb.append("AND ( ");
           sb.append("(p.attributeId1 = " + Integer.valueOf(e.getKey()) + " AND p.attributeValueId1 = " + e.getValue() + ")");
           sb.append(" OR ");
           sb.append("(p.attributeId2 = " + Integer.valueOf(e.getKey()) + " AND p.attributeValueId2 = " + e.getValue() + ")");
           sb.append(" OR ");
           sb.append("(p.attributeId3 = " + Integer.valueOf(e.getKey()) + " AND p.attributeValueId3 = " + e.getValue() + ")");
           sb.append(") ");
        }
        
        return sb.toString();
    }

    /* 
     * @see com.groovyfly.site.service.shopping.ShoppingServiceIF#adjustProductQuantityInCart(java.lang.String, int, int)
     */
    @Override
    public void adjustProductQuantityInCart(String shoppingCartId, int productId, int amount) throws NotEnoughStockException, Exception {
        Connection conn = null;
        PreparedStatement pStmt = null;

        try {
            conn = groovyFlyDS.getConnection();
            
            if (amount > 0) {
                int productsInStock = this.getProductsInStock(conn, productId);
                int quantityAddedAlready = this.hasProductAdded(conn, shoppingCartId, productId);
                
                if (productsInStock == (productsInStock - quantityAddedAlready)) {
                    throw new NotEnoughStockException("Not enough products to stock", productsInStock, "");
                }    
            }

            String sql = 
                "UPDATE shoppingcart " +
                "SET quantity = quantity + (" + amount + ") " +
                "WHERE shoppingCartId = ? and productId = ?";

            pStmt = conn.prepareStatement(sql);
            int index = 0;
            DbUtil.setString(pStmt, ++index, shoppingCartId);
            DbUtil.setInt(pStmt, ++index, productId);

            pStmt.executeUpdate();

        } catch (NotEnoughStockException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log.severe(sw.toString());
            
            throw e;
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log.severe(sw.toString());
            
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(pStmt);
        }
    }

    /* 
     * @see com.groovyfly.site.service.shopping.ShoppingServiceIF#deleteProductFromCart(java.lang.String, int)
     */
    @Override
    public void deleteProductFromCart(String shoppingCartId, int productId) throws Exception {
        Connection conn = null;
        PreparedStatement pStmt = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "DELETE FROM shoppingcart " +
                "WHERE shoppingCartId = ? and productId = ?";

            pStmt = conn.prepareStatement(sql);
            int index = 0;
            DbUtil.setString(pStmt, ++index, shoppingCartId);
            DbUtil.setInt(pStmt, ++index, productId);

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
     * @see com.groovyfly.site.service.shopping.ShoppingServiceIF#createOrder(com.groovyfly.common.structures.ShoppingCart, com.groovyfly.common.structures.Customer, com.groovyfly.common.util.PaymentProcessor, java.lang.String)
     */
    @Override
    public String createOrder(ShoppingCart shoppingCart, Customer customer, PaymentProcessor paymentProcessor) throws Exception, NotEnoughStockException {
        log.info("entering createOrder()");
        Connection conn = null;

        try {
            conn = groovyFlyDS.getConnection();
            conn.setAutoCommit(false);
            
            this.checkForEnoughStock(conn, shoppingCart);
            // if there is not enough stock to fill the order then an exception will be thrown before the rest executes
            String orderNumber = this.createOrderNumber(customer); 
            this.insertOrder(conn, orderNumber, shoppingCart, customer, paymentProcessor);
            this.insertOrderItems(conn, orderNumber, shoppingCart.getShoppingCartEntries());
            this.updateProductStockLevel(conn, shoppingCart.getShoppingCartEntries());
            
            conn.commit();
            
            return orderNumber;
        } catch (NotEnoughStockException e) {
            // roll back the transaction if there is not enough stock to fill the order
            // we have to do this or commit the transaction. no harm in rolling it back
            // even though we haven't modified anything
            conn.rollback();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            DbUtil.closeConnection(conn);
        }
    }
    
    private void checkForEnoughStock(Connection conn, ShoppingCart shoppingCart) throws Exception, NotEnoughStockException {
        Statement stmt = null;
        ResultSet rs = null;

        try {

            Map<Integer, ShoppingCartEntry> productToEntryMap = new HashMap<Integer, ShoppingCartEntry>();
            String inString = this.getInValueForProducts(productToEntryMap, shoppingCart.getShoppingCartEntries());
            
            String sql = 
                "SELECT productId, stockLevel " +
                "FROM product " +
                "WHERE productId IN (" + inString + ")";

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            boolean canFillOrder = true;
            while (rs.next()) {
                ShoppingCartEntry shoppingCartEntry = productToEntryMap.get(rs.getInt(1));
                shoppingCartEntry.setQuantityInStock(rs.getInt(2));
                if (canFillOrder == true && shoppingCartEntry.isQuantityInStockEnough() == false) {
                    canFillOrder = false;
                }
            }
            
            if (canFillOrder == false) {
                throw new NotEnoughStockException("Not enough stock to fill this order");
            }
        } catch (NotEnoughStockException e) {
            log.info("Not enough stock to fill this order");
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(stmt);
            DbUtil.closeResultSet(rs);
        }
    }
    
    public String getInValueForProducts(Map<Integer, ShoppingCartEntry> productToEntryMap, List<ShoppingCartEntry> values) {
        StringBuilder sb = new StringBuilder();
        
        for (ShoppingCartEntry j : values) {
            sb.append(j.getProductId() + ",");   
            productToEntryMap.put(j.getProductId(), j);
        }

        String toReturn = "";
        if (sb.length() > 0) {
            toReturn = sb.substring(0, sb.lastIndexOf(","));
        }
        
        return toReturn;
    }
    
    /*
     * Creates an order number that should be easy to read and can be used as a primary key.
     */
    private String createOrderNumber(Customer customer) {
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String datestring = df.format(new Date()).toString();
        String md5Hex = DigestUtils.md5Hex("groovy" + customer.getEmail());
        int datestringlen = datestring.length();
        String ordernum = datestring+md5Hex.substring(datestringlen, datestringlen + 6).toUpperCase();
        StringBuffer sb = new StringBuffer(ordernum);
        sb.insert(5, "-");
        sb.insert(11, "-");
        sb.insert(17, "-");
        
        return sb.toString();
    }
    
    private void insertOrder(Connection conn, String orderNumber, ShoppingCart shoppingCart, Customer customer, PaymentProcessor paymentProcessor) throws Exception {
        PreparedStatement pStmt = null;

        try {
            String sql = 
                "INSERT INTO `order` (" + // order must be a reserved word or something
                "       orderId, orderStatusCode, " +
                "       shippingName, shippingAddressLine1, shippingAddressLine2, shippingAddressLine3, shippingAddressLine4, " +
                "       shippingCountry, shippingPostcode, shippingAddressStatus, " +
                "       title, forename, surname, email, emailStatus, phoneNumber, " +
                "       paymentprocessor, " +
                "       subTotalAmount, postageAndPackingAmount, discountCode, discountAmount, total, " +
                "       dateCreated) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";

            log.info(sql);

            pStmt = conn.prepareStatement(sql);
            int index = 0;
            DbUtil.setString(pStmt, ++index, orderNumber);
            DbUtil.setString(pStmt, ++index, "01"); //TODO change this to use enum
            ShippingAddress shippingAddress = customer.getShippingAddress();
            DbUtil.setString(pStmt, ++index, shippingAddress.getName());
            DbUtil.setString(pStmt, ++index, shippingAddress.getLine1());
            DbUtil.setString(pStmt, ++index, shippingAddress.getLine2());
            DbUtil.setString(pStmt, ++index, shippingAddress.getLine3());
            DbUtil.setString(pStmt, ++index, shippingAddress.getLine4());
            DbUtil.setString(pStmt, ++index, shippingAddress.getCountry());
            DbUtil.setString(pStmt, ++index, shippingAddress.getPostcode());
            DbUtil.setString(pStmt, ++index, shippingAddress.getAddressStatus());
            DbUtil.setString(pStmt, ++index, customer.getTitle());
            DbUtil.setString(pStmt, ++index, customer.getForename());
            DbUtil.setString(pStmt, ++index, customer.getSurname());
            DbUtil.setString(pStmt, ++index, customer.getEmail());
            DbUtil.setString(pStmt, ++index, customer.getEmailStatus());
            DbUtil.setString(pStmt, ++index, customer.getPhoneNumber());
            DbUtil.setString(pStmt, ++index, paymentProcessor.toString());
            DbUtil.setBigDecimal(pStmt, ++index, shoppingCart.getSubTotal());
            DbUtil.setBigDecimal(pStmt, ++index, shoppingCart.getPostageAndPacking());
            if (shoppingCart.getDiscount() != null) {
                DbUtil.setString(pStmt, ++index, shoppingCart.getDiscount().getDiscountCode());
                DbUtil.setBigDecimal(pStmt, ++index, shoppingCart.getDiscount().getValue());    
            } else {
                pStmt.setNull(++index, Types.NULL);
                pStmt.setNull(++index, Types.NULL);
            }
            
            DbUtil.setBigDecimal(pStmt, ++index, shoppingCart.getTotal());
            
            pStmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(pStmt);
        }
    }
    
    private void insertOrderItems(Connection conn, String orderNumber, List<ShoppingCartEntry> items) throws Exception {
        PreparedStatement pStmt = null;

        try {
            String sql = 
                "INSERT INTO orderitems " +
                "       (orderId, productId, name, sku, quantity, unitPrice, totalPrice, currencyCode) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            log.info(sql);

            pStmt = conn.prepareStatement(sql);
            int index = 0;

            for (ShoppingCartEntry i : items) {
                DbUtil.setString(pStmt, ++index, orderNumber);
                DbUtil.setInt(pStmt, ++index, i.getProductId());
                DbUtil.setString(pStmt, ++index, i.getName());
                DbUtil.setString(pStmt, ++index, i.getSku());
                DbUtil.setInt(pStmt, ++index, i.getQuantity());
                DbUtil.setBigDecimal(pStmt, ++index, i.getUnitPrice());
                DbUtil.setBigDecimal(pStmt, ++index, i.getTotalPrice());
                
                Currency currency = Currency.getInstance(i.getLocale());
                DbUtil.setString(pStmt, ++index, currency.getCurrencyCode());
                
                pStmt.addBatch();
                index = 0;
            }
            
            pStmt.executeBatch();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(pStmt);
        }
    }
    
    private void updateProductStockLevel(Connection conn, List<ShoppingCartEntry> items) throws Exception {
        PreparedStatement pStmt = null;

        try {
            String sql = 
                "UPDATE product " +
                "SET stockLevel = stockLevel - ? " +
                "WHERE productId = ?";

            log.info(sql);

            pStmt = conn.prepareStatement(sql);
            int index = 0;

            for (ShoppingCartEntry i : items) {
                DbUtil.setInt(pStmt, ++index, i.getQuantity());
                DbUtil.setInt(pStmt, ++index, i.getProductId());
                
                pStmt.addBatch();
                index = 0;
            }
            
            pStmt.executeBatch();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(pStmt);
        }
    }

    /* 
     * @see com.groovyfly.site.service.shopping.ShoppingServiceIF#confirmOrder(java.lang.String, java.util.Date, java.lang.String, java.lang.String)
     */
    @Override
    public void confirmOrder(String orderNumber, String paypalPayerId, String transactionId, Date paymentDate, String paymentStatus, String pendingReason) throws Exception {
        Connection conn = null;
        PreparedStatement pStmt = null;

        try {
            conn = groovyFlyDS.getConnection();
            
            String sql = 
                "UPDATE `order` " +
                "SET paypalPayerId = ?, paypalTransationId = ?, paypalPaymentDate = ?, paypalPaymentStatus = ?, payaplPendingReason = ? " +
                "WHERE orderId = ?";

            log.info(sql);

            pStmt = conn.prepareStatement(sql);
            int index = 0;
            
            DbUtil.setString(pStmt, ++index, paypalPayerId);
            DbUtil.setString(pStmt, ++index, transactionId);
            DbUtil.setDate(pStmt, ++index, paymentDate);
            DbUtil.setString(pStmt, ++index, paymentStatus);
            DbUtil.setString(pStmt, ++index, pendingReason);
            DbUtil.setString(pStmt, ++index, orderNumber);
            
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
