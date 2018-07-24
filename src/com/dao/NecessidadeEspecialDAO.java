package com.dao;

import com.core.GenericDAO;
import com.dto.NecessidadeEspecialDTO;
import java.util.logging.Logger;
    
public class NecessidadeEspecialDAO extends GenericDAO<NecessidadeEspecialDTO>{
    
    private static final long serialVersionUID = 20005L;   
    private volatile static NecessidadeEspecialDAO uniqueInstance;
    static final Logger logger = Logger.getLogger(NecessidadeEspecialDAO.class.getName());
    
    public static NecessidadeEspecialDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (NecessidadeEspecialDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new NecessidadeEspecialDAO();
                }
            }
        }
        return uniqueInstance;
    } 
    
    public NecessidadeEspecialDAO() {
        super(NecessidadeEspecialDTO.class);
    }      
}
