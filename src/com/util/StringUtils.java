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

import java.text.Normalizer;

public class StringUtils {

    /**
     * retorna "" se a string for nula, vazia 
     * ou possuir somente espacos. Senao, retorna
     * a propria string
     */
    public static String checkNull(String str) {
        //se for nula ou vazia
        return (str == null)||(str.trim().equals("")) ? "" : str; 
    }
    /**
     * retorna "TRUE" se a string for nula, vazia 
     * ou possuir somente espacos. 
     */
    public static boolean isEmpty(String str) {
        //verifica a string informada
        String aux = checkNull(str);
        //retorna TRUE se ela for nula, vazia ou somente espacos
        return aux.equals(""); 
    }
    
    /**
     * Compara a igualdade entre duas Strings passadas como parâmetros 
     * 
     * 
     * @param str1
     * @param str2
     * @return <code>true</code> se as Strings passadas como parâmetros são iguais
     * 			<code>false</code> caso contrário
     */
    public static boolean isEquals(String str1, String str2){
    	return ((str1 == null && str2 == null) || (str1 != null && !str1.equals(str2)));
    }
    
    /**
     * Verifica se a string tem somente letras, acentuadas ou não, maiúsculas ou minúsculas, e espaço.
     * 
     * @param string string a ser avaliada
     * @return  <code>true</code> se a String for válida
     * 			<code>false</code> caso contrário
     */
    public static boolean temSomenteLetraEEspaco(String string) {
		return string.matches("^[a-zA-ZçÇáéíóúýÁÉÍÓÚÝàèìòùÀÈÌÒÙãõñäëïöüÿÄËÏÖÜÃÕÑâêîôûÂÊÎÔÛ\\s]+$");
	}

    /**
     * Verifica se a string tem somente letras, acentuadas ou não, maiúsculas ou minúsculas, espaço e números.
     * Inclui também o ponto(.)
     * 
     * @param string string a ser avaliada
     * @return  <code>true</code> se a String for válida
     * 			<code>false</code> caso contrário
     */
    public static boolean temSomenteLetraEspacoENumero(String string) {
		return string.matches("^[0-9a-zA-Z.\\s]+$");
	}

    /**
     * Remove todos os acentos da string passada como parâmetro
     * 
     * @param acentuada string a ter os acentos removidos
     * @return nova string sem os acentos
     */
	public static String removerAcentos(String acentuada) {
		CharSequence cs = new StringBuilder(acentuada);
		return Normalizer.normalize(cs, Normalizer.Form.NFKD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}
    
    
}