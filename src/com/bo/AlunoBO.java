package com.bo;

import com.core.GenericBO;
import com.dao.AlunoDAO;
import com.dto.AlunoDTO;
import java.util.List;


public class AlunoBO extends GenericBO<AlunoDTO>{
    
    private static final long serialVersionUID = 30010L;
    private AlunoDAO dao = AlunoDAO.getInstance();

    private volatile static AlunoBO uniqueInstance;

    public static AlunoBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (AlunoBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new AlunoBO();
                }
            }
        }
        return uniqueInstance;
    }

    public AlunoBO() {
        super(AlunoDTO.class);
    }
    
    public AlunoDTO pesquisarPorIdSiga(int idSiga) {
        return dao.pesquisarPorIdSiga(idSiga);
    }
    
    public List<AlunoDTO> pesquisarAlunoPorNome(String aluno, int maxResult) {
        return dao.pesquisarAlunoPorNome(aluno, maxResult);
    }
}
