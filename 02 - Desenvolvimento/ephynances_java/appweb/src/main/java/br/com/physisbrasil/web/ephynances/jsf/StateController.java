package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.RegionBean;
import br.com.physisbrasil.web.ephynances.ejb.StateBean;
import br.com.physisbrasil.web.ephynances.model.Region;
import br.com.physisbrasil.web.ephynances.model.State;
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
public class StateController extends BaseController {

    @EJB
    private StateBean stateBean;
    private List<State> states;
    
    @EJB
    private RegionBean regionBean;

    @PostConstruct
    public void init() {
        if (states == null) {
            states = new ArrayList<State>();
            setStates(stateBean.findAll());
        }
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }
    
    public void filteredList(Region region) {
        if(region != null && region.getId() > 0) {
            setStates(regionBean.findByProperty("id", region.getId()).getStates());
        }
    }
}
