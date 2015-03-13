package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.UserBean;
import br.com.physisbrasil.web.ephynances.model.User;
import br.com.physisbrasil.web.ephynances.servlet.AbstractFilter;
import br.com.physisbrasil.web.ephynances.util.Criptografia;
import br.com.physisbrasil.web.ephynances.util.JsfUtil;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Thomas
 */
@ManagedBean
@ViewScoped
public class LoginController implements Serializable {

    private String email;
    private String senha;
    private String profileRule;
    private User user;

    @EJB
    private UserBean usuarioBean;

    public String login() {
        try {
            if(email.contains("@")) {
                user = usuarioBean.findByEmailSenhaProfile(email, Criptografia.criptografar(senha), profileRule);
            } else {
                if (email.length() != 11) {
                    JsfUtil.addErrorMessage("Informe um cpf ou email válido. Utilize apenas números para o cpf!");
                    return "login";
                }
                email = new StringBuilder(email).insert(3, ".").toString();
                email = new StringBuilder(email).insert(7, ".").toString();
                email = new StringBuilder(email).insert(11, "-").toString();
                user = usuarioBean.findByCpfSenhaProfile(email, Criptografia.criptografar(senha), profileRule);
            }

            if (user.isIsVerified()) {
                HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                session.setAttribute(AbstractFilter.USER_KEY, user);
                return "index";
            } else {
                JsfUtil.addErrorMessage("Usuário não ativado. Procure um administrador.");
                return "login";
            }

        } catch (Exception e) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/bundle").getString("login.invalid"));
            return "login";
        }
    }

    public String logout() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        session.setAttribute(AbstractFilter.USER_KEY, null);
        session.invalidate();

        return "/login?faces-redirect=true";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getProfileRule() {
        return profileRule;
    }

    public void setProfileRule(String profileRule) {
        this.profileRule = profileRule;
    }
    
    public User getLoggedUser() {
        return (User) JsfUtil.getSessionAttribute(AbstractFilter.USER_KEY);
    }

    public Boolean isUserInRuller(String ruller) {
        return getLoggedUser().getProfileRule().equals(ruller);
    }

}
