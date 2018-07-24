package com.bean;

import com.bo.CampusBO;
import com.bo.InterfaceBO;
import com.bo.PermissaoBO;
import com.bo.SetorBO;
import com.bo.UsuarioBO;
import com.bo.UsuarioPermissaoSetorBO;
import com.dto.CampusDTO;
import com.dto.InterfaceDTO;
import com.dto.PermissaoDTO;
import com.dto.SetorDTO;
import com.dto.UsuarioDTO;
import com.dto.UsuarioPermissaoSetorDTO;
import com.util.FacesUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "permissaoBean")
@ViewScoped
public class PermissaoBean implements Serializable {

    private PermissaoBO permissaoBO = PermissaoBO.getInstance();
    private InterfaceBO interfaceBO = InterfaceBO.getInstance();
    private UsuarioPermissaoSetorBO usuarioPermissaoSetorBO = UsuarioPermissaoSetorBO.getInstance();
    private PermissaoDTO permissaoDTO = new PermissaoDTO();
    private UsuarioDTO usuarioDTO;
    private Map<PermissaoDTO, Boolean> checkMapPermissao = new HashMap<>();
    private String campoPesquisar;
    private List<SetorDTO> setoresSelecionados = new ArrayList<>();
    private List<SetorDTO> setoresUsuario = new ArrayList<>();
    private final static String tituloLayoutUnit = "Permissão";
    private boolean alterando;

    private UsuarioPermissaoSetorDTO criaObjetoPermissaoSetorDTO(SetorDTO t) {
        UsuarioPermissaoSetorDTO upsdto = new UsuarioPermissaoSetorDTO();
        upsdto.setSetorDTO(t);
        upsdto.setUsuarioDTO(usuarioDTO);
        upsdto.setPermissaoDTO(permissaoBO.pesquisarPermissaoPorInterface(interfaceBO.pesquisarInterfacePorCdu("relatorio")));
        return upsdto;
    }

    public void save() throws Throwable {
        if (usuarioDTO != null) {
            if (pegarSelecionadoPermissao() != usuarioDTO.getPermissoes()) {
                if (!validaPermissaoRelatorio()) {
                    setoresUsuario.forEach((SetorDTO t) -> usuarioPermissaoSetorBO.remove(criaObjetoPermissaoSetorDTO(t)));
                }
                if (usuarioPermissaoSetorBO.pesquisarPorUsuario(usuarioDTO) != null) {
                    usuarioDTO.setPermissoes(pegarSelecionadoPermissao());
                }
                UsuarioBO.getInstance().update(usuarioDTO);
            }
            //Se ele tiver acesso ao relatório persiste os setores na qual ele tem permissão para ver o relatório
            if (!validaPermissaoRelatorio()) {
                getSetores().forEach((SetorDTO t) -> usuarioPermissaoSetorBO.remove(criaObjetoPermissaoSetorDTO(t)));
                getSetoresSelecionados().forEach(setor
                        -> usuarioPermissaoSetorBO.update(criaObjetoPermissaoSetorDTO(setor))
                );
            }
            FacesUtil.adicionarMensagemAviso("Permissão definida com sucesso.");
        } else {
            FacesUtil.adicionarMensagemErro("Favor selecionar um usuario");
        }
    }

    public String novo() {
        usuarioDTO = null;
        return "";
    }

    private List<PermissaoDTO> pegarSelecionadoPermissao() {
        return getCheckMapPermissao().entrySet().
                stream().filter(entry -> entry.getValue()).
                map(e -> e.getKey()).
                collect(Collectors.toList());
    }

    public List<SetorDTO> getSetoresUsuarioPorCampus(CampusDTO campus) throws Throwable {
        return new SetorBO().consultarSetoresDoUsuarioPorCampus(ContextoBean.getContexto());
    }

    public List<CampusDTO> getCampusUsuarioLogado() throws Throwable {
        return new CampusBO().pesquisarCampusPorUsuario(ContextoBean.getContexto().getUsuarioLogado());
    }

