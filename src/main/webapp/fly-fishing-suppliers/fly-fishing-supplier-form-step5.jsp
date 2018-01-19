<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div class="grid_12">
    <header class="page">
        <h1>Supplier Registration Form - Step 5 of 6, Payment and Delivery</h1>
    </header>
</div>
<div class="clear"></div>

<div class="grid_6 push_3">

	<s:form id="add_site_form" action="fly-fishing-suppliers-form-step6" cssClass="form_place" method="post">
    
        <s:set name="errorMap" value="getFieldErrors()" />	
	  
	    <div>
	        <label for="paymentOptions">What payment methods do you accept? </label>
	        <p style="margin: 0px; padding:0px;"><em class="required">Please choose all that apply.</em></p>
	        <s:select id="paymentOptions" name="supplierRegistrationDetails.paymentOptions" multiple="true" list="paymentOptions" 
	           data-placeholder="Click to enter payment methods" multiple="true" cssClass="chosen-select-deselect" style="width:350px;"/>
	    </div>
        <s:iterator value="#errorMap['supplierRegistrationDetails.paymentOptions']">
          <div class="error_msg" style="margin:5px 0px"><s:property /></div>
        </s:iterator>
              	    
	    <div id="paymentExtraDiv" style="display: none">
          <label for="otherPaymentMethods">Please specify which other payment methods you accept:</label>
          <s:textarea id="otherPaymentMethods" name="supplierRegistrationDetails.otherPaymentMethods" rows="3" cols="55"/>
        </div>
        <s:iterator value="#errorMap['supplierRegistrationDetails.otherPaymentMethods']">
          <div class="error_msg" style="margin:5px 0px"><s:property /></div>
        </s:iterator>        
        
        <div>
            <label for="shippingMethods">What shipping companies do you use?</label>
            <p style="margin: 0px; padding:0px;"><em class="required">Please choose all that apply.</em></p>
            <s:select id="shippingMethods" name="supplierRegistrationDetails.shippingMethods" list="shippingMethods" 
                data-placeholder="Click to enter shipping companies" multiple="true" cssClass="chosen-select-deselect"  style="width:350px;"/>
        </div>
        <s:iterator value="#errorMap['supplierRegistrationDetails.shippingMethods']">
          <div class="error_msg" style="margin:5px 0px"><s:property /></div>
        </s:iterator>  
               
        <div id="shippingExtraDiv" style="display: none">
          <label for="otherShippingMethods">Please specify which other shipping companies you use:</label>
          <s:textarea id="otherShippingMethods" name="supplierRegistrationDetails.otherShippingMethods" rows="3" cols="55" />
        </div>
        <s:iterator value="#errorMap['supplierRegistrationDetails.otherShippingMethods']">
          <div class="error_msg" style="margin:5px 0px"><s:property /></div>
        </s:iterator>  
        
	    <div>
	        <label for="freePostageLargeOrders">Do you offer free postage and packing on larger orders? <em class="required">* Required</em></label>
	        <div class="inlineItems">
	           <s:radio list="#{'true' : 'Yes', 'false' : 'No'}" name="supplierRegistrationDetails.freePostageLargeOrders" />
	        </div>
	    </div>  
        <s:iterator value="#errorMap['supplierRegistrationDetails.freePostageLargeOrders']">
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
	var value = $('#shippingMethods').val();
	showExtraDiv(value, 9, '#shippingExtraDiv', '#otherShippingMethods');
	var value2 = $('#paymentOptions').val();
	showExtraDiv(value2, 10, '#paymentExtraDiv', '#otherPaymentMethods');

});

$('#backBtn').click(function() {
    $('#navigateForward').val(false);
});
$('#forwardBtn').click(function() {
    $('#navigateForward').val(true);
});

$('#paymentOptions').bind('change',function() {
	var value = $(this).val();
	showExtraDiv(value, 10, '#paymentExtraDiv', '#otherPaymentMethods');
});

$('#shippingMethods').bind('change',function() {
    var value = $(this).val();
    showExtraDiv(value, 9, '#shippingExtraDiv', '#otherShippingMethods');
});

function showExtraDiv(arrayOfSelection, otherId, divIdToShow, correspondingTextAreaId) {
	var found = false;
	
    if (arrayOfSelection != null) {
        for (var i = 0; i < arrayOfSelection.length; i++) {
            if (arrayOfSelection[i]  == otherId ) { // this is the "other" option
                found = true; 
                break;
            } 
        }   
    }
    
    if (found === true) {
        var messageDiv = $(divIdToShow);
        // show
        if (messageDiv.css("opacity") < 1 || messageDiv.css('display') == 'none') {
            messageDiv.css("opacity", 0);
            messageDiv.slideDown(500);
            messageDiv.fadeTo(1000, 1);     
        }
   } else {
	    //hide
        var messageDiv = $(divIdToShow);
        if (messageDiv.css('display') == "block") {
            messageDiv.fadeTo(1000, 0);
            messageDiv.slideUp(500);
        }
        // clear text area if other not picked
        $(correspondingTextAreaId).val('');
   }
}

</script>
