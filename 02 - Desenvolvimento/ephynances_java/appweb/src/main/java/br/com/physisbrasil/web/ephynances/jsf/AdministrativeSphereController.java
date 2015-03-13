package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.AdministrativeSphereBean;
import br.com.physisbrasil.web.ephynances.model.AdministrativeSphere;
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
public class AdministrativeSphereController extends BaseController {

    @EJB
    private AdministrativeSphereBean administrativeSphereBean;
    private List<AdministrativeSphere> administrativeSpheres;

    @PostConstruct
    public void init() {
        if (administrativeSpheres == null) {
            administrativeSpheres = new ArrayList<AdministrativeSphere>();
            setAdministrativeSpheres(administrativeSphereBean.findAll());
        }
    }

    public List<AdministrativeSphere> getAdministrativeSpheres() {
        return administrativeSpheres;
    }

    public void setAdministrativeSpheres(List<AdministrativeSphere> administrativeSpheres) {
        this.administrativeSpheres = administrativeSpheres;
    }
}
