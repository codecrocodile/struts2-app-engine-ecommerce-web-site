<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script>

/* constants */
var GROUPED_PRODUCT_TABLE_ROW = 1;
var SEARCH_TABLE_ROW = 2;

var groupedProductIds = new Array();
<s:iterator value="product.products">
    groupedProductIds.push(<s:property value="productId" />);
</s:iterator>

var crudOp = "<s:property value="crudOperation"/>";

//Check for the various File API support.
if (window.File && window.FileReader && window.FileList && window.Blob) {
  // Great success! All the File APIs are supported.
} else {
  alert('The File APIs are not fully supported in this browser. You won\'t be able to upload images.');
}


//var productsInGroupArray = <s:property value="jsonChildProducts" escapeHtml="false" />;
var productSearchResultsArray = null; // this will be initalised when the search query returns


jQuery(document).ready(
    function() {
        initPage();
        initListeners();
        initFormValidation();
        
        if (crudOp != 'update') { // on update there willbe an image and there may be no need to change it
            jQuery("#grouping_form").rules("add", {
                'largePhoto': {
                    required: true
                },
                'mediumPhoto': {
                    required: true
                },
                'smallPhoto': {
                    required: true
                },
                'smallerPhoto': {
                    required: true
                },
            }); 
        }
    });
    
function initPage() {  
	
    jQuery('#datatable').dataTable({
        "bPaginate": true,
        "bLengthChange": false,
        "bFilter": false,
        "bInfo": true,
        "bSort": true,
        "bAutoWidth": true
    });
    
    jQuery('#grouped_products_table').dataTable({
        "bPaginate": true,
        "bLengthChange": false,
        "bFilter": false,
        "bInfo": true,
        "bSort": true,
        "bAutoWidth": true
    });
    
    /*
     * Fad in and out the attribute selection depending on the configuration of the group
     */
    if (jQuery('#product\\.productGroupingConfigId').val() == 1) {       
        jQuery('#product\\.attributeId1').val("");
        jQuery('#product\\.attributeId2').val("");
        jQuery('#product\\.attributeId3').val("");
        jQuery('#optional_attributes').hide();
    } else {
        jQuery('#optional_attributes').show();
    }
    
    jQuery('#large_image').change(handleFileSelectLarge);
    jQuery('#medium_image').change(handleFileSelectMedium);
    jQuery('#small_image').change(handleFileSelectSmall);
    jQuery('#smaller_image').change(handleFileSelectSmaller);
    
    // if page refreshed then get back some of the data that we need before it is saved to database.
    if (jQuery("#large_image")[0].files.length > 0) {
        if (sessionStorage.getItem("image_srcData")) {
            setPreviewDivContents('#preview', sessionStorage.getItem("image_srcData"), 
                    sessionStorage.getItem("image_size"), sessionStorage.getItem("image_type"));
        }       
    }
    
    if (jQuery("#medium_image")[0].files.length > 0) {
        if (sessionStorage.getItem("medium_image_srcData")) {
            setPreviewDivContents('#mediumPreview', sessionStorage.getItem("medium_image_srcData"), 
                    sessionStorage.getItem("medium_image_size"), sessionStorage.getItem("medium_image_type"));
        }       
    }
    
    if (jQuery("#small_image")[0].files.length > 0) {
        if (sessionStorage.getItem("small_image_srcData")) {
            setPreviewDivContents('#smallPreview', sessionStorage.getItem("small_image_srcData"), 
                    sessionStorage.getItem("small_image_size"), sessionStorage.getItem("small_image_type"));
        }       
    }
    
    if (jQuery("#smaller_image")[0].files.length > 0) {
        if (sessionStorage.getItem("smaller_image_srcData")) {
            setPreviewDivContents('#smallerPreview', sessionStorage.getItem("smaller_image_srcData"), 
                    sessionStorage.getItem("smaller_image_size"), sessionStorage.getItem("smaller_image_type"));
        }       
    }
    
    enableDisableSalesConfiguration();
}

