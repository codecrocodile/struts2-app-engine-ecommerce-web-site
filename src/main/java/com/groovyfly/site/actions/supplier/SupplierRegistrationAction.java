/*
 * SupplierRegistrationAction.java
 * 
 * Copyright (c) 2013 Groovy Fly
 */
package com.groovyfly.site.actions.supplier;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.struts2.interceptor.validation.SkipValidation;

import com.groovyfly.admin.structures.Supplier;
import com.groovyfly.common.service.LookupServiceIF;
import com.groovyfly.common.structures.Address;
import com.groovyfly.common.structures.Lookup;
import com.groovyfly.common.structures.Person;
import com.groovyfly.site.actions.BaseSiteAction;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * @author Chris Hatton
 */
@SuppressWarnings("serial")
public class SupplierRegistrationAction extends BaseSiteAction implements Preparable {
    
    private static Logger log = Logger.getLogger(SupplierRegistrationAction.class.getName());

    private LookupServiceIF lookupServiceIF;
    
    private List<Lookup> countries;
    
    private SupplierRegistrationDetails supplierRegistrationDetails;
    
    private boolean navigateForward = true;
    
    private String destination = "fly-fishing-suppliers-form-step1";
    
    /**
     * Constructor
     */
    public SupplierRegistrationAction() {

    }
    
    /*
     * Set by Spring.
     */
    public void setLookupServiceIF(LookupServiceIF lookupServiceIF) {
        this.lookupServiceIF = lookupServiceIF;
    }

    /*
     * Called as part of the wizard process.
     */
    public void setSupplierRegistrationDetails(SupplierRegistrationDetails supplierRegistrationDetails) {
        this.supplierRegistrationDetails = supplierRegistrationDetails;
        
//        log.info("set details " + supplierRegistrationDetails.getEnglishSpeaking());
    }
    
    /*
     * Called as part of the wizard process.
     */
    public SupplierRegistrationDetails getSupplierRegistrationDetails() {
        
//        log.info("getting details = " + supplierRegistrationDetails);
        
        return this.supplierRegistrationDetails;
    }
    
