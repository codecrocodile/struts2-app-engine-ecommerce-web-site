<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<h1>Categories</h1>

<s:set name="errorList" value="getActionErrors()" />

<s:iterator value="errorList">
    <div class="error msg">
        <s:property />
    </div>
</s:iterator>

<form>
    <div style="float:right; padding: 5px 0px 5px 0px">
        <s:url action="categories-create" var="createUrl" escapeAmp="false"></s:url>
        <a class="button" href="${createUrl}">Add Category</a>
    </div>
    <div class="clear"></div>
    
    <table id="table1" class="gtable">
        <thead>
            <tr>
                <th>Category</th>
                <th>Path</th>
                <th>Products in Category</th>
                <th>Move</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <s:iterator value="categories">
                <s:if test="path != '-- none --'"><%-- hide the root element --%>
                <tr>
                    <td><s:property value="name" /></td>
                    <td><s:property value="path" /></td>
                    <td><s:property value="productCountInCategory" /></td>
                    <td>
                        <s:url action="categories-down" var="downUrl" escapeAmp="false">
                            <s:param name="categoryId" value="categoryId" />
                        </s:url> 
                        <s:url action="categories-up" var="upUrl" escapeAmp="false">
                            <s:param name="categoryId" value="categoryId" />
                        </s:url> 
                        
                        <s:if test="lastInCategory == false">
                            <a href="${downUrl}" title="move down"><img src="images/icons/arrow-270-medium.png" alt="Move Down" /></a>
                        </s:if>
                        <s:else>
                            <img src="images/icons/spacer-medium.png" alt="Blocked Move Down" />
                        </s:else> 
                        <s:if test="firstInCategory == false">
                            <a href="${upUrl}" title="move up"><img src="images/icons/arrow-090-medium.png" alt="Move Up" /></a>
                        </s:if>
                        <s:else>
                            <img src="images/icons/spacer-medium.png" alt="Blocked Move Up" />
                        </s:else>  
                    </td>
                    <td>
                        <s:url action="categories-read" var="readUrl" escapeAmp="false">
                            <s:param name="categoryId" value="categoryId" />
                        </s:url> 
                        <s:url action="categories-update" var="updateUrl" escapeAmp="false">
                            <s:param name="categoryId" value="categoryId" />
                        </s:url> 
                        <s:url action="categories-delete" var="deleteUrl" escapeAmp="false">
                            <s:param name="categoryId" value="categoryId" />
                        </s:url> 
                        <a href="${readUrl}" title="View"><img src="images/icons/magnifier-medium.png" alt="View" /></a> 
                        <a href="${updateUrl}" title="Edit"><img src="images/icons/edit.png" alt="Edit" /></a> 
                        <img class="clickable" src="images/icons/cross.png" alt="Delete" onclick="deleteCategory('${deleteUrl}')" />
                    </td>
                </tr>
                </s:if>
            </s:iterator>
        </tbody>
    </table>
</form>


<div id="dialog-confirm" title="Groovy Fly" style="display: none">
    <p style="float:left;" class="ui-icon ui-icon-alert"></p>
    <p style="float:right; width:90%">
        Deleting a category will remove the category from the system. It will not delete the products in the category, but may make the products un-displayed.
    </p>
</div>

<script>

function deleteCategory(url) {
    jQuery( "#dialog-confirm" ).dialog({
        resizable: false,
        modal: true,
        buttons: {
            "Delete Category": function() {
                jQuery(this).dialog("close");
                window.location.href = url;
                
            },
            Cancel: function() {
                jQuery(this).dialog("close");
            }
        }
    });
}

</script>