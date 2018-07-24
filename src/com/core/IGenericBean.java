package com.core;

import java.util.List;

public interface IGenericBean<T> {
    
    public void validaCampo(List<String> erros, T instance);

    public String save() throws Throwable;

    public String update() throws Throwable;

    public String include();

    public void remove() throws Throwable;        

    public String incluirAlterar();

    public String pesquisar();   
    
    public String novo() throws Throwable;    
    
    public List<T> dadosPesquisa();
}