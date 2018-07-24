package com.bo;

import com.core.GenericBO;
import com.dao.FuncaoDAO;
import com.dto.FuncaoDTO;
import com.dto.SetorDTO;
import com.dto.UsuarioDTO;
import java.util.List;

public class FuncaoBO extends GenericBO<FuncaoDTO> {

    private static final long serialVersionUID = 109L;
    private FuncaoDAO dao = FuncaoDAO.getInstance();
    private volatile static FuncaoBO uniqueInstance;

    public static FuncaoBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (FuncaoBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new FuncaoBO();
                }
            }
        }
        return uniqueInstance;
    }

    public FuncaoBO() {
        super(FuncaoDTO.class);
    }

    @Override
    public boolean unique(FuncaoDTO funcaoDTO) {
        try {
            return dao.unique(funcaoDTO);
        } catch (Throwable e) {
            return false;
        }
    }

    public List<FuncaoDTO> pesquisarNome(String funcaoDTO) {
        try {
            return dao.pesquisarNome(funcaoDTO);
        } catch (Exception e) {
            return null;
        }
    }

    public FuncaoDTO pesquisarFuncaoUsuarioSetor(UsuarioDTO usuario, SetorDTO setor) {
        try {
            return dao.pesquisarFuncaoUsuarioSetor(usuario, setor);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<FuncaoDTO> findAll() {
        try {
            return dao.findAll();
        } catch (Exception e) {
            return null;
        }
    }
}