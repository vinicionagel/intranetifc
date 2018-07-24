package com.dto;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "banner")
@PrimaryKeyJoinColumn(name = "publicidade_codigo")
@DiscriminatorValue("2")
public class BannerDTO extends PublicidadeDTO{
    
    private static final long serialVersionUID = 3L;
    
    private String mensagem;
    @Column(name = "descricao", length = 255)
    private String descricao;

    @ManyToMany(fetch = FetchType.LAZY,cascade= CascadeType.ALL)
    @JoinTable(name = "banner_campus",
    joinColumns = {
        @JoinColumn(name = "publicidade_codigo")},
    inverseJoinColumns = {
        @JoinColumn(name = "campus_codigo")})
    private List<CampusDTO> campusDTOs;

    public BannerDTO() {
        campusDTOs = new ArrayList<>();
    }
    
    public List<CampusDTO> getCampusDTOs() {
        return campusDTOs;
    }

    public void setCampusDTOs(List<CampusDTO> campusDTOs) {
        this.campusDTOs = campusDTOs;
    } 
    
    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }        
}