/*
 * Image.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.structures;

/**
 * @author Chris Hatton
 */
public class ProductImages {
    
    public enum ProductImage {
        LARGE,
        MEDIUM,
        SMALL,
        SMALLER
    }
    
    public static final int largeImageWidth = 800;
    public static final int largeImageHeight = 600;
    public static final int mediumImageWidth = 300;
    public static final int mediumImageHeight = 225;
    public static final int smallImageWidth = 206;
    public static final int smallImageHeight = 136;
    public static final int smallerImageWidth = 58;
    public static final int smallerImageHeight = 58;    
    
    /* large image */
    
    private String base64LargeImage;
    
    private byte[] largeImageBytes;
    
    private String largeImageFileName;
    
    private String largeImageMimeType;
    
    private int largeImageByteSize;
    
    private String largeImageUrl;
    
    private String largeImageBlobKeyString;
    
    
    /* MediumImage */
    
    private String base64MediumImage;
    
    private byte[] mediumImageBytes;
    
    private String mediumImageFileName;
    
    private String mediumImageMimeType;
    
    private int mediumImageByteSize;
    
    private String mediumImageUrl;
    
    private String mediumImageBlobKeyString;
    
   
    /* Small Image */
    
    private String base64SmallImage;
    
    private byte[] smallImageBytes;
    
    private String smallImageFileName;
    
    private String smallImageMimeType;
    
    private int smallImageByteSize;
    
    private String smallImageUrl;
    
    private String smallImageBlobKeyString;
    
    
    /* Smaller Image */

    private String base64SmallerImage;
    
    private byte[] smallerImageBytes;
    
    private String smallerImageFileName;
    
    private String smallerImageMimeType;
    
    private int smallerImageByteSize;
    
    private String smallerImageUrl;
    
    private String smallerImageBlobKeyString;
    

    private String altTagDescription;
    
    
    /**
     * Constructor
     */
    public ProductImages() {
        super();
    }

    
    /* large image */
    
    public String getBase64LargeImage() {
        return base64LargeImage;
    }

    public void setBase64LargeImage(String base64LargeImage) {
        this.base64LargeImage = base64LargeImage;
    }
    
    public byte[] getLargeImageBytes() {
        return largeImageBytes;
    }

    public void setLargeImageBytes(byte[] largeImageBytes) {
        this.largeImageBytes = largeImageBytes;
    }
    
    public String getLargeImageFileName() {
        return largeImageFileName;
    }

    public void setLargeImageFileName(String largeImageFileName) {
        this.largeImageFileName = largeImageFileName;
    }

    public String getLargeImageMimeType() {
        return largeImageMimeType;
    }

    public void setLargeImageMimeType(String largeImageMimeType) {
        this.largeImageMimeType = largeImageMimeType;
    }

    public int getLargeImageByteSize() {
        return largeImageByteSize;
    }

    public void setLargeImageByteSize(int largeImageByteSize) {
        this.largeImageByteSize = largeImageByteSize;
    }
    
    public String getLargeImageUrl() {
        return largeImageUrl;
    }

    public void setLargeImageUrl(String largeImageUrl) {
        this.largeImageUrl = largeImageUrl;
    }

    public String getLargeImageBlobKeyString() {
        return largeImageBlobKeyString;
    }

    public void setLargeImageBlobKeyString(String largeImageBlobKeyString) {
        this.largeImageBlobKeyString = largeImageBlobKeyString;
    }
    
    
    
    /* Medium Image */

    public String getBase64MediumImage() {
        return base64MediumImage;
    }

    public void setBase64MediumImage(String base64MediumImage) {
        this.base64MediumImage = base64MediumImage;
    }

    public byte[] getMediumImageBytes() {
        return mediumImageBytes;
    }

    public void setMediumImageBytes(byte[] mediumImageBytes) {
        this.mediumImageBytes = mediumImageBytes;
    }

    public String getMediumImageFileName() {
        return mediumImageFileName;
    }

    public void setMediumImageFileName(String mediumImageFileName) {
        this.mediumImageFileName = mediumImageFileName;
    }

    public String getMediumImageMimeType() {
        return mediumImageMimeType;
    }

