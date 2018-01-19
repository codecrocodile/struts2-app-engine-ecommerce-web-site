<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div class="grid_12">
    <header class="page">
        <h1>Supplier Registration Form - Step 6 of 6, Other Details</h1>
    </header>
</div>
<div class="clear"></div>

<div class="grid_6 push_3">

	<s:form id="add_site_form" action="fly-fishing-suppliers-form-step7" cssClass="form_place" method="post">
	
		<s:set name="errorMap" value="getFieldErrors()" />
		
		<div>
		    <label for="ableToProvideReference">Would any of your existing customers be willing to provide a reference if required? <em class="required">* Required</em></label>
		    <div class="inlineItems">
                <s:radio id="ableToProvideReference" list="#{'true' : 'Yes', 'false' : 'No'}" name="supplierRegistrationDetails.ableToProvideReference" />
		    </div>
		</div>
		<s:iterator value="#errorMap['supplierRegistrationDetails.ableToProvideReference']">
            <div class="error_msg" style="margin:5px 0px"><s:property /></div>
		</s:iterator>
		
		<div>
			<label for="exampleCustomers">Please give two examples of existing customers?</label>
			<p class="additionalNote"><em class="required">Optional. We will not contact them without your explicate permission.</em></p>
			<s:textarea id="exampleCustomers" name="supplierRegistrationDetails.exampleCustomers" rows="5" cols="55"/>
		</div>
		<s:iterator value="#errorMap['supplierRegistrationDetails.exampleCustomers']">
            <div class="error_msg" style="margin:5px 0px"><s:property /></div>
		</s:iterator>	    
		
		<div>
		    <label for="theirflyQualityRating">On a scale of 1 - 10, how do you rate the quality of your flies in comparison to other companies that tie flies world wide? <em class="required">* Required</em></label>
		    <p class="additionalNote"><em class="required">Please do not automatically select 10. This is also a test of honesty.</em></p>
		    <s:select id="theirflyQualityRating" name="supplierRegistrationDetails.theirflyQualityRating"  cssClass="chosen-select-deselect" style="width:150px;" 
                list="numberList(1, 10, 1)"  headerValue="" headerKey="-1"/>
		</div>
		<s:iterator value="#errorMap['supplierRegistrationDetails.theirflyQualityRating']">
            <div class="error_msg" style="margin:5px 0px"><s:property /></div>
		</s:iterator>
		    
		<div>
			<label for="otherCompanyInfo">We would love to know a little about you. Could you tell us a bit about yourself and your company?</label>
			<p class="additionalNote"><em class="required">Optional, but it helps in our decision process.</em></p>
			<s:textarea id="otherCompanyInfo" name="supplierRegistrationDetails.otherCompanyInfo" rows="5" cols="55"/>
		</div>
        <s:iterator value="#errorMap['supplierRegistrationDetails.otherCompanyInfo']">
            <div class="error_msg" style="margin:5px 0px"><s:property /></div>
		</s:iterator>		
		
		<div>
		    <s:hidden id="navigateForward" name="navigateForward" value="true"></s:hidden>
		   
            <input id="backBtn" class="button large blue" type="submit" value="Back" style="float: left; margin: 10px 0; cursor: pointer;" />
			<input id="forwardBtn" class="button large blue" type="submit" value="Finish" />
			<div class="clear"></div>
		</div> 
	 
	</s:form>
	
</div>

<script>
	$('#backBtn').click(function() {
	    $('#navigateForward').val(false);
	});
	$('#forwardBtn').click(function() {
	    $('#navigateForward').val(true);
	});
</script>

