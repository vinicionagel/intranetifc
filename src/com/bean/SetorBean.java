package com.bean;

import com.bo.CampusBO;
import com.bo.ServicoBO;
import com.bo.SetorBO;
import com.bo.TipoServicoBO;
import com.core.GenericBean;
import com.core.IValidaContexto;
import com.dto.CampusDTO;
import com.dto.ServicoDTO;
import com.dto.SetorDTO;
import com.dto.TipoServicoDTO;
import com.util.FacesUtil;
import static com.util.FacesUtil.*;
import com.util.SelectBoxUtil;
import com.util.ValidadorCampo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "setorBean")
@ViewScoped
public class SetorBean extends GenericBean<SetorDTO> implements Serializable, IValidaContexto{
    
    private static final long serialVersionUID = 100018L;
    private TipoServicoBO tipoServicoBO = TipoServicoBO.getInstance();
    private ServicoBO servicoBO = ServicoBO.getInstance();    
    private SetorDTO setorArvore = new SetorDTO();    
    private SetorDTO setorOld;    
    private boolean mostrarArvoreServicos = false;    
    private Map<String, Boolean> mapArvore = new HashMap<>();
    private Long setorDestino = null;    

    public SetorBean() {
        tituloLayoutUnit = "Setor";
        objeto = new SetorDTO();
        getCodigo();
        validaContexto();
        objeto.setCampusDTO(ContextoBean.getContexto().getCampusAtual());
        setorOld = new SetorDTO(ContextoBean.getContexto().getCampusAtual(), objeto.getInterfaceAtivo(), objeto.getDescricao());
    }

    @Override
    public String incluirAlterar() {
        return "setorIncluirAlterar";        
    }

    @Override
    public String pesquisar() {
        return "setorPesquisar";
    }

    @Override
    public String novo() throws Throwable {
        mapArvore = new HashMap<>();
        setAlterando(false);
        dadosPesquisa();
        setMostrarArvoreServicos(false);        
        return pesquisar();
    }

    @Override
    public boolean clausulaUnico() {
        return !getObjeto().getDescricao().equals(getSetorOld().getDescricao());
    }    
      
    public void atualizarArvoreServicos() throws Throwable {
        mapArvore = new HashMap<>();
        setorDestino = 0l;
        mostrarArvoreServicos = true;
    }

    public void copiarArvore() throws Throwable {      
        int contSelecionados = 0;
        for (int i = 0; i < mapArvore.entrySet().toArray().length; i++) {
            String valorMap = mapArvore.entrySet().toArray()[i].toString();
            if (valorMap.substring(valorMap.indexOf("=") + 1).equals("true")) {
                String chaveMap = valorMap.substring(0, valorMap.indexOf("="));
                if (chaveMap.indexOf("-") == 0) {
                    TipoServicoDTO auxTS = tipoServicoBO.findById(Long.parseLong(chaveMap.substring(1)));
                    if (tipoServicoBO.pesquisarPorSetorEDescricao(setorDestino, auxTS) == null) {
                        inserirTipoServico(auxTS);
                    }                    
                } else {
                    ServicoDTO auxS = servicoBO.findById(Long.parseLong(chaveMap.substring(chaveMap.indexOf("-") + 1)));
                    TipoServicoDTO auxTS = tipoServicoBO.findById(Long.parseLong(chaveMap.substring(0, chaveMap.indexOf("-"))));                                        
                    if (tipoServicoBO.pesquisarPorSetorEDescricao(setorDestino, auxTS) == null) {                        
                        inserirServico(inserirTipoServico(auxTS), auxS);
                    } else {
                        if(servicoBO.pesquisarPorTipoServicoEDescricao(tipoServicoBO.pesquisarPorSetorEDescricao(setorDestino, auxTS), auxS) == null){                                                        
                            inserirServico(auxTS, auxS);
                        }
                    }                                        
                }
            } else {
                contSelecionados++;
            }
        }
        if(contSelecionados == mapArvore.entrySet().toArray().length){
            adicionarMensagemErro("Selecione algum item para ser copiado.");
        } else {
            adicionarMensagemAviso("Itens Copiados com sucesso.");
        }
        atualizarArvoreServicos();
    }
     
