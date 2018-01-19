<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>

<script src="/js/jquery.keyfilter-1.7.min.js" type="text/javascript"></script>

<s:form id="purchase_form" cssClass="productQuickPurchase">

<s:hidden name="productDisplayType" value="2"/>  
<s:hidden name="productId" value="%{product.productId}"/>

<fieldset>
    <h3 style="float:left"><s:property value="product.name" /></h3>
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
    <div id="product_purchase_success_msg" class="note_msg"><strong>Note:</strong> Shopping with Groovy Fly is fast, convenient and most importantly secure!</div>
</fieldset>      
</s:form>

        
<script>
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
             $('#product_purchase_success_msg').removeClass('note_msg');
             $('#product_purchase_success_msg').addClass('error_msg');
             
             if (returnValue.quantityLeftInStock > 0) {
            	 jQuery('#quantity').val(returnValue.quantityLeftInStock);	 
             }
         } else {
        	 $('#product_purchase_success_msg').html('<strong>Note:</strong> Shopping with Groovy Fly is fast, convenient and most importantly secure!' );
             $('#product_purchase_success_msg').removeClass('error_msg');
             $('#product_purchase_success_msg').addClass('note_msg');
             
             // update the cart info
             $('#shopping-cart-subtotal').html('<img src="/images/fugue/shopping-basket--arrow-green.png" alt="shopping basket"/> Checkout: ' + returnValue.shoppingCart.subTotalString + '  |  ' + 'Items:  ' + returnValue.shoppingCart.itemCount);
             $('#quickAddDialog').dialog('close');
         }
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