/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bean.filter.chamadoInfra;

import com.bean.ContextoBean;
import com.bo.ChamadoInfraBO;
import com.dto.ChamadoInfraModel;

/**
 *
 * @author vinicio
 */
public class ChamadoPorTitutlo implements FilterChamadoInfra{
    
    private final ChamadoInfraBO chamadoInfraBO = ChamadoInfraBO.getInstance();
    private FilterChamadoInfra proximo;
    private final String camposPesquisa;
    
    public ChamadoPorTitutlo(FilterChamadoInfra proximo, String camposPesquisa) {
        this.proximo = proximo;
        this.camposPesquisa = camposPesquisa;
    }
    
    @Override
    public ChamadoInfraModel filtrando(String opcao) {
        if (opcao.equals("chamadosObservados")){
            ChamadoInfraModel retorno = new ChamadoInfraModel();
            retorno.setWrappedData(chamadoInfraBO.pesquisarChamadosInfraPorTitulo(camposPesquisa, ContextoBean.getContexto()));
            return retorno;
        }
        return proximo.filtrando(opcao);
    }

    @Override
    public void setProximo(FilterChamadoInfra proximo) {
        this.proximo = proximo;
    }
}