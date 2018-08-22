/*
 * To change this license header, choose License Headers in Project Properties.
 * To chcom.bean.filter.chamadoInfraange this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bean.filter.chamadoInfra;

import com.bean.ContextoBean;
import com.bo.ChamadoInfraBO;
import com.bo.PatrimonioBO;
import com.bo.SetorBO;
import com.dto.ChamadoInfraDTO;
import com.dto.ChamadoInfraModel;

/**
 *
 * @author vinicio
 */
public class Filtrando implements FilterChamadoInfra {
    
    private final PatrimonioBO patrimonioBO = PatrimonioBO.getInstance();
    private final SetorBO setorBO = SetorBO.getInstance();
    private final ChamadoInfraBO chamadoInfraBO = ChamadoInfraBO.getInstance();
    private long codigoPatrimonio;
    private long codigoSetor;
    private FilterChamadoInfra proximo;

    public Filtrando(FilterChamadoInfra proximo) {
        this.proximo = proximo;
    }
    
    @Override
    public ChamadoInfraModel filtrando(String opcao) {
        if (opcao.equals("filtrando")){
            ChamadoInfraModel retorno = new ChamadoInfraModel();
            ChamadoInfraDTO chamadoInfraFiltro = new ChamadoInfraDTO();
            chamadoInfraFiltro.setPatrimonioDTO(patrimonioBO.findById(codigoPatrimonio));
            retorno.setWrappedData(chamadoInfraBO.pesquisarChamadosInfraPeloFiltro(
                    chamadoInfraFiltro, 
                    setorBO.findById(codigoSetor), 
                    ContextoBean.getContexto(), null, null));
            return retorno;
        } 
        return proximo.filtrando(opcao);
    }

    @Override
    public void setProximo(FilterChamadoInfra proximo) {
        this.proximo = proximo;
    }
    
}
