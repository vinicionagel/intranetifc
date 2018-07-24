package com.dto;

import com.core.GenericDTOIdDescricao;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "acao_disciplinar")
public class AcaoDisciplinarDTO extends GenericDTOIdDescricao{
    private static final long serialVersionUID = 2002L;
    
    @Column(name = "cor_html")
    private String cor;

    @ManyToOne
    @JoinColumn(name = "tipo_ocorrencia_codigo")
    private TipoOcorrenciaDTO tipoOcorrenciaDTO;
    
    public AcaoDisciplinarDTO(TipoOcorrenciaDTO tipoOcorrenciaDTO, String descricao) {
        this.tipoOcorrenciaDTO = tipoOcorrenciaDTO;
        this.descricao = descricao;
    }

    public AcaoDisciplinarDTO() {
        
    }
    
    public String getCor() {
        return cor;
    }
    
    public void setCor(String cor) {
        this.cor = cor;
    }
    
    public TipoOcorrenciaDTO getTipoOcorrenciaDTO() {
        return tipoOcorrenciaDTO;
    }

    public void setTipoOcorrenciaDTO(TipoOcorrenciaDTO tipoOcorrenciaDTO) {
        this.tipoOcorrenciaDTO = tipoOcorrenciaDTO;
    }
}
