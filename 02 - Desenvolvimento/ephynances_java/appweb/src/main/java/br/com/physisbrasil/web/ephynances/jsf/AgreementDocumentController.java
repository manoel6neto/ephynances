package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.AgreementDocumentBean;
import br.com.physisbrasil.web.ephynances.model.AgreementDocument;
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
public class AgreementDocumentController extends BaseController {

    @EJB
    private AgreementDocumentBean agreementDocumentBean;
    private List<AgreementDocument> agreementDocuments;
    
    @PostConstruct
    public void init() {
        if (agreementDocuments == null) {
            agreementDocuments = new ArrayList<AgreementDocument>();
            setAgreementDocuments(agreementDocumentBean.findAll());
        }
    }

    public List<AgreementDocument> getAgreementDocuments() {
        return agreementDocuments;
    }

    public void setAgreementDocuments(List<AgreementDocument> agreementDocuments) {
        this.agreementDocuments = agreementDocuments;
    }
}
