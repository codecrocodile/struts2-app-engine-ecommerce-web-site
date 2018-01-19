/*
 * BacklinkReporter.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.admin.sitemanagement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Chris Hatton
 */
public class BacklinkReporter {
    
    /*
     * This could be made much more sophisticated. We could check for the link being hidden e.g. in comments; in script, etc...
     * 
     * We could also check for broken links etc...
     */
    
    public enum BacklinkStatus {
        NOT_FOUND,
        FOUND,
        FOUND_WITH_NO_FOLLOW
    }
    
    public BacklinkStatus getBacklinkStatus(String websiteUrl) {
        try {
            URL url = new URL(websiteUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            
            StringBuilder sb = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            
            String anchorHtml = this.getLink(sb.toString().toLowerCase());
            if (anchorHtml != null) {
                boolean hasNoFollow = this.hasNoFollow(anchorHtml);
                if (hasNoFollow) {
                    return BacklinkStatus.FOUND_WITH_NO_FOLLOW;
                } else {
                    return BacklinkStatus.FOUND;
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return BacklinkStatus.NOT_FOUND;
    }
    
    @SuppressWarnings("unused")
    private String getLink(String html) {
        String pattern = "<a[^>]*?href\\s*=\\s*((\'|\")http://www.groovyfly.com(\'|\"))[^>]*?(?!/)>([\\w\\.\\s]+)</a>";  
        
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(html);
        boolean b = false;
        String link = null;
        while (b = m.find()) {
            link = m.group();
            return link;
        }
        
        return null;
    }
    
    private boolean hasNoFollow(String anchorHtml) {
        String pattern = "rel\\s*=\\s*((\'|\")nofollow(\'|\"))";  
        
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(anchorHtml);
        @SuppressWarnings("unused")
        boolean b = false;
        while (b = m.find()) {
            return true;
        }
        
        return false;
    }
}
