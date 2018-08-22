/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bean.filter.chamadoInfra;

import com.bean.ContextoBean;
import com.bo.ChamadoInfraBO;
import com.dto.ChamadoInfraDTO;
import com.dto.ChamadoInfraModel;
import com.dto.SetorDTO;
import com.dto.StatusDTO;
import static com.util.FacesUtil.getSessionAttribute;
import java.util.Date;

/**
 *
 * @author vinicio
 */
public class StatusGrafico implements FilterChamadoInfra{
    
    private final ChamadoInfraBO chamadoInfraBO = ChamadoInfraBO.getInstance();
    private FilterChamadoInfra proximo;
    
    public StatusGrafico(FilterChamadoInfra proximo) {
        this.proximo = proximo;
    }
    
    @Override
    public ChamadoInfraModel filtrando(String opcao) {
        if (getSessionAttribute("statusGrafico") != null){
            ChamadoInfraModel retorno = new ChamadoInfraModel();
            ChamadoInfraDTO chamadoInfra = new ChamadoInfraDTO();
            chamadoInfra.setStatusDTO((StatusDTO) getSessionAttribute("statusGrafico"));
            retorno.setWrappedData(chamadoInfraBO.pesquisarChamadosInfraPeloFiltro(chamadoInfra, 
                    (SetorDTO) getSessionAttribute("setorGrafico"), ContextoBean.getContexto(), 
                    (Date) getSessionAttribute("dataInicial"), (Date) getSessionAttribute("dataFinal")));
            return retorno;
        }
        return proximo.filtrando(opcao);
    }

    @Override
    public void setProximo(FilterChamadoInfra proximo) {
        this.proximo = proximo;
    }
}