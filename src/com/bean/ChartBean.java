package com.bean;

import com.bo.ChamadoInfraBO;
import com.bo.ChamadoServicoBO;
import com.bo.SetorBO;
import com.bo.StatusBO;
import com.dto.ChamadoInfraDTO;
import com.dto.ChamadoServicoDTO;
import com.dto.SetorDTO;
import com.dto.StatusDTO;
import com.util.DataUtil;
import com.util.FacesUtil;
import com.util.SelectBoxUtil;
import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.NavigationHandler;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.PieChartModel;

@Named(value="chartBean")
@ViewScoped
public class ChartBean implements Serializable  {    
    
    private static final long serialVersionUID = 100004L;
    
    private SetorBO setorBO = SetorBO.getInstance();        
    private ChamadoInfraBO chamadoInfraBO = ChamadoInfraBO.getInstance();
    private ChamadoServicoBO chamadoServicoBO = ChamadoServicoBO.getInstance();
    private StatusBO statusBO = StatusBO.getInstance();
    private Long setorSelectItem = null;
    
    private SetorDTO setorDTO;        
    private StatusDTO statusDTO;         
            
    private String tituloTodosStatus;
    private String tituloStatusInfra;
    private String tituloStatusServico;
    private String tituloTodosChamados;
    private String tituloChamadosInfra;
    private String tituloChamadosServico;
    
    private PieChartModel todosChamados = new PieChartModel();
    private PieChartModel chamadosInfra = new PieChartModel();
    private PieChartModel chamadosServico = new PieChartModel();
    
    private PieChartModel todosStatus = new PieChartModel();
    private PieChartModel statusInfra = new PieChartModel();
    private PieChartModel statusServico = new PieChartModel();

    private final String tituloLayoutUnit = "Gráficos";
    private final String POR_SETOR = "PorSetor";
    private final String POR_STATUS = "PorStatus";
    
    private String graficos;
    
    private Date dataInicial = DataUtil.dataAtualMenos30Dias();
    private Date dataFinal = new Date();
    private String mes;
    
    private int opcao = 2;
    
    public ChartBean() {
        setorSelectItem = 0l;                
    }                          
    
