<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<section id="sidebar">
    <div>
    
        <div class="price_head">
            <div class="inside">SUMMARY</div>
        </div>

        <div class="summary-row">
            <span class="alignLeft">Sub-total</span><span id="shopping-cart-sidebar-subtotal" class="alignRight"><s:property value="shoppingCart.subTotalString" /></span>
        </div>
        <div class="summary-row">
            <span class="alignLeft">Postage &amp; Packaging</span><span id="shopping-cart-sidebar-postage" class="alignRight"><s:property value="shoppingCart.postageAndPackingString" /></span>
        </div>
        <div class="summary-row-thin"></div>
        <div class="summary-row-thin"></div>
        <div class="summary-row">
            <span class="alignLeft">Discount Applied</span><span id="shopping-cart-sidebar-discount" class="alignRight"><s:property value="shoppingCart.discountTotalString" /></span>
        </div>
        <div class="summary-row">
            <span class="alignLeft"><b>TOTAL</b></span><span id="shopping-cart-sidebar-total" class="alignRight total"><s:property value="shoppingCart.totalString" /></span>
        </div>
        <div class="price_footer">
            <div class="inside">
                <p>by placing your order you are agree to our <a href="/customer-services/terms-of-sale">terms of sale</a></p>
                <a href='<s:url action="confirm-paypal"/>'" class="button blue large" >Place your order</a>
            </div>
        </div>
    </div>
</section>
