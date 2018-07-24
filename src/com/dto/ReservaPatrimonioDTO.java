package com.dto;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "reserva_patrimonio")
@PrimaryKeyJoinColumn(name = "reserva_codigo")
@DiscriminatorValue("2")
public class ReservaPatrimonioDTO extends ReservaDTO {
    
    private static final long serialVersionUID = 20L;
    
    @ManyToOne
    @JoinColumn(name = "patrimonio_codigo")
    private PatrimonioDTO patrimonioDTO;
    
    public PatrimonioDTO getPatrimonioDTO() {
        return patrimonioDTO;
    }

    public void setPatrimonioDTO(PatrimonioDTO patrimonioDTO) {
        this.patrimonioDTO = patrimonioDTO;
    }        
}