    public void gerarGraficosPorSetor(){        
        if (setorSelectItem != null){
            try {
                setSetorDTO(getSetorBO().findById(setorSelectItem));
            } catch (Throwable ex) {
                Logger.getLogger(ChartBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(getOpcao() == 1) {
            setarDatasAPartirDoMes();
        }
        getTodosStatus();
        getStatusInfra();
        getStatusServico();                
    }
    
    public void gerarGraficosPorStatus(ActionEvent evt){
        if(getOpcao() == 1) {
            setarDatasAPartirDoMes();
        }
        getTodosStatus();       
    }
    
    public void itemSelectPorSetor(ItemSelectEvent event) {                            
        String a = getTodosChamados().getData().entrySet().toArray()[event.getItemIndex()].toString(); 
        StatusDTO status = new StatusDTO();        
        try {
            status = (StatusDTO) getStatusBO().pesquisarDescricao(a.substring(0, a.indexOf("=")));
        } catch (Throwable ex) {
            
        }
        
        FacesUtil.setSessionAttribute("statusGrafico", status);
        FacesUtil.setSessionAttribute("setorGrafico", getSetorDTO());   
        FacesUtil.setSessionAttribute("dataInicial", getDataInicial());
        FacesUtil.setSessionAttribute("dataFinal", getDataFinal());                        
        
        NavigationHandler handler = FacesUtil.getFacesContext().getApplication().getNavigationHandler();
        handler.handleNavigation(FacesUtil.getFacesContext(), null, "chamadoPesquisar");
    }  
    
    public void itemSelectPorStatus(ItemSelectEvent event) {                            
        String a = getTodosStatus().getData().entrySet().toArray()[event.getItemIndex()].toString(); 
        SetorDTO setor = new SetorDTO();        
        try {
            setor = (SetorDTO) getSetorBO().pesquisarDescricao(a.substring(0, a.indexOf("=")));
        } catch (Throwable ex) {
            
        }        
        FacesUtil.setSessionAttribute("statusGrafico", getStatusDTO());
        FacesUtil.setSessionAttribute("setorGrafico", setor);   
        FacesUtil.setSessionAttribute("dataInicial", getDataInicial());
        FacesUtil.setSessionAttribute("dataFinal", getDataFinal());                
        
        NavigationHandler handler = FacesUtil.getFacesContext().getApplication().getNavigationHandler();
        handler.handleNavigation(FacesUtil.getFacesContext(), null, "chamadoPesquisar");
    }  
    
    public void setarDatasAPartirDoMes(){        
        Calendar cal = Calendar.getInstance();        
        String[] mesAno = this.mes.split(" / ");        
        int mesInt = 0;
        DateFormatSymbols dfs = new DateFormatSymbols(new Locale("pt", "BR"));
        
        for(int i = 0; i < dfs.getMonths().length; i++){
            if(dfs.getMonths()[i].equals(mesAno[0])){
                mesInt = i;
                break;
            }
        }
        
        cal.set(Integer.parseInt(mesAno[1]), mesInt, 01);
        dataInicial = cal.getTime();        
        
        cal.set(Calendar.MONTH, mesInt);                       
        
        cal.set(Integer.parseInt(mesAno[1]), mesInt, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        dataFinal = cal.getTime();
    }
    
    public PieChartModel getTodosChamados(){    
        try {                                    
            todosChamados.clear();            
            if (getSetorDTO() != null) {
                boolean existeChamadosI = getChamadoInfraBO().verificarSeExisteChamadoSetor(getSetorDTO(), getDataInicial(), getDataFinal());                 
                boolean existeChamadosS = getChamadoServicoBO().verificarSeExisteChamadoSetor(getSetorDTO(), getDataInicial(), getDataFinal());                 
                if (existeChamadosI || existeChamadosS) {
                    setTituloTodosChamados("Gráfico de todos os chamados de " + getSetorDTO().getDescricao());
                    for (StatusDTO s : getListaTodosStatus()) {                        
                        List<ChamadoInfraDTO> chamadosI = getChamadoInfraBO().pesquisarChamadosPorStatusPorSetor(s, getSetorDTO(), getDataInicial(), getDataFinal());
                        List<ChamadoServicoDTO> chamadosS = getChamadoServicoBO().pesquisarChamadosPorStatusPorSetor(s, getSetorDTO(), getDataInicial(), getDataFinal());
                        if (!chamadosI.isEmpty() || !chamadosS.isEmpty()) {
                            todosChamados.getData().put(s.getDescricao(), (chamadosI.size()+chamadosS.size()));
                        }                        
                    }                    
                } else {                    
                    setTituloTodosChamados("Este setor não tem chamados");
                    todosChamados.getData().put("", null);                    
                }
            } else {                
                setTituloTodosChamados("Selecione um setor");
                todosChamados.getData().put("", null);
            }
            return todosChamados;
        } catch (Throwable throwable) {
             setSetorDTO(null);             
        }
        return null;
    }

    public PieChartModel getChamadosInfra(){    
        try {
            chamadosInfra.clear();            
            if (getSetorDTO() != null) {
                boolean existeChamados = getChamadoInfraBO().verificarSeExisteChamadoSetor(getSetorDTO(), getDataInicial(), getDataFinal());
                if (existeChamados) {
                    setTituloChamadosInfra("Gráfico dos chamados de Infra-Estrutura de " + getSetorDTO().getDescricao());
                    for (StatusDTO s : getListaTodosStatus()) {
                        List<ChamadoInfraDTO> chamados = getChamadoInfraBO().pesquisarChamadosPorStatusPorSetor(s, getSetorDTO(), getDataInicial(), getDataFinal());
                        if (!chamados.isEmpty()) {
                            chamadosInfra.getData().put(s.getDescricao(), chamados.size());
                        }
                    }
                } else {
                    setTituloChamadosInfra("Este setor não tem chamados");
                    chamadosInfra.getData().put("", null);                    
                }
            } else {                
                setTituloChamadosInfra("Selecione um setor");
                chamadosInfra.getData().put("", null);
            }
            return chamadosInfra;
        } catch (Throwable throwable) {
             setSetorDTO(null);             
        }
        return null;
    }
    
    public PieChartModel getChamadosServico(){    
        try {
            chamadosServico.clear();            
            if (getSetorDTO() != null) {
                boolean existeChamados = getChamadoServicoBO().verificarSeExisteChamadoSetor(getSetorDTO(), getDataInicial(), getDataFinal());                 
                if (existeChamados) {
                    setTituloChamadosServico("Gráfico dos chamados de Serviço de " + getSetorDTO().getDescricao());
                    for (StatusDTO s : getListaTodosStatus()) {                                                
                        List<ChamadoServicoDTO> chamados = getChamadoServicoBO().pesquisarChamadosPorStatusPorSetor(s, getSetorDTO(), getDataInicial(), getDataFinal());
                        if (!chamados.isEmpty()) {
                            chamadosServico.getData().put(s.getDescricao(), chamados.size());
                        }                        
                    }                    
                } else {                    
                    setTituloChamadosServico("Este setor não tem chamados");
                    chamadosServico.getData().put("", null);                    
                }
            } else {                
                setTituloChamadosServico("Selecione um setor");
                chamadosServico.getData().put("", null);
            }
            return chamadosServico;
        } catch (Throwable throwable) {
             setSetorDTO(null);             
        }
        return null;
    }
    
    public PieChartModel getTodosStatus(){    
        try {
            todosStatus.clear();   
            if (getStatusDTO() != null) {
                System.out.println("HOLÁS");
                boolean existeChamadosI = getChamadoInfraBO().verificarSeExisteChamadoStatusSetores(getStatusDTO(), ContextoBean.getContexto().getUsuarioLogado(), getDataInicial(), getDataFinal());
                boolean existeChamadosS = getChamadoServicoBO().verificarSeExisteChamadoStatusSetores(getStatusDTO(), ContextoBean.getContexto().getUsuarioLogado(), getDataInicial(), getDataFinal());                 
                if (existeChamadosI || existeChamadosS) {
                    setTituloTodosStatus("Gráfico dos chamados com situação: " + getStatusDTO().getDescricao());
                    for (SetorDTO s : getSetoresUsuario()) {    
                        List<ChamadoInfraDTO> chamadosI = getChamadoInfraBO().pesquisarChamadosPorStatusPorSetor(getStatusDTO(), s, getDataInicial(), getDataFinal());
                        List<ChamadoServicoDTO> chamadosS = getChamadoServicoBO().pesquisarChamadosPorStatusPorSetor(getStatusDTO(), s, getDataInicial(), getDataFinal());
                        if (!chamadosI.isEmpty() || !chamadosS.isEmpty()) {
                            todosStatus.getData().put(s.getDescricao(), (chamadosI.size()+chamadosS.size()));
                        }                        
                    }                    
                } else {                    
                    setTituloTodosStatus("Este status não tem chamados");
                    todosStatus.getData().put("", null);                    
                }
            } else {                
                setTituloTodosStatus("Selecione um status");
                todosStatus.getData().put("", null);
            }
            return todosStatus;
        } catch (Exception e) {
            Logger.getLogger(ChartBean.class.getName()).log(Level.SEVERE, null, e);
            setStatusDTO(null);             
        }
        return null;
    }
    
    public PieChartModel getStatusInfra(){    
        try {
            statusInfra.clear();            
            if (getStatusDTO() != null) {
                boolean existeChamados = getChamadoInfraBO().verificarSeExisteChamadoStatusSetores(getStatusDTO(), ContextoBean.getContexto().getUsuarioLogado(), getDataInicial(), getDataFinal());
                if (existeChamados) {
                    setTituloStatusInfra("Gráfico dos chamados de Infra-Estrutura com situação: " + getStatusDTO().getDescricao());
                    for (SetorDTO s : getSetoresUsuario()) {    
                        List<ChamadoInfraDTO> chamados = getChamadoInfraBO().pesquisarChamadosPorStatusPorSetor(getStatusDTO(), s, getDataInicial(), getDataFinal());                        
                        if (!chamados.isEmpty()) {
                            statusInfra.getData().put(s.getDescricao(), chamados.size());
                        }                        
                    }                    
                } else {                    
                    setTituloStatusInfra("Este status não tem chamados");
                    statusInfra.getData().put("", null);                    
                }
            } else {                
                setTituloStatusInfra("Selecione um status");
                statusInfra.getData().put("", null);
            }
            return statusInfra;
        } catch (Throwable throwable) {
             setStatusDTO(null);             
        }
        return null;
    }
    
    public PieChartModel getStatusServico(){    
        try {
            statusServico.clear();            
            if (getStatusDTO() != null) {
                boolean existeChamados = getChamadoServicoBO().verificarSeExisteChamadoStatusSetores(getStatusDTO(), ContextoBean.getContexto().getUsuarioLogado(), getDataInicial(), getDataFinal());                                 
                if (existeChamados) {
                    setTituloStatusServico("Gráfico dos chamados de Serviço com situação: " + getStatusDTO().getDescricao());
                    for (SetorDTO s : getSetoresUsuario()) {    
                        List<ChamadoServicoDTO> chamados = getChamadoServicoBO().pesquisarChamadosPorStatusPorSetor(getStatusDTO(), s, getDataInicial(), getDataFinal());                        
                        if (!chamados.isEmpty()) {
                            statusServico.getData().put(s.getDescricao(), chamados.size());
                        }                        
                    }                    
                } else {                    
                    setTituloStatusServico("Este status não tem chamados");
                    statusServico.getData().put("", null);                    
                }
            } else {                
                setTituloStatusServico("Selecione um status");
                statusServico.getData().put("", null);
            }
            return statusServico;
        } catch (Throwable throwable) {
             setStatusDTO(null);             
        }
        return null;
    }
    
    public SetorDTO getSetorDTO() {
        return setorDTO;
    }

    public void setSetorDTO(SetorDTO setorDTO) {
        this.setorDTO = setorDTO;
    }

    public List<SetorDTO> getSetoresUsuario() {
        return setorBO.consultarSetoresDoUsuarioPorCampus(ContextoBean.getContexto());
    }

    public List<StatusDTO> getListaTodosStatus() throws Throwable {
        return getStatusBO().findAll();   
    }

    public SetorBO getSetorBO() {
        return setorBO;
    }

    public void setSetorBO(SetorBO setorBO) {
        this.setorBO = setorBO;
    }

    public StatusBO getStatusBO() {
        return statusBO;
    }

    public void setStatusBO(StatusBO statusBO) {
        this.statusBO = statusBO;
    }

    public ChamadoInfraBO getChamadoInfraBO() {
        return chamadoInfraBO;
    }

    public void setChamadoInfraBO(ChamadoInfraBO chamadoInfraBO) {
        this.chamadoInfraBO = chamadoInfraBO;
    }

    public ChamadoServicoBO getChamadoServicoBO() {
        return chamadoServicoBO;
    }

    public void setChamadoServicoBO(ChamadoServicoBO chamadoServicoBO) {
        this.chamadoServicoBO = chamadoServicoBO;
    }

    public String getTituloTodosChamados() {
        return tituloTodosChamados;
    }

    public void setTituloTodosChamados(String tituloTodosChamados) {
        this.tituloTodosChamados = tituloTodosChamados;
    }

    public String getTituloChamadosInfra() {
        return tituloChamadosInfra;
    }

    public void setTituloChamadosInfra(String tituloChamadosInfra) {
        this.tituloChamadosInfra = tituloChamadosInfra;
    }

    public String getTituloChamadosServico() {
        return tituloChamadosServico;
    }

    public void setTituloChamadosServico(String tituloChamadosServico) {
        this.tituloChamadosServico = tituloChamadosServico;
    }

    public void setTodosChamados(PieChartModel todosChamados) {
        this.todosChamados = todosChamados;
    }

    public void setChamadosInfra(PieChartModel chamadosInfra) {
        this.chamadosInfra = chamadosInfra;
    }

    public void setChamadosServico(PieChartModel chamadosServico) {
        this.chamadosServico = chamadosServico;
    }

    public StatusDTO getStatusDTO() {        
        return statusDTO;
    }

    public void setStatusDTO(StatusDTO statusDTO) {
        this.statusDTO = statusDTO;
    }

    public void setTodosStatus(PieChartModel todosStatus) {
        this.todosStatus = todosStatus;
    }

    public String getTituloTodosStatus() {
        return tituloTodosStatus;
    }

    public void setTituloTodosStatus(String tituloTodosStatus) {
        this.tituloTodosStatus = tituloTodosStatus;
    }

    public String getTituloStatusInfra() {
        return tituloStatusInfra;
    }

    public void setTituloStatusInfra(String tituloStatusInfra) {
        this.tituloStatusInfra = tituloStatusInfra;
    }

    public String getTituloStatusServico() {
        return tituloStatusServico;
    }

    public void setTituloStatusServico(String tituloStatusServico) {
        this.tituloStatusServico = tituloStatusServico;
    }

    public void setStatusInfra(PieChartModel statusInfra) {
        this.statusInfra = statusInfra;
    }

    public void setStatusServico(PieChartModel statusServico) {
        this.statusServico = statusServico;
    }

    public List<SelectItem> getSetorSelectItens() throws Throwable {
       return new SelectBoxUtil().retornaListaEmSelectItem(getSetoresUsuario());
    }

    public Long getSetorSelectItem() {
        return setorSelectItem;
    }
    
    public void setSetorSelectItem(Long setorSelectItem) {
        this.setorSelectItem = setorSelectItem;
    }
        
    public String getTituloLayoutUnit() {
        return tituloLayoutUnit;
    }

    public String getPOR_SETOR() {
        return POR_SETOR;
    }

    public String getPOR_STATUS() {
        return POR_STATUS;
    }

    public String getGraficos() {
        return graficos;
    }

    public void setGraficos(String graficos) {
        this.graficos = graficos;
    }

    public Date getDataInicial() {
        return dataInicial == null ? null : (Date) dataInicial.clone();
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial == null ? null : (Date) dataInicial.clone();
    }

    public Date getDataFinal() {
        return dataFinal == null ? null : (Date) dataFinal.clone();
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal == null ? null : (Date) dataFinal.clone();
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public List<SelectItem> getListaMeses() {        
        return DataUtil.gerarUltimosMeses();
    }

    public int getOpcao() {
        return opcao;
    }

    public void setOpcao(int opcao) {
        this.opcao = opcao;
    }
}