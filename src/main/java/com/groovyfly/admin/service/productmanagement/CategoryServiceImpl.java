/*
 * CategoryServiceImpl.java
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

import com.groovyfly.common.structures.Category;
import com.groovyfly.common.structures.Page;
import com.groovyfly.common.util.DbUtil;
import com.groovyfly.common.util.GroovyFlyDS;

/**
 * @author Chris Hatton
 * 
 * Created 22 Jul 2012
 */
public class CategoryServiceImpl implements CategoryServiceIF {

    private static Logger log = Logger.getLogger(CategoryServiceImpl.class.getName());

    private GroovyFlyDS groovyFlyDS;

    public void setGroovyFlyDS(GroovyFlyDS groovyFlyDS) {
        this.groovyFlyDS = groovyFlyDS;
    }

    /* 
     * @see com.groovyfly.admin.service.CategoryServiceIF#getCategories(boolean)
     */
    @Override
    public List<Category> getCategories(boolean includeRetired) throws Exception {
        List<Category> categories = new ArrayList<Category>();

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
                "SELECT c.categoryId, c.parentCategoryId, c.urlAlias, c.name, c.description, c.sortIndex, c.retired, (  " +
                "	SELECT GROUP_CONCAT(c2.name SEPARATOR ' > ')  " +
                "	FROM category c1  " +
                "	INNER JOIN category c2 ON c1.left_side BETWEEN c2.left_side AND c2.right_side and c2.left_side <> 1  " +
                "	WHERE c1.categoryId = c.categoryId  " +
                ") AS path, (  " +
                "	SELECT COUNT(*) AS level  " +
                "	FROM category c3  " +
                "	INNER JOIN category c4  " +
                "	ON c3.left_side BETWEEN c4.left_side AND c4.right_side	 " +
                "	WHERE c3.categoryId = c.categoryId  " +
                "	GROUP BY c3.name  " +
                ") as level, " +
                "CASE WHEN c.sortIndex = sortIndexLimit.minSortIndex THEN 1 ELSE 0 END AS firstInCategory, " +
                "CASE WHEN c.sortIndex = sortIndexLimit.maxSortIndex THEN 1 ELSE 0 END AS lastInCategory " +
                "FROM category c " +
                "LEFT OUTER JOIN (" +
                "	SELECT c5.parentCategoryId, MIN(sortIndex) AS minSortIndex,  MAX(sortIndex) AS maxSortIndex " +
                "	FROM category c5 " +
                "	GROUP BY c5.parentCategoryId " +
                ") sortIndexLimit ON c.parentCategoryId = sortIndexLimit.parentCategoryId " +
                whereCondition + " " +
                "ORDER BY level, sortIndex";

            log.fine(sql);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            Category c = null;
            while (rs.next()) {
                c = new Category();
                c.setCategoryId(rs.getInt("categoryId"));
                c.setParentId(rs.getInt("parentCategoryId"));
                c.setUrlAlias(rs.getString("urlAlias"));
                c.setName(rs.getString("name"));
                c.setDescription(rs.getString("description"));
                c.setPath(rs.getString("path") == null ? "-- none --" : rs.getString("path"));
                c.setSortIndex(rs.getInt("sortIndex"));
                c.setFirstInCategory(rs.getBoolean("firstInCategory"));
                c.setLastInCategory(rs.getBoolean("lastInCategory"));
                c.setRetired(rs.getBoolean("retired"));
                
                c.setProductCountInCategory(this.getProductCountInCategory(c.getCategoryId()));

                categories.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(stmt);
            DbUtil.closeResultSet(rs);
        }

        return categories;
    }
    
