package com.bean;

import com.bo.HoraBO;
import com.core.GenericBean;
import com.dto.ChamadoDTO;
import com.dto.HoraDTO;
import static com.util.FacesUtil.*;
import com.util.ValidadorCampo;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "horaBean")
@ViewScoped
public class HoraBean extends GenericBean<HoraDTO> implements Serializable {

    private static final long serialVersionUID = 100020L;
    private HoraDTO horaDTOold = new HoraDTO();    

    public HoraBean() {
        setObjeto(new HoraDTO());
        tituloLayoutUnit = " Horas";
    }
    
    @Override
    public String novo() throws Throwable {
        setAlterando(false);
        setObjeto(new HoraDTO());        
        return pesquisar();
    }
    
    private String retornarChamado() throws Throwable{
        novo();
        getExternalContext().redirect(
                getExternalContext().getRequestContextPath() + "/pages/chamado/andamentoChamado.jsf?codigo="+getChamadoDTO().getCodigo()); 
        return null;
    }
    
    @Override
    public String save() throws Throwable {                           
        if (super.validacao(getObjeto())) {            
            return null;
        }
        if (alterando) {            
            getBO().update(getObjeto());           
            editadoSucesso();
            objeto = new HoraDTO();
            alterando = false;
            return null;
        } else {                  
            if(getObjeto().getChamadoDTO() == null) {
                getObjeto().setChamadoDTO(getChamadoDTO());
            }
            getObjeto().setUsuarioDTO(ContextoBean.getContexto().getUsuarioLogado());
            getBO().save(getObjeto());
        }
        if(getSessionAttribute("hora") == null && !alterando){
            /**
             * FlashScoop para mostrar mensagem na outra pagina
             */
            adicionarMensagemAviso("Horas adicionadas com sucesso");
            getExternalContext().getFlash().setKeepMessages(true);
            return retornarChamado();
        } else {
            removeSessionAttribute("hora");
            return novo();
        }
    }    
    
    public void update(HoraDTO horaDTO) {
        objeto = horaDTO;
        /**
         * Se o usuario clicar varias vezes em alterar para retornar a tela o que
         * ele estava alterando antes...
         */
        if (isAlterando()) {
            try {
                lista.add(horaDTOold);
            } catch (Throwable ex) {
                Logger.getLogger(HoraBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        setAlterando(true);
        lista.remove(horaDTO);
        setHoraDTOold(horaDTO);
    }   

    @Override
    public void remove() throws Throwable {        
        if (isAlterando()) {
            getLista().add(getHoraDTOold());
        }
        super.remove();        
    }
       
    @Override
    public String incluirAlterar() {
        return "horaIncluirAlterar";
    }

    @Override
    public void validaCampo(List<String> erros, HoraDTO hora) {
        if(hora.getDataTrabalho() != null || hora.getTempo() != null){                        
            ValidadorCampo.validarCampoNulo(hora.getDataTrabalho(), "DATA", erros);                                   
            ValidadorCampo.validarCampoNulo(hora.getTempo(), "TEMPO GASTO", erros);                        
        }
    }
    
    public void cancelarEdicao(){
        alterando = false;
        objeto = new HoraDTO();
    }
    
    public void verificaERedireciona(){
        if((ChamadoDTO) getSessionAttribute("chamadoDTO") == null) {
            try {
                getExternalContext().redirect(   
                        getExternalContext().getRequestContextPath() + "/pages/home/home.jsf");
            } catch (IOException ex) {
                Logger.getLogger(HoraBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public List<HoraDTO> getLista(){
        verificaERedireciona();
        if (!alterando){
                lista = getBO().horasPorChamado(getChamadoDTO());
               
        }
        return lista;        
    }
    
    @Override
    public HoraBO getBO() {
        return HoraBO.getInstance();
    }        
    
    public HoraDTO getHoraDTOold() {
        return horaDTOold;
    }

    public void setHoraDTOold(HoraDTO horaDTOold) {
        this.horaDTOold = horaDTOold;
    }

    public ChamadoDTO getChamadoDTO() {
        return (ChamadoDTO) getSessionAttribute("chamadoDTO");
    }     

    @Override
    public String pesquisar() {
        return null;
    }   

    @Override
    public HoraDTO getObjeto() {
        if(getSessionAttribute("hora") != null) {
            return objeto = ((HoraDTO) getSessionAttribute("hora"));                               
        }         
        return super.getObjeto();
    }        

    @Override
    public List<HoraDTO> dadosPesquisa() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}