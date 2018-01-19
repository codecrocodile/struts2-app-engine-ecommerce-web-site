/*
 * ProductServiceImpl.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.service.productmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.groovyfly.common.structures.GroupingProduct;
import com.groovyfly.common.structures.ProductImages;
import com.groovyfly.common.structures.Lookup;
import com.groovyfly.common.structures.Page;
import com.groovyfly.common.structures.Product;
import com.groovyfly.common.structures.ProductAttribute;
import com.groovyfly.common.structures.ProductAttributeValue;
import com.groovyfly.common.structures.ProductSearchQuery;
import com.groovyfly.common.util.DbUtil;
import com.groovyfly.common.util.GroovyFlyDS;
import com.groovyfly.common.util.ImageServiceGAEWorker;

/**
 * @author Chris Hatton
 * 
 *         Created 28 Jul 2012
 */
public class ProductServiceImpl implements ProductServiceIF {

    private static Logger log = Logger.getLogger(ProductServiceImpl.class.getName());

    private GroovyFlyDS groovyFlyDS;

    public void setGroovyFlyDS(GroovyFlyDS groovyFlyDS) {
        this.groovyFlyDS = groovyFlyDS;
    }

    /*
     * @see com.groovyfly.admin.service.ProductServiceIF#getProductStatuses()
     */
    @Override
    public List<Lookup> getProductStatuses(boolean includeRetired) throws Exception {

        List<Lookup> productStatuses = new ArrayList<Lookup>();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            StringBuilder sql = new StringBuilder();
            sql.append(
                "SELECT id, description, sortIndex, retired " +
                "FROM productstatus_lu "
            );

            if (includeRetired == false) {
                sql.append(" ");
                sql.append("WHERE retired = 0 ");
            }
            
            sql.append("ORDER BY sortIndex");

            log.fine(sql.toString());

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql.toString());

            Lookup l = null;
            while (rs.next()) {
                l = new Lookup();
                l.setId(rs.getInt("id"));
                l.setDescription(rs.getString("description"));
                
                productStatuses.add(l);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(stmt);
            DbUtil.closeResultSet(rs);
        }

