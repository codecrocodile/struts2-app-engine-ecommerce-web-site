/*
 * ProductAction.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.actions.productmanagement;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import com.groovyfly.admin.actions.CRUDAction;
import com.groovyfly.admin.service.configuration.DuplicateUrlAliasException;
import com.groovyfly.admin.service.configuration.InvalidUrlAliasException;
import com.groovyfly.admin.service.configuration.UrlManagerServiceIF;
import com.groovyfly.admin.service.finance.FinanceServiceIF;
import com.groovyfly.admin.service.productmanagement.CategoryServiceIF;
import com.groovyfly.admin.service.productmanagement.ProductServiceIF;
import com.groovyfly.admin.service.productmanagement.ProductsSAXProcessor;
import com.groovyfly.admin.service.productmanagement.SupplierServiceIF;
import com.groovyfly.admin.structures.Supplier;
import com.groovyfly.admin.structures.finance.PriceRule;
import com.groovyfly.common.interfaces.UserAware;
import com.groovyfly.common.structures.Category;
import com.groovyfly.common.structures.GroupingProduct;
import com.groovyfly.common.structures.Lookup;
import com.groovyfly.common.structures.Product;
import com.groovyfly.common.structures.ProductAttribute;
import com.groovyfly.common.structures.ProductAttributeValue;
import com.groovyfly.common.structures.User;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

/**
 * @author Chris Hatton
 * 
 *         Created 28 Jul 2012
 */
public class ProductAction extends CRUDAction implements UserAware, ModelDriven<User>, Preparable {

    private static final long serialVersionUID = -80845391760141365L;

    private static Logger log = Logger.getLogger(ProductAction.class.getName());

    private User user;
    
    private ProductServiceIF productServiceIF;
    
    private CategoryServiceIF categoryServiceIF;
    
    private SupplierServiceIF supplierServiceIF;
    
    private FinanceServiceIF financeServiceIF;
    
    private UrlManagerServiceIF urlManagerServiceIF;
    
    private List<Product> products;
    
    private GroupingProduct parent;
    
    private List<Category> categories;
    
    private List<Supplier> suppliers;
    
    private List<ProductAttribute> productAttributes;
    
    private List<Lookup> productStatuses;
    
    private List<Lookup> vatRates;
    
    private List<PriceRule> priceRules;
    
    private Product product;
    
    private int productId;
    
    private int supplierCount;
    
    private int noOfPages;
    
    private String largePhoto;
    
    private String largePhotoContentType;
    
    private String largePhotoFileName;
    
    private String mediumPhoto;
    
    private String mediumPhotoContentType;
    
    private String mediumPhotoFileName;
    
    private String smallPhoto;
    
    private String smallPhotoContentType;
    
    private String smallPhotoFileName;
    
    private String smallerPhoto;
    
    private String smallerPhotoContentType;
    
    private String smallerPhotoFileName;
    
    private String productXml;
    
    private String destination = "product";
    
    public void setProductServiceIF(ProductServiceIF productServiceIF) {
        this.productServiceIF = productServiceIF;
    }
    
    public void setCategoryServiceIF(CategoryServiceIF categoryServiceIF) {
        this.categoryServiceIF = categoryServiceIF;
    }

    public void setSupplierServiceIF(SupplierServiceIF supplierServiceIF) {
        this.supplierServiceIF = supplierServiceIF;
    }
    
    public void setFinanceServiceIF(FinanceServiceIF financeServiceIF) {
        this.financeServiceIF = financeServiceIF;
    }
   
    public void setUrlManagerServiceIF(UrlManagerServiceIF urlManagerServiceIF) {
        this.urlManagerServiceIF = urlManagerServiceIF;
    }

