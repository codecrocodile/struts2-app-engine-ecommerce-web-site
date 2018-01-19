<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div class="grid_12">
    <header class="page">
        <h1>Supplier Registration Form - Step 3 of 6, Your Fishing Flies</h1>
    </header>
</div>
<div class="clear"></div>

<div class="grid_6 push_3">
	<s:form id="add_site_form" name="step3Form" action="fly-fishing-suppliers-form-step4" cssClass="form_place" method="post">
	
	<s:set name="errorMap" value="getFieldErrors()" /> 
	
	<div>
	    <label for="averageDifferentPatterns">On average, how many different fly patterns do you tie for your standard range? <em class="required">* Required</em></label>
	    <s:textfield id="averageDifferentPatterns" name="supplierRegistrationDetails.averageDifferentPatterns" cssClass="i-format-small mask-int" maxlength="8" />
	</div>
	<s:iterator value="#errorMap['supplierRegistrationDetails.averageDifferentPatterns']">
	  <div class="error_msg" style="margin:5px 0px"><s:property /></div>
	</s:iterator>   
	
	<div>
		<label>Do you tie custom patterns to customer specifications? <em class="required">* Required</em></label>
		<div class="inlineItems">
		       <s:radio id="tiesCustomPatterns" list="#{'true' : 'Yes', 'false' : 'No'}" name="supplierRegistrationDetails.tiesCustomPatterns" />
		</div>
	</div>
    <s:iterator value="#errorMap['supplierRegistrationDetails.tiesCustomPatterns']">
      <div class="error_msg" style="margin:5px 0px"><s:property /></div>
    </s:iterator>  	
	
	<div>
		<label>Do you use branded hooks for your flies? <em class="required">* Required</em></label>
		<div class="inlineItems">
            <s:radio id="brandedHooksQuestion" list="#{'true' : 'Yes', 'false' : 'No'}" name="supplierRegistrationDetails.usesBrandedHooks" />
		</div>
	</div>
    <s:iterator value="#errorMap['supplierRegistrationDetails.usesBrandedHooks']">
      <div class="error_msg" style="margin:5px 0px"><s:property /></div>
    </s:iterator>  	
	
	<div id="hookBrandsExtraDiv" style="display: none">
		<label for="">Please specify which brands of hooks you use: <em class="required"> Optional</em></label>
		<s:textarea id="hookBrands" name="supplierRegistrationDetails.hookBrands" rows="3" cols="55"/>
	</div>
    <s:iterator value="#errorMap['supplierRegistrationDetails.hookBrands']">
      <div class="error_msg" style="margin:5px 0px"><s:property /></div>
    </s:iterator>  	
	   
	<div>
		<label>Do you use branded tying threads? <em class="required">* Required</em></label>
		<div class="inlineItems">
		  <s:radio id="brandedThreadQuestion" list="#{'true' : 'Yes', 'false' : 'No'}" name="supplierRegistrationDetails.usesBrandedThread"/>
		</div>
	</div>
    <s:iterator value="#errorMap['supplierRegistrationDetails.usesBrandedThread']">
      <div class="error_msg" style="margin:5px 0px"><s:property /></div>
    </s:iterator>  	
	
	<div id="threadBrandsExtraDiv" style="display: none">
		<label for="">Please specify which brands of thread you use: <em class="required"> Optional</em></label>
		<s:textarea id="threadBrands" name="supplierRegistrationDetails.threadBrands" rows="3" cols="55"/>
	</div>
    <s:iterator value="#errorMap['supplierRegistrationDetails.threadBrands']">
      <div class="error_msg" style="margin:5px 0px"><s:property /></div>
    </s:iterator>  	
		   
	<div>
		<label for="">Would you be able supply a list of materials used in the production of orders placed with you? <em class="required">* Required</em></label>
		<div class="inlineItems">
		     <s:radio list="#{'YES' : 'Yes', 'NO' : 'No', 'POSSIBLY' : 'We would concider'}" name="supplierRegistrationDetails.materialListSupplied" />
		</div>
	</div>
    <s:iterator value="#errorMap['supplierRegistrationDetails.materialListSupplied']">
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

$(document).ready(function() {
    var i = $('#brandedHooksQuestiontrue:checked').val();
    if (i !== undefined) {
    	showExtraDiv('#hookBrandsExtraDiv', false);
    }
    i = $('#brandedThreadQuestiontrue:checked').val();
    if (i !== undefined) {
    	showExtraDiv('#threadBrandsExtraDiv', false);
    }

    $('#backBtn').click(function() {
        $('#navigateForward').val(false);
    });
    $('#forwardBtn').click(function() {
        $('#navigateForward').val(true);
    });
    
    $('#brandedHooksQuestiontrue').click(function() {
        showExtraDiv('#hookBrandsExtraDiv', true);
    });

    $('#brandedHooksQuestionfalse').click(function() {
        hideExtraDiv('#hookBrandsExtraDiv');
    });

    $('#brandedThreadQuestiontrue').click(function() {
        showExtraDiv('#threadBrandsExtraDiv', true);
    });

    $('#brandedThreadQuestionfalse').click(function() {
        hideExtraDiv('#threadBrandsExtraDiv');
    });
    
});

function showExtraDiv(div, animate) {
	var messageDiv = $(div);
	 if (messageDiv.css("opacity") < 1 || messageDiv.css('display') == 'none') {
	     messageDiv.css("opacity", 0);
	     
	     if (animate === true) {
	         messageDiv.slideDown(500);
	         messageDiv.fadeTo(1000, 1);	    	 
	     } else {
	    	 messageDiv.css("opacity", 1);
	    	 messageDiv.css("display", 'block');
	     }
     
	 }
}

function hideExtraDiv(div) {
    var messageDiv = $(div);
    if (messageDiv.css('display') == "block") {
        messageDiv.fadeTo(1000, 0);
        messageDiv.slideUp(500);
    }
}

</script>
