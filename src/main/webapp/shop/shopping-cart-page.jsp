<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>


<div class="grid_12">
    <header class="page">
        <h1>Shopping Cart</h1>
    </header>
</div>

<div class="clear"></div>

<section id="left_container">

<img src="/images/payment-progress-par-stage1.png" alt="payment progress bar checkout stage"/>

<s:if test="shoppingCart.shoppingCartEntries.size() == 0">
	<div class="call_act">
	    <div class="inside_call">
	        <h2 class="alignleft">Your shopping cart is empty!</h2> 
	        <a href="/shop/collection/popular-collection" class="button large green alignright" style="cursor: pointer;">Continue Shopping</a>
	    </div>
	</div>
</s:if>
<s:else>
	<a id="continue-shopping" href="/shop/collection/popular-collection" class="button green small" style="float:left; margin: 10px 0px 10px 0px; cursor: pointer;">Continue Shopping</a>
	<div class="clear"></div>
</s:else>

<s:if test="shoppingCart.isEnoughQuantityInStock() == false">
    <div id="quantity_error_msg" class="info_msg">Sorry, looks like someone has just beaten you to it! Another customer has just purchased some of the stock required to fill your order. Please check the messages below and amend your order. Don't worry, no payment has been taken yet.</div>
</s:if>

<s:iterator value="shoppingCart.shoppingCartEntries" var="entry">

 <div id="entry-div-productId-${entry.productId}" class="shopping-cart-row">
     <div class="img">
         <div class="img_pf_hover">
             <img src="${entry.smallerImageUrl}"  class="imgbordercart" alt="${entry.imageAltTagDesc}" />
             <a href="${entry.largeImageUrl}" class="crbox" title="${entry.name}"><span class="img_pf_icon zoom_in"></span></a>
         </div>
     </div>
     <div class="name">
         <label>${entry.name}</label>
     </div>
     <div class="qty">
         <input id="entry-quantity-productId-${entry.productId}" type="text" value="${entry.quantity}" class="i-format mask-int" />
         <button class="button white smaller" onclick="minusOne(${entry.productId})"><img src="/images/neg_16.png" alt="subtract button" /></button> 
         <button class="button white smaller" onclick="plusOne(${entry.productId})"><img src="/images/plus_16.png" alt="add button" /></button>
     </div>
     <div class="price">
         <label id="entry-total-productId-${entry.productId}">${entry.totalPriceString}</label>
     </div>
     <div class="del">
         <button class="button white smaller" onclick="deleteItems(${entry.productId})"><img src= "/images/icons24/cross.png" alt="delete button" width="15" height="15"/></button>
     </div>
     <%-- if re-directed here from the confirmation page then there isn't enough stock --%>
     <s:if test="#entry.isQuantityInStockEnough() == false">
        <s:if test="#entry.quantityInStock == 0">
            <div class="error_msg quantity_message">We have none of these left in stock. Please remove from order.</div>
        </s:if>
        <s:else>
            <div class="error_msg quantity_message">We only have ${entry.quantityInStock} of these in stock. Please amend order quantity.</div>
        </s:else>
     </s:if>
     <s:else>
        <div class="error_msg quantity_message" style="display: none;"></div>
     </s:else>
 </div>
    
</s:iterator>

</section>

<script>

//handle the css of the blocking dialog with config.css
$.blockUI.defaults.css = {}; 
function blockScreen() {
    $.blockUI({ 
        message:  '<h4><img src="/images/indicator-brown-bg.gif" />  Please wait...</h4>', 
        overlayCSS:  { 
            backgroundColor: '#222', 
            opacity:         0.1, 
            cursor:          'wait', 
        },
        fadeIn:  0, 
        fadeOut: 0, 
    }); 
}
// unblock when ajax activity stops 
$(document).ajaxStop($.unblockUI); 

function deleteItems(productId) {
    blockScreen();
	
    $.post("/modify-cart-entry-deleteItem", 
            "productId=" + productId,
            function(returnValue) {
	            var entryDivId = "#entry-div-productId-" + productId;
	            $(entryDivId).fadeTo(1000, 0);
	            $(entryDivId).slideUp(500);
	          
	            if (returnValue.newItemCount == 0) {
	                $('#continue-shopping').remove();
	            	$('#left_container > img').after(' <div class="call_act"><div class="inside_call"><h2 class="alignleft">Your shopping cart is empty!</h2> <a href="/shop/collection/popular-collection" class="button large green alignright" style="cursor: pointer;">Continue Shopping</a></div></div>');
	            }
	          
	            updatePageValues(returnValue);
            }, 
            "json");
}

