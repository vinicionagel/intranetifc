package com.auxiliar;

import com.dto.CampusDTO;
import com.dto.UsuarioDTO;

public class Contexto {
    
    public UsuarioDTO usuarioLogado;
    public CampusDTO campusAtual;

    public UsuarioDTO getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(UsuarioDTO usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    public CampusDTO getCampusAtual() {
        return campusAtual;
    }

    public void setCampusAtual(CampusDTO campusAtual) {
        this.campusAtual = campusAtual;
    }            

    @Override
    public String toString() {
        return "Whatever";
    }        
}