function initListeners() {
	
	/* 
	 * If the group attribute configuration changes then the search results become invalid as they 
	 * are used as parameters to the search query so we should clear the search results.
	 */
    jQuery('#product\\.productGroupingConfigId').change(function() {
    	jQuery('#datatable').dataTable().fnClearTable();
    });
    jQuery('#product\\.attributeId1').change(function() {
        jQuery('#datatable').dataTable().fnClearTable();
    });
    jQuery('#product\\.attributeId2').change(function() {
        jQuery('#datatable').dataTable().fnClearTable();
    });
    jQuery('#product\\.attributeId3').change(function() {
        jQuery('#datatable').dataTable().fnClearTable();
    });
    
    /*
     * Fad in and out the attribute selection depending on the configuration of the group
     */
    jQuery('#product\\.productGroupingConfigId').change(function() {
        if (jQuery(this).val() == 2) {
            jQuery('#optional_attributes').fadeIn();
        } else {
            jQuery('#product\\.attributeId1').val("");
            jQuery('#product\\.attributeId2').val("");
            jQuery('#product\\.attributeId3').val("");
            jQuery('#optional_attributes').fadeOut();
        }
    });
    
    jQuery('button[id=save_button]').click(function(e) {
        e.preventDefault();
        
        if (jQuery('#grouping_form').valid() == true) {
        	// remove all disabled attributes before submitting the form or html standard says ignore these fields and doen't post them
            jQuery('select').removeAttr('disabled');	
        }
        
        jQuery('#product\\.childProductIds').val(groupedProductIds);
        
        
        
        jQuery('#grouping_form').submit();
    });

    jQuery('button[id=return_button]').click(function(e) {
        e.preventDefault();

        window.location.href = "groupings-list";

        return true;
    });

    jQuery('button[id=cancel_button]').click(function(e) {
        e.preventDefault();

        window.location.href = "groupings-list";

        return true;
    });
}

function initFormValidation() {
    jQuery("#grouping_form").validate({
        
        rules: {
            'product.categoryId': {
                required: true
            },
            
            'product.name': {
                required: true,
                minlength: 3
            },
            
            'product.images.altTagDescription': {
                required: true
            }
            
        },
    
        errorPlacement: function(error, element) {
            
            if (element.attr("id") == "large_image" ) {
                error.insertBefore("#preview+small");
            } else if (element.attr("id") == "medium_image" ) {
                error.insertBefore("#mediumPreview+small");
            } else if (element.attr("id") == "small_image" ) {
                error.insertBefore("#smallPreview+small");
            } else if (element.attr("id") == "smaller_image" ) {
                error.insertBefore("#smallerPreview+small");
            } else {
                error.insertAfter(element);
            }
        }
    });
}


function handleFileSelectLarge(evt) {
    
    var file = evt.target.files[0]; // File object

    // only process image file.
    if (file.type.match('image.*')) {
        
        var reader = new FileReader();
        
        // closure to capture the file information.
        reader.onload = (function(theFile) {
            return function(e) {
                sessionStorage.setItem("image_name", theFile.name);
                sessionStorage.setItem("image_srcData", e.target.result);
                sessionStorage.setItem("image_size", theFile.size);
                sessionStorage.setItem("image_type", theFile.type);
                
                setPreviewDivContents('#preview', e.target.result, theFile.size, theFile.type);
                
                var altString = theFile.name.substring(0, theFile.name.indexOf("."));
                altString = altString.replace(/[-_]/g, " ");
              
                jQuery('#product\\.images\\.altTagDescription').val(altString);
            };
        })(file);
    }
    
    reader.readAsDataURL(file);
}

