package com.bean;

import com.bo.ImagemLoginBO;
import com.dto.ImagemLoginDTO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;



@RequestScoped
@Named("imageSwitchBean")
public class ImageSwitchBean {

    private final static String CONTEXTO2 = "/.imagesTempfotosLogin";

    private void criarFotos() {
        StringBuilder builderCaminho = new StringBuilder();
        builderCaminho.append("/opt/intranet");
        builderCaminho.append(CONTEXTO2);
        String caminho = builderCaminho.toString();
        File folder = new File(caminho);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        if (getImages() != null){
            getImages().forEach(ima -> criaArquivo(ima.getArquivo(), caminho + 
                    File.separator + ima.getNomeFormato()));
        }
    }

    public ImageSwitchBean() {
        criarFotos();
    }
    
    private void criaArquivo(byte[] bytes, String arquivo) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(arquivo);
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }

    public List<ImagemLoginDTO> getImages() {
        return ImagemLoginBO.getInstance().buscarImagensComDataFimVisualizacao();
    }
}