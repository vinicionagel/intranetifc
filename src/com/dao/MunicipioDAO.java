
package com.dao;

import com.core.GenericDAO;
import com.dto.MunicipioDTO;
import java.util.logging.Logger;


public class MunicipioDAO extends GenericDAO<MunicipioDTO>{
    
    private static final long serialVersionUID = 20009L;   
    private volatile static MunicipioDAO uniqueInstance;
    static final Logger logger = Logger.getLogger(MunicipioDAO.class.getName());
    
    public static MunicipioDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (MunicipioDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new MunicipioDAO();
                }
            }
        }
        return uniqueInstance;
    } 
    
    public MunicipioDAO() {
        super(MunicipioDTO.class);
    }      
}
