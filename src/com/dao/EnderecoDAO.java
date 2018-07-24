/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.core.GenericDAO;
import com.dto.EnderecoDTO;
import java.util.logging.Logger;

/**
 *
 * @author vinicio
 */
public class EnderecoDAO extends GenericDAO<EnderecoDTO>{
    
    private static final long serialVersionUID = 20011L;   
    private volatile static EnderecoDAO uniqueInstance;
    static final Logger logger = Logger.getLogger(EnderecoDAO.class.getName());
    
    public static EnderecoDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (EnderecoDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new EnderecoDAO();
                }
            }
        }
        return uniqueInstance;
    } 
    
    public EnderecoDAO() {
        super(EnderecoDTO.class);
    }      
}
