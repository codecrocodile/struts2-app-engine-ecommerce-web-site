/*
 * ProductsSAXProcessor.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.service.productmanagement;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.codec.binary.Base64;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.groovyfly.common.structures.GroupingProduct;
import com.groovyfly.common.structures.Page;
import com.groovyfly.common.structures.Product;
import com.groovyfly.common.structures.ProductImages;

/**
 * @author Chris Hatton
 */
public class ProductsSAXProcessor extends DefaultHandler {
    
	public static final Logger log = Logger.getLogger(ProductsSAXProcessor.class.getName());
	
    private List<Product> products;

    private Product currentProduct;

    private ProductImages currentImage;

    private Page currentPage;

    private StringBuilder sb = new StringBuilder();

    public List<Product> parseProductsXml(String productsXml) throws Exception {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        SAXParser saxParser = spf.newSAXParser();

        XMLReader xmlReader = saxParser.getXMLReader();
        xmlReader.setContentHandler(this);

        StringReader sr = new StringReader(productsXml);
        InputSource is = new InputSource(sr);
        xmlReader.parse(is);

        return products;
    }

    /*
     * @see org.xml.sax.helpers.DefaultHandler#startDocument()
     */
    @Override
    public void startDocument() throws SAXException {
        products = new ArrayList<Product>();
    }

    /*
     * @see org.xml.sax.helpers.DefaultHandler#endDocument()
     */
    @Override
    public void endDocument() throws SAXException {
        // TODO validate product state
    }

