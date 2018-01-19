<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<s:if test="crudOperation == 'read'"><h1>View Supplier</h1></s:if>
<s:elseif test="crudOperation == 'create'"><h1>Add Supplier</h1></s:elseif>
<s:elseif test="crudOperation == 'update'"><h1>Edit Supplier</h1></s:elseif>

<s:set name="errorMap" value="getFieldErrors()" />

<s:if test="#errorMap.size > 0">
 <div class="error msg">
     Please note all the errors below and correct them
 </div>
</s:if>
        
<s:form id="suppplier_form" action="#" cssClass="uniform" theme="simple">

    <s:hidden name="crudOperation" />
    <s:hidden name="supplier.supplierId" />
    <s:hidden name="supplier.retired" />
    
    <fieldset>
        <legend>Basic Details</legend>
        <dl class="inline">
            <dt><label for="short_code">Short Code *</label></dt>
            <dd><s:textfield name="supplier.shortCode" id="short_code" cssClass="smaller required" maxlength="4" readonly="readOnly" /></dd>
            
            <dt><label for="company_name">Company Name *</label></dt>
            <dd><s:textfield name="supplier.companyName" id="company_name" cssClass="medium required" maxlength="100" readonly="readOnly" /></dd>
        </dl>
    </fieldset>
    
    <s:iterator value="#errorMap['supplier.shortCode']">
        <div class="error msg">
            <s:property />
        </div>
    </s:iterator>
    <s:iterator value="#errorMap['supplier.companyName']">
        <div class="error msg">
            <s:property />
        </div>
    </s:iterator>
    
    <fieldset>
        <legend>Company Address</legend>
        <dl class="inline">
            <dt><label for="address_line_1">Line 1</label></dt>
            <dd><s:textfield name="supplier.address.line1" id="address_line_1" cssClass="medium required" maxlength="50" readonly="readOnly" /></dd>
            
            <dt><label for="address_line_2">Line 2</label></dt>
            <dd><s:textfield name="supplier.address.line2" id="address_line_2" cssClass="medium required" maxlength="50" readonly="readOnly" /></dd>
            
            <dt><label for="address_line_3">Line 3</label></dt>
            <dd><s:textfield name="supplier.address.line3" id="address_line_3" cssClass="medium required" maxlength="50" readonly="readOnly" /></dd>
            
            <dt><label for="address_line_4">Line 4</label></dt>
            <dd><s:textfield name="supplier.address.line4" id="address_line_4" cssClass="medium required" maxlength="50" readonly="readOnly" /></dd>
            
            <dt><label for="country">Country</label></dt>
            <dd><s:select name="supplier.address.country" id="country" list="countries" listKey="description" listValue="description" emptyOption="true" disabled="readOnly" /></dd>
            
            <dt><label for="postcode">Postcode</label></dt>
            <dd><s:textfield name="supplier.address.postcode" id="postcode" cssClass="samller required" maxlength="15" readonly="readOnly" /></dd>
        </dl>
    </fieldset>
    <fieldset>
        <legend>Contact Person</legend>
        <dl class="inline">           
            <dt><label for="title">Title</label></dt>
            <dd><s:select name="supplier.contactPerson.title" id="title" list="salutations" listKey="description" listValue="description" emptyOption="true" disabled="readOnly" /></dd>
            
            <dt><label for="forename">Forename</label></dt>
            <dd><s:textfield name="supplier.contactPerson.forename" id="forename" cssClass="medium required" maxlength="35" readonly="readOnly" /></dd>
            
            <dt><label for="surname">Surname</label></dt>
            <dd><s:textfield name="supplier.contactPerson.surname" id="surname" cssClass="medium required" maxlength="35" readonly="readOnly" /></dd>
        </dl>
    </fieldset>
    <fieldset>
        <legend>Contact Details</legend>
        <dl class="inline">
            <dt><label for="telephone">Telephone</label></dt>
            <dd><s:textfield name="supplier.tel" id="textfield" cssClass="small required" maxlength="50" readonly="readOnly" /></dd>
            
            <dt><label for="mobile">Mobile</label></dt>
            <dd><s:textfield name="supplier.mobile" id="mobile" cssClass="small required" maxlength="50" readonly="readOnly" /></dd>
            
            <dt><label for="fax">Fax</label></dt>
            <dd><s:textfield name="supplier.fax" id="fax" cssClass="small required" maxlength="50" readonly="readOnly" /></dd>
            
            <dt><label for="email">Email</label></dt>
            <dd><s:textfield name="supplier.email" id="email" cssClass="medium required" maxlength="100" readonly="readOnly" /></dd>
            
            <dt><label for="notes">Notes</label></dt>
            <dd><s:textarea name="supplier.notes" id="notes" cssClass="bigger" readonly="readOnly"></s:textarea><small>Add any relevant notes here.</small></dd>
        </dl>
    </fieldset>
    
    <s:iterator value="#errorMap['supplier.email']">
        <div class="error msg">
            <s:property />
        </div>
    </s:iterator>
    
    <div class="buttons">
        <s:if test="crudOperation == 'read'">
	        <button id="return_button" type="button" class="button">Return to Suppliers</button>
        </s:if>
        <s:else>
	        <button id="save_button" type="button" value="" class="button">Save</button>
	        <button id="cancel_button" type="button" class="button white">Cancel</button>
        </s:else>
    </div>
</s:form>

<script>

jQuery(document).ready(function() {

    jQuery('button[id=save_button]').click(function(e) {
        e.preventDefault();
        
        jQuery('form').get(0).setAttribute('action', 'suppliers-save');
        jQuery('form').submit();
        
        return true;
    });
    
    jQuery('button[id=return_button]').click(function(e) {
        e.preventDefault();
        
        jQuery('form').get(0).setAttribute('action', 'suppliers-list');
        jQuery('form').submit();
        
        return true;
    });
    
    jQuery('button[id=cancel_button]').click(function(e) {
        e.preventDefault();
        
        jQuery('form').get(0).setAttribute('action',  'suppliers-list');
        jQuery('form').submit();
        
        return true;
    });
});

</script>