/*
 * Supplier.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.structures;

import java.io.Serializable;

import com.groovyfly.common.structures.Address;
import com.groovyfly.common.structures.Person;

/**
 * @author Chris Hatton
 */
public class Supplier implements Serializable {
    
    private static final long serialVersionUID = 5948364704837800750L;

    private int supplierId;

    private String shortCode;

    private String companyName = "";

    private Person contactPerson;

    private Address address;

    private String tel;

    private String mobile;

    private String fax;

    private String email;

    private String notes;

    private boolean retired;

    /**
     * Constructor
     */
    public Supplier() {
        super();
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Person getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(Person contactPerson) {
        this.contactPerson = contactPerson;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isRetired() {
        return retired;
    }

    public void setRetired(boolean retired) {
        this.retired = retired;
    }
}
