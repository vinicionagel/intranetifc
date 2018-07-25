package com.bean;

import com.bo.CampusBO;
import com.bo.FuncaoBO;
import com.bo.SetorBO;
import com.bo.UsuarioBO;
import com.bo.UsuarioSetorBO;
import com.core.GenericBean;
import com.dto.CampusDTO;
import com.dto.FuncaoDTO;
import com.dto.UsuarioDTO;
import com.dto.UsuarioSetorDTO;
import com.util.FacesUtil;
import static com.util.FacesUtil.*;
import com.util.MD5;
import com.util.ThreadEmail;
import com.util.ValidadorCampo;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;

@Named(value = "usuarioBean")
@ViewScoped
public class UsuarioBean extends GenericBean<UsuarioDTO> implements Serializable{

    private static final long serialVersionUID = 100022L;
    protected String login;
    protected String senha;
    private String confirmaSenha = "asd";
    private String loginOld = "";
    private String emailOld = "";
    private UsuarioDTO usuarioDTOPesquisa = new UsuarioDTO();
    private boolean alterandoSenha = false;
    private boolean alterandoUsuarioAdmin;
    private boolean proprioCadastro; // Indica se é o próprio usuário que está se cadastrando    
    private CampusBO campusBO = CampusBO.getInstance();
    private List<CampusDTO> listaCampus;
    private CampusDTO campusUsuario;

    public UsuarioBean() {
        tituloLayoutUnit = "Usuário";
        String parameter = retornaViaParameter("codigo");
        if (parameter != null) {
            if (StringUtils.isNumeric(parameter)) {
                objeto = getBO().findById(Long.parseLong(parameter));
                alterandoUsuarioAdmin = true;
                alterando = true;
            } else if (parameter.equals("minhaPagina")) {
                setConfig();
                setObjeto(ContextoBean.getContexto().getUsuarioLogado());
            } else if (parameter.equals("cadastrese")){
                cadastrese();
            } else {
                alterando = false;
            }
        }
    }
    
    private void setConfig(){
        setAlterandoUsuarioAdmin(false);
        setAlterando(true);
        setObjeto(ContextoBean.getContexto().getUsuarioLogado());
        getObjeto().setSenhaMD5(getBO().findById(ContextoBean.getContexto().getUsuarioLogado().getCodigo()).getSenha());
        setLoginOld(getObjeto().getLogin());
        setEmailOld(getObjeto().getEmail());
        setAlterandoSenha(false);
        proprioCadastro = false;
    }
    
    public String minhaConta() {
        return "minhaConta";
    }

    public String saveSenha() {
        List<String> erros = new ArrayList<>();

        if (senha.equals(MD5.cripto(""))) {
            adicionarMensagemErro("Campo SENHA ATUAL deve ser informado");
            erros.add("confirmação da senha");
        }

        if (objeto.getSenha().equals(MD5.cripto(""))) {
            adicionarMensagemErro("Campo SENHA NOVA deve ser informado");
            erros.add("confirmação da senha");
        }

        if (confirmaSenha.equals(MD5.cripto(""))) {
            adicionarMensagemErro("Campo CONFIRMAR SENHA deve ser informado");
            erros.add("confirmação da senha");
        }

        if (!MD5.cripto(senha).equals(getBO().findById(ContextoBean.getContexto().getUsuarioLogado().getCodigo()).getSenha())) {
            adicionarMensagemErro("Senha Atual está incorreta");
            erros.add("senha atual");
        }

        if (!objeto.getSenha().equals(confirmaSenha)) {
            adicionarMensagemErro("Confirmação da senha está incorreta");
            erros.add("confirmação da senha");
        }

        if (erros.isEmpty()) {
            getBO().update(objeto);
            setAlterandoSenha(false);
            adicionarMensagemAviso("Senha Alterada com sucesso");
        }
        return "minhaConta";
    }

    public void alterarSenha() {
        this.senha = "";
        setAlterandoSenha(true);
    }

    public void cancelAlteracaoSenha() {
        setAlterandoSenha(false);
    }

    public String home() {
        return "home";
    }
    
