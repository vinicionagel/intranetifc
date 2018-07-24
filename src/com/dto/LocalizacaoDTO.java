package com.dto;

import com.core.GenericDTOIdDescricao;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "localizacao")
public class LocalizacaoDTO extends GenericDTOIdDescricao {

    private static final long serialVersionUID = 13L;
    
    @ManyToOne
    @JoinColumn(name = "campus_codigo")
    private CampusDTO campusDTO;

    public LocalizacaoDTO(String descricao,CampusDTO campusDTO) {
        this.descricao = descricao;
        this.campusDTO = campusDTO;
    }

    public LocalizacaoDTO() {
    }

    public CampusDTO getCampusDTO() {
        return campusDTO;
    }

    public void setCampusDTO(CampusDTO campusDTO) {
        this.campusDTO = campusDTO;
    }
}