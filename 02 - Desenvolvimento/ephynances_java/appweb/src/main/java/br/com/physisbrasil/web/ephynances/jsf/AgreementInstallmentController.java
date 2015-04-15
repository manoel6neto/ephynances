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
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
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
    private AgreementInstallment agreementInstallment;
    
    @EJB
    private AgreementBean agreementBean;
    private Agreement agreement;
    
    @EJB
    private PaymentBean paymentBean;
    private Payment payment;
    
    @EJB
    private SubAgreementInstallmentBean subAgreementInstallmentBean;
    private SubAgreementInstallment subAgreementInstallment;
    
    @PostConstruct
    public void init() {
        
        if ((Agreement) getFlash("agreement") != null) {
            agreement = (Agreement) getFlash("agreement");
        } else {
            if (agreement == null) {
                agreement = new Agreement();
            }
        }
        
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
    }
    
    public String setAgreementInstallments(Long agreementId) {
        try {
            agreementBean.clearCache();
            agreement = agreementBean.find(agreementId);
            if (agreement != null) {
                
                putFlash("agreement", agreement);
                return "/agreementInstallment/installments";
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao carregar as parcelas para o contrato.");
        }
        
        return "/agreement/list";
    }
    
    public void removeInstallment(Long agreementInstallmentId) {
        try {
            agreementInstallmentBean.clearCache();
            AgreementInstallment tempAgreementInstallment = agreementInstallmentBean.find(agreementInstallmentId);
            if (agreement.getStatus().equalsIgnoreCase(Agreement.getSTATE_INCOMPLETO())) {
                if (tempAgreementInstallment != null) {
                    agreementInstallmentBean.remove(tempAgreementInstallment);
                    agreementInstallmentBean.clearCache();
                    agreementBean.clearCache();
                    agreement = agreementBean.find(agreement.getId());
                    
                    putFlash("agreement", agreement);
                    JsfUtil.addSuccessMessage("Parcela removida com sucesso!!");
                }
            } else {
                throw new Exception("Contrato com status inválido para alteração de parcelas.");
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao apagar a parcela solicitada.");
        }
    }
    
    public void addInstallmentPayment() {
        try {
            agreementInstallmentBean.clearCache();
            AgreementInstallment tempAgreementInstallment = agreementInstallmentBean.find(agreementInstallment.getId());
            if (tempAgreementInstallment != null) {
                if (!tempAgreementInstallment.getStatus().equalsIgnoreCase(AgreementInstallment.getSTATUS_PAGO_SEM_CONFIRMACAO()) && !tempAgreementInstallment.getStatus().equalsIgnoreCase(AgreementInstallment.getSTATUS_PAGO_COM_CONFIRMACAO())) {
                    if (tempAgreementInstallment.getValue().compareTo(payment.getTotalValue()) == 0) {
                        //Pagamento do valor total da parcela sem excedente e sem residuo. Cria o pagamento e marca a parcela como paga
                        payment.setAgreementInstallment(tempAgreementInstallment);
                        payment.setTotalValue(payment.getTotalValue());
                        payment.setPaymentDate(new Date(System.currentTimeMillis()));
                        paymentBean.create(payment);
                        paymentBean.clearCache();
                        
                        agreementInstallmentBean.clearCache();
                        tempAgreementInstallment = agreementInstallmentBean.find(tempAgreementInstallment.getId());
                        tempAgreementInstallment.setStatus(AgreementInstallment.getSTATUS_PAGO_SEM_CONFIRMACAO());
                        tempAgreementInstallment.setPayment(payment);
                        agreementInstallmentBean.edit(tempAgreementInstallment);
                        agreementInstallmentBean.clearCache();
                        
                        agreementBean.clearCache();
                        
                        agreement = agreementBean.find(tempAgreementInstallment.getAgreement().getId());
                        
                        JsfUtil.addSuccessMessage("Pagamento realizado com sucesso. Parcela totalmente paga.");
                    } else if (tempAgreementInstallment.getValue().compareTo(payment.getTotalValue()) == -1) {
                        //Pagamento acima do valor da parcela. Pagar as parcelas na ordem.
                        //Pagando a parcela informada primeiramente (mesmo que essa não esteja na ordem)
                        BigDecimal extra = payment.getTotalValue().subtract(tempAgreementInstallment.getValue());
                        
                        payment.setAgreementInstallment(tempAgreementInstallment);
                        payment.setPaymentDate(new Date(System.currentTimeMillis()));
                        payment.setTotalValue(tempAgreementInstallment.getValue());
                        paymentBean.create(payment);
                        paymentBean.clearCache();
                        agreementInstallmentBean.clearCache();
                        agreementBean.clearCache();
                        
                        tempAgreementInstallment = agreementInstallmentBean.find(tempAgreementInstallment.getId());
                        tempAgreementInstallment.setStatus(AgreementInstallment.getSTATUS_PAGO_SEM_CONFIRMACAO());
                        tempAgreementInstallment.setPayment(payment);
                        agreementInstallmentBean.edit(tempAgreementInstallment);
                        agreementInstallmentBean.clearCache();
                        
                        agreement = agreementBean.find(tempAgreementInstallment.getAgreement().getId());
                        List<AgreementInstallment> installmentsForAgreement = agreement.getAgreementInstallments();
                        Collections.sort(installmentsForAgreement);
                        
                        for (AgreementInstallment install : installmentsForAgreement) {
                            if (install.getPayment() == null) {
                                if (install.getValue().compareTo(extra) == 0) {
                                    //Valor restante corresponde ao valor da parcela
                                    payment = new Payment();
                                    payment.setAgreementInstallment(install);
                                    payment.setPaymentDate(new Date(System.currentTimeMillis()));
                                    payment.setTotalValue(extra);
                                    paymentBean.create(payment);
                                    paymentBean.clearCache();
                                    agreementInstallmentBean.clearCache();
                                    agreementBean.clearCache();
                                    
                                    install = agreementInstallmentBean.find(install.getId());
                                    install.setStatus(AgreementInstallment.getSTATUS_PAGO_SEM_CONFIRMACAO());
                                    install.setPayment(payment);
                                    agreementInstallmentBean.edit(install);
                                    agreementInstallmentBean.clearCache();
                                    
                                    agreement = agreementBean.find(install.getAgreement().getId());
                                    
                                    JsfUtil.addSuccessMessage("Pagamento realizado com sucesso. Mais de uma parcela contemplada.");
                                    break;
                                }
                                
                                if (install.getValue().compareTo(extra) == -1) {
                                    //Valor maior que a parcela
                                    payment = new Payment();
                                    payment.setAgreementInstallment(install);
                                    payment.setPaymentDate(new Date(System.currentTimeMillis()));
                                    payment.setTotalValue(install.getValue());
                                    paymentBean.create(payment);
                                    paymentBean.clearCache();
                                    agreementInstallmentBean.clearCache();
                                    agreementBean.clearCache();
                                    
                                    install = agreementInstallmentBean.find(install.getId());
                                    install.setStatus(AgreementInstallment.getSTATUS_PAGO_SEM_CONFIRMACAO());
                                    install.setPayment(payment);
                                    agreementInstallmentBean.edit(install);
                                    agreementInstallmentBean.clearCache();
                                    
                                    agreement = agreementBean.find(install.getAgreement().getId());
                                    
                                    extra = extra.subtract(install.getValue());
                                }
                                
                                if (install.getValue().compareTo(extra) == 1) {
                                    //Valor menor que a parcela
                                    payment = new Payment();
                                    payment.setAgreementInstallment(install);
                                    payment.setPaymentDate(new Date(System.currentTimeMillis()));
                                    payment.setTotalValue(extra);
                                    paymentBean.create(payment);
                                    paymentBean.clearCache();
                                    agreementInstallmentBean.clearCache();
                                    
                                    SubAgreementInstallment subAgreementInstallmentForPayment = new SubAgreementInstallment();
                                    subAgreementInstallmentForPayment.setAgreementInstallment(install);
                                    subAgreementInstallmentForPayment.setValue(install.getValue().subtract(extra));
                                    subAgreementInstallmentBean.create(subAgreementInstallmentForPayment);
                                    subAgreementInstallmentBean.clearCache();
                                    
                                    install = agreementInstallmentBean.find(install.getId());
                                    install.setStatus(AgreementInstallment.getSTATUS_PAGO_SEM_CONFIRMACAO());
                                    install.setPayment(payment);
                                    install.setSubAgreementInstallment(subAgreementInstallmentForPayment);
                                    agreementInstallmentBean.edit(install);
                                    agreementInstallmentBean.clearCache();
                                    
                                    agreementBean.clearCache();
                                    
                                    agreement = agreementBean.find(install.getAgreement().getId());
                                    
                                    JsfUtil.addSuccessMessage("Pagamento realizado com sucesso. Mais de uma parcela contemplada.");
                                    JsfUtil.addErrorMessage("Nova subparcela gerada no valor de R$ " + subAgreementInstallmentForPayment.getValue());
                                    break;
                                }
                            }
                        }
                    } else if (tempAgreementInstallment.getValue().compareTo(payment.getTotalValue()) == 1) {
                        //Pagamento abaixo do valor da parcela. Marcar a parcela como paga. E criar uma subparcela para a mesma com valor restante
                        payment.setAgreementInstallment(tempAgreementInstallment);
                        payment.setTotalValue(payment.getTotalValue());
                        payment.setPaymentDate(new Date(System.currentTimeMillis()));
                        paymentBean.create(payment);
                        paymentBean.clearCache();
                        
                        SubAgreementInstallment subAgreementInstallmentForPayment = new SubAgreementInstallment();
                        subAgreementInstallmentForPayment.setAgreementInstallment(tempAgreementInstallment);
                        subAgreementInstallmentForPayment.setValue(tempAgreementInstallment.getValue().subtract(payment.getTotalValue()));
                        subAgreementInstallmentBean.create(subAgreementInstallmentForPayment);
                        subAgreementInstallmentBean.clearCache();
                        
                        agreementInstallmentBean.clearCache();
                        tempAgreementInstallment = agreementInstallmentBean.find(tempAgreementInstallment.getId());
                        tempAgreementInstallment.setStatus(AgreementInstallment.getSTATUS_PAGO_SEM_CONFIRMACAO());
                        tempAgreementInstallment.setPayment(payment);
                        tempAgreementInstallment.setSubAgreementInstallment(subAgreementInstallmentForPayment);
                        agreementInstallmentBean.edit(tempAgreementInstallment);
                        agreementInstallmentBean.clearCache();
                        
                        agreementBean.clearCache();
                        
                        agreement = agreementBean.find(tempAgreementInstallment.getAgreement().getId());
                        
                        JsfUtil.addSuccessMessage("Pagamento realizado com sucesso. Parcela com pagamento parcial.");
                        JsfUtil.addErrorMessage("Nova subparcela criada com o valor restante de R$ " + subAgreementInstallmentForPayment.getValue().toString());
                    }
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao adicionar pagamento.");
        }
    }
    
    public void confirmPayment(Long agreementInstallmentId) {
        try {
            agreementInstallmentBean.clearCache();
            AgreementInstallment tempAgreementInstallment = agreementInstallmentBean.find(agreementInstallmentId);
            if (tempAgreementInstallment != null) {
                if (tempAgreementInstallment.getPayment() != null) {
                    if (tempAgreementInstallment.getStatus().equalsIgnoreCase(AgreementInstallment.getSTATUS_PAGO_SEM_CONFIRMACAO())) {
                        Payment tempPayment = paymentBean.find(tempAgreementInstallment.getPayment().getId());
                        tempPayment.setConfirmationDate(new Date(System.currentTimeMillis()));
                        paymentBean.edit(tempPayment);
                        paymentBean.clearCache();

                        //Reload and set status
                        agreementInstallmentBean.clearCache();
                        tempAgreementInstallment = agreementInstallmentBean.find(tempAgreementInstallment.getId());
                        tempAgreementInstallment.setStatus(AgreementInstallment.getSTATUS_PAGO_COM_CONFIRMACAO());
                        agreementInstallmentBean.edit(tempAgreementInstallment);
                        agreementInstallmentBean.clearCache();
                        
                        agreementBean.clearCache();
                        
                        agreement = agreementBean.find(tempAgreementInstallment.getAgreement().getId());
                        
                        JsfUtil.addSuccessMessage("Pagamento confirmado com sucesso!!");
                    } else {
                        JsfUtil.addErrorMessage("Status do pagamento não pode ser confirmado.");
                    }
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao mudar o status do pagamento!!");
        }
    }

    //Retorna o valor restante para parcelar
    public BigDecimal getMissingValue() {
        try {
            if (agreement != null) {
                if (agreement.getAgreementInstallments() != null) {
                    if (agreement.getAgreementInstallments().size() > 0) {
                        BigDecimal installmentsTotal = new BigDecimal(0);
                        for (AgreementInstallment installment : agreement.getAgreementInstallments()) {
                            installmentsTotal = installmentsTotal.add(installment.getValue());
                        }
                        return agreement.getTotalPrice().subtract(installmentsTotal);
                    }
                }
                return agreement.getTotalPrice();
            }
            JsfUtil.addErrorMessage("Contrato não encontrado.");
            return new BigDecimal(0);
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao consultar valor disponível para o contrato");
            return new BigDecimal(0);
        }
    }

    //Adiciona nova parcela
    public void addInstallment() {
        try {
            if (agreement != null) {
                if (agreementInstallment.getValue().compareTo(getMissingValue()) == 0 || agreementInstallment.getValue().compareTo(getMissingValue()) == -1) {
                    agreementInstallment.setValue(agreementInstallment.getValue());
                    agreementInstallment.setStatus(AgreementInstallment.getSTATUS_PENDENTE());
                    agreementInstallment.setAgreement(agreement);
                    agreementInstallmentBean.create(agreementInstallment);
                    agreementInstallmentBean.clearCache();
                    
                    agreementBean.clearCache();
                    agreement = agreementBean.find(agreement.getId());
                    
                    agreementInstallment = new AgreementInstallment();
                    
                    JsfUtil.addSuccessMessage("Parcela adicionada com sucesso !!");
                } else {
                    JsfUtil.addErrorMessage("Valor superior ao disponível para o contrato.");
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao cadastrar nova parcela.");
        }
    }
    
    public String checkStatusInstallmentAndAgreement(Long agreementInstallmentId) {
        try {
            AgreementInstallment tempAgreementInstallment = agreementInstallmentBean.find(agreementInstallmentId);
            if (tempAgreementInstallment != null) {
                if (!tempAgreementInstallment.getStatus().equalsIgnoreCase(AgreementInstallment.getSTATUS_PAGO_SEM_CONFIRMACAO()) && !tempAgreementInstallment.getStatus().equalsIgnoreCase(AgreementInstallment.getSTATUS_PAGO_COM_CONFIRMACAO())) {
                    if (!tempAgreementInstallment.getAgreement().getStatus().equalsIgnoreCase(Agreement.getSTATE_INCOMPLETO())) {
                        return String.valueOf(true);
                    }
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao consultar parcela.");
        }
        
        return String.valueOf(false);
    }
    
    public String chekcIsPaid(Long agreementInstallmentId) {
        try {
            AgreementInstallment installment = agreementInstallmentBean.find(agreementInstallmentId);
            if (installment != null) {
                if (installment.getPayment() != null) {
                    return String.valueOf(true);
                }
            }
        } catch (Exception e) {
            return String.valueOf(false);
        }
        return String.valueOf(false);
    }
    
    public String hasSubAgreementInstallments(Long agreementInstallmentId) {
        try {
            AgreementInstallment installment = agreementInstallmentBean.find(agreementInstallmentId);
            if (installment != null) {
                if (installment.getSubAgreementInstallment() != null) {
                    return String.valueOf(true);
                }
            }
        } catch (Exception e) {
            return String.valueOf(false);
        }
        return String.valueOf(false);
    }
    
    public String checkMinDate() {
        DateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyyy");
        return outputFormatter.format(new Date(System.currentTimeMillis()));
    }
    
    public String checkMaxDate() {
        if (agreement != null) {
            DateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyyy");
            return outputFormatter.format(agreement.getExpireDate());
        } else {
            DateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyyy");
            return outputFormatter.format(new Date(System.currentTimeMillis()));
        }
    }
    
    public String formatDate(Date dateToFormat) {
        DateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyyy");
        return outputFormatter.format(dateToFormat);
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
    
    public Payment getPayment() {
        return payment;
    }
    
    public void setPayment(Payment payment) {
        this.payment = payment;
    }
    
    public SubAgreementInstallment getSubAgreementInstallment() {
        return subAgreementInstallment;
    }
    
    public void setSubAgreementInstallment(SubAgreementInstallment subAgreementInstallment) {
        this.subAgreementInstallment = subAgreementInstallment;
    }
    
}
