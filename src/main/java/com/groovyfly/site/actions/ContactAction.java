/*
 * ContactAction.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.actions;

import javax.servlet.http.HttpServletRequest;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.groovyfly.common.email.Emailer;
import com.opensymphony.xwork2.Action;

/**
 * @author Chris Hatton
 * 
 * Created 25 Aug 2012
 */
public class ContactAction extends BaseSiteAction implements ServletRequestAware {
    
    private static final long serialVersionUID = 8067551994414046843L;
    
//    private static Logger log = Logger.getLogger(ContactAction.class.getName());
    
    private HttpServletRequest request;

    private String name;
    
    private String email;
    
    private String subject;
    
    private String comments;
    
    private String successMessage;
    
    /* 
     * @see org.apache.struts2.interceptor.ServletRequestAware#setServletRequest(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }
    
    /* 
     * @see com.opensymphony.xwork2.ActionSupport#execute()
     */
    @Override
    @SkipValidation
    public String execute() throws Exception {
    	
        return Action.SUCCESS;
    }
    
    public String sendMessage() throws Exception {
        Emailer emailer = new Emailer();
        emailer.sendEmail("admin@groovyfly.com", "Groovy Fly Admin", "support@groovyfly.com", "Support", "Message from Groovy Fly Contact Page", this.buildMessage());
        
        name = "";
        email = "";
        subject = "";
        comments = "";
        successMessage = "Thank you, we try to answer all inquires within two working days.";
        
        return Action.SUCCESS;
    }
    
    private String buildMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: " + name);
        sb.append("\n");
        sb.append("Subject: " + subject);
        sb.append("\n");
        sb.append("Email: " + email);
        sb.append("\n");        
        sb.append("Comments " + comments);
        
        return sb.toString();
    }
    
    /*
     * @see com.opensymphony.xwork2.ActionSupport#validate()
     */
    public void validate(){
        
        String remoteAddr = request.getRemoteAddr();
        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
        reCaptcha.setPrivateKey("6Le1sdUSAAAAADHrvyT11KneSdfLx68Acsqas8ql");

        String challenge = request.getParameter("recaptcha_challenge_field");
        String uresponse = request.getParameter("recaptcha_response_field");
        ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);

        if (!reCaptchaResponse.isValid()) {
            addFieldError( "captcha", "Captcha words did not match. Please try again." );
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

}
