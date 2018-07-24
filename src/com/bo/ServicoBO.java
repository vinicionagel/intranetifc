package com.bo;

import com.core.GenericBO;
import com.dao.ServicoDAO;
import com.dto.CampusDTO;
import com.dto.ServicoDTO;
import com.dto.TipoServicoDTO;
import com.dto.UsuarioDTO;
import java.util.List;

public class ServicoBO extends GenericBO<ServicoDTO>{
    
    private static final long serialVersionUID = 119L;
    private ServicoDAO dao = ServicoDAO.getInstance();
    
    private volatile static ServicoBO uniqueInstance;

    public static ServicoBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (ServicoBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new ServicoBO();
                }
            }
        }
        return uniqueInstance;
    }
    
    public ServicoBO(){
        super(ServicoDTO.class);
    }
    
    public List<ServicoDTO> pesquisarNome(ServicoDTO servicoDTO) {
        try{
            return dao.pesquisarNome(servicoDTO);
        }catch(Exception e){
            return null;
        }
    }
    
    public List<ServicoDTO> pesquisarNomeECampus(String servicoDTO, CampusDTO campusDTO) {
        try{
            return dao.pesquisarNomeECampus(servicoDTO, campusDTO);
        }catch(Exception e){
            return null;
        }
    }
    
    public List<ServicoDTO> pesquisarPorTipoServico(TipoServicoDTO tipoServicoDTO)  {
        try {
            return dao.pesquisarPorTipoServico(tipoServicoDTO);
        } catch (Exception e) {
            return null;
        }                
    }
    
    public ServicoDTO pesquisarPorTipoServicoEDescricao(TipoServicoDTO tipoServicoDTO, ServicoDTO servico)  {
        try {
            return dao.pesquisarPorTipoServicoEDescricao(tipoServicoDTO, servico);
        } catch (Exception e) {
            return null;
        }                
    }
    
    @Override
    public boolean unique(ServicoDTO ServicoDTO) {
        try{
            return dao.unique(ServicoDTO);
        } catch (Throwable e) {
            return false;
        }
    }
        
    public List<ServicoDTO> pesquisarServicoPorSetoresDoUsuario(UsuarioDTO usuario) {
        try{
            return dao.pesquisarServicoPorSetoresDoUsuario(usuario);
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override 
    public List<ServicoDTO> findAll(){
        try{
            return dao.findAll();
        } catch (Exception e) {
            return null;
        }
    }
}