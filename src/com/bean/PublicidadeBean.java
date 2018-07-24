package com.bean;

import com.auxiliar.Document;
import com.bo.ImagemLoginBO;
import com.dto.ImagemLoginDTO;
import com.dto.PublicidadeDTO;
import com.util.FacesUtil;
import com.util.GoogleUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
 

@Named(value = "publicidadeBean")
@ViewScoped
public class PublicidadeBean  implements Serializable {
    
    private static final long serialVersionUID = 100005L;
    
    private PublicidadeDTO publicidateDTO;
    private TreeNode selectedNode;
    private TreeNode root;  
    private final String tituloLayoutUnit = "Diretório de imagens";
    private final String contextoLogin = "/.imagesTempfotosLogin";
    private final String contextoBanner = "";
    private String contexto = FacesUtil.getRealPath();
    //private BannerDTO objeto;
    private ImagemLoginDTO objeto = new ImagemLoginDTO();

    private Part file;
    
    public PublicidadeBean() {
        getNods();
        //getCaminho();
    }

    private void getNods(){
        root = new DefaultTreeNode("root", null);  
        TreeNode picturesLogin = new DefaultTreeNode(new Document("Imagens Login", "0 -", "Pasta"), getRoot());
        TreeNode picturesBanner = new DefaultTreeNode(new Document("Imagens Banner", "0 -", "Pasta"), getRoot());
        listarArquivos(picturesLogin,contextoLogin);
        //listarArquivos(picturesBanner,contextoBanner);
    }
    
    private void listarArquivos(TreeNode pictures, String contexto) {
       // System.err.println("NULLLLLLLLLLLLLLLLLLLLLLLLL@@!@@!@!"+FacesUtil.getExternalContext().getRealPath(""));
        File folder = new File(FacesUtil.getExternalContext().getRealPath("")+contextoLogin);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        for (ImagemLoginDTO imagem : ImagemLoginBO.getInstance().buscarImagensComDataFimVisualizacao()) {
            String builder = FacesUtil.getExternalContext().getRealPath(contextoLogin) + File.separator + imagem.getNomeFormato();
            criaArquivo(imagem.getArquivo(), builder);
            String extensao = imagem.getNomeFormato().substring(imagem.getNomeFormato().lastIndexOf("."));
            new DefaultTreeNode("picture", new Document(imagem.getNomeFormato(), 
                    String.valueOf((imagem.getArquivo().length / 1024)), extensao, contexto, imagem.getCodigo().intValue()), pictures);
        }
        
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
    
    public void excluirArquivo(){
        Document documento = (Document) selectedNode.getData();
        ImagemLoginDTO imagem = ImagemLoginBO.getInstance().findById(documento.getId());
        if (imagem.isPodeSerRemovida()){
            File arquivo = new File(new StringBuilder(contexto).append(contextoLogin).append(documento.getName()).toString());
            arquivo.delete();
            ImagemLoginBO.getInstance().remove(imagem);
            getNods();
            documento = null;
            selectedNode = null;
            FacesUtil.excluidoSucesso();
        } else {
           FacesUtil.adicionarMensagemAviso("Essa imagem não pode ser excluida");
        }
    }
            
    private void processaUpload() throws IOException{
        objeto.setArquivo(IOUtils.toByteArray(file.getInputStream()));
        objeto.setNomeFormato(file.getSubmittedFileName());
        objeto.setDataEnvio(new Date());
    }
    
    public String save() throws IOException {
        processaUpload();
        objeto.setLink(GoogleUtil.shorten(objeto.getLink()));
        ImagemLoginBO.getInstance().save(objeto);
        FacesUtil.inseridoSucesso();
        return "publicidadePesquisar";
    }
    
    public String novo(){
        return "publicidadePesquisar";
    }
    
    public String bannerIncluir(){
        return "bannerIncluir";
    }
    
    public TreeNode getRoot() {
        return root;
    }

    public String getTituloLayoutUnit() {
        return tituloLayoutUnit;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }
    public PublicidadeDTO getPublicidateDTO() {
        return publicidateDTO;
    }

    public void setContexto(String contexto) {
        this.contexto = contexto;
    }

    public void setPublicidateDTO(PublicidadeDTO publicidateDTO) {
        this.publicidateDTO = publicidateDTO;
    }

    public ImagemLoginDTO getObjeto() {
        return objeto;
    }

    public void setObjeto(ImagemLoginDTO objeto) {
        this.objeto = objeto;
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

}
