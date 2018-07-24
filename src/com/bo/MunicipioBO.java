/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bo;

import com.core.GenericBO;
import com.dao.MunicipioDAO;
import com.dto.MunicipioDTO;

/**
 *
 * @author vinicio
 */
public class MunicipioBO extends GenericBO<MunicipioDTO>{
    
    private static final long serialVersionUID = 30009L;
    private MunicipioDAO dao = MunicipioDAO.getInstance();

    private volatile static MunicipioBO uniqueInstance;

    public static MunicipioBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (MunicipioBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new MunicipioBO();
                }
            }
        }
        return uniqueInstance;
    }

    
    public MunicipioBO() {
        super(MunicipioDTO.class);
    }

}
