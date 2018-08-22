/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bean.filter.chamadoInfra;

import com.dto.ChamadoInfraModel;


/**
 *
 * @author vinicio
 */
public interface FilterChamadoInfra {
    
    public ChamadoInfraModel filtrando(String opcao);
    
    public void setProximo(FilterChamadoInfra proximo);
}
