<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div class="grid_12">
    <header class="page">
        <h1>Supplier Registration Form - Step 4 of 6, Orders Placed with You</h1>
    </header>
</div>
<div class="clear"></div>

<div class="grid_6 push_3">

	<s:form id="add_site_form" action="fly-fishing-suppliers-form-step5" cssClass="form_place" method="post">
	
		<s:set name="errorMap" value="getFieldErrors()" /> 
		
		<div>
		    <label for="minOrderDozen">What is the minimum quantity order that can be placed? <em class="required"> * Required</em></label>
		    <div class="inlineItems">
			    <s:textfield id="minOrderDozen" name="supplierRegistrationDetails.minOrderDozen" cssClass="i-format-small mask-int" label="Dolars" maxlength="5" />
			    <span> <i>Dozen</i></span>
		    </div>
		</div>
		<s:iterator value="#errorMap['supplierRegistrationDetails.minOrderDozen']">
		  <div class="error_msg" style="margin:5px 0px"><s:property /></div>
		</s:iterator>	
		
		<div>
		    <label for="minOrderDolars">What is the minimum order value that can be placed? <em class="required">Optional</em></label>
		    <div class="inlineItems">
		      <s:textfield id="minOrderDolars" name="supplierRegistrationDetails.minOrderDolars" cssClass="i-format-small mask-money" maxlength="8" />
		      <span> <i>Dollars ($)</i></span>
		    </div>
		</div>
	    <s:iterator value="#errorMap['supplierRegistrationDetails.minOrderDolars']">
	      <div class="error_msg" style="margin:5px 0px"><s:property /></div>
	    </s:iterator>
	    	            
		<div>
		    <label for="minOrderPerPattern">What is the minimum quantity order for any one fly pattern?<em class="required">* Required</em></label>
		    <div class="inlineItems">
		      <s:textfield id="minOrderPerPattern" name="supplierRegistrationDetails.minOrderPerPattern" cssClass="i-format-small mask-int" maxlength="5" />
		      <span> <i>Dozen</i></span>
            </div>
		</div>
	    <s:iterator value="#errorMap['supplierRegistrationDetails.minOrderPerPattern']">
	      <div class="error_msg" style="margin:5px 0px"><s:property /></div>
	    </s:iterator>
	    	
		<div>
		    <label for="leadTimeWeeks">On average how long would it take you to fulfil an order of 1000 dozen flies of a simple fly pattern? <em class="required">* Required</em></label>
		    <s:select id="leadTimeWeeks" name="supplierRegistrationDetails.leadTimeWeeks"  cssClass="chosen-select-deselect" style="width:150px;" 
		       list="#{1 : '1 week', 2 : '2 weeks', 3 : '3 weeks', 4 : '4 weeks', 5 : '5 weeks', 6 : '6 weeks', 7 : '7 weeks', 8: '8 weeks', 9: '9 weeks', 10: '10 weeks', 11 : '11 weeks', 12 : '12 weeks', 52 : 'more'}"  headerKey="" headerValue="" />
		</div>
	    <s:iterator value="#errorMap['supplierRegistrationDetails.leadTimeWeeks']">
	      <div class="error_msg" style="margin:5px 0px"><s:property /></div>
	    </s:iterator>
	    	
		<div>
		    <label for="downPayment">As a percentage value of an order. How much would you require as a down-payment? <em class="required">* Required</em></label>
		    <s:select id="downPayment" name="supplierRegistrationDetails.downPayment"  cssClass="chosen-select-deselect" style="width:250px;" 
		       list="#{0 : 'No down-payment required', 10 : '10%', 20 : '20%', 30 : '30%', 40 : '40%', 50 : '50%', 60 : '60%', 70 : '70%', 80 : '80%', 90 : '90', 100 : '100%'}" headerKey="" headerValue="" />
		</div>
	    <s:iterator value="#errorMap['supplierRegistrationDetails.downPayment']">
	      <div class="error_msg" style="margin:5px 0px"><s:property /></div>
	    </s:iterator>
	    	
		<div>
		    <label>Would this percentage negotiable after trading relations have been established?<em class="required"> * Required</em></label>
		    <div class="inlineItems">
		       <s:radio name="supplierRegistrationDetails.downPaymentNegotiable" 
		           list="#{'YES' : 'Yes', 'NO' : 'No', 'POSSIBLY' : 'We would concider this'}"  />
		    </div>
		</div>
	    <s:iterator value="#errorMap['supplierRegistrationDetails.downPaymentNegotiable']">
	      <div class="error_msg" style="margin:5px 0px"><s:property /></div>
	    </s:iterator>
	    	
		<div>
		    <label for="dozenWetFliesDolars">On average, how much do you charge for one dozen wet flies? <em class="required"> * Required</em></label>
		    <s:textfield id="dozenWetFliesDolars" name="supplierRegistrationDetails.dozenWetFliesDolars" cssClass="i-format-small mask-money" maxlength="8" />
		</div>
	    <s:iterator value="#errorMap['supplierRegistrationDetails.dozenWetFliesDolars']">
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
