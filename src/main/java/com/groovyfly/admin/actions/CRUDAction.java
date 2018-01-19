/*
 * CRUDAction.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.actions;


/**
 * @author Chris Hatton
 */
public abstract class CRUDAction extends BaseAdminAction {

    private static final long serialVersionUID = 7640757054594750385L;

    public static final String LIST = "list";

    public static final String CREATE = "create";

    public static final String READ = "read";

    public static final String UPDATE = "update";

    public static final String DELETE = "delete";

    public static final String SAVE = "save";

    private String crudOperation;

    private boolean readOnly;

    public abstract String list() throws Exception;

    public abstract String create() throws Exception;

    public abstract String read() throws Exception;

    public abstract String update() throws Exception;

    public abstract String delete() throws Exception;

    public abstract String save() throws Exception;

    public abstract String getDestination();

    public String getCrudOperation() {
        return crudOperation;
    }

    public void setCrudOperation(String crudOperation) {
        this.crudOperation = crudOperation;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isReadOnly() {
        return readOnly;
    }
}
