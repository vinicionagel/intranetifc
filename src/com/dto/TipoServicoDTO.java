package com.dto;

import com.core.GenericDTOIdDescricao;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tipo_servico")
public class TipoServicoDTO extends GenericDTOIdDescricao {
    
    private static final long serialVersionUID = 26L;
    
    @ManyToOne
    @JoinColumn(name = "setor_codigo")
    private SetorDTO setorDTO;

    public SetorDTO getSetorDTO() {
        return setorDTO;
    }

    public void setSetorDTO(SetorDTO setorDTO) {
        this.setorDTO = setorDTO;
    }    
}