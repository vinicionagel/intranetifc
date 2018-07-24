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
@Table(name = "andamento_ocorrencia")
public class AndamentoOcorrenciaDTO extends GenericDTOIdDescricao{
    private static final long serialVersionUID = 2012L;   
    
    private String log;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "data_hora_ocorrencia")
    private Date dataOcorrencia;
       
    @ManyToOne()
    @JoinColumn(name = "ocorrencia_codigo")
    private OcorrenciaDTO ocorrenciaDTO;
    
    @ManyToOne
    @JoinColumn(name = "usuario_codigo")
    private UsuarioDTO usuarioDTO;
        
    public AndamentoOcorrenciaDTO(Date dataHora, String log, OcorrenciaDTO ocorrenciaDTO, UsuarioDTO usuarioDTO) {        
        this.dataOcorrencia = (Date) dataHora.clone();
        this.log = log;
        this.ocorrenciaDTO = ocorrenciaDTO;
        this.usuarioDTO = usuarioDTO;                                        
    }      
    
     public AndamentoOcorrenciaDTO (OcorrenciaDTO ocorrenciaDTO, UsuarioDTO usuarioDTO, Date dataOcorrencia){
        this.ocorrenciaDTO = ocorrenciaDTO;
        this.usuarioDTO = usuarioDTO;
        this.dataOcorrencia = dataOcorrencia;
     }

    
    public AndamentoOcorrenciaDTO() {                               
    }       
        
    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Date getDataOcorrencia() {
        return dataOcorrencia;
    }

    public void setDataOcorrencia(Date dataOcorrencia) {
        this.dataOcorrencia = dataOcorrencia;
    }

    public OcorrenciaDTO getOcorrenciaDTO() {
        return ocorrenciaDTO;
    }

    public void setOcorrenciaDTO(OcorrenciaDTO ocorrenciaDTO) {
        this.ocorrenciaDTO = ocorrenciaDTO;
    }

    public UsuarioDTO getUsuarioDTO() {
        return usuarioDTO;
    }

    public void setUsuarioDTO(UsuarioDTO usuarioDTO) {
        this.usuarioDTO = usuarioDTO;
    }
}
