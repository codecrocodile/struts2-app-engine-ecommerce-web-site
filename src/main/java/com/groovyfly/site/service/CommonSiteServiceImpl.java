/*
 * CommonSiteServiceImlp.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.groovyfly.admin.service.productmanagement.CategoryServiceIF;
import com.groovyfly.common.structures.Category;
import com.groovyfly.common.structures.Page;
import com.groovyfly.common.util.DbUtil;
import com.groovyfly.common.util.GroovyFlyDS;
import com.groovyfly.common.util.cache.CacheIF;
import com.groovyfly.common.util.cache.CacheKey;

/**
 * @author Chris Hatton
 */
public class CommonSiteServiceImpl implements CommonSiteServiceIF {
    
    private static Logger log = Logger.getLogger(CommonSiteServiceImpl.class.getName());
    
    private CacheIF cacheIF;
    
    private GroovyFlyDS groovyFlyDS;
    
    private CategoryServiceIF categoryServiceIF;
    
    private Map<Integer, String> monthMap = new HashMap<Integer, String>();
    
    {
        monthMap.put(0, "January");
        monthMap.put(1, "Febuary");
        monthMap.put(2, "March");
        monthMap.put(3, "April");
        monthMap.put(4, "May");
        monthMap.put(5, "June");
        monthMap.put(6, "July");
        monthMap.put(7, "August");
        monthMap.put(8, "September");
        monthMap.put(9, "October");
        monthMap.put(10, "November");
        monthMap.put(11, "December");
    }

    public CacheIF getCacheIF() {
        return cacheIF;
    }

    public void setCacheIF(CacheIF cacheIF) {
        this.cacheIF = cacheIF;
    }

    public void setGroovyFlyDS(GroovyFlyDS groovyFlyDS) {
        this.groovyFlyDS = groovyFlyDS;
    }

    public void setCategoryServiceIF(CategoryServiceIF categoryServiceIF) {
        this.categoryServiceIF = categoryServiceIF;
    }
    
    /* 
     * @see com.groovyfly.site.service.CommonSiteServiceIF#getPage(java.lang.String, java.lang.String)
     */
    @Override
    public Page getPage(String subjectTable, String urlAlias) throws Exception {
        Page page  = new Page();
        
        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT p.pageId, p.title, p.keywords, p.description, p.html " +
                "from " + subjectTable.trim() + " s " +
                "join page p on p.pageId = s.pageId " +
                "where s.urlAlias = ?";

            log.info(sql);

            pStmt = conn.prepareStatement(sql);
            int index = 0;
            DbUtil.setString(pStmt, ++index, urlAlias);

            rs = pStmt.executeQuery();
            while (rs.next()) {
                page.setPageId(rs.getInt(1));
                page.setTitle(rs.getString(2));
                page.setMetaKeywords(rs.getString(3));
                page.setMetaDescription(rs.getString(4));
                page.setHtml(rs.getString(5));
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(pStmt);
            DbUtil.closeConnection(conn);
        }
        
        return page;
    }