    public int getProductCountInCategory(int categoryId) throws Exception {
        int count = 0;
        
        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT COUNT(*) AS cnt " +
                "FROM product2category p2c " +
                "JOIN product p on p2c.productId = p.productId " +
                "WHERE p2c.categoryId = ? AND p.retired != 1 AND p.isGroupingProduct = 0 " +
                "GROUP BY categoryId";

            log.fine(sql);

            pStmt = conn.prepareStatement(sql);
            int index = 0;
            DbUtil.setInt(pStmt, ++index, categoryId);
            
            rs = pStmt.executeQuery();
            
            // if no rows for category then loop will not run and zero will be returned
            while(rs.next()) {
                count = rs.getInt(1);    
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(pStmt);
            DbUtil.closeResultSet(rs);
        }
        
        return count;
    }
    
    public int getCategoryCountInCategory(int categoryId) throws Exception {
        int count = 0;
        
        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT COUNT(*) AS cnt " +
                "FROM category " +
                "WHERE parentCategoryId = ? " +
                "GROUP BY parentCategoryId";

            log.fine(sql);

            pStmt = conn.prepareStatement(sql);
            int index = 0;
            DbUtil.setInt(pStmt, ++index, categoryId);
            
            rs = pStmt.executeQuery();
            
            // if no rows for category then loop will not run and zero will be returned
            while(rs.next()) {
                count = rs.getInt(1);    
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(pStmt);
            DbUtil.closeResultSet(rs);
        }
        
        return count;
    }

    /* 
     * @see com.groovyfly.admin.service.CategoryServiceIF#getCategory(int)
     */
    @Override
    public Category getCategory(int categoryId) throws Exception {

        Connection conn = null;

        try {
            conn = groovyFlyDS.getConnection();

            return this.getCategory(conn, categoryId);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
        }
    }
    
