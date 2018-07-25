package com.bo;

import com.auxiliar.Contexto;
import com.core.GenericBO;
import com.dao.SetorDAO;
import com.dto.CampusDTO;
import com.dto.OcorrenciaDTO;
import com.dto.SetorDTO;
import com.dto.UsuarioDTO;
import java.util.List;

public class SetorBO extends GenericBO<SetorDTO>{

    private static final long serialVersionUID = 120L;
    private SetorDAO dao = SetorDAO.getInstance();

    private volatile static SetorBO uniqueInstance;

    public static SetorBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (SetorBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new SetorBO();
                }
            }
        }
        return uniqueInstance;
    }
    
    public SetorBO() {
        super(SetorDTO.class);
    }
    
    @Override
    public boolean unique(SetorDTO setorDTO) {
        try {
            return dao.unique(setorDTO);
        } catch (Throwable e) {
            return false;
        }
    }

    public List<SetorDTO> pesquisarNome(SetorDTO setorDTO)  {
        try {
            return dao.pesquisarNome(setorDTO);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<SetorDTO> pesquisarNomeECampus(String setorDTO, CampusDTO campusDTO)  {
        try {
            return dao.pesquisarNomeECampus(setorDTO, campusDTO);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<SetorDTO> pesquisarSetorPorCampus(String setorDTO, CampusDTO campusDTO)  {
        try {
            return dao.pesquisarSetorPorCampus(setorDTO, campusDTO);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<SetorDTO> consultarSetoresDoUsuario(UsuarioDTO usuario)  {
        try {
            return dao.consultaTodosSetoresMenosUsuarioSemSetorDoUsuario(usuario);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<SetorDTO> consultarSetoresDoUsuarioPorCampus(Contexto contexto)  {
        try {
            return dao.consultarSetoresDoUsuarioPorCampus(contexto);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<SetorDTO> consultarSetoresDoUsuarioPorCampus(UsuarioDTO usuario, CampusDTO campus)  {
        try {
            return dao.consultarSetoresDoUsuarioPorCampus(usuario, campus);
        } catch (Exception e) {
            return null;
        }
    }

    public List<SetorDTO> pesquisarPorCampus(CampusDTO campusDTO) {
        try {
            return dao.pesquisarPorCampus(campusDTO);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<SetorDTO> pesquisarPorCampus(List<CampusDTO> campusDTOsUsuario) {
        try {
            return dao.pesquisarSetoresPorCampus(campusDTOsUsuario);
        } catch (Exception e) {
            return null;
        }
    }

    public List<SetorDTO> consultarSetoresOrdenadosPorCampus(Contexto contexto) {
        try {
            return dao.consultarSetoresOrdenadosPorCampus(contexto);
        } catch (Exception e) {
            return null;
        }
    }

    public SetorDTO pesquisarDescricao(String setorDTO)  {
        try {
            return dao.pesquisarDescricao(setorDTO);
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public List<SetorDTO> findAll(){
        try {
            return dao.findAll();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<SetorDTO> pesquisarTodosMenosSelecionado(SetorDTO setor){
        try {            
            return dao.pesquisarTodosMenosSelecionado(setor);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<SetorDTO> pesquisarTodosMenosSetorSemSetor(){
        try {
            return dao.pesquisarTodosMenosSetorSemSetor();
        } catch (Exception e) {
            return null;
        }
    }
    
    public SetorDTO pesquisarSetorUsuariosSemSetor(CampusDTO campus)  {
	SetorDTO retorno = dao.pesquisarSetorUsuariosSemSetor(campus);
	
	if (retorno == null) {
	    retorno = new SetorDTO();
	    
	    retorno.setCampusDTO(campus);
	    retorno.setDescricao("Usuário Sem Setor");
	    retorno.setInterfaceAtivo("''");
	    save(retorno);
	}
	
	return retorno;
    }
    
    public List<SetorDTO> consultarSetoresNoCampusAutoresOcorrencia(OcorrenciaDTO ocorrenciaDTO){
        return null;
    }
    
    public List<SetorDTO> consultarSetoresDoUsuarioOcorrenciaPorCampus(Contexto c) {
        return dao.consultarSetoresDoUsuarioOcorrenciaPorCampus(c.getUsuarioLogado(),c.getCampusAtual());
    }
    
    public List<SetorDTO> pesquisarPorCampusOcorrencia(CampusDTO campusDTO) {
        return dao.pesquisarPorCampusOcorrencia(campusDTO);
    }
    
    public List<SetorDTO> consultarSetoresDoUsuarioPorCampusUsuarioSemSetor(UsuarioDTO user, CampusDTO campusDTO) {
        return dao.consultarSetoresDoUsuarioPorCampusUsuarioSemSetor(user,campusDTO);
    }
    
    public List<SetorDTO> pesquisarTodosMenosSetorSemSetorPorCampus(CampusDTO campusDTO){
        return dao.pesquisarTodosMenosSetorSemSetorPorCampus(campusDTO);
    }
    
    public List<SetorDTO> consultarSetoresDoUsuario(UsuarioDTO usuario, CampusDTO campusDTO){
        return dao.consultarSetoresDoUsuario(usuario,campusDTO);
    }
}