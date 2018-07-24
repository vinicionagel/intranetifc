/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.core.GenericDAO;
import com.dto.SexoDTO;
import java.util.logging.Logger;

/**
 *
 * @author vinicio
 */
public class SexoDAO extends GenericDAO<SexoDTO> {

    private static final long serialVersionUID = 20003L;
    static final Logger logger = Logger.getLogger(SexoDAO.class.getName());
    private volatile static SexoDAO uniqueInstance;
    
    public static SexoDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (SexoDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new SexoDAO();
                }
            }
        }
        return uniqueInstance;
    } 
    
    public SexoDAO() {
        super(SexoDTO.class);
    }
}
