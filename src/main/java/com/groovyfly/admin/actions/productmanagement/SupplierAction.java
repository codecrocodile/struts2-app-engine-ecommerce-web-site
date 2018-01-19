/*
 * SuppliersAction.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.actions.productmanagement;

import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import com.groovyfly.admin.actions.CRUDAction;
import com.groovyfly.admin.service.productmanagement.SupplierServiceIF;
import com.groovyfly.admin.structures.Supplier;
import com.groovyfly.common.interfaces.UserAware;
import com.groovyfly.common.service.LookupServiceIF;
import com.groovyfly.common.structures.Lookup;
import com.groovyfly.common.structures.User;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

/**
 * @author Chris Hatton
 * 
 *         Created 19 Jul 2012
 */
public class SupplierAction extends CRUDAction implements UserAware, ModelDriven<User>, Preparable {

    private static final long serialVersionUID = 8870387284243091041L;

    private User user;

    private SupplierServiceIF supplierServiceIF;

    private LookupServiceIF lookupServiceIF;

    private List<Supplier> suppliers;

    private List<Lookup> salutations;

    private List<Lookup> countries;

    private int id;

    private Supplier supplier;

    /*
     * @see
     * com.groovyfly.common.interfaces.UserAware#setUser(com.groovyfly.common
     * .structures.User)
     */
    @Override
    public void setUser(User user) {
        this.user = user;
    }

    public SupplierServiceIF getSupplierServiceIF() {
        return supplierServiceIF;
    }

    public void setSupplierServiceIF(SupplierServiceIF supplierServiceIF) {
        this.supplierServiceIF = supplierServiceIF;
    }

    public LookupServiceIF getLookupServiceIF() {
        return lookupServiceIF;
    }

    public void setLookupServiceIF(LookupServiceIF lookupServiceIF) {
        this.lookupServiceIF = lookupServiceIF;
    }

    /*
     * @see com.groovyfly.common.actions.AbstractCRUDAction#list()
     */
    @Override
    @SkipValidation
    public String list() throws Exception {
        setCrudOperation(CRUDAction.LIST);

        suppliers = supplierServiceIF.getSuppliers(false);

        return CRUDAction.LIST;
    }

    /*
     * @see com.groovyfly.common.actions.AbstractCRUDAction#create()
     */
    @Override
    @SkipValidation
    public String create() throws Exception {
        setCrudOperation(CRUDAction.CREATE);

        supplier = new Supplier();
        salutations = this.lookupServiceIF.getSalutations(false);
        countries = this.lookupServiceIF.getCountries(false);

        return Action.SUCCESS;
    }

    /*
     * @see com.groovyfly.common.actions.AbstractCRUDAction#read()
     */
    @Override
    @SkipValidation
    public String read() throws Exception {
        setCrudOperation(CRUDAction.READ);
        setReadOnly(true);

        supplier = supplierServiceIF.getSupplier(id);
        salutations = this.lookupServiceIF.getSalutations(false);
        countries = this.lookupServiceIF.getCountries(false);

        return Action.SUCCESS;
    }

    /*
     * @see com.groovyfly.common.actions.AbstractCRUDAction#update()
     */
    @Override
    @SkipValidation
    public String update() throws Exception {
        setCrudOperation(CRUDAction.UPDATE);

        supplier = supplierServiceIF.getSupplier(id);
        salutations = this.lookupServiceIF.getSalutations(false);
        countries = this.lookupServiceIF.getCountries(false);

        return Action.SUCCESS;
    }

    /*
     * @see com.groovyfly.common.actions.AbstractCRUDAction#delete()
     */
    @Override
    @SkipValidation
    public String delete() throws Exception {
        setCrudOperation(CRUDAction.DELETE);

        supplierServiceIF.retireSupplier(id);

        return this.list();
    }

    /*
     * @see com.groovyfly.common.actions.CRUDAction#save()
     */
    @Override
    public String save() throws Exception {
        setCrudOperation(CRUDAction.SAVE);

        supplierServiceIF.saveSupplier(supplier);

        return this.list();
    }

    /**
     * This will be called by the prepare interceptor. It uses a naming
     * convention of prepare[ActionMethod] to determine which prepare method to
     * called based on the action method that will be called.
     * 
     * We do this because before the save method is called the fields will be
     * validated and if not valid we need to have the combo box values retrieved
     * from the database and set if we are to return to the input page.
     * 
     * @throws Exception
     */
    public void prepareSave() throws Exception {
        salutations = this.lookupServiceIF.getSalutations(false);
        countries = this.lookupServiceIF.getCountries(false);
    }

    /*
     * @see com.opensymphony.xwork2.Preparable#prepare()
     */
    public void prepare() throws Exception {
    }

    /*
     * @see com.groovyfly.common.actions.AbstractCRUDAction#getDestination()
     */
    @Override
    public String getDestination() {
        return "supplier";
    }

    /*
     * @see com.opensymphony.xwork2.ModelDriven#getModel()
     */
    @Override
    public User getModel() {
        return user;
    }

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public List<Lookup> getSalutations() {
        return salutations;
    }

    public List<Lookup> getCountries() {
        return countries;
    }

    public void setCountries(List<Lookup> countries) {
        this.countries = countries;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
