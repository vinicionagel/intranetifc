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
@Table(name = "patrimonio")
public class PatrimonioDTO extends GenericDTOIdDescricao {    
    
    private static final long serialVersionUID = 15L;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "data_aquisicao")
    private Date dataAquisicao;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "data_baixa")
    private Date dataBaixa;
    @Column(name = "nota_fiscal")
    private String notaFiscal;
    @Column(name = "numero_etiqueta")
    private String numEtiqueta;
    //RELACIONAMENTOS
    @ManyToOne
    @JoinColumn(name = "localizacao_codigo")
    protected LocalizacaoDTO localizacaoDTO;
    @ManyToOne
    @JoinColumn(name = "fabricante_codigo")
    protected FabricanteDTO fabricanteDTO;
    @ManyToOne
    @JoinColumn(name = "tipo_patrimonio_codigo")
    protected TipoPatrimonioDTO tipoPatrimonioDTO;
    @ManyToOne
    @JoinColumn(name = "setor_codigo")
    private SetorDTO setorDTO;

    public LocalizacaoDTO getLocalizacaoDTO() {
        return localizacaoDTO;
    }

    public void setLocalizacaoDTO(LocalizacaoDTO localizacao) {
        this.localizacaoDTO = localizacao;
    }

    public FabricanteDTO getFabricanteDTO() {
        return fabricanteDTO;
    }

    public void setFabricanteDTO(FabricanteDTO fabricante) {
        this.fabricanteDTO = fabricante;
    }

    public TipoPatrimonioDTO getTipoPatrimonioDTO() {
        return tipoPatrimonioDTO;
    }

    public void setTipoPatrimonioDTO(TipoPatrimonioDTO tipoPatrimonio) {
        this.tipoPatrimonioDTO = tipoPatrimonio;
    }

    public Date getDataAquisicao() {        
        return dataAquisicao == null ? null : (Date) dataAquisicao.clone();
    }

    public void setDataAquisicao(Date dataAquisicao) {        
        this.dataAquisicao = dataAquisicao == null ? null : (Date) dataAquisicao.clone();        
    }

    public Date getDataBaixa() {
        return dataBaixa == null ? null : (Date) dataBaixa.clone();
    }

    public void setDataBaixa(Date dataBaixa) {        
        this.dataBaixa = dataBaixa == null ? null : (Date) dataBaixa.clone();        
    }

    public String getNotaFiscal() {
        return notaFiscal;
    }

    public void setNotaFiscal(String notaFiscal) {
        this.notaFiscal = notaFiscal;
    }

    public String getNumEtiqueta() {
        return numEtiqueta;
    }

    public void setNumEtiqueta(String etiqueta) {
        this.numEtiqueta = etiqueta;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PatrimonioDTO [localizacao=");
        builder.append(localizacaoDTO);
        builder.append(", fabricante=");
        builder.append(fabricanteDTO);
        builder.append(", tipoPatrimonio=");
        builder.append(tipoPatrimonioDTO);
        builder.append(", dataAquisicao=");
        builder.append(dataAquisicao);
        builder.append(", dataBaixa=");
        builder.append(dataBaixa);
        builder.append(", notaFiscal=");
        builder.append(notaFiscal);
        builder.append(", numEtiqueta=");
        builder.append(numEtiqueta);
        builder.append(", codigo=");
        builder.append(codigo);
        builder.append(", descricao=");
        builder.append(descricao);
        builder.append("]");
        return builder.toString();
    }

    public SetorDTO getSetorDTO() {
        return setorDTO;
    }

    public void setSetorDTO(SetorDTO setorDTO) {
        this.setorDTO = setorDTO;
    }
}