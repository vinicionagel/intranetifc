package com.converter;

import com.bo.AlunoBO;
import com.dto.AlunoDTO;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;


public class AlunoConverter implements Converter{
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {  
        try {   
            AlunoDTO c = AlunoBO.getInstance().findById(Long.parseLong(value) );  
            return c;  
        } catch (Exception e) {
            return null;
        }
    }  
  
    @Override  
    public String getAsString(FacesContext context, UIComponent component, Object value) {  
        AlunoDTO c = (AlunoDTO) value;  
        return String.valueOf( c.getCodigo());  
    }  
}
