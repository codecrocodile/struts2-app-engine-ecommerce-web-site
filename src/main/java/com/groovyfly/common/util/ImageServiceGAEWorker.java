/*
 * ImageServiceWorker.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import com.groovyfly.common.structures.ProductImages;

/**
 * @author Chris Hatton
 */
public class ImageServiceGAEWorker {
    
    private ImagesService imagesService;
    
    private BlobstoreServiceGAEWorker blobServiceWorker;
    
    private Map<UUID, ProductImages> productImagesMap = new HashMap<UUID, ProductImages>();
    
    /**
     * Constructor
     */
    public ImageServiceGAEWorker() {
        imagesService = ImagesServiceFactory.getImagesService();
        blobServiceWorker = new BlobstoreServiceGAEWorker();
    }
    
    public void storeImages(ProductImages images) throws IOException {
       
        storeMediumImage(images);
        storeSmallmage(images);
        storeSmallerImage(images);
        storeLargeImage(images);
    }
    
    public void storeLargeImage(ProductImages images) throws IOException {
        UUID uuid2 = UUID.nameUUIDFromBytes(images.getLargeImageBytes());
        
        if (productImagesMap.containsKey(uuid2)) {
            ProductImages productImages = productImagesMap.get(uuid2);
            
            images.setLargeImageUrl(productImages.getLargeImageUrl());
            images.setLargeImageBlobKeyString(productImages.getLargeImageBlobKeyString());
        } else {         
            String[] urlAndBlobKey = this.transformAndStoreImage(
                    images.getLargeImageBytes(), 
                    images.getLargeImageMimeType(), 
                    images.getLargeImageFileName(), 
                    "large-", 
                    ProductImages.largeImageWidth, 
                    ProductImages.largeImageHeight);
            
            images.setLargeImageUrl(urlAndBlobKey[0]);
            images.setLargeImageBlobKeyString(urlAndBlobKey[1]);
            
            productImagesMap.put(uuid2, images);
        }
    }
    
    public void storeMediumImage(ProductImages images) throws IOException {
        UUID uuid2 = UUID.nameUUIDFromBytes(images.getMediumImageBytes());
        
        if (productImagesMap.containsKey(uuid2)) {
            ProductImages productImages = productImagesMap.get(uuid2);
            
            images.setMediumImageUrl(productImages.getMediumImageUrl());
            images.setMediumImageBlobKeyString(productImages.getMediumImageBlobKeyString());
        } else {         
            String[] urlAndBlobKey = this.transformAndStoreImage(
                    images.getMediumImageBytes(), 
                    images.getMediumImageMimeType(), 
                    images.getMediumImageFileName(), 
                    "medium-", 
                    ProductImages.mediumImageWidth, 
                    ProductImages.mediumImageHeight);
            
            images.setMediumImageUrl(urlAndBlobKey[0]);
            images.setMediumImageBlobKeyString(urlAndBlobKey[1]);
            
            productImagesMap.put(uuid2, images);
        }
    }
    
    public void storeSmallmage(ProductImages images) throws IOException {
        UUID uuid2 = UUID.nameUUIDFromBytes(images.getSmallImageBytes());
        
        if (productImagesMap.containsKey(uuid2)) {
            ProductImages productImages = productImagesMap.get(uuid2);
            
            images.setSmallImageUrl(productImages.getSmallImageUrl());
            images.setSmallImageBlobKeyString(productImages.getSmallImageBlobKeyString());
        } else {         
            String[] urlAndBlobKey = this.transformAndStoreImage(
                    images.getSmallImageBytes(), 
                    images.getSmallImageMimeType(), 
                    images.getSmallImageFileName(), 
                    "small-", 
                    ProductImages.smallImageWidth, 
                    ProductImages.smallImageHeight);
            
            images.setSmallImageUrl(urlAndBlobKey[0]);
            images.setSmallImageBlobKeyString(urlAndBlobKey[1]);
            
            productImagesMap.put(uuid2, images);
        }
    }
    
    public void storeSmallerImage(ProductImages images) throws IOException {
        UUID uuid2 = UUID.nameUUIDFromBytes(images.getSmallerImageBytes());
        
        if (productImagesMap.containsKey(uuid2)) {
            ProductImages productImages = productImagesMap.get(uuid2);
            
            images.setSmallerImageUrl(productImages.getSmallerImageUrl());
            images.setSmallerImageBlobKeyString(productImages.getSmallerImageBlobKeyString());
        } else {         
            String[] urlAndBlobKey = this.transformAndStoreImage(
                    images.getSmallerImageBytes(), 
                    images.getSmallerImageMimeType(), 
                    images.getSmallerImageFileName(), 
                    "smaller-", 
                    ProductImages.smallerImageWidth, 
                    ProductImages.smallerImageHeight);
            
            images.setSmallerImageUrl(urlAndBlobKey[0]);
            images.setSmallerImageBlobKeyString(urlAndBlobKey[1]);
            
            productImagesMap.put(uuid2, images);
        }
    }
    
    private String[] transformAndStoreImage(byte[] imagesBytes, String mimeType, String fileName, String prefix, int width, int height) throws IOException {
        com.google.appengine.api.images.Image image = ImagesServiceFactory.makeImage(imagesBytes);
        
        if (image.getWidth() != width) {
            Transform resize = ImagesServiceFactory.makeResize(width, height, false);
            image = imagesService.applyTransform(resize, image);
        }
        return blobServiceWorker.writeImage(image.getImageData(), mimeType, prefix + fileName, width);
    }
    

    public void deleteBlob(String blobStringToDelete) {
        blobServiceWorker.deleteBlob(blobStringToDelete);
    }

    public void deleteImagesCreatedForSession() throws IOException {
        blobServiceWorker.deleteSessionBlobs();
    }

}
