package com.bo;

import com.auxiliar.Contexto;
import com.core.GenericBO;
import com.dao.ChamadoDAO;
import com.dto.ChamadoDTO;
import com.dto.UsuarioDTO;
import java.util.List;

public class ChamadoBO extends GenericBO<ChamadoDTO>{

    private static final long serialVersionUID = 105L;
    private ChamadoDAO dao = ChamadoDAO.getInstance();

    private volatile static ChamadoBO uniqueInstance;
    
    public static ChamadoBO getInstance(){
        if (uniqueInstance == null){
            synchronized (ChamadoBO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new ChamadoBO();
                }
            }
        }
        return uniqueInstance;
    }    
    
    
    public ChamadoBO() {
        super(ChamadoDTO.class);
    }
    
    public String pesquisarTituloChamado(ChamadoDTO c){
        try{                            
            return dao.pesquisarTituloChamado(c);
        } catch (Exception e) {          
            return null;
        }
    }
    
    public List<ChamadoDTO> consultarChamadosProximaSemana(UsuarioDTO usuario)  {
        return dao.pesquisarChamadosProximaSemana(usuario);
    }
    
    public List<ChamadoDTO> consultarMeusChamados(Contexto contexto) {
	return dao.pesquisarMeusChamados(contexto);
    }
}