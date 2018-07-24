
package com.dto;

import com.core.GenericDTOIdDescricao;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name = "municipio")
public class MunicipioDTO extends GenericDTOIdDescricao {

    private static final long serialVersionUID = 2009L;
    
    @ManyToOne
    @JoinColumn(name = "unidade_federativa_codigo")
    private UnidadeFederativaDTO UnidadeFederativaDTO;

    public UnidadeFederativaDTO getUnidadeFederativaDTO() {
        return UnidadeFederativaDTO;
    }

    public void setUnidadeFederativaDTO(UnidadeFederativaDTO UnidadeFederativaDTO) {
        this.UnidadeFederativaDTO = UnidadeFederativaDTO;
    }

}