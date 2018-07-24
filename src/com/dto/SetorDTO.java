package com.dto;

import com.core.GenericDTOIdDescricao;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "setor")
public class SetorDTO extends GenericDTOIdDescricao{

    private static final long serialVersionUID = 22L;
    
    @ManyToOne
    @JoinColumn(name = "campus_codigo")
    private CampusDTO campusDTO;
    
    @Column(name = "interface_ativo")
    private String interfaceAtivo;
    
    @Transient
    private HashSet<String> concat = new HashSet<>();
    
    public SetorDTO() {
    }

    public SetorDTO(CampusDTO campusDTO, String interfaceAtivo, String descricao) {
        this.campusDTO = campusDTO;
        this.interfaceAtivo = interfaceAtivo;
        this.descricao = descricao;
    }
    
    public String getInterfaceAtivo() {
        concat.addAll(interfaceAtivo != null ? Arrays.asList(interfaceAtivo.split(",")) : new ArrayList<String>());
        return interfaceAtivo;
    }

    public void setInterfaceAtivo(String interfaceAtivo) {
        this.interfaceAtivo = interfaceAtivo;
    }
    
    public CampusDTO getCampusDTO() {
        return campusDTO;
    }

    public void setCampusDTO(CampusDTO campusDTO) {
        this.campusDTO = campusDTO;
    }

    public HashSet<String> getConcat() {
        return concat;
    }

    public void setConcat(HashSet<String> concat) {
        this.concat = concat;
    }
    
}