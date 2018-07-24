package com.bo;

import com.core.GenericBO;
import com.dao.AndamentoOcorrenciaEditadoDAO;
import com.dto.AndamentoOcorrenciaEditadoDTO;

public class AndamentoOcorrenciaEditadoBO extends GenericBO<AndamentoOcorrenciaEditadoDTO>{
    
    private static final long serialVersionUID = 30012L;
    private AndamentoOcorrenciaEditadoDAO dao = AndamentoOcorrenciaEditadoDAO.getInstance();

    private volatile static AndamentoOcorrenciaEditadoBO uniqueInstance;

    public static AndamentoOcorrenciaEditadoBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (AndamentoOcorrenciaEditadoBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new AndamentoOcorrenciaEditadoBO();
                }
            }
        }
        return uniqueInstance;
    }
    
    public AndamentoOcorrenciaEditadoBO() {
        super(AndamentoOcorrenciaEditadoDTO.class);
    }
}