function handleFileSelectMedium(evt) {
    
    var file = evt.target.files[0]; // File object

    // only process image file.
    if (file.type.match('image.*')) {
        
        var reader = new FileReader();
        
        // closure to capture the file information.
        reader.onload = (function(theFile) {
            return function(e) {
                sessionStorage.setItem("medium_image_name", theFile.name);
                sessionStorage.setItem("medium_image_srcData", e.target.result);
                sessionStorage.setItem("medium_image_size", theFile.size);
                sessionStorage.setItem("medium_image_type", theFile.type);
                
                setPreviewDivContents('#mediumPreview', e.target.result, theFile.size, theFile.type);
                
            };
        })(file);
    }
    
    reader.readAsDataURL(file);
}

function handleFileSelectSmall(evt) {
    
    var file = evt.target.files[0]; // File object

    // only process image file.
    if (file.type.match('image.*')) {
        
        var reader = new FileReader();
        
        // closure to capture the file information.
        reader.onload = (function(theFile) {
            return function(e) {
                sessionStorage.setItem("small_image_name", theFile.name);
                sessionStorage.setItem("small_image_srcData", e.target.result);
                sessionStorage.setItem("small_image_size", theFile.size);
                sessionStorage.setItem("small_image_type", theFile.type);
                
                setPreviewDivContents('#smallPreview', e.target.result, theFile.size, theFile.type);
                
            };
        })(file);
    }
    
    reader.readAsDataURL(file);
}

function handleFileSelectSmaller(evt) {
    
    var file = evt.target.files[0]; // File object

    // only process image file.
    if (file.type.match('image.*')) {
        
        var reader = new FileReader();
        
        // closure to capture the file information.
        reader.onload = (function(theFile) {
            return function(e) {
                sessionStorage.setItem("smaller_image_name", theFile.name);
                sessionStorage.setItem("smaller_image_srcData", e.target.result);
                sessionStorage.setItem("smaller_image_size", theFile.size);
                sessionStorage.setItem("smaller_image_type", theFile.type);
                
                setPreviewDivContents('#smallerPreview', e.target.result, theFile.size, theFile.type);
                
            };
        })(file);
    }
    
    reader.readAsDataURL(file);
}

function setPreviewDivContents(elementId, srcData, size, type) {
    var uploadStatusLine;
    
    if (size <= 500000) { // 1 mb max
        uploadStatusLine = '<li><img src="images/icons/tick-circle.png" />OK</li>';
    } else if (size <= 1000000) {
        uploadStatusLine = '<li><img src="images/icons/exclamation.png" />Try to reduce the file size if you can!</li>';
    } else {
        uploadStatusLine = '<li><img src="images/icons/exclamation-red.png" />File to large!</li>';
    }
    
    var sizeLine =  '<li>Size: ' + size + ' Bytes</li>';
    var typeLine =  '<li>Media Type: ' + type + '</li>';
    
    jQuery(elementId).html([ 
        '<img class="thumb" src="', srcData, '" />',
        '<ul>', 
        uploadStatusLine,
        sizeLine,
        typeLine,
        '</ul>',
        '<div class="clear"></div>'
        ].join(''));
}

/*
 * If products have been added to the group then the attribute configuration should be disabled. This
 * will ensure that the product's attributes in the group match the attribute configuration for the group.
 */
function enableDisableSalesConfiguration() {
    if (groupedProductIds.length > 0) {
        jQuery('#product\\.productGroupingConfigId').attr("disabled", "disabled");
        
        jQuery('#product\\.attributeId1').attr("disabled", "disabled");
        jQuery('#product\\.attributeId2').attr("disabled", "disabled");
        jQuery('#product\\.attributeId3').attr("disabled", "disabled");
    } else {
        jQuery('#product\\.productGroupingConfigId').removeAttr("disabled");
        
        jQuery('#product\\.attributeId1').removeAttr("disabled");
        jQuery('#product\\.attributeId2').removeAttr("disabled");
        jQuery('#product\\.attributeId3').removeAttr("disabled");     
    }
}