    @Override
    public void validaCampo(List<String> erros, UsuarioDTO usuario) {
        ValidadorCampo.validarCampoVazio(usuario.getNome(), "NOME", erros);
        ValidadorCampo.validarCampoVazio(usuario.getEmail(), "EMAIL", erros);
        ValidadorCampo.validarCampoVazio(usuario.getLogin(), "LOGIN", erros);

        if (!isAlterando()) {
            if (getObjeto().getSenha().equals(MD5.cripto(""))) {
                erros.add("Campo SENHA deve ser informado.");
            }
            if (getConfirmaSenha().equals(MD5.cripto(""))) {
                erros.add("Campo CONFIRMAR SENHA deve ser informado.");
            }
        }

        if (isProprioCadastro()) {
            ValidadorCampo.validarCampoNulo(campusUsuario, "UNIDADE", erros);
        }
    }

    public boolean veriricaSenhaEConfirmar() {
        if (!objeto.getSenha().equals(confirmaSenha)) {
            adicionarMensagemErro("Confirmação da senha está incorreta");
            return true;
        }
        return false;
    }

    public boolean verificarCampoUnico() {
        if ((!getObjeto().getLogin().equals(loginOld)) && getBO().loginUnico(objeto)) {
            adicionarMensagemWarning("Login " + getObjeto().getLogin() + " já existe.");
            return true;
        }
        if ((!getObjeto().getEmail().equals(emailOld)) && getBO().emailUnico(objeto)) {
            adicionarMensagemWarning("Email " + getObjeto().getEmail() + " já existe.");
            return true;
        }

        if (!getObjeto().getSenha().equals(confirmaSenha)) {
            adicionarMensagemWarning("SENHA e CONFIRMAR SENHA devem ser iguais.");
            return true;
        }

        return false;
    }

    @Override
    public String novo() {
        alterandoUsuarioAdmin = false;
        this.alterando = false;
        this.proprioCadastro = false;
        dadosPesquisa();
        return pesquisar();
    }
    
    public void saveUsuarioSiga(){
        setAlterando(false);
        setProprioCadastro(true);
        save();
    }

    @Override
    public String save() {
        if (super.validacao(objeto)) {
            return null;
        }

        try {
            if (isAlterando()) {
                if (isAlterandoUsuarioAdmin()) {
                    getBO().update(this.objeto);
                    setAlterandoUsuarioAdmin(false);
                    return novo();
                }
                getObjeto().setSenhaMD5(getBO().findById(ContextoBean.getContexto().getUsuarioLogado().getCodigo()).getSenha());
                getBO().update(this.objeto);
                editadoSucesso();
                return home();
            } else {
                if (veriricaSenhaEConfirmar()) {
                    return null;
                }
                if (verificarCampoUnico()) {
                    return null;
                }
                this.objeto.setEstado(2); // estado 2 = ativação pendente
                getBO().save(this.objeto);
                inseridoSucesso();

                if (proprioCadastro) {
                    incluirUsuarioSetor();
                    enviaConfirmacaoDeCadastro();
                    adicionarMensagemAviso("Seu usuário foi cadastrado e está com ativação pendente.");
                    adicionarMensagemAviso("Assim que o administrador da unidade aprovar seu cadastro, você será notificado e terá acesso ao sistema.");
                    redirect("loginPage");
                }else{
                    campusUsuario = ContextoBean.getContexto().getCampusAtual();
                    incluirUsuarioSetor();
                    adicionarMensagemAviso("Seu usuário foi cadastrado na sua unidade atual e está com ativação pendente.");
                }
                return novo();
            }
        } catch (Exception e) {
            Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            //adicionarMensagemErro(e);
            return null;
        }
    }

