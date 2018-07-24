package com.util;

import com.dto.UsuarioDTO;
import java.util.List;

public class ThreadEmail extends Thread {

    private List<UsuarioDTO> to;
    private String msg;
    private String titulo;

    public ThreadEmail(List<UsuarioDTO> to, String msg, String titulo) {        
        this.to = to;
        this.msg = msg;
        this.titulo = titulo;
    }        
    
    @Override
    public void run() {
        SendMail sm = new SendMail();
        for(UsuarioDTO u : this.to){
            sm.sendMail("intranetifc@gmail.com", u.getEmail(), this.titulo, this.msg);
        }
        this.interrupt();
    }

    public List<UsuarioDTO> getTo() {
        return to;
    }

    public void setTo(List<UsuarioDTO> to) {
        this.to = to;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}