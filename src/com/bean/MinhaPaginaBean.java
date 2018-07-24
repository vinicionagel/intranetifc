package com.bean;

import com.bo.CampusBO;
import com.bo.ChamadoBO;
import com.bo.HoraBO;
import com.bo.SetorBO;
import com.dto.CampusDTO;
import com.dto.ChamadoDTO;
import com.dto.HoraDTO;
import com.dto.SetorDTO;
import com.dto.UsuarioDTO;
import com.util.FacesUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.commons.lang3.time.DateUtils;
import org.primefaces.event.TabChangeEvent;

@Named(value = "minhaPaginaBean")
@ViewScoped
public class MinhaPaginaBean implements Serializable {

    static final Logger logger = Logger.getLogger(MinhaPaginaBean.class.getName());
    private static final long serialVersionUID = 100010L;
    private final String tituloLayoutUnit = "Minha Página";    
    private HoraBO horaBO = HoraBO.getInstance();    
    private ChamadoDTO chamadoDTO = new ChamadoDTO();
    private boolean mostrarDetalhesChamados = false;
    private List<List<HoraDTO>> resumoAtividadesSemana = new ArrayList<>();
    private List<List<HoraDTO>> resumoHorasTrabalhadasSemana = new ArrayList<>();
    private List<List<ChamadoDTO>> eventosProximaSemana = new ArrayList<>();
    private List<List<ChamadoDTO>> meusChamados;

    public MinhaPaginaBean() {
        FacesUtil.removeSessionAttribute("activeIndex");
        this.mostrarDetalhesChamados = false;
    }

    
    
    public String minhaPagina() {
        return "minhaPagina";
    }

    public void alterarDetalhesChamados() {
        this.mostrarDetalhesChamados = !this.mostrarDetalhesChamados;
    }

    public String getTituloLayoutUnit() {
        return tituloLayoutUnit;
    }

    public List<HoraDTO> getListaHorasTrabalhadas() throws Throwable {
        return horaBO.horasPorChamado(getChamadoDTO());
    }

    public ChamadoDTO getChamadoDTO() {
        return chamadoDTO;
    }

    public void setChamadoDTO(ChamadoDTO chamadoDTO) {
        this.chamadoDTO = chamadoDTO;
    }

    public UsuarioDTO getusuario() {
        return ContextoBean.getContexto().getUsuarioLogado();
    }

    public float getHorasSeteDias() throws Throwable {
        return horaBO.consultarHorasSeteDias(getusuario());
    }

    public float getHorasMesAnterior() throws Throwable {
        return horaBO.consultarHorasMesAnterior(getusuario());
    }

    public float getHorasMesAtual() throws Throwable {
        return horaBO.consultarHorasMesAtual(getusuario());
    }

    /**
     * Retorna uma lista com listas representando dias contendo cada uma das
     * atividades registradas por dia na última semana 
     */
    public List<List<HoraDTO>> getResumoHorasTrabalhadasSemana() throws Throwable {
        if (resumoHorasTrabalhadasSemana == null) {
            carregarResumoHorasTrabalhadasSemana();
            carregarResumoAtividadesSemana();
        }

        return resumoHorasTrabalhadasSemana;
    }

    public void setResumoHorasTrabalhadasSemana(List<List<HoraDTO>> resumoHorasTrabalhadasSemana) {
        this.resumoHorasTrabalhadasSemana = resumoHorasTrabalhadasSemana;
    }

    /*
     * Retorna uma lista com listas representando dias, que contém atividades registradas na última semana
     * agrupadas por chamado apenas a soma das horas de cada chamado em cada dia
     */
    public List<List<HoraDTO>> getResumoAtividadesSemana() throws Throwable {
        if (resumoAtividadesSemana == null) {
            carregarResumoHorasTrabalhadasSemana();
            carregarResumoAtividadesSemana();
        }

        return resumoAtividadesSemana;
    }

    public void setResumoAtividadesSemana(List<List<HoraDTO>> resumoAtividadesSemana) {
        this.resumoAtividadesSemana = resumoAtividadesSemana;
    }

    public boolean isMostrarDetalhesChamados() {
        return mostrarDetalhesChamados;
    }

    public void setMostrarDetalhesChamados(boolean mostrarDetalhesChamados) {
        this.mostrarDetalhesChamados = mostrarDetalhesChamados;
    }

