/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bo;

/**
 *
 * @author vinicio
 */
import com.core.GenericBO;
import com.dao.EnderecoDAO;
import com.dto.EnderecoDTO;

public class EnderecoBO extends GenericBO<EnderecoDTO>{
    
    private static final long serialVersionUID = 30011L;
    private EnderecoDAO dao = EnderecoDAO.getInstance();

    private volatile static EnderecoBO uniqueInstance;

    public static EnderecoBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (EnderecoBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new EnderecoBO();
                }
            }
        }
        return uniqueInstance;
    }
    
    public EnderecoBO() {
        super(EnderecoDTO.class);
    }

}
