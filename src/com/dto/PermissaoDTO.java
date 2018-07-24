package com.dto;

import com.core.GenericDTOIdDescricao;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name = "permissao")
public class PermissaoDTO extends GenericDTOIdDescricao {    
    
    private static final long serialVersionUID = 16L;

    public PermissaoDTO() {
    }

    public PermissaoDTO(TipoPermissaoDTO tipoPermissaoDTO, InterfaceDTO interfaceDTO, Long codigo, String descricao) {
        this.tipoPermissaoDTO = tipoPermissaoDTO;
        this.interfaceDTO = interfaceDTO;
        this.codigo = codigo;
        this.descricao = descricao;
    }
    
    @ManyToOne()
    @JoinColumn(name = "tipo_permissao_codigo")
    private TipoPermissaoDTO tipoPermissaoDTO;
    
    @ManyToOne()
    @JoinColumn(name = "interface_codigo",
    insertable = true, updatable = true)
    private InterfaceDTO interfaceDTO;    
    
    public TipoPermissaoDTO getTipoPermissaoDTO() {
        return tipoPermissaoDTO;
    }

    public void setTipoPermissaoDTO(TipoPermissaoDTO tipoPermissaoDTO) {
        this.tipoPermissaoDTO = tipoPermissaoDTO;
    }

    public InterfaceDTO getInterfaceDTO() {
        return interfaceDTO;
    }

    public void setInterfaceDTO(InterfaceDTO interfaceDTO) {
        this.interfaceDTO = interfaceDTO;
    }
}