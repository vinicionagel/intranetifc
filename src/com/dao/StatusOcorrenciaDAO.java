/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.core.GenericDAO;
import com.dto.StatusOcorrenciaDTO;
import java.util.logging.Logger;

/**
 *
 * @author vinicio
 */
public class StatusOcorrenciaDAO extends GenericDAO<StatusOcorrenciaDTO> {

    private static final long serialVersionUID = 20001L;
    static final Logger logger = Logger.getLogger(StatusOcorrenciaDAO.class.getName());
    private volatile static StatusOcorrenciaDAO uniqueInstance;
    
    public static StatusOcorrenciaDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (StatusOcorrenciaDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new StatusOcorrenciaDAO();
                }
            }
        }
        return uniqueInstance;
    } 
    
    public StatusOcorrenciaDAO() {
        super(StatusOcorrenciaDTO.class);
    }
}
