package com.bo;

import com.core.GenericBO;
import com.dao.CampusDAO;
import com.dto.CampusDTO;
import com.dto.SetorDTO;
import com.dto.UsuarioDTO;
import java.util.List;

public class CampusBO extends GenericBO<CampusDTO>{

    private static final long serialVersionUID = 104L;
    private CampusDAO dao = CampusDAO.getInstance();

    private volatile static CampusBO uniqueInstance;

    public static CampusBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (CampusBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new CampusBO();
                }
            }
        }
        return uniqueInstance;
    }

    
    public CampusBO() {
        super(CampusDTO.class);
    }

    @Override
    public boolean save(CampusDTO obj)  {
	boolean retorno = super.save(obj);
	
	SetorDTO setor = new SetorDTO();
	setor.setCampusDTO(obj);
	setor.setDescricao("Administrador");
	setor.setInterfaceAtivo("''");
        SetorBO.getInstance().save(setor);
	
	return retorno;
    }

    @Override
    public boolean unique(CampusDTO campusDTO) {
        try {
            return dao.unique(campusDTO);
        } catch (Throwable e) {
            return false;
        }        
    }

    public List<CampusDTO> pesquisarNome(String campusDTO) {
        try {
            return dao.pesquisarNome(campusDTO);
        } catch (Exception e) {
            return null;
        }
    }

    public List<CampusDTO> pesquisarCampusPorUsuario(UsuarioDTO usuarioDTO)  {
        try {        
            return dao.pesquisarCampusPorUsuario(usuarioDTO);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<CampusDTO> pesquisarCampusPorSetores(String setores)  {
        try {        
            return dao.pesquisarCampusPorSetores(setores);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<CampusDTO> pesquisarCampusPorUsuarioOrderByDesc(UsuarioDTO usuarioDTO) {
        try{
            return dao.pesquisarCampusPorUsuario(usuarioDTO);
        }catch(Exception e){            
            return null;
        }
    }
}