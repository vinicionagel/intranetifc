package com.dao;

import com.core.GenericDAO;
import com.dto.AlunoDTO;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class AlunoDAO extends GenericDAO<AlunoDAO>{
    
    private static final long serialVersionUID = 20010L;   
    private volatile static AlunoDAO uniqueInstance;
    static final Logger logger = Logger.getLogger(AlunoDAO.class.getName());
    
    public static AlunoDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (AlunoDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new AlunoDAO();
                }
            }
        }
        return uniqueInstance;
    } 
    
    public AlunoDAO() {
        super(AlunoDAO.class);
    }
    
    public AlunoDTO pesquisarPorIdSiga(int idSiga) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM AlunoDTO aluno WHERE aluno.idSiga = :id");
            return (AlunoDTO) em.createQuery(sql.toString()).setParameter("id", idSiga).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<AlunoDTO> pesquisarAlunoPorNome(String aluno, int maxResult) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM AlunoDTO aluno WHERE aluno.nome LIKE '%");
            sql.append(aluno);
            sql.append("%'");
            return em.createQuery(sql.toString()).setMaxResults(maxResult).getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}