function minusOne(productId) {
    var entryQuantity = "#entry-quantity-productId-" + productId;
    var level = jQuery(entryQuantity).val();
    if (level == "") {
        level = 1;
    } else {
        level = parseInt(level);
    }
    if (level > 1) {
    	
    	blockScreen();
        
        $.post("/modify-cart-entry-decrementItem", 
	        "productId=" + productId,
	        function(returnValue) {
        	    if (returnValue.enoughInStock == true) {
        	    	var messageDiv = jQuery('#entry-div-productId-' + productId + "> .quantity_message");
                    if (messageDiv.css('display') == "block") {
                        messageDiv.fadeTo(1000, 0);
                        messageDiv.slideUp(500);
                    }	
        	    } 
        	    
        	    jQuery('#entry-quantity-productId-' + productId).val(returnValue.newCartEntryItemCount);
	            jQuery('#entry-total-productId-' + productId).html(returnValue.newCartEntryTotal);
	
	            updatePageValues(returnValue);
	        }, "json");
    } 
}

function plusOne(productId) {
	
	blockScreen();
	
    var entryQuantity = "#entry-quantity-productId-" + productId;
    
    var level = jQuery(entryQuantity).val();
    if (level == "") {
        level = 1;
    } else {
        level = parseInt(level);
    }
    
    $.post("/modify-cart-entry-incrementItem", 
        "productId=" + productId,
        function(returnValue) {
        if (returnValue.incrementSccessful == true){
            $('#entry-quantity-productId-' + productId).val(returnValue.newCartEntryItemCount);
            $('#entry-total-productId-' + productId).html(returnValue.newCartEntryTotal);
        } else {
            if (returnValue.quantityLeftInStock == 0) {
                entryStockMessage(productId, "We have none of these left in stock. Please remove from order.");
            } else if (returnValue.quantityLeftInStock == returnValue.newCartEntryItemCount) {
                entryStockMessage(productId, "We only have " + returnValue.quantityLeftInStock + " of these in stock.");
            } else if (returnValue.quantityLeftInStock < returnValue.newCartEntryItemCount){
                entryStockMessage(productId, "We only have " + returnValue.quantityLeftInStock + " of these in stock. Please amend order quantity.");
            }	
        }  
        updatePageValues(returnValue);
	    }, "json").error(function(e){$.unblockUI; alert(e)});
}

function updatePageValues(returnValue) {
    updateCheckoutAndSideBar(returnValue);
    showHideMinimunSpendAlert(returnValue);
    showDiscountErrorMessage(returnValue);
}

function updateCheckoutAndSideBar(returnValue) {
    $('#shopping-cart-subtotal').html('<img src="/images/fugue/shopping-basket--arrow-green.png" alt="shopping basket"/> Checkout: ' + returnValue.newSubTotal + '  |  ' + 'Items:  ' + returnValue.newItemCount);
    $('#shopping-cart-sidebar-subtotal').html(returnValue.newSubTotal);
    $('#shopping-cart-sidebar-postage').html(returnValue.newPostageAndPacking);
    $('#shopping-cart-sidebar-discount').html(returnValue.newDiscountTotal);
    $('#shopping-cart-sidebar-total').html(returnValue.newTotal);
}

function showHideMinimunSpendAlert(returnValue) {
	var minimumSpend = <s:property value="shoppingCart.minimumSpend" />;
	var opacity = $('#sidebar-min-spend-message').css("opacity");
	
	if (returnValue.subTotal < minimumSpend) { // show message if sub total less than min spend
		if (opacity < 1) {
            $('#sidebar-min-spend-message').slideDown(500);
            $('#sidebar-min-spend-message').fadeTo(1000, 1);	
		}
	
		$('#sidebar-checkout-button-div').html('<img alt="" src="/images/checkout-with-paypal-button-disabled.png">');
	} else {
		if (opacity > 0) { // hide message if sub total greater than min spend
	        $('#sidebar-min-spend-message').fadeTo(1000, 0);
	        $('#sidebar-min-spend-message').slideUp(500);
		}
		
        if (returnValue.enoughInStockForFullOrder == true) {
            if ($('#quantity_error_msg').css('display') == 'block') {
                $('#quantity_error_msg').fadeTo(1000, 0);
                $('#quantity_error_msg').slideUp(800);
            }
            
            $('#sidebar-checkout-button-div').html('<a href="<s:url action="checkout-paypal"/>"><img alt="" src="/images/checkout-with-paypal-button.png" alt="checkout with paypal button" onclick="blockScreen()"></a>');
        }
	}
}

function showDiscountErrorMessage(returnValue) {
	if (returnValue.discountSuccessful == false && returnValue.newItemCount > 0) {
		$('#sidebar-discount-message').removeClass('succes_msg').removeClass('error_msg').addClass('error_msg');
	    
	    $('#sidebar-discount-message strong').html(returnValue.discountMessage);
	    $('#sidebar-discount-message').slideDown(500);
	    $('#sidebar-discount-message').fadeTo(1000, 1);	
	}
	
	if (returnValue.newItemCount == 0) {
		$('#sidebar-discount-message').css("display", "none");
	}
}

function entryStockMessage(productId, messageToDisplay) {
    var messageDiv = jQuery('#entry-div-productId-' + productId + "> .quantity_message");
    messageDiv.html(messageToDisplay);
    
    if (messageDiv.css("opacity") < 1 || messageDiv.css('display') == 'none') {
        messageDiv.css("opacity", 0);
        messageDiv.slideDown(500);
        messageDiv.fadeTo(1000, 1);     
    }
}

</script>