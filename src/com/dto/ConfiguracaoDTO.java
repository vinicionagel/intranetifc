package com.dto;

import com.core.GenericDTOIdDescricao;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "configuracao")
public class ConfiguracaoDTO extends GenericDTOIdDescricao{
    
    private static final long serialVersionUID = 100032L;
    
    @ManyToOne
    @JoinColumn(name="tipo_configuracao_codigo")
    private TipoConfiguracaoDTO tipoConfiguracaoDTO;

    public TipoConfiguracaoDTO getTipoConfiguracaoDTO() {
        return tipoConfiguracaoDTO;
    }

    public void setTipoConfiguracaoDTO(TipoConfiguracaoDTO tipoConfiguracaoDTO) {
        this.tipoConfiguracaoDTO = tipoConfiguracaoDTO;
    }        
}