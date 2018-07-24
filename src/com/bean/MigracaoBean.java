package com.bean;

import com.bo.AgendamentoBO;
import com.bo.AlunoBO;
import com.bo.ConfiguracaoBancoBO;
import com.bo.CursoBO;
import com.bo.DiaSemanaBO;
import com.bo.EnderecoBO;
import com.bo.MunicipioBO;
import com.bo.NecessidadeEspecialBO;
import com.bo.SexoBO;
import com.bo.TipoLogradouroBO;
import com.bo.TipoSanguineoBO;
import com.bo.TipoTelefoneBO;
import com.dto.AgendamentoDTO;
import com.dto.AlunoDTO;
import com.dto.ConfiguracaoBancoDTO;
import com.dto.CursoDTO;
import com.dto.DiaSemanaDTO;
import com.dto.EnderecoDTO;
import com.dto.TelefoneDTO;
import com.util.ConnectionFactory;
import com.util.DataUtil;
import static com.util.FacesUtil.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Named(value = "migracaoBean")
@ViewScoped
public class MigracaoBean  implements Serializable, Job{
    
    private HashSet<String> cursos = new HashSet<>();	
    private float progress;
    private Connection conecta;
    private int indiceMaximo;
    private int indiceAtual;
    private boolean aparecePanelAgendamento;
    private boolean aparecePanelMigracao;
    private ConfiguracaoBancoDTO configuracaoBancoDTO;
    private static final long serialVersionUID = 20000L;
    private List<DiaSemanaDTO> diasSelecionados = new ArrayList<>();
    private Date horarioAgendamento;
    private AgendamentoDTO agendamentoExcluir;
    private final static String contexto = "/.imagesTempfotosLogin";
    private boolean carregou;
    private Date dataCriacaoBanco;
    
    public String getTituloLayoutUnit(){
        return "Migracao";
    }
        
    public MigracaoBean() {
       configuracaoBancoDTO = ConfiguracaoBancoBO.getInstance().pesquisarPorTipoConfiguracao(2);
    }
         
    public void testaConexao(){
        conecta = ConnectionFactory.conectar(configuracaoBancoDTO);
        adicionarMensagemAviso("Conexão OK!");
    }
    public void atualizarBaseDeDados(){
        ConfiguracaoBancoBO.getInstance().update(configuracaoBancoDTO);
        editadoSucesso();
    }
    
     private String montarSQLAlunos() {				
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT pf.data_nascimento AS r1, pf.nome_pai AS r2, pf.nome_mae AS r3, pf.email AS r4, pf.sexo_id AS r5, ");
                sql.append("pf.tipo_sanguineo_id AS r6, pf.nome AS r7, pf.foto as r8, e.tipo_logradouro_id AS r9, e.municipio_id AS r10, e.nome_logradouro AS r11, ");	
                sql.append("e.complemento AS r12, e.caixa_postal AS r13, e.bairro AS r14, e.cep AS r15, e.numero AS r16, pf.id AS r17 ");
        sql.append("FROM pessoa_fisica pf ");
                        sql.append("INNER JOIN (SELECT DISTINCT a.pessoa_fisica_id AS pessoa_fisica_id, a.idea AS idea, max(m.data_matricula) OVER (PARTITION BY a.pessoa_fisica_id, a.idea) AS dataMatri, ");
                                sql.append("max(c.nome) OVER (PARTITION BY a.pessoa_fisica_id, a.idea) AS curso, max(c.tempo_minimo) OVER (PARTITION BY a.pessoa_fisica_id, a.idea) AS minCurso ");
                                sql.append("FROM matricula m ");
                                        sql.append("INNER JOIN status_aluno_curso sac ON m.status_aluno_curso_id = sac.id ");
                                        sql.append("LEFT JOIN (matriz_curricular mc LEFT JOIN curso c ON (c.id = mc.curso_id)) ON mc.id = m.matriz_curricular_id, aluno a ");
                                        sql.append("WHERE a.pessoa_fisica_id = m.aluno_id ");
                                        sql.append("AND sac.id IN (1, 7, 8, 12) ");
                                        sql.append("AND c.unidade_organizacional_id IN (SELECT id FROM elemento_organizacional WHERE nome LIKE '%RSL%' OR nome LIKE '%RIO DO SUL%')) AS al ON pf.id = al.pessoa_fisica_id ");					
                        sql.append("LEFT JOIN (endereco e LEFT JOIN (municipio mu LEFT JOIN unidade_federativa uf ON (mu.unidade_federativa_id = uf.id)) ON (e.municipio_id = mu.id)) ON pf.endereco_id = e.id ");
                        sql.append("LEFT JOIN estado_civil ec ON pf.estado_civil_id = ec.id	");
                        sql.append("LEFT JOIN sexo s ON pf.sexo_id = s.id ");
                        sql.append("LEFT JOIN pais p ON pf.pais_nacionalidade_id = p.id ");
                        sql.append("LEFT JOIN (SELECT * FROM documento_identificacao WHERE tipo_doc_identificacao = 'RG') AS rgs ON pf.id = rgs.pessoa_fisica_id ");
                        sql.append("LEFT JOIN (SELECT * FROM documento_identificacao WHERE tipo_doc_identificacao = 'CPF') AS cpfs ON pf.id = cpfs.pessoa_fisica_id ");
                        sql.append("LEFT JOIN grau_formacao gf ON pf.grau_formacao_id = gf.id ");
                        sql.append("LEFT JOIN (SELECT pft.pessoa_fisica_id AS idPessoa, MAX(t.numero) AS telPessoa FROM pessoa_fisica_telefone pft, telefone t, pessoa_fisica pf2 ");
                                sql.append("WHERE pft.pessoa_fisica_id = pf2.id AND pft.telefone_id = t.id GROUP BY idPessoa) as tel ON pf.id = tel.idPessoa ");
        sql.append("WHERE al.pessoa_fisica_id IS NOT NULL");
        return sql.toString();	
    }	

