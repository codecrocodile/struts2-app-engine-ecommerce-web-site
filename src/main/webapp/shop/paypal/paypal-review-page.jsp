<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<div class="grid_12">
    <header class="page">
        <h1>Order Review</h1>
    </header>
</div>

<div class="clear"></div>

<section id="left_container">

<img src="/images/payment-progress-par-stage3.png" alt="payment progress bar checkout stage"/>

<div class="grid_9">
    <div class="buttonContainer">
        <a href="<s:url action="shopping-cart-page"/>" class="button green small">Edit order</a> 
    </div>
</div>

<div class="grid_9">
    <hr />
</div>

<div class="grid_9">
<table>
    <thead>
        <tr>
	        <th>Contact Details</th>
	        <th>Shipping Address</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>
                <s:property value="customer.displayName"/><br>
                <s:property value="customer.email"/><br>
                <s:if test="customer.phoneNumber != null">
                    <s:property value="customer.phoneNumber"/><br>
                </s:if>
            </td>
            <td>
                <address>
                    <s:iterator value="shippingAddressFields">
                        <s:property /><br>
                    </s:iterator>
                </address>
            </td>        
        </tr>
    </tbody>
</table>
</div>

<div class="grid_9">
    <hr class="dashed" />
</div>

<div class="grid_9">
<table>
    <thead>
        <tr>
	        <th>Product</th>
	        <th>Quantity</th>
	        <th>Item Price</th>
	        <th>Total Price</th>
        </tr>
    </thead>
    <tbody>
	    <s:iterator value="shoppingCart.shoppingCartEntries" var="entry">
	        <tr>
	            <td>${entry.name}</td><td>${entry.quantity}</td><td>${entry.unitPriceString}</td><td>${entry.totalPriceString}</td>
	        </tr>    
	    </s:iterator>
    </tbody>
</table>
</div>

<div class="grid_9">
    <hr />
</div>

</section>


<script>
$(document).ready(function() {
    $("a").click(function() {
        blockScreen();
    });
});

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
</script>