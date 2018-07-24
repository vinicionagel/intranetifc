package com.dao;

import com.core.GenericDAO;
import com.dto.ArquivoDTO;

public class ArquivoDAO extends GenericDAO<ArquivoDTO> {

   private static final long serialVersionUID = 1002L;
   private volatile static ArquivoDAO uniqueInstance;
    
    public static ArquivoDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (ArquivoDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new ArquivoDAO();
                }
            }
        }
        return uniqueInstance;
    }  
    
    public ArquivoDAO() {
        super(ArquivoDTO.class);
    }
}