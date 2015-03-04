package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.RegionBean;
import br.com.physisbrasil.web.ephynances.model.Region;
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
public class RegionController extends BaseController {

    @EJB
    private RegionBean regionBean;
    private List<Region> regions;

    @PostConstruct
    public void init() {
        if (regions == null) {
            regions = new ArrayList<Region>();
            regionBean = new RegionBean();
            setRegions(regionBean.findAll());
        }
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }
}
