package com.dto;

import com.core.GenericDTOIdDescricao;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name = "reserva")
@Inheritance(strategy= InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_reserva",discriminatorType=DiscriminatorType.STRING)
public class ReservaDTO extends GenericDTOIdDescricao{
    
    private static final long serialVersionUID = 18L;
    
    @Column(name = "data_hora_inicial")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataInicial;
    @Column(name = "data_hora_final")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataFinal;
    
    @ManyToOne
    @JoinColumn(name = "usuario_codigo_autor")
    private UsuarioDTO usuarioAutorDTO;
            
    public UsuarioDTO getUsuarioAutorDTO() {
        return usuarioAutorDTO;
    }

    public void setUsuarioAutorDTO(UsuarioDTO usuarioAutorDTO) {
        this.usuarioAutorDTO = usuarioAutorDTO;
    }

    public Date getDataInicial() {
        return dataInicial == null ? null : (Date) dataInicial.clone();
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial == null ? null : (Date) dataInicial.clone();
    }

    public Date getDataFinal() {
        return dataFinal == null ? null : (Date) dataFinal.clone();
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal == null ? null : (Date) dataFinal.clone();
    }
}