    /*
     * @see com.opensymphony.xwork2.Preparable#prepare()
     */
    @Override
    public void prepare() throws Exception {
        try {
            categories = categoryServiceIF.getCategories(false);
            suppliers = supplierServiceIF.getSuppliers(false);
            productAttributes = productServiceIF.getProductAttributes();
            
            
            log.info("Product Attributes Size = " + productAttributes.size());
            
            productStatuses = productServiceIF.getProductStatuses(false);
            vatRates = financeServiceIF.getVatRates(false);
            priceRules = financeServiceIF.getPriceRules(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * @see com.opensymphony.xwork2.ModelDriven#getModel()
     */
    @Override
    public User getModel() {
        return user;
    }
    
    /*
     * @see com.groovyfly.common.interfaces.UserAware#setUser(com.groovyfly.common.structures.User)
     */
    @Override
    public void setUser(User user) {
        this.user = user;
    }

    /*
     * @see com.groovyfly.common.actions.CRUDAction#list()
     */
    @Override
    public String list() throws Exception {
        try {
            products = productServiceIF.getProductSummaries(false);    
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return CRUDAction.LIST;
    }

    /*
     * @see com.groovyfly.common.actions.CRUDAction#create()
     */
    @Override
    public String create() throws Exception {
        try {
            setCrudOperation(CRUDAction.CREATE);
            setReadOnly(false);
            product = new Product();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Action.SUCCESS;
    }
    
    /*
     * @see com.groovyfly.common.actions.CRUDAction#create()
     */
    public String createAndGroup() throws Exception {
        try {
        	
        	for (ProductAttribute pa : productAttributes) {
        		log.info(pa.getId() + " : " + pa.getDescription());
        	}
        	
            setCrudOperation(CRUDAction.CREATE);
            setReadOnly(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "quick-grouping";
    }

    /*
     * @see com.groovyfly.common.actions.CRUDAction#read()
     */
    @Override
    public String read() throws Exception {
        try {
            setCrudOperation(CRUDAction.READ);
            setReadOnly(true);
            product = productServiceIF.getProduct(productId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Action.SUCCESS;
    }

    /*
     * @see com.groovyfly.common.actions.CRUDAction#update()
     */
    @Override
    public String update() throws Exception {
        try {
            setCrudOperation(CRUDAction.UPDATE);
            setReadOnly(false);
            product = productServiceIF.getProduct(productId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Action.SUCCESS;
    }

    /*
     * @see com.groovyfly.common.actions.CRUDAction#delete()
     */
    @Override
    public String delete() throws Exception {
        try {
            setCrudOperation(CRUDAction.DELETE);
            setReadOnly(false);
            productServiceIF.retireProduct(productId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return this.list();
    }
    
    /*
     * @see com.groovyfly.common.actions.CRUDAction#save()
     */
    @Override
    public String save() throws Exception {
        log.info("save");
        
        try {
            if (largePhoto != null && largePhoto.length() > 0) {
                InputStream inputStream = IOUtils.toInputStream(largePhoto, "ISO-8859-1");
                product.getImages().setLargeImageBytes(IOUtils.toByteArray(inputStream));
                product.getImages().setLargeImageMimeType(largePhotoContentType);
                product.getImages().setLargeImageFileName(largePhotoFileName);
            }
            
            if (mediumPhoto != null && mediumPhoto.length() > 0) {
                InputStream inputStream = IOUtils.toInputStream(mediumPhoto, "ISO-8859-1");
                product.getImages().setMediumImageBytes(IOUtils.toByteArray(inputStream));
                product.getImages().setMediumImageMimeType(mediumPhotoContentType);
                product.getImages().setMediumImageFileName(mediumPhotoFileName);
            }
            
            if (smallPhoto != null && smallPhoto.length() > 0) {
                InputStream inputStream = IOUtils.toInputStream(smallPhoto, "ISO-8859-1");
                product.getImages().setSmallImageBytes(IOUtils.toByteArray(inputStream));
                product.getImages().setSmallImageMimeType(smallPhotoContentType);
                product.getImages().setSmallImageFileName(smallPhotoFileName);
            }
            
            if (smallerPhoto != null && smallerPhoto.length() > 0) {
                InputStream inputStream = IOUtils.toInputStream(smallerPhoto, "ISO-8859-1");
                product.getImages().setSmallerImageBytes(IOUtils.toByteArray(inputStream));
                product.getImages().setSmallerImageMimeType(smallerPhotoContentType);
                product.getImages().setSmallerImageFileName(smallerPhotoFileName);
            }
            
            productServiceIF.saveProduct(product);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return this.list();
    }
    
    public void validateSave() throws Exception {
        log.info("validateSave");
        
        try {
            String urlAlias = null;
            if (product.getUrlAlias() == null || product.getUrlAlias().trim().equals("")) {
                try {
                    if (product.getPage() != null && product.getPage().getPageId() > 0) {
                        urlAlias = urlManagerServiceIF.getUrlAliasForExistingProduct(product.getPage().getPageId(), product.getCategoryId(), product.getName());
                    } else {
                        urlAlias = urlManagerServiceIF.getUrlAliasForNewProduct(product.getCategoryId(), product.getName());                    
                    }
                    product.setUrlAlias(urlAlias);
                    product.getPage().setUrlAlias(urlAlias);
                } catch (InvalidUrlAliasException e) {
                    this.addFieldError("product.name", "Product name does not translate to valid URL alias. Change name or specify a URL alias.");
                } catch (DuplicateUrlAliasException e) {
                    this.addFieldError("product.name", "Product name URL alias translation in use. Change name or specify a URL alias.");
                }
            } else {
                try {
                    if (product.getPage() != null && product.getPage().getPageId() > 0) {
                        urlAlias = urlManagerServiceIF.getUrlAliasForExistingProduct(product.getPage().getPageId(), product.getCategoryId(), product.getUrlAlias());
                    } else {
                        urlAlias = urlManagerServiceIF.getUrlAliasForNewProduct(product.getCategoryId(), product.getUrlAlias());                    
                    }
                    product.setUrlAlias(urlAlias);
                    product.getPage().setUrlAlias(urlAlias);
                } catch (InvalidUrlAliasException e) {
                    this.addFieldError("product.urlAlias", "Invalid URL alias");
                } catch (DuplicateUrlAliasException e) {
                    this.addFieldError("product.urlAlias", "URL alias is already in use. Please specify another one.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

        
        // TODO validate all the other fields for the single product
    }

    public String saveProductsAndGrouping() throws Exception {
        log.info("saveProductsAndGrouping()");
        try {
            this.destination = "product";
            productServiceIF.saveProduct(parent);    
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        
        return this.list();
    }
    
    public void validateSaveProductsAndGrouping() throws Exception {
        log.info("validateSaveProductsAndGrouping() ");
        
        this.destination = "product-quick-grouping";
        try {
            
            ProductsSAXProcessor p = new ProductsSAXProcessor();
            
            products = p.parseProductsXml(this.productXml);  
            
            
            
            parent = null;
            List<Product> children = new ArrayList<Product>();
            for (Product prod : products) {
                if (prod instanceof GroupingProduct) {
                    parent = (GroupingProduct) prod;
                } else {
                    children.add(prod);
                }
                
                String urlAlias = null;
                if (prod.getUrlAlias() == null || prod.getUrlAlias().trim().equals("")) {
                    try {
                        if (prod.getPage() != null && prod.getPage().getPageId() > 0) {
                            urlAlias = urlManagerServiceIF.getUrlAliasForExistingProduct(prod.getPage().getPageId(), prod.getCategoryId(), prod.getName());
                        } else {
                            urlAlias = urlManagerServiceIF.getUrlAliasForNewProduct(prod.getCategoryId(), prod.getName());                    
                        }
                        prod.setUrlAlias(urlAlias);
                        prod.getPage().setUrlAlias(urlAlias);
                    } catch (InvalidUrlAliasException e) {
                        this.addFieldError("name", "Product name (" + prod.getName() + ") does not translate to valid URL alias. Change name or specify a URL alias.");
                    } catch (DuplicateUrlAliasException e) {
                        this.addFieldError("name", "Product name URL alias (" + prod.getName() + ") translation in use. Change name or specify a URL alias.");
                    }
                } else {
                    try {
                        if (prod.getPage() != null && prod.getPage().getPageId() > 0) {
                            urlAlias = urlManagerServiceIF.getUrlAliasForExistingProduct(prod.getPage().getPageId(), prod.getCategoryId(), prod.getUrlAlias());
                        } else {
                            urlAlias = urlManagerServiceIF.getUrlAliasForNewProduct(prod.getCategoryId(), prod.getUrlAlias());                    
                        }
                        prod.setUrlAlias(urlAlias);
                        prod.getPage().setUrlAlias(urlAlias);
                    } catch (InvalidUrlAliasException e) {
                        this.addFieldError("urlAlias", "Invalid URL alias (" + prod.getUrlAlias() + ") ");
                    } catch (DuplicateUrlAliasException e) {
                        this.addFieldError("urlAlias", "URL alias (" + prod.getUrlAlias() + ") is already in use. Please specify another one.");
                    }
                }
                 
                prod.getPage().setUrlAlias(urlAlias); 
            }
            
            for (Product prod : products) {
                for (Product prodTemp: products) {
                    if (!prod.equals(prodTemp) && prod.getUrlAlias().trim().equals(prodTemp.getUrlAlias().trim())) {
                        this.addFieldError("urlAlias", "Url alias for products needs to be unique. Please check the names of the products or specified url alias is unique.");
                    }
                }
            }
            
            parent.setProducts(children);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        

    }

    /*
     * @see com.groovyfly.common.actions.CRUDAction#getDestination()
     */
    @Override
    public String getDestination() {
        return destination;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public Product getProduct() {
        return product;
    }
    
    /**
     * It would seem that if the product is null and struts is trying to set a value on the 
     * product then a product instance will be created if we have a set method, otherwise 
     * the saving fails.
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    public int getSupplierCount() {
        return supplierCount;
    }

    public int getNoOfPages() {
        return noOfPages;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    public List<ProductAttribute> getProductAttributes() {
        return productAttributes;
    }

    public void setProductAttributes(List<ProductAttribute> productAttributes) {
        this.productAttributes = productAttributes;
    }

    public List<Lookup> getProductStatuses() {
        return productStatuses;
    }

    public List<Lookup> getVatRates() {
        return vatRates;
    }

    public List<PriceRule> getPriceRules() {
        return priceRules;
    }
   
    public String getJsAttrubuteArrayLiteral() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        
        for (ProductAttribute pa : productAttributes) {
            sb.append("'" + pa.getId() +"' : ");
            sb.append("{");
            for (ProductAttributeValue pav : pa.getProductAttributeValues()) {
                sb.append("'" + pav.getId() + "' : '" + pav.getDescription() + "', ");
            }
            
            sb.append("}, ");
        }
        
        sb.append("}");
        return sb.toString();
    }

    public void setLargePhoto(String largePhoto) {
        this.largePhoto = largePhoto;
    }

    public void setLargePhotoContentType(String largePhotoContentType) {
        this.largePhotoContentType = largePhotoContentType;
    }

    public void setLargePhotoFileName(String largePhotoFileName) {
        this.largePhotoFileName = largePhotoFileName;
    }
    
    public void setMediumPhoto(String mediumPhoto) {
        this.mediumPhoto = mediumPhoto;
    }

    public void setMediumPhotoContentType(String mediumPhotoContentType) {
        this.mediumPhotoContentType = mediumPhotoContentType;
    }

    public void setMediumPhotoFileName(String mediumPhotoFileName) {
        this.mediumPhotoFileName = mediumPhotoFileName;
    }

    public void setSmallPhoto(String smallPhoto) {
        this.smallPhoto = smallPhoto;
    }

    public void setSmallPhotoContentType(String smallPhotoContentType) {
        this.smallPhotoContentType = smallPhotoContentType;
    }

    public void setSmallPhotoFileName(String smallPhotoFileName) {
        this.smallPhotoFileName = smallPhotoFileName;
    }

    public void setSmallerPhoto(String smallerPhoto) {
        this.smallerPhoto = smallerPhoto;
    }

    public void setSmallerPhotoContentType(String smallerPhotoContentType) {
        this.smallerPhotoContentType = smallerPhotoContentType;
    }

    public void setSmallerPhotoFileName(String smallerPhotoFileName) {
        this.smallerPhotoFileName = smallerPhotoFileName;
    }

    public String getProductXml() {
        return productXml;
    }

    public void setProductXml(String productXml) {
        this.productXml = productXml;
    }
}
