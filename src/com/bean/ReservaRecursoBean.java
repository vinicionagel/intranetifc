package com.bean;
    
import com.bo.InterfaceBO;
import com.bo.LocalizacaoBO;
import com.bo.PatrimonioBO;
import com.bo.ReservaBO;
import com.bo.ReservaLocalizacaoBO;
import com.bo.ReservaPatrimonioBO;
import com.dto.LocalizacaoDTO;
import com.dto.PatrimonioDTO;
import com.dto.ReservaDTO;
import com.dto.ReservaLocalizacaoDTO;
import com.dto.ReservaPatrimonioDTO;
import com.util.DataUtil;
import com.util.FacesUtil;
import static com.util.FacesUtil.*;
import com.util.SelectBoxUtil;
import com.util.ValidadorCampo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;  
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

@Named(value = "reservaRecursoBean")
@ViewScoped
public class ReservaRecursoBean implements Serializable{  
    
    private static final long serialVersionUID = 100006L;
    
    private HashSet<String> listaDiasSemana = new HashSet();               
    private ReservaLocalizacaoBO reservaLocalizacaoBO = ReservaLocalizacaoBO.getInstance();
    private ReservaPatrimonioBO reservaPatrimonioBO = ReservaPatrimonioBO.getInstance();
    private ReservaBO reservaBO = ReservaBO.getInstance();
    private LocalizacaoDTO localizacaoDTO = new LocalizacaoDTO();        
    private Long codigoPatrimonio = 0l;
    private Date dataCalendar = new Date();
    private Date data;
    private Date dataInicial;
    private Date dataFinal;
    private Date dataFimRepetir;
    private Date ultimoDiaAno;                
    private ScheduleModel eventModel; 
    private String descricao = " ";
    private int opcao;      
    private boolean diaTodo = false;    
    private boolean repetir = false;    
    
    public ReservaRecursoBean() {
	eventModel = new LazyScheduleModel() {  
            @Override  
            public void loadEvents(Date start, Date end) {  
                clear();  
                renderizaSchedule(start,end);
            }     
        };   
        
    }
    
    public void clearSchedule(){
        getEventModel().clear();
        localizacaoDTO = new LocalizacaoDTO();
        
    }
           
    private void adicionarReservar(ReservaDTO reserva) {       
        reserva.setDescricao(descricao);
        reserva.setDataInicial(dataInicial);
        reserva.setDataFinal(dataFinal);
        reserva.setUsuarioAutorDTO(ContextoBean.getContexto().getUsuarioLogado());
    }
    
    private void adicionarReservaPatrimonio() {       
        if (reservaPatrimonioBO.uniqueReservaPatrimonio(getPatrimonioDTO(), dataInicial, dataFinal).isEmpty()){
            ReservaPatrimonioDTO reservaPatrimonioDTO = new ReservaPatrimonioDTO();
            adicionarReservar(reservaPatrimonioDTO);
            reservaPatrimonioDTO.setPatrimonioDTO(getPatrimonioDTO());
            reservaPatrimonioBO.save(reservaPatrimonioDTO);
        } else {
            adicionarMensagemErro("Ja existe uma reserva dentro desse intervalo de tempo");
        }
    }
    
    private void adicionarReservaLocalizacao() {        
        if (reservaLocalizacaoBO.uniqueReservaLocalizacao(localizacaoDTO, dataInicial, dataFinal).isEmpty()){
            ReservaLocalizacaoDTO reservaLocalizacaoDTO = new ReservaLocalizacaoDTO();
            adicionarReservar(reservaLocalizacaoDTO);
            reservaLocalizacaoDTO.setLocalizacaoDTO(localizacaoDTO);
            reservaLocalizacaoBO.save(reservaLocalizacaoDTO);
        } else {
            adicionarMensagemErro("Ja existe uma reserva dentro desse intervalo de tempo");
        }
    }
    
