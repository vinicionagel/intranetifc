package com.bo;

import com.core.GenericBO;
import com.dao.TipoPermissaoDAO;
import com.dto.TipoPermissaoDTO;
import java.util.List;

public class TipoPermissaoBO extends GenericBO<TipoPermissaoDTO> {

    private static final long serialVersionUID = 123L;
    private TipoPermissaoDAO dao = TipoPermissaoDAO.getInstance();
    private volatile static TipoPermissaoBO uniqueInstance;

    public TipoPermissaoBO() {
        super(TipoPermissaoDTO.class);
    }
    
    public static TipoPermissaoBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (TipoPermissaoBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new TipoPermissaoBO();
                }
            }
        }
        return uniqueInstance;
    }
    
    @Override
    public boolean unique(TipoPermissaoDTO tipoPermissaoDTO){
        try{
            return dao.unique(tipoPermissaoDTO);
        } catch (Throwable e) {
            return false;
        }
    }

    public List<TipoPermissaoDTO> pesquisarNome(TipoPermissaoDTO tipoPermissaoDTO) {
        try {
            return dao.pesquisarNome(tipoPermissaoDTO);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
}