    /*
     * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
     * java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        sb = new StringBuilder();

        if (localName.equalsIgnoreCase("product")) {
            currentProduct = new Product();

        } else if (localName.equalsIgnoreCase("image")) {
            this.currentImage = new ProductImages();

        } else if (localName.equalsIgnoreCase("page")) {
            this.currentPage = new Page();

        }
    }

    /*
     * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        
        if (localName.equalsIgnoreCase("ismasterproduct")) {
            if (sb.toString().equals("true")) {
                currentProduct = new GroupingProduct();
            }
        } 

        if (localName.equalsIgnoreCase("productid")) {
            Integer.valueOf(sb.toString());
            currentProduct.setProductId(Integer.valueOf(sb.toString()));

        } else if (localName.equalsIgnoreCase("productunitgroupingconfigid")) {
            currentProduct.setProductGroupingConfigId(Integer.valueOf(sb.toString()));

        } else if (localName.equalsIgnoreCase("attribute1id")) {
            this.currentProduct.setAttributeId1(Integer.valueOf(sb.toString()));

        } else if (localName.equalsIgnoreCase("attribute2id")) {
            this.currentProduct.setAttributeId2(Integer.valueOf(sb.toString()));

        } else if (localName.equalsIgnoreCase("attribute3id")) {
            this.currentProduct.setAttributeId3(Integer.valueOf(sb.toString()));

        } else if (localName.equalsIgnoreCase("attributevalue1id")) {
            this.currentProduct.setAttributeValueId1(Integer.valueOf(sb.toString().trim().equals("")? "0" : sb.toString()));

        } else if (localName.equalsIgnoreCase("attributevalue2id")) {
        	
        	log.info("psdogpdfogpd " + sb);
        	
        	if (sb == null || sb.toString().trim().equals("null")) {
        		this.currentProduct.setAttributeValueId2(0);
        	} else {
        		this.currentProduct.setAttributeValueId2(Integer.valueOf(sb.toString().trim().equals("")? "0" : sb.toString()));	
        	}
            

        } else if (localName.equalsIgnoreCase("attributevalue3id")) {
        	if (sb == null || sb.toString().trim().equals("null")) {
        		this.currentProduct.setAttributeValueId3(0);
        	} else {
        		this.currentProduct.setAttributeValueId3(Integer.valueOf(sb.toString().trim().equals("")? "0" : sb.toString()));	
        	}
        	
        	

        } else if (localName.equalsIgnoreCase("name")) {
            currentProduct.setName(sb.toString());

        } else if (localName.equalsIgnoreCase("urlAlias")) {
            currentProduct.setUrlAlias(sb.toString());
            
        } else if (localName.equalsIgnoreCase("categoryid")) {
            currentProduct.setCategoryId(Integer.valueOf(sb.toString()));

        } else if (localName.equalsIgnoreCase("description")) {
            currentProduct.setDescription(sb.toString());

        } 
        
        else if (localName.equalsIgnoreCase("largeimagename")) {
            this.currentImage.setLargeImageFileName(sb.toString());

        } else if (localName.equalsIgnoreCase("largeimagetype")) {
            this.currentImage.setLargeImageMimeType(sb.toString());

        } else if (localName.equalsIgnoreCase("largeimagesize")) {
            this.currentImage.setLargeImageByteSize(Integer.valueOf(sb.toString()));

        } else if (localName.equalsIgnoreCase("largeimagesrcdata")) {
            String base64String = sb.toString().substring(sb.toString().lastIndexOf(",") + 1, sb.toString().length());
            this.currentImage.setBase64LargeImage(base64String);
            this.currentImage.setLargeImageBytes(Base64.decodeBase64(base64String));
            
        } 
        
        else if (localName.equalsIgnoreCase("mediumimagename")) {
            this.currentImage.setMediumImageFileName(sb.toString());

        } else if (localName.equalsIgnoreCase("mediumimagetype")) {
            this.currentImage.setMediumImageMimeType(sb.toString());

        } else if (localName.equalsIgnoreCase("mediumimagesize")) {
            this.currentImage.setMediumImageByteSize(Integer.valueOf(sb.toString()));

        } else if (localName.equalsIgnoreCase("mediumimagesrcdata")) {
            String base64String = sb.toString().substring(sb.toString().lastIndexOf(",") + 1, sb.toString().length());
            this.currentImage.setBase64MediumImage(base64String);
            this.currentImage.setMediumImageBytes(Base64.decodeBase64(base64String));
        }
        
        else if (localName.equalsIgnoreCase("smallimagename")) {
            this.currentImage.setSmallImageFileName(sb.toString());

        } else if (localName.equalsIgnoreCase("smallimagetype")) {
            this.currentImage.setSmallImageMimeType(sb.toString());

        } else if (localName.equalsIgnoreCase("smallimagesize")) {
            this.currentImage.setSmallImageByteSize(Integer.valueOf(sb.toString()));

        } else if (localName.equalsIgnoreCase("smallimagesrcdata")) {
            String base64String = sb.toString().substring(sb.toString().lastIndexOf(",") + 1, sb.toString().length());
            this.currentImage.setBase64SmallImage(base64String);
            this.currentImage.setSmallImageBytes(Base64.decodeBase64(base64String));
        } 
        
        else if (localName.equalsIgnoreCase("smallerimagename")) {
            this.currentImage.setSmallerImageFileName(sb.toString());

        } else if (localName.equalsIgnoreCase("smallerimagetype")) {
            this.currentImage.setSmallerImageMimeType(sb.toString());

        } else if (localName.equalsIgnoreCase("smallerimagesize")) {
            this.currentImage.setSmallerImageByteSize(Integer.valueOf(sb.toString()));

        } else if (localName.equalsIgnoreCase("smallerimagesrcdata")) {
            String base64String = sb.toString().substring(sb.toString().lastIndexOf(",") + 1, sb.toString().length());
            this.currentImage.setBase64SmallerImage(base64String);
            this.currentImage.setSmallerImageBytes(Base64.decodeBase64(base64String));
        } 
        
        
        else if (localName.equalsIgnoreCase("largeimagealttag")) {
            this.currentImage.setAltTagDescription(sb.toString());

        } else if (localName.equalsIgnoreCase("supplierid")) {
            currentProduct.setSupplierId(Integer.valueOf(sb.toString()));

        } else if (localName.equalsIgnoreCase("sku")) {
            currentProduct.setSku(sb.toString());

        } else if (localName.equalsIgnoreCase("price")) {
            BigDecimal price = null;
            if (sb.toString().equals("")) {
                price = BigDecimal.valueOf(0.00);
            } else {
                price = BigDecimal.valueOf(Double.valueOf(sb.toString()));
            }
            
            currentProduct.setPrice(price);

        } else if (localName.equalsIgnoreCase("priceruleid")) {
            currentProduct.setPriceRuleId(Integer.valueOf(sb.toString()));

        } else if (localName.equalsIgnoreCase("vatrateid")) {
            currentProduct.setVatRuleId(Integer.valueOf(sb.toString()));

        } else if (localName.equalsIgnoreCase("storeagelocation")) {
            currentProduct.setStoreageLocation(sb.toString());

        } else if (localName.equalsIgnoreCase("stocklevel")) {
            currentProduct.setStockLevel(Integer.valueOf(sb.toString()));

        } else if (localName.equalsIgnoreCase("statusid")) {
            currentProduct.setStatusId(Integer.valueOf(sb.toString()));

        } else if (localName.equalsIgnoreCase("metatagtitle")) {
            this.currentPage.setTitle(sb.toString());

        } else if (localName.equalsIgnoreCase("metatagkeywords")) {
            this.currentPage.setMetaKeywords(sb.toString());

        } else if (localName.equalsIgnoreCase("metatagdescription")) {
            this.currentPage.setMetaDescription(sb.toString());

        } else if (localName.equalsIgnoreCase("productdescription")) {
            this.currentPage.setHtml(sb.toString());

        }
        
        if (localName.equalsIgnoreCase("product")) {
            this.products.add(this.currentProduct);
            
        } else if (localName.equalsIgnoreCase("page")) {
            this.currentProduct.setPage(this.currentPage);
            
        } else if (localName.equalsIgnoreCase("image")) {
            this.currentProduct.setImages(this.currentImage);
            
        }

    }

    /*
     * This may be called any number of times for an element so we must buffer
     * the chars given and use the buffer when the element has ended. I might
     * not happen all the time but it will happen.
     * 
     * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        sb.append(new String(ch, start, length));
    }

}
