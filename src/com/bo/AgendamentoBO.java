/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bo;

import com.core.GenericBO;
import com.dao.AgendamentoDAO;
import com.dto.AgendamentoDTO;
import java.util.List;

/**
 *
 * @author vinicio
 */
public class AgendamentoBO extends GenericBO<AgendamentoDTO> {

    private static final long serialVersionUID = 30018L;
    private AgendamentoDAO dao = AgendamentoDAO.getInstance();

    private volatile static AgendamentoBO uniqueInstance;

    public static AgendamentoBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (AgendamentoBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new AgendamentoBO();
                }
            }
        }
        return uniqueInstance;
    }

    public AgendamentoBO() {
        super(AgendamentoDTO.class);
    }
    
    public List<AgendamentoDTO> findAllOrdenadoPorDia(){
        return dao.findAllOrdenadoPorDia();
    }
}