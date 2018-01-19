<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div class="grid_12">
    <header class="page">
        <h1>Supplier Registration Form - Step 2 of 6, Company Information</h1>
    </header>
</div>
<div class="clear"></div>

<div class="grid_6 push_3">
	<s:form id="add_site_form" action="fly-fishing-suppliers-form-step3" cssClass="form_place" method="post">
	
		<s:set name="errorMap" value="getFieldErrors()" /> 
		
		<div>
		    <label for="tradingYears">How many years have you been trading? <em class="required">* Required</em></label>
		    <s:select id="tradingYears" name="supplierRegistrationDetails.tradingYears"  cssClass="chosen-select-deselect" style="width:150px;" 
		      list="numberList(0, 100, 1)"  headerValue="" headerKey="-1"/>
		</div>
	    <s:iterator value="#errorMap['supplierRegistrationDetails.tradingYears']">
	      <div class="error_msg" style="margin:5px 0px"><s:property /></div>
	    </s:iterator>   
    		
		<div>
		    <label for="fullTimeStaff">How many full-time staff do you employ? <em class="required">* Required</em></label>
		    <s:select id="fullTimeStaff" name="supplierRegistrationDetails.fullTimeStaff"  cssClass="chosen-select-deselect" style="width:150px;" 
		      list="numberList(1, 300, 1)"  headerValue="" headerKey="-1"/>
		</div>
        <s:iterator value="#errorMap['supplierRegistrationDetails.fullTimeStaff']">
          <div class="error_msg" style="margin:5px 0px"><s:property /></div>
        </s:iterator> 
        		
        <div>
		    <label for="partTimeStaff">How many part-time staff do you employ? <em class="required">* Required</em></label>
		    <s:select id="partTimeStaff" name="supplierRegistrationDetails.partTimeStaff"  cssClass="chosen-select-deselect" style="width:150px;" 
		      list="numberList(0, 300, 1)"  headerValue="" headerKey="-1"/>
		</div>
        <s:iterator value="#errorMap['supplierRegistrationDetails.partTimeStaff']">
          <div class="error_msg" style="margin:5px 0px"><s:property /></div>
        </s:iterator> 
        		
		<div>
		    <label for="seasonalStaff">On average how many seasonal staff do you employ? <em class="required">* Required</em></label>
		    <s:select id="seasonalStaff" name="supplierRegistrationDetails.seasonalStaff"  cssClass="chosen-select-deselect" style="width:150px;" 
		      list="numberList(0, 300, 1)"  headerValue="" headerKey=""/>
		</div>
        <s:iterator value="#errorMap['supplierRegistrationDetails.seasonalStaff']">
          <div class="error_msg" style="margin:5px 0px"><s:property /></div>
        </s:iterator> 
        		
		<div>
		    <label for="averageFliesPerMonth">On average how many flies do you tie in a month? <em class="required">* Required</em></label>
		    <div class="inlineItems">
                <s:textfield id="averageFliesPerMonth" name="supplierRegistrationDetails.averageFliesPerMonth" cssClass="i-format-small mask-int" maxlength="5" />
                <span> <i>Dozen</i></span>
            </div>
		</div>
        <s:iterator value="#errorMap['supplierRegistrationDetails.averageFliesPerMonth']">
          <div class="error_msg" style="margin:5px 0px"><s:property /></div>
        </s:iterator> 
        		
		<div>
		    <label>Do you out-source production or otherwise buy flies from other companies to sell under your company name? <em class="required">* Required</em></label>
		    <div class="inlineItems">
		       <s:radio id="outsourcesFlies" name="supplierRegistrationDetails.outsourcesFlies" 
		          list="#{'NO' : 'No', 'YES' : 'Yes', 'SOME' : 'Some'}"/>
		    </div>
		</div>
        <s:iterator value="#errorMap['supplierRegistrationDetails.outsourcesFlies']">
          <div class="error_msg" style="margin:5px 0px"><s:property /></div>
        </s:iterator> 		
		
		<div>
		
		<s:hidden id="navigateForward" name="navigateForward" value="true"></s:hidden>
		
		<input id="backBtn" class="button large blue" type="submit" value="Back" style="float: left; margin: 10px 0; cursor: pointer;" />
		    <input id="forwardBtn" class="button large blue" type="submit" value="Next" />
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
