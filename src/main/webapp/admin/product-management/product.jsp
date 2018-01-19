<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<s:if test="crudOperation == 'read'"><h1>View Product</h1></s:if>
<s:elseif test="crudOperation == 'create'"><h1>Add Product</h1></s:elseif>
<s:elseif test="crudOperation == 'update'"><h1>Edit Product</h1></s:elseif>

<s:set name="errorMap" value="getFieldErrors()" />

<s:if test="#errorMap.size > 0">
	<div class="error msg">Please note all the errors below and correct them</div>
</s:if>

<s:form id="product_form" action="products-save" method="post" enctype="multipart/form-data" cssClass="uniform">
	<fieldset>
		<legend>Product</legend>

		<ul class="tabs">
			<li><a href="#product_unit_details">Details</a></li>
			<li><a href="#product_unit_page">Shop page</a></li>
		</ul>
				
		<div class="tabcontent">
			<div id="product_unit_details">
			
			    <s:hidden name="product.productId" id="product_productId"/>
			    
			    <dl class="inline">
				    <dt><label for="product.categoryId">Category</label></dt>
                    <dd><s:select id="product.categoryId" name="product.categoryId" cssClass="medium" list="categories.{?#this.path != '-- none --'}" listKey="categoryId" listValue="name" emptyOption="true" disabled="readOnly" /></dd>
                    
                    <dt><label for="product.attributeId1">Attribute 1</label></dt>
                    <dd><s:select id="product.attributeId1" name="product.attributeId1" cssClass="medium" list="productAttributes" listKey="id" listValue="description"  headerKey="0" headerValue="" disabled="readOnly" /></dd>
        
                    <dt><label for="product.attributeId2">Attribute 2</label></dt>
                    <dd><s:select id="product.attributeId2" name="product.attributeId2" cssClass="medium" list="productAttributes" listKey="id" listValue="description" headerKey="0" headerValue="" disabled="readOnly" /></dd>
        
                    <dt><label for="product.attributeId3">Attribute 3</label></dt>
                    <dd><s:select id="product.attributeId3" name="product.attributeId3" cssClass="medium" list="productAttributes" listKey="id" listValue="description" headerKey="0" headerValue="" disabled="readOnly" /></dd>
	
                    <dt><label for="product.attributeValueId1">Attribute Value 1</label></dt>
                    <dd><s:select id="product.attributeValueId1" name="product.attributeValueId1" cssClass="medium" list="#{-1 : ''}"  headerKey="0" headerValue="" disabled="readOnly" /></dd>
                    
                    <dt><label for="product.attributeValueId2">Attribute Value 2</label></dt>
                    <dd><s:select  id="product.attributeValueId2" name="product.attributeValueId2" cssClass="medium" list="#{-1 : ''}"  headerKey="0" headerValue="" disabled="readOnly" /> </dd>
                    
                    <dt><label for="product.attributeValueId3">Attribute Value 3</label></dt>
                    <dd><s:select id="product.attributeValueId3" name="product.attributeValueId3" cssClass="medium" list="#{-1 : ''}"  headerKey="0" headerValue="" disabled="readOnly" />	
		                     				    
		            <dt><label for="product.name">Name</label></dt>
		            <dd><s:textfield id="product.name" name="product.name" cssClass="medium" size="50" readonly="readOnly" /></dd>
		            
		            <dt><label for="product.urlAlias">URL Alias</label></dt>
                    <dd><s:textfield id="product.urlAlias" name="product.urlAlias" cssClass="medium" size="2000" readonly="readOnly"/></dd>
		
		            <dt><label for="product.description">Description</label></dt>
                    <dd>
                        <s:textarea id="product.description" name="product.description" cssClass="medium" readonly="readOnly"></s:textarea>
                        <small>Admin purposes only. Will not be displayed to user.</small>
                    </dd>
		
		            <%-- Product Images --%>
		            
					<dt><label for="product_unit_large_image">Large Image 800x600</label></dt>
					<dd>
                        <s:file name="largePhoto"  id="product_unit_large_image" disabled="readOnly" ></s:file>
	                    <div id="product_unit_preview" class="previewBox">
	                       <s:if test="crudOperation == 'read' || crudOperation == 'update'">
	                              <img class="thumb" src="${product.images.smallImageUrl}" />
	                              <ul>
	                                <li>Current Product Image</li>
	                              </ul>
	                              <div class="clear"></div>
	                          </s:if>
	                    </div> 
                    </dd>
                    
                    <dt><label for="product_unit_medium_image">Medium Image 300x225</label></dt>
                    <dd>
                        <s:file name="mediumPhoto"  id="product_unit_medium_image" disabled="readOnly" ></s:file>
                        <div id="product_unit_medium_preview" class="previewBox">
                           <s:if test="crudOperation == 'read' || crudOperation == 'update'">
                                  <img class="thumb" src="${product.images.mediumImageUrl}" />
                                  <ul>
                                    <li>Current Product Image</li>
                                  </ul>
                                  <div class="clear"></div>
                              </s:if>
                        </div> 
                    </dd>
                    
                    <dt><label for="product_unit_small_image">Small Image 200x132</label></dt>
                    <dd>
                        <s:file name="smallPhoto"  id="product_unit_small_image" disabled="readOnly" ></s:file>
                        <div id="product_unit_small_preview" class="previewBox">
                           <s:if test="crudOperation == 'read' || crudOperation == 'update'">
                                  <img class="thumb" src="${product.images.smallImageUrl}" />
                                  <ul>
                                    <li>Current Product Image</li>
                                  </ul>
                                  <div class="clear"></div>
                              </s:if>
                        </div> 
                    </dd>
                    
                    <dt><label for="product_unit_smaller_image">Smaller Image 58x58</label></dt>
                    <dd>
                        <s:file name="smallerPhoto"  id="product_unit_smaller_image" disabled="readOnly" ></s:file>
                        <div id="product_unit_smaller_preview" class="previewBox">
                           <s:if test="crudOperation == 'read' || crudOperation == 'update'">
                                  <img class="thumb" src="${product.images.smallerImageUrl}" />
                                  <ul>
                                    <li>Current Product Image</li>
                                  </ul>
                                  <div class="clear"></div>
                              </s:if>
                        </div> 
                    </dd>                    
                    
                    <%-- This will be the alt text for all the images --%>
                    
                    <dt><label for="product.images.altTagDescription">Image Alt Tag</label></dt>
                    <dd><s:textfield id="product.images.altTagDescription" name="product.images.altTagDescription" cssClass="medium" size="50" readonly="readOnly"/></dd>
                    
                    <%-- End Product Images --%>
		
					<dt><label for="product.supplierId">Supplier</label></dt>
					<dd><s:select id="product.supplierId" name="product.supplierId" cssClass="medium" list="suppliers" listKey="supplierId" listValue="companyName" emptyOption="true" disabled="readOnly" /></dd>
		
					<dt><label for="product.sku">SKU</label></dt>
					<dd>
						<s:textfield id="product.sku" name="product.sku" cssClass="medium"size="50" readonly="readOnly"/>
						<small>Stock-keeping Unit. Is unique for different versions of the same product.</small> 
						<small>"Supplier Short Code" + "-" + "First Letters of Name" + " - " + "Size" + "Random Five Char"</small>
					</dd>
		
					<dt><label for="product.price">Price</label></dt>
					<dd><s:textfield id="product.price" name="product.price" cssClass="small" size="50" readonly="readOnly"/></dd>
		
					<dt><label for="product.priceRuleId">Price Rule</label></dt>
					<dd><s:select id="product.priceRuleId" name="product.priceRuleId" cssClass="medium" list="priceRules" listKey="priceRuleId" listValue="name" disabled="readOnly" /></dd>
		
					<dt><label for="product.vatRuleId">Vat Rate</label></dt>
					<dd><s:select id="product.vatRuleId" name="product.vatRuleId" cssClass="medium" list="vatRates" listKey="id" listValue="description" disabled="readOnly" /></dd>
		
					<dt><label for="product.storeageLocation">Storage Location</label></dt>
					<dd><s:textfield id="product.storeageLocation" name="product.storeageLocation" cssClass="small" size="50" readonly="readOnly" />
					</dd>
		
					<dt><label for="product.stockLevel">Stock Level</label></dt>
					<dd>
					     <s:textfield id="product.stockLevel" name="product.stockLevel" cssClass="small" size="10" readonly="readOnly" />
					     
					     <s:if test="readOnly == false">
						     <button id="minus_one" type="button" class="button smaller red" >&nbsp;- 1</button>
	                         <button id="plus_one" type="button" class="button smaller green">&nbsp;+ 1</button>
	                         <button id="minus_twelve" type="button" class="button smaller red">- 12</button>
	                         <button id="plus_twelve" type="button" class="button smaller green">+ 12</button>
					     </s:if>
					     
					</dd>
		
					<dt><label for="product.statusId">Status</label></dt>
					<dd><s:select id="product.statusId" name="product.statusId" cssClass="medium" list="productStatuses" listKey="id" listValue="description" emptyOption="true" disabled="readOnly" /></dd>
				</dl>
			</div>
		
			<div id="product_unit_page">
				<dl class="inline">
				
                    <s:hidden id="product.page.pageId" name="product.page.pageId" />
			    
					<dt><label for="product.page.title">Meta Tag Title</label></dt>
					<dd>
					    <s:textfield id="product.page.title" name="product.page.title" cssClass="medium" size="50" readonly="readOnly" />
					    <small>Optimally no more than 70 characters in length to describe the page.</small>
					</dd>
					
					<dt><label for="product.page.metaKeywords">Meta Tag Keywords</label></dt>
					<dd>
					    <s:textarea id="product.page.metaKeywords" name="product.page.metaKeywords"  cssClass="medium" readonly="readOnly"></s:textarea>
					    <small>Optimally 10 or 15 terms that most accurately describe the the page.</small>
					</dd>
					
					<dt><label for="product.page.metaDescription">Meta Tag Description</label></dt>
					<dd>
					    <s:textarea id="product.page.metaDescription" name="product.page.metaDescription" cssClass="medium" readonly="readOnly"></s:textarea>
					    <small>Optimally be between 150-160 characters to describe the page.</small>
					</dd>
					
					<dt><label for="product.page.html">Product Description</label></dt>
					<dd><s:textarea id="product.page.html" name="product.page.html" cssClass="bigger ckeditor" readonly="readOnly"></s:textarea></dd>
				</dl>
			</div>
		</div>
	</fieldset>
</s:form><!-- end of product_form -->

    <s:iterator value="#errorMap">
    <s:iterator value="value">
        <div class="error msg">
            <s:property />
        </div>
    </s:iterator>
    </s:iterator>
	
<div class="buttons">
	<s:if test="crudOperation == 'read'">
		<button id="return_button" type="button" class="button">Return to Products</button>
	</s:if>
	<s:else>
		<button id="save_button" type="button" class="button">Save</button>
		<button id="cancel_button" type="button" class="button white">Cancel</button>
	</s:else>
</div>
