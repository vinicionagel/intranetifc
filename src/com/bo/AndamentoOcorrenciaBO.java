package com.bo;

import com.core.GenericBO;
import com.dao.AndamentoOcorrenciaDAO;
import com.dto.AndamentoOcorrenciaDTO;
import com.dto.OcorrenciaDTO;
import java.util.List;

public class AndamentoOcorrenciaBO extends GenericBO<AndamentoOcorrenciaDTO>{
    
    private static final long serialVersionUID = 30012L;
    private AndamentoOcorrenciaDAO dao = AndamentoOcorrenciaDAO.getInstance();

    private volatile static AndamentoOcorrenciaBO uniqueInstance;

    public static AndamentoOcorrenciaBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (AndamentoOcorrenciaBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new AndamentoOcorrenciaBO();
                }
            }
        }
        return uniqueInstance;
    }
    
    public AndamentoOcorrenciaBO() {
        super(AndamentoOcorrenciaDTO.class);
    }
    
    public List<AndamentoOcorrenciaDTO> pesquisarAndamentosDaOcorrencia(OcorrenciaDTO ocorrenciaDTO) {
        return dao.pesquisarAndamentosDaOcorrencia(ocorrenciaDTO);
    }
}
