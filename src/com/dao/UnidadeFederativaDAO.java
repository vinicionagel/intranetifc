/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.core.GenericDAO;
import com.dto.UnidadeFederativaDTO;
import java.util.logging.Logger;

/**
 *
 * @author vinicio
 */
public class UnidadeFederativaDAO extends GenericDAO<UnidadeFederativaDTO> {

    private static final long serialVersionUID = 20007L;
    static final Logger logger = Logger.getLogger(UnidadeFederativaDAO.class.getName());
    private volatile static UnidadeFederativaDAO uniqueInstance;    
    
    public static UnidadeFederativaDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (UnidadeFederativaDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new UnidadeFederativaDAO();
                }
            }
        }
        return uniqueInstance;
    } 
    
    
    public UnidadeFederativaDAO() {
        super(UnidadeFederativaDTO.class);
    }
}
