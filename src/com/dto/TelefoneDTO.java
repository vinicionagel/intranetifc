package com.dto;

import com.core.GenericDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "telefone")
public class TelefoneDTO extends GenericDTO{
    private static final long serialVersionUID = 2008L;   
    
    @ManyToOne
    @JoinColumn(name = "tipo_telefone_codigo")
    private TipoTelefoneDTO tipoTelefoneDTO;
    
    @Column(name = "codigo_area")
    private int codigoArea;

    private int numero;
    private int ramal;
    
    @ManyToOne
    @JoinColumn(name = "aluno_codigo")
    private AlunoDTO alunoDTO;

    public TipoTelefoneDTO getTipoTelefoneDTO() {
        return tipoTelefoneDTO;
    }

    public void setTipoTelefoneDTO(TipoTelefoneDTO tipoTelefoneDTO) {
        this.tipoTelefoneDTO = tipoTelefoneDTO;
    }

    public int getCodigoArea() {
        return codigoArea;
    }

    public void setCodigoArea(int codigoArea) {
        this.codigoArea = codigoArea;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getRamal() {
        return ramal;
    }

    public void setRamal(int ramal) {
        this.ramal = ramal;
    }

    public AlunoDTO getAlunoDTO() {
        return alunoDTO;
    }

    public void setAlunoDTO(AlunoDTO alunoDTO) {
        this.alunoDTO = alunoDTO;
    }
}
