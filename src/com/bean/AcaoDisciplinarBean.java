package com.bean;

import com.bo.AcaoDisciplinarBO;
import com.bo.TipoOcorrenciaBO;
import com.core.GenericBean;
import com.dto.AcaoDisciplinarDTO;
import com.dto.TipoOcorrenciaDTO;
import com.util.ValidadorCampo;
import java.io.Serializable;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author vinicio
 */
@Named(value = "acaoDisciplinarBean")
@ViewScoped
public class AcaoDisciplinarBean extends GenericBean<AcaoDisciplinarDTO> implements Serializable{
              
    private static final long serialVersionUID = 100032132145L;
    private AcaoDisciplinarDTO acaoDisciplinarDTO = new AcaoDisciplinarDTO();        
    
    public AcaoDisciplinarBean() {        
        tituloLayoutUnit = "Ação Disciplinar";
        objeto = new AcaoDisciplinarDTO();
        getCodigo();
        acaoDisciplinarDTO = new AcaoDisciplinarDTO(objeto.getTipoOcorrenciaDTO(), objeto.getDescricao());
    }

    @Override
    public String novo() throws Throwable {  
        dadosPesquisa();        
        return pesquisar();
    }

    @Override
    public List<AcaoDisciplinarDTO> dadosPesquisa(){
        setCampoPesquisar(campoPesquisar == null ? campoPesquisar = "" : getCampoPesquisar()); 
        return lista = getBO().pesquisarNome(getCampoPesquisar());
    }

    @Override
    public void validaCampo(List<String> erros, AcaoDisciplinarDTO acaoDisciplinar) {
        ValidadorCampo.validarCampoVazio(acaoDisciplinar.getDescricao(), "DESCRICAO", erros);
        ValidadorCampo.validarCampoVazio(acaoDisciplinar.getCor(), "COR", erros);
        ValidadorCampo.validarCampoNulo(acaoDisciplinar.getTipoOcorrenciaDTO() , "TIPO OCORRENCIA", erros);
    }

    @Override
    public boolean clausulaUnico() {
        System.out.println(objeto.getDescricao().equals(getAcaoDisciplinarOLD().getDescricao()));
        return !objeto.getDescricao().equals(getAcaoDisciplinarOLD().getDescricao()) 
                || !objeto.getTipoOcorrenciaDTO().equals(getAcaoDisciplinarOLD().getTipoOcorrenciaDTO());
    }       

    
    @Override
    public String incluirAlterar() {
        return "acaoDisciplinarIncluirAlterar";
    }

    @Override
    public String pesquisar() {
        return "acaoDisciplinarPesquisar";
    }
    
    @Override
    public AcaoDisciplinarBO getBO() {
        return AcaoDisciplinarBO.getInstance();
    }
   
    public List<TipoOcorrenciaDTO> getTipoOcorrencia() {
        return TipoOcorrenciaBO.getInstance().findAll();
    }
        
    public AcaoDisciplinarDTO getAcaoDisciplinarOLD() {
        return acaoDisciplinarDTO;
    }

    public void setServicoOLD(AcaoDisciplinarDTO acaoDisciplinarDTO) {
        this.acaoDisciplinarDTO = acaoDisciplinarDTO;
    }          
}