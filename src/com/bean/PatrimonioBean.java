package com.bean;

import com.bo.FabricanteBO;
import com.bo.LocalizacaoBO;
import com.bo.PatrimonioBO;
import com.bo.SetorBO;
import com.bo.TipoPatrimonioBO;
import com.core.GenericBean;
import com.core.IValidaContexto;
import com.dto.CampusDTO;
import com.dto.FabricanteDTO;
import com.dto.LocalizacaoDTO;
import com.dto.PatrimonioDTO;
import com.dto.SetorDTO;
import com.dto.TipoPatrimonioDTO;
import com.util.FacesUtil;
import static com.util.FacesUtil.*;
import com.util.ValidadorCampo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "patrimonioBean")
@ViewScoped
public class PatrimonioBean extends GenericBean<PatrimonioDTO> implements Serializable, IValidaContexto{
                    
    private static final long serialVersionUID = 100016L;
    private PatrimonioDTO patrimonioDTOPesquisa = new PatrimonioDTO();
    private PatrimonioDTO patrimonioOld = new PatrimonioDTO();    
    
    /** Variáreis referêntes ao cadastro multiplo */
    private List<PatrimonioDTO> patrimoniosInclusao = new ArrayList<>();    
    private boolean cadastroMultiplo = false;
    private Map<String, Boolean> mapCamposDinamicos = new HashMap<>();     
    private HashSet<String> camposSelecionadosCadastroMultiplo = new HashSet<>();            
    private int quantidadeCamposDinamicos = 0;    

    public PatrimonioBean() {
        tituloLayoutUnit = "Patrimônio";
        getCodigo();
        validaContexto();
        if (objeto == null){
            objeto = new PatrimonioDTO();
        } else {
            limparVariaveisCadastroMultiplo(); 
            patrimonioOld.setDescricao(objeto.getDescricao());
            patrimonioOld.setLocalizacaoDTO(objeto.getLocalizacaoDTO());  
        }
    }        
    
    @Override
    public String incluirAlterar() {        
        return "patrimonioIncluirAlterar";
    }

    @Override
    public String pesquisar() {
        return "patrimonioPesquisar";
    }

    @Override
    public String include() {                    
        limparVariaveisCadastroMultiplo();
        patrimonioDTOPesquisa = new PatrimonioDTO();                                  
        objeto = new PatrimonioDTO();
        return super.include();
    }
    
    private void limparVariaveisCadastroMultiplo(){
        quantidadeCamposDinamicos = 0;
        cadastroMultiplo = false;
        patrimoniosInclusao = new ArrayList<>();
        camposSelecionadosCadastroMultiplo = new HashSet<>();        
        mapCamposDinamicos = new HashMap<>();     
    }
    
    @Override
    public String novo() {
        dadosPesquisa();
        return pesquisar();
    }

    @Override
    public boolean clausulaUnico() {
        return !objeto.getDescricao().equals(getPatrimonioOld().getDescricao()) 
                || !objeto.getLocalizacaoDTO().getDescricao().equals(getPatrimonioOld().getLocalizacaoDTO().getDescricao());
    }
       
    @Override
    public String save() throws Throwable{
        try {
            if(!isCadastroMultiplo()){
               return super.save();                        
            } else {                  
                while(patrimoniosInclusao.size() > 0){
                    PatrimonioDTO p = patrimoniosInclusao.get(0);
                    completarPatrimonio(p);
                    if(!validarSalvar(p)){
                        return null;
                    }
                    patrimoniosInclusao.remove(p);                    
                }                                                              
                inseridoSucesso();
                return novo();
            }
        } catch (Exception e) {
            adicionarMensagemErro(e);
            return null;
        }
    }

    private boolean validarSalvar(PatrimonioDTO p) {
        if(super.validaCampoUnico(p)) {            
            return false;
        }                    
        if(super.validacao(p)){
            return false;
        }        
        getBO().save(p);
        return true;
    }                    
    
    private void completarPatrimonio(PatrimonioDTO patrimonio){        
        patrimonio.setLocalizacaoDTO(objeto.getLocalizacaoDTO());
        patrimonio.setSetorDTO(objeto.getSetorDTO());        
        if(!camposSelecionadosCadastroMultiplo.contains("tipoPatrimonio")){
            patrimonio.setTipoPatrimonioDTO(objeto.getTipoPatrimonioDTO());        
        }
        if(!camposSelecionadosCadastroMultiplo.contains("fabricante")){
            patrimonio.setFabricanteDTO(objeto.getFabricanteDTO());
        }
        if(!camposSelecionadosCadastroMultiplo.contains("aquisicao")){
            patrimonio.setDataAquisicao(objeto.getDataAquisicao());        
        }
        if(!camposSelecionadosCadastroMultiplo.contains("notaFiscal")){
            patrimonio.setNotaFiscal(objeto.getNotaFiscal());        
        }                
    }
                            
    public void removerPatrimonio(PatrimonioDTO p){               
        if(patrimoniosInclusao.size() == 1){
            mapCamposDinamicos = new HashMap<>();
            setQuantidadeCamposDinamicos(0);
        }
        patrimoniosInclusao.remove(p);                
    }
        