    public Date getDataCriacaoBanco(){
        return dataCriacaoBanco;
    }  
    
    private void buscaDataCriacaoBanco(){
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT (pg_stat_file('base/'||oid||'/PG_VERSION')).modification ");
            sql.append("FROM pg_database");
            sql.append(" WHERE datname = '").append(configuracaoBancoDTO.getNomeBanco()).append("'");
            conecta = ConnectionFactory.conectar(configuracaoBancoDTO);
            Statement statement = conecta.createStatement(
            ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = statement.executeQuery(sql.toString());
            while(rs.next()){
                dataCriacaoBanco = rs.getDate(1);
            }
        }catch(Exception e){
            dataCriacaoBanco = null;
        }
        
    }
     
    private void montarListaUsuarios() {
        try {		                
            conecta = ConnectionFactory.conectar(configuracaoBancoDTO);
            Statement statement = conecta.createStatement(
            ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY);
            montarListaUsuarios(statement.executeQuery(montarSQLAlunos()));
        } catch (Exception ex) {
             adicionarMensagemErro("Ocorreu algum erro na conexao com o banco de dados...");
        }
    }
    
    public void migrarDados(){
        montarListaUsuarios();
        salvarDadosTxtLog();
    }

    private EnderecoDTO setEndereco(ResultSet rs) throws SQLException{
        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setTipoLogradouroDTO(TipoLogradouroBO.getInstance().findById(rs.getInt(9)));
        enderecoDTO.setMunicipioDTO(MunicipioBO.getInstance().findById(rs.getInt(10)));
        enderecoDTO.setNomeLogradouro(rs.getString(11));
        enderecoDTO.setComplemento(rs.getString(12));
        enderecoDTO.setCaixaPostal(rs.getString(13));
        enderecoDTO.setBairro(rs.getString(14));
        enderecoDTO.setCep(rs.getInt(15));
        enderecoDTO.setNumero(rs.getString(16));
        return enderecoDTO;
    }
    
    public void initMyValue() {
        buscaDataCriacaoBanco();
        setCarregou(true);
    }
    
