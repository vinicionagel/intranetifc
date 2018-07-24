package com.dto;

import com.core.GenericDTO;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name = "agendamento")
public class AgendamentoDTO extends GenericDTO {
    
    private static final long serialVersionUID = 2018L;
    
    @ManyToOne()
    @JoinColumn(name = "dia_semana_codigo")
    private DiaSemanaDTO diaSemanaDTO;
    
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date horario;

    public AgendamentoDTO(DiaSemanaDTO diaSemanaDTO, Date horario) {
        this.diaSemanaDTO = diaSemanaDTO;
        this.horario = horario;
    }

    public AgendamentoDTO() {
    }
    
    public DiaSemanaDTO getDiaSemanaDTO() {
        return diaSemanaDTO;
    }

    public void setDiaSemanaDTO(DiaSemanaDTO diaSemanaDTO) {
        this.diaSemanaDTO = diaSemanaDTO;
    }

    public Date getHorario() {
        return horario;
    }

    public void setHorario(Date horario) {
        this.horario = horario;
    }
    
}
