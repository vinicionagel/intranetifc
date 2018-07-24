package com.bo;

import com.core.GenericBO;
import com.dao.PrioridadeDAO;
import com.dto.PrioridadeDTO;

public class PrioridadeBO extends GenericBO<PrioridadeDTO>{
    
    private static final long serialVersionUID = 8002L;
    private PrioridadeDAO dao = PrioridadeDAO.getInstance();

    private volatile static PrioridadeBO uniqueInstance;

    public static PrioridadeBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (PrioridadeBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new PrioridadeBO();
                }
            }
        }
        return uniqueInstance;
    }

    public PrioridadeBO() {
        super(PrioridadeDTO.class);
    }
}
