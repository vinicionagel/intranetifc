package com.bean;

import com.auxiliar.Contexto;
import com.bo.AlunoBO;
import com.bo.AndamentoOcorrenciaBO;
import com.bo.OcorrenciaBO;
import com.core.GenericBean;
import com.dto.AlunoDTO;
import com.dto.AndamentoOcorrenciaDTO;
import com.dto.OcorrenciaDTO;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import static com.util.FacesUtil.setSessionAttribute;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@Named(value = "relatorioOcorrenciaBean")
@ViewScoped
public class RelatorioOcorrenciaBean extends GenericBean<OcorrenciaDTO> implements Serializable {
    
    private AlunoDTO aluno;
    private ContextoBean contextoBean;
    private Long codigoPesquisa;
    private Part file;
    private Date dataInicial;
    private Date dataFinal;
    private Date dataAtual;
    
    public RelatorioOcorrenciaBean() {
        dataFinal =  new Date();
        Calendar calendar =  Calendar.getInstance();
        calendar.setTime(dataFinal);
        calendar.add(Calendar.MONTH, -1);
        dataInicial = calendar.getTime();
        dataAtual =  new Date();
        tituloLayoutUnit = "Relatório Ocorrência";
    }
    
    @Override
    public void validaCampo(List<String> erros, OcorrenciaDTO instance) {
    }

    public Long getCodigoPesquisa() {
        return codigoPesquisa;
    }

    public void setCodigoPesquisa(Long codigoPesquisa) throws Throwable {
        OcorrenciaDTO ocorrenciaDTOAux = OcorrenciaBO.getInstance().findById(codigoPesquisa);
        Contexto c = ContextoBean.getContexto();
        setSessionAttribute("ocorrenciaDTO", ocorrenciaDTOAux);            
        c.setCampusAtual(ocorrenciaDTOAux.getSetorDTOAutor().getCampusDTO());
        setSessionAttribute("contexto", c);
    }
    
    /**
     * 
     * @param texto Texto da Coluna
     * @param fonte Tamanho da Fonte
     * @param linha Numero de Linhas
     * @param coluna Numero de Colunas
     * @param posicao Alinhamento do Texto
     * @return 
     */
    private PdfPCell retornaColuna(String texto, int fonte, int linha, int coluna, int posicao){
        Paragraph pr = new Paragraph(new Phrase(texto, FontFactory.getFont(FontFactory.HELVETICA, fonte, Font.NORMAL)));
        Phrase p =  new Phrase();
        PdfPCell col = new PdfPCell(pr);
        col.setRowspan(linha);
        col.setColspan(coluna);
        col.setHorizontalAlignment(posicao); 
        return col;
    }
    
    /**
     * 
     * @param texto Texto da Coluna
     * @param fonte Tamanho da Fonte
     * @param linha Numero de Linhas
     * @param coluna Numero de Colunas
     * @param color Cor da Coluna
     * @return 
     */
    private PdfPCell retornaHeader(String texto, int fonte, int linha, int coluna, Color color){
        Paragraph pr = new Paragraph(new Phrase(texto, FontFactory.getFont(FontFactory.HELVETICA, fonte, Font.BOLD)));
        PdfPCell col = new PdfPCell(pr);
        col.setRowspan(linha);
        col.setColspan(coluna);
        col.setHorizontalAlignment(Element.ALIGN_CENTER);
        col.setBackgroundColor(color);
        return col;
    }
    
    private static String removeHTML(String html){  
        html = html.replaceAll("<br/>", "\n");
        String noTagRegex = "<[^>]+>";  
        return html.replaceAll(noTagRegex, "").trim();  
    }  
    
