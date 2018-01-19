<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>

<h1>Groupings</h1>

<s:set name="errorMap" value="getFieldErrors()" />

<s:if test="#errorMap.size > 0">
    <div class="error msg">Please note all the errors below and correct them</div>
</s:if>

<section class="grid_12">

	<s:form id="grouping_form" action="groupings-save" method="post" enctype="multipart/form-data" cssClass="uniform">
	
	    <s:hidden id="crudOperation" name="crudOperation" />
	    <s:hidden id="productId" name="productId" />
	    <s:hidden id="product.productId" name="product.productId" />
	    <s:hidden id="product.childProductIds" name="product.childProductIds" />
	
	    <fieldset>
	        <legend>Sales Configuration</legend>
	        <dl class="inline" id="optional_configuration">
	            <dt><label for="product.productGroupingConfigId">Grouping Configuration</label></dt>
	            <dd><s:select id="product.productGroupingConfigId" name="product.productGroupingConfigId" cssClass="medium" list="#{1 : 'Non-Configurable', 2 : 'Configurable'}" disabled="readOnly" /></dd>
	        </dl>
	        <dl class="inline" id="optional_attributes">
	            <dt><label for="product.attributeId1">Attribute 1</label></dt>
	            <dd><s:select id="product.attributeId1" name="product.attributeId1" cssClass="medium" list="productAttributes" listKey="id" listValue="description" headerKey="0" headerValue="" disabled="readOnly" /></dd>
	            
	            <dt><label for="product.attributeId2">Attribute 2</label></dt>
	            <dd><s:select id="product.attributeId2" name="product.attributeId2" cssClass="medium" list="productAttributes" listKey="id" listValue="description" headerKey="0" headerValue="" disabled="readOnly" /></dd>
	            
	            <dt><label for="product.attributeId3">Attribute 3</label></dt>
	            <dd><s:select id="product.attributeId3" name="product.attributeId3" cssClass="medium" list="productAttributes" listKey="id" listValue="description" headerKey="0" headerValue="" disabled="readOnly" /></dd>
	        </dl>
	    </fieldset>
	
	    <fieldset>
	    <legend>Grouping</legend>
	        <ul class="tabs">
	          <li><a href="#master_details">Master Details</a></li>
	          <li><a href="#master_shop_page">Master Shop Page</a></li>
	        </ul>
	        
	        <div class="tabcontent">
	            <div id="master_details">
	                <dl class="inline">
						<dt><label for="product.name">Name</label></dt>
						<dd><s:textfield id="product.name" name="product.name" cssClass="medium" size="50" readonly="readOnly" /></dd>
						
						<dt><label for="product.urlAlias">URL Alias</label></dt>
                        <dd><s:textfield id="product.urlAlias" name="product.urlAlias" cssClass="medium" size="2000" readonly="readOnly"/></dd>
	
	                    <dt><label for="product.categoryId">Category</label></dt>
	                    <dd><s:select id="product.categoryId" name="product.categoryId" cssClass="medium" list="categories.{?#this.path != '-- none --'}" listKey="categoryId" listValue="name" emptyOption="true" disabled="readOnly" /></dd>
	
	                    <dt><label for="product.description">Description</label></dt>
	                    <dd>
	                        <s:textarea id="product.description" name="product.description" cssClass="medium" readonly="readOnly"></s:textarea>
	                        <small>Admin purposes only. Will not be displayed to user.</small>
	                    </dd>
	
	                    <dt><label for="large_image">Large Image 800x600</label></dt>
	                    <dd>
		                    <s:file name="largePhoto" id="large_image" disabled="readOnly" ></s:file>
		                    
		                    <div id="preview" class="previewBox">
		                     <s:if test="crudOperation == 'read' || crudOperation == 'update'">
		                         <img class="thumb" src="${product.images.smallImageUrl}" />
		                         <ul>
		                           <li>Current Product Image</li>
		                         </ul>
		                         <div class="clear"></div>
		                     </s:if>
		                    </div> 
		
		                    <small>800x600</small>
	                    </dd>
	                    
                        <dt><label for="medium_image">Medium Image 300x225</label></dt>
                        <dd>
                            <s:file name="mediumPhoto"  id="medium_image" disabled="readOnly" ></s:file>
                            <div id="mediumPreview" class="previewBox">
                               <s:if test="crudOperation == 'read' || crudOperation == 'update'">
                                      <img class="thumb" src="${product.images.mediumImageUrl}" />
                                      <ul>
                                        <li>Current Product Image</li>
                                      </ul>
                                      <div class="clear"></div>
                                  </s:if>
                            </div> 
                            <small>300x225</small>
                        </dd>
	                    
		                <dt><label for="small_image">Small Image 206x132</label></dt>
	                    <dd>
	                        <s:file name="smallPhoto"  id="small_image" disabled="readOnly" ></s:file>
	                        <div id="smallPreview" class="previewBox">
	                           <s:if test="crudOperation == 'read' || crudOperation == 'update'">
	                                  <img class="thumb" src="${product.images.smallImageUrl}" />
	                                  <ul>
	                                    <li>Current Product Image</li>
	                                  </ul>
	                                  <div class="clear"></div>
	                              </s:if>
	                        </div> 
	                        <small>206x132</small>
	                    </dd>
	                    
	                    <dt><label for="smaller_image">Smaller Image 58x58</label></dt>
                        <dd>
                            <s:file name="smallerPhoto"  id="smaller_image" disabled="readOnly" ></s:file>
                            <div id="smallerPreview" class="previewBox">
                               <s:if test="crudOperation == 'read' || crudOperation == 'update'">
                                      <img class="thumb" src="${product.images.smallerImageUrl}" />
                                      <ul>
                                        <li>Current Product Image</li>
                                      </ul>
                                      <div class="clear"></div>
                                  </s:if>
                            </div> 
                            <small>58x58</small>
                        </dd>
	
	                    <dt><label for="product.images.altTagDescription">Image Alt Tag Description</label></dt>
	                    <dd><s:textfield id="product.images.altTagDescription" name="product.images.altTagDescription" cssClass="medium" size="50" readonly="readOnly" /></dd>
	                    
	                    <dt><label for="product.price">Price</label></dt>
                        <dd><s:textfield id="product.price" name="product.price" cssClass="small" size="50" readonly="readOnly" /></dd>
	                </dl>
	            </div>
	            <div id="master_shop_page">
	                <dl class="inline">
	                
	                    <s:hidden id="product.page.pageId" name="product.page.pageId" />
	                
	                    <dt><label for="product.page.title">Meta Tag Title</label></dt>
	                    <dd>
	                        <s:textfield id="product.page.title" name="product.page.title" cssClass="medium" size="50" readonly="readOnly"/>
	                        <small>Optimally no more than 70 characters in length to describe the page.</small>
	                    </dd>
	
	                    <dt><label for="product.page.metaKeywords">Meta Tag Keywords</label></dt>
	                    <dd>
	                        <s:textarea id="product.page.metaKeywords" name="product.page.metaKeywords" cssClass="medium" readonly="readOnly"></s:textarea>
	                        <small>Optimally 10 or 15 terms that most accurately describe the page.</small>
	                    </dd>
	                    
	                    <dt><label for="product.page.metaDescription">Meta Tag Description</label></dt>
	                    <dd>
	                        <s:textarea id="product.page.metaDescription" name="product.page.metaDescription" cssClass="medium" readonly="readOnly"></s:textarea>
	                        <small>Optimally be between 150-160 characters to describe the page.</small>
	                    </dd>
	                    
	                    <dt><label for="product.page.html">Product Description</label></dt>
	                    <dd><s:textarea id="product.page.html" name="product.page.html" cssClass="bigger ckeditor" disabled="readOnly" ></s:textarea></dd>
	                </dl>
	            </div>
	        </div>
	    </fieldset>
	</s:form>
	
    <s:iterator value="#errorMap">
    <s:iterator value="value">
        <div class="error msg">
            <s:property />
        </div>
    </s:iterator>
    </s:iterator>
    
    <h2>Products in Grouping</h2>
    
    <table id="grouped_products_table" class="gtable">
        <thead>
            <tr>
                <th>Product Name</th>
                <th>Image</th>
                <th>Attributes</th>
                <th>Price</th>
                <th>Storage Location</th>
                <th>Stock Level</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <s:iterator value="product.products">
                <tr>
                    <td><s:property value="name" /></td>
                    <td><img src="<s:property value="images.smallImageUrl" />=s32" /></td>
                    <td><s:property value="attribute1" /><s:property value="',' + attribute2" /><s:property value="',' + attribute3" /></td>
                    <td><s:property value="price" /></td>
                    <td><s:property value="storeageLocation" /></td>
                    <td><s:property value="stockLevel" /></td>
                    <td>
                        <input type="image" src="images/icons/cross.png" alt="delete from grouped products" onclick="deleteFromGroupedProducts(event, '<s:property value="productId" />')" />
                    </td>
                </tr>
            </s:iterator>
        </tbody>
    </table>
    
    <%-- if viewing a grouping then don't show the search table --%>
    <s:if test="crudOperation != 'read'">
    
        <script type="text/javascript">
            jQuery.subscribe('complete', function(event, data) {
                initProductArray(event.originalEvent.request.responseText);
            });
            
            jQuery.subscribe('error', function(event, data) {
                jQuery('#dialog').html("There seems to have been an problem!");
                jQuery('#dialog').dialog('open');
            });
        </script>

	    <h2>Search for Products</h2>
	    <s:form id="search_form" action="product-search" cssClass="uniform">
	    
	        <%-- these will be set by js before the ajax call is made --%>
	        <s:hidden id="productSearchQuery.attributeId1" name="productSearchQuery.attributeId1" />
	        <s:hidden id="productSearchQuery.attributeId2" name="productSearchQuery.attributeId2" />
	        <s:hidden id="productSearchQuery.attributeId3" name="productSearchQuery.attributeId3" />
	        <s:hidden id="productSearchQuery.productIdExcludeString" name="productSearchQuery.productIdExcludeString" />
            
	        <div class="searchConditionsBox">
	            <div>
	                <label for="search_name">Name</label>
	                <s:textfield id="search_name" name="productSearchQuery.name" cssClass="small" />               
	            </div>
	            <div>
	                <label for="productSearchQuery.categoryId">Category</label>
	                <s:select id="productSearchQuery.categoryId" name="productSearchQuery.categoryId" cssClass="medium" list="categories.{?#this.path != '-- none --'}" listKey="categoryId" listValue="name" headerKey="0" headerValue="" disabled="readOnly" />       
	            </div>
	            
	            <div>
	                <sj:submit id="search_submit" 
	                 value="Search" 
	                 cssClass="button green" 
	                 resetForm="false" 
	                 dataType="json"
	                 onclick="setSearchFormVariables()"
	                 onSuccessTopics="complete"
	                 onErrorTopics="error"
	                 targets="XXX"
	                 indicator="indicator"/>
	            </div>
	            
	            <div class="clear"></div>
	        </div>
	        <div class="clear"></div>

        </s:form>
	    <img id="indicator" src="images/indicator.gif" alt="Loading..." style="display:none"/> 
	    <table id="datatable" class="gtable">
	        <thead>
	            <tr>
	                <th>Product Name</th>
	                <th>Image</th>
	                <th>Attributes</th>
	                <th>Price</th>
	                <th>Storage Location</th>
	                <th>Stock Level</th>
	                <th>Actions</th>
	            </tr>
	        </thead>
	        <tbody>
	        </tbody>
	    </table>
    </s:if>
<div class="clear"></div>    
<div class="buttons" style="margin-top: 20px;">
    <s:if test="crudOperation == 'read'">
        <button id="return_button" type="button" class="button">Return to Products</button>
    </s:if>
    <s:else>
        <button id="save_button" type="button" class="button">Save</button>
        <button id="cancel_button" type="button" class="button white">Cancel</button>
    </s:else>
</div>

</section>