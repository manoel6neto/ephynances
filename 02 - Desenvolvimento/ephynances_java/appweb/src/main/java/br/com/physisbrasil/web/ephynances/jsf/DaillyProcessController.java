package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.AgreementBean;
import br.com.physisbrasil.web.ephynances.ejb.ConfigurationBean;
import br.com.physisbrasil.web.ephynances.ejb.UserBean;
import br.com.physisbrasil.web.ephynances.model.Agreement;
import br.com.physisbrasil.web.ephynances.model.Configuration;
import br.com.physisbrasil.web.ephynances.model.User;
import br.com.physisbrasil.web.ephynances.util.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.mail.MessagingException;

/**
 *
 * @author Thomas
 */
@ManagedBean
@ViewScoped
public class DaillyProcessController extends BaseController {

    @EJB
    private AgreementBean agreementBean;
    private List<Agreement> agreements;

    @EJB
    private ConfigurationBean configurationBean;
    private Configuration configuration;

    @EJB
    private UserBean userBean;
    private List<User> users;

    @PostConstruct
    public void init() {

        if ((List<Agreement>) getFlash("agreements") != null) {
            agreements = (List<Agreement>) getFlash("agreements");
        } else {
            if (agreements == null) {
                agreements = new ArrayList<Agreement>();
            }
        }

        if ((List<User>) getFlash("users") != null) {
            users = (List<User>) getFlash("users");
        } else {
            if (users == null) {
                users = userBean.findAll();
            }
        }

        if ((Configuration) getFlash("configuration") != null) {
            configuration = (Configuration) getFlash("configuration");
        } else {
            if (configuration == null) {
                configuration = configurationBean.findAll().get(0);
            }
        }
    }

    public void checkStatusPaymentsAndSubPayments() {
        try {
            if (agreements != null) {
                if (agreements.size() > 0) {
                    for (Agreement contract : agreements) {
                        // Magic
                    }
                }
            }
        } catch (Exception e) {
            try {
                Utils.sendEmail("manoel.carvalho.neto@gmail.com", "Manoel Carvalho Neto", "Erro ao verificar contratos: " + e.getMessage(), configuration.getSmtpServer(), configuration.getEmail(), "Função automática ephynances",
                        configuration.getUserName(), configuration.getPassword(), configuration.getSmtpPort(), configuration.getEmail());
            } catch (MessagingException ex) {
                Logger.getLogger(DaillyProcessController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void sendEmailToAdmins(String message) {
        for (User u : users) {
            if (u.getProfileRule().equalsIgnoreCase(User.getRULER_ADMIN())) {
                try {
                    Utils.sendEmail(u.getEmail(), u.getName(), message, configuration.getSmtpServer(), configuration.getEmail(), "Função automática ephynances",
                            configuration.getUserName(), configuration.getPassword(), configuration.getSmtpPort(), configuration.getEmail());
                } catch (MessagingException ex) {
                    Logger.getLogger(DaillyProcessController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public List<Agreement> getAgreements() {
        return agreements;
    }

    public void setAgreements(List<Agreement> agreements) {
        this.agreements = agreements;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
