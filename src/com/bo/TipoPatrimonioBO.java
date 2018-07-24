package com.bo;

import com.core.GenericBO;
import com.dao.TipoPatrimonioDAO;
import com.dto.PatrimonioDTO;
import com.dto.TipoPatrimonioDTO;
import java.util.List;

public class TipoPatrimonioBO extends GenericBO<TipoPatrimonioDTO>{

    private static final long serialVersionUID = 122L;
    private TipoPatrimonioDAO dao = TipoPatrimonioDAO.getInstance();
    
    private volatile static TipoPatrimonioBO uniqueInstance;

    public static TipoPatrimonioBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (TipoPatrimonioBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new TipoPatrimonioBO();
                }
            }
        }
        return uniqueInstance;
    }

    
    public TipoPatrimonioBO() {
        super(TipoPatrimonioDTO.class);
    }

    @Override
    public boolean unique(TipoPatrimonioDTO tipoPatrimonioDTO) {
        try{
            return dao.unique(tipoPatrimonioDTO);
        } catch (Throwable e) {
            return false;
        }
    }

    public List<TipoPatrimonioDTO> pesquisarNome(String tipoPatrimonioDTO) {
        try {
            return dao.pesquisarNome(tipoPatrimonioDTO);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
    
    public List<TipoPatrimonioDTO> pesquisarTipoPatrimonioPorPatrimonios(List<PatrimonioDTO> patrimonios) {
        try {
            return dao.pesquisarTipoPatrimonioPorPatrimonios(patrimonios);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
    
    @Override
    public List<TipoPatrimonioDTO> findAll(){
        try {
            return dao.findAll();
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
}