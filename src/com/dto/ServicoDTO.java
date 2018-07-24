package com.dto;

import com.core.GenericDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "servico")
public class ServicoDTO extends GenericDTO{ 
    
    private static final long serialVersionUID = 21L;
    
    @Column(name = "descricao_completa")
    private String descricaoCompleta;
    @Column(name = "descricao_curta")
    private String descricaoCurta;

    @ManyToOne
    @JoinColumn(name = "tipo_servico_codigo")
    private TipoServicoDTO tipoServicoDTO;
    
    public String getDescricaoCompleta() {
        return descricaoCompleta;
    }

    public void setDescricaoCompleta(String descricaoCompleta) {
        this.descricaoCompleta = descricaoCompleta;
    }

    public String getDescricaoCurta() {
        return descricaoCurta;
    }

    public void setDescricaoCurta(String descricaoCurta) {
        this.descricaoCurta = descricaoCurta;
    }

    public TipoServicoDTO getTipoServicoDTO() {
        return tipoServicoDTO;
    }

    public void setTipoServicoDTO(TipoServicoDTO tipoServicoDTO) {
        this.tipoServicoDTO = tipoServicoDTO;
    }
}