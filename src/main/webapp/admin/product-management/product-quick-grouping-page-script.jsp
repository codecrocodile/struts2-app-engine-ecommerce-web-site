<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script>

var attribIdToAttribValueMap = <s:property value="jsAttrubuteArrayLiteral"/>;

//Check for the various File API support.
if (window.File && window.FileReader && window.FileList && window.Blob) {
    // Great success! All the File APIs are supported.
} else {
    alert('The File APIs are not fully supported in this browser. You won\'t be able to upload images.');
}

/* class definitions */
function Product() {
    this.isMasterProduct = false;
    this.productId = -1;
    
    // milliseconds since epoch to be used as id before saved to db
    this.newTempProductId = -1;
        
    this.productUnitGroupingConfigId = -1;
    this.attribute1Id = -1;
    this.attribute2Id = -1;
    this.attribute3Id = -1;
    this.attributeValue1Id = -1;
    this.attributeValue2Id = -1;
    this.attributeValue3Id = -1;
    
    this.name = "";
    this.urlAlias = "";
    this.categoryId = -1;
    this.description = "";
    
    this.largeImageName;
    this.largeImageType;
    this.largeImageSize;
    this.largeImageSrcData;
    
    this.mediumImageName;
    this.mediumImageType;
    this.mediumImageSize;
    this.mediumImageSrcData;
    
    this.smallImageName;
    this.smallImageType;
    this.smallImageSize;
    this.smallImageSrcData;
    
    this.smallerImageName;
    this.smallerImageType;
    this.smallerImageSize;
    this.smallerImageSrcData;
    
    this.largeImageAltTag;
    
    this.supplierId = -1;
    this.sku = "";
    this.price = "";
    this.priceRuleId = -1;
    this.vatRateId = -1;
    this.storeageLocation = "";
    this.stockLevel = -1;
    this.statusId = -1;
    
    this.page;
}

function Page() {
    this.isMasterPage = false;
    this.metaTagTitle = "";
    this.metaTagKeywords = "";
    this.metaTagDescription = "";
    this.productDescription = "";
}

function ProductModel() {
    this.masterProduct = new Product();
    this.productUnits = new Array();
    
    this.getProduct = function getProduct(id) {
        for ( var i = 0; i < this.productUnits.length; i++) {
            var prod = this.productUnits[i];
            
            if (prod.productId == id) {
                return this.productUnits[i];
            }
        }
    };
    
    this.getTempProduct = function getTempProduct(id) {
        for ( var i = 0; i < this.productUnits.length; i++) {
            var prod = this.productUnits[i];
            
            if (prod.newTempProductId == id) {
                return this.productUnits[i];
            }
        }
    };
    
    this.removeProduct = function removeProduct(id) {
        var indexToDelete = -1;
        for ( var i = 0; i < this.productUnits.length; i++) {
            var prod = this.productUnits[i];
            
            if (prod.productId == id) {
                
                indexToDelete = i;
                
                break;
            }
        }
        
        if (indexToDelete != -1) {
            this.productUnits.splice(indexToDelete, 1); 
        }
    };
    
    this.removeTempProduct = function removeTempProduct(id) {
        var indexToDelete = -1;
        for ( var i = 0; i < this.productUnits.length; i++) {
            var prod = this.productUnits[i];
            
            if (prod.newTempProductId == id) {
                
                indexToDelete = i;
                
                break;
            }
        }
        
        if (indexToDelete != -1) {
            this.productUnits.splice(indexToDelete, 1); 
        }
    };
}

