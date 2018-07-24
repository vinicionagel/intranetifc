package com.dao;

import com.core.GenericDAO;
import com.dto.DiaSemanaDTO;
import java.util.logging.Logger;

public class DiaSemanaDAO extends GenericDAO<DiaSemanaDTO>{
    
    private static final long serialVersionUID = 20017L;   
    private volatile static DiaSemanaDAO uniqueInstance;
    static final Logger logger = Logger.getLogger(DiaSemanaDAO.class.getName());
    
    public static DiaSemanaDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (DiaSemanaDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new DiaSemanaDAO();
                }
            }
        }
        return uniqueInstance;
    } 
    
    public DiaSemanaDAO() {
        super(DiaSemanaDTO.class);
    }    
}
