package com.bo;

import com.core.GenericBO;
import com.dao.OcorrenciaDAO;
import com.dto.AlunoDTO;
import com.dto.OcorrenciaDTO;
import com.dto.SetorDTO;
import java.util.Date;
import java.util.List;

public class OcorrenciaBO extends GenericBO<OcorrenciaDTO>{
    
    private static final long serialVersionUID = 30013L;
    private OcorrenciaDAO dao = OcorrenciaDAO.getInstance();

    private volatile static OcorrenciaBO uniqueInstance;

    public static OcorrenciaBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (OcorrenciaBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new OcorrenciaBO();
                }
            }
        }
        return uniqueInstance;
    }

    
    public OcorrenciaBO() {
        super(OcorrenciaDTO.class);
    }

    public List<OcorrenciaDTO> pesquisarPorAluno(AlunoDTO alunoDTO)  {
        return dao.pesquisarPorAluno(alunoDTO);
    }
    
    public List<OcorrenciaDTO> pesquisarPorAlunoIntervalo(AlunoDTO alunoDTO, Date dataInicial, Date dataFinal)  {
        return dao.pesquisarPorAlunoIntervalo(alunoDTO, dataInicial, dataFinal);
    }
    
    public List<OcorrenciaDTO> pesquisarPorAlunoEAtribuidosParaSetor(AlunoDTO alunoDTO, List<SetorDTO> listaSetorDTO)  {
        return dao.pesquisarPorAlunoEAtribuidosParaSetor(alunoDTO, listaSetorDTO);
    }
    
    public List<OcorrenciaDTO> pesquisarAtribuidosParaSetor(List<SetorDTO> listaSetorDTO){
        return dao.pesquisarAtribuidosParaSetor(listaSetorDTO);
    }
    
    public List<OcorrenciaDTO> pesquisarObservadas(List<SetorDTO> listaSetorDTO)  {
         return dao.pesquisarObservadas(listaSetorDTO);
    }
    
    public List<OcorrenciaDTO> pesquisarObservadasPorAluno(AlunoDTO alunoDTO, List<SetorDTO> listaSetorDTO)  {
         return dao.pesquisarObservadasPorAluno(alunoDTO, listaSetorDTO);
    }
}