    private void montarListaUsuarios(ResultSet rs){		
        try {
            float rowcount = 0;
            rs.last();
            rowcount = rs.getRow();
            indiceMaximo = rs.getRow();
            rs.beforeFirst(); 
            rowcount = 100/rowcount;
            while (rs.next()){
                setProgress(rs.getRow()*rowcount);
                indiceAtual = rs.getRow();
                AlunoDTO alunoDTO = new AlunoDTO();
                alunoDTO.setDataNascimento(rs.getDate(1));
                alunoDTO.setNomePai(rs.getString(2));
                alunoDTO.setNomeMae(rs.getString(3));
                alunoDTO.setEmail(rs.getString(4));
                alunoDTO.setSexoDTO(SexoBO.getInstance().findById(rs.getInt(5)));
                alunoDTO.setTipoSanguineoDTO(TipoSanguineoBO.getInstance().findById(rs.getInt(6)));
                alunoDTO.setNome(rs.getString(7));
                alunoDTO.setIdSiga(rs.getInt(17));
                AlunoDTO alunoAux = AlunoBO.getInstance().pesquisarPorIdSiga(rs.getInt(17));
                if(alunoAux ==  null){
                    EnderecoDTO endereco = setEndereco(rs);
                    EnderecoBO.getInstance().save(endereco);
                    alunoDTO.setEnderecoDTO(endereco);
                    AlunoBO.getInstance().save(alunoDTO);
                }else{
                    EnderecoDTO endereco = setEndereco(rs);
                    endereco.setCodigo(alunoAux.getEnderecoDTO().getCodigo());
                    alunoDTO.setCodigo(alunoAux.getCodigo());
                    EnderecoBO.getInstance().update(endereco);
                    alunoDTO.setEnderecoDTO(endereco);
                }
                montarListaNecessidadeEspecial(rs.getInt(17), alunoDTO);
                montarListaTelefones(rs.getInt(17), alunoDTO);
                montarListaCursos(rs.getInt(17), alunoDTO);
                AlunoBO.getInstance().update(alunoDTO);
            }
            conecta.close();
            adicionarMensagemAviso("Migração completada com sucesso!");
        } catch (SQLException e) {
            Logger.getLogger(MigracaoBean.class.getName()).log(Level.SEVERE, null, e);
        }		
    }

    private String montarSQLCursos(int id){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT c.nome FROM curso AS c , matricula AS m, matriz_curricular  AS mc WHERE  c.id = mc.curso_id AND mc.id = m.matriz_curricular_id AND m.aluno_id = ");
        sql.append(id);
        return sql.toString();
    }

