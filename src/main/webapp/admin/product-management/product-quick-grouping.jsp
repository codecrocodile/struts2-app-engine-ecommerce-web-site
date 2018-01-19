<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<h1>Product and Grouping</h1>

<s:set name="errorMap" value="getFieldErrors()" />

<s:if test="#errorMap.size > 0">
    <div class="error msg">Please note all the errors below and correct them</div>
</s:if>

<s:form id="master_form" action="#" cssClass="uniform">

    <s:hidden id="crudOperation" name="crudOperation" />
    <s:hidden id="productId" name="productId" value="-1"/>

    <fieldset>
        <legend>Sales Configuration</legend>
        <dl class="inline" id="optional_configuration">
            <dt><label for="attribute_configuration">Grouping Configuration</label></dt>
            <dd><s:select id="attribute_configuration" name="attribute_configuration" cssClass="medium" list="#{1 : 'Non-Configurable', 2 : 'Configurable'}" /></dd>
        </dl>
        
        <p><s:property value="productAttributes.size()"/> </p>
        
        <dl class="inline" id="optional_attributes" style="display: none">
            <dt><label for="attribute_list_1">Attribute 1</label></dt>
            
            <dd>
            <select name="attribute_list_1" id="attribute_list_1" class="medium">
            	<option value=""></option>
	            <s:iterator value="productAttributes">
		        	 <option value="<s:property value="id"/>"><s:property value="description"/></option>
	        	</s:iterator>
        	</select>
			</dd>
           
            <dt><label for="attribute_list_2">Attribute 2</label></dt>
                        <dd>
            <select name="attribute_list_2" id="attribute_list_2" class="medium">
            <option value=""></option>
	            <s:iterator value="productAttributes">
		        	 <option value="<s:property value="id"/>"><s:property value="description"/></option>
	        	</s:iterator>
        	</select>
			</dd>
            
            <dt><label for="attribute_list_3">Attribute 3</label></dt>
                        <dd>
            <select name="attribute_list_3" id="attribute_list_3" class="medium">
            <option value=""></option>
	            <s:iterator value="productAttributes">
		        	 <option value="<s:property value="id"/>"><s:property value="description"/></option>
	        	</s:iterator>
        	</select>
			</dd>
        </dl>
    </fieldset>

    <fieldset>
     <legend>Product</legend>
        <ul class="tabs">
          <li><a href="#master_details">Master Details</a></li>
          <li><a href="#master_shop_page">Master Shop Page</a></li>
        </ul>
        
        <div class="tabcontent">
            <div id="master_details">
                <dl class="inline">
                
                    <dt><label for="category_list">Category</label></dt>
                    <dd><s:select id="category_list" name="category_list" cssClass="medium" list="categories.{?#this.path != '-- none --'}" listKey="categoryId" listValue="name" emptyOption="true" /></dd>
                    
                    <dt><label for="name">Name</label></dt>
                    <dd><s:textfield id="name" name="name" cssClass="medium" size="50" /></dd>
                    
                    <dt><label for="urlAlias">URL Alias</label></dt>
                    <dd><s:textfield id="urlAlias" name="urlAlias" cssClass="medium" size="2000" readonly="readOnly"/></dd>

                    <dt><label for="description">Description</label></dt>
                    <dd>
                        <s:textarea id="description" name="description" cssClass="medium"></s:textarea>
                        <small>Admin purposes only. Will not be displayed to user.</small>
                    </dd>
                    
                    <%-- Product Images --%>

                    <dt><label for="large_image">Large Image 800x600</label></dt>
                    <dd>
                       <s:file id="large_image" name="large_image" />
                       <div id="preview" class="previewBox"></div> 
                       <small>800x600</small>
                    </dd>
                    
                    <dt><label for="medium_image">Medium Image 300x225</label></dt>
                    <dd>
                       <s:file id="medium_image" name="medium_image" />
                       <div id="mediumPreview" class="previewBox"></div> 
                       <small>300x225</small>
                    </dd>
                    
                    <dt><label for="small_image">Small Image 206x132</label></dt>
                    <dd>
                       <s:file id="small_image" name="small_image" />
                       <div id="smallPreview" class="previewBox"></div> 
                       <small>206x132</small>
                    </dd>                                        
                    
                    <dt><label for="smaller_image">Small Image 58x58</label></dt>
                    <dd>
                       <s:file id="smaller_image" name="smaller_image" />
                       <div id="smallerPreview" class="previewBox"></div> 
                       <small>58x58</small>
                    </dd>

                    
                    <%-- End Product Images --%>
                    
                    <dt><label for="image_alt">Image Alt Tag</label></dt>
                    <dd><s:textfield id="image_alt" name="image_alt" cssClass="medium" size="50" /></dd>
                    
                    <dt><label for="price">Price</label></dt>
                    <dd><s:textfield id="price" name="price" cssClass="small" size="50" readonly="readOnly" value="0.00" /></dd>
                </dl>
            </div>
            <div id="master_shop_page">
                <dl class="inline">
                
                    <dt><label for="meta_tag_title">Meta Tag Title</label></dt>
                    <dd>
                        <s:textfield id="meta_tag_title" name="meta_tag_title" cssClass="medium" size="50" />
                        <small>Optimally no more than 70 characters in length to describe the page.</small>
                    </dd>

                    <dt><label for="meta_tag_keywords">Meta Tag Keywords</label></dt>
                    <dd>
                        <s:textarea id="meta_tag_keywords" name="meta_tag_keywords" cssClass="medium"></s:textarea>
                        <small>Optimally 10 or 15 terms that most accurately describe the page.</small>
                    </dd>
                    
                    <dt><label for="meta_tag_description">Meta Tag Description</label></dt>
                    <dd>
                        <s:textarea id="meta_tag_description" name="meta_tag_description" cssClass="medium"></s:textarea>
                        <small>Optimally be between 150-160 characters to describe the page.</small>
                    </dd>
                    
                    <dt><label for="product_description_wysiwyg">Product Description</label></dt>
                    <dd><textarea id="product_description_wysiwyg" class="bigger ckeditor"></textarea></dd>
                </dl>
            </div>
        </div>
    </fieldset>
    
