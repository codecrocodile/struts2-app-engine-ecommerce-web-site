<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<section id="sidebar">
	<div>
	
	    <div class="price_head">
	        <div class="inside">SUMMARY</div>
	    </div>
	    
	    <div class="summary-row-discount">
			<s:form id="discount_form" namespace="/" action="modify-cart-entry-applyDiscountCode">
				<s:label for="discountCode" cssClass="discount-code-lbl" value="Do you have a discount code?"/>
				<s:textfield name="discountCode" id="discountCode" cssClass="discount-code-txt" placeholder="ENTER CODE" />
				<sj:submit id="discount_code_btn" 
				 value="Apply" 
				 cssClass="button orange discount-code-btn" 
				 resetForm="true" 
				 dataType="json" 
				 onSuccessTopics="setDiscountMessage" 
				 onErrorTopics="setDiscountErrorMessage" 
				 targets="XXX"/>
			</s:form> 
			<div class="clear"> </div>
			<div id="sidebar-discount-message" class="error_msg info-discount" style="display: none"><strong></strong></div>
	    </div>
	    
		<script type="text/javascript">
		    $.subscribe('setDiscountMessage', function(event, data) {
		    	var response = JSON.parse(event.originalEvent.request.responseText);
		    	
		    	if (response.discountSuccessful == true) {
		    		$('#sidebar-discount-message').removeClass('error_msg').removeClass('succes_msg').addClass('succes_msg');
	                
	                jQuery('#shopping-cart-sidebar-postage').html(response.newPostageAndPacking);
	                jQuery('#shopping-cart-sidebar-discount').html(response.newDiscountTotal);
	                jQuery('#shopping-cart-sidebar-total').html(response.newTotal);
		    	} else {
		    		$('#sidebar-discount-message').removeClass('succes_msg').removeClass('error_msg').addClass('error_msg');
		    	}
		    	
		    	$('#sidebar-discount-message strong').html(response.discountMessage);
	            $('#sidebar-discount-message').slideDown(500);
	            $('#sidebar-discount-message').fadeTo(1000, 1);  
		    });
		    
		    $.subscribe('setDiscountErrorMessage', function(event, data) {
		    	$('#sidebar-discount-message strong').removeClass('succes_msg').addClass('error_msg');
                $('#sidebar-discount-message').html("Error. Discount not applied.");
                $('#sidebar-discount-message').slideDown(500);
                $('#sidebar-discount-message').fadeTo(1000, 1);    
		    });
		</script>
        
	    <div class="summary-row">
	        <span class="alignLeft">Sub-total</span><span id="shopping-cart-sidebar-subtotal" class="alignRight"><s:property value="shoppingCart.subTotalString" /></span>
	    </div>
	    <div class="summary-row">
            <span class="alignLeft">Estimated Postage</span><span id="shopping-cart-sidebar-postage" class="alignRight"><s:property value="shoppingCart.postageAndPackingString" /></span>
	    </div>
	    <div class="summary-row-thin"></div>
	    <div class="summary-row-thin"></div>
	    <div class="summary-row">
            <span class="alignLeft">Discount Applied</span><span id="shopping-cart-sidebar-discount" class="alignRight"><s:property value="shoppingCart.discountTotalString" /></span>
        </div>
        <div class="summary-row">
            <span class="alignLeft"><b>TOTAL</b></span><span id="shopping-cart-sidebar-total" class="alignRight total"><s:property value="shoppingCart.totalString" /></span>
        </div>
        <div class="summary-message-row">
            <s:if test="shoppingCart.isBellowMinimumSpend() == true">
                <div class="info_msg info-min-spend" id="sidebar-min-spend-message"><strong>Info: </strong>minimum <s:property value="shoppingCart.minimumSpendString" /></div>
            </s:if>
            <s:else>
                <div class="info_msg info-min-spend" id="sidebar-min-spend-message" style="display: none; opacity:0;"><strong>Info: </strong>minimum <s:property value="shoppingCart.minimumSpendString" /></div>
            </s:else>
            <div class="clear"></div>
        </div>
	    <div class="price_footer">
	        <div id="sidebar-checkout-button-div" class="inside">
		        <s:if test="shoppingCart.isBellowMinimumSpend() == true || shoppingCart.isEnoughQuantityInStock() == false">
	                <img src="/images/checkout-with-paypal-button-disabled.png" alt="disabled checkout with paypal button">
	            </s:if>
	            <s:else>
	                <a id="sidebar-paypal-link" href="<s:url action="checkout-paypal"/>"><img src="/images/checkout-with-paypal-button.png" alt="checkout with paypal button" onclick="blockScreen()"></a>
	            </s:else>
	        </div>
	    </div>
	</div>
</section>