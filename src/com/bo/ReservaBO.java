package com.bo;

import com.core.GenericBO;
import com.dao.ReservaDAO;
import com.dto.ReservaDTO;
import com.dto.UsuarioDTO;

public class ReservaBO extends GenericBO<ReservaDTO>{
    
    private static final long serialVersionUID = 116L;
    protected ReservaDAO dao = ReservaDAO.getInstance();
    private volatile static ReservaBO uniqueInstance;
 
    public static ReservaBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (ReservaBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new ReservaBO();
                }
            }
        }
        return uniqueInstance;
    }
    
    public ReservaBO(){
        super(ReservaDTO.class);
    }
    
    public ReservaDTO verificaUsuario (UsuarioDTO usuarioDTO, ReservaDTO reservaDTO) {
        try {
            return dao.verificaUsuario(usuarioDTO, reservaDTO);
        } catch (Exception e) {
            return null;
        }
    }
}
