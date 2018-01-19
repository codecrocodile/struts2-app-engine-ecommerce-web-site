<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>


<div class="grid_12">
    <header class="page">
        <h1><s:property value="product.name" /></h1>
    </header>
</div>

<div class="clear"></div>

<section id="left_container">

	<div class="img_pf_hover">
	    <img src="<s:property value="product.images.mediumImageUrl" />" class="imgborder col4" alt="" />
	    <a href="<s:property value="product.images.largeImageUrl" />" class="crbox" title="<s:property value="product.name" />"><span class="img_pf_icon zoom_in"></span></a>
	</div>
	
	<s:if test="multiProductDisplay == true">
		<s:if test="product.productGroupingConfigId == 1">
		        <p>Non-configurable products not implemented at this time</p> 
		</s:if>
		<s:elseif test="product.productGroupingConfigId == 2">
			<s:form id="purchase_form" action="" cssClass="productPurchase">
		    
		    <s:hidden name="productDisplayType" value="2"/>  
		    <s:hidden name="productId" value="%{product.productId}"/>
		
			<fieldset>
				<p style="float:right; font-size: 24px; color: #667700; margin: 5px;"><s:property value="product.priceString" /></p>
				<hr />
				<s:iterator value="productAttributes" var="prodAttribs">   
					<label><s:property value="description" />:</label>
					<s:radio name="attributeToAttributeValue['%{#prodAttribs.id}']" list="#prodAttribs.productAttributeValues" listKey="id" listValue="description" value="#action.getDefaultAttributeValueId(id)" />
					<div class="clear"></div>
					<hr />  
				</s:iterator> 
				<div>
					<label for="quantity">Quantity:</label>  
					<s:textfield name="quantity" id="quantity" value="1" cssClass="i-format mask-int" />
					<button id="minus_one" class="button white smaller"><img src="/images/neg_16.png" alt="substract button" /></button> 
					<button id="add_one" class="button white smaller"><img src="/images/plus_16.png" alt="add button" /></button>
				</div>
				
				<div class="clear"></div>
				<hr />
				
				<input class="button normal blue" type="submit" value="Add" style="float: right;" />
			    <img id="product-page-indicator" src="/images/indicator-brown-bg.gif" alt="Loading..." style="display: none; float: right; width: 32px; height: 32px; margin-right: 10px;"/> 
				          
                <div class="clear"></div>
				               
				<div id="rating_block">
				    <label>Rating:</label> 
					<s:bean name="org.apache.struts2.util.Counter" var="counter">
					   <s:param name="last" value="5"/>
					</s:bean>
					<s:iterator value="#counter" status="itStatus">
					   <s:if test="#itStatus.count == product.averageStarRating">
					       <input type="radio" class="star" disabled="disabled" checked="checked"/>
					   </s:if>
					   <s:else>
					       <input type="radio" class="star" disabled="disabled"/>
					   </s:else>
					</s:iterator>
				</div>
			</fieldset>      
			</s:form>
		</s:elseif>
        </s:if>
		<s:else>
			<s:form action="" cssClass="productPurchase">
			<fieldset>
			    <p>Single product not implemented at this time</p>            
			</fieldset>
			</s:form>
		</s:else>
        
        <div class="clear"></div>
        <div id="product_purchase_success_msg" class="succes_msg" style="display: none;"></div>
        <hr /> 
	
		<s:form id="product_rating" action="" >
		    
		    <s:hidden name="productId" value="%{product.productId}"/>
		    
			<p>Customer Rating: (1 - 5)</p>
			<input class="hover-star" type="radio" name="productRating" value="1" title="Very poor"/>
			<input class="hover-star" type="radio" name="productRating" value="2" title="Poor"/>
			<input class="hover-star" type="radio" name="productRating" value="3" title="OK" checked="checked"/>
			<input class="hover-star" type="radio" name="productRating" value="4" title="Good"/>
			<input class="hover-star" type="radio" name="productRating" value="5" title="Very Good"/>
			<span id="rating-msg" style="margin:0 0 0 20px;">Have you used this fly before? Why not rate it?</span>
			
			<div id="submit_rating_success_msg" class="succes_msg" style="display: none;"><strong>Thank you</strong>, your rating has been recorded.</div>
		</s:form>
	
		<h2>Product Info</h2>
		<ul class="tabs">
		     <li><a href="#tab1">Fishing Tips</a></li>
		     <li><a href="#tab2">Ratings</a></li>
		</ul>
		<div class="tab_container">
			<div id="tab1" class="tab_content">
                <s:property value="page.html" escapeHtml="false"/>
			</div>
			<div id="tab2" class="tab_content">
			      <h2>Why rate products?</h2>
			      <p>Actually, the ratings are just a little bit of fun interaction and a way to let others know you have found the product useful. 
			      Products like fishing flies are always going to be more useful to some people than others depending on the time of year, your 
			      location and many other factors. That being said, try to use your own good judgement when purchasing fishing flies or any other 
			      products so that they are useful to you. Don't worry too much about wither or not others haven't found success yet.</p>
			</div>
		</div>
