package com.bo;

import com.core.GenericBO;
import com.dao.DiaSemanaDAO;
import com.dto.DiaSemanaDTO;

public class DiaSemanaBO extends GenericBO<DiaSemanaDTO> {

    private static final long serialVersionUID = 30017L;
    private DiaSemanaDAO dao = DiaSemanaDAO.getInstance();

    private volatile static DiaSemanaBO uniqueInstance;

    public static DiaSemanaBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (DiaSemanaBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new DiaSemanaBO();
                }
            }
        }
        return uniqueInstance;
    }

    public DiaSemanaBO() {
        super(DiaSemanaDTO.class);
    }
}
