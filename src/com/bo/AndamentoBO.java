package com.bo;

import com.core.GenericBO;
import com.dao.AndamentoDAO;
import com.dto.AndamentoDTO;
import com.dto.ChamadoDTO;
import java.util.List;

public class AndamentoBO extends GenericBO<AndamentoDTO>{
    
    private static final long serialVersionUID = 101L;
    private AndamentoDAO dao = AndamentoDAO.getInstance();
    
    private volatile static AndamentoBO uniqueInstance;
    
    public static AndamentoBO getInstance(){
        if (uniqueInstance == null){
            synchronized (AndamentoBO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new AndamentoBO();
                }
            }
        }
        return uniqueInstance;
    }    
    
    public AndamentoBO() {
        super(AndamentoDTO.class);
    }
    
    public List<AndamentoDTO> pesquisarNome(AndamentoDTO andamentoDTO){
        try {
            return dao.pesquisarNome(andamentoDTO);
        } catch (Exception e) {
            return null;
        }
    }

    public List<AndamentoDTO> pesquisarAndamentosPorChamada(ChamadoDTO chamadoDTO){
        try{
            return dao.pesquisarAndamentosDaChamada(chamadoDTO);
        }catch (Exception e){
            return null;
        }
    }
}