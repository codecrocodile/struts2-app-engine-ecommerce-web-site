/*
 * @(#)HomePageActionTest.java			1 Feb 2014
 *
 * Copyright (c) 2012-2014 Groovy Fly.
 * 3 Aillort place, East Mains, East Kilbride, Scotland.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Groovy 
 * Fly. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Groovy Fly.
 */
package com.groovyfly.site.actions;

//import java.util.HashMap;
//import java.util.Map;

//import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.StrutsSpringTestCase;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
//import org.apache.struts2.interceptor.ClearSessionInterceptor;
import org.junit.Test;
//import org.mockito.Mock;
import org.mockito.Mockito;

//import com.groovyfly.common.structures.Page;
//import com.groovyfly.site.interceptor.BaseSiteInterceptor;
import com.groovyfly.site.service.CommonSiteServiceIF;
//import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionSupport;
//import com.opensymphony.xwork2.mock.MockActionInvocation;

/**
 * @author Chris Hatton
 */
public class HomePageActionTest extends StrutsSpringTestCase {
	
	/**
	 * Overridden to provide the location of this project's spring application context files.
	 */
	@Override
    protected String[] getContextLocations() {
    	/*
    	 * Note that the applicationContext-*.xml files have to be place in src/test/resources
    	 * for this to work. If any change is made to applicationContext-*.xml files then
    	 * src/test/resources needs to be updated. 
    	 */
        return new String[] {"classpath*:/WEB-INF/applicationContext-*.xml"};
    }
	
	
	@Test
    public void testGetActionMapping() throws Exception {
        ActionMapping mapping = getActionMapping("/homepage");
        
        assertNotNull(mapping);
        assertEquals("/", mapping.getNamespace());
        assertEquals("homepage", mapping.getName());
    }
	
	@Test
	public void testGetActionProxy() throws Exception {
        // set parameters or cookies etc.. before calling getActionProxy
	    //request.setParameter(name, value);
	    //request.setCookies(cookies);
        
		// test the proxy to make sure the correct action method is being called
		ActionProxy actionProxy = getActionProxy("/homepage");
		
		assertNotNull(actionProxy);
		assertEquals("execute", actionProxy.getMethod());
		
		// now lets test the action methods them self 
		HomePageAction action = (HomePageAction) actionProxy.getAction();
        assertNotNull(action);
        
        // mock the site service class because the execute method uses it
        String expectedHtml = "<html><ul><li>test list item</li></ul></html>";
        
        CommonSiteServiceIF mockSiteServiceIF = Mockito.mock(CommonSiteServiceIF.class);
        Mockito.when(mockSiteServiceIF.getCollectionListItemsHtml()).thenReturn(expectedHtml);
        action.setCommonSiteServiceIF(mockSiteServiceIF);
        
	    String actualResult = action.execute();
	    assertEquals(ActionSupport.SUCCESS, actualResult);
	    String countryListItemsHtml = action.getCountryListItemsHtml();
	    assertEquals(expectedHtml, countryListItemsHtml);
	}
	
	@Test
    public void testExecuteAction() throws Exception {
//		ActionProxy actionProxy = getActionProxy("/homepage");
//		HomePageAction action = (HomePageAction) actionProxy.getAction();
		
	
        
//		String expectedHtml1 = "<html><ul><li>test list item</li></ul></html>";
//		String expectedHtml2 = "<html><ul><li>test list item</li></ul></html>";
//		Page page = new Page();
//		page.setHtml("");
//		page.setMetaDescription("");
//		page.setMetaKeywords("");
//		page.setPageId(999);
//		page.setTitle("");
//		page.setUrlAlias("");
//		String expectedHtml3 = "<html><ul><li>test list item</li></ul></html>";
//        
//        CommonSiteServiceIF mockSiteServiceIF = Mockito.mock(CommonSiteServiceIF.class);
//        
//        Mockito.when(mockSiteServiceIF.getCollectionListItemsHtml()).thenReturn(expectedHtml1);
//        Mockito.when(mockSiteServiceIF.getFliesForMonthHtml()).thenReturn(expectedHtml2);
//        Mockito.when(mockSiteServiceIF.getPage(Mockito.anyString(), Mockito.anyString())).thenReturn(page);
//        Mockito.when(mockSiteServiceIF.getPopularFliesHtml()).thenReturn(expectedHtml3);
//        
//		
//        action.setCommonSiteServiceIF(mockSiteServiceIF);
//        
//        //how the hell do you do this
//		
//        BaseSiteInterceptor bean = applicationContext.getBean(BaseSiteInterceptor.class);
//        bean.setCommonSiteServiceIF(mockSiteServiceIF);
//		
////        applicationContext.
//        
//        
//        String output = executeAction("/homepage");
//        
//        System.out.println(output);
////        assertEquals("Hello", output);
    }
	

}
