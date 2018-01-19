/*
 * PackagingType.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.postage;

/**
 * @author Chris Hatton
 * 
 * Created 9 Dec 2012
 */
public enum PostalPackagingType {

    LETTER() {
        /* 
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return "Letter";
        }
    },
    LARGE_LETTER() {
        /* 
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return "Large Letter";
        }
    },
    PARCEL() {
        /* 
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return "Parcel";
        }
    }
}
