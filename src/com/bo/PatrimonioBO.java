package com.bo;

import com.auxiliar.Contexto;
import com.core.GenericBO;
import com.dao.PatrimonioDAO;
import com.dto.*;
import java.util.List;

public class PatrimonioBO extends GenericBO<PatrimonioDTO> {

    private static final long serialVersionUID = 113L;
    private PatrimonioDAO dao = PatrimonioDAO.getInstance();

    private volatile static PatrimonioBO uniqueInstance;

    public static PatrimonioBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (PatrimonioBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new PatrimonioBO();
                }
            }
        }
        return uniqueInstance;
    }
    
    public PatrimonioBO() {
        super(PatrimonioDTO.class);
    }

    @Override
    public boolean unique(PatrimonioDTO patrimonioDTO) {
        try{
            return dao.unique(patrimonioDTO);
        } catch (Throwable e) {
            return false;
        }
    }

    public List<PatrimonioDTO> pesquisarNome(PatrimonioDTO patrimonioDTO) {
        try{
            return dao.pesquisarNome(patrimonioDTO);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<PatrimonioDTO> pesquisarNomeECampus(PatrimonioDTO patrimonioDTO, CampusDTO campusDTO) {
        try{
            return dao.pesquisarNomeECampus(patrimonioDTO, campusDTO);
        } catch (Exception e) {
            return null;
        }
    }

    public List<PatrimonioDTO> pesquisarPorLocalizacao(LocalizacaoDTO localizacaoDTO) {
        try{
            return dao.pesquisarPorLocalizacao(localizacaoDTO);
        } catch (Exception e) {
            return null;
        }
    }

    public List<PatrimonioDTO> pesquisarSemBaixaPorLocalizacao(LocalizacaoDTO localizacaoDTO) {
        try{
            return dao.pesquisarSemBaixaPorLocalizacao(localizacaoDTO);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<PatrimonioDTO> pesquisarPatrimonioPorSetoresDoUsuario(UsuarioDTO usuario)  {
        try{
            return dao.pesquisarPatrimonioPorSetoresDoUsuario(usuario);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<PatrimonioDTO> pesquisarPatrimonioPorSetores(String setores)  {
        try{
            return dao.pesquisarPatrimonioPorSetores(setores);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<PatrimonioDTO> pesquisarPatrimonioPorSetoresDoUsuarioOrdenadoPorCampusELocalizacao(UsuarioDTO usuario) {
        try{
            return dao.pesquisarPatrimonioPorSetoresDoUsuarioOrdenadoPorCampusELocalizacao(usuario);
        }catch(Exception e){
            return null;
        }
    }
    
    public List<PatrimonioDTO> pesquisarPatrimonioSemBaixaPorSetoresDoUsuarioOrdenadoPorCampusELocalizacao(Contexto contexto) {
        try{
            return dao.pesquisarPatrimonioSemBaixaPorSetoresDoUsuarioOrdenadoPorCampusELocalizacao(contexto);
        }catch(Exception e){
            return null;
        }
    }
    
    public List<PatrimonioDTO> pesquisarPatrimoniosPeloFiltro(PatrimonioDTO patrimonio, CampusDTO campus){
        try{
            return dao.pesquisarPatrimoniosPeloFiltro(patrimonio, campus);
        }catch(Exception e){
            return null;
        }
    }
    
    @Override
    public List<PatrimonioDTO> findAll() {
        try{
            return dao.findAll();
        } catch (Exception e) {
            return null;
        }
    }
}