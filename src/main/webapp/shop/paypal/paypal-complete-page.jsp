<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>


<div class="grid_12">
    <header class="page">
        <h1>Order Complete</h1>
    </header>
</div>

<div class="clear"></div>

<section id="left_container">

<img src="/images/payment-progress-par-stage4.png" />


<div class="grid_9">
    <div style="margin-top: 30px;">
        <h2 style="display: inline; margin-right: 20px;">Thank you, your order is now complete!</h2><img style="vertical-align: bottom;" alt="green tick" src="/images/green-tick.png">
    </div>
    <s:if test="waitingForFundsToClear == false">
        <p>You will receive an order confirmation email shortly. Delivery normally takes between 2 - 5 working days. We will email you when your order is packed and posted.</p>    
    </s:if>
    <s:else>
        <p>You will receive an order confirmation email shortly.</p>
        <p><s:property value="fundsToClearMessage"/></p>
    </s:else>
    <p>Your order number is: <strong>#<s:property value="orderNumber" /></strong></p>
    <s:url action="download-receipt" var="downloadUrl">
        <s:param name="orderNumber" value="%{orderNumber}"/>
    </s:url>
    <a href="${downloadUrl}" class="button green small">Download PDF order receipt</a>
</div>

<div class="grid_9">
    <p style="margin-top: 50px;">We hope you have enjoyed shopping with Groovy Fly. If you have any suggestions on how your shopping experience could have been improved or for any other queries you can contact us at any time by sending us a message or email from our <a href="/customer-services/contact">customer services contact page</a>.</p>
    <p><b>Just before you go</b> why not check out some of the articles on our <a href="">groovy pages</a>. We're sure you will find something of interest.</p>
</div>

</section>