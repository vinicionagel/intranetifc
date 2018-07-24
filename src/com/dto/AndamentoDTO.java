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
@Table(name = "andamento")
public class AndamentoDTO extends GenericDTOIdDescricao{

    private static final long serialVersionUID = 1L;
    
    @Column(name = "data_hora")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataHora;
    private String log;
    //relacionamentos
    @ManyToOne
    @JoinColumn(name = "chamado_codigo")
    private ChamadoDTO chamadoDTO;
    @ManyToOne
    @JoinColumn(name = "usuario_codigo")
    private UsuarioDTO usuarioDTO;

    public AndamentoDTO() {
    }
    
    public AndamentoDTO(Date dataHora, String log, ChamadoDTO chamadoDTO, UsuarioDTO usuarioDTO) {        
        this.dataHora = (Date) dataHora.clone();
        this.log = log;
        this.chamadoDTO = chamadoDTO;
        this.usuarioDTO = usuarioDTO;                                        
    }       
    
    public AndamentoDTO(ChamadoDTO chamadoDTO, UsuarioDTO usuarioDTO, Date dataHora) {        
        this.dataHora = (Date) dataHora.clone();
        this.chamadoDTO = chamadoDTO;
        this.usuarioDTO = usuarioDTO;                                        
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

    public Date getDataHora() {
        return dataHora == null ? null : (Date) dataHora.clone();
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora == null ? null : (Date) dataHora.clone();
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AndamentoDTO [chamadoDTO=");
        builder.append(getChamadoDTO());
        builder.append(", usuarioDTO=");
        builder.append(getUsuarioDTO());
        builder.append(", dataHora=");
        builder.append(getDataHora());
        builder.append(", codigo=");
        builder.append(codigo);
        builder.append(", log=");
        builder.append(log);
        builder.append(", descricao=");
        builder.append(descricao);
        builder.append("]");
        return builder.toString();
    }   
}