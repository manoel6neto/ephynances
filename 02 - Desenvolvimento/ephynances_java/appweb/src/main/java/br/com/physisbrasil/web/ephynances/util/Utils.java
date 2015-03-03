package br.com.physisbrasil.web.ephynances.util;

//import br.com.physisbrasil.web.kia.jsf.ConfigController;
import br.com.physisbrasil.web.ephynances.jsf.ConfigController;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 *
 * @author Thadeu
 */
public class Utils {

    public static boolean sendEmail(String toEmail, String toName, String message, String smtpServer,
            String sender, String subject, String user, String passwd, Integer smtpPort, String sourceIdentify) throws EmailException {

        try {
            SimpleEmail simpleEmail = new SimpleEmail();
            simpleEmail.setHostName(smtpServer); // SMTP server
            simpleEmail.addTo(toEmail, toName); //to  email and name 
            simpleEmail.setFrom(sender, sourceIdentify); // from  
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

    public static String getApnsEnvironment() {

        try {
            return ConfigController.properties.getProperty(ConfigController.PROP_APNS_ENVIROMENT);
        } catch (Exception ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String getApnsCertificateName() {
        try {
            if (getApnsEnvironment().equals("production")) {
                return ConfigController.properties.getProperty(ConfigController.PROP_APNS_CERT_PROD_NAME);
            } else {
                return ConfigController.properties.getProperty(ConfigController.PROP_APNS_CERT_DEV_NAME);
            }
        } catch (Exception ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String getApnsCertificatePassword() {

        try {
            if (getApnsEnvironment().equals("production")) {
                return ConfigController.properties.getProperty(ConfigController.PROP_APNS_CERT_PROD_PASS);
            } else {
                return ConfigController.properties.getProperty(ConfigController.PROP_APNS_CERT_DEV_PASS);
            }
        } catch (Exception ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
