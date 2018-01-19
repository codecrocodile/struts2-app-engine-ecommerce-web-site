<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div class="grid_12">
    <header class="page">
        <h1>Suggested Patterns</h1>
    </header>
</div>
<div class="clear"></div>

<div class="grid_9">
    <p><img style="vertical-align: middle;" alt="green tick" src="/images/green-tick.png"><span style="font-size: 14px; font-weight: bold; color:red">Submission Successful</span></p>
    <p>
        Thanks for your suggestion, we will use this information at our next product review. The more information we collect the better we will get
        at stocking the right products, so thank you again and we hope you find something of interest in the Groovy Fly website.
    </p>
    
    <s:if test="email != null && email.trim() != ''">
        <s:if test="isSinglePatternSubmitted() == true">
            <p>We will contact you at the email address you provided if we start stocking the pattern that you have suggested.</p>
        </s:if>
        <s:else>
            <p>We will contact you at the email address you provided if we start stocking any of the patterns that you have suggested.</p>
        </s:else>
    </s:if>
    <s:else>
        <s:if test="isSinglePatternSubmitted() == true">
            <p>We won't be able to contact you, as you never supplied an email address, but please come back at a later time and check 
            to see if we have started to stock the pattern that you suggested.</p>
        </s:if>
        <s:else>
            <p>We won't be able to contact you, as you never supplied an email address, but please come back at a later time and check 
            to see if we have started to stock any of the patterns that you suggested.</p>
        </s:else>
    </s:else>
    
    <p style="font-weight: bolder;">Most popular suggestions:</p>
    <ol>
        <s:iterator value="mostPopular">
            <li><s:property /></li>
        </s:iterator>
    </ol>

</div>