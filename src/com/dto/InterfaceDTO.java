package com.dto;

import com.core.GenericDTO;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "interface")
public class InterfaceDTO extends GenericDTO {

    private static final long serialVersionUID = 12L;
    
    @Column(name = "descricao_completa")
    private String descricaoCompleta;
    @Column(name = "descricao_curta")
    private String descricaoCurta;
    @Column(name = "posicao_menu")
    private int posicaoMenu;
    private String url;
    private String icone;
    private String cor;
    
    @ManyToOne()
    @JoinColumn(name = "interface_codigo")
    private InterfaceDTO interfaceRecursiva;
    
    @OneToMany(mappedBy="interfaceDTO", fetch = FetchType.LAZY, cascade= CascadeType.ALL)
    private List<PermissaoDTO> permissoes;

    public InterfaceDTO() {
        permissoes = new ArrayList<>();
    }
    
    public String getDescricaoCurta() {
        return descricaoCurta;
    }

    public void setDescricaoCurta(String descricaoCurta) {
        this.descricaoCurta = descricaoCurta;
    }

    public int getPosicaoMenu() {
        return posicaoMenu;
    }

    public void setPosicaoMenu(int posicaoMenu) {
        this.posicaoMenu = posicaoMenu;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getDescricaoCompleta() {
        return descricaoCompleta;
    }

    public void setDescricaoCompleta(String descricaoCompleta) {
        this.descricaoCompleta = descricaoCompleta;
    }

    public InterfaceDTO getInterfaceRecursiva() {
        return interfaceRecursiva;
    }

    public void setInterfaceRecursiva(InterfaceDTO interfaceRecursiva) {
        this.interfaceRecursiva = interfaceRecursiva;
    }

    public List<PermissaoDTO> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(List<PermissaoDTO> permissoes) {
        this.permissoes = permissoes;
    }
}