package com.struts2.gae.dispatcher;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.dispatcher.ng.ExecuteOperations;
import org.apache.struts2.dispatcher.ng.InitOperations;
import org.apache.struts2.dispatcher.ng.PrepareOperations;
import org.apache.struts2.dispatcher.ng.filter.FilterHostConfig;
import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

public class GaePrepareAndExecuteFilter extends StrutsPrepareAndExecuteFilter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		InitOperations init = new GaeInitOperations();
		try {
			FilterHostConfig config = new FilterHostConfig(filterConfig);
			init.initLogging(config);
			Dispatcher dispatcher = init.initDispatcher(config);

			init.initStaticContentLoader(config, dispatcher);

			prepare = new PrepareOperations(filterConfig.getServletContext(), dispatcher);
			execute = new ExecuteOperations(filterConfig.getServletContext(), dispatcher);
			this.excludedPatterns = init.buildExcludedPatternsList(dispatcher);

			postInit(dispatcher, filterConfig);
		} finally {
			init.cleanup();
		}
	}

}