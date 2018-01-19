<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>

<div class="grid_12">
    <header class="page">
        <h1><s:property value="category.name" /></h1>
    </header>
</div>

<div class="clear"></div>

<section class="product-listing">

    <section id="category_description">
        <s:property value="page.html" escapeHtml="false"/>
    </section>
    
	<ul id="portfolio" class="portfolio">
		<s:iterator value="productSummaries">
	         <li>
	           <div class="portfolio_item_inner">
	                   <p style="float:right; font-size: 12px; color: #667700; margin: 5px;"><s:property value="priceString" /></p>
    	               <h3><s:property value="name" /></h3>
	               <hr />
	               <div class="img_pf_hover">
	                   <img src="<s:property value="images.smallImageUrl" />" class="col4" alt="" />
	                   <a href="<s:property value="images.largeImageUrl" />" class="crbox" title="<s:property value="name" />"><span class="img_pf_icon zoom_in"></span></a>
	                </div>
	                <hr />
	                <div class="portfolio_meta">
						<s:url id="productUrl" action="product-quick-display">
						    <s:param name="productId"><s:property value="productId" /></s:param>
						</s:url>
						<sj:a 
						    openDialog="quickAddDialog"
						    href="%{productUrl}"
						    cssClass="buy"
						>Add</sj:a>
						<a href="/shop/product<s:property value="urlAlias" />" class="info">Info</a>
	                </div>
	            </div>  
	        </li>
		</s:iterator>
	</ul>
	<div class="clear"></div>
	
	<section id="list_pagination">
        <s:if test="currentPage == 1">
            <span class="disabled_list_pagination">Prev</span>
        </s:if>
        <s:else>
            <a href="<s:property value="page.urlAlias" />/page-<s:property value="currentPage - 1" />">Prev</a>           
        </s:else>
        
        <s:bean name="org.apache.struts2.util.Counter" var="counter">
           <s:param name="last" value="noOfPagesInCategory"/>
        </s:bean>
        <s:iterator value="#counter" status="itStatus">
             <s:if test="currentPage == #itStatus.count">
                 <span class="active_list_link"><s:property value="#itStatus.count" /></span>
             </s:if>
             <s:else>
                 <a href="<s:property value="page.urlAlias" />/page-<s:property value="#itStatus.count" />"><s:property value="#itStatus.count" /></a>
             </s:else>
        </s:iterator>

        <s:if test="currentPage == noOfPagesInCategory">
          <span class="disabled_list_pagination">Next</span>
        </s:if>
        <s:else>
          <a href="<s:property value="page.urlAlias" />/page-<s:property value="currentPage + 1" />">Next</a>        
        </s:else>
    </section>

	<sj:dialog 
	   id="quickAddDialog" 
	   autoOpen="false" 
	   modal="false" 
	   minHeight="395"
	   width="450"
	   showEffect="scale" 
       hideEffect="drop"
       resizable="false"  
       onBeforeTopics="beforeOpenTopic"
       onCloseTopics="onCloseTopic"   
	   >
	   </sj:dialog>

	   
	<script>
	    $.subscribe('beforeOpenTopic', function(event, data) {
             $('#quickAddDialog').html('<div><img src="/images/indicator-brown-bg.gif" alt="Loading..." style="float:left; width: 32px; height: 32px; margin-left: 5px; margin-right: 10px;"/> <p>Loading product page...</p></div>');	    	
		     $('#quickAddDialog').parents(".ui-dialog").css("background", "#b5b5b5");     
	         $('#quickAddDialog').parents(".ui-dialog:first").find(".ui-dialog-content").css("padding-top", 3); 
	         $('#quickAddDialog').parents(".ui-dialog:first").find(".ui-dialog-content").css("padding-bottom", 0); 
	         $('#quickAddDialog').parents(".ui-dialog:first").find(".ui-dialog-content").css("padding-left", 0); 
	         $('#quickAddDialog').parents(".ui-dialog:first").find(".ui-dialog-content").css("padding-right", 0); 
	    });
	    $.subscribe('onCloseTopic', function(event, data) {
            $('#product_purchase_success_msg').html('<strong>Note:</strong> Shopping with Groovy Fly is fast, convenient and most importantly secure!' );
            $('#product_purchase_success_msg').removeClass('error_msg');
            $('#product_purchase_success_msg').addClass('note_msg');
       });
    </script>
</section>