</s:form><!-- end of master_form -->

<s:form id="product_unit_form" action="#" cssClass="uniform">

    <fieldset>
        <legend>Product Units</legend>
        
        <div id="product_unit" style="width: 90%; margin: auto;">
            <div style="float: right; padding: 5px 0px 10px 0px">
                 <button id="create_unit_btn" class="button white">Create Unit</button>
            </div>
            <div class="clear"></div>

            <fieldset id="product_unit_details_panel" style="background-color: #eef; display: none">
                <ul class="tabs">
                    <li><a href="#product_unit_details">Details</a></li>
                    <li><a href="#product_unit_page">Shop page</a></li>
                </ul>
                <div class="tabcontent">
                    <div id="product_unit_details">
                    
                        <s:hidden id="product_unit_id" value="-1" />
                        <s:hidden id="product_unit_temp_id" value="-1" />
                        <s:hidden id="isMasterProduct" value="false" />
                    
                        <dl class="inline" id="product_unit_optional_attributes_values">
                            <dt><label for="product_unit_attribute_value_list_1">Attribute Value 1</label></dt>
                            <dd><s:select id="product_unit_attribute_value_list_1" cssClass="medium" list="#{-1 : ''}" /></dd>
                            <dt><label for="product_unit_attribute_value_list_2">Attribute Value 2</label></dt>
                            <dd><s:select  id="product_unit_attribute_value_list_2" cssClass="medium" list="#{-1 : ''}" /> </dd>
                            <dt><label for="product_unit_attribute_value_list_3">Attribute Value 3</label></dt>
                            <dd><s:select id="product_unit_attribute_value_list_3" cssClass="medium" list="#{-1 : ''}" />                       
                        </dl>
                        <dl class="inline">
                            <dt><label for="product_unit_name">Name</label></dt>
                            <dd><s:textfield id="product_unit_name" name="product_unit_name" cssClass="medium" size="50" /></dd>
                            
                            <dt><label for="product_unit_urlAlias">URL Alias</label></dt>
                            <dd><s:textfield id="product_unit_urlAlias" name="product_unit_urlAlias" cssClass="medium" size="2000" readonly="readOnly"/></dd>

                            <%-- Product Images --%>
                            <dt><label for="product_unit_large_image">Large Image</label></dt>
                            <dd>
                              <input type="file" id="product_unit_large_image" />
                              <div id="product_unit_preview" class="previewBox"></div> 
                              <small>Set this if different from master details.</small>
                            </dd>
                            
                            <dt><label for="product_unit_medium_image">Medium Image</label></dt>
                            <dd>
                              <input type="file" id="product_unit_medium_image" />
                              <div id="product_unit_medium_preview" class="previewBox"></div> 
                              <small>Set this if different from master details.</small>
                            </dd>                            
                            
                            <dt><label for="product_unit_small_image">Small Image</label></dt>
                            <dd>
                              <input type="file" id="product_unit_small_image" />
                              <div id="product_unit_small_preview" class="previewBox"></div> 
                              <small>Set this if different from master details.</small>
                            </dd>
                            
                            <dt><label for="product_unit_smaller_image">Smaller Image</label></dt>
                            <dd>
                              <input type="file" id="product_unit_smaller_image" />
                              <div id="product_unit_smaller_preview" class="previewBox"></div> 
                              <small>Set this if different from master details.</small>
                            </dd>                            
                            
                            <%-- End Product Images --%>
                            
                            <dt><label for="product_unit_image_alt">Image Alt Tag</label></dt>
                            <dd><s:textfield id="product_unit_image_alt" cssClass="medium" size="50" /></dd>

                            <dt><label for="product_unit_supplier_list">Supplier</label></dt>
                            <dd><s:select id="product_unit_supplier_list" name="product_unit_supplier_list" cssClass="medium" list="suppliers" listKey="supplierId" listValue="companyName" emptyOption="true" /></dd>

                            <dt><label for="product_unit_sku">SKU</label></dt>
                            <dd>
                                <s:textfield id="product_unit_sku" name="product_unit_sku" cssClass="medium"size="50" />
                                <small>Stock-keeping Unit. Is unique for different versions of the same product.</small> 
                                <small>"Supplier Short Code" + "-" + "First Letters of Name" + " - " + "Size" + "Random Five Char"</small>
                            </dd>

                            <dt><label for="product_unit_price">Price</label></dt>
                            <dd><s:textfield id="product_unit_price" name="product_unit_price" cssClass="small" size="50" /></dd>

                            <dt><label for="product_unit_price_rule_list">Price Rule</label></dt>
                            <dd><s:select id="product_unit_price_rule_list" name="product_unit_price_rule_list" cssClass="medium" list="priceRules" listKey="priceRuleId" listValue="name" /></dd>

                            <dt><label for="product_unit_vat_rate_list">Vat Rate</label></dt>
                            <dd><s:select id="product_unit_vat_rate_list" name="product_unit_vat_rate_list" cssClass="medium" list="vatRates" listKey="id" listValue="description" /></dd>

                            <dt><label for="product_unit_storeage_location">Storage Location</label></dt>
                            <dd><s:textfield id="product_unit_storeage_location" name="product_unit_storeage_location" cssClass="small" size="50" />
                            </dd>

                            <dt><label for="product_unit_stock_level">Stock Level</label></dt>
                            <dd>
                                 <s:textfield id="product_unit_stock_level" name="product_unit_stock_level" cssClass="small" size="10"/>
                                 
                                 <button id="minus_one" type="button" class="button smaller red">&nbsp;- 1</button>
                                 <button id="plus_one" type="button" class="button smaller green">&nbsp;+ 1</button>
                                 <button id="minus_twelve" type="button" class="button smaller red">- 12</button>
                                 <button id="plus_twelve" type="button" class="button smaller green">+ 12</button>
                            </dd>

                            <dt><label for="product_unit_status_list">Status</label></dt>
                            <dd><s:select id="product_unit_status_list" name="product_unit_status_list" cssClass="medium" list="productStatuses" listKey="id" listValue="description" emptyOption="true" /></dd>
                        </dl>
                        
                        <div style="float: right; padding: 5px 0px 5px 0px">
                            <button id="add_unit_bnt" class="button white">Ok</button>
                            <button id="cancel_unit_btn" class="button white">Cancel</button>
                        </div>
                        
                    </div>

                    <div id="product_unit_page">
                        <dl class="inline">
                            <dt><label for="product_unit_meta_tag_title">Meta Tag Title</label></dt>
                            <dd>
                                <s:textfield id="product_unit_meta_tag_title" name="product_unit_meta_tag_title" cssClass="medium" size="50" />
                                <small>Optimally no more than 70 characters in length to describe the page.</small>
                            </dd>
        
                            <dt><label for="product_unit_meta_tag_keywords">Meta Tag Keywords</label></dt>
                            <dd>
                                <s:textarea id="product_unit_meta_tag_keywords" name="product_unit_meta_tag_keywords" cssClass="medium"></s:textarea>
                                <small>Optimally 10 or 15 terms that most accurately describe the the page.</small>
                            </dd>
                            
                            <dt><label for="product_unit_meta_tag_description">Meta Tag Description</label></dt>
                            <dd>
                                <s:textarea id="product_unit_meta_tag_description" name="product_unit_meta_tag_description" cssClass="medium"></s:textarea>
                                <small>Optimally be between 150-160 characters to describe the page.</small>
                            </dd>
                            
                            <dt><label for="product_unit_product_description_wysiwyg">Product Description</label></dt>
                            <dd><textarea id="product_unit_product_description_wysiwyg" class="bigger ckeditor"></textarea></dd>
                        </dl>
                    </div>
                    
                </div>
            </fieldset>

               <table id="datatable" class="gtable" style="padding-left: 200px">
                <thead>
                    <tr>
                        <th>Product Unit Name</th>
                        <th>SKU</th>
                        <th>Price</th>
                        <th>Stock Level</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody></tbody>
            </table>
        </div>
        <div class="clear"></div>
    </fieldset>
</s:form><!-- end of product_unit_form -->

    
    <s:iterator value="#errorMap">
    <s:iterator value="value">
        <div class="error msg">
            <s:property />
        </div>
    </s:iterator>
    </s:iterator>

<s:form action="products-saveProductsAndGrouping.action" id="product_form">
    <s:hidden id="productXml" name="productXml" />
</s:form>

<div class="buttons">
    <s:if test="crudOperation == 'read'">
        <button id="return_button" type="button" class="button">Return to Products</button>
    </s:if>
    <s:else>
        <button id="save_button" type="button" class="button">Save</button>
        <button id="cancel_button" type="button" class="button white">Cancel</button>
    </s:else>
</div>
