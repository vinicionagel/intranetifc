package com.dto;

import com.core.GenericDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name = "andamento_ocorrencia_editado")
public class AndamentoOcorrenciaEditadoDTO extends GenericDTO{
    private static final long serialVersionUID = 5012L;  
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "data_hora_ultima_edicao")
    private Date dataHoraUltimaEdicao;

    public Date getDataHoraUltimaEdicao() {
        return dataHoraUltimaEdicao;
    }

    public void setDataHoraUltimaEdicao(Date dataHoraUltimaEdicao) {
        this.dataHoraUltimaEdicao = dataHoraUltimaEdicao;
    }
}
