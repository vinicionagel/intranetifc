/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

/**
 * Classe com métodos utilitários para executar e abrir relatórios.
 *
 * @author David Buzatto
 */
public class ReportUtils{

    /**
     * Abre um relatório usando uma conexão como datasource.
     *
     * @param titulo Título usado na janela do relatório.
     * @param inputStream InputStream que contém o relatório.
     * @param parametros Parâmetros utilizados pelo relatório.
     * @param conexao Conexão utilizada para a execução da query.
     * @throws JRException Caso ocorra algum problema na execução do relatório
     */
    public static void openReportPDF(
            String titulo,
            InputStream inputStream,
            Map parametros,
            Connection conexao ) throws JRException, Exception {

        /*
         * Cria um JasperPrint, que é a versão preenchida do relatório,
         * usando uma conexão.
         */
        JasperPrint print = JasperFillManager.fillReport(
                inputStream, parametros, conexao );
        
        JRPdfExporter exp = new JRPdfExporter();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        exp.setParameter(JRExporterParameter.JASPER_PRINT, print);
        exp.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
        exp.exportReport();
        
        try {
			byte[] relatorioPdf = baos.toByteArray();
			HttpServletResponse response = FacesUtil.getResponse();
                        
			response.setContentType("application/pdf");

			
			response.setHeader("Content-disposition", "attachment;filename= "+titulo );
			response.getOutputStream().write(relatorioPdf);
			FacesUtil.getFacesContext().responseComplete();
		} catch (Exception e) {
			throw new Exception("Erro ao gerar o relatorio: " + e.getMessage());
		}
        
        
        // abre o JasperPrint em um JFrame
        
        //viewReportFrame( titulo, print );

    }
    
    public static void openReportXls(
            String titulo,
            InputStream inputStream,
            Map parametros,
            Connection conexao ) throws JRException, Exception {

        /*
         * Cria um JasperPrint, que é a versão preenchida do relatório,
         * usando uma conexão.
         */
        JasperPrint print = JasperFillManager.fillReport(
                inputStream, parametros, conexao );
        
        JRXlsExporter exp = new JRXlsExporter();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        exp.setParameter(JRXlsExporterParameter.JASPER_PRINT, print);
        exp.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos);
        exp.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.FALSE);  
        exp.setParameter(JRXlsExporterParameter.MAXIMUM_ROWS_PER_SHEET, 20000);
        exp.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);  
        exp.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE); 
        exp.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,Boolean.TRUE);
        
        exp.exportReport();
        
        try {
			byte[] relatorioPdf = baos.toByteArray();
			HttpServletResponse response = FacesUtil.getResponse();
                        
			response.setContentType("application/xls"); 

			
			response.setHeader("Content-disposition", "filename=Relatorio.xls");  
			response.getOutputStream().write(relatorioPdf);
			FacesUtil.getFacesContext().responseComplete();
		} catch (Exception e) {
			throw new Exception("Erro ao gerar o relatorio: " + e.getMessage());
		}
        
        
        // abre o JasperPrint em um JFrame
        
        //viewReportFrame( titulo, print );

    }

    /**
     * Abre um relatório usando um datasource genérico.
     *
     * @param titulo Título usado na janela do relatório.
     * @param inputStream InputStream que contém o relatório.
     * @param parametros Parâmetros utilizados pelo relatório.
     * @param dataSource Datasource a ser utilizado pelo relatório.
     * @throws JRException Caso ocorra algum problema na execução do relatório
     
    public static void openReport(
            String titulo,
            InputStream inputStream,
            Map parametros,
            JRDataSource dataSource ) throws JRException {

        /*
         * Cria um JasperPrint, que é a versão preenchida do relatório,
         * usando um datasource genérico.
         
        JasperPrint print = JasperFillManager.fillReport(
                inputStream, parametros, dataSource );

        // abre o JasperPrint em um JFrame
        viewReportFrame( titulo, print );

    }

    /**
     * Cria um JFrame para exibir o relatório representado pelo JasperPrint.
     *
     * @param titulo Título do JFrame.
     * @param print JasperPrint do relatório.
     
    private static void viewReportFrame( String titulo, JasperPrint print ) {

        /*
         * Cria um JRViewer para exibir o relatório.
         * Um JRViewer é uma JPanel.
         
        JRViewer viewer = new JRViewer( print );

        // cria o JFrame
        JFrame frameRelatorio = new JFrame( titulo );

        // adiciona o JRViewer no JFrame
        frameRelatorio.add( viewer, BorderLayout.CENTER );

        // configura o tamanho padrão do JFrame
        frameRelatorio.setSize( 500, 500 );

        // maximiza o JFrame para ocupar a tela toda.
        frameRelatorio.setExtendedState( JFrame.MAXIMIZED_BOTH );

        // configura a operação padrão quando o JFrame for fechado.
        frameRelatorio.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );

        // exibe o JFrame
        frameRelatorio.setVisible( true );

    }
 */
}