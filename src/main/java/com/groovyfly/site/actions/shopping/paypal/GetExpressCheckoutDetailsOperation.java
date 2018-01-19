/*
 * PaypalUtil.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.actions.shopping.paypal;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

import urn.ebay.api.PayPalAPI.GetExpressCheckoutDetailsReq;
import urn.ebay.api.PayPalAPI.GetExpressCheckoutDetailsRequestType;
import urn.ebay.api.PayPalAPI.GetExpressCheckoutDetailsResponseType;
import urn.ebay.api.PayPalAPI.PayPalAPIInterfaceServiceService;
import urn.ebay.apis.CoreComponentTypes.BasicAmountType;
import urn.ebay.apis.eBLBaseComponents.AddressType;
import urn.ebay.apis.eBLBaseComponents.CountryCodeType;
import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;
import urn.ebay.apis.eBLBaseComponents.GetExpressCheckoutDetailsResponseDetailsType;
import urn.ebay.apis.eBLBaseComponents.PayPalUserStatusCodeType;
import urn.ebay.apis.eBLBaseComponents.PayerInfoType;
import urn.ebay.apis.eBLBaseComponents.PaymentDetailsType;
import urn.ebay.apis.eBLBaseComponents.PersonNameType;

import com.groovyfly.common.structures.Customer;
import com.groovyfly.common.structures.ShippingAddress;

/**
 * @author Chris Hatton
 */
public class GetExpressCheckoutDetailsOperation extends ExpressCheckoutOperation {
    
    private static Logger log = Logger.getLogger(GetExpressCheckoutDetailsOperation.class.getName());
    
    private String groovyFlyInvoiceId;
    
    private Customer customer;
    
    private int noOfItemsInPayment;
    
    private BigDecimal itemTotal;
    
    private BigDecimal orderTotal;

    private String currencyCode;
    
    /**
     * Constructor
     */
    public GetExpressCheckoutDetailsOperation(String token, String contextRealPath) {
        super(token, contextRealPath);
    }
    
    public void doOperation() throws Exception {
        // get the basic objects used for the web service call
        PayPalAPIInterfaceServiceService service = new PayPalAPIInterfaceServiceService(configPath); // service is a wrapper for web service calls
        GetExpressCheckoutDetailsReq req = new GetExpressCheckoutDetailsReq();
        GetExpressCheckoutDetailsRequestType reqType = new GetExpressCheckoutDetailsRequestType(token);
        req.setGetExpressCheckoutDetailsRequest(reqType);
        
        GetExpressCheckoutDetailsResponseType resp = service.getExpressCheckoutDetails(req);
        if (resp != null) {
            if (resp.getAck().toString().equalsIgnoreCase("SUCCESS")) {
                GetExpressCheckoutDetailsResponseDetailsType responseDetails = resp.getGetExpressCheckoutDetailsResponseDetails();
                
                this.groovyFlyInvoiceId = responseDetails.getInvoiceID();
              
                
                PayerInfoType payerInfo = responseDetails.getPayerInfo();
                this.customer = this.createCustomer(payerInfo);
                this.customer.setPayPalPayerId(payerInfo.getPayerID());
                this.customer.setPhoneNumber(responseDetails.getContactPhone());
                
                List<PaymentDetailsType> paymentDetails = responseDetails.getPaymentDetails();
                if (paymentDetails.size() == 1) {
                    PaymentDetailsType paymentDetailsType = paymentDetails.get(0);
                    
                    AddressType shipToAddress = paymentDetailsType.getShipToAddress();
                    ShippingAddress customerAddress = this.createShippingAddress(shipToAddress);
                    this.customer.setShippingAddress(customerAddress);
                    
                    this.noOfItemsInPayment = paymentDetailsType.getPaymentDetailsItem().size();
                    BasicAmountType itemTotal = paymentDetailsType.getItemTotal();
                    this.itemTotal = new BigDecimal(itemTotal.getValue());
                    CurrencyCodeType currencyID = itemTotal.getCurrencyID();
                    this.currencyCode = currencyID.getValue();
                    BasicAmountType orderTotal = paymentDetailsType.getOrderTotal();
                    this.orderTotal = new BigDecimal(orderTotal.getValue());
                    
                } else {
                    // we have a problem. we only expect one payment details as we are not implementing parallel payments
                    log.severe("One payment details expected to be returned but returned " + paymentDetails.size());
                    this.errors = resp.getErrors();
                    throw new Exception("One payment details expected to be returned but returned " + paymentDetails.size());
                }
            } else {
                log.severe("No response has been returned for GetExpressCheckoutDetails web service call");
                this.errors = resp.getErrors();
                throw new Exception("Ack code is not success");
            }
        }
    }
    
    private Customer createCustomer(PayerInfoType payerInfo) {
        Customer customer = new Customer();
        customer.setPayPalPayerId(payerInfo.getPayerID());

        PersonNameType payerName = payerInfo.getPayerName(); 
        customer.setTitle(payerName.getSalutation());
        customer.setForename(payerName.getFirstName());
        customer.setSurname(payerName.getLastName());
  
        customer.setEmail(payerInfo.getPayer());
        PayPalUserStatusCodeType payerStatus = payerInfo.getPayerStatus();
        if (payerStatus != null) {
            customer.setEmailStatus(payerStatus.getValue());
        }
        
        AddressType addressType = payerInfo.getAddress();
        ShippingAddress shippingAddress = this.createShippingAddress(addressType);
        
        customer.setAddress(shippingAddress);
        
        return customer;
    }
    
    private ShippingAddress createShippingAddress(AddressType addressType) {
        ShippingAddress shipppingAddress = new ShippingAddress();
        
        shipppingAddress.setName(addressType.getName());
        shipppingAddress.setLine1(addressType.getStreet1());
        shipppingAddress.setLine2(addressType.getStreet2());
        shipppingAddress.setLine3(addressType.getCityName());
        shipppingAddress.setLine4(addressType.getStateOrProvince());
        shipppingAddress.setCountry(addressType.getCountryName());
        shipppingAddress.setPostcode(addressType.getPostalCode());
        
        CountryCodeType country = addressType.getCountry();
        if (country != null) {
            shipppingAddress.setCountryCode(country.getValue());
        }
        
        if (addressType.getAddressStatus() != null) {
            shipppingAddress.setAddressStatus(addressType.getAddressStatus().getValue());    
        }
        
        return shipppingAddress;
    }
    
    public String getGroovyFlyInvoiceId() {
        return groovyFlyInvoiceId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getNoOfItemsInPayment() {
        return noOfItemsInPayment;
    }

    public BigDecimal getItemTotal() {
        return itemTotal;
    }

    public BigDecimal getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(BigDecimal orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

}
