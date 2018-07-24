package com.bo;

import com.core.GenericBO;
import com.dao.ReservaPatrimonioDAO;
import com.dto.PatrimonioDTO;
import com.dto.ReservaPatrimonioDTO;
import java.util.Date;
import java.util.List;

public class ReservaPatrimonioBO extends GenericBO<ReservaPatrimonioDTO>{
    
    private static final long serialVersionUID = 118L;
    private ReservaPatrimonioDAO dao = ReservaPatrimonioDAO.getInstance();
    private volatile static ReservaPatrimonioBO uniqueInstance;
    
    public ReservaPatrimonioBO(){
        super(ReservaPatrimonioDTO.class);
    }
    
    public static ReservaPatrimonioBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (ReservaPatrimonioBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new ReservaPatrimonioBO();
                }
            }
        }
        return uniqueInstance;
    }
    /**
     * Método usando em unique(validação) e para carregamento por semana (lazy loading)
     * @param patrimonioDTO
     * @param dataInicial
     * @param dataFinal
     * @return 
     */
    public List<ReservaPatrimonioDTO> uniqueReservaPatrimonio(PatrimonioDTO patrimonioDTO, Date dataInicial, Date dataFinal)  {
        try {
            return dao.uniqueReservaPatrimonio(patrimonioDTO, dataInicial, dataFinal);
        } catch (Exception e) {
            return null;
        }
    }
    
    public ReservaPatrimonioDTO pesquisarReservaPatrimonioDTO (PatrimonioDTO patrimonio, Date dataInicial, Date dataFinal) {
        try {
            return dao.pesquisarReservaPatrimonioDTO(patrimonio, dataInicial, dataFinal);
        } catch (Exception e) {
            return null;
        }
    }
}