    /*
     * Inclui o usuário que acaba de se cadastrar no setor "Usuário Sem Setor"
     */
    private void incluirUsuarioSetor() {
        UsuarioSetorBO usuarioSetorBO = UsuarioSetorBO.getInstance();
        SetorBO setorBO = SetorBO.getInstance();
        FuncaoBO funcaoBO = FuncaoBO.getInstance();

        UsuarioSetorDTO usuarioSetor = new UsuarioSetorDTO();

        usuarioSetor.setUsuarioDTO(objeto);
        
        usuarioSetor.setSetorDTO(setorBO.pesquisarSetorUsuariosSemSetor(campusUsuario));

        FuncaoDTO funcaoDTO = funcaoBO.pesquisarNome("").get(0); // seta qualquer função, pq o campo é PK

        usuarioSetor.setFuncaoDTO(funcaoDTO);

        usuarioSetorBO.save(usuarioSetor);
    }

    /*
     *	    Envia e-mail para os adm do campus, informando que um usuário se cadastrou no sistema
     *	e está com ativação pendente
     */
    private void enviaConfirmacaoDeCadastro() {
        StringBuilder msg = new StringBuilder();
        msg.append("<html><body><p>Usuário <i>");
        msg.append(objeto.getNome());
        msg.append("</i> se cadastrou no sistema e está com ativação pendente.</p>");
        msg.append("<p><a href=\"http://intranet.ifc-riodosul.edu.br:8080/Intranet-IFC/pages/usuario/usuarioPesquisar.jsf\">Ativar usuário</a>");
        msg.append("</p></body></html>");

        ThreadEmail te = new ThreadEmail(getBO().pesquisarAdmin(campusUsuario), msg.toString(), "Usuário cadastrado com ativação pendente");        
        te.start();
    }

    @Override
    public String update() {
        setAlterandoUsuarioAdmin(true);
        setEmailOld(getObjeto().getEmail());
        setLoginOld(getObjeto().getLogin());
        return super.update();
    }

    @Override
    public String include() {
        this.setObjeto(new UsuarioDTO());
        return super.include();
    }

    private void cadastrese() {
        setAlterando(false);
        setProprioCadastro(true);
        setObjeto(new UsuarioDTO());
        setListaCampus(campusBO.findAll());
    }

    public String ativarUsuario() {
        objeto.setEstado(1);
        objeto.setUltimoCampus(ContextoBean.getContexto().getCampusAtual());
        getBO().update(objeto);

        StringBuilder msg = new StringBuilder();
        msg.append("<html><body><p>Olá ");
        msg.append(objeto.getNome());
        msg.append("</p>");
        msg.append("<p>Seu usuário acaba de ser ativado. Agora você já pode acessar o sistema.</p>");
        msg.append("<ul><li>usuário: ");
        msg.append(objeto.getLogin());
        msg.append("</li><li>Senha: senha que você informou durante seu cadastro</li></ul>");
        msg.append("<p><a href=\"http://intranet.ifc-riodosul.edu.br\">Acessar o sistema</a></p>");
        msg.append("</body></html>");

        ThreadEmail te = new ThreadEmail(Arrays.asList(new UsuarioDTO[]{objeto}), msg.toString(), "Usuário ativado");        
        te.start();

        return pesquisar();
    }

    @Override
    public String incluirAlterar() {
        return "usuarioIncluirAlterar";
    }

    @Override
    public String pesquisar() {
        return "usuarioPesquisar";
    }

    public String esqueceuSuaSenha() {
        return "esqueceuSuaSenha";
    }

    public String minhaPagina() {
        return "minhaPagina";
    }

    @Override
    public List<UsuarioDTO> dadosPesquisa() {
        setCampoPesquisar(campoPesquisar == null ? campoPesquisar = "" : getCampoPesquisar());
        usuarioDTOPesquisa.setNome(campoPesquisar);
        return lista = getBO().pesquisarUsuariosPorNomeECampus(usuarioDTOPesquisa, ContextoBean.getContexto().getCampusAtual());
    }

    public boolean getPermissaoAtivarUsuario(UsuarioDTO usuario) {
        List<CampusDTO> lCampus = campusBO.pesquisarCampusPorUsuario(usuario);

        if (lCampus == null || lCampus.isEmpty()) {
            return false;
        } else {
            return getBO().validaAdmin(lCampus.get(0),ContextoBean.getContexto().getUsuarioLogado());
        }
    }

    public String novaSenha() {
        return "novaSenha";
    }

