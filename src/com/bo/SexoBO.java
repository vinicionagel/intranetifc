/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bo;

import com.core.GenericBO;
import com.dao.SexoDAO;
import com.dto.SexoDTO;

/**
 *
 * @author vinicio
 */
public class SexoBO extends GenericBO<SexoDTO>{

    private static final long serialVersionUID = 30003;
    private SexoDAO dao = SexoDAO.getInstance();

    private volatile static SexoBO uniqueInstance;

    public static SexoBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (SexoBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new SexoBO();
                }
            }
        }
        return uniqueInstance;
    }
    public SexoBO() {
        super(SexoDTO.class);
    }
}
