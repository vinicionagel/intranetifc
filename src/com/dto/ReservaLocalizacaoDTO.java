package com.dto;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "reserva_localizacao")
@PrimaryKeyJoinColumn(name = "reserva_codigo")
@DiscriminatorValue("1")
public class ReservaLocalizacaoDTO extends ReservaDTO {
    
    private static final long serialVersionUID = 19L;
    
    @ManyToOne
    @JoinColumn(name = "localizacao_codigo")
    private LocalizacaoDTO localizacaoDTO;
        
    public LocalizacaoDTO getLocalizacaoDTO() {
        return localizacaoDTO;
    }

    public void setLocalizacaoDTO(LocalizacaoDTO localizacaoDTO) {
        this.localizacaoDTO = localizacaoDTO;
    }      
}