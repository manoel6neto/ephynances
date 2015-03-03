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
    
    @EJB
    private UserBean usuarioBean;
    
    public String login() {
        try {
            User user = usuarioBean.findByEmailSenha(email, Criptografia.criptografar(senha));
            
            if (user.getDeleteDate() == null) {
                if (user != null) {
                    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);                    
                    session.setAttribute(AbstractFilter.USER_KEY, user);
                    return "index";
                    
                } else {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/bundle").getString("login.invalid"));
                    return "login";
                    
                }
            } else {
                JsfUtil.addErrorMessage("Usuário inativo. Procure um administrador.");
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
    
    public User getLoggedUser() {
        return (User) JsfUtil.getSessionAttribute(AbstractFilter.USER_KEY);
    }
    
    public Boolean isUserInRuller(String ruller) {
        if (getLoggedUser().getProfileRule().equals(ruller)) {
            return true;
        } else {
            return false;
        }        
    }
            
}
