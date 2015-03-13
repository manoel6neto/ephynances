package br.com.physisbrasil.web.ephynances.util;

import java.util.Random;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 *
 * @author Thomas
 */
public class Utils {

    public static boolean sendEmail(String toEmail, String toName, String message, String smtpServer,
            String sender, String subject, String user, String passwd, Integer smtpPort, String senderIdentify) throws EmailException {

        try {
            SimpleEmail simpleEmail = new SimpleEmail();
            simpleEmail.setHostName(smtpServer); // SMTP server
            simpleEmail.addTo(toEmail, toName); //to  email and name 
            simpleEmail.setFrom(sender, senderIdentify); // from  
            simpleEmail.setSubject(subject); //subject 
            simpleEmail.setMsg(message); //msg  
            simpleEmail.setAuthentication(user, passwd);  // user and passwd
            simpleEmail.setSmtpPort(smtpPort);  // SMTP port                
            simpleEmail.send();

            return true;
        } catch (EmailException e) {
            JsfUtil.addErrorMessage(e, "Falha na solicitação de envio do email. Consulte o Administrador.");
        }

        return false;
    } 
    
    public static String onlyDigits(String value, boolean dropLeftZeros) {

        String replaceAll = value.replaceAll("[^\\d]", "");

        if (dropLeftZeros) {
            while (replaceAll.length() > 0 && replaceAll.charAt(0) == '0') {
                replaceAll = replaceAll.substring(1);
            }
        }

        return replaceAll;

    }

    public static String randomPassword() {

        char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890/[]".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();

    }

    public static String[] decodeAuth(String auth) {
        //Replacing "Basic THE_BASE_64" to "THE_BASE_64" directly
        auth = auth.replaceFirst("[B|b]asic ", "");

        //Decode the Base64 into byte[]
        byte[] decodedBytes = DatatypeConverter.parseBase64Binary(auth);

        //If the decode fails in any case
        if (decodedBytes == null || decodedBytes.length == 0) {
            return null;
        }

        //Now we can convert the byte[] into a splitted array :
        //  - the first one is login,
        //  - the second one password
        return new String(decodedBytes).split(":", 2);
    }
}