/** Class for creating an xml document of the catch records that can be sent to the server for processing (usually insert or update) */
function XMLDocumentManager() {
    this.xmlDoc;
    this.rootNode;
    this.createXmlDoc = function() {
           
        if (document.implementation && document.implementation.createDocument) {
            this.xmlDoc = document.implementation.createDocument("", "", null);
        } else if (window.ActiveXObject) {
            this.xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
        }

        if (!this.xmlDoc) {
            return;
        }

        this.rootNode = this.xmlDoc.createElement("Products");
        this.xmlDoc.appendChild(this.rootNode);
    };

    this.createProductElement = function createProductElement(p) {
        
        // create all the elements
        var product = this.xmlDoc.createElement('product');
        
        var isMasterProduct = this.xmlDoc.createElement('isMasterProduct');
        var productId = this.xmlDoc.createElement('productId');
        
        var productUnitGroupingConfigId = this.xmlDoc.createElement('productUnitGroupingConfigId');
        var attribute1Id = this.xmlDoc.createElement('attribute1Id');
        var attribute2Id = this.xmlDoc.createElement('attribute2Id');
        var attribute3Id = this.xmlDoc.createElement('attribute3Id');
        var attributeValue1Id = this.xmlDoc.createElement('attributeValue1Id');
        var attributeValue2Id = this.xmlDoc.createElement('attributeValue2Id');
        var attributeValue3Id = this.xmlDoc.createElement('attributeValue3Id');
        
        var name = this.xmlDoc.createElement('name');
        var urlAlias = this.xmlDoc.createElement('urlAlias');
        var categoryId = this.xmlDoc.createElement('categoryId');
        var description = this.xmlDoc.createElement('description');
        
        var image = this.xmlDoc.createElement('image');
        var largeImageName = this.xmlDoc.createElement('largeImageName');
        var largeImageType = this.xmlDoc.createElement('largeImageType');
        var largeImageSize = this.xmlDoc.createElement('largeImageSize');
        var largeImageSrcData = this.xmlDoc.createElement('largeImageSrcData');
        
        var mediumImageName = this.xmlDoc.createElement('mediumImageName');
        var mediumImageType = this.xmlDoc.createElement('mediumImageType');
        var mediumImageSize = this.xmlDoc.createElement('mediumImageSize');
        var mediumImageSrcData = this.xmlDoc.createElement('mediumImageSrcData');
        
        var smallImageName = this.xmlDoc.createElement('smallImageName');
        var smallImageType = this.xmlDoc.createElement('smallImageType');
        var smallImageSize = this.xmlDoc.createElement('smallImageSize');
        var smallImageSrcData = this.xmlDoc.createElement('smallImageSrcData');
        
        var smallerImageName = this.xmlDoc.createElement('smallerImageName');
        var smallerImageType = this.xmlDoc.createElement('smallerImageType');
        var smallerImageSize = this.xmlDoc.createElement('smallerImageSize');
        var smallerImageSrcData = this.xmlDoc.createElement('smallerImageSrcData');
        
        var largeImageAltTag = this.xmlDoc.createElement('largeImageAltTag');
        
        var supplierId = this.xmlDoc.createElement('supplierId');
        var sku = this.xmlDoc.createElement('sku');
        var price = this.xmlDoc.createElement('price');
        var priceRuleId = this.xmlDoc.createElement('priceRuleId');
        var vatRateId = this.xmlDoc.createElement('vatRateId');
        var storeageLocation = this.xmlDoc.createElement('storeageLocation');
        var stockLevel = this.xmlDoc.createElement('stockLevel');
        var statusId = this.xmlDoc.createElement('statusId');
        
        var page = this.xmlDoc.createElement('page');
        var metaTagTitle = this.xmlDoc.createElement('metaTagTitle');
        var metaTagKeywords = this.xmlDoc.createElement('metaTagKeywords');
        var metaTagDescription = this.xmlDoc.createElement('metaTagDescription');
        var productDescription = this.xmlDoc.createElement('productDescription');


        // add the text to the elements        
        isMasterProduct.appendChild(this.xmlDoc.createTextNode(p.isMasterProduct));
        productId.appendChild(this.xmlDoc.createTextNode(p.productId));

        productUnitGroupingConfigId.appendChild(this.xmlDoc.createTextNode(p.productUnitGroupingConfigId));
        attribute1Id.appendChild(this.xmlDoc.createTextNode(p.attribute1Id));
        attribute2Id.appendChild(this.xmlDoc.createTextNode(p.attribute2Id));
        attribute3Id.appendChild(this.xmlDoc.createTextNode(p.attribute3Id));
        attributeValue1Id.appendChild(this.xmlDoc.createTextNode(p.attributeValue1Id));
        attributeValue2Id.appendChild(this.xmlDoc.createTextNode(p.attributeValue2Id));
        attributeValue3Id.appendChild(this.xmlDoc.createTextNode(p.attributeValue3Id));
        name.appendChild(this.xmlDoc.createTextNode(p.name));
        urlAlias.appendChild(this.xmlDoc.createTextNode(p.urlAlias));
        categoryId.appendChild(this.xmlDoc.createTextNode(p.categoryId));
        description.appendChild(this.xmlDoc.createTextNode(p.description));
        
        largeImageName.appendChild(this.xmlDoc.createTextNode(p.largeImageName));
        largeImageType.appendChild(this.xmlDoc.createTextNode(p.largeImageType));
        largeImageSize.appendChild(this.xmlDoc.createTextNode(p.largeImageSize));
        largeImageSrcData.appendChild(this.xmlDoc.createTextNode(p.largeImageSrcData));
        
        mediumImageName.appendChild(this.xmlDoc.createTextNode(p.mediumImageName));
        mediumImageType.appendChild(this.xmlDoc.createTextNode(p.mediumImageType));
        mediumImageSize.appendChild(this.xmlDoc.createTextNode(p.mediumImageSize));
        mediumImageSrcData.appendChild(this.xmlDoc.createTextNode(p.mediumImageSrcData));        
        
        smallImageName.appendChild(this.xmlDoc.createTextNode(p.smallImageName));
        smallImageType.appendChild(this.xmlDoc.createTextNode(p.smallImageType));
        smallImageSize.appendChild(this.xmlDoc.createTextNode(p.smallImageSize));
        smallImageSrcData.appendChild(this.xmlDoc.createTextNode(p.smallImageSrcData));
        
        smallerImageName.appendChild(this.xmlDoc.createTextNode(p.smallerImageName));
        smallerImageType.appendChild(this.xmlDoc.createTextNode(p.smallerImageType));
        smallerImageSize.appendChild(this.xmlDoc.createTextNode(p.smallerImageSize));
        smallerImageSrcData.appendChild(this.xmlDoc.createTextNode(p.smallerImageSrcData));
        
        largeImageAltTag.appendChild(this.xmlDoc.createTextNode(p.largeImageAltTag));
        
        supplierId.appendChild(this.xmlDoc.createTextNode(p.supplierId));
        sku.appendChild(this.xmlDoc.createTextNode(p.sku));
        price.appendChild(this.xmlDoc.createTextNode(p.price));
        priceRuleId.appendChild(this.xmlDoc.createTextNode(p.priceRuleId));
        vatRateId.appendChild(this.xmlDoc.createTextNode(p.vatRateId));
        storeageLocation.appendChild(this.xmlDoc.createTextNode(p.storeageLocation));
        stockLevel.appendChild(this.xmlDoc.createTextNode(p.stockLevel));
        statusId.appendChild(this.xmlDoc.createTextNode(p.statusId));
        
        metaTagTitle.appendChild(this.xmlDoc.createTextNode(p.page.metaTagTitle));
        metaTagKeywords.appendChild(this.xmlDoc.createTextNode(p.page.metaTagKeywords));
        metaTagDescription.appendChild(this.xmlDoc.createTextNode(p.page.metaTagDescription));
        productDescription.appendChild(this.xmlDoc.createTextNode(p.page.productDescription));
        

        // append all nodes to the product node
        product.appendChild(isMasterProduct);
        product.appendChild(productId);

        product.appendChild(productUnitGroupingConfigId);
        product.appendChild(attribute1Id);
        product.appendChild(attribute2Id);
        product.appendChild(attribute3Id);
        product.appendChild(attributeValue1Id);
        product.appendChild(attributeValue2Id);
        product.appendChild(attributeValue3Id);
        product.appendChild(name);
        product.appendChild(urlAlias);
        product.appendChild(categoryId);
        product.appendChild(description);
        
        image.appendChild(largeImageName);
        image.appendChild(largeImageType);
        image.appendChild(largeImageSize);
        image.appendChild(largeImageSrcData);
        
        image.appendChild(mediumImageName);
        image.appendChild(mediumImageType);
        image.appendChild(mediumImageSize);
        image.appendChild(mediumImageSrcData);        
        
        image.appendChild(smallImageName);
        image.appendChild(smallImageType);
        image.appendChild(smallImageSize);
        image.appendChild(smallImageSrcData);
        
        image.appendChild(smallerImageName);
        image.appendChild(smallerImageType);
        image.appendChild(smallerImageSize);
        image.appendChild(smallerImageSrcData);
        
        image.appendChild(largeImageAltTag);
        
        product.appendChild(image);
        product.appendChild(supplierId);
        product.appendChild(sku);
        product.appendChild(price);
        product.appendChild(priceRuleId);
        product.appendChild(vatRateId);
        product.appendChild(storeageLocation);
        product.appendChild(stockLevel);
        product.appendChild(statusId);
        
        page.appendChild(metaTagTitle);
        page.appendChild(metaTagKeywords);
        page.appendChild(metaTagDescription);
        page.appendChild(productDescription);
        product.appendChild(page);

        this.rootNode.appendChild(product);
    };  

    this.toString = function toString() {
        var xmlString = null;
        try {
            xmlString = (new XMLSerializer()).serializeToString(this.xmlDoc);
        } catch(e) {
            xmlString = this.xmlDoc.xml;
        }

        return xmlString;
    };
}


