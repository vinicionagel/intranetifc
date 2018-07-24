package com.dto;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "chamado_servico")
@PrimaryKeyJoinColumn(name="chamado_codigo" ,referencedColumnName="codigo")
@DiscriminatorValue("2")
public class ChamadoServicoDTO extends ChamadoDTO {
    
    private static final long serialVersionUID = 7L;
    
    @ManyToOne
    @JoinColumn(name = "servico_codigo")
    private ServicoDTO servicoDTO;
        
    public ServicoDTO getServicoDTO() {
        return servicoDTO;
    }

    public void setServicoDTO(ServicoDTO servicoDTO) {
        this.servicoDTO = servicoDTO;
    }        
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());       
        return builder.toString();
    }
}