    public void setMediumImageMimeType(String mediumImageMimeType) {
        this.mediumImageMimeType = mediumImageMimeType;
    }

    public int getMediumImageByteSize() {
        return mediumImageByteSize;
    }

    public void setMediumImageByteSize(int mediumImageByteSize) {
        this.mediumImageByteSize = mediumImageByteSize;
    }
    
    public String getMediumImageUrl() {
        return mediumImageUrl;
    }

    public void setMediumImageUrl(String mediumImageUrl) {
        this.mediumImageUrl = mediumImageUrl;
    }
    
    public String getMediumImageBlobKeyString() {
        return mediumImageBlobKeyString;
    }

    public void setMediumImageBlobKeyString(String mediumImageBlobKeyString) {
        this.mediumImageBlobKeyString = mediumImageBlobKeyString;
    }

    
    
    /* Small Image */

    public String getBase64SmallImage() {
        return base64SmallImage;
    }

    public void setBase64SmallImage(String base64SmallImage) {
        this.base64SmallImage = base64SmallImage;
    }

    public byte[] getSmallImageBytes() {
        return smallImageBytes;
    }

    public void setSmallImageBytes(byte[] smallImageBytes) {
        this.smallImageBytes = smallImageBytes;
    }

    public String getSmallImageFileName() {
        return smallImageFileName;
    }

    public void setSmallImageFileName(String smallImageFileName) {
        this.smallImageFileName = smallImageFileName;
    }

    public String getSmallImageMimeType() {
        return smallImageMimeType;
    }

    public void setSmallImageMimeType(String smallImageMimeType) {
        this.smallImageMimeType = smallImageMimeType;
    }

    public int getSmallImageByteSize() {
        return smallImageByteSize;
    }

    public void setSmallImageByteSize(int smallImageByteSize) {
        this.smallImageByteSize = smallImageByteSize;
    }

    public String getSmallImageUrl() {
        return smallImageUrl;
    }
    
    public void setSmallImageUrl(String smallImageUrl) {
        this.smallImageUrl = smallImageUrl;
    }

    public String getSmallImageBlobKeyString() {
        return smallImageBlobKeyString;
    }

    public void setSmallImageBlobKeyString(String smallImageBlobKeyString) {
        this.smallImageBlobKeyString = smallImageBlobKeyString;
    }
    
    
    /* Smaller Image */
    
    public String getBase64SmallerImage() {
        return base64SmallerImage;
    }

    public void setBase64SmallerImage(String base64SmallerImage) {
        this.base64SmallerImage = base64SmallerImage;
    }
    
    public String getSmallerImageFileName() {
        return smallerImageFileName;
    }

    public void setSmallerImageFileName(String smallerImageFileName) {
        this.smallerImageFileName = smallerImageFileName;
    }

    public String getSmallerImageMimeType() {
        return smallerImageMimeType;
    }

    public void setSmallerImageMimeType(String smallerImageMimeType) {
        this.smallerImageMimeType = smallerImageMimeType;
    }

    public int getSmallerImageByteSize() {
        return smallerImageByteSize;
    }

    public void setSmallerImageByteSize(int smallerImageByteSize) {
        this.smallerImageByteSize = smallerImageByteSize;
    }

    public byte[] getSmallerImageBytes() {
        return smallerImageBytes;
    }

    public void setSmallerImageBytes(byte[] smallerImageBytes) {
        this.smallerImageBytes = smallerImageBytes;
    }

    public String getSmallerImageUrl() {
        return smallerImageUrl;
    }

    public void setSmallerImageUrl(String smallerImageUrl) {
        this.smallerImageUrl = smallerImageUrl;
    }
    
    public String getSmallerImageBlobKeyString() {
        return smallerImageBlobKeyString;
    }

    public void setSmallerImageBlobKeyString(String smallerImageBlobKeyString) {
        this.smallerImageBlobKeyString = smallerImageBlobKeyString;
    }

    
    public String getAltTagDescription() {
        return altTagDescription;
    }

    public void setAltTagDescription(String altTagDescription) {
        this.altTagDescription = altTagDescription;
    }

}