function setSearchFormVariables() {
	
	// set hidden form query params
	jQuery('#productSearchQuery\\.attributeId1').val(jQuery('#product\\.attributeId1').val());
	jQuery('#productSearchQuery\\.attributeId2').val(jQuery('#product\\.attributeId2').val());
	jQuery('#productSearchQuery\\.attributeId3').val(jQuery('#product\\.attributeId3').val());
	jQuery('#productSearchQuery\\.productIdExcludeString').val(groupedProductIds.toString());
	
}

function deleteFromGroupedProducts(event, productId) {
	event.preventDefault();
	var nRow = jQuery(event.target).parents('tr')[0];
	
	for ( var i = 0; i < groupedProductIds.length; i++) {
	    if (groupedProductIds[i] == productId) {
	    	groupedProductIds.splice(i, 1);
            break;
        }
	}

	jQuery('#grouped_products_table').dataTable().fnDeleteRow(nRow);
	
	enableDisableSalesConfiguration();
}

/*
 * Called when search results are returned.
 */
function initProductArray(jsonProductsString) {
	productSearchResultsArray = JSON.parse(jsonProductsString);
	
	// override toString method for any attributes
	for ( var key in productSearchResultsArray) {
		var product = productSearchResultsArray[key];
		var toString = function() {
            return this.description + ":" + this.choosenAttributeValue.description; 
        };
        
		if ('attribute1' in product) {
			product.attribute1.toString = toString;
			product.attributeString = product.attribute1.toString();
		}
	    if ('attribute2' in product) {
            product.attribute2.toString = toString;
            product.attributeString += "," + product.attribute2.toString();
	    }
	    if ('attribute3' in product) {
	        product.attribute3.toString = toString;
	        product.attributeString += "," + product.attribute3.toString();
	    }
	}
	
	addRowsToProductSearchResults();
}

function addRowsToProductSearchResults() {

	// first clear any previous search results
	jQuery('#datatable').dataTable().fnClearTable();

	// now using the product array populate the table
	for ( var i = 0; i < productSearchResultsArray.length; i++) {
		var product = productSearchResultsArray[i];
	    var tableRow = createTableRowArray(SEARCH_TABLE_ROW, product);
		jQuery('#datatable').dataTable().fnAddData(tableRow);
	}
}

function createTableRowArray(tableRowTypeConst, product) {

	var row = new Array();
	row.push(product.name);
	row.push('<img src="' + product.images.smallImageUrl + '=s32" />');
	row.push(product.attributeString);
	row.push(product.price);
	row.push(product.storeageLocation);
	row.push(product.stockLevel);

	var actionCol = null;

	// create action column value
	if (tableRowTypeConst == SEARCH_TABLE_ROW) {
		actionCol = '<td><input type="image" src="images/icons/arrow-up-add.png" alt="add to grouped products" onclick="moveToGroupedProducts(event, '
				+ product.productId + ')"/></td>';
	} else if (tableRowTypeConst == GROUPED_PRODUCT_TABLE_ROW) {
		actionCol = '<td><input type="image" src="images/icons/cross.png" alt="add to grouped products" onclick="deleteFromGroupedProducts(event, '
				+ product.productId + ')" /></td>';
	}

	row.push(actionCol);

	return row;
}

function moveToGroupedProducts(event, productId) {
	event.preventDefault();
	
	groupedProductIds.push(productId);

	var nRow = jQuery(event.target).parents('tr')[0];

	var index = -1;
	for ( var i = 0; i < productSearchResultsArray.length; i++) {
		var product = productSearchResultsArray[i];
		if (product.productId == productId) {
			index = i;
		}
	}

	var newRowData = createTableRowArray(GROUPED_PRODUCT_TABLE_ROW, productSearchResultsArray[index]);

	jQuery('#datatable').dataTable().fnDeleteRow(nRow);
	jQuery('#grouped_products_table').dataTable().fnAddData(newRowData);
	
	enableDisableSalesConfiguration();
}
</script>

