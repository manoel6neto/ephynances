package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.AgreementResponsibleBean;
import br.com.physisbrasil.web.ephynances.model.AgreementResponsible;
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
public class AgreementResponsibleController extends BaseController {

    @EJB
    private AgreementResponsibleBean agreementResponsibleBean;
    private List<AgreementResponsible> agreementResponsibles;
    
    @PostConstruct
    public void init() {
        if (agreementResponsibles == null) {
            agreementResponsibles = new ArrayList<AgreementResponsible>();
            setAgreementResponsibles(agreementResponsibleBean.findAll());
        }
    }

    public List<AgreementResponsible> getAgreementResponsibles() {
        return agreementResponsibles;
    }

    public void setAgreementResponsibles(List<AgreementResponsible> agreementResponsibles) {
        this.agreementResponsibles = agreementResponsibles;
    }
}
