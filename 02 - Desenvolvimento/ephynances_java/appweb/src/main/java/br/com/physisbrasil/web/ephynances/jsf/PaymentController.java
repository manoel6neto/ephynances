package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.PaymentBean;
import br.com.physisbrasil.web.ephynances.model.Payment;
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
public class PaymentController extends BaseController {

    @EJB
    private PaymentBean paymentBean;
    private List<Payment> payments;
    
    @PostConstruct
    public void init() {
        if (payments == null) {
            payments = new ArrayList<Payment>();
            setPayments(paymentBean.findAll());
        }
    }
    
    public void addInstallmentPayment(Long agreementInstallmentId) {
        
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }
}
