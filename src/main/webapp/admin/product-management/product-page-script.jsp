<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script>

var attribIdToAttribValueMap = <s:property value="jsAttrubuteArrayLiteral"/>;
var crudOp = "<s:property value="crudOperation"/>";

//Check for the various File API support.
if (window.File && window.FileReader && window.FileList && window.Blob) {
    // Great success! All the File APIs are supported.
} else {
    alert('The File APIs are not fully supported in this browser. You won\'t be able to upload images.');
}

jQuery(document).ready(
    function() {
        initPage();
        initListeners();
        initFormValidation();
        
        if (crudOp != 'update') { // on update there will be an image and there may be no need to change it
        	jQuery("#product_form").rules("add", {
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

function handleFileSelectLarge(evt) {
    
    var file = evt.target.files[0]; // File object

    // only process image file.
    if (file.type.match('image.*')) {
        
        var reader = new FileReader();
        
        // closure to capture the file information.
        reader.onload = (function(theFile) {
            return function(e) {
	            sessionStorage.setItem("product_unit_image_name", theFile.name);
	            sessionStorage.setItem("product_unit_image_srcData", e.target.result);
	            sessionStorage.setItem("product_unit_image_size", theFile.size);
	            sessionStorage.setItem("product_unit_image_type", theFile.type);
	            
	            setPreviewDivContents('#product_unit_preview', e.target.result, theFile.size, theFile.type);
	            
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
                sessionStorage.setItem("product_unit_medium_image_name", theFile.name);
                sessionStorage.setItem("product_unit_medium_image_srcData", e.target.result);
                sessionStorage.setItem("product_unit_medium_image_size", theFile.size);
                sessionStorage.setItem("product_unit_medium_image_type", theFile.type);
                
                setPreviewDivContents('#product_unit_medium_preview', e.target.result, theFile.size, theFile.type);
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
                sessionStorage.setItem("product_unit_small_image_name", theFile.name);
                sessionStorage.setItem("product_unit_small_image_srcData", e.target.result);
                sessionStorage.setItem("product_unit_small_image_size", theFile.size);
                sessionStorage.setItem("product_unit_small_image_type", theFile.type);
                
                setPreviewDivContents('#product_unit_small_preview', e.target.result, theFile.size, theFile.type);
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
	            sessionStorage.setItem("product_unit_smaller_image_name", theFile.name);
	            sessionStorage.setItem("product_unit_smaller_image_srcData", e.target.result);
	            sessionStorage.setItem("product_unit_smaller_image_size", theFile.size);
	            sessionStorage.setItem("product_unit_smaller_image_type", theFile.type);
	            
	            setPreviewDivContents('#product_unit_smaller_preview', e.target.result, theFile.size, theFile.type);
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

function setAttributeValues(source, target) {
    jQuery(target).empty();
     
     var attribId = jQuery(source).val(); // get attrib id 
     var values = attribIdToAttribValueMap[attribId]; // gives associative array of values
     for (var key in values) {
         if (values.hasOwnProperty(key)) {
             jQuery(target).append('<option value=\"' + key + '\">' + values[key] + '</option>');
         }
     }
}

function Pair(attr, attrVal) {
	this.attr = attr;
	this.attrVal = attrVal;
	
	this.equals = function(pair) {
		if (this.attr == pair.attr && this.attrVal == pair.attrVal) {
			return true;
		} 
		
		return false;
	};
}

function validateProducts() {

    if (sessionStorage.getItem("product_unit_image_size") >= 1000000) {
        return "The image you have used is too large. You will need to reduce the size.";
    }
	
	// product units can't have the same attribute more than once e.g hook size 12 and 10
    var pairArray = new Array();
	var pair1 = new Pair(jQuery('#product\\.attributeId1').val(), 1);
	var pair2 = new Pair(jQuery('#product\\.attributeId2').val(), 2);
	var pair3 = new Pair(jQuery('#product\\.attributeId3').val(), 3);
	pairArray.push(pair1);
	pairArray.push(pair2);
	pairArray.push(pair3);
	
    for ( var i = 0; i < pairArray.length; i++) {
        var p = pairArray[i];
      
        for ( var j = 0; j < pairArray.length; j++) {
            var pair = pairArray[j];
            
            if (pair.attr == p.attr && pair.attrVal != p.attrVal && pair.attr != 0) {
                return "Duplicate attribute on the product units. Please ammend.";
            }
        }
    }

	return "";
}
    
/*
 * Set-up the state of the page
 */
function initPage() {
	
    setAttributeValues('#product\\.attributeId1', '#product\\.attributeValueId1');
    setAttributeValues('#product\\.attributeId2', '#product\\.attributeValueId2');
    setAttributeValues('#product\\.attributeId3', '#product\\.attributeValueId3');
	
    // if page refreshed then get back some of the data that we need before it is saved to database.
    if (jQuery("#product_unit_large_image")[0].files.length > 0) {
        if (sessionStorage.getItem("product_unit_image_srcData")) {
            setPreviewDivContents('#product_unit_preview', sessionStorage.getItem("product_unit_image_srcData"), 
            		sessionStorage.getItem("product_unit_image_size"), sessionStorage.getItem("product_unit_image_type"));
        }    	
    }
    if (jQuery("#product_unit_medium_image")[0].files.length > 0) {
        if (sessionStorage.getItem("product_unit_medium_image_srcData")) {
            setPreviewDivContents('#product_unit_medium_preview', sessionStorage.getItem("product_unit_medium_image_srcData"), 
                    sessionStorage.getItem("product_unit_medium_image_size"), sessionStorage.getItem("product_unit_medium_image_type"));
        }       
    }
    if (jQuery("#product_unit_small_image")[0].files.length > 0) {
        if (sessionStorage.getItem("product_unit_small_image_srcData")) {
            setPreviewDivContents('#product_unit_small_preview', sessionStorage.getItem("product_unit_small_image_srcData"), 
                    sessionStorage.getItem("product_unit_small_image_size"), sessionStorage.getItem("product_unit_small_image_type"));
        }       
    }
    if (jQuery("#product_unit_smaller_image")[0].files.length > 0) {
        if (sessionStorage.getItem("product_unit_smaller_image_srcData")) {
            setPreviewDivContents('#product_unit_smaller_preview', sessionStorage.getItem("product_unit_smaller_image_srcData"), 
                    sessionStorage.getItem("product_unit_smaller_image_size"), sessionStorage.getItem("product_unit_smaller_image_type"));
        }       
    }
    
    jQuery('#datatable').dataTable({
        "bPaginate": false,
        "bLengthChange": false,
        "bFilter": true,
        "bSort": false,
        "bAutoWidth": false
    } );
    
    
    if (<s:property value="product.inNoOfGroupings" /> > 0) {
        jQuery('#product\\.attributeId1').attr('disabled', 'disabled'); 
        jQuery('#product\\.attributeValueId1').attr('disabled', 'disabled'); 
        jQuery('#product\\.attributeId2').attr('disabled', 'disabled'); 
        jQuery('#product\\.attributeValueId2').attr('disabled', 'disabled'); 
        jQuery('#product\\.attributeId3').attr('disabled', 'disabled'); 
        jQuery('#product\\.attributeValueId3').attr('disabled', 'disabled'); 
    }
    
    if (jQuery("#product\\.price").val() == "0") {
        jQuery("#product\\.price").val("0.00");
    }
  
}   

/*
 * Set-up the listeners for the page
 */
function initListeners() {
	 
    jQuery('button[id=save_button]').click(function(e) {
        e.preventDefault();
        
        // validate the form that is displayed
        if (jQuery("#product_form").valid() == true) {
        	
        	var errorMessage = "";
        	errorMessage = validateProducts();
        	if (errorMessage == "") {
        		
                jQuery('#product\\.attributeId1').removeAttr('disabled'); 
                jQuery('#product\\.attributeValueId1').removeAttr('disabled'); 
                jQuery('#product\\.attributeId2').removeAttr('disabled'); 
                jQuery('#product\\.attributeValueId2').removeAttr('disabled'); 
                jQuery('#product\\.attributeId3').removeAttr('disabled'); 
                jQuery('#product\\.attributeValueId3').removeAttr('disabled'); 
        		
                jQuery('#product_form').submit();
                
                return true;	
        	} else {
        		jQuery("<div class=\"error msg\">" + errorMessage + "</div>").insertBefore(jQuery("#product_form"));
        	    jQuery('.msg').click(function() {
        	        jQuery(this).fadeTo('slow', 0);
        	        jQuery(this).slideUp(341);
        	    });
        	}
        }

        return false;
    });

    jQuery('button[id=return_button]').click(function(e) {
        e.preventDefault();

        window.location.href = "products-list";

        return true;
    });

    jQuery('button[id=cancel_button]').click(function(e) {
        e.preventDefault();

        window.location.href = "products-list";

        return true;
    });
    
    jQuery('#product\\.attributeId1').change(function() {
        setAttributeValues('#product\\.attributeId1', '#product\\.attributeValueId1');
    });
    
    jQuery('#product\\.attributeId2').change(function() {
        setAttributeValues('#product\\.attributeId2', '#product\\.attributeValueId2');
    });
    
    jQuery('#product\\.attributeId3').change(function() {
        setAttributeValues('#product\\.attributeId3', '#product\\.attributeValueId3');
    });
    
    jQuery('#product_unit_large_image').change(handleFileSelectLarge);
    jQuery('#product_unit_medium_image').change(handleFileSelectMedium);
    jQuery('#product_unit_small_image').change(handleFileSelectSmall);
    jQuery('#product_unit_smaller_image').change(handleFileSelectSmaller);
    
    jQuery('#minus_one').click(function(e) {
        var level = jQuery('#product\\.stockLevel').val();
        if (level == "") {
            level = 0;
        } else {
            level = parseInt(level);
        }
        if (level >= 1) {
            level -= 1;
        }
        jQuery('#product\\.stockLevel').val(level);
    });
    
    jQuery('#plus_one').click(function(e) {
        var level = jQuery('#product\\.stockLevel').val();
        if (level == "") {
            level = 0;
        } else {
            level = parseInt(level);
        }
        level += 1;
        jQuery('#product\\.stockLevel').val(level);
    });
    
    jQuery('#minus_twelve').click(function(e) {
        var level = jQuery('#product\\.stockLevel').val();
        if (level == "") {
            level = 0;
        } else {
            level = parseInt(level);
        }
        if (level >= 12) {
            level -= 12;
        }
        jQuery('#product\\.stockLevel').val(level);
    });
    
    jQuery('#plus_twelve').click(function(e) {
        var level = jQuery('#product\\.stockLevel').val();
        if (level == "") {
            level = 0;
        } else {
            level = parseInt(level);
        }
        level += 12;
        jQuery('#product\\.stockLevel').val(level);
    });
}


/*
 * There is a couple of strange things about this validation plugin, jquery and ognl.
 * 
 */
function initFormValidation() {
	
    if (crudOp == 'update') { // on update there will be an image and there may be no need to change it
        jQuery("#product_form").validate({
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
                },
                'product.supplierId': "required",
                'product.sku': "required",
                'product.price': "required",
                'product.priceRuleId': "required",
                'product.vatRuleId': "required",
                'product.storeageLocation': "required",
                'product.stockLevel': "required",
                'product.statusId': "required",
            },
        });
    } else {
        jQuery("#product_form").validate({
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
                },
                'product.supplierId': "required",
                'product.sku': "required",
                'product.price': "required",
                'product.priceRuleId': "required",
                'product.vatRuleId': "required",
                'product.storeageLocation': "required",
                'product.stockLevel': "required",
                'product.statusId': "required",
                
                'photo': {
                    required: true
                },
                
                'smallPhoto': {
                    required: true
                },
            },
        
            errorPlacement: function(error, element) {
                if (element.attr("id") == "product_unit_large_image" ) {
                    error.insertBefore("#product_unit_preview+small");
                } else if (element.attr("id") == "product_unit_medium_image") {
                    error.insertBefore("#product_unit_medium_preview+small");
                } else if (element.attr("id") == "product_unit_small_image") {
                    error.insertBefore("#product_unit_small_preview+small");
                } else if (element.attr("id") == "product_unit_smaller_image") {
                    error.insertBefore("#product_unit_smaller_preview+small");
                } else {
                    error.insertAfter(element);
                }
            }
        });
    }

}

</script>