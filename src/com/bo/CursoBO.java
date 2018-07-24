package com.bo;

import com.core.GenericBO;
import com.dao.CursoDAO;
import com.dto.CursoDTO;

public class CursoBO extends GenericBO<CursoDTO>{
    
    private static final long serialVersionUID = 30014L;
    private CursoDAO dao = CursoDAO.getInstance();

    private volatile static CursoBO uniqueInstance;

    public static CursoBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (CursoBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new CursoBO();
                }
            }
        }
        return uniqueInstance;
    }

    public CursoDTO pesquisarPorDescricao(String cursoDTO)  {
        return dao.pesquisarPorDescricao(cursoDTO);
    }
    
    public CursoBO() {
        super(CursoDTO.class);
    }

}
