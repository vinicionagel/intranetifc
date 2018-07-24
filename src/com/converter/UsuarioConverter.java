/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.converter;

import com.bo.UsuarioBO;
import com.dto.UsuarioDTO;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author vinicio
 */
public class UsuarioConverter implements Converter{
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {  
        try {   
            UsuarioDTO c = UsuarioBO.getInstance().findById(Long.parseLong(value) );  
            return c;  
        } catch (Exception e) {
            return null;
        }
    }  
  
    @Override  
    public String getAsString(FacesContext context, UIComponent component, Object value) {  
        UsuarioDTO c = (UsuarioDTO) value;  
        return String.valueOf( c.getCodigo());  
    }  
}