    /* 
     * @see com.groovyfly.site.service.CommonSiteServiceIF#getSiteMenuHtmlListItems()
     */
    @Override
    public String getSiteMenuHtml() throws Exception {
        
        Object o = cacheIF.get(CacheKey.MENU_HTML);
        if (o != null) {
            return (String) o; 
        }
        
        /* get a flat list of the categories */
        List<Category> categories = categoryServiceIF.getCategories(false);
        
        /* sort by parent category id ascending so that we are able to add the categories 
         * to there parents as we iterate over the sorted map */
        Collections.sort(categories, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {   
                if (o1 == null && o2 != null) {
                    return -1;
                } else if (o1 != null && o2 == null) {
                    return 1;
                } else if (o1 == null && o2 == null) {
                    return 0;
                } else if (o1.getCategoryId() < o2.getCategoryId()) {
                    return -1;
                } else if (o1.getCategoryId() > o2.getCategoryId()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        /* run through the flat list and map them to their parent categories */
        Map<Integer, Category> catMap = new LinkedHashMap<Integer, Category>();
        for (Category c : categories) {
            catMap.put(c.getCategoryId(), c);
            
            if (catMap.containsKey(c.getParentId())) {
                catMap.get(c.getParentId()).getSubCategories().add(c);
            }
        }
        
        /* by this point we should not have the full tree structure, so now get the root node/category */
        Category rootCategory = null;
        for (Entry<Integer, Category> e : catMap.entrySet()) {
            if (e.getValue().getParentId() == 0) { 
                // this must be the root
                rootCategory = e.getValue();
                break;
            }
        }
        
        StringBuilder menuHtml = new StringBuilder();
        this.buildMenuHtml(rootCategory, menuHtml);
        
        cacheIF.put(CacheKey.MENU_HTML, menuHtml.toString());
        
        return menuHtml.toString();
    }
    
    private String buildMenuHtml(Category category, StringBuilder menuHtml) {
        
        /* 
         * build the menu html of the form:
         * 
         * <li><a href="#">Category 1</a></li>
         * <li><a href="#">Category 2</a></li>
         * <li>
         *     <ul>
         *         <li><a href="#">Sub Category 1</a></li>
         *         <li><a href="#">Sub Category 2</a></li>
         *     </ul>
         * </li> 
         */
        
        for (Category c : category.getSubCategories()) {
            menuHtml.append("<li><a href=\"");
            menuHtml.append(this.getUrl(c));
            menuHtml.append("\">");
            menuHtml.append(c.getName());
            menuHtml.append("</a></li>");
            
            if (c.getSubCategories().size() > 0) {
                menuHtml.append("<li><ul>");
                buildMenuHtml(c, menuHtml);
                menuHtml.append("</ul></li>");
            }
        }
        
        return menuHtml.toString();
    }
    
    private String getUrl(Category category) {
        String url = category.getUrlAlias(); // this is the url alias for the category
        url = url + "/page-1"; // this is the paging information
        
        return url;
    }
    
    private class TempFlyData {
        String urlAlias;
        String name;
        String imageUrl;
        String imageAltDesc;
    }
    
    public static void main(String[] args) {
        System.out.println(Calendar.JANUARY);
        System.out.println(Calendar.DECEMBER);
    }

    /* 
     * @see com.groovyfly.site.service.CommonSiteServiceIF#getFliesForMonthHtml()
     */
    @Override
    public String getFliesForMonthHtml() throws Exception {
        Object o = cacheIF.get(CacheKey.MONTH_FLIES_HTML);
        if (o != null) {
            return (String) o; 
        }
        
        /*
         * get the month of flies we want to display. if day is 5 days before or less
         * than the next month then use the next month to display for flies         
         */
        Calendar nowCal = Calendar.getInstance();
        for (int i = 0; i < 5; i++) {
            nowCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        int month = nowCal.get(Calendar.MONTH);
        
        int collectionId = getCollectionIdForMonth(month + 1);
        List<TempFlyData> tempFlyData = getFlyCollection(collectionId);
        
        String fliesByMonthHtml = this.buildFliesByMonthHtml(month, tempFlyData);
        cacheIF.put(CacheKey.MONTH_FLIES_HTML, fliesByMonthHtml, 86400);
        
        return fliesByMonthHtml;
    }

    private int getCollectionIdForMonth(int monthId) throws Exception {
        int collectionId = 0;
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT collectionId  " +
                "FROM month " +
                "WHERE monthId = " + monthId;
            
            log.info(sql);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                collectionId = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(stmt);
            DbUtil.closeConnection(conn);
        }
        
        return collectionId;
    }
    
    private String buildFliesByMonthHtml(int month, List<TempFlyData> tempFlyData) {
        
        log.info("buildFliesByMonthHtml");
        
        log.info("" + tempFlyData.size());
        /*
         * build the flies by month html of the form:
         * 
         * <h2>Flies for [month]</h2>
         * <div class="flickr_photos">
         *      <a href="#"><img src="" alt="" /></a>
         *      <a href="#"><img src="" alt="" /></a>
         *      <a href="#"><img src="" alt="" /></a>
         *      <a href="#"><img src="" alt="" /></a>
         *      <a href="#"><img src="" alt="" /></a>
         *      <a href="#"><img src="" alt="" /></a>
         * </div>
         * 
         */
        
        StringBuilder fbm = new StringBuilder();
        fbm.append("<h2>Flies for " + monthMap.get(month) + "</h2>");
        fbm.append("<div class=\"flickr_photos\">");
        
        for (TempFlyData tfd : tempFlyData) {
            fbm.append("<a href=\"" + tfd.urlAlias + "\"><img src=\"" + tfd.imageUrl + "\" alt=\"" + tfd.name + "\" /></a>");
        }
        
        int placeHolders = 6 - tempFlyData.size();
        if (placeHolders > 0) {
            for (int i = 0; i < placeHolders; i++) {
                fbm.append("<a href=\"#\"><img src=\"/images/assets/58x58.png\" alt=\"asset\" /></a>");    
            }
        }
        
        fbm.append("</div>");
        
        return fbm.toString();
    }

    /* 
     * @see com.groovyfly.site.service.CommonSiteServiceIF#getPopularFliesHtml()
     */
    @Override
    public String getPopularFliesHtml() throws Exception {
        
        Object o = cacheIF.get(CacheKey.POPULAR_HTML);
        if (o != null) {
            return (String) o; 
        }
        
        List<TempFlyData> tempFlyData = getFlyCollection(13); 
        String popularFliesHtml = this.buildPopularFliesHtml(tempFlyData);
        cacheIF.put(CacheKey.POPULAR_HTML, popularFliesHtml);
        
        return popularFliesHtml;
    }
    
    private String buildPopularFliesHtml(List<TempFlyData> tempFlyData) {
        /*
         * build the popular flies html of the form:
         * 
         * <a href="#"><img src="" alt="" /></a>
         * <a href="#"><img src="" alt="" /></a>
         * <a href="#"><img src="" alt="" /></a>
         */
        
        StringBuilder fbm = new StringBuilder();
        for (TempFlyData tfd : tempFlyData) {
            fbm.append("<a href=\"[url]\"><img src=\"[img_url]\" alt=\"[img_alt]\" /></a>"
                    .replace("[url]", tfd.urlAlias)
                    .replace("[img_url]", tfd.imageUrl)
                    .replace("[img_alt]", tfd.imageAltDesc));
        }
        
        int placeHolders = 3 - tempFlyData.size();
        if (placeHolders > 0) {
            for (int i = 0; i < placeHolders; i++) {
                fbm.append("<a href=\"#\"><img src=\"/images/assets/58x58.png\" alt=\"asset\" /></a>");
            }
        }
        
        return fbm.toString();
    }
    
    public List<TempFlyData> getFlyCollection(int collectionId) throws Exception {
        List<TempFlyData> tempFlyData = new ArrayList<TempFlyData>();
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT p.name, p.smallerImageUrl, p.imageAltTagDesc, c.urlAlias " +
                "FROM collection c " +
                "JOIN collection2product c2p on c2p.collectionId  = c.collectionId " +
                "JOIN product p on p.productId = c2p.productId " +
                "WHERE c.collectionId = " + collectionId;

            log.info(sql);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            TempFlyData tfd = null;
            while (rs.next()) {
                tfd = new TempFlyData();
                tfd.name = rs.getString(1);
                tfd.imageUrl = rs.getString(2);
                tfd.imageAltDesc = rs.getString(3);
                tfd.urlAlias = rs.getString(4);
                tempFlyData.add(tfd);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeConnection(conn);
            DbUtil.closeStatement(stmt);
            DbUtil.closeResultSet(rs);
        }
        
        return tempFlyData;
    }

    /* 
     * @see com.groovyfly.site.service.CommonSiteServiceIF#getCollectionListItemsHtml()
     */
    @Override
    public String getCollectionListItemsHtml() throws Exception {
        
        Object o = cacheIF.get(CacheKey.COUNTRY_LIST_ITEMS_HTML);
        if (o != null) {
            return (String) o; 
        }
        
        StringBuilder listItems = new StringBuilder();
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = groovyFlyDS.getConnection();

            String sql = 
                "SELECT urlAlias, name " +
                "FROM collection " +
                "WHERE collectionTypeId = 3";

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                listItems.append("<li><a href=\"[url]\">[name]</a></li>"
                        .replace("[url]", rs.getString(1))
                        .replace("[name]", rs.getString(2)));
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(stmt);
            DbUtil.closeConnection(conn);
        }
        
        cacheIF.put(CacheKey.COUNTRY_LIST_ITEMS_HTML, listItems.toString());
        
        return listItems.toString();
    }

}
