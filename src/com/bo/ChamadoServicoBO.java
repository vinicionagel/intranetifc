package com.bo;

import com.auxiliar.Contexto;
import com.core.GenericBO;
import com.dao.ChamadoServicoDAO;
import com.dto.CampusDTO;
import com.dto.ChamadoServicoDTO;
import com.dto.SetorDTO;
import com.dto.StatusDTO;
import com.dto.UsuarioDTO;
import java.util.Date;
import java.util.List;

public class ChamadoServicoBO extends GenericBO<ChamadoServicoDTO>{
    
    private static final long serialVersionUID = 107L;
    private ChamadoServicoDAO dao = ChamadoServicoDAO.getInstance();
    
    private volatile static ChamadoServicoBO uniqueInstance;

    public static ChamadoServicoBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (ChamadoServicoBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new ChamadoServicoBO();
                }
            }
        }
        return uniqueInstance;
    }

    public ChamadoServicoBO() {
        super(ChamadoServicoDTO.class);
    }                
    
    
    public ChamadoServicoDTO pesquisarSeChamadoPertenceUsuario(UsuarioDTO usuario, long id){
        try{
            return dao.pesquisarSeChamadoPertenceUsuario(usuario, id);
        } catch (Exception e) {
            return null;
        }
    }
    
    public boolean verificarSeExisteChamadoSetor(SetorDTO setorDTO, Date dataInicial, Date dataFinal) {
        try{
            return dao.verificarSeExisteChamadoSetor(setorDTO, dataInicial, dataFinal);
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean verificarSeExisteChamadoStatusSetores(StatusDTO status, UsuarioDTO usuario, Date dataInicial, Date dataFinal){
        try {
            return dao.verificarSeExisteChamadoStatusSetores(status, usuario, dataInicial, dataFinal);
        } catch (Exception e) {
            return false;
        }
    }
    
    public List<ChamadoServicoDTO> pesquisarChamadosPorStatusPorSetor(StatusDTO status, SetorDTO setor, Date dataInicial, Date dataFinal) {        
        try {
            return dao.pesquisarChamadosPorStatusPorSetor(status, setor, dataInicial, dataFinal);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<ChamadoServicoDTO> pesquisarChamadosServicoPorTitulo(String titulo, Contexto contexto) {        
        try {
            return dao.pesquisarChamadosServicoPorTitulo(titulo, contexto);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<ChamadoServicoDTO> pesquisarChamadosServicoPorSetorUsuario(Contexto contexto) {        
        try {
            return dao.pesquisarChamadosServicoPorSetorUsuario(contexto);
        } catch (Exception e) {
            return null;
        }
    }
       
    public List<ChamadoServicoDTO> pesquisarChamadosServicoPeloFiltro(ChamadoServicoDTO chamado, SetorDTO setor, Contexto contexto, Date dataInicial, Date dataFinal) {
        try {
            return dao.pesquisarChamadosServicoPeloFiltro(chamado, setor, contexto, dataInicial, dataFinal);
        } catch (Exception e) {
            return null;
        }
    }    
    
    public List<ChamadoServicoDTO> pesquisarChamadosServicoObservados(Contexto contexto) {
        try {
            return dao.pesquisarChamadosServicoObservados(contexto);
        } catch (Exception e) {
            return null;
        }
    }    
    
    public List<ChamadoServicoDTO> pesquisarChamadosServicoAtribuidos(Contexto contexto) {
        try {
            return dao.pesquisarChamadosServicoAtribuidos(contexto);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<ChamadoServicoDTO> pesquisarChamadosServicoPorUsuarioEntreDatas(UsuarioDTO usuario, Date dataInicial, Date dataFinal) {
        try {
            return dao.pesquisarChamadosServicoPorUsuarioEntreDatas(usuario, dataInicial, dataFinal);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<ChamadoServicoDTO> pesquisarChamadosServicoPorSetorEntreDatas(SetorDTO setor, Date dataInicial, Date dataFinal) {
        try {
            return dao.pesquisarChamadosServicoPorSetorEntreDatas(setor, dataInicial, dataFinal);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<ChamadoServicoDTO> pesquisarChamadosServicoPorCampusEntreDatas(CampusDTO campus, Date dataInicial, Date dataFinal) {
        try {
            return dao.pesquisarChamadosServicoPorCampusEntreDatas(campus, dataInicial, dataFinal);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<ChamadoServicoDTO> pesquisarMeusChamadosServico(Contexto contexto) {
        try {
            return dao.pesquisarMeusChamadosServico(contexto);
        } catch (Exception e) {
            return null;
        }
    }
}