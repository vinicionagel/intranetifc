package com.bo;

import com.core.GenericBO;
import com.dao.AcaoDisciplinarDAO;
import com.dto.AcaoDisciplinarDTO;
import com.dto.TipoOcorrenciaDTO;
import java.util.List;

public class AcaoDisciplinarBO extends GenericBO<AcaoDisciplinarDTO>{
    
    private static final long serialVersionUID = 30002L;
    private AcaoDisciplinarDAO dao = AcaoDisciplinarDAO.getInstance();

    private volatile static AcaoDisciplinarBO uniqueInstance;

    public static AcaoDisciplinarBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (AcaoDisciplinarBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new AcaoDisciplinarBO();
                }
            }
        }
        return uniqueInstance;
    }

    public AcaoDisciplinarBO() {
        super(AcaoDisciplinarDTO.class);
    }
    
    public List<AcaoDisciplinarDTO> pesquisarAcaoDisciplinarPorTipoOcorrencia(TipoOcorrenciaDTO tipoOcorrencia) {
        return dao.pesquisarAcaoDisciplinarPorTipoOcorrencia(tipoOcorrencia);
    }
    
    public List<AcaoDisciplinarDTO> pesquisarNome(String acaoDisciplinarDTO) {
        return dao.pesquisarNome(acaoDisciplinarDTO);
    }
    
    @Override
    public boolean unique(AcaoDisciplinarDTO acaoDisciplinarDTO) {
        try {
            return dao.unique(acaoDisciplinarDTO);
        } catch (Throwable e) {
            return false;
        }
    }
}