/* end class definitions */

var productModel = new ProductModel();

jQuery(document).ready(
    function() {
        initPage();
        initListeners();
        initFormValidation();
    });

function handleFileSelectLarge(evt) {
    
    var file = evt.target.files[0]; // File object

    // only process image file.
    if (file.type.match('image.*')) {
        
        var reader = new FileReader();
        
        // closure to capture the file information.
        reader.onload = (function(theFile) {
            return function(e) {
                
                if (evt.target.id == "large_image") {
                    sessionStorage.setItem("image_name", theFile.name);
                    sessionStorage.setItem("image_srcData", e.target.result);
                    sessionStorage.setItem("image_size", theFile.size);
                    sessionStorage.setItem("image_type", theFile.type);
                    
                    sessionStorage.setItem("product_unit_image_name", theFile.name);
                    sessionStorage.setItem("product_unit_image_srcData", e.target.result);
                    sessionStorage.setItem("product_unit_image_size", theFile.size);
                    sessionStorage.setItem("product_unit_image_type", theFile.type);
                    
                    setPreviewDivContents('#preview', e.target.result, theFile.size, theFile.type);
                    
                    var altString = theFile.name.substring(0, theFile.name.indexOf("."));
                    altString = altString.replace(/[-_]/g, " ");
                  
                    jQuery('#image_alt').val(altString);
                    
                    jQuery("#master_form").valid(); //will vlear any previous errors with validation                  
                    
                } else if (evt.target.id == "product_unit_large_image") {
                    sessionStorage.setItem("product_unit_image_name", theFile.name);
                    sessionStorage.setItem("product_unit_image_srcData", e.target.result);
                    sessionStorage.setItem("product_unit_image_size", theFile.size);
                    sessionStorage.setItem("product_unit_image_type", theFile.type);
                    
                    setPreviewDivContents('#product_unit_preview', e.target.result, theFile.size, theFile.type);
                    
                    var altString = theFile.name.substring(0, theFile.name.indexOf("."));
                    altString = altString.replace(/[-_]/g, " ");
                  
                    jQuery('#product_unit_image_alt').val(altString);
                }
                
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
                
                if (evt.target.id == "medium_image") {
                    sessionStorage.setItem("medium_image_name", theFile.name);
                    sessionStorage.setItem("medium_image_srcData", e.target.result);
                    sessionStorage.setItem("medium_image_size", theFile.size);
                    sessionStorage.setItem("medium_image_type", theFile.type);
                    
                    sessionStorage.setItem("product_unit_medium_image_name", theFile.name);
                    sessionStorage.setItem("product_unit_medium_image_srcData", e.target.result);
                    sessionStorage.setItem("product_unit_medium_image_size", theFile.size);
                    sessionStorage.setItem("product_unit_medium_image_type", theFile.type);
                    
                    setPreviewDivContents('#mediumPreview', e.target.result, theFile.size, theFile.type);
                    
                    jQuery("#master_form").valid(); //will vlear any previous errors with validation                  
                    
                } else if (evt.target.id == "product_unit_medium_image") {
                    sessionStorage.setItem("product_unit_medium_image_name", theFile.name);
                    sessionStorage.setItem("product_unit_medium_image_srcData", e.target.result);
                    sessionStorage.setItem("product_unit_medium_image_size", theFile.size);
                    sessionStorage.setItem("product_unit_medium_image_type", theFile.type);
                    
                    setPreviewDivContents('#product_unit_medium_preview', e.target.result, theFile.size, theFile.type);
                }

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
                
                if (evt.target.id == "small_image") {
                    sessionStorage.setItem("small_image_name", theFile.name);
                    sessionStorage.setItem("small_image_srcData", e.target.result);
                    sessionStorage.setItem("small_image_size", theFile.size);
                    sessionStorage.setItem("small_image_type", theFile.type);
                    
                    sessionStorage.setItem("product_unit_small_image_name", theFile.name);
                    sessionStorage.setItem("product_unit_small_image_srcData", e.target.result);
                    sessionStorage.setItem("product_unit_small_image_size", theFile.size);
                    sessionStorage.setItem("product_unit_small_image_type", theFile.type);
                    
                    setPreviewDivContents('#smallPreview', e.target.result, theFile.size, theFile.type);
                    
                    jQuery("#master_form").valid(); //will vlear any previous errors with validation                  
                    
                } else if (evt.target.id == "product_unit_small_image") {
                    sessionStorage.setItem("product_unit_small_image_name", theFile.name);
                    sessionStorage.setItem("product_unit_small_image_srcData", e.target.result);
                    sessionStorage.setItem("product_unit_small_image_size", theFile.size);
                    sessionStorage.setItem("product_unit_small_image_type", theFile.type);
                    
                    setPreviewDivContents('#product_unit_small_preview', e.target.result, theFile.size, theFile.type);
                }

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
                
                if (evt.target.id == "smaller_image") {
                    sessionStorage.setItem("smaller_image_name", theFile.name);
                    sessionStorage.setItem("smaller_image_srcData", e.target.result);
                    sessionStorage.setItem("smaller_image_size", theFile.size);
                    sessionStorage.setItem("smaller_image_type", theFile.type);
                    
                    sessionStorage.setItem("product_unit_smaller_image_name", theFile.name);
                    sessionStorage.setItem("product_unit_smaller_image_srcData", e.target.result);
                    sessionStorage.setItem("product_unit_smaller_image_size", theFile.size);
                    sessionStorage.setItem("product_unit_smaller_image_type", theFile.type);
                    
                    setPreviewDivContents('#smallerPreview', e.target.result, theFile.size, theFile.type);
                    
                    jQuery("#master_form").valid(); //will vlear any previous errors with validation                  
                    
                } else if (evt.target.id == "product_unit_smaller_image") {
                    sessionStorage.setItem("product_unit_smaller_image_name", theFile.name);
                    sessionStorage.setItem("product_unit_smaller_image_srcData", e.target.result);
                    sessionStorage.setItem("product_unit_smaller_image_size", theFile.size);
                    sessionStorage.setItem("product_unit_smaller_image_type", theFile.type);
                    
                    setPreviewDivContents('#product_unit_smaller_preview', e.target.result, theFile.size, theFile.type);
                }

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

/*
 * Sets defaults on the product unit form based on the master product details.
 *
 * Usage: This should be called before the product unit form is displayed.
 */
function setProductUnitDefaults() {
    
    // attribute values are controled by the sales configuration selection as this can be changed as you are editing the producy unit
    
    jQuery('#product_unit_id').val(-1);
    jQuery('#product_unit_temp_id').val(-1);
    
    jQuery('#product_unit_name').val(jQuery('#name').val());
    
    jQuery('#product_unit_urlAlias').val(jQuery('#urlAlias').val());
    
    var srcData = jQuery('#preview>img').attr("src");
    if (srcData != null) {
        jQuery('#product_unit_preview').html([ '<img class="thumb" src="', srcData, '"/>', '<div class="clear"></div>' ].join('')); 
    }
    var mediumSrcData = jQuery('#mediumPreview>img').attr("src");
    if (mediumSrcData  != null) {
        jQuery('#product_unit_medium_preview').html([ '<img class="thumb" src="', mediumSrcData, '"/>', '<div class="clear"></div>' ].join('')); 
    }
    var smallSrcData = jQuery('#smallPreview>img').attr("src");
    if (smallSrcData  != null) {
        jQuery('#product_unit_small_preview').html([ '<img class="thumb" src="', smallSrcData, '"/>', '<div class="clear"></div>' ].join('')); 
    }
    var smallerSrcData = jQuery('#smallerPreview>img').attr("src");
    if (smallerSrcData  != null) {
        jQuery('#product_unit_smaller_preview').html([ '<img class="thumb" src="', smallerSrcData, '"/>', '<div class="clear"></div>' ].join('')); 
    }
    jQuery('#product_unit_image_alt').val(jQuery('#image_alt').val());
    
    // don't reset supplier as it is likely the next product unit to be added is from the same supplier
    jQuery("#product_unit_sku").val("");
    // TODO supplier not empty then add in some sensible data to star with
    
    jQuery("#product_unit_price").val(jQuery('#price').val());
    
    jQuery("#product_unit_storeage_location").val("");
    jQuery("#product_unit_stock_level").val("0");
    jQuery("#product_unit_status_list").val("");
    
    jQuery('#product_unit_meta_tag_title').val(jQuery('#meta_tag_title').val());
    jQuery('#product_unit_meta_tag_keywords').val(jQuery('#meta_tag_keywords').val());
    jQuery('#product_unit_meta_tag_description').val(jQuery('#meta_tag_description').val());
    jQuery('#product_unit_product_description_wysiwyg').val(jQuery('#product_description_wysiwyg').val());
    
}


function addMasterProductToModel() {
    var productUnit = new Product();
    
    productUnit.productId = jQuery('#productId').val();
    
    productUnit.isMasterProduct = true;
        
    productUnit.productUnitGroupingConfigId = jQuery('#attribute_configuration').val();
        
    productUnit.attribute1Id = jQuery('#attribute_list_1').val();
    productUnit.attribute2Id = jQuery('#attribute_list_2').val();
    productUnit.attribute3Id = jQuery('#attribute_list_3').val();
    if (productUnit.attribute1Id == "") productUnit.attribute1Id = -1;
    if (productUnit.attribute2Id == "") productUnit.attribute2Id = -1;
    if (productUnit.attribute3Id == "") productUnit.attribute3Id = -1;
    
    productUnit.name = jQuery('#name').val();
    productUnit.urlAlias = jQuery('#urlAlias').val();
    productUnit.categoryId = jQuery('#category_list').val();
    productUnit.description = jQuery('#description').val();
       
    productUnit.largeImageName = sessionStorage.getItem("image_name");
    productUnit.largeImageSrcData = sessionStorage.getItem("image_srcData");
    productUnit.largeImageSize = sessionStorage.getItem("image_size");
    productUnit.largeImageType = sessionStorage.getItem("image_type");

    productUnit.mediumImageName = sessionStorage.getItem("medium_image_name");
    productUnit.mediumImageSrcData = sessionStorage.getItem("medium_image_srcData");
    productUnit.mediumImageSize = sessionStorage.getItem("medium_image_size");
    productUnit.mediumImageType = sessionStorage.getItem("medium_image_type");
    
    productUnit.smallImageName = sessionStorage.getItem("small_image_name");
    productUnit.smallImageSrcData = sessionStorage.getItem("small_image_srcData");
    productUnit.smallImageSize = sessionStorage.getItem("small_image_size");
    productUnit.smallImageType = sessionStorage.getItem("small_image_type");
    
    productUnit.smallerImageName = sessionStorage.getItem("smaller_image_name");
    productUnit.smallerImageSrcData = sessionStorage.getItem("smaller_image_srcData");
    productUnit.smallerImageSize = sessionStorage.getItem("smaller_image_size");
    productUnit.smallerImageType = sessionStorage.getItem("smaller_image_type");    
    
    productUnit.largeImageAltTag = jQuery('#image_alt').val();
    productUnit.price = jQuery('#price').val();
        
    var productUnitPage = new Page();
    productUnitPage.metaTagTitle = jQuery('#meta_tag_title').val();
    productUnitPage.metaTagKeywords = jQuery('#meta_tag_keywords').val();
    productUnitPage.metaTagDescription = jQuery('#meta_tag_description').val();
    productUnitPage.productDescription = jQuery('#product_description_wysiwyg').val();
    productUnit.page = productUnitPage;
    
    productModel.productUnits.push(productUnit);
}

function addUpdateProductUnit() {
    var pId = jQuery('#product_unit_id').val();
    var tpId = jQuery('#product_unit_temp_id').val();
    
    if (pId == "-1" && tpId == "-1") {
        addProductUnit();
    } else {
        updateProductUnit(pId, tpId);
    }
}

/*
 * Add a product unit that is not the master product. 
 */
function addProductUnit() {
    var productUnit = new Product();
    
    setProductUnitFields(productUnit);
    
    var dateTimeNow = new Date();    
    productUnit.newTempProductId = dateTimeNow.getTime();
    
    productModel.productUnits.push(productUnit);
    
    addTableRow(productUnit);
    
    enableDisableSalesConfiguration();
}

function updateProductUnit(productId, tempProductId) {
    // get the existing product 
    var productUnit;
    
    if (productId != -1) {
        productUnit = productModel.getProduct(productId);   
    } else {
        productUnit = productModel.getTempProduct(tempProductId);
    }
    
    // set the values
    setProductUnitFields(productUnit);
    
    // redraw the table
    
    jQuery('#datatable').dataTable().fnClearTable();
    for ( var i = 0; i < productModel.productUnits.length; i++) {
        addTableRow(productModel.productUnits[i]);  
    }
    
}

function setProductUnitFields(productUnit) {
    productUnit.productId = jQuery('#product_unit_id').val();
       
    productUnit.newTempProductId = jQuery('#product_unit_temp_id').val();
    
    productUnit.isMasterProduct = false;
    
    productUnit.productUnitGroupingConfigId = jQuery('#attribute_configuration').val();
    
    productUnit.attribute1Id = jQuery('#attribute_list_1').val();
    productUnit.attribute2Id = jQuery('#attribute_list_2').val();
    productUnit.attribute3Id = jQuery('#attribute_list_3').val();
    if (productUnit.attribute1Id == "") productUnit.attribute1Id = -1;
    if (productUnit.attribute2Id == "") productUnit.attribute2Id = -1;
    if (productUnit.attribute3Id == "") productUnit.attribute3Id = -1;
    
    productUnit.attributeValue1Id = jQuery('#product_unit_attribute_value_list_1').val();
    productUnit.attributeValue2Id = jQuery('#product_unit_attribute_value_list_2').val();
    productUnit.attributeValue3Id = jQuery('#product_unit_attribute_value_list_3').val();
    
    productUnit.name = jQuery('#product_unit_name').val();
    productUnit.urlAlias = jQuery('#product_unit_urlAlias').val();
    productUnit.categoryId = jQuery('#category_list').val();
    productUnit.description = jQuery('#description').val();
    
    // if unit values are set then take them else take the master details
    if (sessionStorage.getItem("product_unit_image_name")) {
        productUnit.largeImageName = sessionStorage.getItem("product_unit_image_name");
        productUnit.largeImageSrcData = sessionStorage.getItem("product_unit_image_srcData");
        productUnit.largeImageSize = sessionStorage.getItem("product_unit_image_size");
        productUnit.largeImageType = sessionStorage.getItem("product_unit_image_type");
    } else {
        productUnit.largeImageName = sessionStorage.getItem("image_name");
        productUnit.largeImageSrcData = sessionStorage.getItem("image_srcData");
        productUnit.largeImageSize = sessionStorage.getItem("image_size");
        productUnit.largeImageType = sessionStorage.getItem("image_type");
    }
    if (sessionStorage.getItem("product_unit_medium_image_name")) {
        productUnit.mediumImageName = sessionStorage.getItem("product_unit_medium_image_name");
        productUnit.mediumImageSrcData = sessionStorage.getItem("product_unit_medium_image_srcData");
        productUnit.mediumImageSize = sessionStorage.getItem("product_unit_medium_image_size");
        productUnit.mediumImageType = sessionStorage.getItem("product_unit_medium_image_type");
    } else {
        productUnit.mediumImageName = sessionStorage.getItem("medium_image_name");
        productUnit.mediumImageSrcData = sessionStorage.getItem("medium_image_srcData");
        productUnit.mediumImageSize = sessionStorage.getItem("medium_image_size");
        productUnit.mediumImageType = sessionStorage.getItem("medium_image_type");
    }
    
    if (sessionStorage.getItem("product_unit_small_image_name")) {
        productUnit.smallImageName = sessionStorage.getItem("product_unit_small_image_name");
        productUnit.smallImageSrcData = sessionStorage.getItem("product_unit_small_image_srcData");
        productUnit.smallImageSize = sessionStorage.getItem("product_unit_small_image_size");
        productUnit.smallImageType = sessionStorage.getItem("product_unit_small_image_type");
    } else {
        productUnit.smallImageName = sessionStorage.getItem("small_image_name");
        productUnit.smallImageSrcData = sessionStorage.getItem("small_image_srcData");
        productUnit.smallImageSize = sessionStorage.getItem("small_image_size");
        productUnit.smallImageType = sessionStorage.getItem("small_image_type");
    }
    
    if (sessionStorage.getItem("product_unit_smaller_image_name")) {
        productUnit.smallerImageName = sessionStorage.getItem("product_unit_smaller_image_name");
        productUnit.smallerImageSrcData = sessionStorage.getItem("product_unit_smaller_image_srcData");
        productUnit.smallerImageSize = sessionStorage.getItem("product_unit_smaller_image_size");
        productUnit.smallerImageType = sessionStorage.getItem("product_unit_smaller_image_type");
    } else {
        productUnit.smallerImageName = sessionStorage.getItem("smaller_image_name");
        productUnit.smallerImageSrcData = sessionStorage.getItem("smaller_image_srcData");
        productUnit.smallerImageSize = sessionStorage.getItem("smaller_image_size");
        productUnit.smallerImageType = sessionStorage.getItem("smaller_image_type");
    }
    
    productUnit.largeImageAltTag = jQuery('#product_unit_image_alt').val();
    
    productUnit.supplierId = jQuery('#product_unit_supplier_list').val();
    productUnit.sku = jQuery('#product_unit_sku').val();
    productUnit.price = jQuery('#product_unit_price').val();
    productUnit.priceRuleId = jQuery('#product_unit_price_rule_list').val();
    productUnit.vatRateId = jQuery('#product_unit_vat_rate_list').val();
    productUnit.storeageLocation = jQuery('#product_unit_storeage_location').val();
    productUnit.stockLevel = jQuery('#product_unit_stock_level').val();
    productUnit.statusId = jQuery('#product_unit_status_list').val();
    
    var productUnitPage = new Page();
    productUnitPage.metaTagTitle = jQuery('#product_unit_meta_tag_title').val();
    productUnitPage.metaTagKeywords = jQuery('#product_unit_meta_tag_keywords').val();
    productUnitPage.metaTagDescription = jQuery('#product_unit_meta_tag_description').val();
    productUnitPage.productDescription = jQuery('#product_unit_product_description_wysiwyg').val();
      
    productUnit.page = productUnitPage; 
}

function addTableRow(productUnit) {
    var row = new Array();
    row.push(productUnit.name);
    row.push(productUnit.sku);
    row.push(productUnit.price);
    row.push(productUnit.stockLevel);
    
    // create action column value
    var actionCol;
    if (productUnit.productId != -1) {
        actionCol = '<td><input type="image" src="images/icons/edit.png" alt="edit" onclick="showProductUnit(event, this.parentNode.parentNode, false, ' + productUnit.productId + ')" /> <input type="image" src="images/icons/cross.png" alt="delete" onclick="deleteProductUnit(this.parentNode.parentNode, false, ' + productUnit.productId + ')"/></td>';  
    } else {
        actionCol = '<td><input type="image" src="images/icons/edit.png" alt="edit" onclick="showProductUnit(event, this.parentNode.parentNode, true, ' + productUnit.newTempProductId + ')" /> <input type="image" src="images/icons/cross.png" alt="delete" onclick="deleteProductUnit(this.parentNode.parentNode, true, ' + productUnit.newTempProductId + ')"/></td>';
    }
    row.push(actionCol);

    jQuery('#datatable').dataTable().fnAddData(row);
}

function deleteProductUnit(tableRow, usingTempProductId, productId) {
    
    if (usingTempProductId == true) {
        productModel.removeTempProduct(productId);
    } else {
        productModel.removeProduct(productId);
    }
    
    jQuery('#datatable').dataTable().fnDeleteRow(tableRow);
    
    enableDisableSalesConfiguration();
    
    // if currently viewing product unit then we should close the view
    jQuery('#product_unit_details_panel').hide();
    jQuery('#create_unit_btn').fadeIn();
}

function enableDisableSalesConfiguration() {
    if (productModel.productUnits.length > 0) {
        jQuery('#attribute_configuration').attr("disabled", "disabled");
        
        jQuery('#attribute_list_1').attr("disabled", "disabled");
        jQuery('#attribute_list_2').attr("disabled", "disabled");
        jQuery('#attribute_list_3').attr("disabled", "disabled");
    } else {
        jQuery('#attribute_configuration').removeAttr("disabled");
        
        jQuery('#attribute_list_1').removeAttr("disabled");
        jQuery('#attribute_list_2').removeAttr("disabled");
        jQuery('#attribute_list_3').removeAttr("disabled");     
    }
}

function showProductUnit(event, tableRow, usingTempProductId, productId) {
    event.preventDefault(); 
    
    var product;
    
    if (usingTempProductId == true) {
        product = productModel.getTempProduct(productId);
    } else {
        product = productModel.getProduct(productId);   
    }
    
    
    // show the product unit
    jQuery('#product_unit_id').val(product.productId);
    
    jQuery('#product_unit_temp_id').val(product.newTempProductId);
    
    jQuery('#isMasterProduct').val(product.isMasterProduct);
    
    jQuery('#attribute_configuration').val(product.productUnitGroupingConfigId);
    
    jQuery('#product_unit_attribute_value_list_1').val(product.attributeValue1Id);
    jQuery('#product_unit_attribute_value_list_2').val(product.attributeValue2Id);
    jQuery('#product_unit_attribute_value_list_3').val(product.attributeValue3Id);
    
    jQuery('#product_unit_name').val(product.name);
    jQuery('#product_unit_urlAlias').val(product.urlAlias);
    
    sessionStorage.setItem("product_unit_image_name", product.largeImageName);
    sessionStorage.setItem("product_unit_image_srcData", product.largeImageSrcData);
    sessionStorage.setItem("product_unit_image_size", product.largeImageSize);
    sessionStorage.setItem("product_unit_image_type", product.largeImageType);

    sessionStorage.setItem("product_unit_medium_image_name", product.mediumImageName);
    sessionStorage.setItem("product_unit_medium_image_srcData", product.mediumImageSrcData);
    sessionStorage.setItem("product_unit_medium_image_size", product.mediumImageSize);
    sessionStorage.setItem("product_unit_medium_image_type", product.mediumImageType);
    
    sessionStorage.setItem("product_unit_small_image_name", product.smallImageName);
    sessionStorage.setItem("product_unit_small_image_srcData", product.smallImageSrcData);
    sessionStorage.setItem("product_unit_small_image_size", product.smallImageSize);
    sessionStorage.setItem("product_unit_small_image_type", product.smallImageType);
    
    sessionStorage.setItem("product_unit_smaller_image_name", product.smallerImageName);
    sessionStorage.setItem("product_unit_smaller_image_srcData", product.smallerImageSrcData);
    sessionStorage.setItem("product_unit_smaller_image_size", product.smallerImageSize);
    sessionStorage.setItem("product_unit_smaller_image_type", product.smallerImageType);
    
    setPreviewDivContents('#product_unit_preview', product.largeImageSrcData, product.largeImageSize, product.largeImageType);
    setPreviewDivContents('#product_unit_medium_preview', product.mediumImageSrcData, product.mediumImageSize, product.mediumImageType);
    setPreviewDivContents('#product_unit_small_preview', product.smallImageSrcData, product.smallImageSize, product.smallImageType);
    setPreviewDivContents('#product_unit_smaller_preview', product.smallerImageSrcData, product.smallerImageSize, product.smallerImageType);
    
    jQuery('#product_unit_image_alt').val(product.largeImageAltTag);
    
    jQuery('#product_unit_supplier_list').val(product.supplierId);
    jQuery('#product_unit_sku').val(product.sku);
    jQuery('#product_unit_price').val(product.price);
    jQuery('#product_unit_price_rule_list').val(product.priceRuleId);
    jQuery('#product_unit_vat_rate_list').val(product.vatRateId);
    jQuery('#product_unit_storeage_location').val(product.storeageLocation);
    jQuery('#product_unit_stock_level').val(product.stockLevel);
    jQuery('#product_unit_status_list').val(product.statusId);
    
    jQuery('#product_unit_meta_tag_title').val(product.page.metaTagTitle);
    jQuery('#product_unit_meta_tag_keywords').val(product.page.metaTagKeywords);
    jQuery('#product_unit_meta_tag_description').val(product.page.metaTagDescription);
    jQuery('#product_unit_product_description_wysiwyg').val(product.page.productDescription);
    
    jQuery('#product_unit_details_panel').slideDown();
    jQuery('#create_unit_btn').fadeOut(); 
    // if attributrs to be selected then it should really focus on that
    jQuery("#product_unit_name").focus();
    
    
    jQuery('html, body').animate({
        scrollTop: jQuery("#product_unit_details_panel").offset().top
    }, 1500);
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
    
    // can't add when there are no product units
    if (productModel.productUnits.length == 0) {
        return "You need to add some product units before you can save.";
    }

    // can't add if one of the images is too large
    for ( var i = 0; i < productModel.productUnits.length; i++) {
        var p = productModel.productUnits[i];
        if (p.largeImageSize >= 1000000) {
            return "One of the images you have used is too large. You will need to reduce the size.";
        }
    }
    
    // product units belonging to a group can't have the same attribute value
    var pairArray = new Array();
    
    for ( var i = 0; i < productModel.productUnits.length; i++) {
    	console.log('product unit ');
    	
        var p = productModel.productUnits[i];
        
        if (p.attribute1Id != -1) {
            var currPair = new Pair(p.attribute1Id, p.attributeValue1Id);
            for ( var j = 0; j < pairArray.length; j++) {
                var pair = pairArray[j];
                
                if (pair.equals(currPair)) {
                	console.log('attrib 1');
                	console.log('' + pair.attr + ' : ' + pair.attrVal);
                	
                    return "Duplicate attribute on the product units. Please ammend.";
                }
            }
            pairArray.push(currPair);
        }
        if (p.attribute2Id != -1) {
              var currPair = new Pair(p.attribute2Id,  p.attributeValue2Id);
              for ( var j = 0; j < pairArray.length; j++) {
                  var pair = pairArray[j];
                    
                  if (pair.equals(currPair)) {
                  	console.log('attrib 2');
                	console.log('' + pair.attr + ' : ' + pair.attrVal);
                      return "Duplicate attribute on the product units. Please ammend.";
                  }
              }
              pairArray.push(currPair);
        }
        if (p.attribute3Id != -1) {
            
              var currPair = new Pair(p.attribute3Id,  p.attributeValue3Id);
              for ( var j = 0; j < pairArray.length; j++) {
                  var pair = pairArray[j];
                    
                  if (pair.equals(currPair)) {
                  	console.log('attrib 3');
                	console.log('' + pair.attr + ' : ' + pair.attrVal);
                        return "Duplicate attribute on the product units. Please ammend.";
                  }
              }
              pairArray.push(currPair);
        }
    }
    
    return "";
}
    
/*
 * Set-up the state of the page
 */
function initPage() {
    
    enableDisableSalesConfiguration();
    
    /*
     * If page refreshed then get back some of the data that we need before it is saved to database.
     */
    if (jQuery("#large_image")[0].files.length > 0) {
        if (sessionStorage.getItem("image_srcData")) {
            setPreviewDivContents('#preview', sessionStorage.getItem("image_srcData"), sessionStorage.getItem("image_size"), sessionStorage.getItem("image_type"));
        }       
    }
    
    if (jQuery("#medium_image")[0].files.length > 0) {
        if (sessionStorage.getItem("medium_image_srcData")) {
            setPreviewDivContents('#mediumPreview', sessionStorage.getItem("medium_image_srcData"), sessionStorage.getItem("medium_image_size"), sessionStorage.getItem("medium_image_type"));
        }       
    }
    
    if (jQuery("#small_image")[0].files.length > 0) {
        if (sessionStorage.getItem("small_image_srcData")) {
            setPreviewDivContents('#smallPreview', sessionStorage.getItem("small_image_srcData"), sessionStorage.getItem("small_image_size"), sessionStorage.getItem("small_image_type"));
        }       
    }
    
    if (jQuery("#smaller_image")[0].files.length > 0) {
        if (sessionStorage.getItem("smaller_image_srcData")) {
            setPreviewDivContents('#smallerPreview', sessionStorage.getItem("smaller_image_srcData"), sessionStorage.getItem("smaller_image_size"), sessionStorage.getItem("smaller_image_type"));
        }       
    }
    
    jQuery('#datatable').dataTable({
        "bPaginate": false,
        "bLengthChange": false,
        "bFilter": true,
        "bSort": false,
        "bAutoWidth": false
    } );
    
    jQuery('#product_unit_details_panel').hide();
    
    if (jQuery('#attribute_configuration').val() == 1) {
        jQuery('#product_unit_attribute_value_list_1').val("");
        jQuery('#product_unit_attribute_value_list_2').val("");
        jQuery('#product_unit_attribute_value_list_3').val("");
        jQuery('#product_unit_optional_attributes_values').hide();
        
        jQuery('#attribute_list_1').val("");
        jQuery('#attribute_list_2').val("");
        jQuery('#attribute_list_3').val("");
        jQuery('#optional_attributes').hide();
    } else {
        jQuery('#optional_attributes').show();
    }
    
}   

/*
 * Set-up the listeners for the page
 */
function initListeners() {
     
    jQuery('button[id=save_button]').click(function(e) {
        e.preventDefault();
        
        // validate the form that is displayed
        if (jQuery("#master_form").valid() == true) {
            
            var errorMessage = "";
            errorMessage = validateProducts();
            if (errorMessage == "") {
                // pull together all the model elements
                addMasterProductToModel();
                // generate the xml
                var xmlDocumentManager = new XMLDocumentManager();
                xmlDocumentManager.createXmlDoc();
                for ( var i = 0; i < productModel.productUnits.length; i++) {
                    var product = productModel.productUnits[i];
                    xmlDocumentManager.createProductElement(product);
                }
                
                //jQuery(xmlDocumentManager.toString()).insertBefore(jQuery('#save_button'));
                // set the form element 
                jQuery('#productXml').val(xmlDocumentManager.toString());
                
                // submit the form
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
    
    jQuery('#attribute_list_1').change(function() {
        setAttributeValues('#attribute_list_1', '#product_unit_attribute_value_list_1');
    });
    
    jQuery('#attribute_list_2').change(function() {
        setAttributeValues('#attribute_list_2', '#product_unit_attribute_value_list_2');
    });
    
    jQuery('#attribute_list_3').change(function() {
        setAttributeValues('#attribute_list_3', '#product_unit_attribute_value_list_3');
    });
    
    jQuery('#attribute_configuration').change(function() {
        if (jQuery(this).val() == 2) {
            jQuery('#product_unit_optional_attributes_values').fadeIn();
            
            jQuery('#optional_attributes').fadeIn();
        } else {
            jQuery('#product_unit_attribute_value_list_1').val("");
            jQuery('#product_unit_attribute_value_list_2').val("");
            jQuery('#product_unit_attribute_value_list_3').val("");
            jQuery('#product_unit_optional_attributes_values').fadeOut();
            
            jQuery('#attribute_list_1').val("");
            jQuery('#attribute_list_2').val("");
            jQuery('#attribute_list_3').val("");
            jQuery('#optional_attributes').fadeOut();
        }
    });
    
    jQuery('#create_unit_btn').click(function(e) {
        e.preventDefault();
      
        // check product master details are valid before creating product units
        if (jQuery("#master_form").valid() == true) {
            setProductUnitDefaults();
            
            jQuery('#product_unit_details_panel').slideDown();
            jQuery(this).fadeOut(); 
            
            jQuery('html, body').animate({
                scrollTop: jQuery("#product_unit_details_panel").offset().top
            }, 1500);
            
                        
            setAttributeValues('#attribute_list_1', '#product_unit_attribute_value_list_1');
            setAttributeValues('#attribute_list_2', '#product_unit_attribute_value_list_2');
            setAttributeValues('#attribute_list_3', '#product_unit_attribute_value_list_3');
            
            // if attributrs to be selected then it should really focus on that
            jQuery("#product_unit_name").focus();
        }
    });
    
    jQuery('#add_unit_bnt').click(function(e) {
        e.preventDefault();
        
        if (jQuery("#product_unit_form").valid() == true) {
            addUpdateProductUnit();
            
            jQuery('#product_unit_details_panel').hide();
            jQuery('#create_unit_btn').fadeIn();            
        }
    });
    
    jQuery('#cancel_unit_btn').click(function(e) {
        e.preventDefault();
    
        jQuery('#product_unit_details_panel').hide();
        jQuery('#create_unit_btn').fadeIn();
        
        jQuery("#product_unit_form").validate().resetForm();
    });
    
    jQuery('#large_image').change(handleFileSelectLarge);
    jQuery('#medium_image').change(handleFileSelectMedium);
    jQuery('#small_image').change(handleFileSelectSmall);
    jQuery('#smaller_image').change(handleFileSelectSmaller);
    
    jQuery('#product_unit_large_image').change(handleFileSelectLarge);
    jQuery('#product_unit_medium_image').change(handleFileSelectMedium);
    jQuery('#product_unit_small_image').change(handleFileSelectSmall);
    jQuery('#product_unit_smaller_image').change(handleFileSelectSmaller);
    
    jQuery('#minus_one').click(function(e) {
        var level = jQuery('#product_unit_stock_level').val();
        if (level == "") {
            level = 0;
        } else {
            level = parseInt(level);
        }
        if (level >= 1) {
            level -= 1;
        }
        jQuery('#product_unit_stock_level').val(level);
    });
    
    jQuery('#plus_one').click(function(e) {
        var level = jQuery('#product_unit_stock_level').val();
        if (level == "") {
            level = 0;
        } else {
            level = parseInt(level);
        }
        level += 1;
        jQuery('#product_unit_stock_level').val(level);
    });
    
    jQuery('#minus_twelve').click(function(e) {
        var level = jQuery('#product_unit_stock_level').val();
        if (level == "") {
            level = 0;
        } else {
            level = parseInt(level);
        }
        if (level >= 12) {
            level -= 12;
        }
        jQuery('#product_unit_stock_level').val(level);
    });
    
    jQuery('#plus_twelve').click(function(e) {
        var level = jQuery('#product_unit_stock_level').val();
        if (level == "") {
            level = 0;
        } else {
            level = parseInt(level);
        }
        level += 12;
        jQuery('#product_unit_stock_level').val(level);
    });
}


function initFormValidation() {
    jQuery("#master_form").validate({
        
        rules: {
            name: {
                required: true,
                minlength: 3
            },
            
            category_list: {
                required: true
            },
            
            large_image: {
                required: true
            },
            
            medium_image: {
                required: true
            },
            
            small_image: {
                required: true
            },
            
            smaller_image: {
                required: true
            },
            
            image_alt: {
                required: true
            },
            
            price: {
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
    
    jQuery("#product_unit_form").validate({
        
        rules: {
            product_unit_name: {
                required: true,
                minlength: 3
            },
            
            product_unit_urlAlias: "required",
            
            product_unit_supplier_list: "required",
            
            product_unit_sku: "required",
            
            product_unit_price: "required",
            
            product_unit_price_rule_list: "required",
            
            product_unit_vat_rate_list: "required",
            
            product_unit_storeage_location: "required",
            
            product_unit_stock_level: "required",
            
            product_unit_status_list: "required"
            
        }
    });
}

function printObject(o) {
    var out = '';
    for (var p in o) {
        out += p + ': ' + o[p] + '\n';
    }
    alert(out);
}

    
    
</script>