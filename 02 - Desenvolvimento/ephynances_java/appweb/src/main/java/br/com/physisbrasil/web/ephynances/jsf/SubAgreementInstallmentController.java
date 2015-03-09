package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.SubAgreementInstallmentBean;
import br.com.physisbrasil.web.ephynances.model.SubAgreementInstallment;
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
public class SubAgreementInstallmentController extends BaseController {

    @EJB
    private SubAgreementInstallmentBean subAgreementInstallmentBean;
    private List<SubAgreementInstallment> subAgreementInstallments;
    
    @PostConstruct
    public void init() {
        if (subAgreementInstallments == null) {
            subAgreementInstallments = new ArrayList<SubAgreementInstallment>();
            setSubAgreementInstallments(subAgreementInstallmentBean.findAll());
        }
    }

    public List<SubAgreementInstallment> getSubAgreementInstallments() {
        return subAgreementInstallments;
    }

    public void setSubAgreementInstallments(List<SubAgreementInstallment> subAgreementInstallments) {
        this.subAgreementInstallments = subAgreementInstallments;
    }
}
