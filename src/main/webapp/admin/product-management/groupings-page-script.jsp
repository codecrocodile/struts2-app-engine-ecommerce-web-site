<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script>

jQuery(document).ready(
    function() {
        initPage();
    });
    
    
function initPage() {
    jQuery('#datatable').dataTable({
        "bPaginate": true,
        "bLengthChange": true,
        "bFilter": true,
        "bInfo": true,
        "bSort": true,
        "bAutoWidth": true
    });
}


function deleteGrouping(url) {
    jQuery( "#dialog-confirm" ).dialog({
        resizable: false,
        modal: true,
        buttons: {
            "Delete Grouping": function() {
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
