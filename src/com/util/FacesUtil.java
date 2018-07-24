/* Copyright 2006, 2007, 2008, 2009, 2010 do SETEC/MEC
 *
 * Este arquivo é parte do programa SigaEPT
 *
 * O SigaEPT é um software livre;  você pode redistribuí-lo e/ou modificá-lo dentro dos
 * termos da Licença Pública Geral GNU como publicada pela fundação do software livre (FSF);
 * na versão 2 da Licença.
 *
 * Este programa é distribuído na esperança que possa ser útil, mas SEM NENHUMA GARANTIA; sem
 * uma garantia implícita de ADEQUAÇÃO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. Veja Licença
 * Pública Geral GNU/GPL em português para maiores detalhes.
 *
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU, sob o título “LICENCA.txt”,
 * junto com este programa, se não, acesse o portal do Software Público Brasileiro no endereço
 * www.softwarepublico.gov.br ou escreva para Fundação do Software Livre (FSF) Inc.,51 Franklin
 * St, Fifth Floor, Boston, MA 02110-1301, USA
 */
package com.util;

import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * classe da arquitetura com metodos utilitarios para desenvolvimento em JSF
 *
 * @author grupo de desenvolvimento
 *
 */
public class FacesUtil {

    /**
     * retorna o path real completo do contexto atual.
     *
     * @return Exemplo: "C:\Glassfish\domains\domain1\WebApp\sigaept-edu-web"
     */
    public static String getRealPath() {
        return getRequest().getSession().getServletContext().getRealPath("");
    }
    /**
     * <p> retorna para a url mapeada no facesConfig </p>
     * @param url exemplo /pages/home/home.jsf
     */
    public static void redirect(String url){
        FacesContext fc = getFacesContext();
        fc.getApplication().getNavigationHandler().handleNavigation(fc, null, url);
    }
    /**
     * 
     * @return retorna exemplo /pages/home/index.xhtml <br/>
     */
    public static String retornaViewId(){
        return getFacesContext().getViewRoot().getViewId();
    }
    /**
     * 
     * @param parameterView identificador do parametro
     * @return o valor em string do que foi passado na URL
     */
    public static String retornaViaParameter(String parameterView){
        return getRequest().getParameter(parameterView);
    }
    /**
     * retorna o path completo do contexto da aplicação.
     *
     * @return Exemplo: "http://localhost:8080/siga-edu-web"
     */
    public static String getContextPath() {
        String contextPath = getRequest().getContextPath();
        String serverName = getRequest().getServerName();
        String serverPort = String.valueOf(getRequest().getServerPort());
        String fullCtxPath = "https://" + serverName + ":" + serverPort
                + contextPath;
        return fullCtxPath;
    }
    /**
     * método que retorna a CDU exemplo: http://localhost:8080/Intranet-IFC/pages/localizacao/localizacaoPesquisar.jsf
     * @return "localizacao";
     */
    public static String getCDU(){
        int i = 6;      
        String viewID = getFacesContext().getViewRoot().getViewId();
        while(viewID.charAt(++i) != '/'){}
        return viewID.substring(7, i);      
    }
    /**
     * retorna o objeto FacesContext atual do JSF
     *
     * @return
     */
    public static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    /**
     * retorna o ExternalContext do JSF
     *
     * @return
     */
    public static ExternalContext getExternalContext() {
        return getFacesContext().getExternalContext();
    }

    /**
     * retorna o request
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) getFacesContext().getExternalContext()
                .getRequest();
    }

    /**
     * retorna o response
     *
     * @return
     */
    public static HttpServletResponse getResponse() {
        return (HttpServletResponse) getFacesContext().getExternalContext()
                .getResponse();
    }

    /**
     * inclui/altera um atributo no request
     *
     * @param key
     * @param value
     */
    public static void setRequestAttribute(String key, Object value) {
        getRequest().setAttribute(key, value);
    }

    /**
     * obtem o valor de um atributo do request
     *
     * @param key
     * @return
     */
    public static Object getRequestAttribute(String key) {
        return getRequest().getAttribute(key);
    }

    /**
     * obtem o valor de um parametro do request
     *
     * @param key
     * @return
     */
    public static String getRequestParameter(String key) {
        return getRequest().getParameter(key);
    }

    /**
     * obtem o valor de um parametro de inicialização definido no WEB.XML
     *
     * @param key
     * @return
     */
    public static String getInitParameter(String key) {
        return getExternalContext().getInitParameter(key);
    }

    /**
     * inclui/altera um atributo no applicationMap
     *
     * @param key
     * @param value
     */
    public static void setApplicationAttribute(String key, Object value) {
        getExternalContext().getApplicationMap().put(key, value);
    }

