package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.AgreementBean;
import br.com.physisbrasil.web.ephynances.ejb.AgreementInstallmentBean;
import br.com.physisbrasil.web.ephynances.ejb.PaymentBean;
import br.com.physisbrasil.web.ephynances.ejb.SubAgreementInstallmentBean;
import br.com.physisbrasil.web.ephynances.model.Agreement;
import br.com.physisbrasil.web.ephynances.model.AgreementInstallment;
import br.com.physisbrasil.web.ephynances.model.Payment;
import br.com.physisbrasil.web.ephynances.model.SubAgreementInstallment;
import br.com.physisbrasil.web.ephynances.util.JsfUtil;
import java.math.BigDecimal;
import java.util.Date;
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
    private Payment payment;

    @EJB
    private AgreementInstallmentBean agreementInstallmentBean;
    private AgreementInstallment agreementInstallment;

    @EJB
    private AgreementBean agreementBean;
    private Agreement agreement;
    
    @EJB
    private SubAgreementInstallmentBean subAgreementInstallmentBean;
    private SubAgreementInstallment subAgreementInstallment;

    @PostConstruct
    public void init() {

        if ((AgreementInstallment) getFlash("agreementInstallment") != null) {
            agreementInstallment = (AgreementInstallment) getFlash("agreementInstallment");
        } else {
            if (agreementInstallment == null) {
                agreementInstallment = new AgreementInstallment();
            }
        }

        if ((Payment) getFlash("payment") != null) {
            payment = (Payment) getFlash("payment");
        } else {
            if (payment == null) {
                payment = new Payment();
                payment.setTotalValue(new BigDecimal(0));
            }
        }

        if ((Agreement) getFlash("agreement") != null) {
            agreement = (Agreement) getFlash("agreement");
        } else {
            if (agreement == null) {
                agreement = new Agreement();
            }
        }
    }

    public String setPaymentStart(Long paymentId) {
        try {
            paymentBean.clearCache();
            payment = paymentBean.find(paymentId);
            if (payment != null) {

                putFlash("payment", payment);
                return "/payment/paymentDetails";
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao carregar dados do pagamento.");
        }

        return "/agreement/list";
    }

    public void addInstallmentPayment(Long agreementInstallmentId) {
        try {
            agreementInstallmentBean.clearCache();
            AgreementInstallment tempAgreementInstallment = agreementInstallmentBean.find(agreementInstallmentId);
            if (tempAgreementInstallment != null) {
                if (!tempAgreementInstallment.getStatus().equalsIgnoreCase(AgreementInstallment.getSTATUS_PAGO_SEM_CONFIRMACAO()) && !tempAgreementInstallment.getStatus().equalsIgnoreCase(AgreementInstallment.getSTATUS_PAGO_COM_CONFIRMACAO())) {
                    if (tempAgreementInstallment.getValue().compareTo(payment.getTotalValue()) == 0) {
                        //Pagamento do valor total da parcela sem excedente e sem residuo. Cria o pagamento e marca a parcela como paga
                        payment.setAgreementInstallment(tempAgreementInstallment);
                        payment.setPaymentDate(new Date(System.currentTimeMillis()));
                        paymentBean.create(payment);
                        paymentBean.clearCache();
                        
                        agreementInstallmentBean.clearCache();
                        tempAgreementInstallment = agreementInstallmentBean.find(tempAgreementInstallment.getId());
                        tempAgreementInstallment.setStatus(AgreementInstallment.getSTATUS_PAGO_SEM_CONFIRMACAO());
                        agreementInstallmentBean.edit(tempAgreementInstallment);
                        agreementInstallmentBean.clearCache();
                        
                        agreementBean.clearCache();
                        
                        JsfUtil.addSuccessMessage("Pagamento realizado com sucesso. Parcela totalmente paga.");
                    } else if (tempAgreementInstallment.getValue().compareTo(payment.getTotalValue()) == -1) {
                        //Pagamento acima do valor da parcela. Não pode aceitar.
                        JsfUtil.addErrorMessage("Não é possível realizar pagamento acima do valor da parcela.");
                    } else if (tempAgreementInstallment.getValue().compareTo(payment.getTotalValue()) == 1) {
                        //Pagamento abaixo do valor da parcela. Marcar a parcela como paga. E criar uma subparcela para a mesma com valor restante
                        payment.setAgreementInstallment(tempAgreementInstallment);
                        payment.setPaymentDate(new Date(System.currentTimeMillis()));
                        paymentBean.create(payment);
                        paymentBean.clearCache();

                        agreementInstallmentBean.clearCache();
                        tempAgreementInstallment = agreementInstallmentBean.find(tempAgreementInstallment.getId());
                        tempAgreementInstallment.setStatus(AgreementInstallment.getSTATUS_PAGO_SEM_CONFIRMACAO());
                        agreementInstallmentBean.edit(tempAgreementInstallment);
                        agreementInstallmentBean.clearCache();
                        
                        SubAgreementInstallment subAgreementInstallmentForPayment = new SubAgreementInstallment();
                        subAgreementInstallmentForPayment.setAgreementInstallment(tempAgreementInstallment);
                        subAgreementInstallmentForPayment.setPayment(payment);
                        subAgreementInstallmentForPayment.setValue(tempAgreementInstallment.getValue().subtract(payment.getTotalValue()));
                        subAgreementInstallmentBean.create(subAgreementInstallment);
                        subAgreementInstallmentBean.clearCache();
                        
                        JsfUtil.addSuccessMessage("Pagamento realizado com sucesso. Parcela com pagamento parcial.");
                        JsfUtil.addErrorMessage("Nova subparcela criada com o valor restante de R$ " + subAgreementInstallmentForPayment.getValue().toString());
                    }
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao adicionar pagamento.");
        }
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public AgreementInstallment getAgreementInstallment() {
        return agreementInstallment;
    }

    public void setAgreementInstallment(AgreementInstallment agreementInstallment) {
        this.agreementInstallment = agreementInstallment;
    }

    public Agreement getAgreement() {
        return agreement;
    }

    public void setAgreement(Agreement agreement) {
        this.agreement = agreement;
    }

    public SubAgreementInstallment getSubAgreementInstallment() {
        return subAgreementInstallment;
    }

    public void setSubAgreementInstallment(SubAgreementInstallment subAgreementInstallment) {
        this.subAgreementInstallment = subAgreementInstallment;
    }

}
