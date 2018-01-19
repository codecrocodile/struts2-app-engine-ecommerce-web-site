<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<h1>Products</h1>

<form>
    <div style="float:right; padding: 5px 0px 5px 0px">
        <s:url action="products-create" var="createUrl" escapeAmp="false"></s:url>
        <s:url action="products-createAndGroup" var="createAndGroupUrl" escapeAmp="false"></s:url>
        <a class="button" href="${createUrl}">Add Product</a>
        <a class="button" href="${createAndGroupUrl}">Add Products &amp; Grouping</a>
    </div>
    <div class="clear"></div>
    
    <table id="datatable" class="gtable">
        <thead>
            <tr>
                <th>Product Name</th>
                <th>Image</th>
                <th>Price</th>
                <th>Storage Location</th>
                <th>Stock Level</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <s:iterator value="products">
                <tr>
                    <td><s:property value="name" /></td>
                    <td><img src="<s:property value="images.smallerImageUrl" />=s32" /></td>
                    <td><s:property value="price" /></td>
                    <td><s:property value="storeageLocation" /></td>
                    <td><s:property value="stockLevel" /></td>
                    <td>
                        <s:url action="products-read" var="readUrl" escapeAmp="false">
                            <s:param name="productId" value="productId" />
                        </s:url> 
                        <s:url action="products-update" var="updateUrl" escapeAmp="false">
                            <s:param name="productId" value="productId" />
                        </s:url> 
                        <s:url action="products-delete" var="deleteUrl" escapeAmp="false">
                            <s:param name="productId" value="productId" />
                        </s:url> 
                        <a href="${readUrl}" title="View"><img src="images/icons/magnifier-medium.png" alt="View" /></a> 
                        <a href="${updateUrl}" title="Edit"><img src="images/icons/edit.png" alt="Edit" /></a> 
                        <img class="clickable" src="images/icons/cross.png" alt="Delete" onclick="deleteProduct('${deleteUrl}')" />
                    </td>
                </tr>
            </s:iterator>
        </tbody>
    </table>
</form>

<div id="dialog-confirm" title="Groovy Fly" style="display: none">
    <p style="float:left;" class="ui-icon ui-icon-alert"></p>
    <p style="float:right; width:90%">
        Deleting a product will remove the product from the system. It will then no longer show for display or re-order.
    </p>
</div>