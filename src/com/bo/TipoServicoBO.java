package com.bo;

import com.core.GenericBO;
import com.dao.TipoServicoDAO;
import com.dto.CampusDTO;
import com.dto.TipoServicoDTO;
import java.util.List;

public class TipoServicoBO extends GenericBO<TipoServicoDTO>{

    private static final long serialVersionUID = 124L;
    private TipoServicoDAO dao = TipoServicoDAO.getInstance();

    private volatile static TipoServicoBO uniqueInstance;

    public static TipoServicoBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (TipoServicoBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new TipoServicoBO();
                }
            }
        }
        return uniqueInstance;
    }

    
    public TipoServicoBO() {
        super(TipoServicoDTO.class);
    }

    public List<TipoServicoDTO> pesquisarNome(TipoServicoDTO tipoServicoDTO) {
        try {
            return dao.pesquisarNome(tipoServicoDTO);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<TipoServicoDTO> pesquisarNomeECampus(String tipoServicoDTO, CampusDTO campusDTO) {
        try {
            return dao.pesquisarNomeECampus(tipoServicoDTO, campusDTO);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<TipoServicoDTO> pesquisarPorCampus(CampusDTO campusDTO) {
        try {
            return dao.pesquisarPorCampus(campusDTO);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean unique(TipoServicoDTO tipoServicoDTO) {
        try {
            return dao.unique(tipoServicoDTO);
        } catch (Throwable e) {
            return false;
        }
    }
    
    public List<TipoServicoDTO> pesquisarPorSetor(long setorDTO){
        try{
            return dao.pesquisarPorSetor(setorDTO);
        } catch (Exception e){
            return null;
        }
    }
    
    public TipoServicoDTO pesquisarPorSetorEDescricao(Long setorDTO, TipoServicoDTO tipoServico){
        try{
            return dao.pesquisarPorSetorEDescricao(setorDTO, tipoServico);
        } catch (Exception e){
            return null;
        }
    }
    
    @Override
    public List<TipoServicoDTO> findAll(){
        try{
            return dao.findAll();
        } catch (Exception e){
            return null;
        }
    }
}