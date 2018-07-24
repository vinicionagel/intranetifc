
package com.bean;

import com.core.GenericBean;
import com.dto.UsuarioDTO;
import java.io.Serializable;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import static com.util.FacesUtil.*;
import javax.faces.event.ActionEvent;

@Named(value = "testBean")
@ViewScoped
public class testeBean extends GenericBean<UsuarioDTO> implements Serializable {

    public testeBean() {
        tituloLayoutUnit = "Teste Supremo";
    
    }
    
    public void testButton(ActionEvent actionEvent){
        System.out.println(" HUAUEUHAEH");
        adicionarMensagemAviso("Ola otarios");
    }
    
    @Override
    public void validaCampo(List<String> erros, UsuarioDTO instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String incluirAlterar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String pesquisar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String novo() throws Throwable {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<UsuarioDTO> dadosPesquisa() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