    private void enviarNovaSenha(UsuarioDTO usuario, String senha) {
        StringBuilder msg = new StringBuilder();
        msg.append("<html><body><p>Olá ");
        msg.append(usuario.getNome());
        msg.append("</p>");
        msg.append("<p>Você solicitou uma nova senha. Seus novos dados são:</p>");
        msg.append("<ul><li>usuário: ");
        msg.append(usuario.getLogin());
        msg.append("</li><li>Senha: ");
        msg.append(senha);
        msg.append("</li></ul>");
        msg.append("<br ><p>Por motivos de segurança, é recomendado que você altere essa senha em seu primeiro acesso ao sistema.</p>");
        msg.append("<p><a href=\"").append(FacesUtil.getContextPath()).append("\">Acessar o sistema</a></p>");
        msg.append("</body></html>");

        ThreadEmail te = new ThreadEmail(Arrays.asList(new UsuarioDTO[]{objeto}), msg.toString(), "Recuparação de senha");        
        te.start();
    }

    public String criarNovaSenha() {
        String novaSenha = new BigInteger(25, new SecureRandom()).toString(32);
        UsuarioDTO usuario = getBO().pesquisarEmail(objeto);

        if (usuario != null) {
            usuario.setSenha(novaSenha);

            if (getBO().update(usuario)) {
                adicionarMensagemAviso("Uma nova senha foi enviada para o e-mail: " + usuario.getEmail());
                adicionarMensagemAviso("Por motivos de segurança, é recomendao que a nova senha seja alterada por você.");

                enviarNovaSenha(usuario, novaSenha);

                return "loginPage";
            } else {
                adicionarMensagemErro("Erro ao atualizar senha.");
                return novaSenha();
            }
        } else {
            adicionarMensagemErro("O e-mail informado é inválido. Você deve informar o e-mail que está vinculado ao seu usuário.");
            return novaSenha();
        }
    }

    @Override
    public UsuarioDTO getObjeto() {
        if (objeto == null) {
            if (ContextoBean.getContexto() != null && alterando){
                return objeto = ContextoBean.getContexto().getUsuarioLogado();
            } else if(getSessionAttribute("contextoLoginSiga") !=  null){
                objeto = (UsuarioDTO) getSessionAttribute("contextoLoginSiga");
                removeSessionAttribute("contextoLoginSiga");
            } else{
                objeto = new UsuarioDTO();
            }
        }
        return objeto;
    }

    @Override
    public UsuarioBO getBO() {
        return UsuarioBO.getInstance();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getLoginOld() {
        return loginOld;
    }

    public void setLoginOld(String loginOld) {
        this.loginOld = loginOld;
    }

    public String getEmailOld() {
        return emailOld;
    }

    public void setEmailOld(String emailOld) {
        this.emailOld = emailOld;
    }

    public boolean isAlterandoSenha() {
        return alterandoSenha;
    }

    public void setAlterandoSenha(boolean alterandoSenha) {
        this.alterandoSenha = alterandoSenha;
    }

    public String getConfirmaSenha() {
        return confirmaSenha;
    }

    public void setConfirmaSenha(String confirmaSenha) {
        this.confirmaSenha = MD5.cripto(confirmaSenha);
    }

    public boolean isAlterandoUsuarioAdmin() {
        return alterandoUsuarioAdmin;
    }

    public void setAlterandoUsuarioAdmin(boolean alterandoUsuarioAdmin) {
        this.alterandoUsuarioAdmin = alterandoUsuarioAdmin;
    }

    public boolean isProprioCadastro() {
        return proprioCadastro;
    }

    public void setProprioCadastro(boolean proprioCadastro) {
        this.proprioCadastro = proprioCadastro;
    }

    public void setListaCampus(List<CampusDTO> listaCampus) {
        this.listaCampus = listaCampus;
    }

    public List<CampusDTO> getListaCampus() {
        return CampusBO.getInstance().findAll();
    }

    public CampusDTO getCampusUsuario() {
        return campusUsuario;
    }

    public void setCampusUsuario(CampusDTO campusUsuario) {
        this.campusUsuario = campusUsuario;
    }
}
