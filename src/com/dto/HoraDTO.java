package com.dto;

import com.core.GenericDTOIdDescricao;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name = "hora")
public class HoraDTO extends GenericDTOIdDescricao {
    
    private static final long serialVersionUID = 10L;
    
    @Temporal(javax.persistence.TemporalType.DATE) 
    @Column(name="data_trabalho")
    private Date dataTrabalho;
    private Float tempo;
    @ManyToOne
    @JoinColumn(name = "chamado_codigo")
    private ChamadoDTO chamadoDTO;
    @ManyToOne
    @JoinColumn(name = "usuario_codigo")
    private UsuarioDTO usuarioDTO;

    public Date getDataTrabalho() {
        return dataTrabalho == null ? null : (Date) dataTrabalho.clone();
    }

    public void setDataTrabalho(Date data) {
        this.dataTrabalho = data == null ? null : (Date) data.clone();
    }

    public Float getTempo() {
        return tempo;
    }

    public void setTempo(Float tempo) {
        this.tempo = tempo;
    }

    public ChamadoDTO getChamadoDTO() {
        return chamadoDTO;
    }

    public void setChamadoDTO(ChamadoDTO chamadoDTO) {
        this.chamadoDTO = chamadoDTO;
    }

    public UsuarioDTO getUsuarioDTO() {
        return usuarioDTO;
    }

    public void setUsuarioDTO(UsuarioDTO usuarioDTO) {
        this.usuarioDTO = usuarioDTO;
    }   
}