package com.bean;

import com.bo.CampusBO;
import com.bo.FuncaoBO;
import com.bo.InterfaceBO;
import com.bo.PermissaoBO;
import com.bo.SetorBO;
import com.bo.UsuarioBO;
import com.bo.UsuarioSetorBO;
import com.core.GenericBean;
import com.dto.CampusDTO;
import com.dto.FuncaoDTO;
import com.dto.PermissaoDTO;
import com.dto.SetorDTO;
import com.dto.UsuarioDTO;
import com.dto.UsuarioSetorDTO;
import com.util.FacesUtil;
import static com.util.FacesUtil.*;
import com.util.SelectBoxUtil;
import com.util.ValidadorCampo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "vincularUsuarioSetorBean")
@ViewScoped
public class VincularUsuarioSetorBean extends GenericBean<UsuarioSetorDTO> implements Serializable{
    
    private static final long serialVersionUID = 100023L;
    private List<UsuarioSetorDTO> listaVinculosPersistencia = new ArrayList<>();                
    private List<SelectItem> listaFuncoesInclusao = new ArrayList<>(); 
    private UsuarioBO usuarioBO = UsuarioBO.getInstance();
    private FuncaoBO funcaoBO = FuncaoBO.getInstance();
    private SetorBO setorBO = SetorBO.getInstance();    
    
    private UsuarioSetorDTO usuarioSetorDTO = new UsuarioSetorDTO();    
    private UsuarioDTO usuarioDTO = new UsuarioDTO();
    private FuncaoDTO funcaoDTO = new FuncaoDTO();   
    private SetorDTO setorDTO = new SetorDTO();    
    private CampusDTO campusDTO;
    
    private UsuarioDTO usuarioDTOPesquisa = new UsuarioDTO();
    private Long setorDTOPesquisa;
    
    private boolean incluindo;    
    private boolean opcao1;
    private boolean opcao2;
    private int opcao;
    
    public VincularUsuarioSetorBean() {
        String parametro = retornaViaParameter("incluindo") != null ? retornaViaParameter("incluindo") : "";
        switch (parametro) {
            case "true":
                incluindo = true;
                break;
            case "false":
                setAlterando(true);
                break;
        }
        tituloLayoutUnit = "Vinculo";
    }

    @Override
    public String incluirAlterar() {        
        return "vincularUsuarioSetorIncluirAlterar";
    }

    @Override
    public String pesquisar() {
        return "vincularUsuarioSetorPesquisar";
    }

    @Override
    public String novo() throws Throwable {
        setorDTOPesquisa = 0l;  
        listaVinculosPersistencia = new ArrayList<>();
        usuarioDTO = new UsuarioDTO();
        funcaoDTO = new FuncaoDTO();
        setorDTO = new SetorDTO();
        dadosPesquisa();
        return pesquisar();
    }

    @Override
    public void validaCampo(List<String> erros, UsuarioSetorDTO usuarioSetor) {
        ValidadorCampo.validarCampoNulo(getUsuarioDTO(), "USUÁRIO", erros);
        for (UsuarioSetorDTO u : listaVinculosPersistencia){
            if(u.getFuncaoDTO() == null){                
                erros.add("Informe a função do setor: " + u.getSetorDTO().getDescricao());
            }                
        }                   
    }
    
    private List<PermissaoDTO> preencherListaPermissao(String interfacePai,String interfaceFilho){
        List permissaList = usuarioDTO.getPermissoes() != null ? usuarioDTO.getPermissoes() : new ArrayList();
        permissaList.addAll(PermissaoBO.getInstance().pesquisarPermisoesPorInterface(InterfaceBO.getInstance().pesquisarInterfacePorCdu(interfaceFilho)));
        permissaList.addAll(PermissaoBO.getInstance().pesquisarPermisoesPorInterface(InterfaceBO.getInstance().pesquisarInterfacePorCdu(interfacePai)));
        return permissaList;
    }
    
    private void inclusao() {
        boolean entroChamado = false;
        boolean entroOcorrencia = false;
        for (UsuarioSetorDTO u : getListaVinculosPersistencia()) {
            if (u.getSetorDTO().getInterfaceAtivo() != null){
                if (u.getSetorDTO().getInterfaceAtivo().equals("'2:1',") && !entroChamado) {
                    usuarioDTO.setPermissoes(preencherListaPermissao("chamado", "CONTM"));
                    entroChamado = true;
                } else if (u.getSetorDTO().getInterfaceAtivo().equals("'22:1',") && !entroOcorrencia) {
                    usuarioDTO.setPermissoes(preencherListaPermissao("ocorrencia", "CONTI"));
                    entroOcorrencia = true;
                } else {
                    usuarioDTO.setPermissoes(preencherListaPermissao("chamado", "CONTM"));
                    usuarioDTO.setPermissoes(preencherListaPermissao("ocorrencia", "CONTI"));
                    entroOcorrencia = true;
                    entroChamado = true;
                }
            }
            u.setResponsavel(u.getResponsavelBool() == true ? 1 : 0);
            getBO().save(u);
        }
    }
    