        return productStatuses;
    }

    /* 
     * @see com.groovyfly.admin.service.ProductServiceIF#getProductAttributes()
     */
    @Override
    public List<ProductAttribute> getProductAttributes() throws Exception {
        return getProductAttributesForIds(null);
    }
    
    /*
     * @see com.groovyfly.admin.service.productmanagement.ProductServiceIF#getProductAttributes(java.util.List)
     */
    public List<ProductAttribute> getProductAttributes(List<Integer> productAttrubuteIds) throws Exception {
        return getProductAttributesForIds(productAttrubuteIds);
    }
    
    private List<ProductAttribute> getProductAttributesForIds(List<Integer> productAttributeIds) throws Exception {
        
        StringBuilder whereCondition = new StringBuilder();
        if (productAttributeIds != null) {
            whereCondition.append("WHERE pa.productAttributeId IN (");
            whereCondition.append(DbUtil.getInValue(productAttributeIds));
            whereCondition.append(")");
        } else {
            whereCondition.append("WHERE 1 = 1");
        }
        

        List<ProductAttribute> productAttributes = new ArrayList<ProductAttribute>();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT pa.productAttributeId, pa.name AS AttribName, pv.productAttributeValueId, pv.name AS AttribValueName, pa2pv.defaultChoosenValue " +
                "FROM productattribute pa " +
                "JOIN productattribute2productattributevalue pa2pv on pa.productAttributeId = pa2pv.productAttributeId " +
                "JOIN productattributevalue pv on pa2pv.productAttributeValueId = pv.productAttributeValueId " +
                whereCondition + " " +
                "ORDER BY pa.sortIndex, pv.sortIndex ";
            
            log.info(sql);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            Map<ProductAttribute, List<ProductAttributeValue>> map = new LinkedHashMap<ProductAttribute, List<ProductAttributeValue>>();
            
            while (rs.next()) {
                ProductAttribute pa = new ProductAttribute();
                pa.setId(rs.getInt("productAttributeId"));
                pa.setDescription(rs.getString("AttribName"));
                
                ProductAttributeValue pav = new ProductAttributeValue();
                pav.setId(rs.getInt("productAttributeValueId"));
                pav.setDescription(rs.getString("AttribValueName"));
                pav.setDefaultChoosen(rs.getBoolean("defaultChoosenValue"));
                
                if (map.containsKey(pa)) {
                    map.get(pa).add(pav);
                } else {
                    ArrayList<ProductAttributeValue> pavList = new ArrayList<ProductAttributeValue>();
                    pavList.add(pav);
                    map.put(pa, pavList);
                }
            }
            
            for (Entry<ProductAttribute, List<ProductAttributeValue>> e : map.entrySet()) {
                e.getKey().setProductAttributeValues(e.getValue());
                productAttributes.add(e.getKey());
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(stmt);
            DbUtil.closeResultSet(rs);
        }

        return productAttributes;
    }

    /*
     * @see com.groovyfly.admin.service.ProductServiceIF#getProductSummaries(boolean)
     */
    public List<Product> getProductSummaries(boolean includeRetired) throws Exception {

        List<Product> productSummaries = new ArrayList<Product>();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            StringBuilder sql = new StringBuilder();
            sql.append(
                    "SELECT p.productId, p.name, p.smallerImageUrl, p.imageAltTagDesc, p.price, p.storageLocation, p.stockLevel, " +
                    "   p.productStatusId " +
                    "FROM product p " +
                    "JOIN product2category p2c ON p2c.productId = p.productId " +
                    "JOIN category c ON p2c.categoryId = c.categoryId " +
                    "WHERE p.isGroupingProduct = 0 "
            );

            if (includeRetired == false) {
                sql.append(" ");
                sql.append("and p.retired = 0 ");
            }
            
            sql.append("ORDER BY p.name");
            
            log.fine(sql.toString());

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql.toString());

            Product p = null;
            while (rs.next()) {
                p = new Product();
                p.setProductId(rs.getInt("productId"));
                p.setName(rs.getString("name"));
                
                ProductImages i = new ProductImages();
                String serImg = rs.getString("smallerImageUrl");
                if (serImg != null && serImg.endsWith("=s58")) {
                    i.setSmallerImageUrl(serImg.substring(0, serImg.length() - 4));    
                } else {
                    i.setSmallerImageUrl(serImg);
                }
                i.setAltTagDescription(rs.getString("imageAltTagDesc"));
                p.setImages(i);
                
                p.setPrice(rs.getBigDecimal("price"));
                p.setStoreageLocation(rs.getString("storageLocation"));
                p.setStockLevel(rs.getInt("stockLevel"));
                
                productSummaries.add(p);
            }  
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(stmt);
            DbUtil.closeResultSet(rs);
        }

        return productSummaries;
    }
    
    /* 
     * @see com.groovyfly.admin.service.productmanagement.ProductServiceIF
     *  #getProductSummaries(com.groovyfly.common.structures.ProductSearchQuery, boolean)
     */
    @Override
    public List<Product> getProductSummaries(ProductSearchQuery productSearchQuery, boolean includeRetired) throws Exception {

        List<Product> productSummaries = new ArrayList<Product>();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            StringBuilder sql = new StringBuilder();
            sql.append(
                    "SELECT p.productId, " +
                    "p.attributeId1, pa1.name AS attrib1Name, p.attributeValueId1, pv1.name AS attribValue1Name, " +
                    "p.attributeId2, pa2.name AS attrib2Name, p.attributeValueId2, pv2.name AS attribValue2Name, " +
                    "p.attributeId3, pa3.name AS attrib3Name, p.attributeValueId3, pv3.name AS attribValue3Name, " +
                    "p.name, p.smallImageUrl, p.imageAltTagDesc, p.price, p.storageLocation, p.stockLevel, p.productStatusId " +
                    "FROM product p " +
                    "LEFT OUTER JOIN productattribute pa1 ON p.attributeId1 = pa1.productAttributeId " +
                    "LEFT OUTER JOIN productattributevalue pv1 ON p.attributeValueId1 = pv1.productAttributeValueId " +
                    "LEFT OUTER JOIN productattribute pa2 ON p.attributeId2 = pa2.productAttributeId " +
                    "LEFT OUTER JOIN productattributevalue pv2 ON p.attributeValueId2 = pv2.productAttributeValueId " +
                    "LEFT OUTER JOIN productattribute pa3 ON p.attributeId3 = pa3.productAttributeId " +
                    "LEFT OUTER JOIN productattributevalue pv3 ON p.attributeValueId3 = pv3.productAttributeValueId " +
                    "JOIN product2category p2c ON p2c.productId = p.productId " +
                    "JOIN category c ON p2c.categoryId = c.categoryId " +
                    "WHERE p.isGroupingProduct = 0 "
            );

            if (includeRetired == false) {
                sql.append(" ");
                sql.append("AND p.retired = 0 ");
            }
            
            if (productSearchQuery.getName() != null && !productSearchQuery.getName().trim().equals("")) {
                sql.append(" ");
                sql.append("AND p.name like '" + DbUtil.addSlashes(productSearchQuery.getName()) + "%' ");
            }
            
            if (productSearchQuery.getCategoryId() > 0) {
                sql.append(" ");
                sql.append("AND c.categoryId = " + productSearchQuery.getCategoryId() + " ");
            }
            
            if (productSearchQuery.getAttributeId1() != 0 || productSearchQuery.getAttributeId2() != 0 || productSearchQuery.getAttributeId3() != 0) {
                sql.append(" ");
                String inString = DbUtil.getInValue(productSearchQuery.getAttributeId1(), productSearchQuery.getAttributeId2(), productSearchQuery.getAttributeId3());
                sql.append("AND (p.attributeId1 IN (" + inString + ") OR p.attributeId2 IN (" + inString + ") OR p.attributeId3 in (" + inString + "))");
            }
            
            if (productSearchQuery.getProductIdExcludeString() != null && !productSearchQuery.getProductIdExcludeString().trim().equals("")) {
                sql.append(" ");
                sql.append("AND p.productId NOT IN (" + DbUtil.addSlashes(productSearchQuery.getProductIdExcludeString()) + ")");
            }
            
            sql.append(" ");
            sql.append("ORDER BY p.name");
            
            log.fine(sql.toString());

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql.toString());

            Product p = null;
            while (rs.next()) {
                p = new Product();
                p.setProductId(rs.getInt("productId"));
                p.setName(rs.getString("name"));

                if (rs.getInt("attributeId1") != 0) {
                    ProductAttribute pa = new ProductAttribute();
                    pa.setId(rs.getInt("attributeId1"));
                    pa.setDescription(rs.getString("attrib1Name"));
                    ProductAttributeValue pav = new ProductAttributeValue();
                    pav.setId(rs.getInt("attributeValueId1"));
                    pav.setDescription(rs.getString("attribValue1Name"));
                    pa.setChoosenAttributeValue(pav);
                    p.setAttribute1(pa);
                }
                
                if (rs.getInt("attributeId2") != 0) {
                    ProductAttribute pa = new ProductAttribute();
                    pa.setId(rs.getInt("attributeId2"));
                    pa.setDescription(rs.getString("attrib2Name"));
                    ProductAttributeValue pav = new ProductAttributeValue();
                    pav.setId(rs.getInt("attributeValueId2"));
                    pav.setDescription(rs.getString("attribValue2Name"));
                    pa.setChoosenAttributeValue(pav);
                    p.setAttribute2(pa);
                }
                
                if (rs.getInt("attributeId3") != 0) {
                    ProductAttribute pa = new ProductAttribute();
                    pa.setId(rs.getInt("attributeId3"));
                    pa.setDescription(rs.getString("attrib3Name"));
                    ProductAttributeValue pav = new ProductAttributeValue();
                    pav.setId(rs.getInt("attributeValueId3"));
                    pav.setDescription(rs.getString("attribValue3Name"));
                    pa.setChoosenAttributeValue(pav);
                    p.setAttribute3(pa);
                }
                
                ProductImages i = new ProductImages();
                i.setSmallImageUrl(rs.getString("smallImageUrl"));
                i.setAltTagDescription(rs.getString("imageAltTagDesc"));
                p.setImages(i);
                
                p.setPrice(rs.getBigDecimal("price"));
                p.setStoreageLocation(rs.getString("storageLocation"));
                p.setStockLevel(rs.getInt("stockLevel"));
                
                productSummaries.add(p);
            }  
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(stmt);
            DbUtil.closeResultSet(rs);
        }

        return productSummaries;
    }
    
    public List<Product> getProductSummaries(int categoryId, boolean groupingProductsOnly, int limit, int offset) throws Exception {
        List<Product> productSummaries = new ArrayList<Product>();
        
        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT p.productId, p2c.categoryId, p.urlAlias, p.name, p.smallImageUrl, p.largeImageUrl, p.price " +
                "FROM product p " +
                "JOIN product2category p2c ON p2c.productId = p.productId " +
                "WHERE isGroupingProduct = ? AND categoryId = ? AND p.retired != 1 " +
                "ORDER BY name " +
                "LIMIT ?, ?"; // first arg is the row offset, second arg is the row limit

            log.info(sql);

            pStmt = conn.prepareStatement(sql);
            int index = 0;
            int isGroupingProduct = groupingProductsOnly ? 1 : 0;
            DbUtil.setInt(pStmt, ++index, isGroupingProduct);
            DbUtil.setInt(pStmt, ++index, categoryId);
            pStmt.setInt(++index, offset);
            pStmt.setInt(++index, limit);
            
            rs = pStmt.executeQuery();
            Product p = null;
            while (rs.next()) {
                p = new Product();
                p.setProductId(rs.getInt(1));
                p.setCategoryId(rs.getInt(2));
                p.setUrlAlias(rs.getString(3));
                p.setName(rs.getString(4));
                
                ProductImages pi = new ProductImages();
                pi.setSmallImageUrl(rs.getString(5));
                pi.setLargeImageUrl(rs.getString(6));
                p.setImages(pi);
                
                p.setPrice(rs.getBigDecimal(7));
                
                productSummaries.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(pStmt);
            DbUtil.closeResultSet(rs);
        }
        
        return productSummaries;
    }
    
    /* 
     * @see com.groovyfly.admin.service.productmanagement.ProductServiceIF#getProductSummaries(java.util.List)
     */
    @Override
    public List<Product> getProductSummaries(List<Integer> productIds) throws Exception {
        List<Product> productSummaries = new ArrayList<Product>();
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT p.productId, p2c.categoryId, p.urlAlias, p.name, p.smallImageUrl, p.largeImageUrl, p.price " +
                "FROM product p " +
                "JOIN product2category p2c ON p2c.productId = p.productId " +
                "WHERE p.productId IN (" + DbUtil.getInValue(productIds) + ") " +
                "ORDER BY name";

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            Product p = null;
            while (rs.next()) {
                p = new Product();
                p.setProductId(rs.getInt(1));
                p.setCategoryId(rs.getInt(2));
                p.setUrlAlias(rs.getString(3));
                p.setName(rs.getString(4));
                
                ProductImages pi = new ProductImages();
                pi.setSmallImageUrl(rs.getString(5));
                pi.setLargeImageUrl(rs.getString(6));
                p.setImages(pi);
                p.setPrice(rs.getBigDecimal(7));
                
                productSummaries.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(stmt);
            DbUtil.closeConnection(conn);
        }
        
        log.info("return " + productSummaries.size());
        return productSummaries;
    }
    
    /* 
     * @see com.groovyfly.admin.service.ProductServiceIF#getProduct(int)
     */
    @Override
    public Product getProduct(int productId) throws Exception { // TODO combine with the bellow

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT p.productId, p.urlAlias, p.isGroupingProduct, p.groupingConfigId, " +
                "       p.attributeId1, p.attributeId2, p.attributeId3, p.attributeValueId1, p.attributeValueId2, p.attributeValueId3, " +
                "       p.name, p.description,  p.smallerImageUrl, p.mediumImageUrl, p.smallImageUrl, p.largeImageUrl, " +
                "       p.imageFileName, p.imageMimeType, p.imageAltTagDesc, p.supplierId, p.sku, p.price, " +
                "       p.priceRuleId, p.vatRateId, p.storageLocation, p.stockLevel, p.productStatusId, p.pageId, p.retired, " +
                "       p2c.categoryId, " +
                "       pg.pageId, pg.title, pg.keywords, pg.description AS pageDesc, pg.html, " +
                "(SELECT count(1) " +
                " FROM groupingproduct2product " +
                " WHERE productId = " + productId + ") AS inNoOfGroupings " +
                "FROM product p " +
                "JOIN product2category p2c on p2c.productId = p.productId " +
                "JOIN page pg ON p.pageId = pg.pageId " +
                "WHERE p.productId = " + productId;
            
            log.fine(sql);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            Product p = null;
            while (rs.next()) {
                if (rs.getBoolean("isGroupingProduct")) {
                    p = new GroupingProduct();
                } else {
                    p = new Product();    
                }
                p.setUrlAlias(rs.getString("urlAlias"));
                p.setCategoryId(rs.getInt("categoryId"));
                p.setProductGroupingConfigId(rs.getInt("groupingConfigId"));
                p.setAttributeId1(rs.getInt("attributeId1"));
                p.setAttributeId2(rs.getInt("attributeId2"));
                p.setAttributeId3(rs.getInt("attributeId3"));
                p.setAttributeValueId1(rs.getInt("attributeValueId1"));
                p.setAttributeValueId2(rs.getInt("attributeValueId2"));
                p.setAttributeValueId3(rs.getInt("attributeValueId3"));
                p.setProductId(rs.getInt("productId"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                
                ProductImages i = new ProductImages();
                i.setSmallerImageUrl(rs.getString("smallerImageUrl"));
                i.setSmallImageUrl(rs.getString("smallImageUrl"));
                i.setMediumImageUrl(rs.getString("mediumImageUrl"));
                i.setLargeImageUrl(rs.getString("largeImageUrl"));
                i.setLargeImageFileName(rs.getString("imageFileName"));
                i.setLargeImageMimeType(rs.getString("imageMimeType"));
                i.setAltTagDescription(rs.getString("imageAltTagDesc"));
                p.setImages(i);
                
                p.setSupplierId(rs.getInt("supplierId"));
                p.setSku(rs.getString("sku"));
                
                p.setPrice(rs.getBigDecimal("price"));
                p.setPriceRuleId(rs.getInt("priceRuleId"));
                p.setVatRuleId(rs.getInt("vatRateId"));
                p.setStoreageLocation(rs.getString("storageLocation"));
                p.setStockLevel(rs.getInt("stockLevel"));
                p.setStatusId(rs.getInt("productStatusId"));
                p.setRetired(rs.getBoolean("retired"));
                
                Page page = new Page();
                page.setPageId(rs.getInt("pageId"));
                page.setTitle(rs.getString("title"));
                page.setMetaKeywords(rs.getString("keywords"));
                page.setMetaDescription(rs.getString("pageDesc"));
                page.setHtml(rs.getString("html"));
                p.setPage(page);
                p.setInNoOfGroupings(rs.getInt("inNoOfGroupings"));
                
                if (rs.getBoolean("isGroupingProduct")) {
                    setGroupingProductAddtionalValues((GroupingProduct) p);
                }
                
            }  
            
            return p;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(stmt);
            DbUtil.closeResultSet(rs);
        }
    }
    
    /* 
     * @see com.groovyfly.admin.service.productmanagement.ProductServiceIF#getProduct(java.lang.String)
     */
    @Override
    public Product getProduct(String urlAlias) throws Exception { // TODO combine with the above

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT p.productId, p.urlAlias, p.isGroupingProduct, p.groupingConfigId, " +
                "       p.attributeId1, p.attributeId2, p.attributeId3, p.attributeValueId1, p.attributeValueId2, p.attributeValueId3, " +
                "       p.name, p.description,  p.smallerImageUrl, p.smallImageUrl, p.mediumImageUrl, p.largeImageUrl, " + 
                "       p.imageFileName, p.imageMimeType, p.imageAltTagDesc, p.supplierId, p.sku, p.price, " +
                "       p.priceRuleId, p.vatRateId, p.storageLocation, p.stockLevel, p.productStatusId, averageStarRating, p.pageId, p.retired, " +
                "       p2c.categoryId, " +
                "       pg.pageId, pg.title, pg.keywords, pg.description AS pageDesc, pg.html " +
                "FROM product p " +
                "JOIN product2category p2c on p2c.productId = p.productId " +
                "JOIN page pg ON p.pageId = pg.pageId " +
                "WHERE p.urlAlias = " + DbUtil.quoteStringOrNull(urlAlias);
            
            log.info(sql);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            Product p = null;
            while (rs.next()) {
                if (rs.getBoolean("isGroupingProduct")) {
                    p = new GroupingProduct();
                    
                    log.info("it is a grouping product");
                    
                } else {
                    p = new Product();
                    
                    log.info("it is a single product");
                }
                p.setUrlAlias(rs.getString("urlAlias"));
                p.setCategoryId(rs.getInt("categoryId"));
                p.setProductGroupingConfigId(rs.getInt("groupingConfigId"));
                p.setAttributeId1(rs.getInt("attributeId1"));
                p.setAttributeId2(rs.getInt("attributeId2"));
                p.setAttributeId3(rs.getInt("attributeId3"));
                p.setAttributeValueId1(rs.getInt("attributeValueId1"));
                p.setAttributeValueId2(rs.getInt("attributeValueId2"));
                p.setAttributeValueId3(rs.getInt("attributeValueId3"));
                p.setProductId(rs.getInt("productId"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                
                ProductImages i = new ProductImages();
                i.setSmallerImageUrl(rs.getString("smallerImageUrl"));
                i.setSmallImageUrl(rs.getString("smallImageUrl"));
                i.setMediumImageUrl(rs.getString("mediumImageUrl"));
                i.setLargeImageUrl(rs.getString("largeImageUrl"));
                i.setLargeImageFileName(rs.getString("imageFileName"));
                i.setLargeImageMimeType(rs.getString("imageMimeType"));
                i.setAltTagDescription(rs.getString("imageAltTagDesc"));
                p.setImages(i);
                
                p.setSupplierId(rs.getInt("supplierId"));
                p.setSku(rs.getString("sku"));
                
                p.setPrice(rs.getBigDecimal("price"));
                p.setPriceRuleId(rs.getInt("priceRuleId"));
                p.setVatRuleId(rs.getInt("vatRateId"));
                p.setStoreageLocation(rs.getString("storageLocation"));
                p.setStockLevel(rs.getInt("stockLevel"));
                p.setStatusId(rs.getInt("productStatusId"));
                p.setAverageStarRating(rs.getByte("averageStarRating"));
                p.setRetired(rs.getBoolean("retired"));
                
                Page page = new Page();
                page.setPageId(rs.getInt("pageId"));
                page.setTitle(rs.getString("title"));
                page.setMetaKeywords(rs.getString("keywords"));
                page.setMetaDescription(rs.getString("pageDesc"));
                page.setHtml(rs.getString("html"));
                p.setPage(page);
                
                if (rs.getBoolean("isGroupingProduct")) {
                    setGroupingProductAddtionalValues((GroupingProduct) p);
                    ((GroupingProduct) p).setInNoOfGroupings(((GroupingProduct) p).getProducts().size());
                } else {
                    p.setInNoOfGroupings(0);
                }
            }  
            
            return p;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(stmt);
            DbUtil.closeResultSet(rs);
        }
    }
    
    private void setGroupingProductAddtionalValues(GroupingProduct groupingProduct) throws Exception {
        
        List<Product> productSummaries = new ArrayList<Product>();
        groupingProduct.setProducts(productSummaries);
        
        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT p.productId, " +
                "p.attributeId1, pa1.name AS attrib1Name, p.attributeValueId1, pv1.name AS attribValue1Name, " +
                "p.attributeId2, pa2.name AS attrib2Name, p.attributeValueId2, pv2.name AS attribValue2Name, " +
                "p.attributeId3, pa3.name AS attrib3Name, p.attributeValueId3, pv3.name AS attribValue3Name, " +
                "p.name, p.smallImageUrl, p.imageAltTagDesc, p.price, p.storageLocation, p.stockLevel, " +
                "p.productStatusId " +
                "FROM product p " +
                "LEFT OUTER JOIN productattribute pa1 ON p.attributeId1 = pa1.productAttributeId " +
                "LEFT OUTER JOIN productattributevalue pv1 ON p.attributeValueId1 = pv1.productAttributeValueId " +
                "LEFT OUTER JOIN productattribute pa2 ON p.attributeId2 = pa2.productAttributeId " +
                "LEFT OUTER JOIN productattributevalue pv2 ON p.attributeValueId2 = pv2.productAttributeValueId " +
                "LEFT OUTER JOIN productattribute pa3 ON p.attributeId3 = pa3.productAttributeId " +
                "LEFT OUTER JOIN productattributevalue pv3 ON p.attributeValueId3 = pv3.productAttributeValueId " +
                "JOIN product2category p2c ON p2c.productId = p.productId " +
                "JOIN category c ON p2c.categoryId = c.categoryId " +
                "WHERE p.productId IN ( " +
                "       SELECT productId " +
                "       FROM groupingproduct2product gp2p " +
                "       WHERE gp2p.groupingProductId = ? " +
                ")";

            log.fine(sql);

            pStmt = conn.prepareStatement(sql);
            int index = 0;
            DbUtil.setInt(pStmt, ++index, groupingProduct.getProductId());
            
            rs = pStmt.executeQuery();

            Product p = null;
            while (rs.next()) {
                p = new Product();
                p.setProductId(rs.getInt("productId"));
                p.setName(rs.getString("name"));
                
                if (rs.getInt("attributeId1") != 0) {
                    ProductAttribute pa = new ProductAttribute();
                    pa.setId(rs.getInt("attributeId1"));
                    pa.setDescription(rs.getString("attrib1Name"));
                    ProductAttributeValue pav = new ProductAttributeValue();
                    pav.setId(rs.getInt("attributeValueId1"));
                    pav.setDescription(rs.getString("attribValue1Name"));
                    pa.setChoosenAttributeValue(pav);
                    p.setAttribute1(pa);
                }
                
                if (rs.getInt("attributeId2") != 0) {
                    ProductAttribute pa = new ProductAttribute();
                    pa.setId(rs.getInt("attributeId2"));
                    pa.setDescription(rs.getString("attrib2Name"));
                    ProductAttributeValue pav = new ProductAttributeValue();
                    pav.setId(rs.getInt("attributeValueId2"));
                    pav.setDescription(rs.getString("attribValue2Name"));
                    pa.setChoosenAttributeValue(pav);
                    p.setAttribute2(pa);
                }
                
                if (rs.getInt("attributeId3") != 0) {
                    ProductAttribute pa = new ProductAttribute();
                    pa.setId(rs.getInt("attributeId3"));
                    pa.setDescription(rs.getString("attrib3Name"));
                    ProductAttributeValue pav = new ProductAttributeValue();
                    pav.setId(rs.getInt("attributeValueId3"));
                    pav.setDescription(rs.getString("attribValue3Name"));
                    pa.setChoosenAttributeValue(pav);
                    p.setAttribute3(pa);
                }
                
                ProductImages i = new ProductImages();
                i.setSmallImageUrl(rs.getString("smallImageUrl"));
                i.setAltTagDescription(rs.getString("imageAltTagDesc"));
                p.setImages(i);
                
                p.setPrice(rs.getBigDecimal("price"));
                p.setStoreageLocation(rs.getString("storageLocation"));
                p.setStockLevel(rs.getInt("stockLevel"));
                
                productSummaries.add(p);
            } 

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(pStmt);
            DbUtil.closeResultSet(rs);
        }
    }
    
    /* 
     * TRANSACTION PROBLEM: There is a problem here that when updating if something goes wrong than any images that where to be updated 
     * might be lost and everything else will be rolled back.
     * 
     * @see com.groovyfly.admin.service.ProductServiceIF#saveProduct(com.groovyfly.common.structures.Product)
     */
    @Override
    public Product saveProduct(Product product) throws Exception {
        
        log.info("saveProduct(Product product) throws Exception");
        
        ImageServiceGAEWorker imageServiceWorker = new ImageServiceGAEWorker();
        
        Product savedProduct = null;
        
        Connection conn = null;

        try {
            conn = groovyFlyDS.getConnection();
            conn.setAutoCommit(false);

            if (product instanceof GroupingProduct) {
                savedProduct = this.saveGroupingProduct(conn, (GroupingProduct) product);
            } else {
                if (product.getProductId() > 0) {
                    
                    if (product.getImages().getLargeImageBytes() != null) {
                        List<String> largeImageBlobsToDelete = this.getLargeImageBlobsToDelete(conn, product);
                        for (String blobStringToDelete : largeImageBlobsToDelete) {
                            imageServiceWorker.deleteBlob(blobStringToDelete);
                        }
                        imageServiceWorker.storeLargeImage(product.getImages());
                    }
                    
                    if (product.getImages().getMediumImageBytes() != null) {
                        List<String> mediumImageBlobsToDelete = this.getMediumImageBlobsToDelete(conn, product);
                        for (String blobStringToDelete : mediumImageBlobsToDelete) {
                            imageServiceWorker.deleteBlob(blobStringToDelete);
                        }
                        imageServiceWorker.storeMediumImage(product.getImages());
                    }
                    
                    if (product.getImages().getSmallImageBytes() != null) {
                        List<String> smallImageBlobsToDelete = this.getSmallImageBlobsToDelete(conn, product);
                        for (String blobStringToDelete : smallImageBlobsToDelete) {
                            imageServiceWorker.deleteBlob(blobStringToDelete);
                        }
                        imageServiceWorker.storeSmallmage(product.getImages());
                    }
                    
                    if (product.getImages().getSmallerImageBytes() != null) {
                        List<String> smallerImageBlobsToDelete = this.getSmallerImageBlobsToDelete(conn, product);
                        for (String blobStringToDelete : smallerImageBlobsToDelete) {
                            imageServiceWorker.deleteBlob(blobStringToDelete);
                        }
                        
                        imageServiceWorker.storeSmallerImage(product.getImages());
                    } 
                    
                    log.info("update product");
                    savedProduct = this.updateProduct(conn, product);
                } else {
                    
                    log.info("insert propduct");
                    imageServiceWorker.storeImages(product.getImages());
                    log.info("insert propduct 2");
                    savedProduct = this.insertProduct(conn, product);
                    log.info("insert propduct 3");
                }
            }
            
            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            DbUtil.closeConnection(conn);
        }

        return savedProduct;
    }
    
    private Product insertProduct(Connection conn, Product product) throws Exception {
        log.info("insert product");
        
        this.savePage(conn, product.getPage());
        
        log.info("insert product 2");

        PreparedStatement pStmt = null;
        ResultSet rs = null;

        try {
            String sql = 
                "INSERT INTO product (" +
                "       urlAlias, isGroupingProduct, groupingConfigId, " +
                "       attributeId1, attributeId2, attributeId3, attributeValueId1, attributeValueId2, attributeValueId3, name, description, " +
                "       smallerImageUrl, smallerImageBlobKey, smallImageUrl, smallImageBlobKey, mediumImageUrl, mediumImageBlobKey, " +
                "       largeImageUrl, largeImageBlobKey, imageFileName, " +
                "       imageMimeType, imageAltTagDesc, supplierId, sku, price, priceRuleId, vatRateId, storageLocation, " +
                "       stockLevel, productStatusId, pageId, retired) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            log.fine(sql);

            pStmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            int index = 0;
            DbUtil.setString(pStmt, ++index, product.getUrlAlias());
            DbUtil.setInt(pStmt, ++index, product instanceof GroupingProduct);
            DbUtil.setInt(pStmt, ++index, product.getProductGroupingConfigId());
            DbUtil.setInt(pStmt, ++index, product.getAttributeId1());
            DbUtil.setInt(pStmt, ++index, product.getAttributeId2());
            DbUtil.setInt(pStmt, ++index, product.getAttributeId3());
            DbUtil.setInt(pStmt, ++index, product.getAttributeValueId1());
            DbUtil.setInt(pStmt, ++index, product.getAttributeValueId2());
            DbUtil.setInt(pStmt, ++index, product.getAttributeValueId3());
            DbUtil.setString(pStmt, ++index, product.getName());
            DbUtil.setString(pStmt, ++index, product.getDescription());
            DbUtil.setString(pStmt, ++index, product.getImages().getSmallerImageUrl());
            DbUtil.setString(pStmt, ++index, product.getImages().getSmallerImageBlobKeyString());
            DbUtil.setString(pStmt, ++index, product.getImages().getSmallImageUrl());
            DbUtil.setString(pStmt, ++index, product.getImages().getSmallImageBlobKeyString());
            DbUtil.setString(pStmt, ++index, product.getImages().getMediumImageUrl());
            DbUtil.setString(pStmt, ++index, product.getImages().getMediumImageBlobKeyString());
            DbUtil.setString(pStmt, ++index, product.getImages().getLargeImageUrl());
            DbUtil.setString(pStmt, ++index, product.getImages().getLargeImageBlobKeyString());
            DbUtil.setString(pStmt, ++index, product.getImages().getLargeImageFileName());
            DbUtil.setString(pStmt, ++index, product.getImages().getLargeImageMimeType());
            DbUtil.setString(pStmt, ++index, product.getImages().getAltTagDescription());
            DbUtil.setInt(pStmt, ++index, product.getSupplierId());
            DbUtil.setString(pStmt, ++index, product.getSku());
            pStmt.setDouble(++index, product.getPrice().doubleValue());
            DbUtil.setInt(pStmt, ++index, product.getPriceRuleId());
            DbUtil.setInt(pStmt, ++index, product.getVatRuleId());
            DbUtil.setString(pStmt, ++index, product.getStoreageLocation());
            DbUtil.setInt(pStmt, ++index, product.getStockLevel());
            DbUtil.setInt(pStmt, ++index, product.getStatusId());
            DbUtil.setInt(pStmt, ++index, product.getPage().getPageId());
            DbUtil.setInt(pStmt, ++index, product.isRetired());

            pStmt.executeUpdate();

            rs = pStmt.getGeneratedKeys();

            while (rs.next()) {
                product.setProductId(rs.getInt(1));
            }
            
            this.linkProductToCategory(conn, product);
            
            log.info("done");

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(pStmt);
            DbUtil.closeResultSet(rs);
        }

        return product;
    }
    
    private void unlinkProductToCategory(Connection conn, Product product) throws Exception {
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String sql = 
                "DELETE FROM product2category " +
                "WHERE productId = " + product.getProductId();

            log.fine(sql);

            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(stmt);
            DbUtil.closeResultSet(rs);
        }
    }
    
    private void linkProductToCategory(Connection conn, Product product) throws Exception {
        
        log.info("linkProductToCategory(Connection conn, Product product) ");
        
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String sql = 
                "INSERT INTO product2category (productId, categoryId) " +
                "SELECT " + product.getProductId() + ", " + product.getCategoryId() + " " +
                "FROM DUAL " +
                "WHERE NOT EXISTS (" +
                "       SELECT 1 FROM product2category " +
                "       where productId = " + product.getProductId() + " and categoryId = " + product.getCategoryId() +
                ")";

            log.fine(sql);

            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(stmt);
            DbUtil.closeResultSet(rs);
        }
    }
    
    private Product updateProduct(Connection conn, Product product) throws Exception {
    	
    	log.info("updateProduct(Connection conn, Product product)");
    	
        this.savePage(conn, product.getPage());

        PreparedStatement pStmt = null;
        ResultSet rs = null;

        try {
            String sqlUpdateAll = 
                "UPDATE product " +
                "SET urlAlias = ?, isGroupingProduct = ?, groupingConfigId = ?, " +
                "       attributeId1 = ?, attributeId2 = ?, attributeId3 = ?, attributeValueId1 = ?, attributeValueId2 = ?, attributeValueId3 = ?, " +
                "       name = ?, description = ?, " +
                "       smallerImageUrl = ?, smallerImageBlobKey = ?, smallImageUrl = ?, smallImageBlobKey = ?, mediumImageUrl = ?, mediumImageBlobKey = ?, largeImageUrl = ?, largeImageBlobKey = ?, " +
                "       imageFileName = ?, imageMimeType = ?, " +
                "       imageAltTagDesc = ?, supplierId = ?, " +
                "       sku = ?, price = ?, priceRuleId = ?,  vatRateId = ?, storageLocation = ?, stockLevel = ?, productStatusId = ?, pageId = ?, retired = ? " +
                "WHERE productId = ?";
            
            String sqlIgnoreImages =
                "UPDATE product " +
                "SET urlAlias = ?, isGroupingProduct = ?, groupingConfigId = ?, " +
                "       attributeId1 = ?, attributeId2 = ?, attributeId3 = ?, attributeValueId1 = ?, attributeValueId2 = ?, attributeValueId3 = ?, " +
                "       name = ?, description = ?, " +
                "       imageAltTagDesc = ?, supplierId = ?, " +
                "       sku = ?, price = ?, priceRuleId = ?, vatRateId = ?, storageLocation = ?, stockLevel = ?, productStatusId = ?, pageId = ?, retired = ? " +
                "WHERE productId = ?";
            
            if (product.getImages().getLargeImageBytes() != null 
                    && product.getImages().getMediumImageBytes() != null
                    && product.getImages().getSmallImageBytes() != null
                    && product.getImages().getSmallerImageBytes() != null) {
                pStmt = conn.prepareStatement(sqlUpdateAll);
                
                log.info("update sql (with images");
            } else {
                pStmt = conn.prepareStatement(sqlIgnoreImages);
                
                log.info("update sql (without images");
            }
            
            int index = 0;
            DbUtil.setString(pStmt, ++index, product.getUrlAlias());
            DbUtil.setInt(pStmt, ++index, product instanceof GroupingProduct);
            DbUtil.setInt(pStmt, ++index, product.getProductGroupingConfigId());
            DbUtil.setInt(pStmt, ++index, product.getAttributeId1());
            DbUtil.setInt(pStmt, ++index, product.getAttributeId2());
            DbUtil.setInt(pStmt, ++index, product.getAttributeId3());
            DbUtil.setInt(pStmt, ++index, product.getAttributeValueId1());
            DbUtil.setInt(pStmt, ++index, product.getAttributeValueId2());
            DbUtil.setInt(pStmt, ++index, product.getAttributeValueId3());
            DbUtil.setString(pStmt, ++index, product.getName());
            DbUtil.setString(pStmt, ++index, product.getDescription());
            
            if (product.getImages().getLargeImageBytes() != null && product.getImages().getSmallerImageBytes() != null) {
                DbUtil.setString(pStmt, ++index, product.getImages().getSmallerImageUrl());
                DbUtil.setString(pStmt, ++index, product.getImages().getSmallerImageBlobKeyString());
                DbUtil.setString(pStmt, ++index, product.getImages().getSmallImageUrl());
                DbUtil.setString(pStmt, ++index, product.getImages().getSmallImageBlobKeyString());
                DbUtil.setString(pStmt, ++index, product.getImages().getMediumImageUrl());
                DbUtil.setString(pStmt, ++index, product.getImages().getMediumImageBlobKeyString());
                DbUtil.setString(pStmt, ++index, product.getImages().getLargeImageUrl());
                DbUtil.setString(pStmt, ++index, product.getImages().getLargeImageBlobKeyString());
                DbUtil.setString(pStmt, ++index, product.getImages().getLargeImageFileName());
                DbUtil.setString(pStmt, ++index, product.getImages().getLargeImageMimeType());  
            } 
            
            DbUtil.setString(pStmt, ++index, product.getImages().getAltTagDescription());
            DbUtil.setInt(pStmt, ++index, product.getSupplierId());
            DbUtil.setString(pStmt, ++index, product.getSku());
            pStmt.setDouble(++index, product.getPrice().doubleValue());
            DbUtil.setInt(pStmt, ++index, product.getPriceRuleId());
            DbUtil.setInt(pStmt, ++index, product.getVatRuleId());
            DbUtil.setString(pStmt, ++index, product.getStoreageLocation());
            DbUtil.setInt(pStmt, ++index, product.getStockLevel());
            DbUtil.setInt(pStmt, ++index, product.getStatusId());
            DbUtil.setInt(pStmt, ++index, product.getPage().getPageId());
            DbUtil.setInt(pStmt, ++index, product.isRetired());
            
            DbUtil.setInt(pStmt, ++index, product.getProductId());

            pStmt.executeUpdate();
            
            this.unlinkProductToCategory(conn, product);
            this.linkProductToCategory(conn, product);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(pStmt);
            DbUtil.closeResultSet(rs);
        }

        return product;
    }
    
    private List<String> getLargeImageBlobsToDelete(Connection conn, Product product) throws Exception {
        List<String> blobStringsToDelete = new ArrayList<String>();
        
        List<String> imageBlobStrings = this.getImageBlobStrings(conn, product, ProductImages.ProductImage.LARGE);
        for (String s : imageBlobStrings) {
            if (!this.isBlobImageinUse(conn, s, product.getProductId())) {
                blobStringsToDelete.add(s);
            }
        }
        
        return blobStringsToDelete;
    }
    
    private List<String> getMediumImageBlobsToDelete(Connection conn, Product product) throws Exception {
        List<String> blobStringsToDelete = new ArrayList<String>();
        
        List<String> imageBlobStrings = this.getImageBlobStrings(conn, product, ProductImages.ProductImage.MEDIUM);
        for (String s : imageBlobStrings) {
            if (!this.isBlobImageinUse(conn, s, product.getProductId())) {
                blobStringsToDelete.add(s);
            }
        }
        
        return blobStringsToDelete;
    }
    
    private List<String> getSmallImageBlobsToDelete(Connection conn, Product product) throws Exception {
        List<String> blobStringsToDelete = new ArrayList<String>();
        
        List<String> imageBlobStrings = this.getImageBlobStrings(conn, product, ProductImages.ProductImage.SMALL);
        for (String s : imageBlobStrings) {
            if (!this.isBlobImageinUse(conn, s, product.getProductId())) {
                blobStringsToDelete.add(s);
            }
        }
        
        return blobStringsToDelete;
    }
    
    private List<String> getSmallerImageBlobsToDelete(Connection conn, Product product) throws Exception {
        List<String> blobStringsToDelete = new ArrayList<String>();
        
        List<String> imageBlobStrings = this.getImageBlobStrings(conn, product, ProductImages.ProductImage.SMALLER);
        
        for (String s : imageBlobStrings) {
            boolean blobImageinUse = this.isBlobImageinUse(conn, s, product.getProductId());
            
            if (!blobImageinUse) {
                blobStringsToDelete.add(s);
            }
        }
        
        return blobStringsToDelete;
    }
    
    private List<String> getImageBlobStrings(Connection conn, Product product, ProductImages.ProductImage image) throws Exception {
        List<String> blobStrings = new ArrayList<String>();
        
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            String sql = 
                "SELECT smallerImageBlobKey, smallImageBlobKey, mediumImageBlobKey, largeImageBlobKey " +
                "FROM product " +
                "WHERE productId = " + product.getProductId();

            log.fine(sql);

            stmt = conn.prepareStatement(sql);

            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                if (image == ProductImages.ProductImage.SMALLER) {
                    blobStrings.add(rs.getString(1));
                } else if (image == ProductImages.ProductImage.MEDIUM) {
                    blobStrings.add(rs.getString(2));
                } else if (image == ProductImages.ProductImage.SMALL) {
                    blobStrings.add(rs.getString(3));
                } else if (image == ProductImages.ProductImage.LARGE) {
                    blobStrings.add(rs.getString(4));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(stmt);
            DbUtil.closeResultSet(rs);
        }
        
        return blobStrings;
    }
    
    @SuppressWarnings("resource")
    private boolean isBlobImageinUse(Connection conn, String blobKeyString, int exculdingproductId) throws Exception {
        boolean inUse = false;
        
        PreparedStatement pStmt = null;
        ResultSet rs = null;

        try {
            String sql = 
                "select count(1) AS inUseCount " +
                "from product p " +
                "where (smallerImageBlobKey = ? OR smallImageBlobKey = ? OR mediumImageBlobKey = ? OR  largeImageBlobKey = ?) and p.productId != ?";

            pStmt = conn.prepareStatement(sql);
            int index = 0;
            DbUtil.setString(pStmt, ++index, blobKeyString);
            DbUtil.setString(pStmt, ++index, blobKeyString);
            DbUtil.setString(pStmt, ++index, blobKeyString);
            DbUtil.setString(pStmt, ++index, blobKeyString);
            DbUtil.setInt(pStmt, ++index, exculdingproductId);

            rs = pStmt.executeQuery();
            while(rs.next()) {
                if (rs.getInt(1) > 0) {
                    return true;
                }
            }

            return inUse;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(pStmt);
            DbUtil.closeResultSet(rs);
        }
    }
    
    
    private GroupingProduct saveGroupingProduct(Connection conn, GroupingProduct groupingProduct) throws Exception {
        ImageServiceGAEWorker imageServiceWorker = new ImageServiceGAEWorker();
        
        try {
            if (groupingProduct.getProductId() > 0) {
                
                if (groupingProduct.getImages().getLargeImageBytes() != null) {
                    List<String> largeImageBlobsToDelete = this.getLargeImageBlobsToDelete(conn, groupingProduct);
                    for (String blobStringToDelete : largeImageBlobsToDelete) {
                        imageServiceWorker.deleteBlob(blobStringToDelete);
                    }
                    imageServiceWorker.storeLargeImage(groupingProduct.getImages());
                }
                
                if (groupingProduct.getImages().getMediumImageBytes() != null) {
                    List<String> mediumImageBlobsToDelete = this.getMediumImageBlobsToDelete(conn, groupingProduct);
                    for (String blobStringToDelete : mediumImageBlobsToDelete) {
                        imageServiceWorker.deleteBlob(blobStringToDelete);
                    }
                    imageServiceWorker.storeMediumImage(groupingProduct.getImages());
                }
                
                if (groupingProduct.getImages().getSmallImageBytes() != null) {
                    List<String> smallImageBlobsToDelete = this.getSmallImageBlobsToDelete(conn, groupingProduct);
                    for (String blobStringToDelete : smallImageBlobsToDelete) {
                        imageServiceWorker.deleteBlob(blobStringToDelete);
                    }
                    imageServiceWorker.storeSmallmage(groupingProduct.getImages());
                }
                
                if (groupingProduct.getImages().getSmallerImageBytes() != null) {
                    List<String> smallerImageBlobsToDelete = this.getSmallerImageBlobsToDelete(conn, groupingProduct);
                    for (String blobStringToDelete : smallerImageBlobsToDelete) {
                        imageServiceWorker.deleteBlob(blobStringToDelete);
                    }
                    
                    imageServiceWorker.storeSmallerImage(groupingProduct.getImages());
                } 
                
                this.updateProduct(conn, groupingProduct);
            } else {
                // create all the images and save them as blobs
                imageServiceWorker.storeImages(groupingProduct.getImages());
                this.insertProduct(conn, groupingProduct);
            }
            
            for (Product p : groupingProduct.getProducts()) {
                // do the same for products in the category. this will link to the same blobs if the same image is used
                imageServiceWorker.storeImages(groupingProduct.getImages());
                if (p.getProductId() > 0) {
                    
                    if (p.getImages().getLargeImageBytes() != null) {
                        List<String> largeImageBlobsToDelete = this.getLargeImageBlobsToDelete(conn, p);
                        for (String blobStringToDelete : largeImageBlobsToDelete) {
                            imageServiceWorker.deleteBlob(blobStringToDelete);
                        }
                        imageServiceWorker.storeLargeImage(p.getImages());
                    }
                    
                    if (p.getImages().getMediumImageBytes() != null) {
                        List<String> mediumImageBlobsToDelete = this.getMediumImageBlobsToDelete(conn, p);
                        for (String blobStringToDelete : mediumImageBlobsToDelete) {
                            imageServiceWorker.deleteBlob(blobStringToDelete);
                        }
                        imageServiceWorker.storeMediumImage(p.getImages());
                    }
                    
                    if (p.getImages().getSmallImageBytes() != null) {
                        List<String> smallImageBlobsToDelete = this.getSmallImageBlobsToDelete(conn, p);
                        for (String blobStringToDelete : smallImageBlobsToDelete) {
                            imageServiceWorker.deleteBlob(blobStringToDelete);
                        }
                        imageServiceWorker.storeSmallmage(p.getImages());
                    }
                    
                    if (p.getImages().getSmallerImageBytes() != null) {
                        List<String> smallerImageBlobsToDelete = this.getSmallerImageBlobsToDelete(conn, p);
                        for (String blobStringToDelete : smallerImageBlobsToDelete) {
                            imageServiceWorker.deleteBlob(blobStringToDelete);
                        }
                        
                        imageServiceWorker.storeSmallerImage(p.getImages());
                    }
                    
                    this.updateProduct(conn, p);
                } else {
                    imageServiceWorker.storeImages(p.getImages());
                    this.insertProduct(conn, p);
                }
            }
        } catch (Exception e) {
            imageServiceWorker.deleteImagesCreatedForSession(); // problem with this on dev server but works live apparently: issue id 4744
            throw e;
        }
        
        this.linkGroupingProduct2Products(conn, groupingProduct);
        
        return groupingProduct;
    }
    
    private Page savePage(Connection conn, Page page) throws Exception {
        
        log.info("save page");
        
        if (page.getPageId() > 0) {
            return this.updatePage(conn, page);
        } else {
            return this.insertPage(conn, page);
        }
    }
    
    private Page insertPage(Connection conn, Page page) throws Exception {

        PreparedStatement pStmt = null;
        ResultSet rs = null;

        try {
            String sql = 
                "INSERT INTO page " +
                "(urlAlias, title, keywords, description, html) " +
                "VALUES (?, ?, ?, ?, ?)";

            log.fine(sql);

            pStmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            int index = 0;
            DbUtil.setString(pStmt, ++index, page.getUrlAlias());
            DbUtil.setString(pStmt, ++index, page.getTitle());
            DbUtil.setString(pStmt, ++index, page.getMetaKeywords());
            DbUtil.setString(pStmt, ++index, page.getMetaDescription());
            DbUtil.setString(pStmt, ++index, page.getHtml());

            pStmt.executeUpdate();

            rs = pStmt.getGeneratedKeys();

            while (rs.next()) {
                page.setPageId(rs.getInt(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(pStmt);
            DbUtil.closeResultSet(rs);
        }

        return page;
    }
    
    private Page updatePage(Connection conn, Page page) throws Exception {
        PreparedStatement pStmt = null;
        ResultSet rs = null;

        try {
            String sql = 
                "UPDATE page " +
                "SET urlAlias = ?, title = ? , keywords = ?, description = ?, html = ? " +
                "WHERE pageId = ? ";

            log.fine(sql);

            pStmt = conn.prepareStatement(sql);
            int index = 0;
            DbUtil.setString(pStmt, ++index, page.getUrlAlias());
            DbUtil.setString(pStmt, ++index, page.getTitle());
            DbUtil.setString(pStmt, ++index, page.getMetaKeywords());
            DbUtil.setString(pStmt, ++index, page.getMetaDescription());
            DbUtil.setString(pStmt, ++index, page.getHtml());
            pStmt.setInt(++index, page.getPageId());

            pStmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(pStmt);
            DbUtil.closeResultSet(rs);
        }

        return page;
    }
       
    private void linkGroupingProduct2Products(Connection conn, GroupingProduct groupingProduct) throws Exception {
        Statement stmt = null;
        PreparedStatement pStmt = null;

        try {
            String sql = 
                "DELETE FROM groupingproduct2product " +
                "WHERE groupingProductId = " + groupingProduct.getProductId();
            
            String sql2 = 
                "INSERT INTO groupingproduct2product (groupingProductId, productId) " +
                "VALUES (?, ?)";

            log.fine(sql);

            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            
            pStmt = conn.prepareStatement(sql2);
            
            int index = 0;
            
            if (groupingProduct.getChildProductIds() != null && !groupingProduct.getChildProductIds().trim().equals("")) {
                // in this case we are just using existing products in the groupings menu
                String[] ids = groupingProduct.getChildProductIds().split(",");
                for (String id : ids) {
                    pStmt.setInt(++index, groupingProduct.getProductId());
                    pStmt.setInt(++index, Integer.valueOf(id));
                    
                    pStmt.addBatch();
                    index = 0;
                }
            } else {
                for (Product p : groupingProduct.getProducts()) {
                    pStmt.setInt(++index, groupingProduct.getProductId());
                    pStmt.setInt(++index, p.getProductId());
                    
                    pStmt.addBatch();
                    index = 0;
                }    
            }
            
            pStmt.executeBatch();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(stmt);
            DbUtil.closeStatement(pStmt);
        }
    }

    /* 
     * @see com.groovyfly.admin.service.ProductServiceIF#retireProduct(int)
     */
    @Override
    public void retireProduct(int productId) throws Exception {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "UPDATE product " +
                "SET retired = 1 " +
                "WHERE productId = " + productId;

            log.fine(sql);

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

    /* 
     * @see com.groovyfly.admin.service.ProductServiceIF#getGroupingProductSummaries(boolean)
     */
    @Override
    public List<GroupingProduct> getGroupingProductSummaries(boolean includeRetired) throws Exception {

        List<GroupingProduct> productSummaries = new ArrayList<GroupingProduct>();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            StringBuilder sql = new StringBuilder();
            sql.append(
                    "SELECT gp.productId, gp.name, gp.smallerImageUrl, gp.imageAltTagDesc, COUNT(gp2p.groupingProductId) AS noProds " +
                    "FROM product gp " +
                    "LEFT OUTER JOIN groupingproduct2product gp2p ON gp2p.groupingProductId = gp.productId " +
                    "LEFT OUTER JOIN product p ON gp2p.productId = p.productId " +
                    "LEFT OUTER JOIN product2category p2c ON p2c.productId = gp.productId  " +
                    "LEFT OUTER JOIN category c ON p2c.categoryId = c.categoryId " +
                    "WHERE gp.isGroupingProduct = 1 "
            );

            if (includeRetired == false) {
                sql.append(" ");
                sql.append("AND gp.retired = 0 ");
            }
            
            sql.append("GROUP BY gp.productId, gp.name, gp.smallerImageUrl, gp.imageAltTagDesc, gp.price ");
            sql.append("ORDER BY p.name");
            
            log.fine(sql.toString());

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql.toString());

            GroupingProduct p = null;
            while (rs.next()) {
                p = new GroupingProduct();
                p.setProductId(rs.getInt("productId"));
                p.setName(rs.getString("name"));
                
                ProductImages i = new ProductImages();
                String serImg = rs.getString("smallerImageUrl");
                if (serImg != null && serImg.endsWith("=s58")) {
                    i.setSmallerImageUrl(serImg.substring(0, serImg.length() - 4));    
                } else {
                    i.setSmallerImageUrl(serImg);
                }
                
                i.setAltTagDescription(rs.getString("imageAltTagDesc"));
                p.setImages(i);
                
                p.setChildProductCount(rs.getInt("noProds"));
                
                productSummaries.add(p);
            }  
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(stmt);
            DbUtil.closeResultSet(rs);
        }

        return productSummaries;
    }
    
}
