package com.dao;

import com.core.GenericDAO;
import com.dto.TelefoneDTO;
import java.util.logging.Logger;

public class TelefoneDAO extends GenericDAO<TelefoneDTO>{
    
    private static final long serialVersionUID = 20008L;   
    private volatile static TelefoneDAO uniqueInstance;
    static final Logger logger = Logger.getLogger(TelefoneDAO.class.getName());
    
    public static TelefoneDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (TelefoneDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new TelefoneDAO();
                }
            }
        }
        return uniqueInstance;
    } 
    
    public TelefoneDAO() {
        super(TelefoneDTO.class);
    }      
}
