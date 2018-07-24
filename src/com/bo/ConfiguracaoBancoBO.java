/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bo;

import com.core.GenericBO;
import com.dao.ConfiguracaoBancoDAO;
import com.dto.ConfiguracaoBancoDTO;

/**
 *
 * @author vinicio
 */
public class ConfiguracaoBancoBO extends GenericBO<ConfiguracaoBancoDTO>{
        
    private static final long serialVersionUID = 30016L;
    private ConfiguracaoBancoDAO dao = ConfiguracaoBancoDAO.getInstance();

    private volatile static ConfiguracaoBancoBO uniqueInstance;

    public static ConfiguracaoBancoBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (ConfiguracaoBancoBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new ConfiguracaoBancoBO();
                }
            }
        }
        return uniqueInstance;
    }
     /**
     * retorna as configuracoes de banco pelo seu determinado tipoDeConfiguracao
     * @param tipoConfiguracaoCodigo codigo do tipo da configuracao
     * @return ConfiguracaoBancoDTO
     */
    public ConfiguracaoBancoDTO pesquisarPorTipoConfiguracao(int tipoConfiguracaoCodigo) {
        return dao.pesquisarPorTipoConfiguracao(tipoConfiguracaoCodigo);
    }
}