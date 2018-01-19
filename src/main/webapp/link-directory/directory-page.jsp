<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div class="grid_12">
    <header class="page">
        <h1><s:property value="websiteCategory.name"/></h1>
    </header>
</div>
<div class="clear"></div>

<section style="display:block; float:left">

<s:if test="websiteCategory.websites.size == 0">
    <p style="margin: 20px 10px">There is nothing added to this category yet. Why not be the first?</p>
</s:if>

<div class="grid_6">
<s:iterator value="websiteCategory.websites">
	<div class="listing" style="padding: 2px 15px; margin: 5px 0px; background-color: #eee" >
	    <div>
	      <h4 style="margin:0px; float:left; display:inline-block;" ><s:property value="title" /></h4>
	      <p style="margin:0px; float:right; display:inline-block; "><s:property value="dateAdded" /></p>
	      <div class="clear"></div>
	    </div>
	    <p style="margin:3px"><s:property value="description" /></p>
	    <s:if test="followLink == true">
	      <p style="margin:3px"><a href="<s:property value="websiteUrl" />" target="_blank"><s:property value="websiteUrl" /></a></p>
	    </s:if>
	    <s:else>
	      <p style="margin:3px"><a href="<s:property value="websiteUrl" />" target="_blank" rel="nofollow"><s:property value="websiteUrl" /></a></p>
	    </s:else>
	</div>
</s:iterator>
</div> <!-- end container -->

<div class="grid_3" >
    <s:property value="websiteCategory.description" />
</div>


<div class="clear"></div>

</section>