    public boolean validaPermissaoRelatorio() throws Throwable {
        return !interfaceBO.pesquisarPorUsuarioInterfaceTemAcesso(usuarioDTO, "relatorio");
    }

    public void update() {
        if (usuarioDTO != null) {
            checkMapPermissao = new HashMap<>();
            setoresSelecionados = usuarioPermissaoSetorBO.pesquisarSetorPermissaoRelatorio(usuarioDTO);
            incluirAlterar();
        }
    }

    public void incluirAlterar() {
        permissaoBO.permissoesUsuario(usuarioDTO).
                forEach(p -> checkMapPermissao.put(p, Boolean.TRUE));

    }

    public PermissaoDTO getPermissaoDTO() {
        return permissaoDTO;
    }

    public void setPermissaoDTO(PermissaoDTO permissaoDTO) {
        this.permissaoDTO = permissaoDTO;
    }

    public List<InterfaceDTO> interfaces(InterfaceDTO pai) {
        return usuarioDTO != null ? interfaceBO.pesquisarPorInterfaceRecursiva(pai) : null;
    }

    public List<InterfaceDTO> getInterefacePai() {
        return usuarioDTO != null ? interfaceBO.interfacesPais() : null;
    }

    public List<UsuarioDTO> getDadosPesquisa() {
        UsuarioDTO userPesquisa = new UsuarioDTO();
        userPesquisa.setNome(campoPesquisar);
        return UsuarioBO.getInstance().pesquisarNomeECampus(userPesquisa, ContextoBean.getContexto().getCampusAtual());
    }

    public String getCampoPesquisar() {
        return campoPesquisar;
    }

    public void setCampoPesquisar(String campoPesquisar) {
        this.campoPesquisar = campoPesquisar;
    }

    public void validaCampo(List<String> erros) {
    }

    public UsuarioDTO getUsuarioDTO() {
        return usuarioDTO;
    }

    public void setUsuarioDTO(UsuarioDTO usuarioDTO) {
        this.usuarioDTO = usuarioDTO;
    }

    public Map<PermissaoDTO, Boolean> getCheckMapPermissao() {
        return checkMapPermissao;
    }

    public void setCheckMapPermissao(Map<PermissaoDTO, Boolean> checkMapPermissao) {
        this.checkMapPermissao = checkMapPermissao;
    }

    public List<PermissaoDTO> getPermissoes() throws Throwable {
        return permissaoBO.permissoesUsuario(usuarioDTO);
    }

    public List<PermissaoDTO> getPermissaoDoUsuarioSelecionado() {
        return null;
    }

    public String getTituloLayoutUnit() {
        return tituloLayoutUnit;
    }

    public List<UsuarioDTO> buscaUsuario(String query) {
        return UsuarioBO.getInstance().consultarUsuariosMesmoCampus(ContextoBean.getContexto().getUsuarioLogado());
    }

    public List<SetorDTO> getSetores() throws Throwable {
        return SetorBO.getInstance().pesquisarPorCampus(CampusBO.getInstance().pesquisarCampusPorUsuario(ContextoBean.getContexto().getUsuarioLogado()));
    }

    public void setSetoresSelecionados(List<SetorDTO> setoresSelecionados) {
        this.setoresSelecionados = setoresSelecionados;
    }

    public List<SetorDTO> getSetoresSelecionados() {
        return setoresSelecionados;
    }

    public List<SetorDTO> getSetoresUsuario() {
        return setoresUsuario;
    }

    public void setSetoresUsuario(List<SetorDTO> setoresUsuario) {
        this.setoresUsuario = setoresUsuario;
    }

    public boolean isAlterando() {
        return alterando;
    }

    public void setAlterando(boolean alterando) {
        this.alterando = alterando;
    }

    public List<UsuarioDTO> usuarios(String nome) {
        UsuarioDTO pesquisa = new UsuarioDTO();
        pesquisa.setNome(nome);
        return UsuarioBO.getInstance().pesquisarNome(pesquisa);
    }

}
