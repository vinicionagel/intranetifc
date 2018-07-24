package com.dao;

import com.core.GenericDAO;
import static com.dao.InterfaceDAO.logger;
import com.dto.ImagemLoginDTO;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;

public class ImagemLoginDAO extends GenericDAO<ImagemLoginDTO> {
    
    private static final long serialVersionUID = 1010L;
    
    private volatile static ImagemLoginDAO uniqueInstance;

    public static ImagemLoginDAO getInstance() {
        if (uniqueInstance == null) {
            synchronized (ImagemLoginDAO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new ImagemLoginDAO();
                }
            }
        }
        return uniqueInstance;
    }  
    
    public ImagemLoginDAO() {
        super(ImagemLoginDTO.class);
    }    
    
    public List<ImagemLoginDTO> buscarImagensComDataFimVisualizacao() {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM ImagemLoginDTO i WHERE (i.dataFimVisualizacao >= CURRENT_DATE OR i.dataFimVisualizacao IS NULL)");
            return em.createQuery(sql.toString()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
}