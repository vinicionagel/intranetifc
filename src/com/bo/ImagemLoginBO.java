
package com.bo;

import com.core.GenericBO;
import com.dao.ImagemLoginDAO;
import com.dto.ImagemLoginDTO;
import java.util.List;

public class ImagemLoginBO extends GenericBO<ImagemLoginDTO>{
    
    private static final long serialVersionUID = 110L;
    private volatile static ImagemLoginBO uniqueInstance;

    public static ImagemLoginBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (ImagemLoginBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new ImagemLoginBO();
                }
            }
        }
        return uniqueInstance;
    }
    
    public ImagemLoginBO(){
        super(ImagemLoginDTO.class);
    }
    
    public List<ImagemLoginDTO> buscarImagensComDataFimVisualizacao(){
        return ImagemLoginDAO.getInstance().buscarImagensComDataFimVisualizacao();
    }
}
