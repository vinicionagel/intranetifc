package com.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

class SimpleAuth extends Authenticator {  
    public String username = null;  
    public String password = null;  
  
    public SimpleAuth(String user, String pwd) {  
        username = user;  
        password = pwd;  
    }  
  
    @Override
    protected PasswordAuthentication getPasswordAuthentication() {  
        return new PasswordAuthentication (username,password);  
    }
}
