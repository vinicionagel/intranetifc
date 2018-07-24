/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util;
import com.bo.ConfiguracaoBancoBO;
import com.dto.ConfiguracaoBancoDTO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Uma fábrica de conexões.
 *
 * @author David Buzatto
 */
public class ConnectionFactory {

    /*
     * Este bloco estático será executado assim que esta classe for carregada,
     * sendo assim, será executado apenas uma vez.
     */
    static {
        try {
            /*
             * Carrega a classe com.mysql.jdbc.Driver, que é a implementação
             * do driver JDBC para o MySQL.
             */
            Class.forName( "org.postgresql.Driver" );

            // caso a classe não seja encontrada
        } catch ( ClassNotFoundException exc ) {

            /*
             * Como log usaremos o stacktrace das excessões, mas recomendo
             * que para um projeto real você utilize algum mecanismo de log
             * melhor, como o Log4J por exemplo.
             */
            exc.printStackTrace();

        }
    }

    /**
     * O método getConnection retorna uma conexão com o banco de dados baseado
     * nos parâmetros fornecidos.
     *
     * @param url O endereço da base de dados.
     * @param usuario O usuário que tem permissão na base de dados especificada.
     * @param senha A senha do usuário especificado
     * @return Uma conexão com o banco de dados especificado na url.
     * @throws SQLException Caso ocorra algum problema durante a conexão.
     */
    public static Connection getConnection(
            String url,
            String usuario,
            String senha ) throws SQLException {
        DriverManager.setLoginTimeout(1);
        return DriverManager.getConnection( url, usuario, senha );

    }

    /**
     * Obtém uma conexão para a base de dados sakila.
     *
     * @return Uma conexão para a base de dados sakila.
     * @throws SQLException Caso ocorra algum problema durante a conexão.
     */
    public static Connection getPostgresConnection() throws SQLException {

        return getConnection(
                "jdbc:postgresql://localhost:5432/intranetifc",
                "postgres",
                "postgres" );

    }
    /**
     * 
     * @param codigoTipoBanco 2 para o banco do siga
     * @return a conexao com o determinado banco
     */
    public static Connection conectar(ConfiguracaoBancoDTO configuracaoBancoParameter) {
        try {
            DriverManager.setLoginTimeout(1); //TEMPO MÁXIMO DE CONEXAO!
            return DriverManager.getConnection(configuracaoBancoParameter.toString(), configuracaoBancoParameter.getUsuario(), configuracaoBancoParameter.getSenha());
        } catch (Exception ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