    private void montarListaCursos(int id, AlunoDTO alunoDTO) {
        try {		                     			
            montarListaCursos(conecta.prepareStatement(montarSQLCursos(1)).executeQuery(), alunoDTO);
        } catch (SQLException | ParseException e) {
            Logger.getLogger(MigracaoBean.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void montarListaCursos(ResultSet rs, AlunoDTO alunoDTO) throws ParseException {		
        try {									
            while (rs.next()) {
                if(!cursos.contains(rs.getString(1))){
                    CursoDTO cursoDTO = new CursoDTO();
                    cursoDTO.setDescricao(rs.getString(1));
                    CursoBO.getInstance().save(cursoDTO);
                    alunoDTO.getCursos().add(cursoDTO);
                    cursos.add(rs.getString(1));
                }else{
                    alunoDTO.getCursos().add(CursoBO.getInstance().pesquisarPorDescricao(rs.getString(1)));
                }
            }
        }catch (SQLException e) {	
            Logger.getLogger(MigracaoBean.class.getName()).log(Level.SEVERE, null, e);
        }		
    }
    
    //Telefone        
    private String montarSQLTelefones(int id){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT tel.tipo_telefone_id, tel.id, tel.numero, tel.ramal FROM telefone as tel, pessoa_fisica as pf, pessoa_fisica_telefone as pft WHERE tel.id = pft.telefone_id AND  pft.pessoa_fisica_id = pf.id AND pf.id = ");
        sql.append(id);
        return sql.toString();
    }

    private void montarListaTelefones(int id, AlunoDTO aluno) {
        try {		                     			
            montarListaTelefones(conecta.prepareStatement(montarSQLTelefones(1)).executeQuery(), aluno);
        } catch (SQLException | ParseException e) { 
            Logger.getLogger(MigracaoBean.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void montarListaTelefones(ResultSet rs, AlunoDTO aluno) throws ParseException {		
        try {									
            while (rs.next()) {	
                TelefoneDTO telefoneDTO = new TelefoneDTO();
                telefoneDTO.setTipoTelefoneDTO(TipoTelefoneBO.getInstance().findById(rs.getInt(1)));
                telefoneDTO.setCodigoArea(rs.getInt(2));
                telefoneDTO.setNumero(rs.getInt(3));
                telefoneDTO.setRamal(rs.getInt(4));
                telefoneDTO.setAlunoDTO(aluno);
                aluno.getTelefones().add(telefoneDTO);
            }
        }catch (SQLException e) {
            Logger.getLogger(MigracaoBean.class.getName()).log(Level.SEVERE, null, e);
        }		
    }

    //NecessidadeEspecial
    private String montarSQLNecessidadeEspecial(int id){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ne.id FROM necessidade_especial AS ne, pessoa_fisica_necessidade_especial AS pfne, pessoa_fisica AS pf WHERE pfne.necessidade_especial_id = ne.id AND pfne.pessoa_fisica_id = pf.id AND pf.id = ");
        sql.append(id);
        return sql.toString();
    }

    private void montarListaNecessidadeEspecial(int id, AlunoDTO aluno) {
        try {		                     			
            montarListaNecessidadeEspecial(conecta.prepareStatement(montarSQLNecessidadeEspecial(1)).executeQuery(), aluno);
        } catch (SQLException | ParseException e) { 
            Logger.getLogger(MigracaoBean.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void montarListaNecessidadeEspecial(ResultSet rs, AlunoDTO aluno) throws ParseException {		
        try {									
             while (rs.next()) {	
                aluno.getNecessidadeEspecial().add(NecessidadeEspecialBO.getInstance().findById(rs.getInt(1)));
            }
        }catch (SQLException e) {
            Logger.getLogger(MigracaoBean.class.getName()).log(Level.SEVERE, null, e);
        }		
    }
    
    public void criarAgendamento(){
        AgendamentoBO agendamentoBO = AgendamentoBO.getInstance();
        for (DiaSemanaDTO dia: diasSelecionados){
            agendamentoBO.save(new AgendamentoDTO(dia, horarioAgendamento));
        }
    }
    
    public void excluirAgendamento(){
        AgendamentoBO.getInstance().remove(agendamentoExcluir);
        excluidoSucesso();
    }
    
    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public int getIndiceMaximo() {
        return indiceMaximo;
    }

    public void setIndiceMaximo(int indiceMaximo) {
        this.indiceMaximo = indiceMaximo;
    }

    public int getIndiceAtual() {
        return indiceAtual;
    }

    public void setIndiceAtual(int indiceAtual) {
        this.indiceAtual = indiceAtual;
    }
    
    
    public boolean isAparecePanelAgendamento() {
        return aparecePanelAgendamento;
    }

    public void setAparecePanelAgendamento(boolean aparecePanelAgendamento) {
        this.aparecePanelAgendamento = aparecePanelAgendamento;
    }

    public boolean isAparecePanelMigracao() {
        return aparecePanelMigracao;
    }

    public void setAparecePanelMigracao(boolean aparecePanelMigracao) {
        this.aparecePanelMigracao = aparecePanelMigracao;
    }
    
    public ConfiguracaoBancoDTO getConfiguracaoBancoDTO() {
        return configuracaoBancoDTO;
    }

    public void setConfiguracaoBancoDTO(ConfiguracaoBancoDTO configuracaoBancoDTO) {
        this.configuracaoBancoDTO = configuracaoBancoDTO;
    }
    
    public List<DiaSemanaDTO> getDias(){
        return DiaSemanaBO.getInstance().findAll();
    }
    
    public List<DiaSemanaDTO> getDiasSelecionados() {
        return diasSelecionados;
    }

    public void setDiasSelecionados(List<DiaSemanaDTO> diasSelecionados) {
        this.diasSelecionados = diasSelecionados;
    }

    public Date getHorarioAgendamento() {
        return horarioAgendamento;
    }

    public void setHorarioAgendamento(Date horarioAgendamento) {
        this.horarioAgendamento = horarioAgendamento;
    }
    
    public List<AgendamentoDTO> getAgendamentos(){
        return AgendamentoBO.getInstance().findAllOrdenadoPorDia();
    }

    public AgendamentoDTO getAgendamentoExcluir() {
        return agendamentoExcluir;
    }

    public void setAgendamentoExcluir(AgendamentoDTO agendamentoExcluir) {
        this.agendamentoExcluir = agendamentoExcluir;
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        migrarDados();
        salvarDadosTxtLog();
    }
    
    public String getDataHoraStringUltimaAtualizacao(){
        try {
            FileInputStream fis = new FileInputStream(retornaStringCaminho());
            String fileContent = new Scanner(fis).useDelimiter("\\Z").next();
            return fileContent.substring(fileContent.lastIndexOf("\n"));
        } catch (FileNotFoundException fileNotFoundException) {
            return "";
        }
    }
    
    private String retornaStringCaminho(){
        StringBuilder builder = new StringBuilder();
        builder.append("/opt/intranet");
        builder.append(File.separator);
        builder.append("logIntranet.txt");
        return builder.toString();
    }
    
    private void salvarDadosTxtLog(){
        try{
            File arquivo = new File(retornaStringCaminho());
            if (!arquivo.exists()) {
                arquivo.createNewFile();
            }
            FileWriter fw = new FileWriter(arquivo, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.newLine();
            bw.append(DataUtil.dataHoraForStringPadrao(new Date()));
            bw.close();
            fw.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean getCarregou() {
        return carregou;
    }

    public void setCarregou(boolean carregou) {
        this.carregou = carregou;
    }
    
    
    
}
