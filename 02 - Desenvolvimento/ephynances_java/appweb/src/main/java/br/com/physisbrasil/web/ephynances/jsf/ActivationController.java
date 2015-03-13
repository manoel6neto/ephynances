package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.ActivationBean;
import br.com.physisbrasil.web.ephynances.model.Activation;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

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

    @PostConstruct
    public void init() {
        if (activations == null) {
            activations = new ArrayList<Activation>();
            setActivations(activationBean.findAll());
        }
    }

    public List<Activation> getActivations() {
        return activations;
    }

    public void setActivations(List<Activation> activations) {
        this.activations = activations;
    }
    
    public void activeUserAndDropRegister() {
        //STUB utilizar no métdodo de validação
    }
}
