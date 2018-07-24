package com.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.model.SelectItem;

public class DataUtil {

    static final Logger logger = Logger.getLogger(DataUtil.class.getName());
    private static Locale currentLocale;

    public DataUtil() {
        super();
        DataUtil.currentLocale = new Locale("pt", "BR");
    }

    public DataUtil(Locale locale) {
        super();
        DataUtil.currentLocale = locale;
    }

    public static String dataForStringXML(Date pDate) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String s = "";
        try {
            s = df.format(pDate);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return s;
    }

    public static int mesEmInteiro(Date pDate) {
        SimpleDateFormat df = new SimpleDateFormat("MM");
        String s = "";
        try {
            s = df.format(pDate);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return Integer.parseInt(s);
    }

    public static int anoEmInteiro(Date pDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        String s = "";
        try {
            s = df.format(pDate);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return Integer.parseInt(s);
    }

    public static int diasUteisRestantes() {
        int dias = 0;

        Calendar calini = new GregorianCalendar();
        Calendar calfim = new GregorianCalendar();

        calini.setTime(new Date());
        calfim.setTime(ultimoDiaMes());

        while (calini.before(calfim) || calini.equals(calfim)) {
            if ((calini.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)
                    && (calini.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)) {
                dias += 1; // dias+= -1;
            }
            calini.add(Calendar.DAY_OF_MONTH, 1);
        }

        return dias;
    }

    public static int diasUteis() {
        int dias = 0;

        Calendar calini = new GregorianCalendar();
        Calendar calfim = new GregorianCalendar();

        calini.setTime(primeiroDiaMes());
        calfim.setTime(ultimoDiaMes());

        while (calini.before(calfim) || calini.equals(calfim)) {
            if ((calini.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)
                    && (calini.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)) {
                dias += 1; // dias+= -1;
            }
            calini.add(Calendar.DAY_OF_MONTH, 1);
        }

        return dias;
    }

    // - Pega a data de hoje
    public static String getDateToday(int dateFormat) {
        Date today = new Date();
        DateFormat formatter;
        try {
            formatter = DateFormat.getDateInstance(dateFormat, currentLocale);
            return formatter.format(today);
        } catch (NullPointerException np) {
            // np.printStackTrace();
        }
        return "o tipo " + dateFormat + " n�o � compativel com a fun��o!";
    }

    public static String hoje() {
        Date d = new Date();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String s = df.format(d);
        return s;
    }

    public static String hojeDataHoraMySQL() {
        Date d = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String s = df.format(d);
        return s;
    }

    public static String hojeDataMySQL() {
        Date d = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String s = df.format(d);
        return s;
    }

    public static String mesAnoString(Date data) {
        SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
        String s = df.format(data);
        return s;
    }

    public static String diaMesAnoString(Date data) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String s = df.format(data);
        return s;
    }

    public static String mesAnoStringBarra(Date data) {
        SimpleDateFormat df = new SimpleDateFormat("MM/yyyy");
        String s = df.format(data);
        return s;
    }

    public static String diaMesAnoStringBarra(Date data) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String s = df.format(data);
        return s;
    }

    public static String dateToStr(Date pDate) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String s = "  /  /    ";
        try {
            s = df.format(pDate);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return s;
    }

    public static String dateToStrHoraMinuto(Date pDate) {
        SimpleDateFormat df = new SimpleDateFormat("hh:mm");
        String s = "  :  ";
        try {
            s = df.format(pDate);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return s;
    }

    public static Date strTodate(String pDate) {

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        Date dia = null;
        try {

            dia = df.parse(pDate);
        } catch (ParseException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        return dia;
    }

    public static String horaMinutoAMPM(Date pDate) {
        SimpleDateFormat df = new SimpleDateFormat("H:mm");
        String s = "  :  ";
        try {
            s = df.format(pDate);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return s;
    }

    public static String dataForStringPadrao(Date pDate) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String s = "";
        try {
            s = df.format(pDate);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return s;
    }

    public static String dataHoraForStringPadrao(Date pDate) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String s = "";
        try {
            s = df.format(pDate);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return s;
    }

    public static String dataHoraForStringPadraoH(Date pDate) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy H:mm:ss");
        String s = "";
        try {
            s = df.format(pDate);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return s;
    }

    public static String dataHoraForStringMySQL(Date pDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String s = "";
        try {
            s = df.format(pDate);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return s;
    }

    public static String dataHoraForStringMySQLHoraDia(Date pDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
        String s = "";
        try {
            s = df.format(pDate);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return s;
    }

    public static String dataForStringMySQL(Date pDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String s = "";
        try {
            s = df.format(pDate);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return s;
    }

    public static Date retornaHojeData() {
        Date dt = new Date();
        return dt;
    }

    public static Date dataMySQLHoraDia(String pDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");

        Date dia = null;
        try {

            dia = df.parse(pDate);
        } catch (ParseException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return dia;
    }

    public static Date primeiroDiaMes() {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        int primeiroDia = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), primeiroDia);
        Date diaDate = cal.getTime();
        String diaString = df.format(diaDate);
        try {
            diaDate = df.parse(diaString);
        } catch (ParseException e) {            
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return diaDate;
    }

    public static Date ultimoDiaMes() {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        int ultimoDia = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), ultimoDia);
        Date diaDate = cal.getTime();
        String diaString = df.format(diaDate);
        try {
            diaDate = df.parse(diaString);
        } catch (ParseException e) {            
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return diaDate;
    }

    public static Date stringParaData(String data) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date dataUtil = df.parse(data);
        try {
            return dataUtil;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }

    public static Date stringParaDataHora(String data) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        java.util.Date dataUtil = df.parse(data);
        try {
            return dataUtil;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
    
    public static List<SelectItem> gerarUltimosMeses(){
        List<SelectItem> listaMeses = new ArrayList<>();
        Calendar cal = Calendar.getInstance();                        
        int mesAtual = cal.get(Calendar.MONTH);        
        int anoAtual = cal.get(Calendar.YEAR);        
        int tamanhoLista = 12;
        for(int i = 0; mesAtual-i >= 0; i++){                  
            cal.set(Calendar.MONTH, (mesAtual-i));
            String item = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("pt", "BR")) + " / " + anoAtual;
            listaMeses.add(new SelectItem(item, item));
            tamanhoLista--;
        }        
                
        for(int j = 11; tamanhoLista >= 0; j--){                
            cal.set(Calendar.MONTH, j);
            String item = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("pt", "BR")) + " / " + (anoAtual-1);
            listaMeses.add(new SelectItem(item, item));
            tamanhoLista--;
        }
        return listaMeses;     
    }
    
    public static Date dataAtualMenos30Dias(){
        Calendar cal = Calendar.getInstance();     
        cal.add(Calendar.DATE, -30);        
        return cal.getTime();
    }
    
    public static Date ultimoSemanaAno(int ano){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, ano);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.WEEK_OF_YEAR, 52);
        calendar.set(Calendar.DAY_OF_WEEK, 7);
        return calendar.getTime();
    }
}