    public void gerarRelatorio(){
        if(aluno != null){
        //Pega o contexto e cria a configuração do PDF
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context
                        .getExternalContext().getResponse();
        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "attachment; filename=Ocorrencia"+ aluno.getNome() +".pdf");
        try {
            //realiza a criação do Documento em HTML para posteriormente ser impresso em PDF 
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            document.open();

            Paragraph dataInfo = new Paragraph(new Phrase("Ministério da Educação",
            FontFactory.getFont(FontFactory.HELVETICA, 8)));
            dataInfo.setAlignment(Element.ALIGN_CENTER);
            document.add(dataInfo);
            dataInfo = new Paragraph(new Phrase("Secretaria de Educação Profissional e Tecnológica",
                            FontFactory.getFont(FontFactory.HELVETICA, 8)));
            dataInfo.setAlignment(Element.ALIGN_CENTER);
            document.add(dataInfo);
            dataInfo = new Paragraph(new Phrase("Instituto Federal de Educação, Ciência e Tecnologia Catarinense",
                            FontFactory.getFont(FontFactory.HELVETICA, 8)));
            dataInfo.setAlignment(Element.ALIGN_CENTER);
            document.add(dataInfo);

            Paragraph p = new Paragraph();
            p.setSpacingAfter(20);
            document.add(p);

            //Cria um paragrafo e adiciona no PDF
            dataInfo = new Paragraph(new Phrase("Relatório de Ocorrências do Aluno",
                            FontFactory.getFont(FontFactory.HELVETICA, 16)));
            dataInfo.setAlignment(Element.ALIGN_CENTER);
            document.add(dataInfo);

            document.add(new Phrase("Aluno: " + aluno.getNome()+ "\n", FontFactory.getFont(FontFactory.HELVETICA, 10)));
            SimpleDateFormat dateFormatador = new SimpleDateFormat("dd/MM/yyyy");
            document.add(new Phrase("Data de Nascimento: " + dateFormatador.format(aluno.getDataNascimento())+ "\n", FontFactory.getFont(FontFactory.HELVETICA, 10)));
            
            Date data = new Date();
            SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            document.add(new Phrase("Data Emissão: " + formatador.format(data)+ "\n", FontFactory.getFont(FontFactory.HELVETICA, 10)));

            
            p = new Paragraph();
            p.setSpacingAfter(20);
            document.add(p);

            //Informna o tamanho das celulas da tabela, neste caso como o array possui duas informações de tamanho esta tabela possui duas celulas
            float[] tamanho = {0.6f, 0.7f, 0.4f, 0.4f};
            List<OcorrenciaDTO> ocorrencias = OcorrenciaBO.getInstance().pesquisarPorAlunoIntervalo(aluno, dataInicial, dataFinal);
            for(OcorrenciaDTO ocorrencia : ocorrencias){
                //Cria uma Tabela no documento PDF
                PdfPTable table = new PdfPTable(tamanho);
                
                //Cria uma Celula para a tabela
                //Header
                table.addCell(retornaHeader("Ocorrencia",8,1,6, Color.GRAY));
                table.addCell(retornaColuna("Titulo: " + ocorrencia.getTitulo(),8,1,4, Element.ALIGN_LEFT));
                table.addCell(retornaColuna("Descricao: " +ocorrencia.getDescricao(),8,1,4, Element.ALIGN_LEFT));
                table.addCell(retornaHeader("Data Abertura",8,1,1,Color.WHITE));
                table.addCell(retornaHeader("Data Fechamento",8,1,1,Color.WHITE));
                table.addCell(retornaHeader("Tipo Ocorrencia",8,1,1,Color.WHITE));
                table.addCell(retornaHeader("Acao Disciplinar",8,1,1,Color.WHITE));
                table.addCell(retornaColuna(formatador.format(ocorrencia.getDataHoraAbertura()),8,1,1,Element.ALIGN_CENTER));
                if(ocorrencia.getDataHoraFechamento() !=  null){
                    table.addCell(retornaColuna(formatador.format(ocorrencia.getDataHoraFechamento()),8,1,1,Element.ALIGN_CENTER));
                }else{
                    table.addCell(retornaColuna(" ",8,1,1,Element.ALIGN_CENTER));
                }
                table.addCell(retornaColuna(ocorrencia.getAcaoDisciplinarDTO().getTipoOcorrenciaDTO().getDescricao(),8,1,1,Element.ALIGN_CENTER));
                table.addCell(retornaColuna(ocorrencia.getAcaoDisciplinarDTO().getDescricao(),8,1,1,Element.ALIGN_CENTER));
                
                //Header
                table.addCell(retornaHeader("Andamentos",8,1,4,Color.GRAY));
                table.addCell(retornaHeader("Andamento",8,1,1,Color.WHITE));
                table.addCell(retornaHeader("Log",8,1,1,Color.WHITE));
                table.addCell(retornaHeader("Data",8,1,1,Color.WHITE));
                table.addCell(retornaHeader("Usuario",8,1,1,Color.WHITE));
                
                //Andamentos
                for(AndamentoOcorrenciaDTO andamento : AndamentoOcorrenciaBO.getInstance().pesquisarAndamentosDaOcorrencia(ocorrencia)){
                    table.addCell(retornaColuna(andamento.getDescricao(),8,1,1,Element.ALIGN_CENTER));
                    table.addCell(retornaColuna(andamento.getLog(),8,1,1,Element.ALIGN_CENTER));
                    table.addCell(retornaColuna(formatador.format(andamento.getDataOcorrencia()),8,1,1,Element.ALIGN_CENTER));
                    table.addCell(retornaColuna(andamento.getUsuarioDTO().getNome(),8,1,1,Element.ALIGN_CENTER));
                }

                //Seta o tamanho da tabela e adiciona no documento
                table.setWidthPercentage(100);
                document.add(table);
                
                document.add(new Phrase("\n", FontFactory.getFont(FontFactory.HELVETICA, 10)));
            }
            //Realiza a configuração do documento PDF e fecha todos os fluxos utilizados na criação
            document.close();
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            java.io.OutputStream os = response.getOutputStream();
            baos.writeTo(os);
            os.flush();
            os.close();				
        } catch (DocumentException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        } catch (Exception ex) {
                ex.printStackTrace();
        }
        context.responseComplete();	
        }
    }
    
    public ContextoBean getContextoBean() {
        return contextoBean;
    }

    public void setContextoBean(ContextoBean contextoBean) {
        this.contextoBean = contextoBean;
    }
        
    @Override
    public String incluirAlterar() {
        return "ocorrenciaIncluirAlterar";
    }

    @Override
    public String pesquisar() {
        return "ocorrenciaPesquisar";
    }

    @Override
    public String novo() {        
        this.setAlterando(false);
        return pesquisar();
    }

    @Override
    public List<OcorrenciaDTO> dadosPesquisa() {
        return new ArrayList<>();
    }
    
    public List<AlunoDTO> alunos(String texto){
        return AlunoBO.getInstance().pesquisarAlunoPorNome(texto.toUpperCase(),10);
    }
    
    public AlunoDTO getAluno() {
        return aluno;
    }

    public void setAluno(AlunoDTO aluno) {
        this.aluno = aluno;
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public Date getDataAtual() {
        return dataAtual;
    }

    public void setDataAtual(Date dataAtual) {
        this.dataAtual = dataAtual;
    }
}
