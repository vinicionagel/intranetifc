package com.bo;

import com.core.GenericBO;
import com.dao.StatusDAO;
import com.dto.StatusDTO;
import java.util.List;

public class StatusBO extends GenericBO<StatusDTO>{

    private static final long serialVersionUID = 121L;
    private StatusDAO dao = StatusDAO.getInstance();

    private volatile static StatusBO uniqueInstance;

    public static StatusBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (StatusBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new StatusBO();
                }
            }
        }
        return uniqueInstance;
    }

    
    public StatusBO() {
        super(StatusDTO.class);
    }
    
    public StatusDTO pesquisarDescricao(String statusDTO)  {
        try {            
            return dao.pesquisarDescricao(statusDTO);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<StatusDTO> pesquisarNome(StatusDTO statusDTO) {
        try {
            return dao.pesquisarNome(statusDTO);
        } catch (Exception e) {
            return null;
        }
    }
}