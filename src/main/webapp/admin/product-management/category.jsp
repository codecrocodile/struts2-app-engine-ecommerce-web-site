<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<s:if test="crudOperation == 'read'"><h1>View Category</h1></s:if>
<s:elseif test="crudOperation == 'create'"><h1>Add Category</h1></s:elseif>
<s:elseif test="crudOperation == 'update'"><h1>Edit Category</h1></s:elseif>

<s:set name="errorMap" value="getFieldErrors()" />

<s:if test="#errorMap.size > 0">
 <div class="error msg">
     Please note all the errors below and correct them
 </div>
</s:if>

<s:form cssClass="uniform">
    
    <s:hidden name="crudOperation" />
    <s:hidden name="category.categoryId" />

	<ul class="tabs">
		<li><a href="#details">Details</a></li>
		<li><a href="#shop_page">Shop Page</a></li>
	</ul>
	<div class="tabcontent">
		<div id="details">
			<dl class="inline">
				<dt><label for="category.name">Category</label></dt>
				<dd><s:textfield name="category.name" id="category.name" cssClass="medium" readonly="readOnly"/></dd>
				
			   <dt><label for="category.urlAlias">URL Alias</label></dt>
               <dd>
                   <s:textfield id="category.urlAlias" name="category.urlAlias" cssClass="medium" size="2000" readonly="readOnly" />
                   <small>This will be the [url alias] in http://[domain]/shop/category/[url alias]/products-[paging info]</small>
               </dd>

				<dt><label for="category.parentId">Parent</label></dt>
                <dd><s:select name="category.parentId" id="category.parentId" list="categories" listKey="categoryId" listValue="path" disabled="readOnly" /></dd>
                
				<dt><label for="category.description">Description</label></dt>
				<dd><s:textarea name="category.description" id="category.description" cssClass="bigger" readonly="readOnly"></s:textarea><small>for admin use only</small></dd>
			</dl>
		</div>
		<div id="shop_page">
	        <dl class="inline">
	        
	           <s:hidden name="category.page.pageId" />
	           
	           <dt><label for="category.page.title">Meta Tag Title</label></dt>
	           <dd>
	               <s:textfield id="category.page.title" name="category.page.title" cssClass="medium" size="50" readonly="readOnly" />
	               <small>Optimally no more than 70 characters in length to describe the page.</small>
	           </dd>
	
	           <dt><label for="category.page.metaKeywords">Meta Tag Keywords</label></dt>
	           <dd>
	               <s:textarea id="category.page.metaKeywords" name="category.page.metaKeywords" cssClass="medium" readonly="readOnly"></s:textarea>
	               <small>Optimally 10 or 15 terms that most accurately describe the page.</small>
	           </dd>
	           
	           <dt><label for="category.page.metaDescription">Meta Tag Description</label></dt>
	           <dd>
	               <s:textarea id="category.page.metaDescription" name="category.page.metaDescription" cssClass="medium" readonly="readOnly"></s:textarea>
	               <small>Optimally be between 150-160 characters to describe the page.</small>
	           </dd>
	           
	           <dt><label for="category.page.html">Product Description</label></dt>
	           <dd><s:textarea id="category.page.html" name="category.page.html" cssClass="bigger ckeditor" readonly="readOnly"></s:textarea></dd>
	       </dl>
		</div>
	</div>

	<div class="buttons">
        <s:if test="crudOperation == 'read'">
	        <button id="return_button" type="button" class="button">Return to Categories</button>
	    </s:if>
	    <s:else>
	        <button id="save_button" type="button" value="" class="button">Save</button>
	        <button id="cancel_button" type="button" class="button white">Cancel</button>
	    </s:else>
    </div>
    
    <s:iterator value="#errorMap">
    <s:iterator value="value">
        <div class="error msg">
            <s:property />
        </div>
    </s:iterator>
    </s:iterator>

</s:form>

<script type="text/javascript">

jQuery(document).ready(function() {

    jQuery('button[id=save_button]').click(function(e) {
        e.preventDefault();
        
        jQuery('form').get(0).setAttribute('action', 'categories-save');
        jQuery('form').submit();
        
        return true;
    });
    
    jQuery('button[id=return_button]').click(function(e) {
        e.preventDefault();
        
        jQuery('form').get(0).setAttribute('action', 'categories-list');
        jQuery('form').submit();
        
        return true;
    });
    
    jQuery('button[id=cancel_button]').click(function(e) {
        e.preventDefault();
        
        jQuery('form').get(0).setAttribute('action',  'categories-list');
        jQuery('form').submit();
        
        return true;
    });
});
</script>
