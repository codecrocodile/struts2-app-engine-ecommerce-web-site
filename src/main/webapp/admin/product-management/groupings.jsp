<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<h1>Groupings</h1>

<form>
	<table id="datatable" class="gtable">
	    <thead>
	        <tr>
	            <th>Grouping Product Name</th>
	            <th>Image</th>
	            <th>No. of Products in Grouping</th>
	            <th>Actions</th>
	        </tr>
	    </thead>
	    <tbody>
	        <s:iterator value="products">
	            <tr>
	                <td><s:property value="name" /></td>
	                <td><img src="<s:property value="images.smallerImageUrl" />=s32" /></td>
	                <td><s:property value="childProductCount" /></td>
	                <td>
	                    <s:url action="groupings-read" var="readUrl" escapeAmp="false">
	                        <s:param name="productId" value="productId" />
	                    </s:url> 
	                    <s:url action="groupings-update" var="updateUrl" escapeAmp="false">
	                        <s:param name="productId" value="productId" />
	                    </s:url> 
	                    <s:url action="groupings-delete" var="deleteUrl" escapeAmp="false">
	                        <s:param name="productId" value="productId" />
	                    </s:url> 
	                    <a href="${readUrl}" title="View"><img src="images/icons/magnifier-medium.png" alt="View" /></a> 
	                    <a href="${updateUrl}" title="Edit"><img src="images/icons/edit.png" alt="Edit" /></a> 
	                    <img class="clickable" src="images/icons/cross.png" alt="Delete" onclick="deleteGrouping('${deleteUrl}')" />
	                </td>
	            </tr>
	        </s:iterator>
	    </tbody>
	</table>
</form>

<div id="dialog-confirm" title="Groovy Fly" style="display: none">
    <p style="float:left;" class="ui-icon ui-icon-alert"></p>
    <p style="float:right; width:90%">
        Deleting a grouping will not delete the products, only the grouping. This may leave
        the products un-displayed.
    </p>
</div>