    public Category getCategoryForPageId(int pageId) throws Exception {
        Category c = null;
        
        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;

        try {
            String sql = 
                "SELECT c.categoryId, c.parentCategoryId, c.name, c.description " +
                "FROM category c " +
                "WHERE c.pageId = ? ";
            
            conn = groovyFlyDS.getConnection();
            pStmt = conn.prepareStatement(sql);
            int index = 0;
            DbUtil.setInt(pStmt, ++index, pageId);
            
            rs = pStmt.executeQuery();

            while (rs.next()) {
                c = new Category();
                c.setCategoryId(rs.getInt("categoryId"));
                c.setParentId(rs.getInt("parentCategoryId"));
                c.setName(rs.getString("name"));
                c.setDescription(rs.getString("description"));
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

    private Category getCategory(Connection conn, int id) throws Exception {
        Category c = null;

        Statement stmt = null;
        ResultSet rs = null;

        try {
            String sql = 
                "SELECT c.categoryId, c.parentCategoryId, c.urlAlias, c.name, c.description, " +
                "       p.pageId, p.title, p.keywords, p.description as pageDesc, p.html " +
                "FROM category c " +
                "JOIN page p ON c.pageId = p.pageId " +
                "WHERE categoryId = " + id;

            log.fine(sql);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                c = new Category();
                c.setCategoryId(rs.getInt("categoryId"));
                c.setParentId(rs.getInt("parentCategoryId"));
                c.setUrlAlias(rs.getString("urlAlias"));
                c.setName(rs.getString("name"));
                c.setDescription(rs.getString("description"));
                
                Page p = new Page();
                p.setPageId(rs.getInt("pageId"));
                p.setTitle(rs.getString("title"));
                p.setMetaKeywords(rs.getString("keywords"));
                p.setMetaDescription(rs.getString("pageDesc"));
                p.setHtml(rs.getString("html"));
                
                c.setPage(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(stmt);
            DbUtil.closeResultSet(rs);
        }

        return c;
    }

    /* 
     * @see com.groovyfly.admin.service.CategoryServiceIF#insertCategory(com.groovyfly.admin.structures.Category)
     */
    @Override
    public void insertCategory(Category category) throws Exception {
        
        Connection conn = null;

        try {
            conn = groovyFlyDS.getConnection();
            conn.setAutoCommit(false);
            this.insertCategory(conn, category);
            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);

            DbUtil.closeConnection(conn);
        }
    }

    private Category insertCategory(Connection conn, Category category) throws Exception {

        Statement stmt = null;
        Statement stmt1 = null;
        Statement stmt2 = null;
        Statement stmt3 = null;
        Statement stmt4 = null;
        ResultSet rs = null;

        try {
            
            Page savedPage = this.savePage(conn, category.getPage());
            
            String sql = 
                "SET @parentCategoryId = " + category.getParentId();

            String sql1 = 
                "SELECT @nextSortIndex := COALESCE(MAX(sortIndex) + 1, 0) " +
                "FROM category c5 " +
                "WHERE c5.parentCategoryId = @parentCategoryId";

            String sql2 = 
                "SELECT @insert_right := right_side " +
                "FROM category " +
                "WHERE categoryId = @parentCategoryId";

            String sql3 = 
                "UPDATE category " +
                "SET left_side = IF(left_side > @insert_right, left_side + 2, left_side), " +
                "	right_side = IF(right_side >= @insert_right, right_side + 2, right_side) " +
                "WHERE right_side >= @insert_right";

            String sql4 = 
                "INSERT INTO category " +
                "(parentCategoryId, urlAlias, name, description, sortIndex, left_side, right_side, pageId, retired) " +
                "VALUES (@parentCategoryId , " + DbUtil.quoteStringOrNull(category.getUrlAlias()) + ", " + DbUtil.quoteStringOrNull(category.getName()) + ", " + DbUtil.quoteStringOrNull(category.getDescription()) + ", @nextSortIndex, @insert_right, (@insert_right + 1), " + savedPage.getPageId() + ", 0)";

            log.fine(sql);
            log.fine(sql4);

            StringBuffer sb = new StringBuffer();
            sb.append(sql + "; ");
            sb.append(sql1 + "; ");
            sb.append(sql2 + "; ");
            sb.append(sql3 + "; ");
            sb.append(sql4 + "; ");

            log.fine(sb.toString());

            stmt = conn.createStatement();
            stmt1 = conn.createStatement();
            stmt2 = conn.createStatement();
            stmt3 = conn.createStatement();
            stmt4 = conn.createStatement();

            stmt.execute(sql);
            stmt1.execute(sql1);
            stmt2.execute(sql2);
            stmt3.executeUpdate(sql3);
            stmt4.executeUpdate(sql4, Statement.RETURN_GENERATED_KEYS);

            rs = stmt4.getGeneratedKeys();

            while (rs.next()) {
                category.setCategoryId(rs.getInt(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(stmt);
            DbUtil.closeStatement(stmt1);
            DbUtil.closeStatement(stmt2);
            DbUtil.closeStatement(stmt3);
            DbUtil.closeStatement(stmt4);
            DbUtil.closeResultSet(rs);
        }

        return category;
    }

    /* 
     * @see com.groovyfly.admin.service.CategoryServiceIF#updateCategory(com.groovyfly.admin.structures.Category)
     */
    @Override
    public void updateCategory(Category category) throws Exception {
        log.fine("updateCategory(Category category)");

        Connection conn = null;

        try {
            conn = groovyFlyDS.getConnection();
            conn.setAutoCommit(false);

            if (this.hasParentChanged(conn, category)) {
                
                int oldCategoryId = category.getCategoryId();
                
                this.disableConstraints(conn);
                this.deleteCategory(conn, category.getCategoryId());
                Category newCategory = this.insertCategory(conn, category);
                this.changeProductParent(conn, newCategory, oldCategoryId);
                this.enableConstraints(conn);
            } else {
                this.updateCategoryIgnoreParent(conn, category);
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
    }
    
    private boolean hasParentChanged(Connection conn, Category category) throws Exception {
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        
        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT parentCategoryId " +
                "FROM category " +
                "WHERE categoryId = ?";

            pStmt = conn.prepareStatement(sql);
            int index = 0;
            DbUtil.setInt(pStmt, ++index, category.getCategoryId());
            rs = pStmt.executeQuery();
            rs.next();
            int currentParentCategoryId = rs.getInt(1);
            if (category.getParentId() != currentParentCategoryId) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(pStmt);
            DbUtil.closeResultSet(rs);
        }
    }
    
    private void updateCategoryIgnoreParent(Connection conn, Category category) throws Exception {
        PreparedStatement pStmt = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "UPDATE category " +
                "SET urlAlias = ?, name = ?, description = ?" +
                "WHERE categoryId = ?";

            log.fine(sql);

            pStmt = conn.prepareStatement(sql);
            int index = 0;
            DbUtil.setString(pStmt, ++index, category.getUrlAlias());
            DbUtil.setString(pStmt, ++index, category.getName());
            DbUtil.setString(pStmt, ++index, category.getDescription());
            DbUtil.setInt(pStmt, ++index, category.getCategoryId());
            pStmt.executeUpdate();
            
            this.savePage(conn, category.getPage());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(pStmt);
        }
    }

    private void disableConstraints(Connection conn) throws Exception {
        Statement stmt = null;

        try {
            stmt = conn.createStatement();
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(stmt);
        }
    }
    
    private void enableConstraints(Connection conn) throws Exception {
        Statement stmt = null;

        try {
            stmt = conn.createStatement();
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(stmt);
        }      
    }
    
    private void changeProductParent(Connection conn, Category newCategory, int oldCategoryId) throws Exception {
        Statement stmt = null;

        try {
            stmt = conn.createStatement();

            String sql = 
                "UPDATE product2category " +
                "SET categoryId = " + newCategory.getCategoryId() + " " +
                "WHERE categoryId = " + oldCategoryId;
            
            log.fine(sql);

            stmt.executeUpdate(sql);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(stmt);
        }
    }
    
    /* 
     * @see com.groovyfly.admin.service.CategoryServiceIF#retireCategory(int)
     */
    @Override
    public void deleteCategory(int categoryId) throws Exception {
        Connection conn = null;

        try {
            conn = groovyFlyDS.getConnection();
            conn.setAutoCommit(false);
            
            int categoryPageId = this.getPageIdForCategoryId(conn, categoryId) ;
            this.deleteCategory(conn, categoryId);
            this.deletePage(conn, categoryPageId);
            
            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
            conn.rollback();
            throw new SQLException(e.getMessage());
        } finally {
            conn.setAutoCommit(true);
            DbUtil.closeConnection(conn);
        }
    }
    
    private int getPageIdForCategoryId(Connection conn, int categoryId) throws Exception {
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String sql = 
                "SELECT c.pageId " +
                "FROM category c " +
                "WHERE c.categoryId = " + categoryId;

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(stmt);
        }
        
        return -1;
    }

    private void deleteCategory(Connection conn, int id) throws Exception {
        Statement stmt = null;
        Statement stmt1 = null;
        Statement stmt2 = null;

        try {
            String sql = 
                "SELECT @delete_left := left_side, @delete_right := right_side " +
                "FROM category " +
                "WHERE categoryId = " + id;

            String sql1 = 
                "UPDATE category " +
                "SET left_side = IF(left_side > @delete_left, left_side - 2, left_side), " +
                "	 right_side = IF(right_side > @delete_right, right_side - 2, right_side) " +
                "WHERE right_side > @delete_right";

            String sql2 = 
                "DELETE FROM category " +
                "WHERE category.categoryId = " + id;

            log.fine(sql);

            stmt = conn.createStatement();
            stmt1 = conn.createStatement();
            stmt2 = conn.createStatement();

            stmt.execute(sql);
            stmt1.executeUpdate(sql1);
            stmt2.executeUpdate(sql2);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(stmt);
            DbUtil.closeStatement(stmt1);
            DbUtil.closeStatement(stmt2);
        }
    }
    
    private void deletePage(Connection conn, int pageId) throws Exception {
        Statement stmt = null;

        try {
            String sql = 
                "DELETE FROM page " +
                "WHERE pageId = " + pageId;

            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(stmt);
        }
    }

    /*
     * @see com.groovyfly.admin.service.CategoryServiceIF#moveUpIndex(int)
     */
    public void moveUpIndex(int categoryId) throws Exception {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String sql = 
                "SELECT categoryId, parentCategoryId, sortIndex " +
                "FROM category " +
                "WHERE parentCategoryId = ( " +
                "	SELECT parentCategoryId " +
                "	FROM category " +
                "	WHERE category.categoryId = " + categoryId +
                ") " +
                "ORDER BY sortIndex DESC";

            log.fine(sql);

            conn = groovyFlyDS.getConnection();

            conn.setAutoCommit(false);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt(1);
                int parentId = rs.getInt(2);
                int sortIndex = rs.getInt(3);

                if (id == categoryId) { // now we are on the target row we are to update


                    if (rs.next()) {
                        int nextId = rs.getInt(1);
                        int nextParentId = rs.getInt(2);
                        int nextSortIndex = rs.getInt(3);

                        if (parentId == nextParentId) {
                            // update our target row (move index up one)
                            this.updateIndex(conn, id, nextSortIndex);

                            // update the row after the target row (move index down one)
                            this.updateIndex(conn, nextId, sortIndex);
                        }
                    }
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
            DbUtil.closeStatement(stmt);
        }
    }

    /*
     * I wanted to do this using a scrollable result set, but this time the google driver does not support scrollable result sets.
     * 
     * @see com.groovyfly.admin.service.CategoryServiceIF#moveDownIndex(int)
     */
    public void moveDownIndex(int categoryId) throws Exception {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String sql = 
                "SELECT categoryId, parentCategoryId, sortIndex " +
                "FROM category " +
                "WHERE parentCategoryId = ( " +
                "	SELECT parentCategoryId " +
                "	FROM category " +
                "	WHERE category.categoryId = " + categoryId +
                ") " +
                "ORDER BY sortIndex";

            log.fine(sql);

            conn = groovyFlyDS.getConnection();

            conn.setAutoCommit(false);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt(1);
                int parentId = rs.getInt(2);
                int sortIndex = rs.getInt(3);

                if (id == categoryId) { // now we are on the target row we are to update

                    if (rs.next()) {
                        int nextId = rs.getInt(1);
                        int nextParentId = rs.getInt(2);
                        int nextSortIndex = rs.getInt(3);

                        if (parentId == nextParentId) {
                            // update our target row (move index up one)
                            this.updateIndex(conn, id, nextSortIndex);

                            // update the row after the target row (move index down one)
                            this.updateIndex(conn, nextId, sortIndex);
                        }
                    }
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
            DbUtil.closeStatement(stmt);
        }
    }

    private void updateIndex(Connection conn, int categoryId, int sortIndex) throws Exception {
        Statement stmt = null;

        try {

            stmt = conn.createStatement();

            String sql =
                "UPDATE category " +
                "SET sortIndex = " + sortIndex + " " + 
                "WHERE categoryId = " + categoryId;

            log.fine(sql);

            stmt.executeUpdate(sql);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeStatement(stmt);
        }
    }
    
    private Page savePage(Connection conn, Page page) throws Exception {
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
    
    @SuppressWarnings("resource")
    public boolean isUrlAliasUnique(String urlAlias, Category category) throws Exception {
        boolean isUnique = true;
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = null;
            
            if (category.getCategoryId() == 0) {
                sql = 
                    "SELECT count(1) AS cnt " +
                    "FROM category " +
                    "WHERE urlAlias = " + DbUtil.quoteStringOrNull(urlAlias);
            } else {
                sql = 
                    "SELECT count(1) AS cnt " +
                    "FROM category " +
                    "WHERE urlAlias = " + DbUtil.quoteStringOrNull(urlAlias) + " " +
                    "AND category.categoryId != " + category.getCategoryId();
            }

            log.fine(sql);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(stmt);
            DbUtil.closeResultSet(rs);
        }
        
        return isUnique;
    }
}
