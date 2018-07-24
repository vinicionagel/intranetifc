package com.dao;

import com.core.GenericDAO;
import com.dto.AndamentoOcorrenciaEditadoDTO;
import java.util.logging.Logger;

public class AndamentoOcorrenciaEditadoDAO extends GenericDAO<AndamentoOcorrenciaEditadoDTO>{
    
    private static final long serialVersionUID = 20012L;   
    private volatile static AndamentoOcorrenciaEditadoDAO uniqueInstance;
    static final Logger logger = Logger.getLogger(AndamentoOcorrenciaEditadoDAO.class.getName());
    
    public static AndamentoOcorrenciaEditadoDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (AndamentoOcorrenciaEditadoDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new AndamentoOcorrenciaEditadoDAO();
                }
            }
        }
        return uniqueInstance;
    } 
    
    public AndamentoOcorrenciaEditadoDAO() {
        super(AndamentoOcorrenciaEditadoDTO.class);
    }     
}
