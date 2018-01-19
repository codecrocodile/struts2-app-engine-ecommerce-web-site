<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div class="grid_12">
    <header class="page">
        <h1>Supplier Registration Form - Step 1 of 6, Basic Company Details</h1>
    </header>
</div>
<div class="clear"></div>


<div class="grid_6 push_3">
    
    <p><em>No information you provide us with will be published or shared with any third parties.</em></p>

	<s:form id="add_site_form" action="fly-fishing-suppliers-form-step2" cssClass="form_place" method="post">
	   
	    <s:set name="errorMap" value="getFieldErrors()" />
	  
	 
		<!-- id give extra form style properties. think about changing this to class -->

		<div>
			<label for="companyName">Company Name: <em class="required">* Required</em></label>
			<s:textfield id="companyName" name="supplierRegistrationDetails.supplier.companyName" cssClass="i-format" maxlength="100" />
		</div>
	    <s:iterator value="#errorMap['supplierRegistrationDetails.supplier.companyName']">
            <div class="error_msg" style="margin:5px 0px"><s:property /></div>
        </s:iterator>
		
		<div>
            <label for="country">Country: <em class="required">* Required</em></label>
            <s:select id="country" name="supplierRegistrationDetails.supplier.address.country"  cssClass="chosen-select-deselect" style="width:350px;" 
                    list="countries" listValue="description" listKey="description" emptyOption="true"/>
        </div>
        <s:iterator value="#errorMap['supplierRegistrationDetails.supplier.address.country']">
            <div class="error_msg" style="margin:5px 0px"><s:property /></div>
        </s:iterator>        
		
		<div>
            <label for="contactForename">Contact Forename: <em class="required">* Required</em></label>
            <s:textfield id="contactForename" name="supplierRegistrationDetails.supplier.contactPerson.forename" cssClass="i-format" maxlength="35" />
        </div>
        <s:iterator value="#errorMap['supplierRegistrationDetails.supplier.contactPerson.forename']">
            <div class="error_msg" style="margin:5px 0px"><s:property /></div>
        </s:iterator>         
        
        <div>
            <label for="contactSurname">Contact Surname: <em class="required">* Required</em></label>
            <s:textfield id="contactSurname" name="supplierRegistrationDetails.supplier.contactPerson.surname" cssClass="i-format" maxlength="35" />
        </div>
        <s:iterator value="#errorMap['supplierRegistrationDetails.supplier.contactPerson.surname']">
            <div class="error_msg" style="margin:5px 0px"><s:property /></div>
        </s:iterator>         
        
        <div>
            <label for="tel">Phone Number: <em class="required">* Required</em></label>
            <s:textfield id="tel" name="supplierRegistrationDetails.supplier.tel" cssClass="i-format" maxlength="50" />
        </div>
        <s:iterator value="#errorMap['supplierRegistrationDetails.supplier.tel']">
            <div class="error_msg" style="margin:5px 0px"><s:property /></div>
        </s:iterator>         
        
        <div>
            <label for="englishSpeaking">Would the phone operator be able to speak in English? <em class="required">* Required</em></label>
            <div class="inlineItems">
                <s:radio id="englishSpeaking" list="#{'YES' : 'Yes', 'NO' : 'No', 'POSSIBLY' : 'Not always'}" name="supplierRegistrationDetails.englishSpeaking"/>
            </div>
        </div>
        <s:iterator value="#errorMap['supplierRegistrationDetails.englishSpeaking']">
            <div class="error_msg" style="margin:5px 0px"><s:property /></div>
        </s:iterator>         
        
        <div>
            <label for="email">Email Address: <em class="required">* Required</em></label>
            <s:textfield id="email" name="supplierRegistrationDetails.supplier.email" cssClass="i-format" maxlength="100" />
        </div>
        <s:iterator value="#errorMap['supplierRegistrationDetails.supplier.email']">
            <div class="error_msg" style="margin:5px 0px"><s:property /></div>
        </s:iterator>         
        
		<div>
			<input class="button large blue" type="submit" value="Next" />
			<div class="clear"></div>
		</div>

	</s:form>
</div>