    @Override
    public String save() throws Throwable {              
        this.listaVinculosPersistencia = gerarListaPersistencia(getListaVinculosPersistencia());
        List<String> erros = new ArrayList<>();
        validaCampo(erros, null);
        if (!erros.isEmpty()) {
            FacesUtil.adicionarMensagemErro(erros);
            return null;
        }         
        try {
            if(isAlterando()){                                             
                for(UsuarioSetorDTO u : getListaVinculosPersistencia()){
                    u.setResponsavel(u.getResponsavelBool() == true ? 1 : 0);
                    getBO().update(u);
                }
                editadoSucesso();
                return novo();
            } else if(isIncluindo()){                                          
                if(!getListaVinculosPersistencia().isEmpty()){
                    UsuarioSetorDTO remover = getBO().pesquisarUsuarioSetorUsuarioSemSetor(getListaVinculosPersistencia().get(0).getUsuarioDTO());
                    if(remover != null){
                        getBO().remove(remover);
                    }
                }
                inclusao();
                try {
                    usuarioBO.update(usuarioDTO);  // pode acontecer erro mas sem problemas...
                } catch (Exception e) {
                }
                inseridoSucesso();
                return novo();
            } else {             
                for(UsuarioSetorDTO u : getListaVinculosPersistencia()){
                    getBO().remove(u);
                }                
                excluidoSucesso();
                return novo();
            }                        
        } catch (Exception e) {
            return null;
        }
    }

    public UsuarioSetorDTO criarVinculo() {
        UsuarioSetorDTO usuarioSetor = new UsuarioSetorDTO();
        usuarioSetor.setUsuarioDTO(usuarioDTO);        
        usuarioSetor.setSetorDTO(setorDTO);        
        usuarioSetor.setFuncaoDTO(funcaoDTO);
        return usuarioSetor;
    }
    
    public List<UsuarioSetorDTO> gerarListaPersistencia(List<UsuarioSetorDTO> listaInclusao) {
        List<UsuarioSetorDTO> retorno = new ArrayList<>();
        for(UsuarioSetorDTO u : listaInclusao){
            if(u.isIsSelecionado()){           
                u.setSetorDTO(u.getSetorDTO());
                u.setUsuarioDTO(usuarioDTO);
                try {
                    u.setFuncaoDTO(funcaoBO.findById(u.getFuncao()));
                } catch (NullPointerException nullPointer){
                } finally {
                    retorno.add(u);   
                }
            }
        }
        return retorno;
    }
    
    public void carregarListaSetoresFuncoes() {
        listaVinculosPersistencia = new ArrayList<>();
        if(usuarioDTO != null && campusDTO != null){
            List<SetorDTO> listaAuxiliar;
            if(isIncluindo()){
                listaAuxiliar = setoresDisponiveisUsuario(setorBO.consultarSetoresDoUsuarioPorCampusUsuarioSemSetor(usuarioDTO,campusDTO));
            } else {
                listaAuxiliar = setorBO.consultarSetoresDoUsuario(usuarioDTO,campusDTO);
            }
            UsuarioSetorBO usuarioSetorBO = UsuarioSetorBO.getInstance();
            for(SetorDTO s : listaAuxiliar){
                UsuarioSetorDTO usuarioSetor = new UsuarioSetorDTO();     
                usuarioSetor.setSetorDTO(s);
                usuarioSetor.setResponsavel(usuarioSetorBO.pesquisarUsuarioSetorPorSetorEUsuario(usuarioDTO, s));
                FuncaoDTO f = funcaoBO.pesquisarFuncaoUsuarioSetor(usuarioDTO, s);
                if(f != null){
                    usuarioSetor.setFuncao(f.getCodigo());
                }

                this.listaVinculosPersistencia.add(usuarioSetor);
            }
        }
    }
    
    public List<SetorDTO> setoresDisponiveisUsuario(List<SetorDTO> setores){
        List<SetorDTO> retorno = getListaSetores();
        retorno.removeAll(setores);
        return retorno;
    }
       
