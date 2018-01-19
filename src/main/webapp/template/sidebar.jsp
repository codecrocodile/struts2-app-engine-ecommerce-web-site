<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<section id="sidebar">
                    
    <section class="widget">
        <h2>Fishing Fly Categories</h2>
        <ul class="vertical-menu">
            <s:property value="menuHtml" escapeHtml="false"/>
        </ul>
        <div class="clear"></div>
    </section>
    
    <section class="widget">
        <h2>Customer Suggestions</h2>
        <ul class="vertical-menu">
            <li><a href="/suggest-a-fly-pattern">Suggest a Fly Pattern</a></li>
        </ul>
        <div class="clear"></div>
    </section>        
    
    <!-- 
    <section class="widget">
        <s:property value="fliesByMonthHtml" escapeHtml="false"/>
        <div class="clear"></div>
    </section>
     -->
     
    <section class="widget">
        <h2>Popular Flies</h2>
        <div class="flickr_photos">
            <s:property value="popularFliesHtml" escapeHtml="false"/>
        </div>
        <div class="clear"></div>
    </section>
    
</section>

                