    public void reservaDiaTodo(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dataInicial);
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        dataInicial = calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 0);
        dataFinal = calendar.getTime();
    }
    
    private void setandoDia(int dia, Calendar ci, Calendar cf){
        Calendar calendar = Calendar.getInstance();
        Calendar cAux = Calendar.getInstance();
        cAux.setTime(data);
        calendar.setTime(dataFimRepetir);
        calendar.setTime(dataFimRepetir);
        ci.set(Calendar.DAY_OF_WEEK, dia);
        cf.set(Calendar.DAY_OF_WEEK, dia);        
        if((cAux.before(ci))|| (calendar.get(Calendar.DAY_OF_YEAR) == cf.get(Calendar.DAY_OF_YEAR))){    
            dataInicial = ci.getTime();
            dataFinal = cf.getTime();
            gravarReserva();
        } else {
           ci.getTime(); 
           cf.getTime();
        }        
    }
    
    private void reservaRepetir() {
        List<String> erros = new ArrayList<>();
        ValidadorCampo.validarCampoCollection(listaDiasSemana, "DIAS", erros);
        if(erros.isEmpty()){
            Calendar calAux = Calendar.getInstance();
            Calendar calInicial = Calendar.getInstance();
            Calendar calFinal = Calendar.getInstance();   
            Calendar calFimRepetir =  Calendar.getInstance();
            //Data
            calAux.setTime(data);
            //Hora Inicial
            calInicial.setTime(dataInicial);
            //Hora Final
            calFinal.setTime(dataFinal);
            //Data Fim da Repetição
            calFimRepetir.setTime(dataFimRepetir);
            calInicial.set(Calendar.YEAR, calAux.get(Calendar.YEAR));
            calInicial.set(Calendar.MONTH, calAux.get(Calendar.MONTH));
            calInicial.set(Calendar.DAY_OF_MONTH, calAux.get(Calendar.DAY_OF_MONTH));
            calFinal.set(Calendar.YEAR, calAux.get(Calendar.YEAR));
            calFinal.set(Calendar.MONTH, calAux.get(Calendar.MONTH));
            calFinal.set(Calendar.DAY_OF_MONTH, calAux.get(Calendar.DAY_OF_MONTH));
            calInicial.getTime();
            calFinal.getTime();
            if(diaTodo){
                reservaDiaTodo();
            }
            int dias;
            dias = calFimRepetir.get(Calendar.WEEK_OF_YEAR) - calFinal.get(Calendar.WEEK_OF_YEAR);
            for (int i=0; i<= dias; i++){           
                for(Integer j=1; j<=7; j++){
                    if(listaDiasSemana.contains(j.toString())){
                        setandoDia(j, calInicial, calFinal);
                    }
                }
                calInicial.add(Calendar.WEEK_OF_YEAR, 1);
                calFinal.add(Calendar.WEEK_OF_YEAR, 1);
            }
            dataCalendar = dataFinal;
            renderizaSchedule(calInicial.getTime(), calFinal.getTime());
        }else{
            adicionarMensagemErro(erros);
        }
    }     
           
    public void adicionarReserva(){
        if(repetir){
            reservaRepetir();
        }else{
            if(diaTodo){
                reservaDiaTodo();
            }
            gravarReserva();
            dataCalendar = dataFinal;
            renderizaSchedule(dataInicial,dataFinal);
        }
    }      
    
    public void gravarReserva() {
        List<String> erros = new ArrayList<>();
        ValidadorCampo.validarDatas(dataInicial, "DATA INICIAL", dataFinal, "DATA FINAL", erros);
        if(erros.isEmpty()){            
            if(opcao == 1){
                adicionarReservaPatrimonio();
            } else if(opcao == 2){
                adicionarReservaLocalizacao();
            }
        }else{
            adicionarMensagemErro(erros.get(0));
        }
    }

    private void adicionaEvento(List<?> lista){
        for(ReservaDTO rpDTO : (List<ReservaDTO>) lista){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(rpDTO.getDataInicial());
            rpDTO.setDataInicial(calendar.getTime());
            calendar.setTime(rpDTO.getDataFinal());
            rpDTO.setDataFinal(calendar.getTime());
            getEventModel().addEvent(new DefaultScheduleEvent(rpDTO.getUsuarioAutorDTO().getNome(), rpDTO.getDataInicial(), rpDTO.getDataFinal()));  
        }
    }
    
    public void setarFimAno(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        ultimoDiaAno = DataUtil.ultimoSemanaAno(calendar.get(Calendar.YEAR));
    }
        
    public void renderizaSchedule(Date dataInicial, Date dataFinal) {
        getEventModel().clear();
        if(opcao == 1){
            List<ReservaPatrimonioDTO> listaReservaPatrimonio;
            if(getPatrimonioDTO() != null){
                listaReservaPatrimonio = reservaPatrimonioBO.uniqueReservaPatrimonio(getPatrimonioDTO(),dataInicial,dataFinal);
                if(listaReservaPatrimonio != null){
                    adicionaEvento(listaReservaPatrimonio);
                }
            }
        }else if (opcao == 2){
            if(localizacaoDTO != null){
                List<ReservaLocalizacaoDTO> listaReservaLocalizacao;
                listaReservaLocalizacao = reservaLocalizacaoBO.uniqueReservaLocalizacao(localizacaoDTO,dataInicial, dataFinal);
                if(listaReservaLocalizacao != null){                    
                    adicionaEvento(listaReservaLocalizacao);
                }
            }
        }
    }
    
    public Date getData() {
        return data == null ? null : (Date) data.clone();
    }

    public void setData(Date data) {
        this.data = data == null ? null : (Date) data.clone();
    }

    public List<LocalizacaoDTO> getListaLocalizacao()  {
        return new LocalizacaoBO().pesquisarPorCampus(ContextoBean.getContexto().getCampusAtual());
    }
        
    public LocalizacaoDTO getLocalizacaoDTO() {
        return localizacaoDTO;
    }

    public void setLocalizacaoDTO(LocalizacaoDTO localizacaoDTO) {
        this.localizacaoDTO = localizacaoDTO;
    }

    public String getTituloLayoutUnit() {
        return "Reserva de Recurso";
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

    public ScheduleModel getEventModel() {
        return eventModel;
    }
    
    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    public PatrimonioDTO getPatrimonioDTO() {
        try {
            return new PatrimonioBO().findById(getCodigoPatrimonio());
        } catch (Throwable ex) {            
            Logger.getLogger(ReservaRecursoBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<SelectItem> getListaPatrimonios()  {
        try {
            return new SelectBoxUtil().retornaListaPatrimonioSelectItem(PatrimonioBO.getInstance().pesquisarPorLocalizacao(localizacaoDTO));
        } catch (Throwable ex) {            
            Logger.getLogger(ReservaRecursoBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public int getOpcao() {
        return opcao;
    }

    public void setOpcao(int opcao) {
        this.opcao = opcao;
    }

    public ReservaLocalizacaoBO getReservaLocalizacaoBO() {
        return reservaLocalizacaoBO;
    }
        
    public ReservaPatrimonioBO getReservaPatrimonioBO() {
        return reservaPatrimonioBO;
    }
    
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isDiaTodo() {
        return diaTodo;
    }

    public void setDiaTodo(boolean diaTodo) {
        this.diaTodo = diaTodo;
    }

    public boolean isRepetir() {
        return repetir;
    }

    public void setRepetir(boolean repetir) {
        this.repetir = repetir;
    }

    public HashSet<String> getListaDiasSemana() {
        return listaDiasSemana;
    }

    public void setListaDiasSemana(HashSet<String> listaDiasSemana) {
        this.listaDiasSemana = listaDiasSemana;
    }

    public Date getDataFimRepetir() {
        return dataFimRepetir == null ? null : (Date) dataFimRepetir.clone();
    }

    public void setDataFimRepetir(Date dataFimRepetir) {
        this.dataFimRepetir = dataFimRepetir == null ? null : (Date) dataFimRepetir.clone();
    }

    public Date getUltimoDiaAno() {
        return ultimoDiaAno == null ? null : (Date) ultimoDiaAno.clone();
    }

    public void setUltimoDiaAno(Date ultimoDiaAno) {
        this.ultimoDiaAno = ultimoDiaAno == null ? null : (Date) ultimoDiaAno.clone();
    }

    public Date getDataCalendar() {
        return dataCalendar == null ? null : (Date) dataCalendar.clone();
    }

    public void setDataCalendar(Date dataCalendar) {
        this.dataCalendar = dataCalendar == null ? null : (Date) dataCalendar.clone();
    }

    public Long getCodigoPatrimonio() {
        return codigoPatrimonio;
    }

    public void setCodigoPatrimonio(Long codigoPatrimonio) {
        this.codigoPatrimonio = codigoPatrimonio;
    }
    
    public void selecionaEvento(SelectEvent selectEvent) {
        try {
            ScheduleEvent scheduleEvent = (ScheduleEvent) selectEvent.getObject();
            setDataInicial(scheduleEvent.getStartDate());
            setDataFinal(scheduleEvent.getEndDate());
        } catch (Exception e) {
            Logger.getLogger(ReservaRecursoBean.class.getName()).log(Level.SEVERE, null, e);
        }
    }  
    
    public boolean validaExcluir(){
        if (opcao == 1) {
            if (getPatrimonioDTO() != null) {
                ReservaPatrimonioDTO reservaPatrimonioDTO = reservaPatrimonioBO.pesquisarReservaPatrimonioDTO(getPatrimonioDTO(), dataInicial, dataFinal);
                if(reservaBO.verificaUsuario(ContextoBean.getContexto().getUsuarioLogado(), reservaPatrimonioDTO) != null){
                    return true;
                }
            }
        } else if (opcao == 2) {
            if (getLocalizacaoDTO() != null) {
                ReservaLocalizacaoDTO reservaLocalizacaoDTO = reservaLocalizacaoBO.pesquisarReservaLocalizacaoDTO(getLocalizacaoDTO(), dataInicial, dataFinal);
                if(reservaBO.verificaUsuario(ContextoBean.getContexto().getUsuarioLogado(), reservaLocalizacaoDTO) != null){
                    return true;
                }
            }
        }
        return false;
    }
    
    public void removeReserva(){
        try {
            InterfaceBO interfaceBO = InterfaceBO.getInstance();
            if(interfaceBO.verificaVisualizacaoBotao(ContextoBean.getContexto().getUsuarioLogado(), getCDU(), 4l)|| validaExcluir()){
                if (opcao == 1) {//Patrimonio
                    if (getPatrimonioDTO() != null) {
                        ReservaPatrimonioDTO reservaPatrimonioDTO = reservaPatrimonioBO.pesquisarReservaPatrimonioDTO(getPatrimonioDTO(), dataInicial, dataFinal);
                        reservaPatrimonioBO.remove(reservaPatrimonioDTO);
                    }
                } else if (opcao == 2) {//Localizacao
                    if (getLocalizacaoDTO() != null) {
                        ReservaLocalizacaoDTO reservaLocalizacaoDTO = reservaLocalizacaoBO.pesquisarReservaLocalizacaoDTO(getLocalizacaoDTO(), dataInicial, dataFinal);
                        reservaLocalizacaoBO.remove(reservaLocalizacaoDTO);
                    }
                }
            }else{
                FacesUtil.adicionarMensagemErro("Você não tem permissão para remover essa reserva");
            }
        } catch (Exception e) {
            Logger.getLogger(ReservaRecursoBean.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public String getCDU(){
        int i = 6;      
        String viewID = FacesUtil.getFacesContext().getViewRoot().getViewId();
        while(viewID.charAt(++i) != '/'){}
        return viewID.substring(7, i);        
    }
    
 

    public ReservaBO getReservaBO() {
        return reservaBO;
    }

    public void setReservaBO(ReservaBO reservaBO) {
        this.reservaBO = reservaBO;
    }
    
    public Date getDiaAtual(){
        return new Date();
    }
}