    public List<List<ChamadoDTO>> getEventosProximaSemana() {
        if (eventosProximaSemana == null) {
            carregarEventosProximaSemana();
            carregarMeusChamados();
        }

        return eventosProximaSemana;
    }

    private void balanceamentoLista(List<List<ChamadoDTO>> lista) {
        while (lista.size() < 7) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(lista.get(lista.size() - 1).get(0).getDataPrevista());
            cal.add(Calendar.DATE, 1);

            ChamadoDTO chUtil = new ChamadoDTO();
            chUtil.setDataPrevista(cal.getTime());

            ArrayList<ChamadoDTO> listaUtil = new ArrayList<>();

            listaUtil.add(chUtil);
            lista.add(listaUtil);
        }
    }

    public void setEventosProximaSemana(List<List<ChamadoDTO>> eventosProximaSemana) {
        this.eventosProximaSemana = eventosProximaSemana;
    }

    public List<CampusDTO> getCampusUsuarioLogado() throws Throwable {
	return CampusBO.getInstance().pesquisarCampusPorUsuario(getusuario());
    }

    public List<SetorDTO> getSetoresUsuarioPorCampus(CampusDTO campus) throws Throwable {
	return SetorBO.getInstance().consultarSetoresDoUsuarioPorCampus(ContextoBean.getContexto().getUsuarioLogado(), campus);
    }

    public List<List<ChamadoDTO>> getMeusChamados(){
        try{
            if(meusChamados == null) {
                carregarEventosProximaSemana();
                carregarMeusChamados();
            }
        } catch (Exception e){
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
	return meusChamados;
    }

    private void carregarMeusChamados() {
        List<ChamadoDTO> chamados =  ChamadoBO.getInstance().consultarMeusChamados(ContextoBean.getContexto());
        if (chamados != null && !chamados.isEmpty()) {
            meusChamados = new LinkedList<>();

            List<ChamadoDTO> aux = new LinkedList<>();
            meusChamados.add(aux);

            for (ChamadoDTO chamado : chamados) {
                if (aux.isEmpty()) {
                    aux.add(chamado);
                } else if (DateUtils.isSameDay(aux.get(0).getDataHoraAbertura(), chamado.getDataHoraAbertura())) {
                    aux.add(chamado);
                } else {
                    aux = new LinkedList<>();
                    aux.add(chamado);
                    meusChamados.add(aux);
                }
            }
        }
    }

    public void carregarEventosProximaSemana() {
        eventosProximaSemana = new ArrayList<List<ChamadoDTO>>() {
            @Override
            public boolean add(List<ChamadoDTO> lista) {
                ChamadoDTO ch;
                Calendar cal = Calendar.getInstance();
                Calendar calCh = Calendar.getInstance();

                if (lista.size() > 0) {
                    ch = lista.get(0);
                } else {
                    /*
                     * A lista incluída está vazia, indicando que é a última lista incluída e
                     * será incluída no final da lista.
                     */
                    calCh.setTime(Calendar.getInstance().getTime());
                    //calCh.add(Calendar.DATE, 6);

                    ch = new ChamadoDTO();
                    ch.setDataPrevista(calCh.getTime());
                    lista.add(ch);
                }

                if (isEmpty()) {
                    /*
                     * Primeiro add na lista
                     */
                    calCh.setTime(ch.getDataPrevista());

                    /*
                     * verifica se o primeiro add da lista é depoi de hoje
                     */
                    if (calCh.after(cal)) {
                        int intervalo = calCh.get(Calendar.DATE) - cal.get(Calendar.DATE);
                        for (int i = 0; i < intervalo; i++) {
                            ArrayList<ChamadoDTO> listaAux = new ArrayList<>();

                            ChamadoDTO chamadoAux = new ChamadoDTO();
                            chamadoAux.setDataPrevista(cal.getTime());

                            listaAux.add(chamadoAux);
                            super.add(listaAux);
                            cal.add(Calendar.DATE, 1);
                        }
                    }
                } else {
                    calCh.setTime(ch.getDataPrevista());
                    cal.setTime(get(size() - 1).get(0).getDataPrevista());

                    while (cal.get(Calendar.DATE) + 1 < calCh.get(Calendar.DATE)) {
                        cal.add(Calendar.DATE, 1);
                        ArrayList<ChamadoDTO> listaAux = new ArrayList<>();

                        ChamadoDTO chamadoAux = new ChamadoDTO();
                        chamadoAux.setDataPrevista(cal.getTime());

                        listaAux.add(chamadoAux);
                        super.add(listaAux);
                    }

                }
                return super.add(lista);
            }
        };

        List<ChamadoDTO> listaAux = new ArrayList<>();
        try {
            for (ChamadoDTO chamado : ChamadoBO.getInstance().consultarChamadosProximaSemana(getusuario())) {
                if (listaAux.isEmpty() || listaAux.get(0).getDataPrevista().equals(chamado.getDataPrevista())) {
                    listaAux.add(chamado);
                } else {
                    eventosProximaSemana.add(listaAux);

                    listaAux = new ArrayList<>();
                    listaAux.add(chamado);
                }
            }
        } catch (Throwable ex) {
        }

        eventosProximaSemana.add(listaAux);
        balanceamentoLista(eventosProximaSemana);
    }

    private void carregarResumoAtividadesSemana() {
        resumoAtividadesSemana = new ArrayList<>();

        List<HoraDTO> listaAux;
        HoraDTO horaAux;

        try {
            for (List<HoraDTO> listaDia : getResumoHorasTrabalhadasSemana()) {
                listaAux = new ArrayList<>();
                horaAux = null;

                for (HoraDTO hora : listaDia) {
                    if (horaAux == null) {
                        horaAux = new HoraDTO();
                        horaAux.setDataTrabalho(hora.getDataTrabalho());
                        horaAux.setTempo(hora.getTempo());
                        horaAux.setChamadoDTO(hora.getChamadoDTO());
                    } else if (horaAux.getChamadoDTO().getCodigo().equals(hora.getChamadoDTO().getCodigo())) {
                        horaAux.setTempo(horaAux.getTempo() + hora.getTempo());
                    } else {
                        listaAux.add(horaAux);

                        horaAux = new HoraDTO();
                        horaAux.setDataTrabalho(hora.getDataTrabalho());
                        horaAux.setTempo(hora.getTempo());
                        horaAux.setChamadoDTO(hora.getChamadoDTO());
                    }
                }

                listaAux.add(horaAux);
                resumoAtividadesSemana.add(listaAux);
            }
        } catch (Throwable th) {
            logger.log(Level.SEVERE, th.getMessage(), th);
        }
    }

    private void carregarResumoHorasTrabalhadasSemana() {
        try {
            List<HoraDTO> horasSemana = horaBO.listarHorasSemana(getusuario());

            resumoHorasTrabalhadasSemana = new ArrayList<>();
            List<HoraDTO> aux = new ArrayList<>();

            if (horasSemana != null && !horasSemana.isEmpty()) {
                for (HoraDTO hora : horasSemana) {
                    if (aux.isEmpty() || aux.get(0).getDataTrabalho().equals(hora.getDataTrabalho())) {
                        aux.add(hora);
                    } else {
                        resumoHorasTrabalhadasSemana.add(aux);
                        aux = new ArrayList<>();
                        aux.add(hora);
                    }
                }
            } else {
                aux.add(new HoraDTO());
            }
            resumoHorasTrabalhadasSemana.add(aux);
        } catch (Throwable th) {
            logger.log(Level.SEVERE, th.getMessage(), th);
        }
    }

    public void onTabChange(TabChangeEvent event) {
        try {
            setTabIndex(Integer.parseInt(event.getTab().getId().charAt(event.getTab().getId().length() - 1) + ""));
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            setTabIndex(1);
        }

        carregarAbas(event.getTab().getId());
    }

    private void carregarAbas(String id) {
        switch (id) {
            case "tab1":
                // Aba chamados: carregar listas.
                carregarMeusChamados();
                carregarEventosProximaSemana();
                break;
            case "tab2":
                // Aba resumo de suas atividades
                carregarResumoHorasTrabalhadasSemana();
                carregarResumoAtividadesSemana();
                break;
            default: 
                break;
        }
    }

    public int getTabIndex() {
        try {
            int tabIndex = 0;
            if (FacesUtil.getSessionAttribute("tabIndexHome") != null){
                tabIndex = Integer.parseInt(FacesUtil.getSessionAttribute("tabIndexHome").toString());
            }
            carregarAbas("tab" + tabIndex);
            return tabIndex;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return 0;
        }
    }

    public void setTabIndex(int tabIndex) {
        FacesUtil.setSessionAttribute("tabIndexHome", tabIndex);
    }
}