    public void adicionarPatrimonio(){         
        if(getQuantidadeCamposDinamicos() == 0){            
            setQuantidadeCamposDinamicos(gerarListaCamposSelecionados().size()+3);
        }
        getPatrimoniosInclusao().add(new PatrimonioDTO());                     
    }
    //FIX
    private HashSet<String> gerarListaCamposSelecionados(){        
        HashSet<String> campos = new HashSet<>();
        for(int i = 0; i < mapCamposDinamicos.entrySet().toArray().length; i++){
            String valorMap = mapCamposDinamicos.entrySet().toArray()[i].toString();
            if(valorMap.substring(valorMap.indexOf("=")+1).equals("true")){
                campos.add(valorMap.substring(0, valorMap.indexOf("=")));
            }
        }        
        return camposSelecionadosCadastroMultiplo = campos;
    }
      
    @Override
    public void validaCampo(List<String> erros, PatrimonioDTO patrimonio) {        
        ValidadorCampo.validarCampoVazio(patrimonio.getDescricao(), "DESCRICAO", erros);        
        ValidadorCampo.validarCampoNulo(patrimonio.getLocalizacaoDTO(), "LOCALIZAÇÃO", erros);
        ValidadorCampo.validarCampoNulo(patrimonio.getSetorDTO(), "SETOR", erros);                                
        ValidadorCampo.validarCampoNulo(patrimonio.getFabricanteDTO(), "FABRICANTE", erros);
        ValidadorCampo.validarCampoNulo(patrimonio.getTipoPatrimonioDTO(), "TIPO PATRIMONIO", erros);        
        ValidadorCampo.validarCampoVazio(patrimonio.getNumEtiqueta(), "NÚMERO ETIQUETA", erros);                                                  
    }

    @Override
    public PatrimonioBO getBO() {
        return PatrimonioBO.getInstance();
    }    
    
    public void setQuantidadeCamposDinamicos(int quantidadeCamposDinamicos){                        
        this.quantidadeCamposDinamicos = quantidadeCamposDinamicos;
    }  
    
    public int getQuantidadeCamposDinamicos(){                        
        return quantidadeCamposDinamicos;        
    }        
    
    @Override
    public List<PatrimonioDTO> dadosPesquisa(){ 
        setCampoPesquisar(campoPesquisar == null ? campoPesquisar = "" : getCampoPesquisar());
        patrimonioDTOPesquisa.setDescricao(campoPesquisar);        
        return lista = getBO().pesquisarPatrimoniosPeloFiltro(patrimonioDTOPesquisa, ContextoBean.getContexto().getCampusAtual());
    }

    public PatrimonioDTO getPatrimonioDTOPesquisa() {
        return patrimonioDTOPesquisa;
    }

    public void setPatrimonioDTOPesquisa(PatrimonioDTO patrimonioDTOPesquisa) {
        this.patrimonioDTOPesquisa = patrimonioDTOPesquisa;
    }

    public List<LocalizacaoDTO> getListaLocalizacao() throws Throwable {        
        return LocalizacaoBO.getInstance().pesquisarPorCampus(getCampusDTO());                
    }

    public List<FabricanteDTO> getListaFabricante() throws Throwable {
        return FabricanteBO.getInstance().findAll();
    }

    public List<TipoPatrimonioDTO> getListaTipoPatrimonio() throws Throwable {
        return TipoPatrimonioBO.getInstance().findAll();
    }
           
    public CampusDTO getCampusDTO() {
        return ContextoBean.getContexto().getCampusAtual();
    }   

    public List<SetorDTO> getListaSetores() {
        return SetorBO.getInstance().pesquisarPorCampus(getCampusDTO());
    }
    
    public PatrimonioDTO getPatrimonioOld() {
        return patrimonioOld;
    }

    public void setPatrimonioOld(PatrimonioDTO patrimonioOld) {
        this.patrimonioOld = patrimonioOld;
    }
   
    public List<PatrimonioDTO> getPatrimoniosInclusao() {
        return patrimoniosInclusao;
    }

    public void setPatrimoniosInclusao(List<PatrimonioDTO> patrimoniosInclusao) {
        this.patrimoniosInclusao = patrimoniosInclusao;
    }

    public boolean isCadastroMultiplo() {
        return cadastroMultiplo;
    }

    public void setCadastroMultiplo(boolean cadastroMultiplo) {
        this.cadastroMultiplo = cadastroMultiplo;
    }

    public Map<String, Boolean> getMapCamposDinamicos() {
        return mapCamposDinamicos;
    }

    public void setMapCamposDinamicos(Map<String, Boolean> mapCamposDinamicos) {
        this.mapCamposDinamicos = mapCamposDinamicos;
    }    
    
    @Override
    public void validaContexto() {
        if (alterando){
            if (!objeto.getLocalizacaoDTO().getCampusDTO().equals(ContextoBean.getContexto().campusAtual)){
                FacesUtil.adicionarMensagemErro("Mude seu contexto para acessar o campus desejado!");
                FacesUtil.redirect("erroPaginaNaoEncontrada");
            }
        }
    }
}