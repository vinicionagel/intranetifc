/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bo;

import com.core.GenericBO;
import com.dao.StatusOcorrenciaDAO;
import com.dto.StatusOcorrenciaDTO;

/**
 *
 * @author vinicio
 */
public class StatusOcorrenciaBO extends GenericBO<StatusOcorrenciaDTO>{

    private static final long serialVersionUID = 30001;
    private StatusOcorrenciaDAO dao = StatusOcorrenciaDAO.getInstance();

    private volatile static StatusOcorrenciaBO uniqueInstance;

    public static StatusOcorrenciaBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (StatusOcorrenciaBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new StatusOcorrenciaBO();
                }
            }
        }
        return uniqueInstance;
    }
    public StatusOcorrenciaBO() {
        super(StatusOcorrenciaDTO.class);
    }
}