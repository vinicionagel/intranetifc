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
public class MeusChamados implements FilterChamadoInfra{
    
    private final ChamadoInfraBO chamadoInfraBO = ChamadoInfraBO.getInstance();
    private FilterChamadoInfra proximo;

    public MeusChamados(FilterChamadoInfra proximo) {
        this.proximo = proximo;
    }
    
    @Override
    public ChamadoInfraModel filtrando(String opcao) {
        if (opcao.equals("meusChamados")){
            ChamadoInfraModel retorno = new ChamadoInfraModel();
            retorno.setWrappedData(chamadoInfraBO.pesquisarMeusChamadosInfra(ContextoBean.getContexto()));
            return retorno;
        }
        return proximo.filtrando(opcao);
    }

    @Override
    public void setProximo(FilterChamadoInfra proximo) {
        this.proximo = proximo;
    }
    
}
