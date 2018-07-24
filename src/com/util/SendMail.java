package com.util;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
  
public class SendMail {  
      
    static final Logger logger = Logger.getLogger(SendMail.class.getName());
    private String mailSMTPServer;  
    private String mailSMTPServerPort;  
       
    public SendMail() {  
        mailSMTPServer = "smtp.gmail.com";  
        mailSMTPServerPort = "465";  
    }  

    public SendMail(String mailSMTPServer, String mailSMTPServerPort) { 
        this.mailSMTPServer = mailSMTPServer;  
        this.mailSMTPServerPort = mailSMTPServerPort;  
    }  
     
    public void sendMail(String from, String to, String subject, String message) {            
        Properties props = new Properties();   
  
        if(to.contains("@gmail.com") || to.contains("@ifc-riodosul.edu.br")){
            mailSMTPServer = "smtp.gmail.com";  
            mailSMTPServerPort = "465"; 
        }
        if(to.contains("@hotmail.com") || to.contains("@live.com")){
            mailSMTPServer = "smtp.live.com";
            mailSMTPServerPort = "587";
        }
        
        props.put("mail.transport.protocol", "smtp");  
        props.put("mail.smtp.starttls.enable","true");   
        props.put("mail.smtp.host", mailSMTPServer); 
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.user", from); 
        props.put("mail.debug", "true");  
        props.put("mail.smtp.port", mailSMTPServerPort);
        props.put("mail.smtp.socketFactory.port", mailSMTPServerPort);  
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");  
        props.put("mail.smtp.socketFactory.fallback", "false");  
           
        SimpleAuth auth = new SimpleAuth ("intranetifc@gmail.com","siga@ifc$bcc");  
        
        Session session = Session.getInstance(props, auth);  
        session.setDebug(true); 
    
        Message msg = new MimeMessage(session);  
  
        try {  
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));   
            msg.setFrom(new InternetAddress(from));    
            msg.setSubject(subject);  
            msg.setContent(message,"text/html");    
        } catch (RuntimeException | MessagingException e) {  
            logger.log(Level.SEVERE, e.getMessage(), e);
        }  
        Transport tr;  
        try {  
            tr = session.getTransport("smtp"); 
            
            tr.connect("smtp.gmail.com", "intranetifc@gmail.com", "siga@ifc$bcc");  
            msg.saveChanges(); 
            tr.sendMessage(msg, msg.getAllRecipients());  
            tr.close();  
        } catch (RuntimeException | MessagingException e) {  
            logger.log(Level.SEVERE, e.getMessage(), e); 
        }    
    }  
}  