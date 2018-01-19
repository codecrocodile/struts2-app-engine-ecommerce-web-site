package com.struts2.gae.dispatcher;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.dispatcher.ng.HostConfig;
import org.apache.struts2.dispatcher.ng.InitOperations;

public class GaeInitOperations extends InitOperations {

	@Override
	public Dispatcher initDispatcher(HostConfig filterConfig) {
		Dispatcher dispatcher = createDispatcher(filterConfig);
		dispatcher.init();
		return dispatcher;
	}

	@SuppressWarnings("rawtypes")
	private Dispatcher createDispatcher(HostConfig filterConfig) {
		Map<String, String> params = new HashMap<String, String>();
		for (Iterator e = filterConfig.getInitParameterNames(); e.hasNext();) {
			String name = (String) e.next();
			String value = filterConfig.getInitParameter(name);
			params.put(name, value);
		}
		return new GaeDispatcher(filterConfig.getServletContext(), params);
	}
	
}