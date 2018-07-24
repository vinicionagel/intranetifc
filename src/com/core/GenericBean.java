package com.core;

import static com.util.FacesUtil.*;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericBean<T> implements IGenericBean<T> {

    protected T objeto;
    protected List<T> lista;    
    protected String campoPesquisar;
    protected String tituloLayoutUnit;
    protected boolean alterando;
    /**
     * Serve para carregar o objeto se for uma alteracao...
     */
    protected void getCodigo(){
        String parameter = retornaViaParameter("codigo");
        if (parameter != null) {
            objeto = getBO().findById(Long.parseLong(parameter));
            alterando = true;
        }
    }
    
    @Override
    public String save() throws Throwable{        
        if (validacao(objeto)) {
            return null;
        }
        if (clausulaUnico()){
            if(validaCampoUnico(objeto)){               
                return null;            
            }
        }        
        try {
            if (isAlterando()) {
                getBO().update(objeto);
                editadoSucesso();
            } else {                
                getBO().save(objeto);
                inseridoSucesso();
            }            
            return novo();
        } catch (Exception e) {
            adicionarMensagemErro(e);
            return null;
        }
    }

    @Override
    public void remove() throws Throwable {
        if (getBO().remove(objeto)) {
            novo();
            excluidoSucesso();
        } else {
            adicionarMensagemWarning("Não pode ser deletado pois existem registros em outras tabelas");
        }
    }

    @Override
    public String update() {
        return incluirAlterar();
    }

    @Override
    public String include() {
        return incluirAlterar();
    }
    
    /**
     * Mostra as mensagens de erros, dos campos obrigatórios.
     * @param instance objeto(dados form)
     * @return true se aconteceu um erro. 
     */
    public boolean validacao(T instance) {
        List<String> erros = new ArrayList<>();        
        validaCampo(erros, instance);       
        if (!erros.isEmpty()) {            
            adicionarMensagemErro(erros);
            return true;
        }        
        return false;
    }

    public boolean validaCampoUnico(T instance) {
        if (verificarCampoUnico(instance)) {
            jaExiste();
            return true;
        }
        return false;
    }

    public boolean verificarCampoUnico(T instance) {
        return getBO().unique(instance);
    }

    public boolean clausulaUnico() {
        return false;
    }

    public T getObjeto() {
        return objeto;
    }

    public void setObjeto(T objeto) {
        this.objeto = objeto;
    }

    public List<T> getLista() {
        return dadosPesquisa();
    }

    public void setLista(List<T> lista) {
        this.lista = lista;
    }

    public String getCampoPesquisar() {
        return campoPesquisar;
    }

    public void setCampoPesquisar(String campoPesquisar) {
        this.campoPesquisar = campoPesquisar;
    }

    public boolean isAlterando() {
        return alterando;
    }

    public void setAlterando(boolean alterando) {
        this.alterando = alterando;
    }

    public GenericBO<T> getBO() {
        return null;
    }   

    public String getTituloLayoutUnit() {
        return tituloLayoutUnit;
    }

    public void setTituloLayoutUnit(String tituloLayoutUnit) {
        this.tituloLayoutUnit = tituloLayoutUnit;
    }
    
    public String getIncluirAlterar(){
        return incluirAlterar();
    }
}