/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bo;

import com.core.GenericBO;
import com.dao.UnidadeFederativaDAO;
import com.dto.UnidadeFederativaDTO;

/**
 *
 * @author vinicio
 */
public class UnidadeFederativaBO extends GenericBO<UnidadeFederativaDTO>{
    
    private static final long serialVersionUID = 30007L;
    private UnidadeFederativaDAO dao = UnidadeFederativaDAO.getInstance();

    private volatile static UnidadeFederativaBO uniqueInstance;

    public static UnidadeFederativaBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (UnidadeFederativaBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new UnidadeFederativaBO();
                }
            }
        }
        return uniqueInstance;
    }

    
    public UnidadeFederativaBO() {
        super(UnidadeFederativaDTO.class);
    }
}
