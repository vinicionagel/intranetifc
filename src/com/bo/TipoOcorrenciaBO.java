package com.bo;

import com.core.GenericBO;
import com.dao.TipoOcorrenciaDAO;
import com.dto.TipoOcorrenciaDTO;

public class TipoOcorrenciaBO extends GenericBO<TipoOcorrenciaDTO>{
    
    private static final long serialVersionUID = 7002L;
    private TipoOcorrenciaDAO dao = TipoOcorrenciaDAO.getInstance();

    private volatile static TipoOcorrenciaBO uniqueInstance;

    public static TipoOcorrenciaBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (TipoOcorrenciaBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new TipoOcorrenciaBO();
                }
            }
        }
        return uniqueInstance;
    }

    
    public TipoOcorrenciaBO() {
        super(TipoOcorrenciaDTO.class);
    }

}