    /**
     * obtem o valor de um atributo do applicationMap
     *
     * @param key
     * @return
     */
    public static Object getApplicationAttribute(String key) {
        return getExternalContext().getApplicationMap().get(key);
    }

    /**
     * inclui/altera um atributo no session
     *
     * @param key
     * @param value
     */
    public static void setSessionAttribute(String key, Object value) {
        getRequest().getSession().setAttribute(key, value);
    }

    /**
     *
     * Remover atributo na sessao
     *
     * @param key
     */
    public static void removeSessionAttribute(String key) {
        getRequest().getSession().removeAttribute(key);
    }

    /**
     * obtem o valor de um atributo do session
     *
     * @param key
     * @return
     */
    public static Object getSessionAttribute(String key) {
        return getRequest().getSession().getAttribute(key);
    }

    /**
     * obtem o HttpSession
     *
     * @param create
     *
     * @return
     */
    public static HttpSession getSession(boolean create) {
        return getRequest().getSession(create);
    }

    /**
     * gera o relatorio como PDF e o envia como resposta para o browser.
     *
     * @param arquivoJasper path completo do arquivo do relatorio compilado
     * .jasper
     * @param dadosRelatorio um List contendo os dados do relatorio
     * @param parametrosRelatorio parametros que o relatorio necessita
     * @param nomePdf
     * @throws NegocioException
     */
    /*@SuppressWarnings("unchecked")
     public static void gerarRelatorioPdf(String arquivoJasper, List dadosRelatorio, Map<String, Object> parametrosRelatorio, String nomePdf)
     throws Exception {
		
     try {

     //obtem o relatorio como PDF através da classe RelatorioJasper
     //passando o 'arquivo jasper', os 'dados' e os 'parametros' do relatorio (este ultimo
     //nao eh obrigatorio)
     byte[] relatorioPdf = RelatorioJasper.gerarPdf(arquivoJasper, dadosRelatorio, parametrosRelatorio);

     // obtem o objeto response
     HttpServletResponse response = getResponse();

     // Define No response o tipo do arquivo que será exibido no
     // navegador (PDF)
     response.setContentType("application/pdf");

     // Configura informações sobre o arquivo no response
     if (StringUtils.isEmpty(nomePdf)) {
     nomePdf = "relatorio.pdf";
     }
     response.setHeader("Content-disposition", "attachment;filename=" + nomePdf);

     // Envia os bytes do arquivo PDF, através de um fluxo de dados, para
     // o browser
     response.getOutputStream().write(relatorioPdf);

     // Avisa p/ o 'ciclo de vida JSF' que já terminamos de usar o
     // response,
     // logo, os demais passos do ciclo de vida serão executados até que
     // o
     // PDF seja exibido em tela, para o usuário
     getFacesContext().responseComplete();

     } catch (Exception e) {
     throw new NegocioException("Erro ao gerar o relatorio: "
     + e.getMessage());
     }

     }
	
     @SuppressWarnings("unchecked")
     public static void gerarRelatorioPdfJUNTAR(ArrayList<JasperPrint> jasperPrintList, String nomePdf)
     throws NegocioException {
     byte[] relatorioPdf;
		
     System.out.println(">>> chegou JUNTAR >>>>>>>>>> "+jasperPrintList.size());
		
 
     JasperPrint pages = new JasperPrint();
		 
		 
		 
		 
     //pages = jasperPrintList.get(0);
		 

     if (jasperPrintList.size() > 0){
     pages = jasperPrintList.get(0);
     int i = pages.getPages().size();
     boolean primeiraPagina = true;
     for (JasperPrint jp : jasperPrintList){
     if (primeiraPagina){
     primeiraPagina = false;
     } else {
     List<JRPrintPage> list = new  ArrayList<JRPrintPage>();
     list = jp.getPages();
     for (JRPrintPage jr : list){
     pages.addPage(i,jr);
     i++;
     }
     }
 
     }
     }
		 
		 
		 
     /*    synchronized (pages)  
     {  
     if (jasperPrintList.size()>1) {  
		   
     jasperPrint = jasperPrintList.get(jasperPrintList.size()-1);  
		                   
     List<JRPrintPage> list = jasperPrint.getPages();  
		   
     for (JRPrintPage jRPrintPage : list) {  
     pages.addPage(jRPrintPage);  
     }  
     }  
     } */
    /*	         JasperPrint jasperPrintView = pages;
		         
		
     try {

     //obtem o relatorio como PDF através da classe RelatorioJasper
     //passando o 'arquivo jasper', os 'dados' e os 'parametros' do relatorio (este ultimo
     //nao eh obrigatorio)

     //exporta o relatorio para PDF e retorna-o
     relatorioPdf = JasperExportManager.exportReportToPdf(jasperPrintView);

     // obtem o objeto response
     HttpServletResponse response = getResponse();

     // Define No response o tipo do arquivo que será exibido no
     // navegador (PDF)
     response.setContentType("application/pdf");

     // Configura informações sobre o arquivo no response
     if (StringUtils.isEmpty(nomePdf)) {
     nomePdf = "relatorio.pdf";
     }
     response.setHeader("Content-disposition", "attachment;filename=" + nomePdf);

     // Envia os bytes do arquivo PDF, através de um fluxo de dados, para
     // o browser
     response.getOutputStream().write(relatorioPdf);

     // Avisa p/ o 'ciclo de vida JSF' que já terminamos de usar o
     // response,
     // logo, os demais passos do ciclo de vida serão executados até que
     // o
     // PDF seja exibido em tela, para o usuário
     getFacesContext().responseComplete();

     } catch (Exception e) {
     throw new NegocioException("Erro ao gerar o relatorio: "
     + e.getMessage());
     }

     }*/
    /**
     * adiciona as mensagens de erro do NegocioException informado no contexto
     * do JSF
     *
     * @param e objeto Negocioxception
     */
    public static void adicionarMensagemErro(Exception e) {
        adicionarMensagemErro(e.getMessage());
    }