</section>

<script>
$('.hover-star').rating({
	focus: function(value, link){
		var tip = $('#rating-msg');
		tip[0].data = tip[0].data || tip.html();
		tip.html(link.title || 'value: '+value);
	},
	blur: function(value, link){
		var tip = $('#rating-msg');
		$('#rating-msg').html(tip[0].data || '');
	}
});

$('#purchase_form').submit(function() {
	
	$("input[type=submit]").addClass('disabledBlueButton');
	$("input[type=submit]").attr("disabled", "disabled");
	$('#product-page-indicator').css('display', 'inline');
	
    $.post("/purchase-product-submit", 
        $(this).serialize(),
        function(returnValue) {
         $('#product-page-indicator').css('display', 'none');
         $("input[type=submit]").removeAttr("disabled");
         $("input[type=submit]").removeClass('disabledBlueButton');
         
         if (returnValue.stockMessage != null) {
             $('#product_purchase_success_msg').html(returnValue.stockMessage);
             $('#product_purchase_success_msg').removeClass('succes_msg');
             $('#product_purchase_success_msg').addClass('error_msg');
             $('#product_purchase_success_msg').css({ opacity:0  });
             $('#product_purchase_success_msg').slideDown(500);
             $('#product_purchase_success_msg').fadeTo(1000, 1);
             
             if (returnValue.quantityLeftInStock > 0) {
                 jQuery('#quantity').val(returnValue.quantityLeftInStock);   
             }
         } else {
             $('#product_purchase_success_msg').html('<strong>Success: </strong>' + returnValue.shoppingCartEntry.quantity + ', ' + returnValue.shoppingCartEntry.name + ', have been added to you basket. <em>Add more</em> if required.' );
             $('#product_purchase_success_msg').removeClass('error_msg');
             $('#product_purchase_success_msg').addClass('succes_msg');
             $('#product_purchase_success_msg').css({ opacity:0  });
             $('#product_purchase_success_msg').slideDown(500);
             $('#product_purchase_success_msg').fadeTo(1000, 1);
             
             // update the cart info
             $('#shopping-cart-subtotal').html('<img src="/images/fugue/shopping-basket--arrow-green.png" alt="shopping basket"/> Checkout: ' + returnValue.shoppingCart.subTotalString + '  |  ' + 'Items: ' + returnValue.shoppingCart.itemCount);
         }
        }, 
        "json"
    );
      
    return false;
});

$('.hover-star').click(function() {
	$('#product_rating').submit();
});

$('#product_rating').submit(function() {
	$('.hover-star').rating('readOnly',true);
	$('.hover-star').unbind('click');
	
	$.post("/product-rating-submit", 
	    $(this).serialize(),
	    function(returnValue) {
		   $('#submit_rating_success_msg').css({ opacity:0  });
           $('#submit_rating_success_msg').slideDown(500);
		   $('#submit_rating_success_msg').fadeTo(1000, 1);
		   var tip = $('#rating-msg');
		   tip[0].data = returnValue.successMessage;
		   $('#rating-msg').html(returnValue.successMessage);
	    }, 
	    "json"
    );
	  
	return false;
});

jQuery('#minus_one').click(function(e) {
	e.preventDefault();
    var level = jQuery('#quantity').val();
    if (level == "") {
        level = 1;
    } else {
        level = parseInt(level);
    }
    if (level > 1) {
        level -= 1;
    }
    jQuery('#quantity').val(level);
});

jQuery('#add_one').click(function(e) {
	e.preventDefault();
    var level = jQuery('#quantity').val();
    if (level == "") {
        level = 1;
    } else {
        level = parseInt(level);
    }
    level += 1;
    jQuery('#quantity').val(level);
});

</script>