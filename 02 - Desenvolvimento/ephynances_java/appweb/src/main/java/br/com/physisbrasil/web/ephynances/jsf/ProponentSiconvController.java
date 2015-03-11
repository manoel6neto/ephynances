package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.ProponentSiconvBean;
import br.com.physisbrasil.web.ephynances.model.ProponentSiconv;
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
public class ProponentSiconvController extends BaseController {

    @EJB
    private ProponentSiconvBean proponentSiconvBean;
    private List<ProponentSiconv> proponentSiconvs;
    
    @PostConstruct
    public void init() {
        if (proponentSiconvs == null) {
            proponentSiconvs = new ArrayList<ProponentSiconv>();
            setProponentSiconvs(proponentSiconvBean.findAll());
        }
    }

    public List<ProponentSiconv> getProponentSiconvs() {
        return proponentSiconvs;
    }

    public void setProponentSiconvs(List<ProponentSiconv> proponentSiconvs) {
        this.proponentSiconvs = proponentSiconvs;
    }
}
