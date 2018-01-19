/*
 * @(#)SetupStart.java			2 Feb 2014
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
package com.groovyfly.setup;


/**
 * @author Chris Hatton
 */
public class SetupStart {
	
	public static void main(String[] args) {
		
	    ImageUploader imageUploader = new ImageUploader();
	    try {
	        imageUploader.setupImages();
        } catch (Exception e) {
	        e.printStackTrace();
        }
    }

}
