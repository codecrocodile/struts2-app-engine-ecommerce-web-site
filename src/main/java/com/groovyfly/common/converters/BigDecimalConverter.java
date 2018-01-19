/*
 * BigDecimalConverter.java
 * 
 * Copyright (c) 2013 Groovy Fly
 */
package com.groovyfly.common.converters;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

/**
 * @author Chris Hatton
 */
@SuppressWarnings("rawtypes") 
public class BigDecimalConverter extends StrutsTypeConverter {

    @Override
    public Object convertFromString(Map context, String[] values, Class toClass) {
        if (values != null && values.length > 0) {
            String s = values[0];
            if (s != null && s.trim().length() > 0) {
                
                // replace all comma separators
                s = s.replaceAll(",", "");
                // still has a slight problem with multiple "."'s
                
                BigDecimal bd = new BigDecimal(s);
                return bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            }
        }
        return null;
    }

    @Override
    public String convertToString(Map context, Object o) {
        return o.toString();

    }
}

