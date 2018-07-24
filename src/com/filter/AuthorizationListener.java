package com.filter;

import com.bean.ContextoBean;
import com.bo.InterfaceBO;
import com.bo.PermissaoBO;
import com.dto.UsuarioDTO;
import static com.util.FacesUtil.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;

public class AuthorizationListener implements PhaseListener {
    
    @Override
    public void afterPhase(PhaseEvent event) {      
        HttpServletRequest request = getRequest();
        String currentPage = retornaViewId();     
        UsuarioDTO currentUser = null;
         
        try {
            currentUser = ContextoBean.getContexto() == null ? null : ContextoBean.getContexto().getUsuarioLogado();
        } catch (Exception e) {
            Logger.getLogger(AuthorizationListener.class.getName()).log(Level.SEVERE, null, e);
        }
        boolean isLoginPage = (currentPage.lastIndexOf("index.xhtml") > -1);
        boolean paginaSemLogin = currentPage.substring(1, currentPage.indexOf("/", 1)).equals("publico");

        if (!isLoginPage && currentUser == null && !paginaSemLogin) {
            // Praparando para redirecionar para a url acessada após logar
            StringBuilder currentURL = new StringBuilder();
            currentURL.append(getRequest().getRequestURL().toString());            
            if (request.getQueryString() != null) {
                currentURL.append("?");
                currentURL.append(request.getQueryString());
            }
            setSessionAttribute("currentPage", currentURL.toString());
            // Aqui o login deu pau e usuário será lançado para LOGIN            
            redirect("loginPage");
        } else if (currentPage.equals("/pages/chamado/andamentoChamado.xhtml")) {
            if (request.getParameter("codigo") != null) {
                if (PermissaoBO.getInstance().verificarPermissaoParaVerChamado(currentUser, Long.parseLong(request.getParameter("codigo")))) {
                   adicionarMensagemErro("Chamado não encontrado!");
                   redirect("home");
                }
            }
        // controle de acesso!
        } else if (!paginaSemLogin) {
            String cdu = getCDU();
            if (!cdu.equals("home")) {
                try {
                    if (!InterfaceBO.getInstance().pesquisarPorUsuarioInterfaceTemAcesso(currentUser, cdu)) {
                        redirect("erroPaginaNaoEncontrada");
                    }
                } catch (Exception ex) {                    
                    Logger.getLogger(AuthorizationListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public void beforePhase(PhaseEvent event) {
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}