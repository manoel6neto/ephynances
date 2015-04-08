package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.AgreementBean;
import br.com.physisbrasil.web.ephynances.ejb.AgreementInstallmentBean;
import br.com.physisbrasil.web.ephynances.model.Agreement;
import br.com.physisbrasil.web.ephynances.model.AgreementInstallment;
import br.com.physisbrasil.web.ephynances.util.JsfUtil;
import java.math.BigDecimal;
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
                    agreementInstallment.setStatus(AgreementInstallment.getSTATUS_PENDENTE());
                    agreementInstallmentBean.create(agreementInstallment);
                    agreementInstallmentBean.clearCache();

                    agreementBean.clearCache();
                    agreement = agreementBean.find(agreement.getId());

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
                if (!tempAgreementInstallment.getStatus().equalsIgnoreCase(AgreementInstallment.getSTATUS_PAGO())) {
                    if (tempAgreementInstallment.getAgreement().getStatus().equalsIgnoreCase(Agreement.getSTATE_INCOMPLETO())) {
                        return String.valueOf(true);
                    }
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao consultar parcela.");
        }

        return String.valueOf(false);
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

}
