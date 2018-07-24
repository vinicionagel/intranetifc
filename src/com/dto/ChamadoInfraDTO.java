package com.dto;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "chamado_infra")
@PrimaryKeyJoinColumn(name="chamado_codigo" ,referencedColumnName="codigo")
@DiscriminatorValue("1")
public class ChamadoInfraDTO extends ChamadoDTO{
    
    private static final long serialVersionUID = 6L;
    
    @ManyToOne
    @JoinColumn(name = "patrimonio_codigo")
    protected PatrimonioDTO patrimonioDTO;
    
    public PatrimonioDTO getPatrimonioDTO() {
        return patrimonioDTO;
    }

    public void setPatrimonioDTO(PatrimonioDTO patrimonioDTO) {
        this.patrimonioDTO = patrimonioDTO;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());       
        return builder.toString();
    }

}