package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.PaymentDocumentBean;
import br.com.physisbrasil.web.ephynances.model.PaymentDocument;
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
public class PaymentDocumentController extends BaseController {

    @EJB
    private PaymentDocumentBean paymentDocumentBean;
    private List<PaymentDocument> paymentDocuments;
    
    @PostConstruct
    public void init() {
        if (paymentDocuments == null) {
            paymentDocuments = new ArrayList<PaymentDocument>();
            setPaymentDocuments(paymentDocumentBean.findAll());
        }
    }

    public List<PaymentDocument> getPaymentDocuments() {
        return paymentDocuments;
    }

    public void setPaymentDocuments(List<PaymentDocument> paymentDocuments) {
        this.paymentDocuments = paymentDocuments;
    }
}
