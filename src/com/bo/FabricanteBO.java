package com.bo;

import com.core.GenericBO;
import com.dao.FabricanteDAO;
import com.dto.FabricanteDTO;
import java.util.List;

public class FabricanteBO extends GenericBO<FabricanteDTO>{

    private static final long serialVersionUID = 108L;
    private FabricanteDAO dao = FabricanteDAO.getInstance();

    private volatile static FabricanteBO uniqueInstance;

    public static FabricanteBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (FabricanteBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new FabricanteBO();
                }
            }
        }
        return uniqueInstance;
    }

    
    public FabricanteBO() {
        super(FabricanteDTO.class);
    }

    @Override
    public boolean unique(FabricanteDTO fabricanteDTO) {
        try {
            return dao.unique(fabricanteDTO);
        } catch (Throwable e) {
            return false;
        }
    }

    public List<FabricanteDTO> pesquisarNome(String fabricanteDTO)  {
        try {
            return dao.pesquisarNome(fabricanteDTO);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<FabricanteDTO> findAll() {
        try {
            return dao.findAll();
        } catch (Exception e) {
            return null;
        }
    }
}