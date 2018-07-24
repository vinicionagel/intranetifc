package com.bean;

import com.auxiliar.Contexto;
import com.bo.CampusBO;
import com.bo.ConfiguracaoBancoBO;
import com.bo.UsuarioBO;
import com.dto.CampusDTO;
import com.dto.UsuarioDTO;
import com.util.ConnectionFactory;
import static com.util.FacesUtil.*;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@SessionScoped
@ManagedBean(name = "contextoBean")
public class ContextoBean implements Serializable{

    private static final long serialVersionUID = 100012L;
    
    private String login;
    private String senha;
    private CampusDTO campus;    
    private boolean loginSiga;

    public String getNomeUsuarioLogado() {
        String nome = "Erro ao carregar o nome do usuário da sessão";
        try {
            nome = getContexto().getUsuarioLogado().getNome();
        } catch (Exception e) {
            Logger.getLogger(ContextoBean.class.getName()).log(Level.SEVERE, null, e);
        }
        return nome;
    }
    
    private void verificaUltimoCampus(Contexto ctxt) {
        if (ctxt.usuarioLogado.getUltimoCampus() == null) {
            if (!getListaCampus().isEmpty()) {
                setSomenteCampus(getListaCampus().get(0));
                atualizarUsuario(ctxt);
            }
        }
    }
    
    public String logar() throws Throwable {
        UsuarioDTO user = new UsuarioDTO(); // Digitados no login
        user.setSenha(getSenha());
        user.setLogin(getLogin());
        UsuarioDTO usuarioDTO = UsuarioBO.getInstance().pesquisarPorLoginESenha(user); // procura usuário com aquele login e senha
        UsuarioDTO usuarioLocal = UsuarioBO.getInstance().pesquisarPorLogin(user); // procura usuário com login
        if (usuarioDTO != null) { //Se o login e senha estiverem corretos
            return autenticar(usuarioDTO);
        }else if(getLoginSiga() & usuarioLocal != null){ // se selecionar para logar com o siga  e o login conferir
            usuarioDTO = carregarDadosSiga(user);
            if(usuarioDTO != null && usuarioDTO.getSenha().equals(user.getSenha())){// confere a senha e atualiza os dados
                user.setNome(usuarioLocal.getNome());
                user.setEmail(usuarioLocal.getEmail());
                user.setCodigo(usuarioLocal.getCodigo());
                user.setEstado(usuarioLocal.getEstado());
                user.setVerificacao(usuarioLocal.getVerificacao());
                user.setUltimoCampus(usuarioLocal.getUltimoCampus());
                UsuarioBO.getInstance().update(user);
                return autenticar(usuarioLocal);
            }
            adicionarMensagemErro("Combinação incorreta de usuário/senha");
            return null;
        } else{ 
            if(getLoginSiga()){
                if(carregarDadosSiga(user) != null){
                    return "loginSiga";
                }
            }
            adicionarMensagemErro("Combinação incorreta de usuário/senha");
            return null;
        }
    }
    /**
     * ctxt.getUsuarioLogado().getEstado() == 1 eq usuario ativo
     * @param usuarioDTO
     * @return
     * @throws IOException 
     */
    private String autenticar(UsuarioDTO usuarioDTO) throws IOException{
        Contexto ctxt = new Contexto();
        ctxt.usuarioLogado = usuarioDTO;
        if (ctxt.getUsuarioLogado().getEstado() == 1) {
            setSessionAttribute("contexto", ctxt);
            verificaUltimoCampus(ctxt);
            ctxt.campusAtual = ctxt.usuarioLogado.getUltimoCampus();
            this.campus = ctxt.campusAtual;
            String currentPage = (String) getSessionAttribute("currentPage");
            setSessionAttribute("currentPage", null);
            if (currentPage != null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(currentPage);
            }
            return "home";
        } else {
            adicionarMensagemErro("O seu usuario está desativado no sistema.");
            return null;
        }
    }

    private UsuarioDTO carregarDadosSiga(UsuarioDTO user) throws SQLException{
        Connection conexao = ConnectionFactory.conectar(ConfiguracaoBancoBO.getInstance().pesquisarPorTipoConfiguracao(2));
        montarSQLSiga(user);
        Statement statement = conexao.createStatement(
        ResultSet.TYPE_SCROLL_INSENSITIVE,
        ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = statement.executeQuery(montarSQLSiga(user));
        while (rs.next()){
            user.setNome(rs.getString(1));
            user.setEmail(rs.getString(2));
            user.setDataNascimento(rs.getDate(3));
        }
        setSessionAttribute("contextoLoginSiga", user);
        rs.last();
        if(rs.getRow() > 0){
            return user;
        } 
        return null;
    }
    
    private String montarSQLSiga(UsuarioDTO user) {				
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT pf.nome, pf.email, pf.data_nascimento ");
        sql.append("FROM usuario as us, pessoa_fisica as pf ");
        sql.append("WHERE us.pessoa_fisica_id = pf.id ");
        sql.append("AND us.login = '").append(user.getLogin());
        sql.append("' AND us.senha = '").append(user.getSenha()).append("'");
        return sql.toString();	
    }	
         
    public void logoff() throws Throwable {
        redirect("/pages/home/index.jsf");
        getSession(false).invalidate();
    }

    public static synchronized Contexto getContexto() {
        return (Contexto) getSessionAttribute("contexto");
    }

    public void setCampus(CampusDTO campus) {
        this.campus = campus;
        Contexto ctxt = new Contexto();
        ctxt.campusAtual = campus;
        ctxt.usuarioLogado = getContexto().getUsuarioLogado();
        if(!ctxt.usuarioLogado.getUltimoCampus().getCodigo().equals(campus.getCodigo())){
            atualizarUsuario(ctxt);
        }
        setSessionAttribute("contexto", ctxt);
        redirect("/pages/home/home.jsf");
    }
    
    public void setSomenteCampus(CampusDTO campus) {
        this.campus = campus;        
    }
    
    private void atualizarUsuario(Contexto ctxt){
        ctxt.usuarioLogado.setUltimoCampus(getCampus());
        UsuarioBO.getInstance().update(ctxt.usuarioLogado);
    }

    public CampusDTO getCampus() {
        return this.campus;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public List<CampusDTO> getListaCampus() {
        return CampusBO.getInstance().pesquisarCampusPorUsuarioOrderByDesc(getContexto().getUsuarioLogado());
    }   

    public boolean getLoginSiga() {
        return loginSiga;
    }

    public void setLoginSiga(boolean loginSiga) {
        this.loginSiga = loginSiga;
    }
}