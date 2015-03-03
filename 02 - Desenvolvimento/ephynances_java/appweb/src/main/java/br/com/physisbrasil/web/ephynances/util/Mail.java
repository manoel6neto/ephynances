/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.physisbrasil.web.ephynances.util;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Thadeu
 */
public class Mail {

    private static Mail mail;

    private Mail() {
    }

    public static Mail getInstance() {
        if (mail == null) {
            mail = new Mail();
        }
        return mail;
    }

    public boolean sendMail(String fromName, String to, String subject, String message) {

        try {
            Properties props = new Properties();
            props.load(getClass().getResourceAsStream("/mail.properties"));

            final String user = (String) props.get("mail.smtp.user");
            final String password = (String) props.get("mail.smtp.password");

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, password);
                }
            });

            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(user, fromName));
            msg.setRecipients(Message.RecipientType.TO, to);
            msg.setSubject(subject);
            msg.setText(message);

            Transport.send(msg);
            return true;

        } catch (Exception e) {

            Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }

    }
}
