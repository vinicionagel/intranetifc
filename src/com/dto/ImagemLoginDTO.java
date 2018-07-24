
package com.dto;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "imagem_login")
@PrimaryKeyJoinColumn(name = "publicidade_codigo")
@DiscriminatorValue("1")
public class ImagemLoginDTO extends PublicidadeDTO{
    
    private static final long serialVersionUID = 11L;
    
    @Column(name="pode_ser_removido")
    private boolean podeSerRemovida;
    @Column(name = "link_externo")
    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
    
    public boolean isPodeSerRemovida() {
        return podeSerRemovida;
    }

    public void setPodeSerRemovida(boolean podeSerRemovida) {
        this.podeSerRemovida = podeSerRemovida;
    }    
}