    @Override
    public List<UsuarioSetorDTO> dadosPesquisa(){
        if (getOpcao() == 1 && setorDTOPesquisa != null) {       
            try {
                setorDTO = (setorBO.findById(setorDTOPesquisa));
            } catch (Throwable ex) {
                Logger.getLogger(ChartBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            return lista = getBO().pesquisarUsuariosSetor(setorDTO);    
        } else if (getOpcao() == 2 && usuarioDTOPesquisa != null) {             
            return lista = getBO().pesquisarSetoresUsuario(usuarioDTOPesquisa);
        } else {
            return lista = getBO().findAll();
        }
    }

    public void mostrarOpcaoPesquisa() {
        if (getOpcao() == 1) {
            setOpcao1(true);
            setOpcao2(false);
        } else {
            setOpcao1(false);
            setOpcao2(true);
        }
    }
        
    @Override
    public UsuarioSetorBO getBO() {
        return UsuarioSetorBO.getInstance(); 
    }        

    public int getOpcao() {
        return opcao;
    }

    public void setOpcao(int opcao) {
	this.opcao = opcao;
    }
    
    public UsuarioSetorDTO getUsuarioSetorDTO() {
        return usuarioSetorDTO;
    }

    public void setUsuarioSetorDTO(UsuarioSetorDTO usuarioSetorDTO) {
        this.usuarioSetorDTO = usuarioSetorDTO;
    }

    public UsuarioDTO getUsuarioDTO() {
        return usuarioDTO;
    }

    public void setUsuarioDTO(UsuarioDTO usuarioDTO) {
        this.usuarioDTO = usuarioDTO;
    }

    public SetorDTO getSetorDTO() {
        return setorDTO;
    }

    public void setSetorDTO(SetorDTO setorDTO) {
        this.setorDTO = setorDTO;
    }

    public List<UsuarioDTO> getListaUsuarios() throws Throwable {                   
        return usuarioBO.pesquisarTodosAtivos();
    }

    public List<SetorDTO> getListaSetores() {
        return setorBO.pesquisarTodosMenosSetorSemSetorPorCampus(campusDTO);
    }
    
    public boolean isOpcao1() {
        return opcao1;
    }

    public void setOpcao1(boolean opcao1) {
        this.opcao1 = opcao1;
    }

    public boolean isOpcao2() {
        return opcao2;
    }

    public void setOpcao2(boolean opcao2) {
        this.opcao2 = opcao2;
    }

    public List<FuncaoDTO> getListaFuncoes() throws Throwable {
        return funcaoBO.findAll();
    }

    public FuncaoDTO getFuncaoDTO() {
        return funcaoDTO;
    }

    public void setFuncaoDTO(FuncaoDTO funcaoDTO) {
        this.funcaoDTO = funcaoDTO;
    }

    public UsuarioDTO getUsuarioDTOPesquisa() {
        return usuarioDTOPesquisa;
    }

    public void setUsuarioDTOPesquisa(UsuarioDTO usuarioDTOPesquisa) {
        this.usuarioDTOPesquisa = usuarioDTOPesquisa;
    }

    public Long getSetorDTOPesquisa() {
        return setorDTOPesquisa;
    }

    public void setSetorDTOPesquisa(Long setorDTOPesquisa) {
        this.setorDTOPesquisa = setorDTOPesquisa;
    }

    public List<UsuarioSetorDTO> getListaVinculosPersistencia() {
        return listaVinculosPersistencia;
    }

    public void setListaVinculosPersistencia(List<UsuarioSetorDTO> listaVinculosPersistencia) {
        this.listaVinculosPersistencia = listaVinculosPersistencia;
    }

    public List<SelectItem> getListaFuncoesInclusao() throws Throwable {
        listaFuncoesInclusao.clear();
        for(FuncaoDTO f : getListaFuncoes()){
            listaFuncoesInclusao.add(new SelectItem(f.getCodigo(), f.getDescricao()));
        }
        return listaFuncoesInclusao;
    }

    public void setListaFuncoesInclusao(List<SelectItem> listaFuncoesInclusao) {
        this.listaFuncoesInclusao = listaFuncoesInclusao;
    }

    public boolean isIncluindo() {
        return incluindo;
    }

    public void setIncluindo(boolean incluindo) {
        this.incluindo = incluindo;
    }   
    
    public List<SelectItem> getSetorSelectItens() throws Throwable {
       return new SelectBoxUtil().retornaListaEmSelectItem(getSetoresUsuario());      
    }
    
    public List<SetorDTO> getSetoresUsuario() throws Throwable {
        return setorBO.consultarSetoresOrdenadosPorCampus(ContextoBean.getContexto());
    }             
    
    public List<CampusDTO> getListaCampus(){
        return CampusBO.getInstance().findAll();
    }

    public CampusDTO getCampusDTO() {
        return campusDTO;
    }

    public void setCampusDTO(CampusDTO campusDTO) {
        this.campusDTO = campusDTO;
    }
    
    
}