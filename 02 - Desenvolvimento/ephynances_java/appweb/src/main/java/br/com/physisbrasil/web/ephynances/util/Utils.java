package br.com.physisbrasil.web.ephynances.util;

import br.com.physisbrasil.web.ephynances.model.User;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import javax.mail.Message;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.internet.MimeMessage;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Thomas
 */
public class Utils {

    public static boolean sendEmail(String toEmail, String toName, String message, String smtpServer,
            String sender, String subject, String user, String passwd, Integer smtpPort, String senderIdentify) throws MessagingException {

        java.util.Properties props = new java.util.Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", smtpServer);
        props.put("mail.smtp.user", sender);
        //props.put("mail.smtp.user", "Physis Brasil");
        props.put("mail.smtp.password", passwd);
        props.put("mail.smtp.port", smtpPort.toString());
        props.put("mail.debug", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", smtpServer);

        final String userName = (String) props.get("mail.smtp.user");
        final String password = (String) props.get("mail.smtp.password");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        });

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(user, "Physis Brasil"));
            msg.setRecipients(Message.RecipientType.TO, toEmail);
            msg.setSubject(subject);
            msg.setText(message, "utf-8", "html");

            // Send the message.
            Transport.send(msg);

            return true;
        } catch (MessagingException e) {
            JsfUtil.addErrorMessage(e, "Falha na solicitação de envio do email. Consulte o Administrador.");
        } catch (UnsupportedEncodingException e) {
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

    public static String generateToken(User user) {
        String token = Criptografia.criptografar(user.getId() + user.getEmail());

        return token;
    }
}
