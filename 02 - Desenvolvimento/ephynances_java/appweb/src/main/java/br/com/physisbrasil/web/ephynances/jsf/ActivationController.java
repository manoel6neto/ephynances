package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.ActivationBean;
import br.com.physisbrasil.web.ephynances.ejb.UserBean;
import br.com.physisbrasil.web.ephynances.model.Activation;
import br.com.physisbrasil.web.ephynances.model.User;
import br.com.physisbrasil.web.ephynances.util.Criptografia;
import br.com.physisbrasil.web.ephynances.util.JsfUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Thomas
 */
@ManagedBean
@ViewScoped
public class ActivationController extends BaseController {

    @EJB
    private ActivationBean activationBean;
    private List<Activation> activations;
    private Activation activation;

    @EJB
    private UserBean userBean;

    String password;

    @PostConstruct
    public void init() {
        if (activations == null) {
            activations = new ArrayList<Activation>();
            setActivations(activationBean.findAll());
        }

        if (password == null) {
            password = new String();
        }
        
        activation = (Activation) getFlash("activation");
        if (activation == null) {
            activation = new Activation();
        }
        
        putFlash("activation", null);
    }

    public List<Activation> getActivations() {
        return activations;
    }

    public void setActivations(List<Activation> activations) {
        this.activations = activations;
    }

    public Activation getActivation() {
        return activation;
    }

    public void setActivation(Activation activation) {
        this.activation = activation;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String active() {
        try {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            String token = params.get("token");

            if (token != null && !token.equals("")) {
                activation = activationBean.findByProperty("token", token);

                putFlash("activation", activation);

                return "active";
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Falha ao carregar os dados para ativação. Entre em contato com o administrador.");
        }
        
        return "/login?faces-redirect=true";
    }

    public String activeUserAndDropRegister() {
        try {
            User user = activation.getUser();
            user.setPassword(Criptografia.criptografar(password));
            user.setIsVerified(true);
            userBean.edit(user);

            activationBean.remove(activation);

            userBean.clearCache();
            activationBean.clearCache();
            JsfUtil.addSuccessMessage("Usuário ativado com sucesso !!");

            return "login";
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Falha ao ativar o usuário. Entre em contato com o administrador.");
        }

        return "/login?faces-redirect=true";
    }
}
