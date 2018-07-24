package com.dao;

import com.core.GenericDAO;
import com.dto.TipoOcorrenciaDTO;
import java.util.logging.Logger;

public class TipoOcorrenciaDAO extends GenericDAO<TipoOcorrenciaDTO>{
    
    private static final long serialVersionUID = 7001L;   
    private volatile static TipoOcorrenciaDAO uniqueInstance;
    static final Logger logger = Logger.getLogger(TipoOcorrenciaDAO.class.getName());
    
    public static TipoOcorrenciaDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (TipoOcorrenciaDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new TipoOcorrenciaDAO();
                }
            }
        }
        return uniqueInstance;
    } 
    
    public TipoOcorrenciaDAO() {
        super(TipoOcorrenciaDTO.class);
    }      
}
