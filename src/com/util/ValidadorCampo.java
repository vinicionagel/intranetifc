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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe responsável pela validação dos campos de uma entidade. Utilizada para
 * validar atributos de classes prestes a serem inseridas ou atualizadas na base
 * de dados.
 */
public class ValidadorCampo {

    private static String CAMPO_OBRIGATORIO = "Campo %1 deve ser informado";
    private static String CAMPO_TEXTO_CONTEUDO_INVALIDO = "Campo %1 deve possuir um texto válido";
    private static String DATA_ANTERIOR = "Campo %1 deve ser anterior ao campo %2";
    private static String PERIODO_DATA_MIN = "O ano da data informada no campo %1 não pode ser inferior a 1822";
    private static String PERIODO_DATA_MAX = "O ano da data informada no campo %1 não pode ser superior a 2099";
    private static String CAMPO_INVALIDO = "Campo %1 não é válido";
    private static String TAMANHO_CAMPO = "Campo %1 deve ter no mínimo %2 caracteres";
    private static String DATA_ANTERIOR_HOJE = "Campo %1 deve ser anterior à data de hoje";
    private static String SOMENTE_LETRA_E_ESPACO = "Campo %1 deve conter somente letras ou espaço";
    private static String SOMENTE_LETRA_ESPACO_E_NUMERO = "Campo %1 deve conter somente letras, espaço ou números.";
    private static GregorianCalendar dataMin = new GregorianCalendar(1822, 1, 1);
    private static GregorianCalendar dataMax = new GregorianCalendar(2099, 11, 31);

    /**
     * Valida um long verificando se é um número de CPF válido, caso contrário
     * adiciona um erro no formato:
     *
     * <pre>
     * Campo &lt;code&gt;%1&lt;/code&gt; não é válido
     * </pre>
     *
     * onde o
     * <code>%1</code> deve ser substituido pelo
     * <code>nomeCampo</code> caso o
     * <code>valorCampo</code> seja inválido.
     *
     * @param valorCampo o valor a ser verificado
     * @param nomeCampo o nome a ser colocado no lugar de %1
     * @param erros lista de erros a que deve ser adicionado o erro caso exista
     */
    public static void validarCampoCPF(long valorCampo, String nomeCampo,
            List<String> erros) {
        // algorítmo que valida CPF

        int soma, resultado1, resultado2;        
        boolean ret = false;
        int[] numero = new int[11];
        long cpfn = valorCampo;

        String cpf = String.format("%011d", valorCampo);

        for (int cont = 0; cont < cpf.length(); cont++) {
            numero[cont] = Integer.parseInt(String.valueOf(cpf.charAt(cont)));
        }
        if (cpf.length() == 11) {
            if (cpfn != 00000000000 && cpfn != Long.parseLong("11111111111")
                    && cpfn != Long.parseLong("22222222222")
                    && cpfn != Long.parseLong("33333333333")
                    && cpfn != Long.parseLong("44444444444")
                    && cpfn != Long.parseLong("55555555555")
                    && cpfn != Long.parseLong("66666666666")
                    && cpfn != Long.parseLong("77777777777")
                    && cpfn != Long.parseLong("88888888888")
                    && cpfn != Long.parseLong("99999999999")) {
                soma = 10 * numero[0] + 9 * numero[1] + 8 * numero[2] + 7
                        * numero[3] + 6 * numero[4] + 5 * numero[5] + 4
                        * numero[6] + 3 * numero[7] + 2 * numero[8];
                soma -= (11 * ((soma / 11)));
                if (soma == 0 || soma == 1) {
                    resultado1 = 0;
                } else {
                    resultado1 = 11 - soma;
                }
                if (resultado1 == numero[9]) {
                    soma = numero[0] * 11 + numero[1] * 10 + numero[2] * 9
                            + numero[3] * 8 + numero[4] * 7 + numero[5] * 6
                            + numero[6] * 5 + numero[7] * 4 + numero[8] * 3
                            + numero[9] * 2;
                    soma -= (11 * ((soma / 11)));

                    if (soma == 0 || soma == 1) {
                        resultado2 = 0;
                    } else {
                        resultado2 = 11 - soma;
                    }
                    if (resultado2 == numero[10]) {
                        ret = true;
                    } else {
                        ret = false;
                    }
                } else {
                    ret = false;
                }
            }
            if (ret == false) {
                erros.add(CAMPO_INVALIDO.replaceFirst("%1", nomeCampo));
            }
        }
    }
    /**
     * Valida um long verificando se é um número de CNPJ válido, caso contrário
     * adiciona um erro no formato:
     *
     * <pre>
     * Campo &lt;code&gt;%1&lt;/code&gt; não é válido
     * </pre>
     *
     * onde o
     * <code>%1</code> deve ser substituido pelo
     * <code>nomeCampo</code> caso o
     * <code>valorCampo</code> seja inválido.
     *
     * @param valorCampo o valor a ser verificado
     * @param nomeCampo o nome a ser colocado no lugar de %1
     * @param erros lista de erros a que deve ser adicionado o erro caso exista
     */
    private static final String CNPJ_ZERADO = "00000000000000";