    /* 
     * @see com.opensymphony.xwork2.Preparable#prepare()
     */
    @Override
    public void prepare() throws Exception {
        try {
            countries = lookupServiceIF.getCountries(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    @SkipValidation
    public String step1() throws Exception {
        
        log.info("doing step1");
        
        if (supplierRegistrationDetails == null) {
          // init object used as wizard model
          supplierRegistrationDetails = new SupplierRegistrationDetails();
          Supplier supplier = new Supplier();
          supplier.setAddress(new Address());
          supplier.setContactPerson(new Person());
          supplierRegistrationDetails.setSupplier(supplier);
        }
        
        try {
            countries = lookupServiceIF.getCountries(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        destination = "fly-fishing-suppliers-form-step1";
        
        return ActionSupport.SUCCESS;
    }

//    @Validations(
//      requiredFields = {
//              @RequiredFieldValidator(type = ValidatorType.FIELD, fieldName = "supplierRegistrationDetails.englishSpeaking", message = "You must enter a value for field.")
//      },
//      requiredStrings = {
//          @RequiredStringValidator(type = ValidatorType.FIELD, fieldName = "supplierRegistrationDetails.supplier.companyName", message = "You must enter a value for field."),
//          @RequiredStringValidator(type = ValidatorType.FIELD, fieldName = "supplierRegistrationDetails.supplier.address.country", message = "You must enter a value for field."),
//          @RequiredStringValidator(type = ValidatorType.FIELD, fieldName = "supplierRegistrationDetails.supplier.contactPerson.forename", message = "You must enter a value for field."),
//          @RequiredStringValidator(type = ValidatorType.FIELD, fieldName = "supplierRegistrationDetails.supplier.contactPerson.surname", message = "You must enter a value for field."),
//          @RequiredStringValidator(type = ValidatorType.FIELD, fieldName = "supplierRegistrationDetails.supplier.tel", message = "You must enter a value for field."),
//          @RequiredStringValidator(type = ValidatorType.FIELD, fieldName = "supplierRegistrationDetails.supplier.email", message = "You must enter a value for field.")
//          },
//      stringLengthFields = 
//          {@StringLengthFieldValidator(
//                  type = ValidatorType.FIELD, 
//                  trim = true, 
//                  minLength="2", 
//                  maxLength = "50", 
//                  fieldName = "supplierRegistrationDetails.supplier.companyName", 
//                  message = "Must be between 2 and 50 charaters.")}
//    )
    public String step2() throws Exception {
        try {
            log.info("step2");
            
        } catch (Exception e) {
            e.printStackTrace();
        }


        destination = "fly-fishing-suppliers-form-step2";
        
        
        return ActionSupport.SUCCESS;
    }
    

    public String step3() throws Exception {
        
        log.info("step3");
        
        if (navigateForward) {
            destination = "fly-fishing-suppliers-form-step3";    
        } else {
            try {
                countries = lookupServiceIF.getCountries(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            destination = "fly-fishing-suppliers-form-step1";    
        }
        
        
        return ActionSupport.SUCCESS;
    }
    
    public String step4() throws Exception {
        
        log.info("step4");
        
        
        if (navigateForward) {
            destination = "fly-fishing-suppliers-form-step4";    
        } else {
            destination = "fly-fishing-suppliers-form-step2";    
        }
        
        return ActionSupport.SUCCESS;
    }
    
    public String step5() throws Exception {
        
        log.info("step5");
        
        if (navigateForward) {
            destination = "fly-fishing-suppliers-form-step5";    
        } else {
            destination = "fly-fishing-suppliers-form-step3";    
        }
        
        
        return ActionSupport.SUCCESS;
    }
    
    public String step6() throws Exception {
        
        log.info("step6");
        
        if (navigateForward) {
            destination = "fly-fishing-suppliers-form-step6";    
        } else {
            destination = "fly-fishing-suppliers-form-step4";    
        }
        
        return ActionSupport.SUCCESS;
    }
    
//    @Validations(
//        requiredFields = {
//            @RequiredFieldValidator(type = ValidatorType.FIELD, fieldName = "supplierRegistrationDetails.ableToProvideReference", message = "You must enter a value for field."),
//            @RequiredFieldValidator(type = ValidatorType.FIELD, fieldName = "supplierRegistrationDetails.theirflyQualityRating", message = "You must enter a value for field.")
//        }
//    )
    public String step7() throws Exception {
        
        log.info("step7");
        
        if (navigateForward) {
            
            return ActionSupport.NONE; // this tells the view we want to now redirect to complete and finish the process
        } else {
            destination = "fly-fishing-suppliers-form-step5";    
        }
        
        
        return ActionSupport.SUCCESS;
    }
    
    public String complete() throws Exception {
        
        log.info("completing registration process........................");
        
        return ActionSupport.SUCCESS;
    }
    
    
    public boolean isNavigateForward() {
        return navigateForward;
    }
    

    public void setNavigateForward(boolean navigateForward) {
        
        this.navigateForward = navigateForward;
    }
    
    

    /**
     * This is the next page to navigate to after the action method has finished executing.
     * 
     * @return String - the next page to display.
     */
    public String getDestination() {
        return destination;
    }
    
    public void setDestination(String destination) {
        this.destination = destination;
    }
    

    /**
     * Gets a list of countries.
     * 
     * @return List<Lookup> - list of countries.
     */
    public List<Lookup> getCountries() {
        return countries;
    }

    /**
     * Produces a list of Integers to be used in the view, e.g. drop-down list values.
     */
    public List<Integer> numberList(int start, int number, int increment) {
        List<Integer> resultList = new ArrayList<Integer>();
        
        for (int i = start; i <= number; i += increment) {
            resultList.add(i);
        }
        
        return resultList;
    }
    
    public Map<String, String> getPaymentOptions() {
        Map<String, String> resultMap = new LinkedHashMap<String, String>();
        
        resultMap.put("1", "Escrow");
        resultMap.put("2", "Credit Card");
        resultMap.put("3", "Money Gram");
        resultMap.put("4", "Letter of Credit (LC)");
        resultMap.put("5", "Open Account");
        resultMap.put("6", "PayPal");
        resultMap.put("7", "PoaPay");
        resultMap.put("8", "Telegraphic Transfer (TT)");
        resultMap.put("9", "Western Union");
        resultMap.put("10", "Other");
        
        return resultMap;
    }
    
    public Map<String, String> getShippingMethods() {
        Map<String, String> resultMap = new LinkedHashMap<String, String>();
        
        resultMap.put("1", "Aramex FedEx");
        resultMap.put("2", "DHL");
        resultMap.put("3", "EMS (Express Mail Service)");
        resultMap.put("4", "FedEx");
        resultMap.put("5", "Mex Logistics (Africa) Ltd");
        resultMap.put("6", "MEX Logistics.L.L.C");
        resultMap.put("7", "One World Logistics");
        resultMap.put("8", "UPS");
        resultMap.put("9", "Other");
        
        return resultMap;
    }

}
