package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.CityOrganBean;
import br.com.physisbrasil.web.ephynances.model.CityOrgan;
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
}
