package com.bo;

import com.core.GenericBO;
import com.dao.LocalizacaoDAO;
import com.dto.CampusDTO;
import com.dto.LocalizacaoDTO;
import java.util.List;

public class LocalizacaoBO extends GenericBO<LocalizacaoDTO> {

    private static final long serialVersionUID = 112L;
    private LocalizacaoDAO dao = LocalizacaoDAO.getInstance();
    private volatile static LocalizacaoBO uniqueInstance;

    public static LocalizacaoBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (LocalizacaoBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new LocalizacaoBO();
                }
            }
        }
        return uniqueInstance;
    }

    public LocalizacaoBO() {
        super(LocalizacaoDTO.class);
    }

    @Override
    public boolean unique(LocalizacaoDTO localizacaoDTO) {
        try {
            return dao.unique(localizacaoDTO);
        } catch (Throwable e) {
            return false;
        }
    }

    public List<LocalizacaoDTO> pesquisarNome(LocalizacaoDTO localizacaoDTO) {
        try {
            return dao.pesquisarNome(localizacaoDTO);
        } catch (Exception e) {
            return null;
        }
    }

    public List<LocalizacaoDTO> pesquisarNomeECampus(String localizacaoDTO, CampusDTO campusDTO) {
        try {
            return dao.pesquisarNomeECampus(localizacaoDTO, campusDTO);
        } catch (Exception e) {
            return null;
        }
    }

    public List<LocalizacaoDTO> pesquisarPorCampus(CampusDTO campusDTO) {
        try {
            return dao.pesquisarPorCampus(campusDTO);
        } catch (Exception e) {
            return null;
        }
    }
}