    /**
     * adiciona várias mensagens de erro no contexto do JSF
     *
     * @param msg mensagens que serão exibidas
     */
    public static void adicionarMensagemErro(List<String> msgs) {
        for (String msg : msgs) {
            adicionarMensagemErro(msg);
        }
    }

    /**
     * adiciona uma mensagem de erro no contexto do JSF
     *
     * @param msg mensagem a ser exibida
     */
    public static void adicionarMensagemErro(String msg) {
        adicionarMensagem(null, msg, FacesMessage.SEVERITY_ERROR);
    }

    /**
     * adiciona uma mensagem de erro no contexto do JSF para ser mostrado
     * referente a um componente
     *
     * @param msg mensagem a ser exibida
     * @param clientId id do componente referenciado
     */
    public static void adicionarMensagemErro(String clientId, String msg) {
        adicionarMensagem(clientId, msg, FacesMessage.SEVERITY_ERROR);
    }

    /**
     * adiciona as mensagens de aviso do NegocioException informado no contexto
     * do JSF
     *
     * @param e objeto Negocioxception
     */
    public static void adicionarMensagemAviso(Exception e) {
        adicionarMensagemAviso(e.getMessage());
    }

    /**
     * adiciona várias mensagens de aviso no contexto do JSF
     *
     * @param msg mensagens que serão exibidas
     */
    public static void adicionarMensagemAviso(List<String> msgs) {
        for (String msg : msgs) {
            adicionarMensagemAviso(msg);
        }
    }

    /**
     * adiciona uma mensagem de aviso no contexto do JSF
     *
     * @param msg mensagem a ser exibida
     */
    public static void adicionarMensagemAviso(String msg) {
        adicionarMensagem(null, msg, FacesMessage.SEVERITY_INFO);
    }
    

    /**
     * adiciona uma mensagem de aviso no contexto do JSF para ser mostrado
     * referente a um componente
     *
     * @param msg mensagem a ser exibida
     * @param clientId id do componente referenciado
     */
    public static void adicionarMensagemAviso(String clientId, String msg) {
        adicionarMensagem(clientId, msg, FacesMessage.SEVERITY_INFO);
    }

    public static void adicionarMensagemWarning(Exception e) {
        adicionarMensagemWarning(e.getMessage());
    }

    public static void adicionarMensagemWarning(List<String> msgs) {
        for (String msg : msgs) {
            adicionarMensagemWarning(msg);
        }
    }

    public static void adicionarMensagemWarning(String msg) {
        adicionarMensagem(null, msg, FacesMessage.SEVERITY_WARN);
    }

    /**
     *
     * adiciona uma mensagem no contexto do JSF
     *
     * @param clientId id do campo ao qual se refere a mensagem
     * @param msg mensagem a ser exibida
     * @param severity tipo da mensagem: ERRO, ALERT etc
     */
    public static void adicionarMensagem(String clientId, String msg, Severity severity) {
        FacesUtil.getFacesContext().addMessage(clientId, new FacesMessage(severity, msg, null));
    }
    
    public static void inseridoSucesso(){
        adicionarMensagemAviso("Inserido com Sucesso");
    }
    
    public static void editadoSucesso(){
        adicionarMensagemAviso("Editado com Sucesso");
    }
    
    public static void excluidoSucesso(){
        adicionarMensagemAviso("Excluído com Sucesso");
    }
    
    public static void jaExiste(){
        adicionarMensagemWarning("Este registro já existe.");
    }
}