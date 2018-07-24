package com.bo;

import com.auxiliar.Contexto;
import com.core.GenericBO;
import com.dao.ChamadoInfraDAO;
import com.dto.CampusDTO;
import com.dto.ChamadoInfraDTO;
import com.dto.LocalizacaoDTO;
import com.dto.PatrimonioDTO;
import com.dto.SetorDTO;
import com.dto.StatusDTO;
import com.dto.TipoPatrimonioDTO;
import com.dto.UsuarioDTO;
import java.util.Date;
import java.util.List;

public class ChamadoInfraBO extends GenericBO<ChamadoInfraDTO>{
    
    private static final long serialVersionUID = 106L;
    
    public ChamadoInfraBO() {
        super(ChamadoInfraDTO.class);
    }
    
    ChamadoInfraDAO dao = ChamadoInfraDAO.getInstance();
    
    private volatile static ChamadoInfraBO uniqueInstance;

    public static ChamadoInfraBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (ChamadoInfraBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new ChamadoInfraBO();
                }
            }
        }
        return uniqueInstance;
    }
    
    public ChamadoInfraDTO pesquisarSeChamadoPertenceUsuario(UsuarioDTO usuario, long id){
        try {
            return dao.pesquisarSeChamadoPertenceUsuario(usuario, id);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<ChamadoInfraDTO> pesquisarChamadosPorStatusPorSetor(StatusDTO status, SetorDTO setor, Date dataInicial, Date dataFinal) {        
        try {
            return dao.pesquisarChamadosPorStatusPorSetor(status, setor, dataInicial, dataFinal);
        } catch (Exception e) {
            return null;
        }
    }
        
    public boolean verificarSeExisteChamadoSetor(SetorDTO setorDTO, Date dataInicial, Date dataFinal) {
        try {
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
    
    public List<ChamadoInfraDTO> pesquisarChamadosInfraPorTitulo(String titulo, Contexto contexto) {        
        try {
            return dao.pesquisarChamadosInfraPorTitulo(titulo, contexto);
        } catch (Exception e) {
            return null;
        }
    }    
    
    public List<ChamadoInfraDTO> pesquisarChamadosInfraPorSetorUsuario(Contexto contexto) {        
        try {
            return dao.pesquisarChamadosInfraPorSetorUsuario(contexto);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<ChamadoInfraDTO> pesquisarChamadosInfraObservados(Contexto contexto) {
        try {
            return dao.pesquisarChamadosInfraObservados(contexto);
        } catch (Exception e) {
            return null;
        }
    }    
    
    public List<ChamadoInfraDTO> pesquisarChamadosInfraAtribuidos(Contexto contexto) {
        try {
            return dao.pesquisarChamadosInfraAtribuidos(contexto);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<ChamadoInfraDTO> pesquisarChamadosInfraPorUsuarioEntreDatas(UsuarioDTO usuario, Date dataInicial, Date dataFinal) {
        try {
            return dao.pesquisarChamadosInfraAtribuidosEntreDatas(usuario, dataInicial, dataFinal);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<ChamadoInfraDTO> pesquisarChamadosInfraPorPatrimonioEntreDatas(PatrimonioDTO patrimonio, Date dataInicial, Date dataFinal) {
        try {
            return dao.pesquisarChamadosInfraPorPatrimonioEntreDatas(patrimonio, dataInicial, dataFinal);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<ChamadoInfraDTO> pesquisarChamadosInfraPorTipoPatrimonioEntreDatas(TipoPatrimonioDTO tipoPatrimonio, Date dataInicial, Date dataFinal) {
        try {
            return dao.pesquisarChamadosInfraPorTipoPatrimonioEntreDatas(tipoPatrimonio, dataInicial, dataFinal);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<ChamadoInfraDTO> pesquisarChamadosInfraPorSetorEntreDatas(SetorDTO setor, Date dataInicial, Date dataFinal) {
        try {
            return dao.pesquisarChamadosInfraPorSetorEntreDatas(setor, dataInicial, dataFinal);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<ChamadoInfraDTO> pesquisarChamadosInfraPorCampusEntreDatas(CampusDTO campus, Date dataInicial, Date dataFinal) {
        try {
            return dao.pesquisarChamadosInfraPorCampusEntreDatas(campus, dataInicial, dataFinal);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<ChamadoInfraDTO> pesquisarChamadosInfraPorLocalizacaoEntreDatas(LocalizacaoDTO localizacao, Date dataInicial, Date dataFinal) {
        try {
            return dao.pesquisarChamadosInfraPorLocalizacaoEntreDatas(localizacao, dataInicial, dataFinal);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<ChamadoInfraDTO> pesquisarMeusChamadosInfra(Contexto contexto) {
        try {
            return dao.pesquisarMeusChamadosInfra(contexto);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<ChamadoInfraDTO> pesquisarChamadosInfraPeloFiltro(ChamadoInfraDTO chamado, SetorDTO setor, Contexto contexto, Date dataInicial, Date dataFinal) {
        try {
            return dao.pesquisarChamadosInfraPeloFiltro(chamado, setor, contexto, dataInicial, dataFinal);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<ChamadoInfraDTO> pesquisarEquipamentosEmManutencaoEntreDatasContexto(Date dataInicial, Date dataFinal, String setores) {
        try {
            return dao.pesquisarEquipamentosEmManutencaoEntreDatasContexto(dataInicial, dataFinal, setores);
        } catch (Exception e) {
            return null;
        }
    }
}