package com.bo;

import com.core.GenericBO;
import com.dao.HoraDAO;
import com.dto.ChamadoDTO;
import com.dto.HoraDTO;
import com.dto.UsuarioDTO;
import java.util.Date;
import java.util.List;

public class HoraBO extends GenericBO<HoraDTO> {
        
    private static final long serialVersionUID = 100L;
    
    public HoraBO() {
        super(HoraDTO.class);
    }
    
    private volatile static HoraBO uniqueInstance;
    
    public static HoraBO getInstance(){
        if (uniqueInstance == null){
            synchronized (HoraBO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new HoraBO();
                }
            }
        }
        return uniqueInstance;
    }    
    
    
    HoraDAO dao = HoraDAO.getInstance();
    
    public float tempoGastoPorChamado(ChamadoDTO chamadoDTO) {
        try {
            return dao.tempoGastoPorChamado(chamadoDTO);
        } catch (NullPointerException e) {
            return 0;
        }        
    }
    
    public List<HoraDTO> pesquisarHorasTrabalhadasEntreDatas(Date dataInicial, Date dataFinal) {
        try {
            return dao.pesquisarHorasTrabalhadasEntreDatas(dataInicial,dataFinal);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<HoraDTO> horasPorChamado(ChamadoDTO chamadoDTO) {
        try {
            return dao.horasPorChamado(chamadoDTO);
        } catch (Exception e) {
            return null;
        }        
    }
    
    public float consultarHorasSeteDias(UsuarioDTO usuario)  {
        return dao.consultarHorasSeteDias(usuario);
    }
    
    public float consultarHorasMesAnterior(UsuarioDTO usuario)  {
        return dao.consultarHorasMesAnterior(usuario);
    }
    
    public float consultarHorasMesAtual(UsuarioDTO usuario)  {
        return dao.consultarHorasMesAtual(usuario);
    }
    
    public List<HoraDTO> listarHorasSemana(UsuarioDTO usuario)  {
        return dao.listarHorasSemana(usuario);
    }
}
