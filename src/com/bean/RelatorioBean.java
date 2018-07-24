package com.bean;

import com.bo.CampusBO;
import com.bo.ChamadoInfraBO;
import com.bo.ChamadoServicoBO;
import com.bo.HoraBO;
import com.bo.LocalizacaoBO;
import com.bo.PatrimonioBO;
import com.bo.SetorBO;
import com.bo.TipoPatrimonioBO;
import com.bo.UsuarioBO;
import com.bo.UsuarioPermissaoSetorBO;
import com.dto.CampusDTO;
import com.dto.LocalizacaoDTO;
import com.dto.PatrimonioDTO;
import com.dto.SetorDTO;
import com.dto.TipoPatrimonioDTO;
import com.dto.UsuarioDTO;
import com.dto.UsuarioPermissaoSetorDTO;
import com.util.ConnectionFactory;
import com.util.DataUtil;
import com.util.FacesUtil;
import com.util.ReportUtils;
import com.util.SelectBoxUtil;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "relatorioBean")
@ViewScoped
public class RelatorioBean implements Serializable{
    
    private UsuarioDTO usuarioDTOPesquisa = new UsuarioDTO();   
    private CampusDTO campusDTOPesquisa = new CampusDTO();
    private PatrimonioDTO patrimonioDTOPesquisa = new PatrimonioDTO();
    private TipoPatrimonioDTO tipoPatrimonioDTOPesquisa = new TipoPatrimonioDTO();
    private LocalizacaoDTO localizacaoDTOPesquisa = new LocalizacaoDTO();
    private ChamadoServicoBO chamadoServicoBO = ChamadoServicoBO.getInstance();
    private ChamadoInfraBO chamadoInfraBO = ChamadoInfraBO.getInstance();    

    private int opcao;
    private int tipoRelatorio;
    private int opcaoData = 2;
    private String parametro;
    private Long parametroCodigo;
    private String diretorio;
    private String mes;
    private final String tituloLayoutUnit = "Relatórios";
    private boolean valida = true;
    private boolean verifica = true;
    private Date dataInicial;
    private Date dataFinal;
    private Date dataAux;
    private Long setorSelectItem;

    public RelatorioBean() {
        setorSelectItem=0l;
    }
    
