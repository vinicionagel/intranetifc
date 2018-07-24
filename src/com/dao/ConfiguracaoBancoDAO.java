/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.core.GenericDAO;
import com.dto.ConfiguracaoBancoDTO;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

/**
 *
 * @author vinicio
 */
public class ConfiguracaoBancoDAO extends GenericDAO<ConfiguracaoBancoDTO> {

    private static final long serialVersionUID = 20016L;
    static final Logger logger = Logger.getLogger(ConfiguracaoBancoDAO.class.getName());
    private volatile static ConfiguracaoBancoDAO uniqueInstance;

    public ConfiguracaoBancoDAO() {
        super(ConfiguracaoBancoDAO.class);
    }

    public static ConfiguracaoBancoDAO getInstance() {
        if (uniqueInstance == null) {
            synchronized (ConfiguracaoBancoDAO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new ConfiguracaoBancoDAO();
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
        EntityManager em = emf.createEntityManager();
        try {
            String sql = "FROM ConfiguracaoBancoDTO cB WHERE cB.tipoConfiguracaoDTO.codigo = :codigo";
            return (ConfiguracaoBancoDTO) em.createQuery(sql.toString()).setParameter("codigo", tipoConfiguracaoCodigo).getSingleResult();
        } catch (Exception e){
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
}
