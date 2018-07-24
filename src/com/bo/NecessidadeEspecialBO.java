package com.bo;

import com.core.GenericBO;
import com.dao.NecessidadeEspecialDAO;
import com.dto.NecessidadeEspecialDTO;

public class NecessidadeEspecialBO extends GenericBO<NecessidadeEspecialDTO>{
    
    private static final long serialVersionUID = 30005L;
    private NecessidadeEspecialDAO dao = NecessidadeEspecialDAO.getInstance();

    private volatile static NecessidadeEspecialBO uniqueInstance;

    public static NecessidadeEspecialBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (NecessidadeEspecialBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new NecessidadeEspecialBO();
                }
            }
        }
        return uniqueInstance;
    }

    public NecessidadeEspecialBO() {
        super(NecessidadeEspecialDTO.class);
    }
}
