/*
 * LogoutAction.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.actions;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.groovyfly.admin.actions.BaseAdminAction;
import com.opensymphony.xwork2.Action;

/**
 * @author Chris Hatton
 * 
 * Created 23 Jun 2012
 */
public class LogoutAction extends BaseAdminAction implements SessionAware {

	private static final long serialVersionUID = 1803173804323723151L;
	
	private Map<String, Object> sessionMap;
	
	/* 
	 * @see org.apache.struts2.interceptor.SessionAware#setSession(java.util.Map)
	 */
	@Override
	public void setSession(Map<String, Object> session) {
		this.sessionMap = session;
	}
	
	/* 
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	@Override
	public String execute() throws Exception {
		this.sessionMap.clear();
		
		return Action.SUCCESS;
	}

}
