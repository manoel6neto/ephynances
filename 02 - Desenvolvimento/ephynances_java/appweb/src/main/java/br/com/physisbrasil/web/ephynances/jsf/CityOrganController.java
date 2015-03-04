package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.CityOrganBean;
import br.com.physisbrasil.web.ephynances.ejb.StateBean;
import br.com.physisbrasil.web.ephynances.model.CityOrgan;
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
public class CityOrganController extends BaseController {

    @EJB
    private CityOrganBean cityOrganBean;
    private List<CityOrgan> cityOrgans;
    
    @EJB
    private StateBean stateBean;

    @PostConstruct
    public void init() {
        if (cityOrgans == null) {
            cityOrgans = new ArrayList<CityOrgan>();
            cityOrganBean = new CityOrganBean();
            setCityOrgans(cityOrganBean.findAll());
        }
    }

    public List<CityOrgan> getCityOrgans() {
        return cityOrgans;
    }

    public void setCityOrgans(List<CityOrgan> cityOrgans) {
        this.cityOrgans = cityOrgans;
    }
    
    public void filteredList(State state) {
        if(state != null && state.getId() > 0) {
            stateBean = new StateBean();
            setCityOrgans(stateBean.findByProperty("id", state.getId()).getCityOrgans());
        }
    }
}
