/*
 * BlobstoreServiceWorker.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

/**
 * @author Chris Hatton
 */
public class BlobstoreServiceGAEWorker {
    
	public static final Logger log = Logger.getLogger(BlobstoreServiceGAEWorker.class.toString());
	
    private List<BlobKey> sessionBlobKeys = new ArrayList<BlobKey>();
    
    /**
     * Constructor
     */
    public BlobstoreServiceGAEWorker() {
        super();
    }
    
    /**
     * Saves an image and returns the url and BlobKey string so that we can delete it later if need.
     * 
     * NOTE: At the time of this writing, when we try and save image of 800x600 it is being served back as 512x???
     * which seems to be the default size. We have added =s[width] to this to bring back the correct size of image we need.
     *  
     * @return url for the image
     * @throws IOException
     */
    public String[] writeImage(byte[] bytes, String mimeType, String fileName, int width) throws IOException {
    	
    	return writeFilesToGoogleCloudStorage(bytes, mimeType, fileName, width);
        
//        // get a file service
//        FileService fileService = FileServiceFactory.getFileService();
//        
//        // create a new Blob file with mime-type e.g "image/png"
//        AppEngineFile file = fileService.createNewBlobFile(mimeType, fileName);
//        
//        // open a channel to write to it
//        boolean lock = true;
//        FileWriteChannel writeChannel = fileService.openWriteChannel(file, lock);
//        writeChannel.write(ByteBuffer.wrap(bytes));
//        
//        // finalise
//        writeChannel.closeFinally();
//            
//        // read from the file using the Blobstore API
//        BlobKey blobKey = fileService.getBlobKey(file);
//        sessionBlobKeys.add(blobKey);
//        
//        // use image service to give you a url for the image
//        ImagesService imagesService = ImagesServiceFactory.getImagesService();
//        ServingUrlOptions rurlo = ServingUrlOptions.Builder.withBlobKey(blobKey);
//        
//        rurlo.imageSize(width);
//        
//        String servingUrl = imagesService.getServingUrl(rurlo);
//        String blobKeyString = blobKey.getKeyString();
//        
//        String[] toReturn = {servingUrl, blobKeyString};
//        
//        return toReturn;
    }
    
	private String[] writeFilesToGoogleCloudStorage(byte[] bytes, String mimeType, String fileName, int width) {
		

		GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
	      .initialRetryDelayMillis(10)
	      .retryMaxAttempts(10)
	      .totalRetryPeriodMillis(15000)
	      .requestTimeoutMillis(150000)
	      .retryMinAttempts(5)
	      .build()); 
				
				
				//GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance());
		GcsOutputChannel createOrReplace = null;
		try {

			GcsFilename gcsFilename = new GcsFilename("ProductImages", fileName);
			GcsFileOptions gcsFileOptions = GcsFileOptions.getDefaultInstance();
			createOrReplace = gcsService.createOrReplace(gcsFilename, gcsFileOptions);
			ByteBuffer wrap = ByteBuffer.wrap(bytes);
			createOrReplace.write(wrap);
			createOrReplace.close(); // fuck sake don't forget to close or it disappears from store
			
			
	        // use image service to give you a url for the image
	        ImagesService imagesService = ImagesServiceFactory.getImagesService();
	        
	        log.info("/gs/" + gcsFilename.getBucketName() + "/" + gcsFilename.getObjectName());
	        
	        String cloudStorageURL = "/gs/" + gcsFilename.getBucketName() + "/" + gcsFilename.getObjectName();
	        BlobstoreService bs = BlobstoreServiceFactory.getBlobstoreService();
	        BlobKey bk = bs.createGsBlobKey(cloudStorageURL);
	        
	        ServingUrlOptions rurlo = ServingUrlOptions.Builder.withGoogleStorageFileName(cloudStorageURL); //ServingUrlOptions.Builder.withBlobKey(bk);
	        rurlo.imageSize(width);
	        String servingUrl = imagesService.getServingUrl(rurlo);
	        
	        log.info(servingUrl);
	        
	        String blobKeyString = bk.getKeyString();
	        
			String[] toReturn = { servingUrl, blobKeyString };
			return toReturn;

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception ee) {
			ee.printStackTrace();
		} finally {
			try {
				createOrReplace.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

    /**
     * Deletes all the recent Blob that were created in this http call.
     */
    public void deleteSessionBlobs() {
//        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
//        for (BlobKey blob : sessionBlobKeys) { 
//            blobstoreService.fetchData(blob, 0, 1); 
//            blobstoreService.delete(blob);
//        }
    }
    
    /**
     * Deletes a single Blob from the Blobstore for the given BlobKey string.
     */
    public void deleteBlob(String blobKey) {
//        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
//        BlobKey blobKeyToDelete = new BlobKey(blobKey);
//        blobstoreService.fetchData(blobKeyToDelete, 0, 1); // workaround to get this to work on the development server, doesn't work all the time
//        blobstoreService.delete(blobKeyToDelete); // know problem on the development server doing this, supposed to work ok on the production server : Issue 4744
    }
}