    private TipoServicoDTO inserirTipoServico(TipoServicoDTO auxTS){
        try {                        
            TipoServicoDTO ts = new TipoServicoDTO();
            ts.setDescricao(auxTS.getDescricao());
            ts.setSetorDTO(getBO().findById(setorDestino));
            tipoServicoBO.save(ts);
            return ts;
        } catch (Throwable ex) {
            Logger.getLogger(SetorBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
            
    private void inserirServico(TipoServicoDTO ts, ServicoDTO auxS){
        try {
            ServicoDTO s = new ServicoDTO();
            s.setDescricaoCompleta(auxS.getDescricaoCompleta());
            s.setDescricaoCurta(auxS.getDescricaoCurta());
            s.setTipoServicoDTO(tipoServicoBO.pesquisarPorSetorEDescricao(setorDestino, ts));
            servicoBO.save(s);
        } catch (Throwable ex) {
            Logger.getLogger(SetorBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getChave(int codigo0,int codigo1) {
        return codigo0 + "-"+codigo1;
    }
        
    public String getChave(int codigo) {
        return "-"+codigo;
    }
    
    @Override
    public void validaCampo(List erros, SetorDTO setor) {
        objeto.setInterfaceAtivo(objeto.getConcat().toString().replaceAll(" ", "").
                    replaceAll("\\[","").replaceAll("]", ""));
        objeto.setInterfaceAtivo(!objeto.getInterfaceAtivo().contains(",") ? objeto.getInterfaceAtivo().concat(",") : objeto.getInterfaceAtivo());
        ValidadorCampo.validarCampoVazio(setor.getInterfaceAtivo(), "INTERFACE", erros); 
        ValidadorCampo.validarCampoVazio(setor.getDescricao(), "DESCRIÇÃO", erros);        
    }

    @Override
    public List<SetorDTO> dadosPesquisa() {
        setCampoPesquisar(campoPesquisar == null ? campoPesquisar = "" : getCampoPesquisar());
        return lista = getBO().pesquisarSetorPorCampus(getCampoPesquisar(), ContextoBean.getContexto().getCampusAtual());
    }
    
    @Override
    public SetorBO getBO() {
        return SetorBO.getInstance();
    }    
      
    public SetorDTO getSetorOld() {
        return setorOld;
    }

    public void setSetorOld(SetorDTO setorOld) {
        this.setorOld = setorOld;
    }
        
    public boolean isMostrarArvoreServicos() {
        return mostrarArvoreServicos;
    }

    public void setMostrarArvoreServicos(boolean mostrarArvoreServico) {
        this.mostrarArvoreServicos = mostrarArvoreServico;
    }

    public SetorDTO getSetorArvore() {
        return setorArvore;
    }

    public void setSetorArvore(SetorDTO setorArvore) {
        this.setorArvore = setorArvore;
    }

    public List<TipoServicoDTO> getTipoServicosDoSetor() {
        return getSetorArvore() != null ? tipoServicoBO.pesquisarPorSetor(getSetorArvore().getCodigo()) : new ArrayList<TipoServicoDTO>();
    }

    public List<ServicoDTO> getServicosDoTipoServico(TipoServicoDTO ts) throws Throwable {
        return ts != null ? servicoBO.pesquisarPorTipoServico(ts) : new ArrayList<ServicoDTO>();
    }

    public Map<String, Boolean> getMapArvore() {
        return mapArvore;
    }

    public void setMapArvore(Map<String, Boolean> mapArvore) {
        this.mapArvore = mapArvore;
    }

    public List<SelectItem> getSetorSelectItens() throws Throwable {
        return new SelectBoxUtil().retornaListaEmSelectItem(getBO().pesquisarTodosMenosSelecionado(setorArvore));
    }
    
    public Long getSetorDestino() {
        return setorDestino;
    }

    public void setSetorDestino(Long setorDestino) {
        this.setorDestino = setorDestino;
    }            
    
    public List<CampusDTO> getListaCampus(){
        return CampusBO.getInstance().findAll();
    }
    
    @Override
    public void validaContexto() {
        if (alterando) {
            if (!objeto.getCampusDTO().equals(ContextoBean.getContexto().campusAtual)) {
                FacesUtil.adicionarMensagemErro("Mude seu contexto para acessar o campus desejado!");
                FacesUtil.redirect("erroPaginaNaoEncontrada");
            }
        }
    }
}