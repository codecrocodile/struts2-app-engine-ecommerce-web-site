/*
 * Emailer.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.common.email;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author Chris Hatton
 */
public class Emailer {
    
    /** Normally required to set mail host and transport protocol etc.. not required to set this for GAE */
    private Properties props = new Properties();
    
    private Session session = Session.getDefaultInstance(props, null);
    
    /**
     * Constructor
     */
    public Emailer() {
        super();
    }

    //TODO ensure that the senderAddress is on that is allowed by GAE i.e. one they can respond to - it will fail otherwise
    
    public void sendEmail(String senderAddress, String senderName, String recipientAddress, String recipientName, String subject, String msgBody) {
        Message msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(senderAddress, senderName));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientAddress, recipientName));
            msg.setSubject(subject);
            msg.setText(msgBody);
            Transport.send(msg);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    
    public void sendHtmlEmail(String senderAddress, String senderName, String recipientAddress, String recipientName, String subject, String htmlMsgBody) {
        Message msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(senderAddress, senderName));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientAddress, recipientName));
            msg.setSubject(subject);
            Multipart mp = new MimeMultipart();
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlMsgBody, "text/html");
            mp.addBodyPart(htmlPart);
            msg.setContent(mp);
            Transport.send(msg);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