    public void gerarRelatorio() throws SQLException, Throwable{ 
        verifica = true;
        //Por Mes
        if(opcaoData == 1){
            setDataInicial(DataUtil.dataAtualMenos30Dias());
            setarDatasAPartirDoMes();
        //Por Periodo
        }else if(opcaoData == 2){
            if(getDataInicial() == null) {
                setDataInicial(DataUtil.stringParaData("01/01/2000"));
            }
            if(getDataFinal() == null) {
                setDataFinal(new Date());
            } 
        }    
        //Data Final +1 para imprimir relatório até aquele dia
        Calendar calendar = Calendar.getInstance(); 
        calendar.setTime(dataFinal);
        calendar.add(Calendar.DAY_OF_MONTH, 1); 
        setDataAux(calendar.getTime());
        if(getTipoRelatorio() != 3){
            if (getOpcao() == 1) {
                parametro = getUsuarioDTOPesquisa().getNome();
                parametroCodigo = getUsuarioDTOPesquisa().getCodigo();
            }
            if (getOpcao() == 2){
                parametro = getSetorDTOPesquisa().getDescricao();
                parametroCodigo = getSetorDTOPesquisa().getCodigo();
            }
            if (getOpcao() == 3) {
                parametro = getCampusDTOPesquisa().getDescricao();
                parametroCodigo = getCampusDTOPesquisa().getCodigo();
            }
            if(getOpcao() == 4){
                parametro = getPatrimonioDTOPesquisa().getDescricao();
                parametroCodigo = getPatrimonioDTOPesquisa().getCodigo();
            }
            if(getOpcao() == 5){
                parametro = getTipoPatrimonioDTOPesquisa().getDescricao();
                parametroCodigo = getTipoPatrimonioDTOPesquisa().getCodigo();
            }
            if(getOpcao() == 6){
                parametro = getLocalizacaoDTOPesquisa().getDescricao();
                parametroCodigo = getLocalizacaoDTOPesquisa().getCodigo();
            }
        }
        try {
            abrirRelatorio(parametro);
        } catch (Exception ex) {
            Logger.getLogger(RelatorioBean.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
    /**Diretório dos Relatórios das Horas Trabalhadas
     * 
     * @return 
     */
    private String relatorioHorasTrabalhadas() throws Throwable{
        String dir = "";
        HoraBO hb = HoraBO.getInstance();
        //Por Usuário
       if (getOpcao() == 1){
            if(!(hb.pesquisarHorasTrabalhadasEntreDatas(dataInicial, dataAux)).isEmpty()){
                dir = "/com/relatorios/RelatorioHorasTrabalhadasPorUsuario.jasper";
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Essa data não apresenta horas trabalhadas", "erro"));
                verifica = false;
            }
       }
       if (getOpcao() == 2){
            if(!(hb.pesquisarHorasTrabalhadasEntreDatas(dataInicial, dataAux)).isEmpty()){
                dir = "/com/relatorios/RelatorioHorasTrabalhadasPorSetor.jasper";
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Essa data não apresenta horas trabalhadas", "erro"));
                verifica = false;
            }
       }
       if (getOpcao() == 3){
            if(!(hb.pesquisarHorasTrabalhadasEntreDatas(dataInicial, dataAux)).isEmpty()){
                dir = "/com/relatorios/RelatorioHorasTrabalhadasPorCampus.jasper";
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Essa data não apresenta horas trabalhadas", "erro"));
                verifica = false;
            }
       }
       return dir;
    }
    
    /**Diretório dos Relatórios dos Chamados de Serviço */
    private String relatorioChamadoServico() throws Throwable{
        String dir = "";
        //Por Usuário
        switch (getOpcao()) {
            case 1:
                if(!((chamadoServicoBO.pesquisarChamadosServicoPorUsuarioEntreDatas(usuarioDTOPesquisa, getDataInicial(), getDataAux())).isEmpty())) {
                    dir = "/com/relatorios/RelatorioChamadoServPorUsuario.jasper";
                }
                else{
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Esse Usuário não apresenta Chamados de Serviço nesse período", "erro"));
                    verifica = false;
                }   break;
            case 2:
                if(!((chamadoServicoBO.pesquisarChamadosServicoPorSetorEntreDatas(getSetorDTOPesquisa(), getDataInicial(), getDataAux())).isEmpty())) {
                    dir = "/com/relatorios/RelatorioChamadoServPorSetor.jasper" ;
                }
                else{
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Esse Setor não apresenta Chamados de Serviço nesse período", "erro"));
                    verifica = false;
                }   break;
            case 3:
                if(!((chamadoServicoBO.pesquisarChamadosServicoPorCampusEntreDatas(campusDTOPesquisa, getDataInicial(), getDataAux())).isEmpty())) {
                    dir = "/com/relatorios/RelatorioChamadoServPorCampus.jasper";
                }
                else{
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Esse Campus não apresenta Chamados de Serviço nesse período", "erro"));
                    verifica = false;
                }   break;
            default:
                break;
        }
        return dir;
    }
    
    public void limpaOpcao(){
        setOpcao(0);
    }
    
    /**Diretório dos Relatórios dos Chamados de Infraestrutura */
    private String relatorioChamadoInfra() throws Throwable{
        String dir = "";
        switch (getOpcao()) {
            case 1:
                if(!(chamadoInfraBO.pesquisarChamadosInfraPorUsuarioEntreDatas(usuarioDTOPesquisa, getDataInicial(), getDataAux())).isEmpty()) {
                    dir = "/com/relatorios/RelatorioChamadoInfraPorUsuario.jasper";
                }
                else{
                    FacesUtil.adicionarMensagemErro("Esse Usuário não apresenta chamados nesse período");
                    verifica = false;
                }   break;
            case 2:
                if(!(chamadoInfraBO.pesquisarChamadosInfraPorSetorEntreDatas(getSetorDTOPesquisa(), getDataInicial(), getDataAux())).isEmpty()) {
                    dir = "/com/relatorios/RelatorioChamadoInfraPorSetor.jasper" ;
                }
                else{
                    FacesUtil.adicionarMensagemErro("Esse Setor não apresenta chamados nesse período");
                    verifica = false;
                }   break;
            case 3:
                if(!(chamadoInfraBO.pesquisarChamadosInfraPorCampusEntreDatas(campusDTOPesquisa, getDataInicial(), getDataAux())).isEmpty()) {
                    dir = "/com/relatorios/RelatorioChamadoInfraPorCampus.jasper";
                }
                else{
                    FacesUtil.adicionarMensagemErro("Esse Campus não apresenta chamados nesse período");
                    verifica = false;
                }   break;
            case 4:
                if(!(chamadoInfraBO.pesquisarChamadosInfraPorPatrimonioEntreDatas(getPatrimonioDTOPesquisa(), getDataInicial(), getDataAux())).isEmpty()) {
                    dir = "/com/relatorios/RelatorioChamadoInfraPorPatrimonio.jasper";
                }
                else{
                    FacesUtil.adicionarMensagemErro("Esse Patrimônio não apresenta chamados nesse período");
                    verifica = false;
                }   break;
            case 5:
                if(!(chamadoInfraBO.pesquisarChamadosInfraPorTipoPatrimonioEntreDatas(getTipoPatrimonioDTOPesquisa(), getDataInicial(), getDataAux())).isEmpty()) {
                    dir = "/com/relatorios/RelatorioChamadoInfraPorTipoPatrimonio.jasper";
                }
                else{
                    FacesUtil.adicionarMensagemErro("Esse Tipo de Patrimônio não apresenta chamados nesse período");
                    verifica = false;
                }   break;
            case 6:
                if(!(chamadoInfraBO.pesquisarChamadosInfraPorLocalizacaoEntreDatas(getLocalizacaoDTOPesquisa(), getDataInicial(), getDataAux())).isEmpty()) {
                    dir = "/com/relatorios/RelatorioChamadoInfraPorLocalizacao.jasper";
                }
                else{
                    FacesUtil.adicionarMensagemErro("Essa Localização apresenta chamados nesse período");
                    verifica = false;
                }   break;
            default:
                break;
        }
      
        return dir;
    }
    
    public void abrirRelatorio(String parametro) throws SQLException, Exception, Throwable {
        //Diretório
        switch (tipoRelatorio) {
            case 1:
                diretorio = relatorioChamadoInfra();
                break;
            case 2:
                diretorio = relatorioChamadoServico();
                break;
            case 3:
                diretorio = relatorioHorasTrabalhadas();
                break;
            case 4:
                if(!(chamadoInfraBO.pesquisarEquipamentosEmManutencaoEntreDatasContexto(getDataInicial(), getDataAux(),gerarListaIN(getSetoresUsuario()))).isEmpty()) {
                    diretorio = "/com/relatorios/RelatorioEquipamentoManutencao.jasper";
                } 
                else{
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Essa Data não apresenta equipamentos em manutenção", "erro"));
                    verifica = false;
                }   break;
            default:
                break;
        }
        
        //Geração do Relatório
        if(verifica){
            InputStream inputStream = getClass().getResourceAsStream(diretorio);
            // mapa de parâmetros do relatório (ainda vamos aprender a usar)
            Map parametros = new HashMap();
            if(tipoRelatorio == 3 || tipoRelatorio == 4){
                parametros.put("contexto", gerarListaIN(getSetoresUsuario()));
            }
            parametros.put("usuario",ContextoBean.getContexto().getUsuarioLogado().getNome());
            parametros.put("Descricao", parametro);
            parametros.put("codigo", parametroCodigo);
            parametros.put("dir", FacesUtil.getRealPath());
            parametros.put("dataInicial", getDataInicial());
            parametros.put("dataAux", getDataAux());
            parametros.put("dataFinal", getDataFinal());
            try {
                String nomePDF;
                switch (tipoRelatorio) {
                    case 3:
                        nomePDF = "Relatório_Horas_Trabalhadas_"+ DataUtil.dataForStringPadrao(dataInicial)+"_"+DataUtil.dataForStringPadrao(dataAux);
                        break;
                    case 4:
                        nomePDF = "Relatório_Equip_Manutencao_"+ DataUtil.dataForStringPadrao(dataInicial)+"_"+DataUtil.dataForStringPadrao(dataAux);
                        break;
                    default:
                        nomePDF = "Relatório_"+ parametro.replace(" ", "_") +"_"+ DataUtil.dataForStringPadrao(dataInicial)+"_"+DataUtil.dataForStringPadrao(dataAux);
                        break;
                }
                // abre o relatório
               ReportUtils.openReportPDF( nomePDF, inputStream, parametros,ConnectionFactory.getPostgresConnection() );
            
            } catch ( Exception exc) {                
                Logger.getLogger(RelatorioBean.class.getName()).log(Level.SEVERE, exc.getMessage(), exc);
            }            
        }
    }    
     
    public UsuarioDTO getUsuarioDTOPesquisa() {
        return usuarioDTOPesquisa;
    }
    
    public void setUsuarioDTOPesquisa(UsuarioDTO usuarioDTOPesquisa) {
        this.usuarioDTOPesquisa = usuarioDTOPesquisa;
    }
    
    public SetorDTO getSetorDTOPesquisa() throws Throwable {
        return SetorBO.getInstance().findById(setorSelectItem);
    }
    
    public CampusDTO getCampusDTOPesquisa() {
        return campusDTOPesquisa;
    }
    
    public void setCampusDTOPesquisa(CampusDTO campusDTOPesquisa) {
        this.campusDTOPesquisa = campusDTOPesquisa;
    }
    
    public void setLocalizacaoDTOPesquisa(LocalizacaoDTO localizacaoDTO) {
        this.localizacaoDTOPesquisa = localizacaoDTO;
    }
    
    public LocalizacaoDTO getLocalizacaoDTOPesquisa() {
        return localizacaoDTOPesquisa;
    }

    public PatrimonioDTO getPatrimonioDTOPesquisa() {
        return patrimonioDTOPesquisa;
    }

    public void setPatrimonioDTOPesquisa(PatrimonioDTO patrimonioDTOPesquisa) {
        this.patrimonioDTOPesquisa = patrimonioDTOPesquisa;
    }

    public TipoPatrimonioDTO getTipoPatrimonioDTOPesquisa() {
        return tipoPatrimonioDTOPesquisa;
    }

    public void setTipoPatrimonioDTOPesquisa(TipoPatrimonioDTO tipoPatrimonioDTOPesquisa) {
        this.tipoPatrimonioDTOPesquisa = tipoPatrimonioDTOPesquisa;
    }
    
    public int getOpcao() {
        return opcao;
    }
    
    public void setOpcao(int opcao) {
        this.opcao = opcao;
    }
    
    public String getParametro() {
        return parametro;
    }
    
    public void setParametro(String parametro) {
        this.parametro = parametro;
    }

    public String getDiretorio() {
        return diretorio;
    }

    public void setDiretorio(String diretorio) {
        this.diretorio = diretorio;
    }

    public boolean isValida() {
        return valida;
    }

    public void setValida(boolean valida) {
        this.valida = valida;
    }
    
    public int getTipoRelatorio() {
        return tipoRelatorio;
    }

    public void setTipoRelatorio(int tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
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

    public Long getSetorSelectItem() {
        return setorSelectItem;
    }

    public void setSetorSelectItem(Long setorSelectItem) {
        this.setorSelectItem = setorSelectItem;
    }
    
    public String getTituloLayoutUnit() {
        return tituloLayoutUnit;
    }

    public Date getDataAux() {
        return dataAux == null ? null : (Date) dataAux.clone();
    }

    public void setDataAux(Date dataAux) {
        this.dataAux = dataAux == null ? null : (Date) dataAux.clone();
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }
    
    public int getOpcaoData() {
        return opcaoData;
    }

    public void setOpcaoData(int opcaoData) {
        this.opcaoData = opcaoData;
    }

    public Long getParametroCodigo() {
        return parametroCodigo;
    }

    public void setParametroCodigo(Long parametroCodigo) {
        this.parametroCodigo = parametroCodigo;
    }
        
    public List<SelectItem> getSetorSelectItens() throws Throwable {
        return new SelectBoxUtil().retornaListaEmSelectItem(getSetoresUsuario());
    }
       
    public List<SelectItem> getListaMeses() {        
        return DataUtil.gerarUltimosMeses();
    }
    
    public List<UsuarioPermissaoSetorDTO> getListaUsuarioPermissaoSetor() {
        return UsuarioPermissaoSetorBO.getInstance().pesquisarPorUsuario(ContextoBean.getContexto().getUsuarioLogado());
    }
    
    public List<TipoPatrimonioDTO> getListaTipoPatrimonios() throws Throwable {
        return TipoPatrimonioBO.getInstance().pesquisarTipoPatrimonioPorPatrimonios(getListaPatrimonios());
    }
    
    public List<SetorDTO> getSetoresUsuario() throws Throwable {
        return getListaUsuarioPermissaoSetor().stream().map(usuarioSetorPermissao -> 
                (SetorDTO) usuarioSetorPermissao.getSetorDTO()).collect(Collectors.toList());
    }
    
    public List<PatrimonioDTO> getListaPatrimonios() throws Throwable {
        return PatrimonioBO.getInstance().pesquisarPatrimonioPorSetores(gerarListaIN(getSetoresUsuario()));
    }
    
    public List<LocalizacaoDTO> getListaLocalizacoes() throws Throwable {
        List<LocalizacaoDTO> localizacaoDTOs =  new ArrayList<>();
        for(CampusDTO campus: getListaCampus()){
            localizacaoDTOs.addAll(LocalizacaoBO.getInstance().pesquisarPorCampus(campus));
        }
        return localizacaoDTOs;
    }
    
    public List<UsuarioDTO> getListaUsuarios() throws Throwable {
        return UsuarioBO.getInstance().pesquisarUsuariosPorSetores(gerarListaIN(getSetoresUsuario()));
    }
    
    public List<CampusDTO> getListaCampus() throws Throwable {
        return CampusBO.getInstance().pesquisarCampusPorSetores(gerarListaIN(getSetoresUsuario()));
    }
    
    private String gerarListaIN(List<SetorDTO> lista){        
        String result = "";        
        int contador = 0;        
        for (SetorDTO c : lista){            
            result = result.concat(c.getCodigo().toString());
            if (lista.size() > (contador + 1)){
                result = result.concat(",");
            } 
           contador ++;
        }	
        return result;
    }
}