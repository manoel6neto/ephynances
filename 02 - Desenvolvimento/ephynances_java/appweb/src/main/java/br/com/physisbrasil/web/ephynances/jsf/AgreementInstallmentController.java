package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.AgreementInstallmentBean;
import br.com.physisbrasil.web.ephynances.model.AgreementInstallment;
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
public class AgreementInstallmentController extends BaseController {

    @EJB
    private AgreementInstallmentBean agreementInstallmentBean;
    private List<AgreementInstallment> agreementInstallments;
    
    @PostConstruct
    public void init() {
        if (agreementInstallments == null) {
            agreementInstallments = new ArrayList<AgreementInstallment>();
            setAgreementInstallments(agreementInstallmentBean.findAll());
        }
    }

    public List<AgreementInstallment> getAgreementInstallments() {
        return agreementInstallments;
    }

    public void setAgreementInstallments(List<AgreementInstallment> agreementInstallments) {
        this.agreementInstallments = agreementInstallments;
    }
}
