/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ipsoftbrasil.web.agility.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thadeu
 */
public class Criptografia {

    private static final String chave = "agility";

    public static final String SENHA_PADRAO = "123456";

    /**
     * Utiliza padrao MD para criptografia
     *
     * @param s string a ser criptografada
     * @return s criptografado
     * @throws NoSuchAlgorithmException
     */
    public static String criptografar(String s) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(s.getBytes());
            md5.update(chave.getBytes());//personaliza a geracao da criptografia

            return toHex(md5.digest());
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Criptografia.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static String toHex(byte[] digest) {
        StringBuilder buf = new StringBuilder();
        for (Integer i = 0; i < digest.length; i++) {
            buf.append(Integer.toHexString(digest[i] & 0x00ff));
        }
        return buf.toString();
    }

    public static void main(String[] args) {
        System.out.println(Criptografia.criptografar("123456"));

    }
}