    public static void validarCampoCNPJ(final long valorCampo, final String nomeCampo, final List<String> erros) {
        boolean ret;
        NumberFormat nf = new DecimalFormat("00000000000000");
        String cnpj = nf.format(valorCampo);

        if ((cnpj.length() == 14) && (!cnpj.equals(CNPJ_ZERADO))) {
            int soma = 0, dig;
            String cnpj_calc = cnpj.substring(0, 12);
            char[] chr_cnpj = cnpj.toCharArray();

            // --------- Primeira parte
            for (int i = 0; i < 4; i++) {
                if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
                    soma += (chr_cnpj[i] - 48) * (6 - (i + 1));
                }
            }
            for (int i = 0; i < 8; i++) {
                if (chr_cnpj[i + 4] - 48 >= 0 && chr_cnpj[i + 4] - 48 <= 9) {
                    soma += (chr_cnpj[i + 4] - 48) * (10 - (i + 1));
                }
            }
            dig = 11 - (soma % 11);
            cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer.toString(dig);

            // --------- Segunda parte
            soma = 0;
            for (int i = 0; i < 5; i++) {
                if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
                    soma += (chr_cnpj[i] - 48) * (7 - (i + 1));
                }
            }
            for (int i = 0; i < 8; i++) {
                if (chr_cnpj[i + 5] - 48 >= 0 && chr_cnpj[i + 5] - 48 <= 9) {
                    soma += (chr_cnpj[i + 5] - 48) * (10 - (i + 1));
                }
            }
            dig = 11 - (soma % 11);
            cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer.toString(dig);

