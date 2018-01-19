<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<h1>Suppliers</h1>

<form>
	<div style="float:right; padding: 5px 0px 5px 0px">
		<s:url action="suppliers-create" var="createUrl" escapeAmp="false" />
		<a class="button" href="${createUrl}">Add Supplier</a>
	</div>
	<div class="clear"></div>
	
	<table id="datatable" class="gtable">
		<thead>
			<tr>
				<th>Company Name</th>
				<th>Short Code</th>
				<th>Telephone</th>
				<th>Email</th>
				<th>Actions</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="suppliers">
                <tr>
                    <td><s:property value="companyName" /></td>
                    <td><s:property value="shortCode" /></td>
                    <td><s:property value="tel" /></td>
                    <td><s:property value="email" /></td>
                    <td>
                        <s:url action="suppliers-read" var="readUrl" escapeAmp="false">
                            <s:param name="id" value="supplierId" />
                        </s:url> 
                        <s:url action="suppliers-update" var="updateUrl" escapeAmp="false">
							<s:param name="id" value="supplierId" />
						</s:url> 
						<s:url action="suppliers-delete" var="deleteUrl" escapeAmp="false">
							<s:param name="id" value="supplierId" />
						</s:url> 
						<a href="${readUrl}" title="View"><img src="images/icons/magnifier-medium.png" alt="View" /></a> 
                        <a href="${updateUrl}" title="Edit"><img src="images/icons/edit.png" alt="Edit" /></a> 
                        <img class="clickable" src="images/icons/cross.png" alt="Delete" onclick="deleteSupplier('${deleteUrl}')" />
                    </td>
                </tr>
			</s:iterator>
		</tbody>
	</table>
</form>

<div id="dialog-confirm" title="Groovy Fly" style="display: none">
    <p style="float:left;" class="ui-icon ui-icon-alert"></p>
    <p style="float:right; width:90%">
        Deleting a supplier will remove the supplier from the system. It will then no longer show for display or assignment.
    </p>
</div>

<script>

jQuery('#datatable').dataTable({
    "bPaginate": true,
    "bLengthChange": true,
    "bFilter": true,
    "bInfo": true,
    "bSort": true,
    "bAutoWidth": true
});

function deleteSupplier(url) {
    jQuery( "#dialog-confirm" ).dialog({
        resizable: false,
        modal: true,
        buttons: {
            "Delete Supplier": function() {
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