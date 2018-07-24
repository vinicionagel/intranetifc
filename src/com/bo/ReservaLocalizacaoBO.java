package com.bo;

import com.core.GenericBO;
import com.dao.ReservaLocalizacaoDAO;
import com.dto.LocalizacaoDTO;
import com.dto.ReservaLocalizacaoDTO;
import java.util.Date;
import java.util.List;

public class ReservaLocalizacaoBO extends GenericBO<ReservaLocalizacaoDTO>{
    
    private static final long serialVersionUID = 117L;
    protected ReservaLocalizacaoDAO dao = ReservaLocalizacaoDAO.getInstance();
    private volatile static ReservaLocalizacaoBO uniqueInstance;
    
    public ReservaLocalizacaoBO(){
        super(ReservaLocalizacaoDTO.class);
    }
    
    public static ReservaLocalizacaoBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (ReservaLocalizacaoBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new ReservaLocalizacaoBO();
                }
            }
        }
        return uniqueInstance;
    }
    
    /**
     * Método usando em unique e para carregamento por semana (lazy loading)
     * @param localizacao
     * @param dataInicial
     * @param dataFinal
     * @return 
     */
    public List<ReservaLocalizacaoDTO> uniqueReservaLocalizacao(LocalizacaoDTO localizacaoDTO, Date dataInicial, Date dataFinal)  {
        try {
            return dao.uniqueReservaLocalizacao(localizacaoDTO, dataInicial, dataFinal);
        } catch (Exception e) {
            return null;
        }
    }
    
    public ReservaLocalizacaoDTO pesquisarReservaLocalizacaoDTO (LocalizacaoDTO localizacao, Date dataInicial, Date dataFinal) {
        try {
            return dao.pesquisarReservaLocalizacaoDTO(localizacao, dataInicial, dataFinal);
        } catch (Exception e) {
            return null;
        }
    }
}