            ret = cnpj.equals(cnpj_calc);
        } else {
            ret = false;
        }

        if (!ret) {
            erros.add(CAMPO_INVALIDO.replaceFirst("%1", nomeCampo));
        }
    }

    /**
     * Valida uma String verificando ela tem o número de caracteres mínimo
     * permitido, e caso não tenha adiciona um erro no formato:
     *
     *
     * <pre>
     * Campo &lt;code&gt;%1&lt;/code&gt; deve ter no mínimo &lt;code&gt;%2&lt;/code&gt; caracteres
     * </pre>
     *
     * onde o
     * <code>%1</code> deve ser substituido pelo
     * <code>nomeCampo</code> e o
     * <code>%2</code> pelo
     * <code>tamanhoMinimoCampo</code> caso o
     * <code>valorCampo</code> seja inválido.
     *
     * @param tamanhoCampo tamanho mínimo permitido
     * @param valorCampo o valor a ser verificado
     * @param nomeCampo o nome a ser colocado no lugar de %1
     * @param erros lista de erros a que deve ser adicionado o erro caso exista
     */
    public static void validarTamanhoCampo(int tamanhoMinimoCampo,
            String valorCampo, String nomeCampo, List<String> erros) {
        if (valorCampo.length() < tamanhoMinimoCampo) {
            erros.add(TAMANHO_CAMPO.replaceFirst("%1", nomeCampo).replaceFirst(
                    "%2", Integer.toString(tamanhoMinimoCampo)));
        }
    }

    /**
     * Valida uma String verificando se está vazia, e caso esteja adiciona um
     * erro no formato:
     *
     * <pre>
     * Campo &lt;code&gt;%1&lt;/code&gt; deve ser informado
     * </pre>
     *
     * onde o
     * <code>%1</code> deve ser substituido pelo
     * <code>nomeCampo</code> caso o
     * <code>valorCampo</code> seja inválido.
     *
     * @param valorCampo o valor a ser verificado
     * @param nomeCampo o nome a ser colocado no lugar de %1
     * @param erros lista de erros a que deve ser adicionado o erro caso exista
     */
    public static void validarCampoVazio(String valorCampo, String nomeCampo,
            List<String> erros) {
        if (StringUtils.isEmpty(valorCampo)) {
            erros.add(CAMPO_OBRIGATORIO.replaceFirst("%1", nomeCampo));
        }
        validarCampoTexto(valorCampo, nomeCampo, erros);
    }

    /**
     *
     * Valida de a String passada é valida, verificando se existe alguma letra
     * ou número. retorna mensagem de erro se não for encontrada nem uma letra
     * ou número.
     *
     * <pre>
     * Campo &lt;code&gt;%1&lt;/code&gt; deve possuir um texto válido
     * </pre>
     *
     * obs.: este método deve ser utilizado em campos de texto que não são
     * obrigatórios.
     *
     * @param valorCampo o valor a ser verificado
     * @param nomeCampo o nome a ser colocado no lugar de %1
     * @param erros lista de erros a que deve ser adicionado o erro caso exista
     */
    public static void validarCampoTexto(String valorCampo, String nomeCampo, List<String> erros) {
        if (!StringUtils.isEmpty(valorCampo) && valorCampo != null) {
            StringBuilder host = new StringBuilder();

            Pattern pattern = Pattern.compile("[[A-Za-z0-9áéíóúàèìòùãẽĩõũâêôôûÁÉÍÓÚÀÈÌÒÙÃẼĨÕŨÂÊÎÔÛ]]");
            Matcher matcher = pattern.matcher(valorCampo);

            while (matcher.find()) {
                // imprimi o retorno tratado
                host.append(matcher.group());
            }
            if (StringUtils.isEmpty(host.toString())) {
                erros.add(CAMPO_TEXTO_CONTEUDO_INVALIDO.replaceFirst("%1", nomeCampo));
            }
        }
    }

    /**
     * Valida um Object verificando se está null, e caso esteja adiciona um erro
     * no formato:
     *
     * <pre>
     * Campo &lt;code&gt;%1&lt;/code&gt; deve ser informado
     * </pre>
     *
     * onde o
     * <code>%1</code> deve ser substituido pelo
     * <code>nomeCampo</code> caso o
     * <code>valorCampo</code> seja inválido.
     *
     * @param valorCampo o valor a ser verificado
     * @param nomeCampo o nome a ser colocado no lugar de %1
     * @param erros lista de erros a que deve ser adicionado o erro caso exista
     */
    public static void validarCampoNulo(Object valorCampo, String nomeCampo,
            List<String> erros) {
        if (valorCampo == null) {
            erros.add(CAMPO_OBRIGATORIO.replaceFirst("%1", nomeCampo));
        }
    }

    /**
     * Valida um int verificando se está igual a zero, e caso esteja adiciona um
     * erro no formato:
     *
     * <pre>
     * Campo &lt;code&gt;%1&lt;/code&gt; deve ser informado
     * </pre>
     *
     * onde o
     * <code>%1</code> deve ser substituido pelo
     * <code>nomeCampo</code> caso o
     * <code>valorCampo</code> seja inválido.
     *
     * @param valorCampo o valor a ser verificado
     * @param nomeCampo o nome a ser colocado no lugar de %1
     * @param erros lista de erros a que deve ser adicionado o erro caso exista
     */
    public static void validarCampoInteiro(int valorCampo, String nomeCampo,
            List<String> erros) {
        if (valorCampo == 0) {
            erros.add(CAMPO_OBRIGATORIO.replaceFirst("%1", nomeCampo));
        }
    }

    /**
     * Valida um long verificando se está igual a zero, e caso esteja adiciona
     * um erro no formato:
     *
     * <pre>
     * Campo &lt;code&gt;%1&lt;/code&gt; deve ser informado
     * </pre>
     *
     * onde o
     * <code>%1</code> deve ser substituido pelo
     * <code>nomeCampo</code> caso o
     * <code>valorCampo</code> seja inválido.
     *
     * @param valorCampo o valor a ser verificado
     * @param nomeCampo o nome a ser colocado no lugar de %1
     * @param erros lista de erros a que deve ser adicionado o erro caso exista
     */
    public static void validarCampoLong(long valorCampo, String nomeCampo,
            List<String> erros) {
        if (valorCampo == 0) {
            erros.add(CAMPO_OBRIGATORIO.replaceFirst("%1", nomeCampo));
        }
    }

    /**
     * Valida um double verificando se está igual a zero, e caso esteja adiciona
     * um erro no formato:
     *
     * <pre>
     * Campo &lt;code&gt;%1&lt;/code&gt; deve ser informado
     * </pre>
     *
     * onde o
     * <code>%1</code> deve ser substituido pelo
     * <code>nomeCampo</code> caso o
     * <code>valorCampo</code> seja inválido.
     *
     * @param valorCampo o valor a ser verificado
     * @param nomeCampo o nome a ser colocado no lugar de %1
     * @param erros lista de erros a que deve ser adicionado o erro caso exista
     */
    public static void validarCampoDouble(double valorCampo, String nomeCampo,
            List<String> erros) {
        if (valorCampo == 0) {
            erros.add(CAMPO_OBRIGATORIO.replaceFirst("%1", nomeCampo));
        }
    }

    /**
     * Valida uma coleção de objetos verificando se está vazia, e caso esteja
     * adiciona um erro no formato:
     *
     * <pre>
     * Campo &lt;code&gt;%1&lt;/code&gt; deve ser informado
     * </pre>
     *
     * onde o
     * <code>%1</code> deve ser substituido pelo
     * <code>nomeCampo</code> caso o
     * <code>valorCampo</code> seja inválido.
     *
     * @param valorCampo o valor a ser verificado
     * @param nomeCampo o nome a ser colocado no lugar de %1
     * @param erros lista de erros a que deve ser adicionado o erro caso exista
     */
    public static void validarCampoCollection(Collection<?> valorCampo,
            String nomeCampo, List<String> erros) {
        if (valorCampo == null || valorCampo.isEmpty()) {
            erros.add(CAMPO_OBRIGATORIO.replaceFirst("%1", nomeCampo));
        }
    }

    /**
     * Valida dois objetos Date verificando se estão em ordem cronológica
     * correta, e caso esteja adiciona o erro
     * <code>mensagem</code>.
     *
     * <pre>
     * Campo &lt;code&gt;%1&lt;/code&gt; deve ser anterior ao campo &lt;code&gt;%2&lt;/code&gt;
     * </pre>
     *
     *
     * @param inicio o valor do primeiro Date a ser validado.
     * @param nomeCampoInicio o nome a ser colocado no lugar de %1
     * @param fim o valor do segundo Date a ser validado.
     * @param nomeCampoFim o nome a ser colocado no lugar de %2
     * @param erros lista de erros a que deve ser adicionado o erro caso exista.
     */
    public static void validarDatas(Date inicio, String nomeCampoInicio, Date fim, String nomeCampoFim, List<String> erros) {
        if (inicio == null || fim == null || inicio.after(fim)) {
            erros.add(DATA_ANTERIOR.replaceFirst("%1", nomeCampoInicio).replaceFirst("%2", nomeCampoFim));
        } else {
            validarDataMinMax(inicio, nomeCampoInicio, erros);
            validarDataMinMax(fim, nomeCampoFim, erros);
        }
    }

    /**
     * Valida dois objetos Calendar verificando se estão em ordem cronológica
     * correta, e caso esteja adiciona o erro
     * <code>mensagem</code>.
     *
     * <pre>
     * Campo &lt;code&gt;%1&lt;/code&gt; deve ser anterior ao campo &lt;code&gt;%2&lt;/code&gt;
     * </pre>
     *
     *
     * @param inicio o valor do primeiro Calendar a ser validado.
     * @param nomeCampoInicio o nome a ser colocado no lugar de %1
     * @param fim o valor do segundo Calendar a ser validado.
     * @param nomeCampoFim o nome a ser colocado no lugar de %2
     * @param erros lista de erros a que deve ser adicionado o erro caso exista.
     */
    public static void validarDatas(Calendar inicio, String nomeCampoInicio, Calendar fim, String nomeCampoFim, List<String> erros) {
        validarDatas(inicio.getTime(), nomeCampoInicio, fim.getTime(), nomeCampoFim, erros);
    }

    /**
     * Valida dois objetos Date verificando se estão em ordem cronológica
     * correta, e caso esteja adiciona o erro
     * <code>mensagem</code>.
     *
     * <pre>
     * Campo &lt;code&gt;%1&lt;/code&gt; deve ser anterior a data de hoje
     * </pre>
     *
     *
     * @param data o valor do Date a ser validado.
     * @param nomeCampoInicio o nome a ser colocado no lugar de %1
     * @param erros lista de erros a que deve ser adicionado o erro caso exista.
     */
    public static void validarDataAnteriorHoje(Date data, String nomeCampo, List<String> erros) {
        if (data == null || data.after(new Date())) {
            erros.add(DATA_ANTERIOR_HOJE.replaceFirst("%1", nomeCampo));
        } else {
            validarDataMinMax(data, nomeCampo, erros);
        }
    }

    /**
     * Valida dois objetos Calendar verificando se estão em ordem cronológica
     * correta, e caso esteja adiciona o erro
     * <code>mensagem</code>.
     *
     * <pre>
     * Campo &lt;code&gt;%1&lt;/code&gt; deve ser anterior a data de hoje
     * </pre>
     *
     *
     * @param data o valor do Calendar a ser validado.
     * @param nomeCampoInicio o nome a ser colocado no lugar de %1
     * @param erros lista de erros a que deve ser adicionado o erro caso exista.
     */
    public static void validarDataAnteriorHoje(Calendar data, String nomeCampo, List<String> erros) {
        validarDataAnteriorHoje(data.getTime(), nomeCampo, erros);
    }

    /**
     * Valida dois objetos Date verificando se estão em ordem cronológica
     * correta, e caso esteja adiciona o erro mensagem.
     *
     * Campo %1 deve ser anterior ao campo %2
     *
     * Caso a data Final seja opcional, e não tenha sido informada, apenas
     * valida se a data inicial foi preenchida, e caso não tenha sido adiciona o
     * erro mensagem:
     *
     * Campo %1 deve ser informado
     *
     * @param inicio o valor do primeiro Date a ser validado.
     * @param nomeCampoInicio o nome a ser colocado no lugar de %1
     * @param fim o valor do segundo Date a ser validado.
     * @param nomeCampoFim o nome a ser colocado no lugar de %2
     * @param erros lista de erros a que deve ser adicionado o erro caso exista.
     * @param dataFimOpcional determina se a data final é opcional
     */
    public static void validarDatas(Date inicio, String nomeCampoInicio,
            Date fim, String nomeCampoFim, List<String> erros,
            boolean dataFimOpcional) {
        if (dataFimOpcional) {
            if (fim == null) {
                validarCampoNulo(inicio, nomeCampoInicio, erros);
            } else {
                validarDatas(inicio, nomeCampoInicio, fim, nomeCampoFim, erros);
            }
        } // Se a dataFim nao é opcional, apenas valida as datas
        else {
            validarDatas(inicio, nomeCampoInicio, fim, nomeCampoFim, erros);
        }
    }

    /**
     * Valida se o objeto Date passado está entre do período de 07/09/1822 e
     * 31/12/2099.
     *
     * Se a data informada for menor que 07/09/28: O ano da data informada no
     * campo %1 não pode ser menor que 1822
     *
     * Se a data informada for maior que 31/12/2099
     *
     * @param data o objeto Date a ser validado.
     * @param nomeCampo o nome a ser colocado no lugar de %1
     * @param erros lista de erros a que deve ser adicionado o erro caso exista.
     */
    public static void validarDataMinMax(Date data, String nomeCampo, List<String> erros) {
        if (data == null) {
            erros.add(CAMPO_OBRIGATORIO.replaceFirst("%1", nomeCampo));
        } else {
            if (data.before(dataMin.getTime())) {
                erros.add(PERIODO_DATA_MIN.replaceFirst("%1", nomeCampo));
            }
            if (data.after(dataMax.getTime())) {
                erros.add(PERIODO_DATA_MAX.replaceFirst("%1", nomeCampo));
            }
        }
    }

    /**
     * Valida uma String verificando se ela possui os caracteres de uma
     * expressão regular, e caso não possua adiciona o erro
     * <code>mensagem</code>.
     *
     * @param expressaoRegular expressão regular verificadora
     * @param campo o valor a ser validado
     * @param mensagem a mensagem a ser mandado para a lista de erros.
     * @param erros lista de erros a que deve ser adicionado o erro caso exista
     */
    public static void validarCampoPorExpressaoRegular(String expressaoRegular,
            String campo, String mensagem, List<String> erros) {
        final Pattern padrao = Pattern.compile(expressaoRegular);
        final Matcher pesquisa = padrao.matcher(campo);
        if (!pesquisa.matches()) {
            erros.add(mensagem);
        }
    }

    /**
     * Valida uma String verificando ela tem somente letras, acentuadas ou não,
     * maiúsculas ou minúsculas, e espaço, e caso não tenha adiciona um erro no
     * formato:
     *
     * <pre>
     * Campo &lt;code&gt;%1&lt;/code&gt; deve conter somente letras ou espaços
     * </pre>
     *
     * onde o
     * <code>%1</code> deve ser substituido pelo
     * <code>nomeCampo</code>
     *
     * @param valorCampo o valor a ser verificado
     * @param nomeCampo o nome a ser colocado no lugar de %1
     * @param erros lista de erros a que deve ser adicionado o erro caso exista
     */
    public static void validaSomenteLetraEEspaco(String valorCampo,
            String nomeCampo, List<String> erros) {
        if (!StringUtils.temSomenteLetraEEspaco(valorCampo)) {
            erros.add(SOMENTE_LETRA_E_ESPACO.replaceFirst("%1", nomeCampo));
        }
    }

    /**
     * Valida uma String verificando ela tem somente letras, acentuadas ou não,
     * maiúsculas ou minúsculas, espaço e números, caso não tenha adiciona um
     * erro no formato:
     *
     * <pre>
     * Campo &lt;code&gt;%1&lt;/code&gt; deve conter somente letras ou espaços
     * </pre>
     *
     * onde o
     * <code>%1</code> deve ser substituido pelo
     * <code>nomeCampo</code>
     *
     * @param valorCampo o valor a ser verificado
     * @param nomeCampo o nome a ser colocado no lugar de %1
     * @param erros lista de erros a que deve ser adicionado o erro caso exista
     */
    public static void validaSomenteLetraEspacoENumero(String valorCampo,
            String nomeCampo, List<String> erros) {
        if (!StringUtils.temSomenteLetraEspacoENumero(valorCampo)) {
            erros.add(SOMENTE_LETRA_ESPACO_E_NUMERO.replaceFirst("%1", nomeCampo));
        }
    }
}