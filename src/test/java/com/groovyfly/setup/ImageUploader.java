/*
 * @(#)ImageUploader.java			2 Feb 2014
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.capabilities.CapabilitiesService;
import com.google.appengine.api.capabilities.CapabilitiesServiceFactory;
import com.google.appengine.api.capabilities.Capability;
import com.google.appengine.api.capabilities.CapabilityState;
import com.google.appengine.api.capabilities.CapabilityStatus;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import com.google.appengine.tools.development.testing.LocalBlobstoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.remoteapi.RemoteApiInstaller;
import com.google.appengine.tools.remoteapi.RemoteApiOptions;
//import com.google.appengine.api.images.ImagesService;
//import com.google.appengine.api.images.ImagesServiceFactory;

/**
 * Sets-up the local google app engine datastore with test images. Requires that
 * the project have the standard Maven directory structure.
 * 
 * 
 * @author Chris Hatton
 */
@SuppressWarnings("all")
public class ImageUploader {
	
	  /** Use this to make the library run locally as opposed to in a deployed servlet.*/
	private LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalBlobstoreServiceTestConfig(), new LocalDatastoreServiceTestConfig());

	/**
	 * 
	 */
	public void setupImages() throws Exception {
		
	  

//		GoogleAppEngineProperties devServerProperties = getDevServerProperties();
//
//		RemoteApiOptions options = new RemoteApiOptions()
//		        .server(devServerProperties.getServer(), devServerProperties.getPort())
//		        .credentials(devServerProperties.getUsername(), devServerProperties.getPassword());

//		RemoteApiInstaller installer = new RemoteApiInstaller();
//		try {
//			installer.install(options);
			
//			checkCapabilities();
//			deleteAllFromGoogleCloudStorage();
			writeFilesToGoogleCloudStorage();
			
			
			
//		} catch (IllegalArgumentException iae) {
//			iae.printStackTrace();
//			throw new Exception("Server or credentials weren't provided. Please check the properties file.");
//		} catch (IOException e) {
//			e.printStackTrace();
//			throw new Exception("Unable to connect to the remote API. Check the dev server is running.");
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			installer.uninstall();
//		}


		// now check file fro prevois runs and if app engine has changed versin

		// delete all that is in app engine

		// put in your own

		// save this to file

		// update the satus file
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public GoogleAppEngineProperties getDevServerProperties() throws Exception {
		String projectDirectoryStr = System.getProperty("user.dir");
		String gaeLocalPropertiesFileStr = projectDirectoryStr
		        + "//src//test//resources//setup//gae-local.properties".replace("//", File.separator);

		File gaeLocalPropertiesFile = new File(gaeLocalPropertiesFileStr);
		if (!gaeLocalPropertiesFile.exists()) {
			throw new Exception("App engine properties files not found, was expecting it to be here: " + gaeLocalPropertiesFileStr);
		}

		Properties properties = new Properties();
		FileInputStream fio = new FileInputStream(gaeLocalPropertiesFile);
		properties.load(fio);

		GoogleAppEngineProperties googleAppEngineProperties = new GoogleAppEngineProperties();
		googleAppEngineProperties.setServer(properties.getProperty("gae.server"));
		googleAppEngineProperties.setPort(Integer.valueOf(properties.getProperty("gae.port")));
		googleAppEngineProperties.setUsername(properties.getProperty("gae.username"));
		googleAppEngineProperties.setPassword(properties.getProperty("gae.password"));

		return googleAppEngineProperties;
	}

	private void checkCapabilities() throws Exception {
		CapabilitiesService service = CapabilitiesServiceFactory.getCapabilitiesService();

		List<CapabilityState> statuses = new ArrayList<>();
		statuses.add(service.getStatus(Capability.BLOBSTORE));
		statuses.add(service.getStatus(Capability.DATASTORE));
		statuses.add(service.getStatus(Capability.DATASTORE_WRITE));
		statuses.add(service.getStatus(Capability.IMAGES));
		statuses.add(service.getStatus(Capability.MAIL));
		statuses.add(service.getStatus(Capability.MEMCACHE));
		statuses.add(service.getStatus(Capability.PROSPECTIVE_SEARCH));
		statuses.add(service.getStatus(Capability.TASKQUEUE));
		statuses.add(service.getStatus(Capability.URL_FETCH));
		statuses.add(service.getStatus(Capability.XMPP));

		for (CapabilityState s : statuses) {
			if (s.getStatus() != CapabilityStatus.ENABLED) {
				throw new Exception("The status of " + s.getCapability() + " is " + s.getStatus()
				        + ". Perhaps you should check why it is not "
				        + CapabilityStatus.ENABLED + " before continuing");
			}
		}
	}

	private void deleteAllFromGoogleCloudStorage() {

		ImagesService imagesService = ImagesServiceFactory.getImagesService();

		BlobInfoFactory blobInfoFactory = new BlobInfoFactory();
		Iterator<BlobInfo> queryBlobInfos = blobInfoFactory.queryBlobInfos();

		while (queryBlobInfos.hasNext()) {
			BlobInfo blobInfo = queryBlobInfos.next();
//			System.out.println(blobInfo.getFilename());
		}



	}
	
	
	private void writeFilesToGoogleCloudStorage() {
		
		  helper.setUp();
		 GcsService gcsService =  GcsServiceFactory.createGcsService(new RetryParams.Builder()
    	      .initialRetryDelayMillis(10)
    	      .retryMaxAttempts(10)
    	      .totalRetryPeriodMillis(15000)
    	      .build()); 
		 GcsOutputChannel createOrReplace = null;
		 try {
			 
			 List<Path> paths = getImagePaths();
			 for (Path f : paths) {
//				 System.out.println(f.getFileName());
			 }
			 
			 Path testPath = paths.get(0);
			byte[] readAllBytes = Files.readAllBytes(testPath);
			
			GcsFilename gcsFilename = new GcsFilename("ProductImagesChris", "testimageload"+testPath.getFileName().toString());
			GcsFileOptions gcsFileOptions = GcsFileOptions.getDefaultInstance();
	        createOrReplace = gcsService.createOrReplace(gcsFilename, gcsFileOptions);
	        
	        
	        ByteBuffer wrap = ByteBuffer.wrap(readAllBytes);
	        
	        int write = createOrReplace.write(wrap);
	        createOrReplace.close();
	        
        } catch (IOException e) {
	        e.printStackTrace();
        } catch(Exception ee) {
        	ee.printStackTrace();
        } finally {
        	try {
	            createOrReplace.close();
	            
	            helper.tearDown();
            } catch (IOException e) {
	            e.printStackTrace();
            }
        }
		 
	}
	
	private List<Path> getImagePaths() throws IOException {
		List<Path> paths = new ArrayList<>();
		
		String projectDirectoryStr = System.getProperty("user.dir");
		String imagesDirStr = projectDirectoryStr
		        + "//src//test//resources//setup//images".replace("//", File.separator);
		
		Path imageDirPath = Paths.get(imagesDirStr);
		
		// java 7 try-with-resources, auto closes resources i.e. classes that implement java.io.Closeable 
        try (
        		DirectoryStream<Path> directoryStream = Files.newDirectoryStream(imageDirPath);
        	) {
        	Iterator<Path> iterator = directoryStream.iterator();
        	while (iterator.hasNext()) {
        		paths.add(iterator.next());
        	}
        } 
		
		return paths;
	}
}
