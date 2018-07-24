package com.util;

import com.bean.RelatorioBean;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MD5 {

    public static String cripto(String dados) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(dados.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            String senhaCriptografada = hash.toString(16);
            return senhaCriptografada;
        } catch (NoSuchAlgorithmException ns) {
            Logger.getLogger(RelatorioBean.class.getName()).log(Level.SEVERE, ns.getMessage(), ns);
            return null;
        }
    }
}