/*
 * SupplierServiceIF.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.service.productmanagement;

import java.util.List;

import com.groovyfly.admin.structures.Supplier;

/**
 * @author Chris Hatton
 */
public interface SupplierServiceIF {

    public List<Supplier> getSuppliers(boolean includeRetired) throws Exception;

    public Supplier getSupplier(int id) throws Exception;

    public void saveSuppliers(List<Supplier> suppliers) throws Exception;
    
    public void saveSupplier(Supplier supplier) throws Exception;

    public void insertSupplier(Supplier supplier) throws Exception;

    public void updateSupplier(Supplier supplier) throws Exception;

    public void retireSupplier(int id) throws Exception;

}
